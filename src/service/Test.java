package service;

import hyit.app.model.ClassInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.SessionInfo;
import hyit.app.model.SubjectInfo;
import hyit.app.model.SummaryValue;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception{
		
		BaseFunction bf = new BaseFunction();
		//������пγ���Ϣ
		ServiceFunction sf = new ServiceFunction();
		List<SummaryValue> list = sf.getAllAbsent();
		System.out.println(list.size());
		
	}
}
