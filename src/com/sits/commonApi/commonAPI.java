package com.sits.commonApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sits.general.General;
import com.sits.general.ReadProps;

public class commonAPI {
	 public synchronized static JSONObject getDropDownByWebService(String webUrl,JSONObject finalObject) {

			JSONObject json1 =null;
			
			try {
				int BUFFER_SIZE=0;
				JSONParser parser = new JSONParser();
				String webServiceUrl = General.checknull(ReadProps.getkeyValue("hrmsapi.url", "sitsResource"));
				URL url = new URL(webServiceUrl + webUrl);
			System.out.println("URL getDropDownByWebService--------------------------"+url);
				URLConnection  conn = (URLConnection ) url.openConnection();
				conn.setDoOutput(true);
				
				conn.setRequestProperty("Content-Type", "application/json");
				conn.setConnectTimeout(8000);
				conn.setReadTimeout(8000);
				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
				out.write(finalObject.toString());
				out.close();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				BUFFER_SIZE=1024;
				StringBuffer buffer1=new StringBuffer();
				char[] buffer = new char[BUFFER_SIZE]; // or some other size, 
				int charsRead = 0;
				while ( (charsRead  = br.read(buffer, 0, BUFFER_SIZE)) != -1) {
				 
					buffer1.append(buffer, 0, charsRead);		
				}
				//System.out.println("hi"+buffer1);
				 json1 = (JSONObject) parser.parse(buffer1.toString());
				//System.out.println("id"+json1.get("data"));
			} catch (Exception e) {
				System.out.println("EXCEPTION CAUSED BY: CommonDropDownFile[getDropDownByWebService]" + " "
						+ e.getMessage());
				
			} finally {
				try {

				} catch (Exception e) {
					System.out.println("EXCEPTION CAUSED BY: CommonDropDownFile[getDropDownByWebService]" + " "
							+ e.getMessage().toUpperCase());
					
				}
			}
			return json1;
		}

}
