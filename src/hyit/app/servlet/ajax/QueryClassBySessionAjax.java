package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SessionInfo;

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
 * Servlet implementation class QueryClassBySessionAjax
 */
@WebServlet("/queryClass2")
public class QueryClassBySessionAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryClassBySessionAjax() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("utf-8");
		response.setContentType("html/text");
		Integer subjectNumber = Integer.parseInt(request.getParameter("subject"));
		List<SessionInfo> list = null;
		Iterator<SessionInfo> iter = null;
		SessionInfo info = null;
		JSONArray json = new JSONArray();
		JSONObject jsonObj = null;
		try {
			list = DAOFactory.getISessionInfoDAOInstance().getBySubjectNumber(subjectNumber);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("查询课程列表失败！");
		}
		iter = list.iterator();
		while(iter.hasNext()){
			info = iter.next();
			jsonObj = new JSONObject();
			jsonObj.put("id", info.getSessionNumber());
			jsonObj.put("name", info.getClassName());
			json.add(jsonObj);
		}
		PrintWriter pw = response.getWriter();
		pw.print(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
