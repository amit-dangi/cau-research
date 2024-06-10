package com.sits.patent.discipline_thrust_area_mapping;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.sits.common.AesUtil;
import com.sits.general.General;
import com.sits.general.Logging;

/**
 * Servlet implementation class MasterCourseService
 */
@WebServlet("/PATENT/DisciplineThrustAreaMappingService")
public class DisciplineThrustAreaMappingService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	AesUtil aesUtil = new AesUtil(128, 10);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DisciplineThrustAreaMappingService() {
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
		 System.out.println("Status tanum:::"+Status);
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
			String errMsg = "", flg = "Y";
			JSONObject jsonObj = new JSONObject();

			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String ip = General.check_null((String) request.getSession().getAttribute("ip"));
			String cr_id = General.checknull(request.getParameter("cr_id")).trim();

			if (user_id.equals("")) {
				jsonObj.put("status", "Session Expire!!");
				jsonObj.put("flag", false);
			} else {
				jsonObj = DisciplineThrustAreaMappingManager.deleteRecord(cr_id, user_id, ip);
			}
			out.print(jsonObj);
		} catch (Exception e) {
			System.out.println("EXCEPTION IS CAUSED BY: DisciplineThrustAreaMappingService[deleteRecord()]"+ e.getMessage().trim().toUpperCase());
			Logger.getLogger("usglog").fatal(Logging.logException("DisciplineThrustAreaMappingService[deleteRecord]", e.toString()));
		}
	}

	private void saverecord(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String errMsg = "", flg = "Y";
			JSONObject jsonObj = new JSONObject();
			DisciplineThrustAreaMappingModel model = new DisciplineThrustAreaMappingModel();
			String uniqueKey = General.checknull((String) request.getSession().getAttribute("AESUniqueKey"));
			String encData = General.checknull(request.getParameter("encData"));
			String decodeData = new String(java.util.Base64.getDecoder().decode(encData));
			String decData = AesUtil.parseAes(encData, aesUtil, uniqueKey);
			JSONObject dataObj = (JSONObject) new JSONParser().parse(decData);
			String fstatus = General.checknull((String) dataObj.get("fstatus")).trim();
			String user_id = General.checknull((String) request.getSession().getAttribute("user_id"));
			String ip = General.check_null((String) request.getSession().getAttribute("ip"));
			String discipline = General.checknull((String) dataObj.get("discipline")).trim();
			String thrust_area = General.checknull((String) dataObj.get("thrust_area")).trim();
			String sub_thrust_area = General.checknull((String) dataObj.get("sub_thrust_area")).trim();
			String cr_id = General.checknull((String) dataObj.get("cr_id")).trim();

			model.setDiscipline(discipline);
			model.setThrust_area(thrust_area);
			model.setSub_thrust_area(sub_thrust_area);
			model.setDiscipline(discipline);
			model.setId(cr_id);
			System.out.println("user_id fstatus"+fstatus);
			//if (user_id.equals("")) {
				
			  if (fstatus.equals("N")) {
				jsonObj = DisciplineThrustAreaMappingManager.save(model, user_id, ip);
			} else if (fstatus.equals("E")) {
				jsonObj = DisciplineThrustAreaMappingManager.update(model, user_id, ip);
			}
			out.print(jsonObj);
		} catch (Exception e) {
			System.out.println("EXCEPTION IS CAUSED BY: DisciplineThrustAreaMappingService[saveRecord()]" + e.getMessage().trim().toUpperCase());
			Logger.getLogger("usglog").fatal(Logging.logException("DisciplineThrustAreaMappingService[saveRecord]", e.toString()));
		}
	}
}
