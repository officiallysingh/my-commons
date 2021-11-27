package com.clearcaptions.ccwsv3.common.boot.config.aws.messaging;

import java.util.UUID;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.handlers.RequestHandler2;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;

import io.awspring.cloud.messaging.core.SqsMessageHeaders;

public class SnsFifoMessageGroupIdHandler extends RequestHandler2 {

    @Override
    public AmazonWebServiceRequest beforeExecution(AmazonWebServiceRequest request) {
        AmazonWebServiceRequest amazonWebServiceRequest = super.beforeExecution(request);
        if (amazonWebServiceRequest instanceof PublishRequest) {
            PublishRequest publishRequest = (PublishRequest) amazonWebServiceRequest;
            if (publishRequest.getTopicArn().contains(".fifo")) {
            	MessageAttributeValue messageGroupId = publishRequest.getMessageAttributes()
            			.get(SqsMessageHeaders.SQS_GROUP_ID_HEADER);
            	MessageAttributeValue messageDeduplicationId = publishRequest.getMessageAttributes()
            			.get(SqsMessageHeaders.SQS_DEDUPLICATION_ID_HEADER);
            	if(messageGroupId != null) {
	            	publishRequest.setMessageGroupId(messageGroupId.getStringValue());
	                publishRequest.setMessageDeduplicationId(messageDeduplicationId != null 
	                		? messageDeduplicationId.getStringValue() : UUID.randomUUID().toString());
            	}
            }
        }
        return amazonWebServiceRequest;
    }
}
