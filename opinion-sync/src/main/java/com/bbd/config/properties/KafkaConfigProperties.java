/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author tjwang
 * @version $Id: KafkaProperties.java, v 0.1 2017/11/8 0008 11:10 tjwang Exp $
 */
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {

    private String  bootstrapServers;

    private String  groupId;

    private Boolean enableAutoCommit;

    private Integer maxPoolRecord;

    private String  sessionTimeoutMs;

    private String  autoOffsetReset;

    private String  keyDeserializer;

    private String  valueDeserializer;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Boolean getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public void setEnableAutoCommit(Boolean enableAutoCommit) {
        this.enableAutoCommit = enableAutoCommit;
    }

    public Integer getMaxPoolRecord() {
        return maxPoolRecord;
    }

    public void setMaxPoolRecord(Integer maxPoolRecord) {
        this.maxPoolRecord = maxPoolRecord;
    }

    public String getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(String sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getKeyDeserializer() {
        return keyDeserializer;
    }

    public void setKeyDeserializer(String keyDeserializer) {
        this.keyDeserializer = keyDeserializer;
    }

    public String getValueDeserializer() {
        return valueDeserializer;
    }

    public void setValueDeserializer(String valueDeserializer) {
        this.valueDeserializer = valueDeserializer;
    }
}
