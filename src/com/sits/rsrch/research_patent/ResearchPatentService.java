package com.sits.rsrch.research_patent;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
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
import com.sits.general.ApplicationConstants;
import com.sits.general.General;
import com.sits.general.Logging;
@WebServlet("/ResearchPatentService")
public class ResearchPatentService extends HttpServlet {
		private static final long serialVersionUID = 1L;
		static Logger log = Logger.getLogger("exceptionlog");
		AesUtil aesUtil = new AesUtil(128,10);
		
	    /**
	     * @see HttpServlet#HttpServlet()
	     */
	    public ResearchPatentService() {
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
				response.getWriter().append("Served at ResearchPatentService: ").append(request.getContextPath());
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
			System.out.println("SERVICE TSSBJ"+General.checknull(request.getHeader("fstatus")));
			switch (General.checknull(request.getHeader("fstatus"))){
			case "N":
				saverequest(request, response);
				break;
			case "E":
				saverequest(request, response);
				break;
			case "D":
				delete(request, response);
				break;
			default: System.out.println("Invalid grade ResearchPatentService");
			}
		}
		//this method is used to send data on manager to save or update data in db
		public synchronized void saverequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
			PrintWriter out=response.getWriter();
			response.setContentType("application/json");
			try{
				JSONObject finalResult= new JSONObject();
				String errMsg="", flg="Y",jString="";
				
				String uniqueKey = General.checknull((String)request.getSession().getAttribute("AESUniqueKey"));
				String encData	= General.checknull(request.getHeader("encData"));
				String fstatus	= General.checknull(request.getHeader("fstatus"));
				String decodeData=new String(java.util.Base64.getDecoder().decode(encData));
				String decData=AesUtil.parseAes(encData, aesUtil, uniqueKey);
				JSONObject obj 	= (JSONObject) new JSONParser().parse(decData);
				String pat_type = General.checknull((String) obj.get("pat_type"));
				String pat_status = General.checknull((String) obj.get("pat_status"));
				String location = General.checknull((String) obj.get("location"));
				String ddo = General.checknull((String) obj.get("ddo"));
				String app_name = General.checknull((String) obj.get("app_name"));
				String pat_title = General.checknull((String) obj.get("pat_title"));
				String pat_app_num = General.checknull((String) obj.get("pat_app_num"));
				String filing_date = General.checknull((String) obj.get("filing_date"));
				String resh_category = General.checknull((String) obj.get("resh_category"));
				String sub_category = General.checknull((String) obj.get("sub_category"));
				String pat_grnt_date = General.checknull((String) obj.get("pat_grnt_date"));
				
			    String user_id = General.check_null((String) request.getSession().getAttribute("user_id"));
			    String machine = General.checknull((String) request.getSession().getAttribute("s_ip"));	
				
			    if (user_id.equals("")) {
					errMsg = "Session is expired";
					flg = "N";
			  }else if (pat_type.equals("")) {
					errMsg = "Patent Type is blank";
					flg = "N";
			  }else if (pat_status.equals("")) {
			 		errMsg = "Patent Status is blank";
					flg = "N";
			  }else if (location.equals("")) {
			 		errMsg = "Location is blank";
					flg = "N";
			  }else if (ddo.equals("")) {
			 		errMsg = "Ddo is blank";
					flg = "N";
			  }else if (app_name.equals("")) {
			 		errMsg = "Applicant name is blank";
					flg = "N";
			  }else if (pat_title.equals("")) {
			 		errMsg = "Patent Title is blank";
					flg = "N";
			  }else if (pat_app_num.equals("")) {
			 		errMsg = "Patent Application Number is blank";
					flg = "N";
			  }else if (filing_date.equals("")) {
			 		errMsg = "Date Filing date is blank";
					flg = "N";
			  }else if (resh_category.equals("")) {
			 		errMsg = "Category is blank";
					flg = "N";
			  }/*else if (sub_category.equals("")&& !sub_category.equals("NA")) {
			 		errMsg = "Sub Category is blank";
					flg = "N";
			  }*/else if (pat_grnt_date.equals("")) {
			 		errMsg = "Patent published/ granted date is blank";
					flg = "N";
			  }
			    
			    
			    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				FileItem items=null;
	            if (isMultipart) { 
	            	 FileItemFactory factory = new DiskFileItemFactory();
		             ServletFileUpload upload = new ServletFileUpload(factory);
		             List item = upload.parseRequest(request);
		                System.out.println(items);
		                Iterator<FileItem> iter = item.iterator();
		                while (iter.hasNext()) {
		                    FileItem fileItem = iter.next();{
		                    	items = fileItem;
		                    	 System.out.println(items.getName());
		                    	 
		                }
		                    if (items.getName().equals("")) {
		    			 		errMsg = "Document name is blank";
		    					flg = "N";
		    			  }  
		                }
		        }
	           
	       	
	            ResearchPatentModel model=new ResearchPatentModel();
	            Type type = new TypeToken<ResearchPatentModel>() {}.getType();
				model = new Gson().fromJson(obj.toString(), type);
				if(((flg.trim().equalsIgnoreCase("Y"))&&(errMsg.trim().equals("")))){
					if(fstatus.equals("N")){
					model = ResearchPatentManager.save(model, items, user_id, machine, fstatus);
				}
				
				if(fstatus.equals("E")){
					model = ResearchPatentManager.Update(model, items, user_id, machine, fstatus);
				}
				if (model.isValid()) {
					flg = "Y";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, machine, "ResearchPatentService", model.getErrMsg()));
				} else {
					flg = "I";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, machine, "ResearchPatentService", model.getErrMsg()));
				}
				}
			// System.out.println(flg+"|"+errMsg+"|"+fstatus);
			finalResult.put("flg", flg);
			finalResult.put("errMsg", errMsg);
			finalResult.put("fstatus", fstatus);
			jString = aesUtil.encrypt(finalResult.toString(), decodeData.split("::")[0], uniqueKey,
					decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
		} catch (Exception ex) {
			System.out.println("Error in ResearchPatentService[saveRecord] : " + ex.getMessage());
			Logger.getLogger("usglog").fatal(Logging.logException("ResearchPatentService[saveRecord] ", ex.getMessage().toString()));
		} finally {
			out.close();
		}
}

		//this method is using for send id and file name on manager to delete from database
		public synchronized void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, JSONException {
			try{
				PrintWriter out = response.getWriter();
				response.setContentType("application/json");
				JSONObject finalResult= new JSONObject();
				String errMsg= "",flg="Y";
				ResearchPatentModel model =new ResearchPatentModel();
				String id = General.checknull(request.getHeader("id"));
				String fname =General.checknull(request.getHeader("fname"));
				model = ResearchPatentManager.delete(id,fname);
				
				if (model.isValid()) {
					flg = "Y";
					errMsg = model.getErrMsg();
				} else {
					flg = "I";
					errMsg = model.getErrMsg();
				}
			 //System.out.println(flg+"|"+errMsg+"|"+fstatus);
			finalResult.put("flg", flg);
			finalResult.put("errMsg", errMsg);
			out.println(finalResult);
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("EXCEPTION CAUSED BY: ResearchPatentService [delete]"+" "+e.getMessage().toUpperCase());
				log.fatal(Logging.logException("ResearchPatentService [delete]", e.toString()));
			}
		}

}