package com.lgcns.test.suno.examples.r18;

import com.lgcns.test.suno.model.IMessageExecutor;
import com.lgcns.test.suno.model.r17.SimpleMessageExecutor;
import com.lgcns.test.suno.util.Shell;
import com.lgcns.test.suno.util.ShellCharacterMode;

public class R2018E01 {

	public static void main(String[] args) {
		IMessageExecutor executor = new SimpleMessageExecutor();
		Shell shell = new Shell();

		shell.start(ShellCharacterMode.UPPER_CASE, "Exit", false, executor);
		
		
	
	}
}
