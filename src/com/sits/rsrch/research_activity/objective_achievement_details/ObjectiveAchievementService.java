package com.sits.rsrch.research_activity.objective_achievement_details;

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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;



/**
 * Servlet implementation class PrintingCourseMaterialService
 */
@WebServlet("/RESEARCH/ObjectiveAchievementService")
public class ObjectiveAchievementService extends HttpServlet {
	private static final long serialVersionUID = 1L;
 	AesUtil aesUtil = new AesUtil(128,10);  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ObjectiveAchievementService() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String formStatus=General.checknull(request.getHeader("fstatus"));

 
		if(formStatus.equals("N") || formStatus.equals("E")){
			processRequest(request,response);
		}
		else if(formStatus.equals("UD")){
			try {
				deleteattachfile(request,response);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		else if(formStatus.equals("D")){
			processDeleteRequest(request,response);
		} 
	}
	protected void processRequest(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException
	{
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out=response.getWriter();
		try
		{
			String errMsg="",flg="Y";
			boolean flag=false;
			
			JSONObject returnJSONObj = new JSONObject();
			String fstatus 	= General.check_null(request.getHeader("fstatus")).trim();
			String uniqueKey= General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getHeader("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData,aesUtil,uniqueKey);
			
			JSONObject dataObj 	= (JSONObject) new JSONParser().parse(decData);
			
			String ip 		=General.check_null((String)request.getSession().getAttribute("ip"));
			String user_id 	= General.checknull((String)request.getSession().getAttribute("user_id"));

		    String rsrch_proposal 	= General.checknull((String)dataObj.get("rsrch_proposal")).trim();
		    String from_date 	= General.checknull((String)dataObj.get("from_date")).trim();
		    String to_date 	    = General.checknull((String)dataObj.get("to_date")).trim();
		    String achievement_det 	= General.checknull((String)dataObj.get("achievement_det")).trim();
		    String is_doc_uploaded 	= General.checknull((String)dataObj.get("is_doc_uploaded")).trim();
			String oa_id = General.checknull((String)dataObj.get("oa_id")).trim();

			ObjectiveAchievementModel model   = new ObjectiveAchievementModel();
			model.setRsrch_proposal(rsrch_proposal);
			model.setFrom_date(from_date);
			model.setTo_date(to_date);
			model.setAchievement_det(achievement_det);
			model.setOa_id(oa_id);
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
			
            if (isMultipart) { 
            	FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
            	items = upload.parseRequest(request); 
        
            }
			if(user_id.equals("")) {
				errMsg="Session Expire";
    			flg="N";
			} else if(!((fstatus.equals("N")) || (fstatus.equals("E")))) {
				errMsg="Invalid Entry Type";
				flg="N";
			} 
	
		
		
        String jString="";
		if((flg.trim().equalsIgnoreCase("Y"))&&(errMsg.trim().equals(""))){
				Type type = new TypeToken<ObjectiveAchievementModel>() {
				}.getType();
				model = new Gson().fromJson(dataObj.toString(), type);
				
			flag=ObjectiveAchievementManager.getRecordExist(model);
		if(!flag){
			if(fstatus.equals("N")){
					model = ObjectiveAchievementManager.save(model, user_id, ip,items);	
					if(model.isValid()) {
						flg="V";
						errMsg=model.getErrMsg();
			    				Logger.getLogger("usglog").debug(Logging.onSucess("",""," ObjectiveAchievementService", model.getErrMsg()));
							} else {
								flg="I";
								errMsg=model.getErrMsg();
			    				Logger.getLogger("usglog").debug(Logging.onSucess("","","ObjectiveAchievementService", model.getErrMsg()));
						}
		} 
		}
		else {
			flg="I";
			errMsg="Record Already Exists Between these Dates at Selected Proposal!";
			Logger.getLogger("usglog").debug(Logging.onSucess("","","ObjectiveAchievementService", model.getErrMsg()));
	}
		if(fstatus.equals("E")) {
		//	System.out.println("hello service");
			model = ObjectiveAchievementManager.updateRecord(model, user_id, ip,items);
			if(model.isValid()) {
				flg="V";
				errMsg=model.getErrMsg();
	    				Logger.getLogger("usglog").debug(Logging.onSucess("",""," ObjectiveAchievementService", model.getErrMsg()));
					} else {
						flg="I";
						errMsg=model.getErrMsg();
	    				Logger.getLogger("usglog").debug(Logging.onSucess("","","ObjectiveAchievementService", model.getErrMsg()));
				}
		} 
		}
			returnJSONObj.put("flg", flg);
			returnJSONObj.put("errMsg", errMsg);
			returnJSONObj.put("fstatus", fstatus);
		//	System.out.println("returnJSONObj>>>"+returnJSONObj);
			jString=aesUtil.encrypt(returnJSONObj.toString(),decodeData.split("::")[0],uniqueKey,decodeData.split("::")[1]);
			
			out.println(new Gson().toJson(jString));
		}catch (Exception ex) {
        	System.out.println("Error in ObjectiveAchievementService : "+ex.getMessage());
        	Logger.getLogger("usglog").fatal(Logging.logException("ObjectiveAchievementService", ex.getMessage().toString()));
        } finally {
        	out.close();
        }

	}
	
	public synchronized void deleteattachfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, 
	JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String upld_id		= General.checknull(request.getHeader("upld_id"));
			String upld_name	= General.checknull(request.getHeader("upld_name"));
			
			String user_id = General.check_null((String) request.getSession().getAttribute("user_id"));
			String delete=""; 
			if(!user_id.equals(""))
    	 delete=ObjectiveAchievementManager.deletattchdata(upld_id,upld_name);
			//System.out.println("delete::"+delete);
			if(delete.equals(""))
	        {
         		finalResult.put("status", 0);
	    		finalResult.put("msg", ApplicationConstants.FAIL);
	        }
	        else
	        {
         		finalResult.put("status", 1);
	    		finalResult.put("msg", ApplicationConstants.DELETED);
	        }
	        response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ObjectiveAchievementService[deleteattachfile]"+" "+e.getMessage().toUpperCase());
			Logger.getLogger("usglog").fatal(Logging.logException("ObjectiveAchievementService[deleteattachfile]", e.getMessage().toString()));
		}
	}
	protected void processDeleteRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject returnJSONObj = new JSONObject();
		String flg = "", jString = "", errMsg = "";
		try {
			String ip = General.checknull((String) request.getSession().getAttribute("s_ip"));
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String fstatus	= General.checknull(request.getHeader("fstatus"));
			String upl_doc 		= General.checknull(request.getHeader("upl_doc"));
		//	String fstatus = General.checknull(request.getParameter("fstatus"));
			String oa_id = General.checknull(request.getHeader("oa_id"));
		//	System.out.println("upl_doc service--------"+upl_doc);
			if (!user_id.equals("")) {
				ObjectiveAchievementModel model = ObjectiveAchievementManager.deleteRecord(oa_id.trim(),upl_doc.trim());
				if (model.isValid()) {
					flg = "V";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, ip, "ObjectiveAchievementService[[delete]]", model.getErrMsg()));
				} else {
					flg = "I";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, ip, "ObjectiveAchievementService[[delete]]", model.getErrMsg()));
				}
			} else {
				errMsg = "session expire!";
				flg = "N";
			}
			returnJSONObj.put("flg", flg);
			returnJSONObj.put("errMsg", errMsg);
			returnJSONObj.put("fstatus", fstatus);
		//	System.out.println("returnJSONObj--------"+returnJSONObj);
			jString = JSONObject.toJSONString(returnJSONObj);
			out.println(jString);
		} catch (Exception e) {
			System.out
					.println("ERROR IN ObjectiveAchievementService[delete] IS CAUSED BY" + " " + e.getMessage().toUpperCase());
			Logger.getLogger("usglog")
					.fatal(Logging.logException("ObjectiveAchievementService[delete]", e.getMessage().toString()));
		}
	}

}

