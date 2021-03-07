package DBConnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBConnection {
	
	private static final String USERNAME = "goshi";
	private static final String PASSWORD = "goshi";
	private static final String CONN = "jdbc:mysql://localhost/bank?serverTimezone=UTC";
	
	public static Connection getConnection() throws SQLException {
		
		return DriverManager.getConnection(CONN, USERNAME, PASSWORD);
		
	}
	
	public static void createTable() throws Exception{
		try {
			Connection conn = getConnection();
			PreparedStatement create = conn.prepareStatement(SQLCommands.CREATE_TABLE_BANK_ACCOUNTS);
			create.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		}finally {
			System.out.println("Func comlpeted");
		}
	}
}
