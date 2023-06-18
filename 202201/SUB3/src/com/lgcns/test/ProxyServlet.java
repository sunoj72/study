package com.lgcns.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.annotations.SerializedName;



public class ProxyServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
    private HttpClient httpClient = null;

	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		process(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		process(req, res);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse res) {
		httpClient = new HttpClient();

		// http://127.0.0.1:5001/auth/lgcns?id=apple&key=DFGE
		// http://127.0.0.1:5001/auth?id=lgcns
		String servletPath = req.getServletPath().replaceFirst("/", "");
		String[] servletPaths = servletPath.split("\\/");

		String query = req.getQueryString();

		String remoteUrl=null;
		String result=null;
		
		ContentResponse ctxResponse = null;
		
//		System.out.println("Request QuertString: " + query);
		if (RunManager.proxy != null && RunManager.proxy.routes != null) {
			Iterator<RoutingTable> itr = RunManager.proxy.routes.iterator();
			
			while(itr.hasNext()) {
				RoutingTable node = itr.next();
				if (node.PathPrefix.endsWith(servletPaths[0])) {
					remoteUrl = node.Url;
					break;
				}
			}
		}
		
		if (remoteUrl == null || remoteUrl.isEmpty()) {
			res.setStatus(HttpStatus.NOT_FOUND_404);
			return;
		}
		
		try {
			httpClient.start();
			
			String url = null;
			if (query == null || query.isEmpty()) {
				url = remoteUrl + req.getServletPath();
			} else {
				url = remoteUrl + req.getServletPath() + "?" + query;
			}
			System.out.println("Remote Url: " + url);
			
	        Request request = httpClient.newRequest(url).method(HttpMethod.fromString(req.getMethod()));
	        
	        ctxResponse = request.send();
	        
	        res.addHeader("Content-Type", ctxResponse.getHeaders().get(HttpHeader.CONTENT_TYPE));
	        
	        res.getWriter().write(ctxResponse.getContentAsString());
	        
			res.setStatus(ctxResponse.getStatus());
			
	        httpClient.stop();
			
				
		} catch (Exception e) {
			res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			e.printStackTrace();
			return;
		}
	}
}