package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.ClassInfo;

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
 * Servlet implementation class QueryClassAjax
 */
@WebServlet("/queryClass")
public class QueryClassAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QueryClassAjax() {
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
		Integer departmentNumber = Integer.parseInt(request.getParameter("department"));
		JSONArray json = new JSONArray();
		JSONObject jsonObj = null;
		List<ClassInfo> list = null;
		ClassInfo info = null;
		Iterator<ClassInfo> iter = null;
		try {
			list = DAOFactory.getIClassInfoDAOInstance().getByDepartmentNumber(departmentNumber);
			iter = list.iterator();
			while(iter.hasNext()){
				info = iter.next();
				jsonObj = new JSONObject();
				jsonObj.put("id", info.getClassNumber());
				jsonObj.put("name", info.getName());
				json.add(jsonObj);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("取得班级列表失败！");
			e.printStackTrace();
		}
		PrintWriter pw = response.getWriter();
		pw.print(json);
		//System.out.println(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
