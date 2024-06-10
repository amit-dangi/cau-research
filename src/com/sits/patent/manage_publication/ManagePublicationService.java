package com.sits.patent.manage_publication;
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
import com.sits.general.General;
import com.sits.general.Logging;
import com.sits.rsrch.research_patent.ResearchPatentManager;
import com.sits.rsrch.research_patent.ResearchPatentModel;


@WebServlet("/ManagePublicationService")
public class ManagePublicationService  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AesUtil aesUtil = new AesUtil(128, 10);
		/**
		 * @see HttpServlet#HttpServlet()
		 */
		public ManagePublicationService() {
			super();
			// TODO Auto-generated constructor stub
		}
		/**
		 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
		 *      response)
		 */
		protected void doGet(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			// TODO Auto-generated method stub
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}
		/**
		 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
		 *      response)
		 */
		protected void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String Status = General.checknull(request.getParameter("fstatus").trim());
			 //System.out.println("Status :::"+Status);
			if (Status.equals("N") || Status.equals("E")) {
				saverecord(request, response);
			} else if (Status.equals("D")) {
				delete(request, response);
			}
		}

		private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			try {
				JSONObject jsonObj = new JSONObject();
				String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
				String ip = General.checknull((String) request.getSession().getAttribute("s_ip"));
				String id = General.checknull(request.getParameter("id")).trim();
				//System.out.println("user_id:"+user_id);
				if (user_id.equals("")) {
					jsonObj.put("status", "Session Expire!!");
					jsonObj.put("flag", false);
				} else {
					jsonObj = ManagePublicationManager.deleteRecord(id, user_id, ip);
				}
				out.print(jsonObj);
			} catch (Exception e) {
				System.out.println("EXCEPTION IS CAUSED BY: ManagePublicationService[delete()]"+ e.getMessage().trim().toUpperCase());
				Logger.getLogger("usglog").fatal(Logging.logException("ManagePublicationService[delete]", e.toString()));
			}
		}

		private void saverecord(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out = response.getWriter();
			try {
				String errMsg = "", flg = "Y",jString="";
				JSONObject jsonObj = new JSONObject();
				ManagePublicationModel model = new ManagePublicationModel();
				String uniqueKey = General.checknull((String) request.getSession().getAttribute("AESUniqueKey"));
				String encData = General.checknull(request.getParameter("encData"));
				String decodeData = new String(java.util.Base64.getDecoder().decode(encData));
				String decData = AesUtil.parseAes(encData, aesUtil, uniqueKey);
				JSONObject dataObj = (JSONObject) new JSONParser().parse(decData);
				String fstatus = General.checknull((String) dataObj.get("fstatus")).trim();
				String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
				String ip = General.check_null((String) request.getSession().getAttribute("s_ip"));
				String location = General.checknull((String) dataObj.get("LOCATION_CODE"));
				String DDO_ID = General.checknull((String) dataObj.get("DDO_ID"));
				String pub_id = General.checknull((String) dataObj.get("pub_id"));

				model.setLOCATION_CODE(location);
				model.setDDO_ID(DDO_ID);
				model.setPub_id(pub_id);
				System.out.println("user_id fstatus"+fstatus);
				if (user_id.equals("")) {
					errMsg = "Session is expired";
					flg = "N";
			  }else if (location.equals("")) {
					errMsg = "Location is blank";
					flg = "N";
			  }else if (DDO_ID.equals("")) {
			 		errMsg = "Ddo is blank";
					flg = "N";
			  }else if (pub_id.equals("")) {
			 		errMsg = "Publication is blank";
					flg = "N";
			  }
		            Type type = new TypeToken<ManagePublicationModel>() {}.getType();
					model = new Gson().fromJson(dataObj.toString(), type);
					
				if(((flg.trim().equalsIgnoreCase("Y"))&&(errMsg.trim().equals("")))){
			    if (fstatus.equals("N")) {
					  model = ManagePublicationManager.save(model, user_id, ip);
				}else{
					model = ManagePublicationManager.update(model, user_id, ip);
				}if (model.isValid()) {
					flg = "Y";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, ip, "ManagePublicationService", model.getErrMsg()));
				} else {
					flg = "N";
					errMsg = model.getErrMsg();
					Logger.getLogger("usglog")
							.debug(Logging.onSucess(user_id, ip, "ManagePublicationService", model.getErrMsg()));
				}
				}
				  jsonObj.put("flg", flg);
				  jsonObj.put("errMsg", errMsg);
				  jsonObj.put("fstatus", fstatus);
					jString = aesUtil.encrypt(jsonObj.toString(), decodeData.split("::")[0], uniqueKey,
							decodeData.split("::")[1]);
					out.println(new Gson().toJson(jString));
			} catch (Exception e) {
				System.out.println("EXCEPTION IS CAUSED BY: ManagePublicationService[saveRecord()]" + e.getMessage().trim().toUpperCase());
				Logger.getLogger("usglog").fatal(Logging.logException("ManagePublicationService[saveRecord]", e.toString()));
			}
		}
}
