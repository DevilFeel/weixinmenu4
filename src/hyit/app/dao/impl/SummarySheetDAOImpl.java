package hyit.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hyit.app.dao.ISummarySheetDAO;
import hyit.app.model.SummarySheet;

public class SummarySheetDAOImpl implements ISummarySheetDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;

	public SummarySheetDAOImpl(Connection conn) {
		// TODO Auto-generated constructor stub
		this.conn = conn;
	}

	@Override
	public boolean doCreate(SummarySheet sheet) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		String sql = "INSERT INTO summary_sheet VALUES(NULL,?,?,?,?,?)";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setLong(1, sheet.getStudentNumber());
		this.pstmt.setInt(2, sheet.getSessionNumber());
		this.pstmt.setInt(3, sheet.getAbsenteeism());
		this.pstmt.setInt(4, sheet.getLate());
		this.pstmt.setInt(5, sheet.getCount());
		if (this.pstmt.executeUpdate() > 0) {
			flag = true;
		}
		this.pstmt.close();
		return flag;
	}

	@Override
	public boolean update(SummarySheet sheet) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		String sql = "UPDATE summary_sheet SET absenteeism = ?, late = ?,count = ? WHERE id = ?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, sheet.getAbsenteeism());
		this.pstmt.setInt(2, sheet.getLate());
		this.pstmt.setInt(3, sheet.getCount());
		this.pstmt.setInt(4, sheet.getId());
		if (this.pstmt.executeUpdate() > 0) {
			flag = true;
		}
		this.pstmt.close();
		return flag;
	}

	@Override
	public SummarySheet getByStudentNumberAndSessionNumber(Long studentNumber,
			Integer sessionNumber) throws Exception {
		// TODO Auto-generated method stub
		SummarySheet sheet = null;
		String sql = "SELECT id,studentNumber,sessionNumber,absenteeism,late,count "
				+ "FROM summary_sheet WHERE studentNumber = ? AND sessionNumber = ?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setLong(1, studentNumber);
		this.pstmt.setInt(2, sessionNumber);
		ResultSet rs = this.pstmt.executeQuery();
		while (rs.next()) {
			sheet = new SummarySheet();
			sheet.setId(rs.getInt(1));
			sheet.setStudentNumber(rs.getLong(2));
			sheet.setSessionNumber(rs.getInt(3));
			sheet.setAbsenteeism(rs.getInt(4));
			sheet.setLate(rs.getInt(5));
			sheet.setCount(rs.getInt(6));
		}
		this.pstmt.close();
		return sheet;
	}

	@Override
	public List<SummarySheet> getByStudentNumber(Long number) throws Exception {
		// TODO Auto-generated method stub
		List<SummarySheet> all = new ArrayList<SummarySheet>();
		SummarySheet sheet = null;
		String sql = "SELECT id,studentNumber,sessionNumber,absenteeism,late,count "
				+ "FROM summary_sheet WHERE studentNumber = ?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setLong(1, number);
		ResultSet rs = this.pstmt.executeQuery();
		while (rs.next()) {
			sheet = new SummarySheet();
			sheet.setId(rs.getInt(1));
			sheet.setStudentNumber(rs.getLong(2));
			sheet.setSessionNumber(rs.getInt(3));
			sheet.setAbsenteeism(rs.getInt(4));
			sheet.setLate(rs.getInt(5));
			sheet.setCount(rs.getInt(6));
			all.add(sheet);
		}
		this.pstmt.close();
		return all;
	}

	@Override
	public List<SummarySheet> getBySessionNumber(Integer number)
			throws Exception {
		// TODO Auto-generated method stub
		List<SummarySheet> all = new ArrayList<SummarySheet>();
		SummarySheet sheet = null;
		String sql = "SELECT id,studentNumber,sessionNumber,absenteeism,late,count "
				+ "FROM summary_sheet WHERE sessionNumber = ?";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, number);
		ResultSet rs = this.pstmt.executeQuery();
		while (rs.next()) {
			sheet = new SummarySheet();
			sheet.setId(rs.getInt(1));
			sheet.setStudentNumber(rs.getLong(2));
			sheet.setSessionNumber(rs.getInt(3));
			sheet.setAbsenteeism(rs.getInt(4));
			sheet.setLate(rs.getInt(5));
			sheet.setCount(rs.getInt(6));
			all.add(sheet);
		}
		this.pstmt.close();
		return all;
	}

	@Override
	public List<SummarySheet> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
