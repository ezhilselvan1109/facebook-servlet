package com.facebook.notification.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.facebook.api.user.User;
import com.facebook.notification.Notification;
import com.facebook.notification.websocket.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.time.Duration;
import java.util.Collections;
import java.util.List;
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
    	ObjectMapper objectMapper = new ObjectMapper();
        while (true) {
            try {
                for (ConsumerRecord<String, String> record : consumer.poll(Duration.ofMillis(100))) {
                    String notificationMessage = record.value();
                    JSONObject notificationJson = new JSONObject(notificationMessage);
                    int postUserId = notificationJson.getInt("post_user_id");
                    String comment = notificationJson.getString("comment");
                    int postId = notificationJson.getInt("post_id");
                    JSONArray tagArray = notificationJson.getJSONArray("tag");
                    List<Object> taggedUserIds = tagArray.toList();
                    JSONArray userArray = notificationJson.getJSONArray("user");
                    List<User> userList = objectMapper.readValue(userArray.toString(), objectMapper.getTypeFactory().constructCollectionType(List.class, Object.class));
                    String post=new JSONObject(new Notification(null,postId,userList,"New comment on your post : "+comment,null)).toString();
                    String tag=new JSONObject(new Notification(null,postId,userList,"You were tagged in a comment : "+comment,null)).toString();
                	Service.sendNotification(postUserId, post);
                    for (Object taggedUserId : taggedUserIds) {
                        Service.sendNotification(taggedUserId, tag);
                    }
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
