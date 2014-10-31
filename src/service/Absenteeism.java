package service;

import hyit.app.model.CheckInfo;
import hyit.app.model.ClassInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.DepartmentInfo;
import hyit.app.model.StudentInfo;
import hyit.app.model.SubjectInfo;
import hyit.app.model.SummaryValue;
import hyit.app.model.TeacherInfo;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Absenteeism {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BaseFunction baseFunction = new BaseFunction();
		SummaryAbsent sa = new SummaryAbsent();
		SummaryValue sv = null;
		List<SubjectInfo> listSubjcetInfo = new ArrayList<SubjectInfo>();
		List<Integer> listCronNumber = new ArrayList<Integer>();
		List<CheckInfo> listCheckInfo = new ArrayList<CheckInfo>();
		//获得所有课程
		listSubjcetInfo = baseFunction.getAllSubjectInfo();
		
		for(int js=0; js<listSubjcetInfo.size(); js++){
			SubjectInfo subjectInfo = listSubjcetInfo.get(js);
			String subjectName = subjectInfo.getName();
			int subjectNumber = subjectInfo.getSubjectNumber();
			int teacherNumber = subjectInfo.getTeacherNumber();
			TeacherInfo teacherInfo = baseFunction.getTeacherInfo(teacherNumber);
			int departmentNumber = teacherInfo.getDepartmentNumber();
			Date date = null;
			//获得课程计划
			listCronNumber = sa.getCron(subjectNumber);
			int countAll = listCronNumber.size();
			int countAbsent = 0;
			for(int jc=0; jc<listCronNumber.size(); jc++){
				int cronNumber = listCronNumber.get(jc);
				
				listCheckInfo = baseFunction.getCheckInfo(cronNumber);
				for(int jch =0; jch<listCheckInfo.size(); jch++){
					CheckInfo checkInfo = listCheckInfo.get(jch);
					date = checkInfo.getDate();
					if(checkInfo.getAbsent().equals("未考勤")){
						countAbsent++;
					}
					countAll++;
				}
				
			}
			if(countAll != 0){
				//System.out.print("今日无考勤");
				double value = (double)countAbsent / countAll ;
				NumberFormat nt = NumberFormat.getPercentInstance();
				nt.setMinimumFractionDigits(2);
				System.out.println(nt.format(value));
				String str = String.valueOf(nt.format(value));
				System.out.println(str);
				sv = new SummaryValue();
				sv.setTeacherNumber(teacherNumber);
				sv.setDepartmentNumber(departmentNumber);
				sv.setSubjectName(subjectName);
				sv.setAll(countAll);
				sv.setAbsent(countAbsent);
				sv.setValue(str);
				sv.setDate(date);
				if(baseFunction.docreateSummaryValue(sv)){
					System.out.println("导入成功");
				}
				
			}else{
				String str = "";
				str = subjectName + "共考勤人数:"+countAll + " 缺勤人数" + countAbsent;
				System.out.println(str);
			}	
		}
		
	}

}
