package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SemesterInfo;
import hyit.app.model.SubjectInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
 * Servlet implementation class QuerySubjectAjax
 */
@WebServlet("/querySubject")
public class QuerySubjectAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuerySubjectAjax() {
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
		response.setCharacterEncoding("utf-8");
		response.setContentType("html/text");
		String year = request.getParameter("year");
		String semester = request.getParameter("semester");
		Integer teacherNumber = Integer.parseInt(request.getSession()
				.getAttribute("teacherNumber").toString());
		List<SemesterInfo> list = new ArrayList<SemesterInfo>();
		Iterator<SemesterInfo> iter = null;
		SemesterInfo semesterInfo = new SemesterInfo();
		List<SubjectInfo> listSub = null;
		Iterator<SubjectInfo> iterSub = null;
		SubjectInfo subInfo = null;
		JSONArray json = new JSONArray();
		JSONObject jsonObj = null;
		try {
			list = DAOFactory.getISemesterDAOInstance().getAll();
			iter = list.iterator();
			while (iter.hasNext()) { // ÕÒ³öÑ§ÆÚ±àºÅ
				semesterInfo = iter.next();
				if (semesterInfo.getYear().equals(year)
						&& semesterInfo.getSemester().equals(semester)) {
					break;
				} else {
					semesterInfo = null;
				}
			}
			if (semesterInfo != null) {
				listSub = DAOFactory.getISubjectInfoDAOInstance()
						.getByTeacherNumberAndSemesterNumber(teacherNumber,
								semesterInfo.getSemesterNumber());
				iterSub = listSub.iterator();
				while (iterSub.hasNext()) {
					subInfo = iterSub.next();
					jsonObj = new JSONObject();
					jsonObj.put("id", subInfo.getSubjectNumber());
					jsonObj.put("name", subInfo.getName());
					json.add(jsonObj);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
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
