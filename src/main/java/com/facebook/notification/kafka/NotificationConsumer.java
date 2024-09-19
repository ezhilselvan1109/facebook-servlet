package com.facebook.notification.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.facebook.notification.websocket.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class NotificationConsumer {
    private KafkaConsumer<String, String> consumer;
    private static final String TOPIC = "Topic1";

    public NotificationConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification_group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    public void consume() {
        while (true) {
            try {
                for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100))) {
                    String notificationMessage = record.value();
                    Service.broadcast("Message from Comsumer : "+notificationMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        consumer.close();
    }
}
