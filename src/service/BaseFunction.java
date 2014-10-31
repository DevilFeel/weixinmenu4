package service;

import hyit.app.model.CheckInfo;
import hyit.app.model.ClassInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.DepartmentInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.SessionInfo;
import hyit.app.model.StudentInfo;
import hyit.app.model.SubjectInfo;
import hyit.app.model.SummaryValue;
import hyit.app.model.TeacherInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseFunction {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	//获得rank from teacher_info;
	public int getRank(String account) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from teacher_info where account = '"+account+"'");
		while(rs.next()){
			return rs.getInt("rank");
		}
		return -1;
	}
	//获得rank from parent_info
	public int getRankFromParent(String openid) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+openid+"'");
		while(rs.next()){
			return rs.getInt("rank");
		}
		return -1;
	}
	//获得teacher_number  By String account
	public int getTeacherNumber(String account) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from teacher_info where account = '"+account+"'");
		while(rs.next()){
			return rs.getInt("teacher_number");
		}
		return -1;
	}
	
	//验证班级
	public boolean isExistClass(String className) throws Exception{
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM class_info WHERE `name` = '"+className+"'");
		while(rs.next()){
			return true;
		}
		return false;
	}
	//获得所有学院编号
	public List<DepartmentInfo> getAllDepartmentInfo() throws Exception{
		List<DepartmentInfo> all = new ArrayList<DepartmentInfo>();
		DepartmentInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM department_info");
		while(rs.next()){
			info = new DepartmentInfo();
			info.setDepartmentNumber(rs.getInt(1));
			info.setName(rs.getString(2));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	//通过学院昵称获得学院编号
	public int getDepartmentNumber(String name) throws Exception{
		int number = -1;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM department_info WHERE `name` = '"+name+"'");
		while(rs.next()){
			number = rs.getInt("department_number");
		}
		rs.close();
		stmt.close();
		conn.close();
		return number;
	}
	
	//获得所有班级编号通过学院编号
	public List<ClassInfo> getClassInfo(int number) throws Exception{
		List<ClassInfo> all = new ArrayList<ClassInfo>();
		ClassInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM class_info WHERE department_number = '"+number+"'");
		while(rs.next()){
			info = new ClassInfo();
			info.setClassNumber(rs.getInt(1));
			info.setName(rs.getString(2));
			info.setDepartmentNumber(rs.getInt(3));
			info.setTeacherNumber(rs.getInt(4));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//获得所有学号 通过班级
	public List<StudentInfo> getStudentInfo(int number) throws Exception{
		List<StudentInfo> all = new ArrayList<StudentInfo>();
		StudentInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM student_info WHERE class_number = '"+number+"'");
		while(rs.next()){
			info = new StudentInfo() ;
			info.setStudentNumber(rs.getLong(1));
			info.setName(rs.getString(2));
			info.setCardMac(rs.getString(3));
			info.setEnterYear(rs.getString(4));
			info.setClassNumber(rs.getInt(5));
			info.setCardID(rs.getInt(6));
			info.setDepartmentNumber(rs.getInt(7));
			info.setProfession(rs.getString(8));
			info.setSex(rs.getString(9));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//获得所有subject_info信息
	public List<SubjectInfo> getAllSubjectInfo() throws Exception{
		List<SubjectInfo> all = new ArrayList<SubjectInfo>();
		SubjectInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM subject_info");
		while(rs.next()){
			info = new SubjectInfo();
			info.setSubjectNumber(rs.getInt(1));
			info.setName(rs.getString(2));
			info.setSemesterNumber(rs.getInt(3));
			info.setTeacherNumber(rs.getInt(4));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//获得subject_number by课程昵称
	public int getSubjectNumber(String name) throws Exception{
		int number = -1;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM subject_info WHERE `name` = '"+name+"'");
		while(rs.next()){
			number = rs.getInt("subject_number");
		}
		rs.close();
		stmt.close();
		conn.close();
		return number;
	}
	
	//session_number  By subjce_number
	public List<SessionInfo> getSessionInfo(int number) throws Exception{
		List<SessionInfo> all = new ArrayList<SessionInfo>();
		SessionInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM session_info WHERE subject_number = '"+number +"'");
		while(rs.next()){
			info = new SessionInfo();
			info.setSessionNumber(rs.getInt(1));
			info.setSubjectNumber(rs.getInt(2));
			info.setClassName(rs.getString(3));
			info.setStartWeek(rs.getInt(4));
			info.setEndWeek(rs.getInt(5));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//lesson_number By session_number
	public List<LessonInfo> getlessonInfo(int number) throws Exception{
		List<LessonInfo> all = new ArrayList<LessonInfo>();
		LessonInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM lesson_info WHERE session_number= '"+number +"'");
		while(rs.next()){
			info = new LessonInfo();
			info.setLessonNumber(rs.getInt(1));
			info.setSessionNumber(rs.getInt(2));
			info.setDayOfWeek(rs.getInt(3));
			info.setEvenOld(rs.getInt(4));
			info.setStartLesson(rs.getInt(5));
			info.setEndLesson(rs.getInt(6));
			info.setClassroom(rs.getString(7));
			all.add(info);
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//cron_number By lesson_number 并且状态值为1的
	public List<CronInfo> getCronInfo(int number) throws Exception{
		List<CronInfo> all = new ArrayList<CronInfo>();
		CronInfo info = null;
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM cron_info WHERE lesson_number= '"+number +"' and DATE(execute_date)=DATE_SUB('2014-10-26',INTERVAL 0 DAY)");
		while(rs.next()){
			info = new CronInfo();
			if(rs.getInt("status")==1){
				info.setCronNumber(rs.getInt(1));
				info.setLessonNumber(rs.getInt(2));
				info.setExecuteTime(rs.getTime(3));
				info.setExecuteDate(rs.getDate(4));
				info.setWeek(rs.getInt(5));
				info.setClassroom(rs.getString(6));
				info.setOrderTime(rs.getTimestamp(7));
				info.setStatus(rs.getInt(8));
				all.add(info);
			}
			
			
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//根据Cron_number 是否 未考勤 , 未考勤则返回true
		public List<CheckInfo> getCheckInfo(int cronNumber) throws Exception{
			List<CheckInfo> all = new ArrayList<>();
			CheckInfo info = null;
			Class.forName(DBDRIVER).newInstance();
			Connection conn = null;
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			Statement stmt;
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM check_info WHERE cron_number = '"+cronNumber+"'");
			while(rs.next()){
				info = new CheckInfo();
				info.setCkNumber(rs.getInt(1));
				info.setCronNumber(rs.getInt(2));
				info.setStudentNumber(rs.getLong(3));
				info.setAbsent(rs.getString(4));
				info.setDate(rs.getDate(5));
				info.setTime(rs.getTime(6));
				all.add(info);
			}
			rs.close();
			stmt.close();
			conn.close();
			return all;
		}
		
		//插入数据库
		public boolean docreateSummaryValue(SummaryValue value) throws Exception{
			Class.forName(DBDRIVER).newInstance();
			Connection conn = null;
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			PreparedStatement pstmt = null;
			boolean flag = false;
			String sql = "INSERT INTO summary_value VALUES(?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, value.getTeacherNumber());
			pstmt.setInt(2, value.getDepartmentNumber());
			pstmt.setString(3, value.getSubjectName());
			pstmt.setInt(4, value.getAll());
			pstmt.setInt(5, value.getAbsent());
			pstmt.setString(6, value.getValue());
			pstmt.setDate(7, value.getDate());
			if (pstmt.executeUpdate() > 0) {
				flag = true;
			}
			pstmt.close();
			return flag;	
		}
		//根据teacherNumber获得DepartmentNumber
		public TeacherInfo getTeacherInfo(int number) throws Exception{
			TeacherInfo teacherInfo = null;
			Class.forName(DBDRIVER).newInstance();
			Connection conn = null;
			conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
			Statement stmt;
			stmt = conn.createStatement();
			String sql = "SELECT * FROM teacher_info WHERE teacher_number = '"+number+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()){
				teacherInfo = new TeacherInfo();
				teacherInfo.setTeacherNumber(rs.getInt(1));
				teacherInfo.setName(rs.getString(2));
				teacherInfo.setCardMac(rs.getString(3));
				teacherInfo.setAccount(rs.getString(4));
				teacherInfo.setPassword(rs.getString(5));
				teacherInfo.setRank(rs.getInt(6));
				teacherInfo.setEmail(rs.getString(7));
				teacherInfo.setDepartmentNumber(rs.getInt(8));
			}
			return teacherInfo;
		}
}
