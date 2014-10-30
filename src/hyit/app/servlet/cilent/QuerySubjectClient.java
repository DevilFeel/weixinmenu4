package hyit.app.servlet.cilent;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SubjectInfo;
import hyit.app.model.TeacherInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QuerySubjectClient
 */
@WebServlet("/querySubjectClient")
public class QuerySubjectClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuerySubjectClient() {
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
		response.setContentType("text/xml");
		String account = request.getParameter("account");
		String password = request.getParameter("password");
		String cardMac = request.getParameter("cardMac");
		boolean flag = true;
		TeacherInfo teacherInfo = new TeacherInfo();
		List<SubjectInfo> list = null;
		Iterator<SubjectInfo> iter = null;
		SubjectInfo subjectInfo = null;
		if (null == cardMac) {
			teacherInfo.setAccount(account);
			teacherInfo.setPassword(password);
		} else {
			teacherInfo.setCardMac(cardMac);
		}
		try {
			teacherInfo = DAOFactory.getITeacherInfoDAOInstance().check(
					teacherInfo);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("账号验证失败！");
		}
		try {
			list = DAOFactory.getISubjectInfoDAOInstance().getByTeacherNumber(
					teacherInfo.getTeacherNumber());
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("查询学科列表失败！");
		}
		if (flag) {
			iter = list.iterator();
			PrintWriter pw = response.getWriter();
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<info>");
			pw.println("<teacher>");
			pw.println("<teacherNumber>" + teacherInfo.getTeacherNumber()
					+ "</teacherNumber>");
			pw.println("<name>" + teacherInfo.getName() + "</name>");
			pw.println("<departmentNumber>" + teacherInfo.getDepartmentNumber()
					+ "</departmentNumber>");
			pw.println("</teacher>");
			pw.println("<list>");
			while (iter.hasNext()) {
				subjectInfo = iter.next();
				pw.println("<subject>");
				pw.println("<subjectNumber>" + subjectInfo.getSubjectNumber()
						+ "</subjectNumber>");
				pw.println("<name>" + subjectInfo.getName() + "</name>");
				pw.println("</subject>");
			}
			pw.println("</list>");
			pw.println("</info>");
			pw.close();
		}
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
