package service;



import hyit.app.factory.DAOFactory;
import hyit.app.model.SummarySheet;
import hyit.app.trigger.SummaryAbsentInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;










import message.resp.Article;
import message.resp.NewsMessage;
import message.resp.TextMessage;
import util.MessageUtil;

public class CoreService {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	//防SQL注入
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
			
			List<Article> articleList = new ArrayList<Article>();
			//创建图文消息
			NewsMessage newsMessage = new NewsMessage();
			newsMessage.setToUserName(fromUserName);
			newsMessage.setFromUserName(toUserName);
			newsMessage.setCreateTime(new Date().getTime());
			newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
			newsMessage.setFuncFlag(0);
			if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
				String reqContent = requestMap.get("Content").replace(" ", "");
				
				//创建图文消息
//				NewsMessage newsMessage = new NewsMessage();
//				newsMessage.setToUserName(fromUserName);
//				newsMessage.setFromUserName(toUserName);
//				newsMessage.setCreateTime(new Date().getTime());
//				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
//				newsMessage.setFuncFlag(0);
				
				//List<Article> articleList = new ArrayList<Article>();
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
						
						Long studentNumber = serviceFunction.getStudentNumberByOpenid(fromUserName);
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
				else if("100".equals(reqContent)){
					
					ServiceFunction className = new ServiceFunction();
					Class.forName(DBDRIVER).newInstance();
					Connection conn = null;
					conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
					Statement stmt;
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+ fromUserName +"'");
					boolean opposite = true;
					while(rs.next() && opposite){
						opposite = false;
						respContent = className.getClassName(rs.getString("student_number"));
					}
					rs.close();
					stmt.close();
					conn.close();
					
					textMessage.setContent(respContent);
					respMessage = MessageUtil.textMessageToXml(textMessage);
					
					
				}
				else if("101".equals(reqContent)){
					ServiceFunction className = new ServiceFunction();
					Class.forName(DBDRIVER).newInstance();
					Connection conn = null;
					conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
					Statement stmt;
					stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select * from parent_info where openid = '"+ fromUserName +"'");
					boolean opposite = true;
					while(rs.next() && opposite){
						opposite = false;
						respContent = className.getAbsentState(rs.getLong("student_number"));
					}
					rs.close();
					stmt.close();
					conn.close();
					
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
					} else if (eventKey.equals("14")) {
						ServiceFunction serviceFunction = new ServiceFunction();
						Long studentNumber = serviceFunction.getStudentNumberByOpenid(fromUserName);
						String className = serviceFunction.getClassByStudentNumber(studentNumber);
						if(studentNumber !=0){
							SummarySheet summary = null;
							List<Long> listAllStudentNumber = null; //全班学号
							Long number = null; //学号
							List<SummarySheet> list = null;
							String str = "";
							listAllStudentNumber = serviceFunction.getClassNumberByOneNumber(studentNumber);
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
							respContent =className + "班的缺勤人员有：\n"+ str;
						}else{
							respContent = "请绑定学号";
						}
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("21")) {
						ServiceFunction serviceFunction = new ServiceFunction();
						long number = serviceFunction.getStudentNumberByOpenid(fromUserName);
						respContent = String.valueOf(number);
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
							respContent = str;
						}else{
							respContent = "请绑定学号";
						}
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("22")) {
						Decrition decrition = new Decrition();
						respContent = decrition.absentClass();
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("23")) {
						respContent = "敬请期待";
						textMessage.setContent(respContent);
						respMessage = MessageUtil.textMessageToXml(textMessage);
					} else if (eventKey.equals("24")) {
						respContent = "敬请期待";
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
