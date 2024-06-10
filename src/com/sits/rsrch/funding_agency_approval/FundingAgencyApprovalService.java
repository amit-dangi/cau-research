/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.funding_agency_approval;

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
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class RegistrationService
 */
@WebServlet("/FundingAgencyApprovalService")
public class FundingAgencyApprovalService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FundingAgencyApprovalService() {
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
			response.getWriter().append("Served at FundingAgencyApprovalService: ").append(request.getContextPath());
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
		//switch (General.checknull(request.getHeader("fstatus"))){
		switch (General.checknull(request.getParameter("fstatus"))){
		case "SAVE":
			saverequest(request, response);
			break;
		case "DELETE":
			delete(request, response);
			break;
		case "GETPROJECTSTENURE":
			GetProjectTenurebyApprovedProjects(request, response);
			break;
		case "UpdateAmt":
			UpdateAmt(request, response);
			break;
		default: System.out.println("Invalid grade FundingAgencyApprovalService");
		}
	}
	
	public synchronized void saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String errMsg="", flg="N";
			
			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getHeader("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			
			FundingAgencyApprovalModel model = new FundingAgencyApprovalModel();
			
			Type type = new TypeToken<FundingAgencyApprovalModel>() {}.getType(); 
			model = new Gson().fromJson(obj.toString(), type);
			
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("ip"));
		    
		    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
            if (isMultipart) { 
            	 FileItemFactory factory = new DiskFileItemFactory();
	             ServletFileUpload upload = new ServletFileUpload(factory);
	              items = upload.parseRequest(request);
	        }
            
			finalResult = FundingAgencyApprovalManager.save(model, machine, user_id,items);
			
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundingAgencyApprovalService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyApprovalService [saverequest]", e.toString()));
		}
	}
	
	public synchronized void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String delete= "";
			String faaId = request.getParameter("faaId");
			String ip 		= General.check_null((String)request.getSession().getAttribute("s_ip"));
			String user_id 	= General.check_null((String)request.getSession().getAttribute("user_id"));
			/*JSONObject obj = (JSONObject) JSONValue.parse(objJson);
			String faaId = General.checknull((String) obj.get("faaId"));
			*/
			delete = FundingAgencyApprovalManager.delete(faaId,user_id,ip);
			
			if(delete.equals("1")){
				finalResult.put("msg",  "Record Deleted Successfully");
				finalResult.put("flg",  "Y");
			}else if(delete.equals("2")){
        		finalResult.put("msg", ApplicationConstants.DELETE_FORIEGN_KEY);
        		finalResult.put("flg",  "N");
        	}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        		finalResult.put("flg",  "N");
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundingAgencyApprovalService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyApprovalService [delete]", e.toString()));
		}
	}
	
	
	public synchronized void GetProjectTenurebyApprovedProjects(HttpServletRequest request, HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String projectId = General.checknull(request.getParameter("projectId"));
			
			finalResult = FundingAgencyApprovalManager.GetProjectTenurebyApprovedProjects(projectId);
			
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			System.out.println("EXCEPTION CAUSED BY: ContractualEmpProjectMapService [getPorojectsSavedData] :"+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ContractualEmpProjectMapService [getPorojectsSavedData] :", e.toString()));
		}
	}
	
	public synchronized void UpdateAmt(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String errMsg="", flg="N";
			
			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getHeader("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			
			FundingAgencyApprovalModel model = new FundingAgencyApprovalModel();
			
			Type type = new TypeToken<FundingAgencyApprovalModel>() {}.getType(); 
			model = new Gson().fromJson(obj.toString(), type);
			
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("ip"));
		    
			finalResult = FundingAgencyApprovalManager.UpdateAmt(model, machine, user_id);
			
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundingAgencyApprovalService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundingAgencyApprovalService [delete]", e.toString()));
		}
	}
	
	
}