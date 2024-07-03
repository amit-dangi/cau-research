/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sits.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sits.general.General;
import com.sits.general.ReadProps;

@WebServlet(name = "DownloadFile", urlPatterns = {"/downloadfile"})
public class DownloadFile extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filePath="", path="";    	
		
		System.out.println("Git test");
		//System.out.println("fileName :"+request.getParameter("filename"));
		String fileName=General.checknull(request.getParameter("filename"));		
    	//System.out.println("fileName :"+fileName);
    	
		path=General.checknull(ReadProps.getkeyValue("document.path", "sitsResource"));	
		//System.out.println("path :"+path);
		String folder=General.checknull(request.getParameter("folderName"));
		//System.out.println("folder :"+folder);
		String subFolderName=General.checknull(request.getParameter("subFolderName"));
		
		if(!folder.equals("") && subFolderName.equals("")){
			filePath = path+folder+"/"+fileName;
		}else if(!folder.equals("") && !subFolderName.equals("")){
			filePath = path+folder+"/"+subFolderName+"/"+fileName;
		}
		//System.out.println("filePath :"+filePath);
		try {
    		File downloadFile = new File(filePath);
            FileInputStream inStream = new FileInputStream(downloadFile);
             
            // if you want to use a relative path to context root:
            String relativePath = getServletContext().getRealPath("");
            
            // obtains ServletContext
            ServletContext context = getServletContext();
             
            // gets MIME type of the file
            String mimeType = context.getMimeType(filePath);
            if (mimeType == null) {        
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream";
            }
             
            // modifies response
            response.setContentType(mimeType);
            response.setContentLength((int) downloadFile.length());
             
            // forces download
            String headerKey = "Content-Disposition";
            String headerValue = String.format("inline; filename=\"%s\"", downloadFile.getName());
            response.setHeader(headerKey, headerValue);
             
            // obtains response's output stream
            OutputStream outStream = response.getOutputStream();
             
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
             
            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }
             
            inStream.close();
            outStream.close();  	
		} catch (Exception e) {
			System.out.println("ERROR IN DownloadFile :"+e.getMessage());			
			/*if(General.checknull(e.getMessage().toString()).contains("The system cannot find")){
				System.out.println("test ratan");
				//File downloadFile = new File(General.checknull(ReadProps.getkeyValue("cuk_exp_file_path", "filemovement")));
				File downloadFile = new File("D://CUK-FILES/Demo7");
				FileInputStream inStream = new FileInputStream(downloadFile);
	            ServletContext context = getServletContext();
	            String mimeType = context.getMimeType(filePath);
	            if(mimeType == null) {        
	            	mimeType = "application/octet-stream";
	            }	             
	            response.setContentType(mimeType);
	            response.setContentLength((int) downloadFile.length());
	            String headerKey = "Content-Disposition";
	            String headerValue = String.format("inline; filename=\"%s\"", downloadFile.getName());
	            response.setHeader(headerKey, headerValue);	             
	            OutputStream outStream = response.getOutputStream();	             
	            byte[] buffer = new byte[4096];
	            int bytesRead = -1;	             
	            while ((bytesRead = inStream.read(buffer)) != -1) {
	            	outStream.write(buffer, 0, bytesRead);
	            }	             
	            inStream.close();
	            outStream.close();
			}
		
*/	
		}	
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	public String getServletInfo() {
		return "Short description";
	}
}