/**
 * BBD Service Inc
 * All Rights Reserved @2016
 */
package com.bbd.config;

/**
 *
 * @author tjwang
 * @version $Id: KafkaConfig.java, v 0.1 2017/11/8 0008 11:10 tjwang Exp $
 */
//@EnableKafka
//@Configuration
public class KafkaConsumerConfig {

    //    @Autowired
    //    private KafkaConfigProperties kafkaConfigProperties;
    //
    //    @Bean
    //    public KafkaListenerContainerFactory<MessageListenerContainer> kafkaListenerContainerFactory() {
    //        KafkaListenerContainerFactory factory = new KafkaMessageListenerContainer(consumerFactory(), consumerConfigs());
    //        return factory;
    //    }
    //
    //    public ConsumerFactory<String, String> consumerFactory() {
    //        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    //    }
    //
    //    public Map<String, Object> consumerConfigs() {
    //        Map<String, Object> props = new HashMap<>();
    //        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigProperties.getBootstrapServers());
    //        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
    //        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
    //        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
    //        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    //        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    //        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
    //        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    //        return props;
    //    }
}
