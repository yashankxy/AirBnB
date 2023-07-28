package Functions;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;  
   

public class sqlFunctions {
	private static final String DBc = "com.mysql.jdbc.Driver.AirBnB";
	private static final String CON_url = "jdbc:mysql://127.0.0.1/";
	private static final String USER = "root";
	private static final String PASS = "1234";

	Connection con = null;
	Statement stmt = null;


	/** Connects and Initialize */
	public boolean connect() {
		boolean result = true;
		try {
			Scanner sc = new Scanner(System.in);

			Class.forName(DBc);
			con = DriverManager.getConnection(CON_url, USER, PASS);
			this.stmt = con.createStatement();
			sc.close();
		} catch ( Exception e ) {
			result = false;
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}
		return result;
	}
	
	/** Disconnect */
	public void disconnect() {
		try {
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		} finally{
			stmt =null;
			con =null;
		}
	}

	/** */
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

	/** Create a user */
	public boolean createuser(boolean host ){ // Add customer details, or host details create a class if required
		return true;
	}

	public boolean listings(int userID ){ // Add Listing details, create a class if required for listings
		return true;
	}

	public boolean removeListings(int userID, int ListingId){ // Add other listing details
		return false; 
	} 

	public boolean bookListings(int userID, int ListingId){ // Add other listing details
		return false; 
	} 

	public boolean cancelBookings(int userID, int bookingId){ // Add other booking details if required
            // String query = "DELETE FROM bookings WHERE booking_id = ?";
            // PreparedStatement stmt = con.prepareStatement(query);
            // stmt.setInt(1, bookingId);
            // int rowsAffected = stmt.executeUpdate();
            // return rowsAffected > 0;
			return false;

	} 




}