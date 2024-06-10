/**
 * @ AUTHOR Chetan Sharma
 */
package com.sits.rsrch.funding_agency;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sits.common.AesUtil;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;

@WebServlet("/FundingAgencyService")
public class FundingAgencyService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger l = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String formStatus=General.checknull(request.getParameter("fstatus").trim().toUpperCase());
		if(formStatus.equals("D")){
			processDeleteRequest(request, response);
		} else {
			processRequest(request, response);
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = null;
		try {
			String errMsg = "", flag = "Y";
			
			String uniqueKey= General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getParameter("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData,aesUtil,uniqueKey);
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			//System.out.println("dataObj :"+dataObj);			
			//String objJson = request.getParameter("jsonObject");
			//JSONObject obj = (JSONObject) JSONValue.parse(objJson);
			
			String faName = General.checknull((String) obj.get("faName")).trim();
			String faType = General.checknull((String) obj.get("faType")).trim();
			String faMobNo = General.checknull((String) obj.get("faMobNo"));
			String fstatus = General.checknull(((String) request.getParameter("fstatus")));
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
			
			Type type = new TypeToken<FundingAgencyModel>() {}.getType(); 
			FundingAgencyModel faModel = new Gson().fromJson(obj.toString(), type);
			if (user_id.equals("")) {
				errMsg = "Session Expire.";
				flag = "N";
			} else if (!((fstatus.equals("N")) || (fstatus.equals("E")))) {
				flag = "N";
				errMsg = "Invalid Form Status";				
			}
			
			if (flag.trim().equals("Y") && errMsg.trim().equals("") && !fstatus.equals("D")) {
				if(faName.trim().equals("")){
					errMsg = "Funding Agency Name is required.";
					flag = "N";
				}else if(faType.trim().equals("")){
					errMsg = "Funding Agency Type is required.";
					flag = "N";
				}else if(faMobNo.trim().equals("")){
					errMsg = "Contact No is required.";
					flag = "N";
				}else if(!faMobNo.trim().equals("")){
					if(!(faMobNo.length() == 11 || faMobNo.length() == 10)){
						errMsg = "Invalid Contact No.";
						flag = "N";
					}
				}
			}
			
			if (flag.trim().equalsIgnoreCase("Y") && errMsg.trim().equals("") && !fstatus.equals("D")) {
				if (fstatus.equals("N")) {
					jsonObject = FundingAgencyManager.save(faModel, machine, user_id);
				}
				if (fstatus.equals("E")) {
					jsonObject = FundingAgencyManager.update(faModel, machine,user_id);
				}
				/*if (fstatus.equals("D")) {
					jsonObject = FundingAgencyManager.delete(faModel, machine, user_id);
				}*/
			}else{
				JSONObject Obj = new JSONObject();
				Obj.put("status", errMsg);
				Obj.put("flag", "N");
				jsonObject=Obj;
			}
			
			String jString = aesUtil.encrypt(jsonObject.toString(), decodeData.split("::")[0], uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));		
			//out.write(jsonObject.toString());
			Logger.getLogger("usglog").debug(Logging.onSucess(user_id, machine, "FundingAgencyService", ApplicationConstants.SUCCESS));
		} catch (Exception e) {
			System.out.println("ERROR IN SERVICE IS CAUSED BY FundingAgencyService :"+e.getMessage().toUpperCase());
			e.printStackTrace();
			l.fatal(Logging.logException("FundingAgencyService", e.toString()));
		} finally {
			out.close();
		}
	}
	
	protected void processDeleteRequest(HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String errMsg="";
		JSONObject jsonObject = null;
		try{
			String ip 		= General.check_null((String)request.getSession().getAttribute("s_ip"));
			String user_id 	= General.check_null((String)request.getSession().getAttribute("user_id"));
			String faId 	= General.checknull(request.getParameter("faId"));
			
			if(!user_id.equals("")) {
				jsonObject = FundingAgencyManager.delete(faId, ip, user_id);	
			}else{
				JSONObject Obj = new JSONObject();
				Obj.put("status", errMsg);
				Obj.put("flag", "N");
				jsonObject=Obj;
			}			
			out.println(jsonObject.toString());			
		}catch(Exception e){
			System.out.println("ERROR IN ItemLocService[delete] IS CAUSED BY"+" "+e.getMessage().toUpperCase());
			Logger.getLogger("usglog").fatal(Logging.logException("ItemLocService[delete]", e.getMessage().toString()));
		}
	}
	
}