/**
 * BBD Service Inc
 * AllRight Reserved @2017
 */
package com.bbd.util;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.Map;

/**
 * http请求
 * 
 * @author xc
 * @version $Id: HttpUtil.java, v 0.1 2016年12月5日 下午1:28:30 xc Exp $
 */
public class HttpUtil {

    /** logger */
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /***
     * 高并发http连接管理器。
     */
    private static MultiThreadedHttpConnectionManager connectionManager;

    /** 高并发client */
    private static HttpClient                         client;

    static {
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setConnectionTimeout(30000);
        params.setSoTimeout(15000);
        params.setMaxTotalConnections(100);
        //　检查连接是否可用
        params.setBooleanParameter(HttpConnectionManagerParams.STALE_CONNECTION_CHECK, false);
        connectionManager.setParams(params);
        client = new HttpClient(connectionManager);
        client.getParams().setContentCharset("utf-8");
        client.setConnectionTimeout(30000);
        client.setTimeout(15000);
    }

    public static <T> T getHttp(String url, Map<String, String> params, Class<T> clazz) {
        String result = getHttp(url, params);
        return (T) JsonUtil.parseObject(result, clazz);
    }

    /**
     * get方式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String getHttp(String url, Map<String, String> params) {
        StringBuffer responseMsg = new StringBuffer();

        // 1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient(connectionManager);

        // 用于测试的http接口的url  
        StringBuffer buffer = new StringBuffer("?");
        for (String key : params.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(params.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            buffer.append(key).append("=").append(value).append("&");
        }
        String paramStr = buffer.toString();

        String urlParam = url + paramStr.substring(0, paramStr.length() - 1);
        // 2.创建GetMethod的实例
        GetMethod getMethod = new GetMethod(urlParam);
        getMethod.setRequestHeader("Accept", "application/json;charset=UTF-8");
        getMethod.setRequestHeader(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
        // 使用系统系统的默认的恢复策略  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

        for (String key : params.keySet()) {
            getMethod.getParams().setParameter(key, params.get(key));
        }

        try {
            //3.执行getMethod,调用http接口  
            httpClient.executeMethod(getMethod);

            //4.读取内容  
            InputStream in = getMethod.getResponseBodyAsStream();

            //5.处理返回的内容  
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str = "";
            while ( (str = br.readLine()) != null ) {
                responseMsg.append(str);
            }

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //6.释放连接  
            getMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg.toString();
    }

    /**
     * get方式
     * 
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String getHttp(String url, Map<String, String> headers, Map<String, String> params) {
        String responseMsg = "";

        // 1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        // 用于测试的http接口的url  
        StringBuffer buffer = new StringBuffer("?");
        for (String key : params.keySet()) {
            buffer.append(key).append("=").append(params.get(key)).append("&");
        }
        String paramStr = buffer.toString();
        String urlParam = url + paramStr.substring(0, paramStr.length() - 1);
        // 2.创建GetMethod的实例  
        GetMethod getMethod = new GetMethod(urlParam);

        // 使用系统的默认的恢复策略  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        for (String key : headers.keySet()) {
            getMethod.addRequestHeader(key, headers.get(key));
        }

        for (String key : params.keySet()) {
            getMethod.getParams().setParameter(key, params.get(key));
        }

        try {
            //3.执行getMethod,调用http接口  
            httpClient.executeMethod(getMethod);

            //4.读取内容  
            byte[] responseBody = getMethod.getResponseBody();

            //5.处理返回的内容  
            responseMsg = new String(responseBody);

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //6.释放连接  
            getMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    /**
     * post方式
     * 
     * @param url
     * @param params
     * @return
     */
    public static String postHttp(String url, Map<String, Object> params) {
        String responseMsg = "";

        //1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");

        //2.构造PostMethod的实例  
        PostMethod postMethod = new PostMethod(url);

        //3.把参数值放入到PostMethod对象中  
        for (String key : params.keySet()) {
            postMethod.addParameter(key, String.valueOf(params.get(key)));
            System.out.println("key= " + key + " and value= " + params.get(key));
        }

        try {
            // 4.执行postMethod,调用http接口  
            httpClient.executeMethod(postMethod);//200  

            //5.读取内容  
            responseMsg = postMethod.getResponseBodyAsString().trim();

            //6.处理返回的内容  

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //7.释放连接  
            postMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    /**
     * post方式
     * 
     * @param url
     * @param params
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T postHttp(String url, Map<String, Object> params, Class<T> clz) {
        T t = null;
        PostMethod postMethod = null;
        try {
            //2.构造PostMethod的实例  
            postMethod = new PostMethod(url);

            //3.把参数值放入到PostMethod对象中  
            for (String key : params.keySet()) {
                postMethod.addParameter(key, String.valueOf(params.get(key)));
                logger.debug("key= " + key + " and value= " + params.get(key));
            }
            // 4.执行postMethod,调用http接口  
            int code = client.executeMethod(postMethod);
            if (code == 200) {
                //5.读取内容  
                //6.处理返回的内容  
                t = (T) JsonUtil.parseObject(postMethod.getResponseBodyAsString().trim(), clz);
            } else {
                logger.error("请求接口状态错误,状态码:{}.接口结果原始信息:{}.", code, postMethod.getResponseBodyAsString());
            }
        } catch (Exception e) {
            logger.error("请求出错：", e);
        } finally {
            //7.释放连接  
            if (postMethod != null)
                postMethod.releaseConnection();
        }
        return t;
    }

    /**
     * post方式
     * 
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postHttp(String url, Map<String, Object> headers, Map<String, Object> params) {
        String responseMsg = "";

        //1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");

        //2.构造PostMethod的实例  
        PostMethod postMethod = new PostMethod(url);

        for (String key : headers.keySet()) {
            postMethod.addRequestHeader(key, String.valueOf(headers.get(key)));
        }
        //3.把参数值放入到PostMethod对象中  
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value instanceof Object[]) {
                Object[] array = (Object[]) value;
                for (int i = 0; i < array.length; i++) {
                    postMethod.addParameter(key, String.valueOf(array[i]));
                }
            } else {
                postMethod.addParameter(key, String.valueOf(params.get(key)));
            }
            System.out.println("key= " + key + " and value= " + params.get(key));
        }

        try {
            // 4.执行postMethod,调用http接口  
            httpClient.executeMethod(postMethod);//200  

            //5.读取内容  
            responseMsg = postMethod.getResponseBodyAsString().trim();

            //6.处理返回的内容  

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //7.释放连接  
            postMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    /**
     * postbody方式
     * 
     * @param url
     * @param param
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String postBody(String url, String param) {
        String responseMsg = "";

        //1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");

        //2.构造PostMethod的实例  
        PostMethod postMethod = new PostMethod(url);

        postMethod.setRequestBody(param);
        try {
            // 4.执行postMethod,调用http接口  
            httpClient.executeMethod(postMethod);//200  

            //5.读取内容  
            responseMsg = postMethod.getResponseBodyAsString().trim();

            //6.处理返回的内容  

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //7.释放连接  
            postMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    /**
     * postbody方式
     * 
     * @param url
     * @param headers
     * @param param
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String postBody(String url, Map<String, Object> headers, String param) {
        String responseMsg = "";

        //1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");

        //2.构造PostMethod的实例  
        PostMethod postMethod = new PostMethod(url);

        for (String key : headers.keySet()) {
            postMethod.addRequestHeader(key, String.valueOf(headers.get(key)));
        }

        postMethod.setRequestBody(param);
        try {
            // 4.执行postMethod,调用http接口  
            httpClient.executeMethod(postMethod);//200  

            //5.读取内容  
            responseMsg = postMethod.getResponseBodyAsString().trim();

            //6.处理返回的内容  

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //7.释放连接  
            postMethod.releaseConnection();
        }
        System.out.println(responseMsg);
        return responseMsg;
    }

    @SuppressWarnings("deprecation")
    public static String postBody(String url, Map<String, Object> headers, Map<String, Object> params, String body) {
        String responseMsg = "";

        //1.构造HttpClient的实例  
        HttpClient httpClient = new HttpClient();

        httpClient.getParams().setContentCharset("utf-8");

        //2.构造PostMethod的实例  
        PostMethod postMethod = new PostMethod(url);

        if (headers != null) {
            for (String key : headers.keySet()) {
                postMethod.addRequestHeader(key, String.valueOf(headers.get(key)));
            }
        }
        if (params != null) {
            for (String key : params.keySet()) {
                postMethod.addParameter(key, String.valueOf(params.get(key)));
                System.out.println("key= " + key + " and value= " + params.get(key));
            }
        }
        if (body != null) {
            postMethod.setRequestBody(body);
        }
        try {
            // 4.执行postMethod,调用http接口  
            httpClient.executeMethod(postMethod);//200  

            //5.读取内容  
            responseMsg = postMethod.getResponseBodyAsString().trim();

            //6.处理返回的内容  

        } catch (HttpException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            //7.释放连接  
            postMethod.releaseConnection();
        }
        System.out.println("result:" + responseMsg);
        return responseMsg;
    }

}