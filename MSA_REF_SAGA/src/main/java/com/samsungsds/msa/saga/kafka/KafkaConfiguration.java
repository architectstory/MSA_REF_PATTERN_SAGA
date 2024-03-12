package com.samsungsds.msa.saga.kafka;

import com.samsungsds.msa.saga.event.SagaEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.server-url}")
    String serverUrl;
    @Value("${kafka.consumer.group-id}")
    String groupId;
    @Value("${kafka.consumer.json-trusted-packages}")
    String jsonTrustedPackages;

    @Value("${kafka.username}")
    String kafkaUsername;

    @Value("${kafka.password}")
    String kafkaPassword;

    @Bean
    public ProducerFactory<String, SagaEvent<?>> producerFactory(){
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverUrl);
        configProps.put("group-id", groupId);
/** for cloud server
        configProps.put("security.protocol","SASL_PLAINTEXT");
        configProps.put("sasl.mechanism", "SCRAM-SHA-256");
        configProps.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='"+kafkaUsername+"' password='"+kafkaPassword+"';");
*/
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
    @Bean
    public KafkaTemplate<String, SagaEvent<?>> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, SagaEvent<?>> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverUrl);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
/** for cloud server
        configProps.put("security.protocol", "SASL_PLAINTEXT");
        configProps.put("sasl.mechanism", "SCRAM-SHA-256");
        configProps.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username='"+kafkaUsername+"' password='"+kafkaPassword+"';");
*/
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, jsonTrustedPackages);
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SagaEvent<?>> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, SagaEvent<?>> concurrentKafkaListenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }
}
