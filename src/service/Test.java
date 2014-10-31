package service;

import hyit.app.model.ClassInfo;
import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.SessionInfo;
import hyit.app.model.SubjectInfo;

import java.util.ArrayList;
import java.util.List;

public class Test {
	public static void main(String[] args) throws Exception{
		
		BaseFunction bf = new BaseFunction();
		//获得所有课程信息
		List<SubjectInfo> listSubjectInfo = new ArrayList<>(); //subject
		List<SessionInfo> listSessionInfo = new ArrayList<>(); //session
		List<LessonInfo> listLessonInfo = new ArrayList<>(); //lesson
		List<CronInfo> listCronInfo = new ArrayList<>(); //cron
		
		
		listSubjectInfo = bf.getAllSubjectInfo();
		
		for(int i=0; i<listSubjectInfo.size(); i++){
			SubjectInfo subjectInfo = listSubjectInfo.get(i);
			//System.out.println(subjectInfo.getName()+"subject");
			//获得session_number
			listSessionInfo = bf.getSessionInfo(subjectInfo.getSubjectNumber());
			for(int is = 0; is<listSessionInfo.size(); is++){
				SessionInfo sessionInfo = listSessionInfo.get(is);
				//System.out.println(sessionInfo.getSessionNumber()+" session");
				//lesson_number
				listLessonInfo = bf.getlessonInfo(sessionInfo.getSessionNumber());
				for(int il=0; il<listLessonInfo.size(); il++){
					LessonInfo lessonInfo = listLessonInfo.get(il);
					//System.out.println(lessonInfo.getLessonNumber()+" lesson");
					//cron
					listCronInfo = bf.getCronInfo(lessonInfo.getLessonNumber());
					for(int ic=0; ic<listCronInfo.size(); ic++){
						CronInfo cronInfo = listCronInfo.get(ic);
						int cronNumber = cronInfo.getCronNumber();
						System.out.println(cronNumber + " cron" + cronInfo.getStatus());
					}
				}
			}
		}
		
	}
}
