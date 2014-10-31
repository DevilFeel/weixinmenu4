package service;

import hyit.app.model.CronInfo;
import hyit.app.model.LessonInfo;
import hyit.app.model.SessionInfo;

import java.util.ArrayList;
import java.util.List;

public class SummaryAbsent {
	//获得一门课的所有cron_number
	public List<Integer> getCron(int subjectNumber) throws Exception{
		BaseFunction bf = new BaseFunction();
		List<Integer> all = new ArrayList<Integer>();
		
		List<SessionInfo> listSessionInfo = new ArrayList<SessionInfo>(); //session
		List<LessonInfo> listLessonInfo = new ArrayList<LessonInfo>(); //lesson
		List<CronInfo> listCronInfo = new ArrayList<CronInfo>(); //cron
		//获得session_number
		listSessionInfo = bf.getSessionInfo(subjectNumber);
		for(int is = 0; is<listSessionInfo.size(); is++){
			SessionInfo sessionInfo = listSessionInfo.get(is);
			
			//lesson_number
			listLessonInfo = bf.getlessonInfo(sessionInfo.getSessionNumber());
			for(int il=0; il<listLessonInfo.size(); il++){
				LessonInfo lessonInfo = listLessonInfo.get(il);
				
				//cron
				listCronInfo = bf.getCronInfo(lessonInfo.getLessonNumber());
				for(int ic=0; ic<listCronInfo.size(); ic++){
					CronInfo cronInfo = listCronInfo.get(ic);
					int cronNumber = cronInfo.getCronNumber();
					all.add(cronNumber);
				}
			}
		}
		return all;	
	}
	
	
}
