package hyit.app.servlet.ajax;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SemesterInfo;

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

/**
 * Servlet implementation class QuerySemesterAjax
 */
@WebServlet("/queryYear")
public class QuerySemesterAjax extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuerySemesterAjax() {
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
		List<SemesterInfo> list = null;
		Iterator<SemesterInfo> iter = null;
		JSONArray json = new JSONArray();
		try {
			list = DAOFactory.getISemesterDAOInstance().getAll();
			iter = list.iterator();
			while(iter.hasNext()){
				json.add(iter.next().getYear());
			}
		} catch (Exception e) {
			// TODO: handle exception
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
