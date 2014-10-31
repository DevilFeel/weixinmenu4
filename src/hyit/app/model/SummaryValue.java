package hyit.app.model;

import java.sql.Date;

public class SummaryValue {
	private int teacherNumber;
	private int departmentNumber;
	private String subjectName;
	private int all;
	private int absent;
	private String value;
	private Date date;
	public int getDepartmentNumber() {
		return departmentNumber;
	}
	public void setDepartmentNumber(int departmentNumber) {
		this.departmentNumber = departmentNumber;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	public int getAbsent() {
		return absent;
	}
	public void setAbsent(int absent) {
		this.absent = absent;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getTeacherNumber() {
		return teacherNumber;
	}
	public void setTeacherNumber(int teacherNumber) {
		this.teacherNumber = teacherNumber;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
