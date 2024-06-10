/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.research_activity.target_achievement_details;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sits.common.AesUtil;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class TargetAchievementService
 */

@WebServlet("/TargetAchievementService")
public class TargetAchievementService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog research activity TargetAchievementService");
	AesUtil aesUtil = new AesUtil(128,10);
	
    
    public TargetAchievementService() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			processRequest(request, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().append("Served at TargetAchievementService: ").append(request.getContextPath());
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		switch (General.checknull(request.getParameter("fstatus"))){
		case "UPLOAD":
			TargetAchievementUploadrequest(request, response);
			break;
		case "DETAILS":
			getTargetAchievementDetail(request, response);
			break;
		default: System.out.println("Invalid grade TargetAchievementService");
		}
	}
	
	/*Void Method to get the getTargetAchievementDetail saved details or
	get the selected proposal details from Project Proposal Submission form as per proposal id*/
	public synchronized void getTargetAchievementDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String id=General.checknull(request.getParameter("pi_id"));
			String finYr=General.checknull(request.getParameter("finYr"));
			
			finalResult = TargetAchievementManager.getTargetAchievementDetail(id,finYr);

			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: TargetAchievementService [getUtilizationCertificateDetail]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("TargetAchievementService [getUtilizationCertificateDetail]", e.toString()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void TargetAchievementUploadrequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String jString="";
			
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
			String savemode =General.checknull(request.getParameter("fstatus"));
			

			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getHeader("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			
			TargetAchievementModel TAmodel = new TargetAchievementModel();
			
			Type type = new TypeToken<TargetAchievementModel>() {}.getType(); 
			TAmodel = new Gson().fromJson(obj.toString(), type);
			System.out.println("TAmodel||"+TAmodel.getResPrpsId());
			
			//Getting the resPrpsId for Target/Achivements certificate
			String resPrpsId =General.checknull(TAmodel.getResPrpsId());
		    
		    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
            if (isMultipart) { 
            	 FileItemFactory factory = new DiskFileItemFactory();
	             ServletFileUpload upload = new ServletFileUpload(factory);
	              items = upload.parseRequest(request);
	        }
            
            finalResult=TargetAchievementManager.TargetAchievementUpload(TAmodel,machine, user_id,items, resPrpsId);
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,
					decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: TargetAchievementService [TargetAchievementUploadrequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("TargetAchievementService [TargetAchievementUploadrequest]", e.toString()));
		}
	}
	
}