package com.facebook.notification.kafka;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class NotificationProducer {
    private KafkaProducer<Integer, String> producer;
    private static final String TOPIC = "notification";

    public NotificationProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producer = new KafkaProducer<>(props);
    }

    public void sendNotification(String type,int userId, String message) {
        try {
        	int partition = getPartitionForType(type);
        	Future<RecordMetadata> future = producer.send(new ProducerRecord<>(TOPIC,partition, userId, message));
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
    
    private int getPartitionForType(String type) {
        switch (type.toLowerCase()) {
            case "comment":
                return 0;
            case "like":
                return 1;
            case "tag":
                return 2;
            default:
                return 0;
        }
    }
}
