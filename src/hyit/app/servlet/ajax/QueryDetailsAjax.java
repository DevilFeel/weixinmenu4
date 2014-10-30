package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.CheckInfo;
import hyit.app.model.ClassInfo;
import hyit.app.model.StudentInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class QueryDetailsAjax
 */
@WebServlet("/queryDetails")
public class QueryDetailsAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryDetailsAjax() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		Integer cronNumber = Integer.parseInt(request
				.getParameter("cronNumber"));
		List<CheckInfo> checkList = null;
		Iterator<CheckInfo> checkIter = null;
		CheckInfo checkInfo = null;
		StudentInfo studentInfo = null;
		ClassInfo classInfo = null;
		JSONArray json = new JSONArray();
		JSONObject jsonObj = null;
		try {
			checkList = DAOFactory.getICheckInfoDAOInstance().getByCronNumber(
					cronNumber);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取考勤表失败！");
		}
		checkIter = checkList.iterator();
		while (checkIter.hasNext()) {
			checkInfo = checkIter.next();
			try {
				studentInfo = DAOFactory.getIStudentInfoDAOInstance().getByID(
						checkInfo.getStudentNumber());
				classInfo = DAOFactory.getIClassInfoDAOInstance().getByID(
						studentInfo.getClassNumber());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取学生详细信息失败！");
			}
			jsonObj = new JSONObject();
			jsonObj.put("id", studentInfo.getStudentNumber());
			jsonObj.put("name", studentInfo.getName());
			jsonObj.put("sex", studentInfo.getSex());
			jsonObj.put("className", classInfo.getName());
			jsonObj.put("status", checkInfo.getAbsent());
			jsonObj.put("time", checkInfo.getTime().toString());
			jsonObj.put("date", checkInfo.getDate().toString());
			json.add(jsonObj);
		}
		PrintWriter pw = response.getWriter();
		pw.print(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
