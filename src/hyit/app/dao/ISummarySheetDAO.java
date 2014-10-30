package hyit.app.dao;

import java.util.List;

import hyit.app.model.SummarySheet;

public interface ISummarySheetDAO {
	public boolean doCreate(SummarySheet sheet) throws Exception;

	public boolean update(SummarySheet sheet) throws Exception;

	public SummarySheet getByStudentNumberAndSessionNumber(Long studentNumber,
			Integer sessionNumber) throws Exception;

	public List<SummarySheet> getByStudentNumber(Long number) throws Exception;

	public List<SummarySheet> getBySessionNumber(Integer number)
			throws Exception;

	public List<SummarySheet> getAll() throws Exception;
}
