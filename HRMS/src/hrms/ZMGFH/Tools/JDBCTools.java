package hrms.ZMGFH.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class JDBCTools {
	private static final String driver = "com.mysql.cj.jdbc.Driver";// 驱动
	private static final String username = "root";// 账号
	private static final String password = "203815";// 密码
	private static final String address = "jdbc:mysql://127.0.0.1:3306/hrms_mysql?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=CONVERT_TO_NULL&allowPublicKeyRetrieval=true";// 数据库地址
	private static Connection connection = null;// 数据库连接

	public static Connection getConnection() {// 获取数据库连接
		try {
			if (connection == null || connection.isClosed()) {
				Class.forName(driver);
				connection = DriverManager.getConnection(address, username, password);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
        return connection;
	}

	public static void close(ResultSet rs) {// 关闭Resultset
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(Statement stmt) {// 关闭Statement
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(PreparedStatement ps) {// 关闭PreparedStatement
		try {
			if (ps != null)
				ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs, Statement stmt, PreparedStatement ps) {
		close(rs);
		close(ps);
		close(stmt);
	}

	public static void closeConnection() {// 关闭Connection
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
