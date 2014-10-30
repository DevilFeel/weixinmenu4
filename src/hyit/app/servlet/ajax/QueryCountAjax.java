package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.CheckInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.SessionInfo;
import hyit.app.model.StudentInfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class QueryCountAjax
 */
@WebServlet("/queryCount")
public class QueryCountAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryCountAjax() {
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
		response.setContentType("text/html");
		Integer subjectNumber = Integer.parseInt(request
				.getParameter("subjectNumber"));
		String startDay = request.getParameter("startDay");
		String endDay = request.getParameter("endDay");
		java.util.Date curDate = new java.util.Date(startDay);
		Date start = new Date(curDate.getTime());
		curDate = new java.util.Date(endDay);
		Date end = new Date(curDate.getTime());
		List<SessionInfo> sessionList = null;
		Iterator<SessionInfo> sessionIter = null;
		SessionInfo sessionInfo = null;
		List<LessonInfo> lessonList = null;
		Iterator<LessonInfo> lessonIter = null;
		LessonInfo lessonInfo = null;
		List<CronInfo> cronList = null;
		Iterator<CronInfo> cronIter = null;
		CronInfo cronInfo = null;
		List<CheckInfo> checkList = null;
		Iterator<CheckInfo> checkIter = null;
		CheckInfo checkInfo = null;
		StudentInfo studentInfo = null;
		Map<String, Map<String, Integer>> result = new HashMap<String, Map<String, Integer>>();
		Map<String, Integer> map = null;
		JSONObject jsonObj = null;
		
		try { // 获取课程下各个班级的信息
			sessionList = DAOFactory.getISessionInfoDAOInstance()
					.getBySubjectNumber(subjectNumber);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取班级列表失败！");
		}
		sessionIter = sessionList.iterator();
		while (sessionIter.hasNext()) {
			map = new HashMap<String, Integer>();
			sessionInfo = sessionIter.next();
			try { // 获取该课程下上课班级的课时信息
				lessonList = DAOFactory.getILessonInfoDAOInstance()
						.getBySessionNumber(sessionInfo.getSessionNumber());
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取课时失败！");
			}
			lessonIter = lessonList.iterator();
			while (lessonIter.hasNext()) {
				lessonInfo = lessonIter.next();
				try { // 获取课时对应的所有计划表，并选出区间范围内的表
					cronList = DAOFactory.getICronInfoDAOInstance()
							.getByLessonNumber(lessonInfo.getLessonNumber());
				} catch (Exception e) {
					// TODO: handle exception
				}
				cronIter = cronList.iterator();
				while (cronIter.hasNext()) {
					cronInfo = cronIter.next();
					if (cronInfo.getStatus() == 1
							&& cronInfo.getExecuteDate().after(start)
							&& cronInfo.getExecuteDate().before(end)) {
						try {
							checkList = DAOFactory.getICheckInfoDAOInstance()
									.getByCronNumber(cronInfo.getCronNumber());
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						checkIter = checkList.iterator();
						while (checkIter.hasNext()) {
							checkInfo = checkIter.next();
							if (checkInfo.getAbsent().equals("未考勤")) {
								try {
									studentInfo = DAOFactory.getIStudentInfoDAOInstance().
											getByID(checkInfo.getStudentNumber());
								} catch (Exception e) {
									// TODO: handle exception
								}
								if(map.get(studentInfo.getName())==null){
									map.put(studentInfo.getName(),1);
								}else{
									map.put(studentInfo.getName(), map.get(studentInfo.getName())+1);
								}
							}
						}

					}
				}
			}
			result.put(sessionInfo.getClassName(), map);
		}
		//输出统计数据json
		Set<String> classNameSet = result.keySet();
		Set<String> studentNameSet = null;
		Iterator<String> classIter = classNameSet.iterator();
		Iterator<String> nameIter = null;
		String className = null;
		String studentName = null;
		JSONArray json = new JSONArray();
		String info = "";
		while(classIter.hasNext()){
			className = classIter.next();
			map = result.get(className);
			studentNameSet = map.keySet();
			nameIter = studentNameSet.iterator();
			while(nameIter.hasNext()){
				studentName = nameIter.next();
				info = info + studentName + "("+ map.get(studentName) + ") ";
			}
			jsonObj = new JSONObject();
			jsonObj.put("className", className);
			jsonObj.put("info", info);
			json.add(jsonObj);
		}
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.close();
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
