package service;

import hyit.app.model.ClassInfo;

import java.sql.Connection;
import java.sql.DriverManager;
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
	public List<Integer> getClassNumber(int number) throws Exception{
		List<Integer> all = new ArrayList<Integer>();
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM class_info WHERE department_number = '"+number+"'");
		while(rs.next()){
			all.add(rs.getInt("class_number"));
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//获得所有学号 通过班级
	public List<Integer> getStudentNumber(int number) throws Exception{
		List<Integer> all = new ArrayList<Integer>();
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM student_info WHERE class_number = '"+number+"'");
		while(rs.next()){
			all.add(rs.getInt("student_number"));
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
	public List<Integer> getSessionNumber(int number) throws Exception{
		List<Integer> all = new ArrayList<Integer>();
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM session_info WHERE subject_number = '"+number +"'");
		while(rs.next()){
			all.add(rs.getInt("session_number"));
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//lesson_number By session_number
	public List<Integer> getlessonNumber(int number) throws Exception{
		List<Integer> all = new ArrayList<Integer>();
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM lesson_info WHERE lesson_number= '"+number +"'");
		while(rs.next()){
			all.add(rs.getInt("lesson_number"));
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
	
	//cron_number By lesson_number 并且状态值为1的
	public List<Integer> getCronNumber(int number) throws Exception{
		List<Integer> all = new ArrayList<Integer>();
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM cron_info WHERE cron_number= '"+number +"'");
		while(rs.next()){
			if(rs.getInt("status")==1){
				all.add(rs.getInt("cron_number"));
			}
		}
		rs.close();
		stmt.close();
		conn.close();
		return all;
	}
}
