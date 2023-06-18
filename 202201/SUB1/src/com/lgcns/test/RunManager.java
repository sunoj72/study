package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class RunManager {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		
		
		String policyFile = readLineFromFile(line + ".txt");
		
		if ( policyFile != null) {
			System.out.println(readLineFromFile(policyFile));
		}
		
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
