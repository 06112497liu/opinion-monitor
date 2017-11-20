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
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author xc
 * @version $Id: ss.java, v 0.1 2016年12月12日 上午11:39:49 xc Exp $
 */
@Component
public class EsUtil {

    /**
     * 索引
     */
    public static final String INDEX     = "bbd_opinion";
    /**
     * 舆情类型
     */
    public static final String TYPE      = "opinion";

    public static final String HOT_INDEX = "bbd_opinion_hot";
    public static final String HOT_TYPE  = "hot";
    private static Logger      logger    = LoggerFactory.getLogger(EsUtil.class);
    /**
     * es链接
     */
    private TransportClient    client;
    @Value("${es.host}")
    private String             esHost;
    @Value("${es.port}")
    private Integer            esPort;
    @Value("${es.cluster}")
    private String             esCluster;

    @PostConstruct
    public void init() {
        try {
            Settings settings = Settings.builder().put("cluster.name", esCluster).build();
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(getEsHost()), getEsPort()));
            List<DiscoveryNode> nodes = client.listedNodes();
            for (DiscoveryNode node : nodes) {
                logger.info("Discovered node: " + node.getHostAddress());
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public TransportClient getClient() {
        return client;
    }

    public String getEsHost() {
        return esHost;
    }

    public Integer getEsPort() {
        return esPort;
    }
}
