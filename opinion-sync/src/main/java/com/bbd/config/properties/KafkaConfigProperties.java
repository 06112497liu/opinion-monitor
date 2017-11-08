/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.config.properties;

/**
 *
 * @author tjwang
 * @version $Id: KafkaProperties.java, v 0.1 2017/11/8 0008 11:10 tjwang Exp $
 */
//@Component
//@ConfigurationProperties(prefix = "kafka")
public class KafkaConfigProperties {

    private String  bootstrapServers;

    private String  groupId;

    private Boolean enableAutoCommit;

    private String  keyDeserializer;

    private String  valueDeserializer;

    private String  autoOffsetReset;

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

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }
}
