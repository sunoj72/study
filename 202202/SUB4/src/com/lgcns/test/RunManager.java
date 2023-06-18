package com.lgcns.test;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.http.HttpMethod;

import com.google.gson.Gson;

public class RunManager {
	public static InputQueueManager inputQueueManager = null; 
	public static HashMap<Integer, Process> children = new HashMap<>();
	public static int processId = -1;
	public static int workerOffset = -1;

	public static void main(String[] args) throws Exception {
		
		WorkerInfo winfo = null;
		
		if (args.length == 0) {
			winfo = initWorkers();
			
//			winfo = new WorkerInfo();
//			winfo.processCount = 2;
//			winfo.threadCount = 2;
//			winfo.outputQueueBatchSize = 2;
//			winfo.inputQueueCount = 3;
//			
//			winfo.inputQueueURIs = new ArrayList<>();
//			winfo.inputQueueURIs.add("http://127.0.0.1:8010/input");
//			winfo.inputQueueURIs.add("http://127.0.0.1:8011/input");
//			winfo.inputQueueURIs.add("http://127.0.0.1:8012/input");
//			winfo.outputQueueURI = "http://127.0.0.1:9010/output";
			
			initChildProcesses(winfo);
			
//			children.get(0).waitFor();
		} else {
			Gson gson = new Gson();
			
			processId = Integer.parseInt(args[0]);
			workerOffset = Integer.parseInt(args[2]);
			String strWorkerInfo = new String(Base64.getDecoder().decode(args[3]));
			winfo = gson.fromJson(strWorkerInfo, WorkerInfo.class);

			System.out.println("[ChildProcess#" + processId + ":" + workerOffset + "]");
			
			inputQueueManager = new InputQueueManager(winfo);
		}
		
		
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
	
	public static void initChildProcesses(WorkerInfo winfo) throws Exception {
		int processCount = winfo.processCount;
		Runtime rt = Runtime.getRuntime();
		Gson gson = new Gson();
		
		String strWorkerInfo = new String(Base64.getEncoder().encode(gson.toJson(winfo).getBytes("utf-8")));
		
		for (int i=0; i<processCount; i++) {
			// java -classpath %classpath% com.lgcns.test.RunManager %1(processIndex) %2(threadCount) %3(threadStartIndex) %4 %5 %6 %7 %8 %9
			ArrayList<String> cmd = new ArrayList<>();
			cmd.add("java");
			cmd.add("-classpath");
			cmd.add(".;./bin;../lib/*;./lib/*");
			cmd.add("com.lgcns.test.RunManager");
			cmd.add(Integer.toString(i));	// processIndex
			cmd.add(Integer.toString(0));	// threadOffset
			cmd.add(Integer.toString(i * winfo.threadCount));	// workerOffset == inputQueueOffset
			cmd.add(strWorkerInfo);			// workerInfo
			
			Process p = null;
			ProcessBuilder pb = new ProcessBuilder(cmd).inheritIO();
			p = pb.start();
			children.put(i, p);
			
			System.out.println("[Create Process #" + i + ":" + i * winfo.threadCount + "]");
		}
	}
}

class WorkerInfo {
	int processCount;
	int threadCount;
	int outputQueueBatchSize;
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
		this.threadCount = workerInfo.threadCount;
		
		int workerIndex = RunManager.workerOffset;
		for(int i=0; i<workerInfo.threadCount; i++) {
			Worker w = null;
			Thread t = null;
			if (workerIndex < workerInfo.inputQueueCount) {
				w = new Worker(workerIndex);
				workers.put(i, w);
				t = new Thread(new WorkerRunnable(workerIndex, workerInfo.inputQueueURIs.get(workerIndex), workerInfo.outputQueueURI, w));
			} else {
				t = new Thread(new WorkerRunnable(workerIndex, "", workerInfo.outputQueueURI, w));
			}
			
			workerIndex++;
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
		System.out.println("[ChildProcess#CreateWorker#" + index + ":" + worker + ":" + inputQueueURI + "]");
		
		
		while(true) {
			try {
				if (this.worker != null) {
					HttpClient httpClient = new HttpClient();
					httpClient.start();
					
//					System.out.println("[ChildProcess#GetInputQueue#" + index + "]");
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
				} else {
					Thread.sleep(10 * 1000);
				}
				
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
