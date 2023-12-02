package com.josemiguel.codechallenge.infrastructure.interceptors;

import org.apache.commons.codec.binary.Hex;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

import com.nimbusds.jose.util.Base64;

import lombok.extern.slf4j.Slf4j;

//@Component
//@GlobalChannelInterceptor(patterns = "*-in*")
@Slf4j
public class KafkaInputChannelInterceptor implements ChannelInterceptor {

	

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		// TODO Auto-generated method stub

		log.info("!!!!! Class preSend Channel:" + message.getPayload().getClass());
		
		byte[] rawMessage = (byte[])message.getPayload();
		
		var b64RawMessage = Base64.encode(rawMessage);
		var hexRawMessage = Hex.encodeHex(rawMessage);
		
		log.info("!!!!!preSend Channel:" + b64RawMessage);
		log.info("!!!!!preSend Channel:" + new String(hexRawMessage));
		
		log.info("!!!!!Headers:" + message.getHeaders());
		return ChannelInterceptor.super.preSend(message, channel);
	}

	
}
