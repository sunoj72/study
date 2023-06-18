package com.lgcns.test;

import java.nio.charset.Charset;
import java.nio.charset.spi.CharsetProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

public class RunManager {
	public static InputQueueManager inputQueueManager = null; 

	public static void main(String[] args) throws Exception {
		
		WorkerInfo winfo = initWorkers();

		inputQueueManager = new InputQueueManager(winfo);
		
		
	}
	
	public static WorkerInfo initWorkers() throws Exception {
		HttpClient httpClient = new HttpClient();
		httpClient.start();
		ContentResponse contentRes = httpClient.newRequest("http://127.0.0.1:8080/queueInfo")
				.method(HttpMethod.GET).send();
		
		Gson gson = new Gson();
		WorkerInfo winfo = gson.fromJson(contentRes.getContentAsString(), WorkerInfo.class);
		
		System.out.println(contentRes.getContentAsString());
		httpClient.stop();
		
		return winfo;
	}
	
	
}

class WorkerInfo {
	int inputQueueCount;
	List<String> inputQueueURIs;
	String outputQueueURI;
}

class InputMessage {
	long timestamp;
	String value;
}

class OutputMessage {
	String result;
	
	public OutputMessage(String message) {
		result = message;
	}
}

class InputQueueManager {
	int threadCount = 0;
	WorkerInfo workerInfo;
	Map<Integer, Thread> threads = new HashMap<>();
	Map<Integer, Worker> workers = new HashMap<>();
	
	public InputQueueManager(WorkerInfo workerInfo) {
		this.threadCount = workerInfo.inputQueueCount;
		
		for(int i=0; i<workerInfo.inputQueueCount; i++) {
			Worker w = new Worker(i);
			workers.put(i, w);
			
			Thread t = new Thread(new WorkerRunnable(i, workerInfo.inputQueueURIs.get(i), workerInfo.outputQueueURI, w));
			threads.put(i, t);
			t.start();
		}
	}
}


class WorkerRunnable implements Runnable {
	int index = -1;
	String inputQueueURI;
	String outputQueueURI;
	Worker worker;
	
	public WorkerRunnable(int index, String inputURI, String outputURI, Worker worker) {
		this.index = index;
		inputQueueURI = inputURI;
		outputQueueURI = outputURI;
		this.worker = worker;
	}

	@Override
	public void run() {
		while(true) {
			try {
				HttpClient httpClient = new HttpClient();
				httpClient.start();
				
				ContentResponse contentRes = httpClient.newRequest(inputQueueURI)
						.method(HttpMethod.GET).send();
				
				Gson gson = new Gson();
				InputMessage inputMessage = gson.fromJson(contentRes.getContentAsString(), InputMessage.class);
				
				String result = worker.run(inputMessage.timestamp, inputMessage.value);
				
				if (result != null) {
					sendOutputQueue(result);
					System.out.println(result);
				}
				
				httpClient.stop();
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public void sendOutputQueue(String message) {
		OutputMessage outnputMessage = new OutputMessage(message);
		Gson gson = new Gson();
		
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.start();
			
			httpClient.newRequest(outputQueueURI)
				.content(new StringContentProvider(gson.toJson(outnputMessage), "utf-8"))
				.method(HttpMethod.POST).send();
			
			httpClient.stop();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
