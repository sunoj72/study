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
import com.google.gson.annotations.SerializedName;

class RoutingTable {
	@SerializedName(value = "pathPrefix")
	String PathPrefix;

	@SerializedName(value = "url")
	String Url;
}
	
class Proxy {
	int port = -1;
	List<RoutingTable> routes = new ArrayList<>();
//	List<HashMap<String, String>> routes = new ArrayList<>();
	HashMap<String, String> map = new HashMap<>();
}





public class RunManager {
	public static Proxy proxy = null;
	
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
