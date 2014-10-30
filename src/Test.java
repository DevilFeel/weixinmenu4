import hyit.app.factory.DAOFactory;
import hyit.app.model.SummarySheet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import service.BaseFunction;
import service.Decrition;
import service.ServiceFunction;
import service.SubFunction;


public class Test {
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		double percent = 50.5 / 150;
		//输出一下，确认你的小数无误
		System.out.println("小数：" + percent);
		//获取格式化对象
		NumberFormat nt = NumberFormat.getPercentInstance();
		//设置百分数精确度2即保留两位小数
		nt.setMinimumFractionDigits(2);
		//最后格式化并输出
		System.out.println("百分数：" + nt.format(percent));
		//
	}
}
