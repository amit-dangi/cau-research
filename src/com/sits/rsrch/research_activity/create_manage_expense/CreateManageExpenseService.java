package com.sits.rsrch.research_activity.create_manage_expense;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.google.common.reflect.TypeToken;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


/*import java.io.IOException;
import java.io.PrintWriter;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
*/



/**
 * Servlet implementation class CreateManageExpenseService
 */

@WebServlet("/RESEARCH/CreateManageExpenseService")
public class CreateManageExpenseService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateManageExpenseService(){
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
			response.getWriter().append("Served at CreateManageExpenseService: ").append(request.getContextPath());
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
		switch (General.checknull(request.getParameter("fstatus"))){
		case "RP":
			getResearchPropsal(request, response);
			break;
		case "S":
			processSaveRequest(request, response);
			break;
		case "DELETE":
			processdeleteduploadedfile(request, response);
			break;
		case "SAVECLOSINGAMT":
			processClosingBlncRequest(request, response);
			break;
		default: System.out.println("Invalid grade CreateManageExpenseService");
		}
	}
	
	public synchronized void getResearchPropsal(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String locationCode = General.checknull(request.getParameter("locationCode"));
			String ddoCode = General.checknull(request.getParameter("ddoCode"));
			finalResult = CreateManageExpenseManager.getResearchProposal(locationCode,ddoCode,request);
			
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: CreateManageExpenseService [getResearchPropsal]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("CreateManageExpenseService [getResearchPropsal]", e.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	protected void processSaveRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		try {
			String errMsg = "", flg = "Y", jString = ""; 
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
			String login_id = General.checknull((String) request.getSession().getAttribute("user_id"));			
			String uniqueKey= General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
 			String encData	= General.checknull(request.getHeader("encData"));//get encrypted data from js
 			String decodeData=new String(java.util.Base64.getDecoder().decode(encData)); //decode whole data 
 			String decData=AesUtil.parseAes(encData,aesUtil,uniqueKey); 
			JSONObject dataObj 	= (JSONObject) new JSONParser().parse(decData);//decoded data
			//System.out.println("sanction"+sanction+"purpose"+purpose);
			JSONObject jsonReturn = new JSONObject(); 
			
			if (login_id.equals("")){
				flg = "N";
				errMsg = "Session Expire";
			} else {
				CreateManageExpenseModel model = new CreateManageExpenseModel(); 
				 Type type = new TypeToken<CreateManageExpenseModel>() {
					private static final long serialVersionUID = 1L;
				}.getType();
				model = new Gson().fromJson(dataObj.toString(), type); 
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				//FileItem flItem = null;
				
				List<FileItem> items=null;
	            if (isMultipart) { 
	            	FileItemFactory factory = new DiskFileItemFactory();
	                ServletFileUpload upload = new ServletFileUpload(factory);
	            	items = upload.parseRequest(request);
	            	System.out.println("items"+items.toString());
	            }
	            	            
	            List<CreateManageExpenseModel> heaadlist= model.getSubHeadArray();
	            if(heaadlist.size()>0 && flg.equals("Y")){
	            	int a=0; 
	            	for(CreateManageExpenseModel list:model.getSubHeadArray()){
	            		++a;
            			if(General.checknull(list.getHeadAmt()).equals("")){
		             		flg = "N";
		    				errMsg = "Head Amount is blank in at row :"+a;
		    				break;
		             	}
	            	}
	            }	            

	            if (flg.trim().equals("Y") && (errMsg.trim().equals(""))) {
	            	model = CreateManageExpenseManager.save(model, login_id, machine,items);	
				if (model.valid) {
					flg = "V";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog").debug(Logging.onSucess(login_id, machine, "CreateManageExpenseService[processSaveRequest]", ApplicationConstants.SAVE));
				} else if (!model.valid) {
					flg = "Y";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog").debug(Logging.onSucess(login_id, machine, "CreateManageExpenseService[processSaveRequest]", ApplicationConstants.SAVE));
				} 
	            }
			}
			jsonReturn.put("flg", flg);
			jsonReturn.put("errMsg", errMsg); 			
			jString=aesUtil.encrypt(jsonReturn.toString(),decodeData.split("::")[0],uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		} catch (Exception e) {
			System.out.println("Error in CreateManageExpenseService(processSaveRequest): " + e);
			Logger.getLogger("usglog").fatal(Logging.logException("CreateManageExpenseService", e.getMessage().toString()));
		} finally {
			out.close();
		}
	}
	
	protected void processdeleteduploadedfile(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			String fileFolderName = General.checknull(request.getHeader("fileFolderName"));
			String id = General.checknull(request.getHeader("id"));
			
			JSONObject returnJSONObj = CreateManageExpenseManager.deleteFiles(fileFolderName, id);
			
			out.print(returnJSONObj);
		} catch (Exception e) {
			System.out.println("Exception in CreateManageExpenseService [processdeleteduploadedfile()]" + e.getMessage());
			Logger.getLogger("usglog").fatal(Logging.logException("CreateManageExpenseService[processdeleteduploadedfile]", e.toString()));
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void processClosingBlncRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		try {
			String errMsg = "", flg = "Y", jString = ""; 
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
			String login_id = General.checknull((String) request.getSession().getAttribute("user_name"));			
			String uniqueKey= General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
 			String encData	= General.checknull(request.getHeader("encData"));//get encrypted data from js
 			String decodeData=new String(java.util.Base64.getDecoder().decode(encData)); //decode whole data 
 			String decData=AesUtil.parseAes(encData,aesUtil,uniqueKey); 
			JSONObject dataObj 	= (JSONObject) new JSONParser().parse(decData);//decoded data
			JSONObject jsonReturn = new JSONObject(); 
			
			if (login_id.equals("")){
				flg = "N";
				errMsg = "Session Expire";
			} else {
				CreateManageExpenseModel model = new CreateManageExpenseModel(); 
				 Type type = new TypeToken<CreateManageExpenseModel>() {
					private static final long serialVersionUID = 1L;
				}.getType();
				model = new Gson().fromJson(dataObj.toString(), type); 
	            	            
	            if (flg.trim().equals("Y") && (errMsg.trim().equals(""))) {
	            	model = CreateManageExpenseManager.saveClosingBlnc(model, login_id, machine);	
				if (model.valid) {
					flg = "V";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog").debug(Logging.onSucess(login_id, machine, "CreateManageExpenseService[processClosingBlncRequest]", ApplicationConstants.SAVE));
				} else if (!model.valid) {
					flg = "Y";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog").debug(Logging.onSucess(login_id, machine, "CreateManageExpenseService[processClosingBlncRequest]", ApplicationConstants.SAVE));
				} 
	            }
			}
			jsonReturn.put("flg", flg);
			jsonReturn.put("errMsg", errMsg); 			
			jString=aesUtil.encrypt(jsonReturn.toString(),decodeData.split("::")[0],uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		} catch (Exception e) {
			System.out.println("Error in CreateManageExpenseService(processClosingBlncRequest): " + e);
			Logger.getLogger("usglog").fatal(Logging.logException("CreateManageExpenseService", e.getMessage().toString()));
		} finally {
			out.close();
		}
	}
	
}