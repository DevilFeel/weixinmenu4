package service;

import hyit.app.model.SummaryValue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ServiceFunction {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	
	//获得班级名称
	public String getClassName(String Studentid) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from student_info where student_number = '"+ Studentid +"'");
		while(rs.next()){
			int number = rs.getInt("class_number");
			stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery("select * from class_info where class_number = '"+number+"'");
			while(result.next()){
				return result.getString("name");
			}
		}
		rs.close();
		stmt.close();
		conn.close();
		return null;
	}
	//获得学科编号从sessionnumber
	public int getSubjectNumberBySessionNumber(int sessionNumber) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from session_info where session_number = '"+ sessionNumber +"'");
		while(rs.next()){
			return rs.getInt("subject_number");
		}
		return -1;
	}
	//获得学科姓名从学科编号
	public String getNameBySubjectNumber(int number) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from subject_info where subject_number = '"+ number +"'");
		while(rs.next()){
			return rs.getString("name");
		}
		return null;
	}
	//获得学号从parent_info
	public long getStudentNumberByOpenid(String openid) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+ openid +"'");
		while(rs.next()){
			
			return rs.getLong("student_number");
		}
		rs.close();
		stmt.close();
		conn.close();
		
		return 0;
	}
	
	//获得teacherNumber从parent_info
	public int getTeacherNumberByOpenid(String openid) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+ openid +"'");
		while(rs.next()){
			
			return rs.getInt("teacher_number");
		}
		rs.close();
		stmt.close();
		conn.close();
		
		return 0;
	} 
	//根据教师编号获得学院编号
	public int getDepartmentByTeacherNumber(int number) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql="SELECT * FROM teacher_info WHERE teacher_number = '"+number+"'";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			return rs.getInt("department_number");
		}
		return 0;
	}
	
	//获得考勤次数 (学生个人考勤情况)
	public String getAbsentState(long studentNumber) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		int count;//缺勤次数
		int countLate; //迟到次数
		int countLeave; //请假次数
		int allCount;
		String str = "";
		String name = getNameByStudentNumber(studentNumber);
		//获得学科信息
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rsSubject = stmt.executeQuery("select * from subject_info");
		while(rsSubject.next()){
			String tmp="";
			count = 0;
			countLate = 0;
			countLeave = 0;
			allCount = 0;
			//名字
			String subjectName = null;
			subjectName = rsSubject.getString("name");
			int subjectNumber = rsSubject.getInt("subject_number");
			//次数
			  //课程标号
			stmt = conn.createStatement();
			ResultSet rsSession = stmt.executeQuery("select * from session_info where subject_number = '"+ subjectNumber +"'");
			while(rsSession.next()){
				int sessionNumber = rsSession.getInt("session_number");
				//获得课时编号
				stmt = conn.createStatement();
				ResultSet rsLesson = stmt.executeQuery("select * from lesson_info where session_number = '"+sessionNumber+"'");
				while(rsLesson.next()){
					int lessonNumber = rsLesson.getInt("lesson_number");
					//获得计划编号
					stmt = conn.createStatement();
					ResultSet rsCron = stmt.executeQuery("select * from cron_info where lesson_number = '"+lessonNumber+"'");
					while(rsCron.next()){
						if(rsCron.getInt("status") == 1){
							allCount++;
							
						}
						
						int cronNumber = rsCron.getInt("cron_number");
						//通过计划和学号来统计缺勤
						stmt = conn.createStatement();
						ResultSet rsCheck = stmt.executeQuery("select * from check_info where cron_number = '"+cronNumber+"' and student_number = '"+studentNumber+"'");
						while(rsCheck.next()){
							String absent = rsCheck.getString("adsent");
							//allCount++;
							if(absent.equals("未考勤")){
									count++;
							}
							if(absent.equals("迟到")){
								countLate++;
							}
							if(absent.equals("请假")){
								countLeave++;
							}
							
						}
						
					}
				}
				
			}
			//String strAllCount = "";
			if(allCount != 0){
				
				//strAllCount = strAllCount + subjectName+ " 共考勤  " + allCount +"次\n";
				tmp = tmp +subjectName+ ": 共考勤  " + allCount +"次" 
				+ "  缺勤  " + count +"次" + "  迟到  " + countLate +"次" 
				+ "  请假  " + countLeave +"次"+ "\n\n";
			}
			//名字+次数 = 字符串
			//str = str + strAllCount;
			str += tmp;
			
		}
		rsSubject.close();
		stmt.close();
		conn.close();
		
		str = name+"的考勤情况：\n" + str;//加上姓名
		return str;
	}
	
	//获得一个班级的缺勤人员名单 及次数
	public String getClassAbsent(String classname) throws Exception{
		
		int classNum = getClassNumber(classname);
		long studentNumber;
		//String tmp = "";
		String str = " ";
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql = "select * from student_info where class_number = '"+classNum+"'";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			//str += rs.getString("name")+" ";
			studentNumber = rs.getLong("student_number");
			if(!"未缺勤".equals(getOneAbsent(studentNumber))){
				str += rs.getString("name")+" ";
			}
		}
		return str;
	}
	//获得班级编号
	public int getClassNumber(String classname) throws Exception{
		int classNumber=0;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql = "select * from class_info where name = '"+classname+"'";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			classNumber = rs.getInt("class_number");
		}
		rs.close();
		stmt.close();
		conn.close();
		return classNumber;
	}
	//获得个人缺勤次数，没缺勤则返回“正常”
	public String getOneAbsent(long studentNumber) throws Exception{
		
		int count;//缺勤次数
		int allCount;
		String str = "";
		String name = getNameByStudentNumber(studentNumber);
		//获得学科信息
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rsSubject = stmt.executeQuery("select * from subject_info");
		while(rsSubject.next()){
			String tmp="";
			count = 0;
			allCount = 0;
			//名字
			String subjectName = null;
			subjectName = rsSubject.getString("name");
			int subjectNumber = rsSubject.getInt("subject_number");
			//次数
			  //课程标号
			stmt = conn.createStatement();
			ResultSet rsSession = stmt.executeQuery("select * from session_info where subject_number = '"+ subjectNumber +"'");
			while(rsSession.next()){
				int sessionNumber = rsSession.getInt("session_number");
				//获得课时编号
				stmt = conn.createStatement();
				ResultSet rsLesson = stmt.executeQuery("select * from lesson_info where session_number = '"+sessionNumber+"'");
				while(rsLesson.next()){
					int lessonNumber = rsLesson.getInt("lesson_number");
					//获得计划编号
					stmt = conn.createStatement();
					ResultSet rsCron = stmt.executeQuery("select * from cron_info where lesson_number = '"+lessonNumber+"'");
					while(rsCron.next()){
						if(rsCron.getInt("status") == 1){
							allCount++;
							
						}
						
						int cronNumber = rsCron.getInt("cron_number");
						//通过计划和学号来统计缺勤
						stmt = conn.createStatement();
						ResultSet rsCheck = stmt.executeQuery("select * from check_info where cron_number = '"+cronNumber+"' and student_number = '"+studentNumber+"'");
						while(rsCheck.next()){
							String absent = rsCheck.getString("adsent");
							//allCount++;
							if(absent.equals("未考勤")){
									count++;
							}
							
						}
						
					}
				}
				
			}
			//String strAllCount = "";
			if(count != 0){	
				//strAllCount = strAllCount + subjectName+ " 共考勤  " + allCount +"次\n";
				tmp = tmp+subjectName +"  缺勤  " + count +"次" + "\n\n";
				
			}
			//名字+次数 = 字符串
			//str = str + strAllCount;
			str += tmp;
		}
		rsSubject.close();
		stmt.close();
		conn.close();
		if(str.equals("")){
			return "未缺勤";
		}
		return str;
	}
	
	//根据学号获得班级
	public String getClassByStudentNumber(long studentNumber) throws Exception{
		//int number = 0;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from student_info where student_number = '"+studentNumber+"'");
		while(rs.next()){
			int number = rs.getInt("class_number");
			stmt = conn.createStatement();
			ResultSet rsClass = stmt.executeQuery("select * from class_info where class_number = '"+number+"'");
			while(rsClass.next()){
				return rsClass.getString("name");
			}
			rsClass.close();
		}
		rs.close();
		stmt.close();
		conn.close();
		return "未找到班级";
		
	}
	//根据学号获得姓名
	public String getNameByStudentNumber(long studentNumber) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from student_info where student_number = '"+studentNumber+"'");
		while(rs.next()){
			return rs.getString("name");
		}
		return null;
	}
	//获得班级全部学号
	public List<Long> getClassNumberByOneNumber(Long number) throws Exception{
		String className = null;
		List<Long> list = new ArrayList<Long>();
		className = getClassByStudentNumber(number);
		int classNumber = getClassNumber(className);
		
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from student_info where class_number = '"+classNumber+"'");
		while(rs.next()){
			list.add(rs.getLong("student_number"));
		}
		return list;
	}
	//根据班级获得全部学号
	public List<Long> getClassNumberByClass(String className) throws Exception{
		List<Long> list = new ArrayList<Long>();
		int classNumber = getClassNumber(className);
		
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from student_info where class_number = '"+classNumber+"'");
		while(rs.next()){
			list.add(rs.getLong("student_number"));
		}
		return list;
	}
	//有名字的返回true
	public boolean isThisName(String name) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql = "select * from student_info where name = '"+ name +"'";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			return true;
		}
		return false;
	}
	//家长不知道学生的学号，通过姓名绑定，如果姓名唯一则返回学号
	public long isOneName(String name) throws Exception{
		int count = 0;
		long number = 0;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql = "select * from student_info where name = '"+ name +"'";
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			count++;
			number = rs.getLong("student_Number");
		}
		if(count>1){
			return -1;
		}else{
			return number;
		}
	}
	
	//查询parent_info表中是否绑定的,已绑定返回true 否则返回false
	public boolean subOver(String openid) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		String sql = "select * from parent_info where openid = '"+openid+"'";
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			return true;
		}
		return false;
	}
	
	
	//通过学号绑定 
	public String subByStudentNumber(long number, String openid) throws Exception, IllegalAccessException, ClassNotFoundException{
		String str = null;
		//String name = getNameByStudentNumber(number);
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn =  DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		String sql = "select * from student_info where student_number = '"+ number +"'";
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet result = stmt.executeQuery(sql);
		
		boolean flag = false;
		while(result.next()){
			flag = true;
			if(subOver(openid)){
				//update
				Decrition decrition = new Decrition();
				str = decrition.subSuccess();
				stmt = conn.createStatement();
				stmt.executeUpdate("update parent_info set student_number='"+number+"' where openid = '"+openid+"'");
			}else{
				//insert
				Decrition decrition = new Decrition();
				str = decrition.subSuccess();
				stmt = conn.createStatement();
				stmt.executeUpdate("insert into parent_info(openid, student_number,rank) values('"
						+ openid
						+ "','" 
						+ number
						+ "','" 
						+ 1
						+"')");
			}
			
			
		}
		result.close();
		stmt.close();
		conn.close();
		if(!flag){
			str = "请输入正确的学号！该学号有误。";
		}
		return str;
	}
	
	//清除一条数据记录 ，用户不关注的时候
	public void del(String openid) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		String sql = "delete from parent_info where openid = '" + openid + "'";
		stmt.executeUpdate(sql);
	}
	
	
	//获得前一天所有考勤情况
	public List<SummaryValue> getAllAbsent() throws Exception{
		List<SummaryValue> all = new ArrayList<SummaryValue>();
		SummaryValue info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		String sql = "SELECT * FROM summary_value WHERE DATE(date)>=DATE_SUB(CURDATE(),INTERVAL 7 DAY) AND DATE(date)<=DATE_SUB(CURDATE(),INTERVAL 1 DAY) ";//统计前7天的数据
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			info = new SummaryValue();
			info.setTeacherNumber(rs.getInt(1));
			info.setDepartmentNumber(rs.getInt(2));
			info.setSubjectName(rs.getString(3));
			info.setAll(rs.getInt(4));
			info.setAbsent(rs.getInt(5));
			info.setValue(rs.getString(6));
			info.setDate(rs.getDate(7));
			all.add(info);
		}
		return all;
	}
	//获得前一天学院所有考勤情况
	public List<SummaryValue> getDepartmentAbsent(int departmentNumber) throws Exception{
		List<SummaryValue> all = new ArrayList<SummaryValue>();
		SummaryValue info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		String sql = "SELECT * FROM summary_value WHERE DATE(date)>=DATE_SUB(CURDATE(),INTERVAL 7 DAY) AND DATE(date)<=DATE_SUB(CURDATE(),INTERVAL 1 DAY)";//统计前一天的数据
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			if(rs.getInt("department_number")==departmentNumber){
				info = new SummaryValue();
				info.setTeacherNumber(rs.getInt(1));
				info.setDepartmentNumber(rs.getInt(2));
				info.setSubjectName(rs.getString(3));
				info.setAll(rs.getInt(4));
				info.setAbsent(rs.getInt(5));
				info.setValue(rs.getString(6));
				info.setDate(rs.getDate(7));
				all.add(info);
			}
			
		}
		return all;
	}
	//获得前一天教师课程考勤情况
	public List<SummaryValue> getTeacherAbsent(int teacherNumber) throws Exception{
		List<SummaryValue> all = new ArrayList<SummaryValue>();
		SummaryValue info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		String sql = "SELECT * FROM summary_value WHERE DATE(date)>=DATE_SUB(CURDATE(),INTERVAL 7 DAY) AND DATE(date)<=DATE_SUB(CURDATE(),INTERVAL 1 DAY)";//统计前一天的数据
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			if(rs.getInt("teacher_number")==teacherNumber){
				info = new SummaryValue();
				info.setTeacherNumber(rs.getInt(1));
				info.setDepartmentNumber(rs.getInt(2));
				info.setSubjectName(rs.getString(3));
				info.setAll(rs.getInt(4));
				info.setAbsent(rs.getInt(5));
				info.setValue(rs.getString(6));
				info.setDate(rs.getDate(7));
				all.add(info);
			}
			
		}
		return all;
	}
	
	
}
