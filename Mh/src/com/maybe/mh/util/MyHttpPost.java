package com.maybe.mh.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class MyHttpPost {

	public static String doPost(String url, List<NameValuePair> params) {

		HttpParams httpParams;

		HttpClient httpClient;

		HttpPost httpRequest = new HttpPost(url);

		String strResult = "doPostError";

		httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSoTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		HttpClientParams.setRedirecting(httpParams, true);

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		httpClient = new DefaultHttpClient(httpParams);

		try {
			if (params != null) {
				httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		}

		//System.out.println("strResult =" + strResult);

		return strResult;
	}
	
	public static String doGet(String url, String alias,String page,String page_limit) {

		HttpParams httpParams;

		HttpClient httpClient;

		HttpGet httpRequest = new HttpGet(url + "?alias=" + alias+"&page="+page+"&page_limit="+page_limit);

		String strResult = "doGetError";

		httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSoTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		HttpClientParams.setRedirecting(httpParams, true);

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		httpClient = new DefaultHttpClient(httpParams);

		try {
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		}

		//System.out.println("strResult =" + strResult);

		return strResult;
	}
	
	public static String doGet(String url) {

		HttpParams httpParams;

		HttpClient httpClient;
		
		HttpGet httpRequest = new HttpGet(url);

		String strResult = "doGetError";

		httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSoTimeout(httpParams, 4 * 1000);

		HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

		HttpClientParams.setRedirecting(httpParams, true);

		String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
		HttpProtocolParams.setUserAgent(httpParams, userAgent);

		httpClient = new DefaultHttpClient(httpParams);

		try {
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				strResult = "Error Response: " + httpResponse.getStatusLine().toString();
			}
		} catch (ClientProtocolException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (IOException e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		} catch (Exception e) {
			// strResult = e.getMessage().toString();
			e.printStackTrace();
		}

		//System.out.println("strResult =" + strResult);

		return strResult;
	}


}
