package com.sits.rsrch.research_activity.fin_year_wise_fund_allocation;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class FundAllocationService
 */

@WebServlet("/FundAllocationService")
public class FundAllocationService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FundAllocationService() {
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
			response.getWriter().append("Served at FundAllocationService: ").append(request.getContextPath());
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
		case "S":
			saverequest(request, response);
			break;
		case "D":
			delete(request, response);
			break;
		case "RP":
			getResearchPropsal(request, response);
			break;
		case "HL":
			getHeadNameList(request, response);
			break;  
		case "amt":
			getInsallMentAmount(request, response);
			break;
		case "getList":
			getListDetails(request, response);
			break;
		case "RD":
			processDeleteDetailsRequest(request,response);
			break;
 					 
		default: System.out.println("Invalid grade FundAllocationService");
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
			finalResult = FundAllocationManager.getResearchProposal(locationCode,ddoCode,request);
			//System.out.println("finalResult||"+finalResult.toString());
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [getResearchPropsal]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [getResearchPropsal]", e.toString()));
		}
	}

	public synchronized void saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			//String errMsg="", flg="N";
			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getParameter("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			
			//String FundAgency = General.checknull((String) obj.get("FundAgency"));
			String PiId = General.checknull((String) obj.get("PiId"));
			String XALLDATE = General.checknull((String) obj.get("XALLDATE"));
			String resPrps = General.checknull((String) obj.get("resPrps"));
			String remark = General.checknull((String) obj.get("remark"));
			String finYr = General.checknull((String) obj.get("finYr"));
			String installId = General.checknull((String) obj.get("installId"));
			String user_id = General.check_null((String) request.getSession().getAttribute("user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("ip"));	
			String work_exp = General.checknull((String) obj.get("workarray"));
			String mode = General.checknull((String) obj.get("mode"));
			String locationCode = General.checknull((String) obj.get("locationCode"));
			String ddoCode = General.checknull((String) obj.get("ddoCode"));

            FundAllocationModel model = new FundAllocationModel();
        	//model.setfAgency(FundAgency);
        	model.setPiid(PiId);
        	model.setDate(XALLDATE);
        	model.setResPrps(resPrps);
        	model.setRemark(remark);
        	model.setFinYr(finYr);
        	model.setLocationCode(locationCode);
        	model.setDdoCode(ddoCode);
        	model.setInstalId(installId);
        	
			JSONArray workobj = (JSONArray)JSONValue.parse(work_exp);				
			String save = "";
			
			if(mode.equals("S")){
				save = FundAllocationManager.save(model, workobj, user_id, machine);
			}
			if(mode.equals("U")){
				//System.out.println(": update service:");
				//save = FundAllocationManager.Update(model, workobj, items, user_id, machine, mode);
			}
        	if(save.equals("1")){
        		finalResult.put("flag", "Y");
        		
        		if(mode.equals("S")){
        			finalResult.put("status",  "Financial Year Wise Fund Allocation Saved Successfully");
        		}else if(mode.equals("U")){
        			finalResult.put("status",  "Financial Year Wise Fund Allocation Update Successfully");
        		}else{
        			finalResult.put("flag", "N");
            		finalResult.put("status", ApplicationConstants.FAIL);
        		}
        	}else{
        		finalResult.put("flag", "N");
        		finalResult.put("status", ApplicationConstants.FAIL);
        	}
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	
        	String jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));        	
        	
        	//out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [saverequest]", e.toString()));
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
			
			delete = FundAllocationManager.delete(id);
			
			if(delete.equals("1")){
				finalResult.put("msg",  "Financial Year Wise Fund Allocation Record delete Successfully");
			}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [delete]", e.toString()));
		}
	}
	
	public synchronized void getHeadNameList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String id=General.checknull(request.getParameter("pi_id"));
			
			finalResult = FundAllocationManager.getHeadNameList(id);

			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [getHeadNameList]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [getHeadNameList]", e.toString()));
		}
	}
	
	
	 
	public synchronized void getListDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{ 
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String rese_id=General.checknull(request.getParameter("resPrps"));
			String installId=General.checknull(request.getParameter("Id"));
			finalResult = FundAllocationManager.getListDetails(rese_id,installId);

			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [getHeadDetils]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [getHeadDetils]", e.toString()));
		}
	}	
	 
	
	public synchronized void getInsallMentAmount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json"); 
			JSONObject finalResult= new JSONObject();
			String hId=General.checknull(request.getParameter("Id"));
			finalResult = FundAllocationManager.getInsallMentAmt(hId);

			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: FundAllocationService [getHeadDetils]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("FundAllocationService [getHeadDetils]", e.toString()));
		}
	}		
	//delete details data
	@SuppressWarnings("unchecked")
	protected void processDeleteDetailsRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//JSONObject returnJSONObj = new JSONObject();
		String   delete = "";
		
		try {
			JSONObject finalResult = new JSONObject();
			String user_id = General.check_null((String) request.getSession().getAttribute("user_id"));
			String id = General.checknull(request.getParameter("id"));
			//System.out.println("id -"+id + " -- "+user_id);
			if (!user_id.equals("")) {
				delete = FundAllocationManager.deleteDetailRecord(id.trim());
			}
			if (delete.equals("")) {
				finalResult.put("status", 0);
				finalResult.put("msg", ApplicationConstants.FAIL);
			} else {
				finalResult.put("status", 1);
				finalResult.put("msg", ApplicationConstants.DELETED);
			}
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-store");
			out.print(finalResult);

		} catch (Exception e) {
			System.out
					.println("ERROR IN FundAllocationService[processDeleteDetailsRequest] IS CAUSED BY" + " " + e.getMessage().toUpperCase());
			Logger.getLogger("usglog")
					.fatal(Logging.logException("FundAllocationService[processDeleteDetailsRequest]", e.getMessage().toString()));
		}
	}

	
}