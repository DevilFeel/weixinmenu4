package hyit.app.servlet.cilent;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SessionInfo;
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
 * Servlet implementation class QuerySessionClient
 */
@WebServlet("/querySessionClient")
public class QuerySessionClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuerySessionClient() {
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
		Integer subjectNumber = Integer.parseInt(request
				.getParameter("subjectNumber"));
		Integer teacherNumber = Integer.parseInt(request.getParameter("teacherNumber"));
		/*String account = request.getParameter("account");
		String password = request.getParameter("password");
		String cardMac = request.getParameter("cardMac");*/
		boolean flag = true;
		TeacherInfo teacherInfo = new TeacherInfo();
		List<SessionInfo> list = null;
		Iterator<SessionInfo> iter = null;
		SessionInfo sessionInfo = null;
		/*if (null == cardMac) {
			teacherInfo.setAccount(account);
			teacherInfo.setPassword(password);
		} else {
			teacherInfo.setCardMac(cardMac);
		}*/
		try {
			teacherInfo = DAOFactory.getITeacherInfoDAOInstance().getByID(teacherNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("账号验证失败！");
		}
		try {
			list = DAOFactory.getISessionInfoDAOInstance().getBySubjectNumber(
					subjectNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("获取课程列表失败！");
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
				sessionInfo = iter.next();
				pw.println("<session>");
				pw.println("<sessionNumber>" + sessionInfo.getSessionNumber()
						+ "</sessionNumber>");
				pw.println("<subjectNumber>" + sessionInfo.getSubjectNumber()
						+ "</subjectNumber>");
				pw.println("<className>" + sessionInfo.getClassName()
						+ "</className>");
				pw.println("<startWeek>" + sessionInfo.getStartWeek()
						+ "</startWeek>");
				pw.println("<endWeek>" + sessionInfo.getEndWeek()
						+ "</endWeek>");
				pw.println("</session>");
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
