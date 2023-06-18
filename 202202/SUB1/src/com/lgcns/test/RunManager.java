package com.lgcns.test;

import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Worker[] workers = new Worker[2];
		
		
		// Worker 생성 및 실행 Sample - 아래 2개의 라인을 지우고 구현하세요.
		workers[0] = new Worker(0);
		workers[1] = new Worker(1);
		
		Scanner scaner = new Scanner(System.in);
		
		while(scaner.hasNextLine()) {
			String line = scaner.nextLine();
			String result = null;
			
			String[] params = line.split("\\ ");
			if (params[0].equals("0")) {
				result = workers[0].run(params[1]);
			} {
				result = workers[1].run(params[1]);
			}
			
			if (result != null) {
				System.out.println(result);
			}
		}
	}
}
