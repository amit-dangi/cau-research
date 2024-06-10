/**
 * @ AUTHOR Amit Dangi
 */
package com.sits.rsrch.research_activity.fin_year_wise_utilization_certificate;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.Iterator;
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
import com.sits.conn.DBConnection;
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.rsrch.research_activity.fin_year_wise_fund_allocation.FundAllocationManager;

/**
 * Servlet implementation class UtilizationCertificateService
 */

@WebServlet("/UtilizationCertificateService")
public class UtilizationCertificateService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog research activity UtilizationCertificateService");
	AesUtil aesUtil = new AesUtil(128,10);
	
    
    public UtilizationCertificateService() {
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
			response.getWriter().append("Served at UtilizationCertificateService: ").append(request.getContextPath());
		}		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		switch (General.checknull(request.getParameter("fstatus"))){
		case "UPLOAD":
			certificateUploadrequest(request, response);
			break;
		case "DETAILS":
			getUtilizationCertificateDetail(request, response);
			break;
		default: System.out.println("Invalid grade UtilizationCertificateService");
		}
	}
	
	/*Void Method to get the utilization certificate saved details if any or
	get the selected proposal details from Project Proposal Submission form as per proposal id*/
	public synchronized void getUtilizationCertificateDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			String id=General.checknull(request.getParameter("pi_id"));
			String fin_year=General.checknull(request.getParameter("fin_year"));
			finalResult = UtilizationCertificateManager.getUtilizationCertificateDetail(id,fin_year);

			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: UtilizationCertificateService [getUtilizationCertificateDetail]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("UtilizationCertificateService [getUtilizationCertificateDetail]", e.toString()));
		}
	}
	
	@SuppressWarnings("unchecked")
	public synchronized void certificateUploadrequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));
			System.out.println("user_id-"+user_id);
			String savemode =General.checknull(request.getParameter("fstatus"));
			
			//Getting the resPrpsId for upload certificate
			String resPrpsId =General.checknull(request.getParameter("resPrpsId"));
			String uc_certname =General.checknull(request.getParameter("uc_certname"));
			String auc_certname =General.checknull(request.getParameter("auc_certname"));
			String finYr =General.checknull(request.getParameter("finYr"));
			String uc_id=General.checknull(request.getParameter("uc_id"));
			UtilizationCertificateModel UCmodel = new UtilizationCertificateModel();
			UCmodel.setResPrpsId(resPrpsId);
			UCmodel.setUc_certname(uc_certname);
			UCmodel.setAuc_certname(auc_certname);
			UCmodel.setFin_year(finYr);
			UCmodel.setUc_id(uc_id);
			System.out.println("savemode-"+savemode+" formId-"+resPrpsId+"||uc_certname||"+uc_certname+"|auc_certname|"+auc_certname);
		    
		    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
            if (isMultipart) { 
            	 FileItemFactory factory = new DiskFileItemFactory();
	             ServletFileUpload upload = new ServletFileUpload(factory);
	              items = upload.parseRequest(request);
	        }
            if(items!=null){
				/* code commented as new table Introduce
				 * Iterator<FileItem> iter = items.iterator();
					while (iter.hasNext()) {
						FileItem fileItem = iter.next();
						System.out.println("fileItem||"+fileItem.toString());
						finalResult=UtilizationCertificateManager.Uploaddoc(machine, UCmodel, user_id, fileItem);
					}*/
					//New method created for adding new table dependency
					finalResult=UtilizationCertificateManager.UtilizationCertificateUpload(UCmodel,machine,user_id, items);
            }
        	response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: UtilizationCertificateService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("UtilizationCertificateService [saverequest]", e.toString()));
		}
	}
	
}