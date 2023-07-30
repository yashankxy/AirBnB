package Functions;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;  
   

public class sqlFunctions {
	private static final String DBc = "com.mysql.jdbc.Driver";
	private static final String CON_url = "jdbc:mysql://127.0.0.1/airbnb";
	private static final String USER = "root";
	private static final String PASS = "1234";

	public Connection con = null;
	public Statement stmt = null;


	/** Connects and Initialize */
	public boolean connect() {
		boolean result = true;
		try {
			Scanner sc = new Scanner(System.in);

			Class.forName(DBc);
			con = DriverManager.getConnection(CON_url, USER, PASS);
			this.stmt = con.createStatement();
			// sc.close();
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
	public boolean createuser(String name, String email, String password, String address, String occupation, String sin, String dob, boolean host ) throws SQLException{ // Add customer details, or host details create a class if required
		try{
			String query = "INSERT INTO users (name, email, password, address, occupation, sin, dob) VALUES ('%s', '%s','%s', '%s', '%s', %s, '%s')";
			query = String.format(query, name, email, password, address, occupation, sin, dob);
			this.stmt.execute(query);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public ResultSet getUser(String uid){
		try{
			String query = "SELECT * FROM Users WHERE uid = '%s'";
			query = String.format(query, uid);
			return this.stmt.executeQuery(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}

	public ResultSet updateUser(String uid, String name, String email, String pwd, String address, String occup, String sin, String dob){
		try{
			String query = "UPDATE users SET name='%s', email='%s', password='%s', address='%s', occupation='%s', sin='%s', dob='%s' WHERE uid = '%s'";
        	query = String.format(query, name, email, pwd, address, occup, sin, dob, uid);
			return this.stmt.executeQuery(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}

	public ResultSet delUser(String uid){
		try{
			String query = "DELETE FROM Users WHERE uid = '%s'";
			query = String.format(query, uid);
			return this.stmt.executeQuery(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
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
			return true;

	} 




}