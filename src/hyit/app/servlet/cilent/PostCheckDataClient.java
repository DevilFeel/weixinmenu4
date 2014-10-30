package hyit.app.servlet.cilent;

import hyit.app.factory.DAOFactory;
import hyit.app.model.CheckInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.StudentInfo;
import hyit.app.model.TeacherInfo;
import hyit.app.trigger.SummaryAbsentInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PostCheckDataClient
 */
@WebServlet("/postCheckData")
public class PostCheckDataClient extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PostCheckDataClient() {
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
		Integer teacherNumber = Integer.parseInt(request
				.getParameter("teacherNumber"));
		Integer cronNumber = Integer.parseInt(request
				.getParameter("cronNumber"));
		Time executeTime = Time.valueOf(request.getParameter("executeTime"));
		String[] studentNumberArray = request
				.getParameterValues("studentNumber");
		String[] cardMac = request.getParameterValues("cardMac");
		String[] absent = request.getParameterValues("absent");
		String[] date = request.getParameterValues("date");
		String[] time = request.getParameterValues("time");
		StudentInfo studentInfo = null;
		TeacherInfo teacherInfo = null;
		CronInfo cronInfo = null;
		CheckInfo checkInfo = null;
		String msg = null;
		boolean flag = true;
		try {
			teacherInfo = DAOFactory.getITeacherInfoDAOInstance().getByID(
					teacherNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			System.out.println("�˺���֤ʧ�ܣ�");
		}
		if (null != teacherInfo) {
			try {
				cronInfo = DAOFactory.getICronInfoDAOInstance().getByID(
						cronNumber);
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
				System.out.println("��ȡ�ƻ������ʧ�ܣ�");
			}
			if (cronInfo.getStatus() == 0) { // ȷ������û�б��ϴ���ִ���ϴ�����
				// ȡ�ÿ�ʱ��Ϣ
				LessonInfo lessonInfo = null;
				try {
					lessonInfo = DAOFactory.getILessonInfoDAOInstance()
							.getByID(cronInfo.getLessonNumber());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.out.println("�ϴ�-ȡ�ÿ�ʱ��Ϣʧ�ܣ�");
				}
				for (int i = 0; i < studentNumberArray.length; i++) {
					// ���Ÿ���
					studentInfo = new StudentInfo();
					try {
						studentInfo = DAOFactory.getIStudentInfoDAOInstance()
								.getByID(Long.parseLong(studentNumberArray[i]));
						if (!cardMac[i].equals(studentInfo.getCardMac())) {
							studentInfo.setCardMac(cardMac[i]);
							DAOFactory.getIStudentInfoDAOInstance().update(
									studentInfo);
						}
					} catch (Exception e) {
						// TODO: handle exception
						flag = false;
						System.out.println("���¿���ʧ��");
					}
					// ��¼����
					checkInfo = new CheckInfo();
					checkInfo.setCronNumber(cronNumber);
					checkInfo.setStudentNumber(Long
							.parseLong(studentNumberArray[i]));
					checkInfo.setAbsent(absent[i]);
					checkInfo.setDate(Date.valueOf(date[i]));
					checkInfo.setTime(Time.valueOf(time[i]));
					// ��鿼��״̬
					Integer status = 0;
					if (checkInfo.getAbsent().equals("δ����")) {
						status = 1;
					} else if (checkInfo.getAbsent().equals("�ٵ�")) {
						status = 2;
					}
					try {
						DAOFactory.getICheckInfoDAOInstance().doCreate(
								checkInfo);
						// ���»��ܱ�
						SummaryAbsentInfo.summary(checkInfo.getStudentNumber(),
								lessonInfo.getSessionNumber(), status);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						flag = false;
						System.out.println("��¼����ʧ�ܣ�");
					}
				}
				// ���¿�������״̬
				try {
					cronInfo.setExecuteTime(executeTime);
					cronInfo.setStatus(1);
					flag = DAOFactory.getICronInfoDAOInstance()
							.update(cronInfo);
				} catch (Exception e) {
					// TODO: handle exception
					flag = false;
					System.out.println("���¿�������״̬ʧ�ܣ�");
					e.printStackTrace();
				}
				msg = "succeed"; // ����ִ�гɹ�������succeed
			} else {
				msg = "fail"; // �����Ѿ����ϴ���������ִ��ʧ�ܣ�����fail
			}
		} else {
			flag = false; // ��ʦ��Ϣ��֤ʧ�ܣ��޷����κ�����
		}
		// ����ִ��״̬
		if (flag) {
			PrintWriter pw = response.getWriter();
			pw.print(msg);
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
