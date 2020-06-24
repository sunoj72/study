package com.lgcns.test.suno.model.r17;

import java.util.StringTokenizer;

import com.lgcns.test.suno.model.IEntity;

public class SimpleEntity implements IEntity {
	String type;
	String message;
	String timestamp;

	public SimpleEntity() {}
	
	@Override
	public IEntity fromString(String buff) {
		StringTokenizer token = new StringTokenizer(buff, "#");
		
		this.timestamp = token.nextToken();
		this.type = token.nextToken();
		this.message = token.nextToken();
		
		return this;
	}

	@Override
	public String getKey() {
		return this.type;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %s", type, timestamp, message);
	}
}
