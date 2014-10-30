package hyit.app.trigger;

import hyit.app.factory.DAOFactory;
import hyit.app.model.SummarySheet;

public class SummaryAbsentInfo {
	public static boolean summary(Long studentNumber,Integer sessionNumber,Integer status) {	//status为旷课标识0表正常，1为旷课，2为迟到
		SummarySheet sheet = null;
		boolean flag = false;
		try {
			sheet = DAOFactory.getISummarySheetDAOInstance()
					.getByStudentNumberAndSessionNumber(studentNumber,sessionNumber);
		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
		}
		if(sheet!=null){
			Integer absent = sheet.getAbsenteeism();
			Integer count = sheet.getCount();
			Integer late = sheet.getLate();
			if(status==1){
				absent++;
			}else if (status==2) {
				late++;
			}
			count++;
			sheet.setAbsenteeism(absent);
			sheet.setCount(count);
		}else{
			sheet = new SummarySheet();
			sheet.setStudentNumber(studentNumber);
			sheet.setSessionNumber(sessionNumber);
			if(status==1){
				sheet.setAbsenteeism(1);
				sheet.setLate(0);
			}else if(status==2){
				sheet.setAbsenteeism(0);
				sheet.setLate(1);
			}else if(status==0){
				sheet.setAbsenteeism(0);
				sheet.setLate(0);
			}
			sheet.setCount(1);
			try {
				flag = DAOFactory.getISummarySheetDAOInstance().doCreate(sheet);
			} catch (Exception e) {
				// TODO: handle exception
				flag = false;
			}
		}
		return flag;
	}
}
