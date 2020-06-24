package com.lgcns.test.suno.model.r17;

import com.lgcns.test.suno.model.IMessageExecutor;

public class SimpleMessageExecutor implements IMessageExecutor {

	@Override
	public void execute(String msg) {
		System.out.println(msg);
	}

}
