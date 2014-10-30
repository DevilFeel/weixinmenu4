package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;

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
 * Servlet implementation class QueryCronAjax
 */
@WebServlet("/queryCron")
public class QueryCronAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QueryCronAjax() {
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
		Integer sessionNumber = Integer.parseInt(request.getParameter("class")); // 获取课程编号
		List<CronInfo> cronList = null;
		Iterator<CronInfo> cronIter = null;
		CronInfo cronInfo = null;
		List<LessonInfo> lessonList = null;
		Iterator<LessonInfo> lessonIter = null;
		LessonInfo lessonInfo = null;
		try {
			lessonList = DAOFactory.getILessonInfoDAOInstance()
					.getBySessionNumber(sessionNumber);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("获取课时列表失败！");
		}
		lessonIter = lessonList.iterator();
		JSONArray json = new JSONArray();
		JSONObject jsonObj = null;
		while (lessonIter.hasNext()) {
			lessonInfo = lessonIter.next();
			try {
				cronList = DAOFactory.getICronInfoDAOInstance()
						.getByLessonNumber(lessonInfo.getLessonNumber());
				cronIter = cronList.iterator();
				while(cronIter.hasNext()){
					cronInfo = cronIter.next();
					if(cronInfo.getStatus()==1){	//只传输已经上传考勤记录的计划
						jsonObj = new JSONObject();
						jsonObj.put("id", cronInfo.getCronNumber());
						jsonObj.put("week", cronInfo.getWeek());
						jsonObj.put("executeTime", cronInfo.getExecuteTime().toString());
						jsonObj.put("executeDate", cronInfo.getExecuteDate().toString());
						jsonObj.put("classroom", cronInfo.getClassroom());
						json.add(jsonObj);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("获取计划列表失败！");
			}
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
