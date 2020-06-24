package com.lgcns.test.suno.examples.r17;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import com.lgcns.test.suno.model.r17.LogEntity;
import com.lgcns.test.suno.model.r17.SimpleCounter;
import com.lgcns.test.suno.util.FileUtil;

public class R2017E01 {
	public static void main(String[] args) {
		String fileInput = "./data/17/1/LOGFILE_A.TXT";
		String fileOutput = "./data/17/1/REPORT_1.TXT";

		// 1. 파일 읽기
		BufferedReader reader = FileUtil.getLineReader(fileInput);
		
		// 2. 파일 처리
		String line;
		LogEntity item;
		SimpleCounter counter = new SimpleCounter(); 
		
		try {
			while ((line = reader.readLine()) != null) {
				item = new LogEntity();
				item.fromString(line);
				counter.append(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 3. 파일 저장
		counter.printCounter();
		
		try (BufferedWriter writer = FileUtil.getLineWriter(fileOutput)) {
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
