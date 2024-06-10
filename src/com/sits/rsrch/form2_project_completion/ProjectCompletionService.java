package com.sits.rsrch.form2_project_completion;

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
 * Servlet implementation class ProjectCompletionService
 */

@WebServlet("/ProjectCompletionService")
public class ProjectCompletionService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128,10);
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectCompletionService() {
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
			response.getWriter().append("Served at ProjectCompletionService: ").append(request.getContextPath());
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
		case "2":
			deleteattachfile(request, response);
			break;
		case "3":
			getEmployee(request, response);
			break;
		default: System.out.println("Invalid grade ProjectCompletionService");
		}
	}
	
	public synchronized void getEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
		// TODO Auto-generated method stub
		try{
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			JSONObject finalResult= new JSONObject();
			
			finalResult = ProjectCompletionManager.getEmployee(request);
			
			response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectCompletionService [getEmployee]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectCompletionService [getEmployee]", e.toString()));
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
			
			String piId = General.checknull((String) obj.get("piId"));
			String dept = General.checknull((String) obj.get("dept"));
			String desg = General.checknull((String) obj.get("desg"));
			String XTODATE = General.checknull((String) obj.get("XTODATE"));
			String RelvInfo = General.checknull((String) obj.get("RelvInfo"));
			String piAccClose = General.checknull((String) obj.get("piAccClose"));
			String blncRefFundAgn = General.checknull((String) obj.get("blncRefFundAgn"));
			String submToFundAgn = General.checknull((String) obj.get("submToFundAgn"));
			String submToProjCell = General.checknull((String) obj.get("submToProjCell"));
			String techRepsubmToFundAgn = General.checknull((String) obj.get("techRepsubmToFundAgn"));
			String mode = General.checknull((String) obj.get("mode"));
			String id = General.checknull((String) obj.get("fId"));		    
			String work_exp = General.checknull((String) obj.get("workarray"));
			String locationCode = General.checknull((String) obj.get("locationCode"));		    
			String ddoCode = General.checknull((String) obj.get("ddoCode"));
			String user_id = General.check_null((String) request.getSession().getAttribute("s_user_id"));
		    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));	

			ProjectCompletionModel model = new ProjectCompletionModel();
        	model.setPiId(piId);
        	model.setDept(dept);
        	model.setDesg(desg);
        	model.setXTODATE(XTODATE);
        	model.setRelvInfo(RelvInfo);
			model.setPiAccClose(piAccClose);
			model.setBlncRefFundAgn(blncRefFundAgn);
			model.setSubmToFundAgn(submToFundAgn);
			model.setSubmToProjCell(submToProjCell);
			model.setTechRepsubmToFundAgn(techRepsubmToFundAgn);			
			model.setId(id);
			model.setUser_id(user_id);
			model.setMachine(machine);
			model.setLocationCode(locationCode);
			model.setDdoCode(ddoCode);
			
			JSONArray workobj = (JSONArray)JSONValue.parse(work_exp);			
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			List<FileItem> items=null;
            if (isMultipart) { 
            	 FileItemFactory factory = new DiskFileItemFactory();
	             ServletFileUpload upload = new ServletFileUpload(factory);
	              items = upload.parseRequest(request);
	        }
			String save = "";
			if(mode.equals("S")){
				save = ProjectCompletionManager.save(model, workobj, items, mode);
			}
			if(mode.equals("U") || mode.equals("US")){
				save = ProjectCompletionManager.Update(model, workobj, items, mode);
			}
			
        	if(save.equals("1")){
        		finalResult.put("status", 1);
        		if(mode.equals("S")){
        			finalResult.put("mode", "S");
        			finalResult.put("msg",  "Form 2 Project Completion Save Successfully");
        		}else if(mode.equals("U")){
        			finalResult.put("mode", "U");
        			finalResult.put("msg",  "Form 2 Project Completion Update Successfully");
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
			System.out.println("EXCEPTION CAUSED BY: ProjectCompletionService [saverequest]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectCompletionService [saverequest]", e.toString()));
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
			
			delete = ProjectCompletionManager.delete(id);
			
			if(delete.equals("1")){
				finalResult.put("msg",  "Form 2 Record delete Successfully");
			}else{
        		finalResult.put("msg", ApplicationConstants.FAIL);
        	}
        	
			response.setContentType("application/json");
        	response.setHeader("Cache-Control", "no-store");
        	out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectCompletionService [delete]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectCompletionService [delete]", e.toString()));
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
        	
			String delete=ProjectCompletionManager.deletattchdata(aid, filename, mastid);
			
			if(delete.equals("")){
				finalResult.put("status", 0);
				finalResult.put("msg", ApplicationConstants.FAIL);
			}else{
				finalResult.put("status", 1);
				finalResult.put("msg", "File Deleted Successfully");
	        }
	        response.setContentType("application/json");
		    response.setHeader("Cache-Control", "no-store");
		    out.print(finalResult);
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println("EXCEPTION CAUSED BY: ProjectCompletionService[deleteattachfile]"+" "+e.getMessage().toUpperCase());
			log.fatal(Logging.logException("ProjectCompletionService[deleteattachfile]", e.toString()));
		}
	}
	
	
}