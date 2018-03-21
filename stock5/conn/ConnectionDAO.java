package stock5.conn;

import com.mysql.jdbc.*;
import java.sql.*;

public class ConnectionDAO {
	
	public static java.sql.Connection getConnection() throws Exception {
		String mysql_url="jdbc:mysql://localhost:3306/stock";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection conn = DriverManager.getConnection (mysql_url, "root", "1977660114");
		
		return conn;
	}
}
