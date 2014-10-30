package hyit.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hyit.app.dao.ICheckInfoDAO;
import hyit.app.model.CheckInfo;

public class CheckInfoDAOImpl implements ICheckInfoDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public CheckInfoDAOImpl(Connection conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
	}

	@Override
	public boolean doCreate(CheckInfo info) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		String sql = "INSERT INTO check_info VALUES(NULL,?,?,?,?,?)";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, info.getCronNumber());
		this.pstmt.setLong(2, info.getStudentNumber());
		this.pstmt.setString(3, info.getAbsent());
		this.pstmt.setDate(4, info.getDate());
		this.pstmt.setTime(5, info.getTime());
		if (this.pstmt.executeUpdate() > 0) {
			flag = true;
		}
		this.pstmt.close();
		return flag;
	}

	@Override
	public boolean update(CheckInfo info) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CheckInfo delete(Integer number) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CheckInfo getByID(Integer number) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CheckInfo> getByCronNumber(Integer number) throws Exception {
		// TODO Auto-generated method stub
		List<CheckInfo> all = new ArrayList<CheckInfo>();
		CheckInfo info = null;
		String sql = "SELECT ck_number,cron_number,student_number,adsent,date,time FROM check_info WHERE cron_number = ?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, number);
		ResultSet rs = this.pstmt.executeQuery();
		while (rs.next()) {
			info = new CheckInfo();
			info.setCkNumber(rs.getInt(1));
			info.setCronNumber(rs.getInt(2));
			info.setStudentNumber(rs.getLong(3));
			info.setAbsent(rs.getString(4));
			info.setDate(rs.getDate(5));
			info.setTime(rs.getTime(6));
			all.add(info);
		}
		this.pstmt.close();
		return all;
	}

	@Override
	public List<CheckInfo> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
