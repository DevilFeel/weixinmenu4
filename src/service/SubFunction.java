package service;

import hyit.app.model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubFunction {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	//��֤�û���
	public boolean checkUserPwd(String user, String pwd) throws Exception{
		//return 2 �û������붼��ȷ��return 1�û�����ȷ��������� return 0 ����ȷ
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from teacher_info where account = '"+user+"'");
		
		while(rs.next()){
			if(rs.getString("password").equals(pwd)){
				return true; 
			}
		}
		return false;
	}
	
	//�û�����֤�ɹ��������ݿ�
	public String importUser(String user, String openid) throws Exception{
		BaseFunction baseFunction = new BaseFunction();
		int rank = baseFunction.getRank(user); //Ȩ�޵ȼ�
		int teacherNumber = baseFunction.getTeacherNumber(user);
		Class.forName(DBDRIVER).newInstance();
		Connection conn = null;
		conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
		Statement stmt;
		stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+openid+"'");
		boolean flag = false;
		while(rs.next()){
			flag = true; //�и�openid
		}
		if(flag){
			stmt.executeUpdate("UPDATE parent_info SET teacher_number = '"+teacherNumber+"',rank = '"+rank+"' WHERE openid = '"+openid+"'");
			return "�û��ɹ����°󶨣�";
		}else{
			stmt.executeUpdate("insert into parent_info(openid, teacher_number, rank) values('"
						+ openid
						+ "','" 
						+ teacherNumber
						+ "','" 
						+ rank
						+"')");
			return "�û��ɹ��󶨣�";
		}
	}
}
