/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.form1_project_submission;

import java.io.IOException;
import java.io.PrintWriter;
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

import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class RegistrationService
 */
@WebServlet("/ProjectSubmissionService")
public class ProjectSubmissionService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectSubmissionService() {
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
			response.getWriter().append("Served at ProjectSubmissionService: ").append(request.getContextPath());
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
		case "S":
			saverequest(request, response);
			break;
		case "D":
			delete(request, response);
			break;
		case "2":
			deleteattachfile(request, response);
			break;
		case "EXT":
			saveExtensionrequest(request, response);
			break;
		default: System.out.println("Invalid grade ProjectSubmissionService");
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
			
			String PiName = General.checknull((String) obj.get("PiName"));
			String dept = General.checknull((String) obj.get("dept"));
			String locationCode = General.checknull((String) obj.get("locationCode"));
			String ddoCode = General.checknull((String) obj.get("ddoCode"));
			String desg = General.checknull((String) obj.get("desg"));
			String projPropsal = General.checknull((String) obj.get("projPropsal"));
			String durPropProj = General.checknull((String) obj.get("durPropProj"));
			String totalBudgProp = General.checknull((String) obj.get("totalBudgProp"));
			String FundAgency = General.checknull((String) obj.get("FundAgency"));
			String nameAddrCoPi = General.checknull((String) obj.get("nameAddrCoPi"));
			String XTODATE = General.checknull((String) obj.get("XTODATE"));
			String proj_start_date = General.checknull((String) obj.get("proj_start_date"));
			String nonRecCost = General.checknull((String) obj.get("nonRecCost"));
			String chemAndCon = General.checknull((String) obj.get("chemAndCon"));
			String manpower = General.checknull((String) obj.get("manpower"));
			String contingency = General.checknull((String) obj.get("contingency"));
			String travel = General.checknull((String) obj.get("travel"));
			String outSourcingCharge = General.checknull((String) obj.get("outSourcingCharge"));
			String overCharg = General.checknull((String) obj.get("overCharg"));
			String projPropSub = General.checknull((String) obj.get("projPropSub"));
			String projPropClear = General.checknull((String) obj.get("projPropClear"));
			String necClearObt = General.checknull((String) obj.get("necClearObt"));
			String finCommitUni = General.checknull((String) obj.get("finCommitUni"));
			String AttchCertif = General.checknull((String) obj.get("AttchCertif"));
			String finCommitUniDetails = General.checknull((String) obj.get("finCommitUniDetails"));
			String id = General.checknull((String) obj.get("fId"));
			String ppId = General.checknull((String) obj.get("ppId"));
		    //String user_id = General.check_null((String) request.getSession().getAttribute("s_user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));	
			String work_exp = General.checknull((String) obj.get("workarray"));
			String mode = General.checknull((String) obj.get("mode"));
			String projPropsalIdManual = General.checknull((String) obj.get("projPropsalIdManual"));
			String durPropProjYear = General.checknull((String) obj.get("durPropProjYear"));
			String previous_pi_Name= General.checknull((String) obj.get("previous_pi_Name"));
			String applied_date= General.checknull((String) obj.get("applied_date"));
			
            ProjectSubmissionModel model = new ProjectSubmissionModel();
            model.setProjtype(General.checknull((String) obj.get("projtype")));
            model.setErptype(General.checknull((String) obj.get("erptype")));
            model.setProjterm(General.checknull((String) obj.get("projterm")));
            model.setIsApprovalReq(General.checknull((String) obj.get("IsApprovalReq")));
            
            model.setFn_agency(General.checknull((String) obj.get("fn_agency")));
            model.setProj_obj(General.checknull((String) obj.get("proj_obj")));
            
            model.setThrust_area(General.checknull((String) obj.get("thrust_area")));
            model.setSub_thrust_area(General.checknull((String) obj.get("sub_thrust_area")));
            model.setRetirePiName(General.checknull((String) obj.get("retirePiName")));
            model.setBudgetHeads(General.checknull((String) obj.get("budgetHeads")));
            
            model.setInst_charges(General.checknull((String) obj.get("inst_charges")));
            model.setProjunder(General.checknull((String) obj.get("projunder")));
            
            String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
            System.out.println("General.checknull((String) obj.get|||||||"+General.checknull((String) obj.get("retirePiName")));
        	model.setPiName(PiName);
        	model.setDept(dept);
        	model.setDesg(desg);
        	model.setProjPropsal(projPropsal);
        	model.setDurPropProj(durPropProj);
			model.setTotalBudgProp(totalBudgProp);
			model.setFundAgency(FundAgency);
			model.setNameAddrCoPi(nameAddrCoPi);
			model.setXTODATE(XTODATE);
			model.setProj_start_date(proj_start_date);
			model.setNonRecCost(nonRecCost);
			model.setChemAndCon(chemAndCon);
			model.setManpower(manpower);
			model.setContingency(contingency);
			model.setTravel(travel);
			model.setOutSourcingCharge(outSourcingCharge);
			model.setOverCharg(overCharg);		    
			model.setProjPropSub(projPropSub);
			model.setProjPropClear(projPropClear);
			model.setNecClearObt(necClearObt);
			model.setFinCommitUni(finCommitUni);
			model.setAttchCertif(AttchCertif);
			model.setfId(id);
		    model.setPpId(ppId);
		    model.setFinCommitUniDetails(finCommitUniDetails);
		    model.setLocationCode(locationCode);
		    model.setDdoCode(ddoCode);
		    model.setProjPropsalIdManual(projPropsalIdManual);
		    model.setDurPropProjYear(durPropProjYear);
		    model.setPrevious_pi_Name(previous_pi_Name);
		    model.setApplied_date(applied_date);
		    
		    
		    JSONArray workobj = (JSONArray)JSONValue.parse(work_exp);		
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
            if (isMultipart) { 
            	 FileItemFactory factory = new DiskFileItemFactory();
	             ServletFileUpload upload = new ServletFileUpload(factory);
	              items = upload.parseRequest(request);
	        }
			String save = "";
			if(mode.equals("S") || mode.equals("SS")){
				save = ProjectSubmissionManager.save(model, workobj, items, user_id, machine, mode);
			}
			if(mode.equals("U") || mode.equals("US")){
				save = ProjectSubmissionManager.Update(model, workobj, items, user_id, machine, mode);
			}
			//System.out.println("save||"+save);
        	if(save.equals("1")){
        		finalResult.put("status", 1);
        		if(mode.equals("SS") || mode.equals("US")){
        			finalResult.put("msg",  "Project Proposal Submitted Successfully");	
        		}else if(mode.equals("S")){
        			finalResult.put("msg",  "Project Proposal Saved Successfully");
        		}else if(mode.equals("U")){
        			finalResult.put("msg",  "Project Proposal Updated Successfully");
        		}else{
        			finalResult.put("status", 0);
            		finalResult.put("msg", ApplicationConstants.FAIL);
        		}
        	}else if(save.contains("Duplicate")){
        		finalResult.put("status", 0);
        		finalResult.put("msg", ApplicationConstants.DUPLICATE);
        	}else{
        		finalResult.put("status", 0);
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectSubmissionService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectSubmissionService [saverequest]", e.toString()));
		}
	}
	
	public synchronized void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String delete= "";
			String objJson = request.getParameter("jsonObject");
			JSONObject obj = (JSONObject) JSONValue.parse(objJson);
			String id = General.checknull((String) obj.get("id"));
			
			delete = ProjectSubmissionManager.delete(id);
			
			if(delete.equals("1")){
				finalResult.put("msg",  "Project Proposal deleted Successfully");
			}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectSubmissionService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectSubmissionService [delete]", e.toString()));
		}
	}

	public synchronized void deleteattachfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String aid		= General.checknull(request.getParameter("attid"));
			String filename	= General.checknull(request.getParameter("filename"));
			String mastid	= General.checknull(request.getParameter("mastid"));
        	
			String delete=ProjectSubmissionManager.deletattchdata(aid, filename, mastid);
			
			if(delete.equals(""))
	        {
	           		finalResult.put("status", 0);
		    		finalResult.put("msg", ApplicationConstants.FAIL);
	        }
	        else
	        {
	           		finalResult.put("status", 1);
		    		finalResult.put("msg", "File Deleted Successfully");
	        }
	        response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectSubmissionService[deleteattachfile]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectSubmissionService[deleteattachfile]", e.toString()));
		}
	}
	
	public synchronized void saveExtensionrequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String save= "";
			String objJson = request.getParameter("jsonObject");
			JSONObject obj = (JSONObject) JSONValue.parse(objJson);
			String id = General.checknull((String) obj.get("id"));
			String projET = General.checknull((String) obj.get("projET"));
			String extdurPropProjYear = General.checknull((String) obj.get("extdurPropProjYear"));
			String extdurPropProjMonth = General.checknull((String) obj.get("extdurPropProjMonth"));
			String user_id = General.check_null((String) request.getSession().getAttribute("s_user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));	
		    
			ProjectSubmissionModel model = new ProjectSubmissionModel();
			model.setfId(id);
			model.setProjET(projET);
			model.setExtdurPropProjYear(extdurPropProjYear);
			model.setExtdurPropProjMonth(extdurPropProjMonth);
			save = ProjectSubmissionManager.saveExtensionrequest(model,user_id,machine);
			
			if(save.equals("1")){
				finalResult.put("msg",  "Form 1 extended Successfully");
			}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectSubmissionService [saveExtensionrequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectSubmissionService [saveExtensionrequest]", e.toString()));
		}
	}
	
}