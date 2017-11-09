/*
 * Copyright (c) BrandBigData.com Inc.
 * All Rights Reserved 2017.
 */

/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.util;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xc
 * @version $Id: ss.java, v 0.1 2016年12月12日 上午11:39:49 xc Exp $
 */
public class EsUtil {

    /**
     * 索引
     */
    public static final String     INDEX     = "bbd_opinion";
    /**
     * 失联企业
     */
    public static final String     TYPE      = "opinion";

    public static final String     HOT_INDEX = "bbd_opinion_hot";
    public static final String     HOT_TYPE  = "hot";

    private static Logger          logger    = LoggerFactory.getLogger(EsUtil.class);
    /**
     * es链接
     */
    private static TransportClient client;

    static {
        try {
            Settings settings = Settings.builder().put("cluster.name", "bbd_opinion_cluster").build();
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.28.100.70"), 9300));

        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static TransportClient getClient() {
        return client;
    }

}
