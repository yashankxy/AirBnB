package Functions;


import java.sql.*;
import java.util.Scanner;  
   

public class sqlFunctions {
	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION = "jdbc:mysql://127.0.0.1/airbnb";
	private static final String sqlUser = "root";
	private static final String sqlPass = "1234";

	Connection con = null;
	Statement stmt = null;

	public boolean connect() {
		boolean result = true;
		try {
			Scanner sc = new Scanner(System.in);
			String[] credential = new String[3];
			System.out.print("User: ");
			credential[0] = sc.nextLine();
			System.out.print("Pwd: ");
			credential[1] = sc.nextLine();
			System.out.print("Database: ");
			credential[2] = sc.nextLine();
			String user = credential[0];
			String pwd = credential[1];

			String connection = "jdbc:mysql://127.0.0.1/" + credential[2] + "?useLegacyDatetimeCode=false&serverTimezone=UTC";
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(connection, user, pwd);
			this.stmt = con.createStatement();
			sc.close();
		} catch ( Exception e ) {
			result = false;
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return result;
	}
	
	public void disconnect() {
		try {
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	public ResultSet executeQuery(String query) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(query);
		} catch(Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		return rs;
	}


}