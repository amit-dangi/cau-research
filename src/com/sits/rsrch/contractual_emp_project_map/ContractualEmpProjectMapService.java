package com.sits.rsrch.contractual_emp_project_map;

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
 * Servlet implementation class ContractualEmpProjectMapService
 */
@WebServlet("/ContractualEmpProjectMapService")
public class ContractualEmpProjectMapService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ContractualEmpProjectMapService() {
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
			response.getWriter().append("Served at ContractualEmpProjectMapService :").append(request.getContextPath());
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
			getSavedData(request, response);
			break;
		case "GETPROJECTS":
			getPorojectsSavedData(request, response);
			break;
		
		default: System.out.println("Invalid grade ContractualEmpProjectMapService");
		}
	}
	
	public synchronized void saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getParameter("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			String work_exp = General.checknull((String) obj.get("workarray"));
			JSONArray workobj = (JSONArray)JSONValue.parse(work_exp);
			String projId = General.checknull((String) obj.get("proj"));
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
		    String locationCode = General.checknull((String) obj.get("locationCode"));
		    String ddoCode = General.checknull((String) obj.get("ddoCode"));
		    String natureType = General.checknull(obj.get("natureType").toString());
		    String mode = General.checknull((String) obj.get("mode"));
			String save = "";
			if(mode.equals("S")){
				save = ContractualEmpProjectMapManager.save(projId, workobj, user_id, machine,locationCode,ddoCode,natureType);
			}
			if(mode.equals("U")){
				//String proj = General.checknull((String) obj.get("proj"));
				//String cEmp = General.checknull((String) obj.get("cEmp"));
				String id = General.checknull((String) obj.get("id"));

				save = ContractualEmpProjectMapManager.Update(projId, workobj, id, user_id, machine, mode);
			}
			
        	if(save.equals("1")){
        		//finalResult.put("status", 1);
        		if(mode.equals("S")){
        			finalResult.put("status",  "Employee & Project Mapping Save Successfully");
        			finalResult.put("flag", "Y");
        		}else if(mode.equals("U")){
        			finalResult.put("status",  "Employee & Project Mapping Save Successfully");
        			finalResult.put("flag", "Y");
        		}else{
        			finalResult.put("flag", "N");
            		finalResult.put("status", ApplicationConstants.FAIL);
        		}
        	}else if(save.equals("5")){
        		finalResult.put("flag", "N");
        		finalResult.put("status", ApplicationConstants.DUPLICATE);
        	}else{
        		finalResult.put("flag", "N");
        		finalResult.put("status", ApplicationConstants.FAIL);
        	}
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	//out.print(finalResult);
        	String jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));        	
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ContractualEmpProjectMapService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ContractualEmpProjectMapService [saverequest]", e.toString()));
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
			
			delete = ContractualEmpProjectMapManager.delete(id);
			
			if(delete.equals("1")){
				finalResult.put("msg",  "Employee & Project Mapping deleted Successfully");
			}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ContractualEmpProjectMapService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ContractualEmpProjectMapService [delete]", e.toString()));
		}
	}

	public synchronized void getSavedData(HttpServletRequest request, HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String id = General.checknull(request.getParameter("id"));
			finalResult = ContractualEmpProjectMapManager.getSavedData(id);
			
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			System.out.println("EXCEPTION CAUSED BY: ContractualEmpProjectMapService [getSavedData] :"+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ContractualEmpProjectMapService [getSavedData] :", e.toString()));
		}
	}
	
	public synchronized void getPorojectsSavedData(HttpServletRequest request, HttpServletResponse response){
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String locationCode = General.checknull(request.getParameter("locationCode"));
			String ddoCode = General.checknull(request.getParameter("ddoCode"));
			
			finalResult = ContractualEmpProjectMapManager.getPorojectsSavedData(locationCode,ddoCode,request);
			
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			System.out.println("EXCEPTION CAUSED BY: ContractualEmpProjectMapService [getPorojectsSavedData] :"+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ContractualEmpProjectMapService [getPorojectsSavedData] :", e.toString()));
		}
	}

}