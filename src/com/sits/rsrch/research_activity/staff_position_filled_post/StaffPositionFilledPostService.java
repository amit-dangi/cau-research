package com.sits.rsrch.research_activity.staff_position_filled_post;

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
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sits.common.AesUtil;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class StaffPositionFilledPostService
 */
@WebServlet("/StaffPositionFilledPostService")
public class StaffPositionFilledPostService extends HttpServlet {
	static Logger log = Logger.getLogger("exceptionlog research activity StaffPositionFilledPostService");
	private static final long serialVersionUID = 1L;
	AesUtil aesUtil = new AesUtil(128,10);   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StaffPositionFilledPostService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			processRequest(request, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.getWriter().append("Served at StaffPositionFilledPostService: ").append(request.getContextPath());
		}	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		System.out.println("in");
		switch (General.checknull(request.getParameter("fstatus"))){
		case "SAVE":
			Saverequest(request, response);
			break;
		case "DETAILS":
			getPositionFilledDetail(request, response);
			break;
		default: System.out.println("Invalid grade StaffPositionFilledPostService");
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void Saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String jString="";
			
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
		//	System.out.println("user_id-"+user_id);
			String savemode =General.checknull(request.getParameter("fstatus"));
			

			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getHeader("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			//System.out.println("obj:::"+obj);
			
			StaffPositionFilledPostModel model = new StaffPositionFilledPostModel(); 
			 Type type = new TypeToken<StaffPositionFilledPostModel>() {
				private static final long serialVersionUID = 1L;
			}.getType();
			model = new Gson().fromJson(obj.toString(), type); 
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			FileItem flItem = null;
			List<FileItem> items=null;
		//	System.out.println("isMultipart:::"+isMultipart);
           if (isMultipart) { 
           	FileItemFactory factory = new DiskFileItemFactory();
               ServletFileUpload upload = new ServletFileUpload(factory);
           	   items = upload.parseRequest(request); 
           }
            
            finalResult=StaffPositionFilledPostManager.StaffPositionFilledSave(model,machine, user_id,items);
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	
        	jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,
					decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: StaffPositionFilledPostService [TargetAchievementUploadrequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("StaffPositionFilledPostService [TargetAchievementUploadrequest]", e.toString()));
		}
	}
	
	/*Void Method to get the getTargetAchievementDetail saved details or
	get the selected proposal details from Project Proposal Submission form as per proposal id*/
	public synchronized void getPositionFilledDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			JSONArray arr = new JSONArray();
			String id=General.checknull(request.getParameter("pi_id"));
			
			finalResult = StaffPositionFilledPostManager.getSanctionPostDetail(id);
			arr = StaffPositionFilledPostManager.getSaveDetail(id);

			finalResult.put("list", arr);		
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: StaffPositionFilledPostService [getUtilizationCertificateDetail]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("StaffPositionFilledPostService [getUtilizationCertificateDetail]", e.toString()));
		}
	}

}
