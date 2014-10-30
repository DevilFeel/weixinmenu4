package hyit.app.dao;

import java.util.List;

import hyit.app.model.ParentInfo;

public interface IParentInfoDAO {
	public boolean doCreate(ParentInfo info) throws Exception;

	public boolean update(ParentInfo info) throws Exception;

	public ParentInfo delete(Integer number) throws Exception;

	public ParentInfo getByID(Integer number) throws Exception;

	public List<ParentInfo> getByStudentNumber(Long number) throws Exception;

	public List<ParentInfo> getAll() throws Exception;
}
