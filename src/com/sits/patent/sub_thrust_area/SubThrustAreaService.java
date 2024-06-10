/**
 * @ AUTHOR TANUJALA
 */
package com.sits.patent.sub_thrust_area;

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

@WebServlet("/SubThrustAreaService")
public class SubThrustAreaService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Logger l = Logger.getLogger("exceptionlog");
	AesUtil aesUtil = new AesUtil(128, 10);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String formStatus = General.checknull(request.getParameter("fstatus").trim().toUpperCase());
		if (formStatus.equals("D")) {
			processDeleteRequest(request, response);
		} else {
			processRequest(request, response);
		}
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		JSONObject jsonObject = null;
		try {
			String errMsg = "", flag = "Y";

			String uniqueKey = General.checknull((String) request.getSession().getAttribute("AESUniqueKey"));
			String encData = General.checknull(request.getParameter("encData"));
			String decodeData = new String(java.util.Base64.getDecoder().decode(encData));
			String decData = AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject obj = (JSONObject) new JSONParser().parse(decData);
			String thrust_area = General.checknull((String) obj.get("thrust_area")).trim();
			String sub_thrust_area= General.checknull((String) obj.get("sub_thrust_area")).trim();
			// String status = General.checknull((String) obj.get("headType")).trim();
			String fstatus = General.checknull(((String) request.getParameter("fstatus")));
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String machine = General.checknull((String) request.getSession().getAttribute("ip"));

			Type type = new TypeToken<SubThrustAreaModel>() {
			}.getType();
			SubThrustAreaModel faModel = new Gson().fromJson(obj.toString(), type);

			if (user_id.equals("")) {
				errMsg = "Session Expire.";
				flag = "N";
			} else if (!((fstatus.equals("N")) || (fstatus.equals("E")))) {
				flag = "N";
				errMsg = "Invalid Form Status";
			}

			if (flag.trim().equals("Y") && errMsg.trim().equals("") && !fstatus.equals("D")) {
				if (thrust_area.trim().equals("")) {
					errMsg = " Thrust Area is required.";
					flag = "N";
				} else if (sub_thrust_area.trim().equals("")) {
					errMsg = "Sub Thrust Area is required.";
					flag = "N";
				} 

			}

			if (flag.trim().equalsIgnoreCase("Y") && errMsg.trim().equals("") && !fstatus.equals("D")) {
				if (fstatus.equals("N")) {
					jsonObject = SubThrustAreaManager.save(faModel, machine, user_id);
				}
				if (fstatus.equals("E")) {
					jsonObject = SubThrustAreaManager.update(faModel, machine, user_id);
				}
				/*
				 * if (fstatus.equals("D")) { jsonObject = ResearchHeadManager.delete(faModel,
				 * machine, user_id); }
				 */
			} else {
				JSONObject Obj = new JSONObject();
				Obj.put("status", errMsg);
				Obj.put("flag", "N");
				jsonObject = Obj;
			}

			String jString = aesUtil.encrypt(jsonObject.toString(), decodeData.split("::")[0], uniqueKey,
					decodeData.split("::")[1]);
			out.println(new Gson().toJson(jString));
			// out.write(jsonObject.toString());
			Logger.getLogger("usglog")
					.debug(Logging.onSucess(user_id, machine, "SubThrustAreaService", ApplicationConstants.SUCCESS));
		} catch (Exception e) {
			System.out.println("ERROR IN SERVICE IS CAUSED BY SubThrustAreaService :" + e.getMessage().toUpperCase());
			e.printStackTrace();
			l.fatal(Logging.logException("SubThrustAreaService", e.toString()));
		} finally {
			out.close();
		}
	}

	protected void processDeleteRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String errMsg = "";
		JSONObject jsonObject = null;
		try {
			String ip = General.check_null((String) request.getSession().getAttribute("s_ip"));
			String user_id = General.check_null((String) request.getSession().getAttribute("user_id"));
			String id = General.checknull(request.getParameter("id"));

			if (!user_id.equals("")) {
				jsonObject = SubThrustAreaManager.delete(id, ip, user_id);
			} else {
				JSONObject Obj = new JSONObject();
				Obj.put("status", errMsg);
				Obj.put("flag", "N");
				jsonObject = Obj;
			}
			out.println(jsonObject.toString());
		} catch (Exception e) {
			System.out.println(
					"ERROR IN SubThrustAreaService [delete] IS CAUSED BY" + " " + e.getMessage().toUpperCase());
			Logger.getLogger("usglog")
					.fatal(Logging.logException("SubThrustAreaService [delete]", e.getMessage().toString()));
		}
	}

}