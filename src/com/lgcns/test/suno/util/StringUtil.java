package com.lgcns.test.suno.util;

import java.util.Scanner;

public class StringUtil {	
	public static String readLineFromConsole() {
		String line;
		Scanner cin = new Scanner(System.in);
		
		line = cin.nextLine().toUpperCase();
		cin.close();
		
		return line;
	}
	
	
}
