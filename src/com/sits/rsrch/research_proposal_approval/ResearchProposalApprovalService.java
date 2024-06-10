package com.sits.rsrch.research_proposal_approval;

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
 * Servlet implementation class ResearchProposalApprovalService
 */
@WebServlet("/ResearchProposalApprovalService")
public class ResearchProposalApprovalService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger l = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResearchProposalApprovalService() {
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
			response.getWriter().append("Served at ResearchProposalApprovalService: ").append(request.getContextPath());
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
		/*case "D":
			delete(request, response);
			break;*/
		default: System.out.println("Invalid grade ResearchProposalApprovalService");
		}
	}

	public synchronized void saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();			
			String errMsg="", flg="N", save = "";
			
			String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
			String encData	= General.checknull(request.getParameter("encData"));
			String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
			String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);			
			JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
			
			String user_id = General.check_null((String) request.getSession().getAttribute("s_user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
		    String list = General.checknull((String) obj.get("list"));
		    String mode = General.checknull((String) obj.get("type"));
			JSONArray workobj = (JSONArray)JSONValue.parse(list);
			
			if(mode.equals("HD") || mode.equals("RP") || mode.equals("RR") || mode.equals("VC") || mode.equals("DDR")){
				save = ResearchProposalApprovalManager.save(workobj, user_id, machine, mode);
			}else{
				save="";
			}
			
			finalResult.put("type", mode);
        	if(save.equals("1")){
        		finalResult.put("status", 1);
        		if(mode.equals("HD") || mode.equals("RP") || mode.equals("RR") || mode.equals("VC") || mode.equals("DDR")){
        			finalResult.put("msg",  "Research Proposal Approval Saved Successfully");
        		}else if(mode.equals("U")){
        			finalResult.put("msg",  "Research Proposal Approval Updated Successfully");
        		}else{
        			finalResult.put("status", 0);
        			finalResult.put("msg", ApplicationConstants.FAIL);
        		}
        	}else{
        		if(save.equals("3")){
        			finalResult.put("status", 0);
            		finalResult.put("msg", ApplicationConstants.DUPLICATE);
            	}else{
            		finalResult.put("status", 0);
            		finalResult.put("msg", ApplicationConstants.FAIL);
            	}
        	}
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	String jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ResearchProposalApprovalService [saverequest]"+" "+e.getMessage().toUpperCase());
			l.fatal(Logging.logException("ResearchProposalApprovalService [saverequest]", e.toString()));
		}
	}
	
}