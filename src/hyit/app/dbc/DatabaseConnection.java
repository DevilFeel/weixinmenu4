package hyit.app.dbc;

import java.sql.Connection;
//import java.sql.DriverManager;

import java.sql.DriverManager;



public class DatabaseConnection {
	//private static final String DSNAME = "java:comp/env/jdbc/myDS";
	private static final String DBDRIVER = "org.gjt.mm.mysql.Driver";
	private static final String dbUrl = "jdbc:mysql://localhost:3306/attendanceV2";
	private static final String dbUser = "root";
	private static final String dbPwd = "nicai";
	/*
	 * private static final String DBDRIVER = "com.mysql.jdbc.Driver"; private
	 * static final String DBURL = "jdbc:mysql://localhost:3306/attendanceV2";
	 * private static final String DBUSER = "zyy"; private static final String
	 * DBPASSWORD = "kurimu"; for mysql
	 */
	/*
	 * private static final String DBDRIVER = "com.mysql.jdbc.Driver"; //for SAE
	 * private static final String DBURL =
	 * "jdbc:mysql://w.rdc.sae.sina.com.cn:3307/app_akinoneko"; //for SAE
	 * private static final String DBUSER = "0lyx1jw04o"; //for SAE private
	 * static final String DBPASSWORD =
	 * "hjjh3m1lx3j5wmxm1lk0zml0wy04ii525lh4wkzw"; //for SAE
	 */private Connection conn = null;

	public DatabaseConnection() throws Exception {
		try {

			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(dbUrl, dbUser, dbPwd);

			// Context ctx = new InitialContext(); // 使用tomcat数据源
			// DataSource ds = (DataSource) ctx.lookup(DSNAME);
			// this.conn = ds.getConnection();
		} catch (Exception e) {
			throw e;
		}
	}

	public Connection getConnection() {
		return this.conn;
	}

	public void close() throws Exception {
		if (this.conn != null) {
			try {
				this.conn.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}
}
