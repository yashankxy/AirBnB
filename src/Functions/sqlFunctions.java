package Functions;


import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	
	
	
// ------------------------------ Connection FUNCTIONS ------------------------------ //	


	/** Connects **/
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


// ------------------------------ INSERT FUNCTIONS ------------------------------ //

	/** Create a host */
	public boolean createuser(String table, String name, String email, String password, String address, String occupation, String sin, String dob ) throws SQLException{ 
		try{
			String query = "INSERT INTO `%s` (name, email, password, address, occupation, sin, dob) VALUES ('%s', '%s','%s', '%s', '%s', %s, '%s')";
			query = String.format(query, table, name, email, password, address, occupation, sin, dob);
			this.stmt.execute(query);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}


	public int createuser1(String table, String name, String email, String password, String address, String occupation, String sin, String dob) throws SQLException {
		try {
			String query = "INSERT INTO `%s` (name, email, password, address, occupation, sin, dob) VALUES ('%s', '%s', '%s', '%s', '%s', %s, '%s')";
			query = String.format(query, table, name, email, password, address, occupation, sin, dob);
	
			// Execute the insertion query and get the auto-generated keys (ID).
			PreparedStatement pstmt = this.con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pstmt.executeUpdate();
	
			// Retrieve the auto-generated ID.
			int id = -1;
			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					id = generatedKeys.getInt(1);
				}
			}
	
			return id; // Return the auto-generated ID.
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return -1; // Return -1 to indicate an error.
		}
	}
	

	/** Create a Table for credit card */
	public boolean link_cc(String cc_num, String cc_name, String cc_exp, String cc_cvv, int uid) throws SQLException{ 
		try{
			String query = "INSERT INTO `cc` (cc_num, cc_name, cc_exp, cc_cvv, uid) VALUES ('%s', '%s','%s', '%s', '%s')";
			query = String.format(query, cc_num, cc_name, cc_exp, cc_cvv, uid);
			this.stmt.execute(query);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public void bookListings(int listingId, int renterId, String sformattedDate, String eformattedDate, Double totalPricing, String status) {

        String sql = "INSERT INTO bookings (listing_id, renter_id, start_date, finish_date, pricing, status) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, listingId);
            statement.setInt(2, renterId);
            statement.setString(3, sformattedDate);
            statement.setString(4, eformattedDate);
            statement.setDouble(5, totalPricing);
            statement.setString(6, status);
            
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Booking confirmed");
            } else {
                System.out.println("Booking failed, try again");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while booking the listing.");
        }
    }
// ------------------------------ SELECT FUNCTIONS ------------------------------ //


	/* Find User */
	public List<String> getUser(String email){
		List<String> info = new ArrayList<String>();
		String query = "SELECT * FROM user WHERE email = '%s'";
		query = String.format(query, email);
		try{
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				info.add(String.valueOf(rs.getInt("id")));
				info.add(rs.getString("name"));
				info.add(rs.getString("email"));
				info.add(rs.getString("password"));
				info.add(rs.getString("address"));
				info.add(rs.getString("occupation"));
				info.add(rs.getString("sin"));
				info.add(rs.getString("dob"));
				info.add(String.valueOf(rs.getBoolean("renter")));
			}
			rs.close();
		}catch(Exception e){
			System.out.println("Unable to extract information");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return info;
	}

	/** Get CC details */
	public List<String> getcc(int uid){
		List<String> info = new ArrayList<String>();
		String query = "SELECT * FROM cc WHERE id = '%s'";
		query = String.format(query, uid);
		try{
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				info.add(rs.getString("cc_num"));
				info.add(rs.getString("cc_name"));
				info.add(rs.getString("cc_exp"));
				info.add(rs.getString("cc_cvv"));
			}
			rs.close();
		}catch(Exception e){
			System.out.println("Unable to extract Credit Card details");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}

		return info;
	}
	
	public List<String> select(String tableName, String columnToSelect, String columnToMatch, String value) throws SQLException {
        List<String> result = new ArrayList<>();
        try (
			PreparedStatement stm = con.prepareStatement("SELECT " + columnToSelect + " FROM " + tableName + " WHERE " + columnToMatch + " = ?")) {
            stm.setString(1, value);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String columnValue = rs.getString(columnToSelect);
                    result.add(columnValue);
                }
            }
        }
        return result;
    }

	public List<String> getAvailableDates(int listingId) {
        List<String> availableDates = new ArrayList<>();
        String sql = "SELECT date FROM availability WHERE listing_id = ?";
        
        try (PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, listingId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String date = resultSet.getString("date");
                    availableDates.add(date);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching available dates.");
        }
        
        return availableDates;
    }


// ------------------------------ UPDATE FUNCTIONS ------------------------------ //


	public ResultSet updateUser(String uid, String name, String email, String pwd, String address, String occup, String sin, String dob){
		try{
			String query = "UPDATE user SET name='%s', email='%s', password='%s', address='%s', occupation='%s', sin='%s', dob='%s' WHERE uid = '%s'";
        	query = String.format(query, name, email, pwd, address, occup, sin, dob, uid);
			return this.stmt.executeQuery(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return null;
		}
	}


// ------------------------------ DELETE FUNCTIONS ------------------------------ //


	public ResultSet delUser(String uid){
		try{
			String query = "DELETE FROM user WHERE uid = '%s'";
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

	

	public boolean cancelBookings(int userID, int bookingId){ // Add other booking details if required
            // String query = "DELETE FROM bookings WHERE booking_id = ?";
            // PreparedStatement stmt = con.prepareStatement(query);
            // stmt.setInt(1, bookingId);
            // int rowsAffected = stmt.executeUpdate();
            // return rowsAffected > 0;
			return true;
	} 




}