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
		//���һ�£�ȷ�����С������
		System.out.println("С����" + percent);
		//��ȡ��ʽ������
		NumberFormat nt = NumberFormat.getPercentInstance();
		//���ðٷ�����ȷ��2��������λС��
		nt.setMinimumFractionDigits(2);
		//����ʽ�������
		System.out.println("�ٷ�����" + nt.format(percent));
		//
	}
}
