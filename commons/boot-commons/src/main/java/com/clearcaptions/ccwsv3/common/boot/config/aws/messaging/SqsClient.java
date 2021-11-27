package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import java.util.Map;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;

import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqsClient {
	
	private final QueueMessagingTemplate queueMessagingTemplate;

	public SqsClient(final QueueMessagingTemplate queueMessagingTemplate) {
		this.queueMessagingTemplate = queueMessagingTemplate;
	}

    public <T> void sendMessage(final String queue, final Message<T> message) {
    	try {
	        this.queueMessagingTemplate.convertAndSend(queue, message);
	        log.debug("Sent message to Queue: " + queue + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Queue: " + queue, e);
			throw new MessagingException("Error while pushing message to Queue: " + queue, e);
		}
    }
    
    public <T> void sendMessage(final String queue, final Message<T> message, 
    		final Map<String, Object> headers) {
    	try {
	        this.queueMessagingTemplate.convertAndSend(queue, message, headers);
	        log.debug("Sent message to Queue: " + queue + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Queue: " + queue, e);
			throw new MessagingException("Error while pushing message to Queue: " + queue, e);
		}
    }

    public <T> void sendMessage(final String queue, final T message) {
    	try {
	        this.queueMessagingTemplate.convertAndSend(queue, message);
	        log.debug("Sent message to Queue: " + queue + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Queue: " + queue, e);
			throw new MessagingException("Error while pushing message to Queue: " + queue, e);
		}
    }
    
    public <T> void sendMessage(final String queue, final T message, 
    		final Map<String, Object> headers) {
    	try {
	        this.queueMessagingTemplate.convertAndSend(queue, message, headers);
	        log.debug("Sent message to Queue: " + queue + " content: " + message);
    	} catch (final Exception e) {
    		log.error("Error while pushing message to Queue: " + queue, e);
			throw new MessagingException("Error while pushing message to Queue: " + queue, e);
		}
    }

    public Optional<String> retrieveMessage(final String queue) {
   		MessageWrapper messageWrapper = this.queueMessagingTemplate
   				.receiveAndConvert(queue, MessageWrapper.class);
   		if(messageWrapper == null) {
   			return Optional.empty();
   		} else {
   			return Optional.ofNullable(messageWrapper.payload);
   		}
   	}

   	public <T> Optional<T> retrieveMessage(final String queue, final Class<T> targetClass) {
   		T message = this.queueMessagingTemplate
   				.receiveAndConvert(queue, targetClass);
   		if(message == null) {
   			return Optional.empty();
   		} else {
   			return Optional.ofNullable(message);
   		}
   	}
}
