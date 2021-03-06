package com.lgcns.test.suno.util;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import com.lgcns.test.suno.thread.ProcessThread;

public class ProcessUtil {
	public static void execute() {
		
	}
	
	public static String executeWithReturn(List<String> exec) throws IOException, InterruptedException {
		return executeWithReturn(exec, 1);
	}
	
	public static String executeWithReturn(List<String> exec, int waitLines) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		ProcessBuilder pb = new ProcessBuilder(exec);
		//pb.redirectErrorStream(true);
		Process ps = pb.start();
		
		Scanner scan = new Scanner(ps.getInputStream()); 
		
		int i = 0;
		while (scan.hasNextLine()) {
			
			String line = scan.nextLine();
			sb.append(String.format("%s\n", line));
			i++;
			
			if (waitLines > 0 && i >= waitLines) {
				break;
			}
		}
		
		ps.waitFor();
		scan.close();
			
		return sb.toString();
	}

	public static ProcessThread executeWithThread(String name, List<String> exec) {
		ProcessThread thread = new ProcessThread(name, exec);
		thread.startThread();
		
		return thread;
	}
	
//	public static void main(String[] args) {
//		// List.get(0) == Programm
//		ArrayList<String> exec = new ArrayList<>();
//		exec.add("cmd.exe");
//		exec.add("/c");
//		exec.add("dir");
//		exec.add("/w");
//		
//		try {
//			System.out.println(ProcessUtil.executeWithReturn(exec, 0));
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
}