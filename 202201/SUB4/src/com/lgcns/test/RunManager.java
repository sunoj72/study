package com.lgcns.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class RoutingTable {
	@Expose
	@SerializedName(value = "pathPrefix")
	String PathPrefix;
	
	@Expose
	@SerializedName(value = "url")
	String Url;
}

class Proxy {
	int port = -1;
	List<RoutingTable> routes = new ArrayList<>();
//	List<HashMap<String, String>> routes = new ArrayList<>();
//	HashMap<String, String> map = new HashMap<>();
}

class RequestTrace {
	String Id;
	@Expose
	@SerializedName(value = "target")
	String Target;
	@Expose
	@SerializedName(value = "status")
	String Status = "0";
	@Expose
	@SerializedName(value = "services")
	List<RequestTrace> Services = null;
	
	public RequestTrace(String target) {
		this.Target = target;
	}
	
	public RequestTrace(String target, String status) {
		this.Target = target;
		this.Status = status;
	}
	
	public RequestTrace(String target, String status, String service) {
		this.Target = target;
		this.Status = status;
		
		if (this.Services == null)
			this.Services = new ArrayList<>();
		
		this.Services.add(new RequestTrace(service));
	}
}


public class RunManager {
	public static Proxy proxy = null;
	public static HashMap<String, RequestTrace> tracingInfomation = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		proxy = initProxy(args[0]);
		
		initWebServer(proxy.port);
		
	}

	
	public static String readLineFromFile(String filename) {
		BufferedReader reader = null;
		String line = null;
		try {
			reader = new BufferedReader(new FileReader(filename));
			line = reader.readLine();
			
			if (reader != null) {
				reader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("File could not found. " + filename);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		return line;
	}
	
	public static Proxy initProxy(String path) {
		Proxy proxy = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(path));
			Gson gson = new Gson();
			proxy = gson.fromJson(reader, Proxy.class);
			
			if (reader != null) {
				reader.close();
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not found. " + path);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return proxy;
	}

	public static void initWebServer(int port) throws Exception {
		Server server = new Server();
		ServerConnector httpConector = new ServerConnector(server);
		httpConector.setHost("127.0.0.1");
		httpConector.setPort(port);
		server.addConnector(httpConector);
		
        // 2. Servlet Handler 매핑
		ServletHandler servletHandler = new ServletHandler();
		servletHandler.addServletWithMapping(ProxyServlet.class, "/");
		server.setHandler(servletHandler);
		
        // 3. Web Server start
		server.start();
		server.join();
	}
	
}
