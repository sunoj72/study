package com.lgcns.test.suno.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FileUtil {
	public static Scanner getFileScanner(String filename) {
		return (new Scanner(getLineReader(filename)));
	}
	
	public static BufferedReader getLineReader(String filename) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			return reader;
		} catch (FileNotFoundException e) {
			System.out.println("File could not found. " + filename);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static BufferedReader getByteReader(String filename) {
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(filename));
			return reader;
		} catch (FileNotFoundException e) {
			System.out.println("File could not fouind. " + filename);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}	
	
	public static PrintWriter getFileWriter(String filename) {
		return getFileWriter(filename, false);
	}
	
	public static PrintWriter getFileWriter(String filename, boolean append) {
		return (new PrintWriter(getLineWriter(filename, append)));
	}
	
	public static BufferedWriter getLineWriter(String filename) {
		return getLineWriter(filename, false);
	}

	public static BufferedWriter getLineWriter(String filename, boolean append) {
		BufferedWriter writer = null;
		
		try {
			writer = new BufferedWriter(new FileWriter(filename, append));
			return writer;
		} catch (IOException e) {
			System.out.println("File could not created. " + filename);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}	

	public static void copyFile(String input, String output) {
		int BUFFER_SIZE = 8 * 1024;
		int readLen;
		
		try {
			InputStream is = new FileInputStream(input);
			OutputStream os = new FileOutputStream(output);
			
			byte[] buff = new byte[BUFFER_SIZE];
			
			while ((readLen = is.read(buff)) != -1) {
				os.write(buff, 0, readLen);
			}
			
			is.close();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void mkdirDirectory(String dirname) {
		Path dir = Paths.get(dirname);
		
		if (!Files.exists(dir)) {
			try {
				Files.createDirectories(dir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
