package service;

public class Decrition {
	public String getCustomizeMenu(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("感谢您的关注！").append("\n\n");
		//buffer.append("想关注你或者你孩子在学校的学习嘛").append("\n");
		buffer.append("你可以回复“AA” + “关注的学生的学号”。 如：AA1121302114").append("\n\n");
		buffer.append("也可以回复“姓名”+“关注学生的姓名”。如：姓名张亮").append("\n");
		//buffer.append("回复“101”可查看这学期所修课程考勤情况。").append("\n");
		//buffer.append("回复“102”可查看最近一周的考勤情况。").append("\n");
		//buffer.append("回复“103”可查看前一天的考勤情况。").append("\n");
		//buffer.append("回复“104”可查看今天的考勤情况。").append("\n");
		return buffer.toString();	
	}
	
	public String getFirstCustomize(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("如果您还没有关注，您可以回复“AA” + “关注的学生的学号”。 如：AA1121302114").append("\n\n");
		buffer.append("也可以回复“姓名”+“关注学生的姓名”。如：姓名张亮").append("\n");
		//buffer.append("您好，请回复“AA” + “关注的学生的学号”。如：AA1121302114").append("\n\n");
		buffer.append("如果已经关注成功，回复“100”可查看其所在班级").append("\n");
		//buffer.append("如果您想查询他们的学号，可以回复“姓名” + “您需要查的名字”").append("\n");
		//buffer.append("回复“101”可查看这学期所修课程及考勤情况。").append("\n");
		//buffer.append("回复“102”可查看最近一周的考勤情况。").append("\n");
		//buffer.append("回复“103”可查看前一天的考勤情况。").append("\n");
		//buffer.append("回复“104”可查看今天的考勤情况。").append("\n");
		return buffer.toString();
	}
	
	public String subSuccess(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好，您已经定制成功，可通过菜单查询相关信息。").append("\n\n");
		//buffer.append("如果您想查询他们的学号，可以回复“姓名” + “您需要查的名字”").append("\n");
		//buffer.append("回复“101”可查看这学期所修课程及考勤情况。").append("\n");
		//buffer.append("回复“102”可查看最近一周的考勤情况。").append("\n");
		//buffer.append("回复“103”可查看前一天的考勤情况。").append("\n");
		//buffer.append("回复“104”可查看今天的考勤情况。").append("\n");
		return buffer.toString();
	}
	
	public String subName(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好,请回复“姓名”+“关注学生的姓名”。\n如：姓名张亮").append("\n");
		return buffer.toString();
	}
	//学号绑定
	public String subStudentNumber(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好,请回复“AA” + “关注的学生的学号”。\n 如：AA1121302114").append("\n");
		return buffer.toString();
	}
	//用户名密码绑定
	public String subUserPwd(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好,请回复“用户” + “用户名#密码”。 \n如：用户123456#mima").append("\n");
		return buffer.toString();
	}
	//22 班级考勤
	public String absentClass(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("您好,请回复“班级” + “班级名称”。 \n如：班级软件1121").append("\n");
		return buffer.toString();
	}
}
