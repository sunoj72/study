package com.lgcns.test.suno.model.r17;

import java.util.StringTokenizer;

import com.lgcns.test.suno.model.IEntity;

public class LogEntity implements IEntity {
	String type;
	String dateTime;
	String messageId;

	public LogEntity(String type, String dateTime, String messageId) {
		this.type = type;
		this.dateTime = dateTime;
		this.messageId = messageId;
	}

	public LogEntity() {}

	@Override
	public IEntity fromString(String buff) {
		StringTokenizer token = new StringTokenizer(buff, "#");
		
		this.dateTime = token.nextToken();
		this.type = token.nextToken();
		this.messageId = token.nextToken();
		
		return this;
	}

	@Override
	public String getKey() {
		return this.type;
	}

	@Override
	public String toString() {
		return String.format("%s, %s, %s", type, dateTime, messageId);
	}
} 