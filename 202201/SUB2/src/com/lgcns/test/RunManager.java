package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

class Proxy {
	HashMap<String, String> map = new HashMap<>();
	
	public Proxy(String path) {
		String line = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(path));

			while((line = reader.readLine()) != null) {
				String[] params = line.split("#");
				map.put(params[0], params[1]);
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not found. " + path);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class RunManager {
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		String[] params = line.split(" ");
		
		Proxy proxy = new Proxy(params[0] + ".txt");
		String path = proxy.map.getOrDefault(params[1], null);
		
		while (path.startsWith("Proxy-")) {
			proxy = new Proxy(path);
			path = proxy.map.getOrDefault(params[1], null);
		}
		
		System.out.println(readLineFromFile(path));

		scanner.close();
	}

	
	public static String readLineFromFile(String filename) {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			line = reader.readLine();
		} catch (FileNotFoundException e) {
			System.out.println("File could not found. " + filename);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		

		return line;
	}
}
