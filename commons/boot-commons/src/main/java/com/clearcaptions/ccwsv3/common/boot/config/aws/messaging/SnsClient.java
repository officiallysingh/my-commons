package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SnsClient {
	
	private final NotificationMessagingTemplate notificationMessagingTemplate;
	
	public SnsClient(final NotificationMessagingTemplate notificationMessagingTemplate) {
		this.notificationMessagingTemplate = notificationMessagingTemplate;
	}

    public <T> void publishMessage(final String topic, final Message<T> message) {
    	try {
	        this.notificationMessagingTemplate.convertAndSend(topic, message, message.getHeaders());
	        log.debug("Published message to topic: " + topic + " content: " + message.getPayload());
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Topic: " + topic, e);
			throw new MessagingException("Error while pushing message to Topic: " + topic, e);
		}
    }

    public <T> void publishMessage(final String topic, final Message<T> message, 
    		final Map<String, Object> headers) {
    	try {
	        this.notificationMessagingTemplate.convertAndSend(topic, message, headers);
	        log.debug("Published message to topic: " + topic + " content: " + message.getPayload());
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Topic: " + topic, e);
			throw new MessagingException("Error while pushing message to Topic: " + topic, e);
		}
    }

    public <T> void publishMessage(final String topic, final T message) {
    	try {
	        this.notificationMessagingTemplate.convertAndSend(topic, message);
	        log.debug("Published message to topic: " + topic + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Topic: " + topic, e);
			throw new MessagingException("Error while pushing message to Topic: " + topic, e);
		}
    }

    public <T> void publishMessage(final String topic, final T message, 
    		final Map<String, Object> headers) {
    	try {
	        this.notificationMessagingTemplate.convertAndSend(topic, message, headers);
	        log.debug("Published message to topic: " + topic + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Topic: " + topic, e);
			throw new MessagingException("Error while pushing message to Topic: " + topic, e);
		}
    }
}
