package service;



import hyit.app.factory.DAOFactory;
import hyit.app.model.DepartmentInfo;
import hyit.app.model.SummarySheet;
import hyit.app.model.SummaryValue;
import hyit.app.model.TeacherInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import message.resp.Article;
import message.resp.NewsMessage;
import message.resp.TextMessage;
import util.MessageUtil;

public class CoreService {
//	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
//	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
//	private static final String dbUser = "root";
//	private static final String dbPwd = "nicai";
	//防SQL注入
	private static Logger logger = Logger.getLogger(CoreService.class);
	public static String TransactSQLInjection(String str)

    {

          return str.replaceAll(".*([';]+|(--)+).*", " ");

    }
	
	public static String processRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		String respMessage = null;
		try{
			//默认返回的文本消息内容
			String respContent = "请先绑定帐号哦！";
			
			//xml解析
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			
			//发送方帐号(open_id)
			String fromUserName = requestMap.get("FromUserName");
			
			//公众帐号
			String toUserName = requestMap.get("ToUserName");
			
			//消息类型
			String msgType = requestMap.get("MsgType");
			
			//回复文本消息
			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);
			
			//List<Article> articleList = new ArrayList<Article>();
			//创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			
			//List<Article> article = new ArrayList<Article>();
			
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String reqContent = requestMap.get("Content").replace(" ", "");
				if(reqContent.startsWith("AA")){
					String reqStudentId = reqContent.substring(2);
					long studentNumber = Long.parseLong(reqStudentId);
					ServiceFunction serviceFunction = new ServiceFunction();
					respContent = serviceFunction.subByStudentNumber(studentNumber, fromUserName);
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
					
					
				}else if(reqContent.startsWith("姓名")){
					String reqName = reqContent.substring(2);
					ServiceFunction serviceFunction = new ServiceFunction();
					boolean isThisName = serviceFunction.isThisName(reqName);
					if(isThisName){
						if(serviceFunction.isOneName(reqName) != -1){
							long studentNumber = serviceFunction.isOneName(reqName);
							respContent = serviceFunction.subByStudentNumber(studentNumber, fromUserName);
						}else{
							respContent = "该姓名重复，请输入学号来进行绑定！";
						}
					}else{
						respContent = "该姓名不存在，请重新输入！";
					}
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);			
				}else if(reqContent.startsWith("用户")){
					String reqUser = reqContent.substring(2);
					String user = reqUser.substring(0,reqUser.lastIndexOf("#"));
					String pwd = reqUser.substring(reqUser.lastIndexOf("#")+1);
					SubFunction subFuction = new SubFunction();
					Decrition decrition = new Decrition();
					//验证
					if(subFuction.checkUserPwd(user, pwd)){
						respContent = subFuction.importUser(user, fromUserName);
					}else{
						respContent = "用户名或密码有误，请重新输入。\n"+decrition.subUserPwd();
					}
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}else if(reqContent.startsWith("班级")){
					String reqClassName = reqContent.substring(2);
					
					ServiceFunction serviceFunction = new ServiceFunction();
					BaseFunction baseFunction = new BaseFunction();
					Decrition decrition = new Decrition();
					if(baseFunction.isExistClass(reqClassName)){
						int rank = baseFunction.getRankFromParent(fromUserName);
						
						//Long studentNumber = serviceFunction.getStudentNumberByOpenid(fromUserName);
						if(rank ==1 || rank == 2 || rank==3 || rank ==0){
							SummarySheet summary = null;
							List<Long> listAllStudentNumber = null; //全班学号
							Long number = null; //学号
							List<SummarySheet> list = null;
							String str = "";
							listAllStudentNumber = serviceFunction.getClassNumberByClass(reqClassName);
							Iterator<Long> iterAll = listAllStudentNumber.iterator();
							while(iterAll.hasNext()){
								number = iterAll.next();
								list = DAOFactory.getISummarySheetDAOInstance().getByStudentNumber(number);
								Iterator<SummarySheet> iter = list.iterator();
								while(iter.hasNext()){
									summary = iter.next();
									if(summary.getAbsenteeism() >0){
										String studentName = serviceFunction.getNameByStudentNumber(summary.getStudentNumber());
										str = str + studentName + " ";
										break;
									}
									
								}
							}
							respContent =reqClassName + "班的缺勤人员有：\n"+ str;
						}else{
							respContent = "请先绑定";
						}
					}else{
						respContent = "不存在该班级，请重新输入\n" + decrition.absentClass();
					}
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);	
				}
				else if(reqContent.startsWith("学院")){
					String reqDepartmentNumber = reqContent.substring(2);
					int departmentNumber = Integer.parseInt(reqDepartmentNumber);
				
					ServiceFunction sf = new ServiceFunction();
					BaseFunction bf = new BaseFunction();
					int rank = bf.getRankFromParent(fromUserName);
					if(rank==3 || rank==0){
						List<SummaryValue> listSummaryValue = new ArrayList<SummaryValue>();
						String str = "";
						listSummaryValue = sf.getDepartmentAbsent(departmentNumber);
						if(listSummaryValue.size()==0){
							respContent = "最近一周没有数据";
						}else{
							for(int i=0; i<listSummaryValue.size(); i++){
								SummaryValue sv = listSummaryValue.get(i);
								str = str+ sv.getDate().toString()+"\n" +"["+ sv.getSubjectName()+"]的考勤人数为："+ sv.getAll()
										+ ",缺勤人数为：" + sv.getAbsent() +"，考勤率为：" + sv.getValue()
										+ "\n";
								
							}
							respContent = "最近一周考勤情况：\n" + str;
						}
					}else{
						respContent = "你 的账号等级不符。";
					}
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				else if(reqContent.startsWith("教师")){
					String reqTeacherNumber = reqContent.substring(2);
					int teacherNumber = Integer.parseInt(reqTeacherNumber);
				
					ServiceFunction sf = new ServiceFunction();
					BaseFunction bf = new BaseFunction();
					int rank = bf.getRankFromParent(fromUserName);
					if(rank==3 || rank==0 || rank==2){
						List<SummaryValue> listSummaryValue = new ArrayList<SummaryValue>();
						String str = "";
						listSummaryValue = sf.getTeacherAbsent(teacherNumber);
						if(listSummaryValue.size()==0){
							respContent = "最近一周没有数据";
						}else{
							for(int i=0; i<listSummaryValue.size(); i++){
								SummaryValue sv = listSummaryValue.get(i);
								str = str+ sv.getDate().toString()+"\n" +"["+ sv.getSubjectName()+"]的考勤人数为："+ sv.getAll()
										+ ",缺勤人数为：" + sv.getAbsent() +"，考勤率为：" + sv.getValue()
										+ "\n";
								
							}
							respContent = "最近一周考勤情况：\n" + str;
						}
					}else{
						respContent = "你 的账号等级不符。";
					}
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				else{
					Decrition decrition = new Decrition();
					respContent = decrition.getFirstCustomize();
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				
//				respContent = "您发送的是文本消息！";
			}
			// 图片消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
				respContent = "您发送的是图片消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 地理位置消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
				respContent = "您发送的是地理位置消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 链接消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
				respContent = "您发送的是链接消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 音频消息
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
				respContent = "您发送的是音频消息！";
				textMessage.setContent(respContent);
				respMessage = MessageUtil.textMessageToXml(textMessage);
			}
			// 事件推送
			else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					
					Decrition decrition = new Decrition();
					respContent = decrition.getCustomizeMenu();
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
					ServiceFunction serviceFunction = new ServiceFunction();
					serviceFunction.del(fromUserName);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");

					if (eventKey.equals("11")) {
						Decrition decrition = new Decrition();
						respContent = decrition.subName();
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("12")) {
						Decrition decrition = new Decrition();
						respContent = decrition.subStudentNumber();
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("13")) {
						
						Decrition decrition = new Decrition();
						respContent = decrition.subUserPwd();
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					}  else if (eventKey.equals("21")) {
						ServiceFunction serviceFunction = new ServiceFunction();
						long number = serviceFunction.getStudentNumberByOpenid(fromUserName);
						String name = serviceFunction.getNameByStudentNumber(number);
						
						if(number !=0){
							SummarySheet summary = null;
							List<SummarySheet> list = null;
							String str = "";
							
							list = DAOFactory.getISummarySheetDAOInstance().getByStudentNumber(number);
							Iterator<SummarySheet> iter = list.iterator();
							while(iter.hasNext()){
								summary = iter.next();
								int sessionNumber = summary.getSessionNumber();
								int subjectNumber = serviceFunction.getSubjectNumberBySessionNumber(sessionNumber);
								String subjectName = serviceFunction.getNameBySubjectNumber(subjectNumber);
								str = str + subjectName +" 缺勤"+summary.getAbsenteeism() + "次\n";
							}
							respContent =name + "\n" + str;
						}else{
							respContent = "请绑定学号";
						}
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("22")) {
						BaseFunction bf = new BaseFunction();
						ServiceFunction sf = new ServiceFunction();
						List<SummaryValue> listSummaryValue = new ArrayList<SummaryValue>();
						int teacherNumber = sf.getTeacherNumberByOpenid(fromUserName);
						//java.sql.Date date = null;
						String str = "";
						int rank = bf.getRankFromParent(fromUserName);
						if(rank==1){
							
							listSummaryValue = sf.getTeacherAbsent(teacherNumber);
							if(listSummaryValue.size()==0){
								respContent = "最近一周没有数据";
							}else{
								for(int i=0; i<listSummaryValue.size(); i++){
									SummaryValue sv = listSummaryValue.get(i);
									str = str+ sv.getDate().toString()+"\n" +"["+ sv.getSubjectName()+"]的考勤人数为："+ sv.getAll()
											+ ",缺勤人数为：" + sv.getAbsent() +"，考勤率为：" + sv.getValue()
											+ "\n";
									//date = sv.getDate();
								}
								respContent = "最近一周考勤情况：\n" + str;
							}
						}
						else if(rank ==0 || rank ==3){
							List<TeacherInfo> listTeacherInfo = new ArrayList<TeacherInfo>();
							listTeacherInfo = bf.getAllTeacherInfo(1);
							String tmp = "";
							for(int i=0; i<listTeacherInfo.size(); i++){
								TeacherInfo teacherInfo = listTeacherInfo.get(i);
								tmp = tmp +"教师："+ teacherInfo.getName() +"，编号："+ teacherInfo.getTeacherNumber()+"\n";
							}
							respContent = "请输入“教师”+你想查询的教师编号\n例如：教师1314520\n\n"+"\n教师信息如下：\n"+tmp;
						}
						else if(rank ==2){
							List<TeacherInfo> listTeacherInfo = new ArrayList<TeacherInfo>();
							listTeacherInfo = bf.getAllTeacherInfo(1);
							String tmp = "";
							for(int i=0; i<listTeacherInfo.size(); i++){
								TeacherInfo teacherInfo = listTeacherInfo.get(i);
								if(teacherInfo.getDepartmentNumber()==sf.getDepartmentByTeacherNumber(teacherNumber)){
									tmp = tmp +"教师："+ teacherInfo.getName() +"，编号："+ teacherInfo.getTeacherNumber()+"\n";
								}
							}
							respContent = "请输入“教师”+你想查询的教师编号\n例如：教师1314520\n\n"+"\n教师信息如下：\n"+tmp;
						}
						else{
							respContent = "你不是授课教师哦。";
						}
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
						
					} else if (eventKey.equals("23")) {
						BaseFunction bf = new BaseFunction();
						ServiceFunction sf = new ServiceFunction();
						List<SummaryValue> listSummaryValue = new ArrayList<SummaryValue>();
						
						//java.sql.Date date = null;
						String str = "";
						int rank = bf.getRankFromParent(fromUserName);
						if(rank==2){
							int teacherNumber = sf.getTeacherNumberByOpenid(fromUserName);
							int departmentNumber = sf.getDepartmentByTeacherNumber(teacherNumber);
							listSummaryValue = sf.getDepartmentAbsent(departmentNumber);
							if(listSummaryValue.size()==0){
								respContent = "最近一周没有数据";
							}else{
								for(int i=0; i<listSummaryValue.size(); i++){
									SummaryValue sv = listSummaryValue.get(i);
									str = str+ sv.getDate().toString()+"\n" +"["+ sv.getSubjectName()+"]的考勤人数为："+ sv.getAll()
											+ ",缺勤人数为：" + sv.getAbsent() +"，考勤率为：" + sv.getValue()
											+ "\n";
									//date = sv.getDate();
								}
								respContent = "最近一周考勤情况：\n" + str;
							}
						}
						else if(rank ==3 || rank ==0){
							List<DepartmentInfo> listDepartment = new ArrayList<DepartmentInfo>();
							listDepartment = bf.getAllDepartmentInfo();
							String strTmp = "";
							for(int j=0; j<listDepartment.size(); j++){
								DepartmentInfo departmentInfo = listDepartment.get(j);
								strTmp = strTmp +"学院：" + departmentInfo.getName()+",编号："+departmentInfo.getDepartmentNumber()+"\n";
							}
							respContent = "请输入“学院”+您想查询的学院编号\n例如：学院666680\n\n各学院信息如下：\n" + strTmp;
						}
						else{
							respContent = "你不是学院监察员呢。";
						}
						
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("24")) {
						BaseFunction bf = new BaseFunction();
						ServiceFunction sf = new ServiceFunction();
						List<SummaryValue> listSummaryValue = new ArrayList<SummaryValue>();
						//java.sql.Date date = null;
						String str = "";
						int rank = bf.getRankFromParent(fromUserName);
						if(rank==3 || rank==0){
							listSummaryValue = sf.getAllAbsent();
							if(listSummaryValue.size()==0){
								respContent = "最近一周没有数据";
							}else{
								for(int i=0; i<listSummaryValue.size(); i++){
									SummaryValue sv = listSummaryValue.get(i);
									str = str + sv.getDate().toString()+"\n"+"["+ sv.getSubjectName()+"]的考勤人数为："+ sv.getAll()
											+ ",缺勤人数为：" + sv.getAbsent() +"，考勤率为：" + sv.getValue()
											+ "\n";
									//date = sv.getDate();
								}
								respContent = "最近一周考勤情况：\n" + str;
							}
						}else{
							respContent = "抱歉，您不是学校监察员";
						}
						
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("31")) {
						respContent = "QQ:260038276";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("32")) {
						respContent = "请发送邮件到260038276@qq.com";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					}
					
				}
			}

			//textMessage.setContent(respContent);
			//respMessage = MessageUtil.textMessageToXml(textMessage);
		}catch(Exception e){
			e.printStackTrace();
		}
		return respMessage;
	}
	
}
