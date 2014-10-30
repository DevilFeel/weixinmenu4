package hyit.app.servlet.cilent;

import hyit.app.factory.DAOFactory;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.ScheduleInfo;
import hyit.app.model.SelectionInfo;
import hyit.app.model.StudentInfo;
import hyit.app.model.TeacherInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class QueryCronClient
 */
@WebServlet("/queryCronClient")
public class QueryCronClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryCronClient() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/xml");
		Integer sessionNumber = Integer.parseInt(request
				.getParameter("sessionNumber"));
		Integer teacherNumber = Integer.parseInt(request
				.getParameter("teacherNumber"));
		/*
		 * String account = request.getParameter("account"); String password =
		 * request.getParameter("password"); String cardMac =
		 * request.getParameter("cardMac");
		 */
		boolean flag = true;
		TeacherInfo teacherInfo = new TeacherInfo();
		List<LessonInfo> lessonList = null;
		Iterator<LessonInfo> lessonIter = null;
		LessonInfo lessonInfo = null;
		List<CronInfo> cronList = null;
		Iterator<CronInfo> cronIter = null;
		CronInfo cronInfo = null;
		List<SelectionInfo> selectionList = null;
		Iterator<SelectionInfo> selectionIter = null;
		SelectionInfo selectionInfo = null;
		StudentInfo studentInfo = null;
		ScheduleInfo scheduleStart = null;
		ScheduleInfo scheduleEnd = null;
		/*
		 * if (null == cardMac) { teacherInfo.setAccount(account);
		 * teacherInfo.setPassword(password); } else {
		 * teacherInfo.setCardMac(cardMac); }
		 */
		try {
			teacherInfo = DAOFactory.getITeacherInfoDAOInstance().getByID(
					teacherNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("�˺���֤ʧ�ܣ�");
		}
		try {
			lessonList = DAOFactory.getILessonInfoDAOInstance()
					.getBySessionNumber(sessionNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("��ȡ��ʱ�б�ʧ�ܣ�");
		}
		Date date = new Date(System.currentTimeMillis()); // ��ȡ��ǰϵͳʱ��
		Integer week = new Integer(date.getDay());
		lessonIter = lessonList.iterator();
		// ѡ������Ŀ�ʱ
		while (lessonIter.hasNext()) {
			lessonInfo = lessonIter.next();
			if (lessonInfo.getDayOfWeek().intValue()%7 == week.intValue()) {	//����week�������ʾ0������ȡ��7
				break;
			} else {
				lessonInfo = null;
			}
		}
		if (null != lessonInfo) {
			try {
				cronList = DAOFactory.getICronInfoDAOInstance()
						.getByLessonNumber(lessonInfo.getLessonNumber());
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
				System.out.println("��ȡ���������б�ʧ�ܣ�");
			}
			cronIter = cronList.iterator();
			// ѡ������ÿ�ʱ�Ŀ�������
			while (cronIter.hasNext()) {
				cronInfo = cronIter.next();
				if (Date.valueOf(cronInfo.getExecuteDate().toString()).equals( // �Ա�ʱ���Ƿ���ͬ
						Date.valueOf(date.toString()))) {
					break;
				} else {
					cronInfo = null;
				}
			}
			if (null == cronInfo) {
				flag = false;
			}
		}
		// ��ȡ�༶����
		try {
			selectionList = DAOFactory.getISelectionInfoDAOInstance()
					.getBySessionNumber(sessionNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("��ȡ�༶����ʧ�ܣ�");
		}
		// ��ȡ�Ͽ�ʱ��
		try {
			scheduleStart = DAOFactory.getIScheduleInfoDAOInstance().getByID(
					lessonInfo.getStartLesson());
			scheduleEnd = DAOFactory.getIScheduleInfoDAOInstance().getByID(
					lessonInfo.getEndLesson());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			flag = false;
			System.out.println("��ȡ�Ͽ�ʱ��ʧ�ܣ�");
		}
		// ��ȡ��������ǰ��ʱ��Time
		Time time = new Time(System.currentTimeMillis());
		// ��XML�ṹ�����������
		if (flag) {
			selectionIter = selectionList.iterator();
			PrintWriter pw = response.getWriter();
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			pw.println("<info>");
			// �����ʦ��Ϣ
			pw.println("<teacher>");
			pw.println("<teacherNumber>" + teacherInfo.getTeacherNumber()
					+ "</teacherNumber>");
			pw.println("<name>" + teacherInfo.getName() + "</name>");
			pw.println("<departmentNumber>" + teacherInfo.getDepartmentNumber()
					+ "</departmentNumber>");
			pw.println("</teacher>");
			// ���������Ϣ
			pw.println("<cron>");
			pw.println("<cronNumber>" + cronInfo.getCronNumber()
					+ "</cronNumber>");
			pw.println("<executeTime>" + time + "</executeTime>");
			pw.println("<executeDate>" + cronInfo.getExecuteDate()
					+ "</executeDate>");
			pw.println("<orderDate>" + cronInfo.getOrderTime() + "</orderDate>");
			pw.println("<status>" + cronInfo.getStatus() + "</status>");
			pw.println("</cron>");
			// �����ʱ��Ϣ
			pw.println("<lesson>");
			pw.println("<lessonNumber>" + lessonInfo.getLessonNumber()
					+ "</lessonNumber>");
			pw.println("<startTime>" + scheduleStart.getStartTime()
					+ "</startTime>");
			pw.println("<endTime>" + scheduleEnd.getEndTime() + "</endTime>");
			pw.println("<classroom>" + lessonInfo.getClassroom()
					+ "</classroom>");
			pw.println("</lesson>");
			// �����������
			pw.println("<list>");
			while (selectionIter.hasNext()) {
				selectionInfo = selectionIter.next();
				try {
					studentInfo = DAOFactory.getIStudentInfoDAOInstance()
							.getByID(selectionInfo.getStudentNumber());
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("����ѧ����Ϣʧ�ܣ�");
				}
				pw.println("<student>");
				pw.println("<studentNumber>" + studentInfo.getStudentNumber()
						+ "</studentNumber>");
				pw.println("<name>" + studentInfo.getName() + "</name>");
				pw.println("<cardMac>" + studentInfo.getCardMac()
						+ "</cardMac>");
				pw.println("<enterYear>" + studentInfo.getEnterYear()
						+ "</enterYear>");
				pw.println("<cardID>" + studentInfo.getCardID() + "</cardID>");
				pw.println("<profession>" + studentInfo.getProfession()
						+ "</profession>");
				pw.println("<sex>" + studentInfo.getSex() + "</sex>");
				pw.println("</student>");
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
