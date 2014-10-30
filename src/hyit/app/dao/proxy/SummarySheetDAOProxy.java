package hyit.app.dao.proxy;

import java.util.List;

import hyit.app.dao.ISummarySheetDAO;
import hyit.app.dao.impl.SummarySheetDAOImpl;
import hyit.app.dbc.DatabaseConnection;
import hyit.app.model.SummarySheet;

public class SummarySheetDAOProxy implements ISummarySheetDAO {
	private DatabaseConnection dbc = null;
	private ISummarySheetDAO dao = null;

	public SummarySheetDAOProxy() throws Exception {
		// TODO Auto-generated constructor stub
		this.dbc = new DatabaseConnection();
		this.dao = new SummarySheetDAOImpl(this.dbc.getConnection());
	}

	@Override
	public boolean doCreate(SummarySheet sheet) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			if (this.dao.getByStudentNumberAndSessionNumber(
					sheet.getStudentNumber(), sheet.getSessionNumber()) == null) {
				flag = this.dao.doCreate(sheet);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			this.dbc.close();
		}
		return flag;
	}

	@Override
	public boolean update(SummarySheet sheet) throws Exception {
		// TODO Auto-generated method stub
		boolean flag = false;
		try {
			flag = this.dao.update(sheet);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			this.dbc.close();
		}
		return flag;
	}

	@Override
	public SummarySheet getByStudentNumberAndSessionNumber(Long studentNumber,
			Integer sessionNumber) throws Exception {
		// TODO Auto-generated method stub
		SummarySheet sheet = null;
		try {
			sheet = this.dao.getByStudentNumberAndSessionNumber(studentNumber,
					sessionNumber);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			this.dbc.close();
		}
		return sheet;
	}

	@Override
	public List<SummarySheet> getByStudentNumber(Long number) throws Exception {
		// TODO Auto-generated method stub
		List<SummarySheet> all = null;
		try {
			all = this.dao.getByStudentNumber(number);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			this.dbc.close();
		}
		return all;
	}

	@Override
	public List<SummarySheet> getBySessionNumber(Integer number)
			throws Exception {
		// TODO Auto-generated method stub
		List<SummarySheet> all = null;
		try {
			all = this.dao.getBySessionNumber(number);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		} finally {
			this.dbc.close();
		}
		return all;
	}

	@Override
	public List<SummarySheet> getAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
