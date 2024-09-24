package com.facebook.notification.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.facebook.notification.websocket.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationConsumer {
	private volatile boolean exit = false;
	public static Map<Integer, NotificationConsumer> userConsumer = new ConcurrentHashMap<>();
	public Consumer<Integer, String> consumer;
	private static final String TOPIC = "notification";
	private long lastOffset = -1;

	public NotificationConsumer(Integer userId) {
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "notification-group-" + userId);
		props.put("key.deserializer", IntegerDeserializer.class.getName());
		props.put("value.deserializer", StringDeserializer.class.getName());
		props.put("enable.auto.commit", "false");
		props.put("auto.offset.reset", "earliest");
		consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));
		userConsumer.put(userId, this);
	}

	public void removeConsumer(Integer userId,NotificationConsumer consumer) {
		if (consumer != null) {
			consumer.consumer.close();
			userConsumer.remove(userId);
			exit = true;
		}
	}

	public void consume(Integer userId) {
		if (consumer != null) {
			while (!exit) {
				try {
					synchronized (this) {
						ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
						records.forEach(record -> {
							if (record.key().equals(userId)) {
								Service.sendNotification(userId, record.value());
								lastOffset = record.offset();
							}
						});
						if (lastOffset >= 0) {
							consumer.commitSync(
									Collections.singletonMap(new org.apache.kafka.common.TopicPartition(TOPIC, 0),
											new org.apache.kafka.clients.consumer.OffsetAndMetadata(lastOffset + 1)));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
