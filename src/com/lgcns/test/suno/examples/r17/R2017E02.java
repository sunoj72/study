package com.lgcns.test.suno.examples.r17;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.lgcns.test.suno.model.r17.SimpleCounter;
import com.lgcns.test.suno.model.r17.SimpleEntity;
import com.lgcns.test.suno.util.FileUtil;

public class R2017E02 {
	public static void main(String[] args) {
		//String pathInput = "";
		//String pathOutput = "";
		String fileInput = "./data/17/1/LOGFILE_A.TXT";
		String fileOutput = "./data/17/1/REPORT_1.TXT";

		// 1. 파일 읽기
		BufferedReader reader = FileUtil.getLineReader(fileInput);
		BufferedWriter writer = FileUtil.getLineWriter(fileOutput);
		
		// 2. 파일 처리
		String line;
		SimpleEntity item;
		SimpleCounter counter = new SimpleCounter(); 
		
		try {
			while ((line = reader.readLine()) != null) {
				item = new SimpleEntity();
				item.fromString(line);
				counter.append(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 3. 파일 저장
		try {
			for(Map.Entry<String, Integer> kv : counter.getCounters().entrySet()) {
				writer.write(String.format("%s %s%s", kv.getKey(), kv.getValue(), System.lineSeparator()));
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

