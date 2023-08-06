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

	public int bookListings(int listingId, int renterId, String sformattedDate, String eformattedDate, Double totalPricing, String status) {
		String sql = "INSERT INTO bookings (listing_id, renter_id, start_date, finish_date, pricing, status) VALUES (?, ?, ?, ?, ?, ?)";
		
		int generatedId = -1; // Initialize to a default value in case the insert fails
		
		try (PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, listingId);
			statement.setInt(2, renterId);
			statement.setString(3, sformattedDate);
			statement.setString(4, eformattedDate);
			statement.setDouble(5, totalPricing);
			statement.setString(6, status);
			
			int rowsAffected = statement.executeUpdate();
			if (rowsAffected > 0) {
				// Retrieve the auto-generated ID of the inserted booking
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
					generatedId = generatedKeys.getInt(1);
				}
				System.out.println("Booking confirmed. ID: " + generatedId);
			} else {
				System.out.println("Booking failed, try again");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("An error occurred while booking the listing.");
		}
		
		return generatedId;
	}
	

	public void insertAvailability(String table, int listing_id, String date){
		try{
			String query = "INSERT INTO `%s` (listing_id, date) VALUES ('%s', '%s')";
			query = String.format(query, table, listing_id, date);
			this.stmt.execute(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
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

	public boolean verifybooking(String booking_id, int id2) throws SQLException {
        try (
            PreparedStatement stm = con.prepareStatement("SELECT id FROM bookings WHERE id = ? AND renter_id = ?")) {
            stm.setString(1, booking_id);
            stm.setInt(2, id2);
            try (ResultSet rs = stm.executeQuery()) {
                return rs.next(); // If there is a match, rs.next() will return true; otherwise, it will return false.
            }
        }
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

	public List<String> getPrice(List<String> dates, String lid){
		List<String> prices = new ArrayList<>();
		String query = "SELECT price FROM availability WHERE listing_id = ? AND date IN (" + getQuestionMarks(dates.size()) + ")";
		try (PreparedStatement stmt = con.prepareStatement(query)) {
			stmt.setInt(1, Integer.parseInt(lid));

			for (int i = 0; i < dates.size(); i++) {
				stmt.setString(i + 2, dates.get(i));
			}

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					float price = rs.getFloat("price");
					prices.add(String.valueOf(price));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("An error occurred while fetching prices.");
		} 
		return prices;

	}

	private String getQuestionMarks(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("?");
        }
        return sb.toString();
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
	
	/*	Delete booking */
	public void deletebookings(String bookings, String booking_id) {
		try{
			String query = "DELETE FROM %s WHERE id = %s";
			query = String.format(query, bookings, booking_id);
			this.stmt.execute(query);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
	}


	public void deleteAvailability(String table, int listing_id, String date) {
		try {
			String query = "DELETE FROM `%s` WHERE listing_id = %s AND date = '%s'";
			query = String.format(query, table, listing_id, date);
			this.stmt.execute(query);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
	

	/** Returns the id that corresponds to the given email**/
	public String getIdFromEmail(String email){
		try{
			String query = "SELECT id FROM user WHERE email = '%s'";
			query = String.format(query,email);
			ResultSet rs = this.stmt.executeQuery(query);
			if (rs.next()) {
                // Get the id value from the result set and convert it to string
                return Integer.toString(rs.getInt("id"));
            }
			return "";
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return "";
		}
	}	
	
	public String createListing(String host_id, float latitude, float longitude,
	String type_of_listing, String postal_code, String city, String country) {
		try{
			String query = "INSERT INTO listing (host_id, latitude, longitude, type_of_listing, postal_code, city, country)"
							+ "VALUES (%s, %s, %s, '%s', '%s', '%s', '%s')";
			query = String.format(query,host_id, latitude, longitude, 
			type_of_listing, postal_code, city, country);
			this.stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			// Get the generated keys (auto-incremented ID)
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                return Integer.toString(id) ;
            } else {
                throw new Exception("Failed to get the generated ID.");
            }
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return "";
		}
	}

	public Boolean addAmenities(String listingId, List<String> listOfAmenities) {
		try{
			String query = "INSERT INTO listing_amenities (listing_id, wifi, washer, air_conditioning," +
							"dedicated_workspace, hair_dryer, kitchen, dryer, heating, tv, iron)"
							+ "VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)";
			query = String.format(query,listingId, listOfAmenities.get(0),
								listOfAmenities.get(1),
								listOfAmenities.get(2),
								listOfAmenities.get(3),
								listOfAmenities.get(4),
								listOfAmenities.get(5),
								listOfAmenities.get(6),
								listOfAmenities.get(7),
								listOfAmenities.get(8),
								listOfAmenities.get(9));
			this.stmt.execute(query);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public Boolean addAvailability(String listingId, List<String> calendar_availability, String price){
		try{
			for (String date : calendar_availability){
				String query = "INSERT INTO availability (listing_id, date, price) VALUES (%s, '%s', %s)"
							+ "ON DUPLICATE KEY UPDATE price = %s;";
				if(InBookingNormal(listingId, date)){
					System.out.println(date + " already booked.");
				}
				else{
					query = String.format(query,listingId, date, price, price);
					this.stmt.execute(query);
				}
			}
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public ResultSet GetAllActiveListings(String host_id){
		ResultSet rs = null;
		try{
			String query = "SELECT * FROM listing WHERE host_id = %s AND listed=1 ORDER BY id";
			query = String.format(query,host_id);
			rs = this.stmt.executeQuery(query);
			return rs;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return rs;
		}
	}

	public boolean ListingNotEmpty(String host_id) {
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM listing WHERE host_id = %s AND listed=1 ORDER BY id";
			query = String.format(query, host_id);
			rs = this.stmt.executeQuery(query);
			
			// Check if the result set has any rows
			if (rs.next()) {
				// The result set is not empty, so return true
				return true;
			} else {
				// The result set is empty, so return false
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean ListingIDNotEmpty(String host_id, String listing_id) {
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM listing WHERE host_id = %s AND listed=1 AND id = %s ORDER BY id";
			query = String.format(query, host_id, listing_id);
			rs = this.stmt.executeQuery(query);
			
			// Check if the result set has any rows
			if (rs.next()) {
				// The result set is not empty, so return true
				return true;
			} else {
				// The result set is empty, so return false
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ResultSet GetListingAvailability(String listing_id){
		ResultSet rs = null;
		try{
			String query = "SELECT * FROM availability WHERE listing_id = %s AND date >= CURRENT_DATE() ORDER BY date";
			query = String.format(query,listing_id);
			rs = this.stmt.executeQuery(query);
			return rs;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return rs;
		}
	}

	public boolean ListingAvailabilityNotEmpty(String listing_id) {
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM availability WHERE listing_id = %s AND date >= CURRENT_DATE() ORDER BY date";
			query = String.format(query,listing_id);
			rs = this.stmt.executeQuery(query);
			
			// Check if the result set has any rows
			if (rs.next()) {
				// The result set is not empty, so return true
				return true;
			} else {
				// The result set is empty, so return false
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean changePrice(String listingId, String startDate,  String endDate,String price){
		try{
			String query = "UPDATE availability SET price = %s "
							+ "WHERE listing_id = %s AND date BETWEEN '%s' AND '%s';";
			query = String.format(query, price, listingId, startDate, endDate);
			Integer num_rows = this.stmt.executeUpdate(query);
			System.out.println("Number of entries changed: " + num_rows);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean deleteAvailability(String listingId, String startDate,  String endDate){
		try{
			String query = "DELETE FROM availability "
							+ " WHERE listing_id = %s AND date BETWEEN '%s' AND '%s';";
			query = String.format(query,  listingId, startDate, endDate);
			Integer num_rows = this.stmt.executeUpdate(query);
			System.out.println("Number of entries deleted: " + num_rows);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public boolean InBookingNormal(String listing_id, String date) {
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM bookings WHERE listing_id = %s AND status = 'normal'" 
						+ " AND '%s' BETWEEN start_date AND finish_date";
			query = String.format(query,listing_id, date);
			rs = this.stmt.executeQuery(query);
			
			// Check if the result set has any rows
			if (rs.next()) {
				// The result set is not empty, so return true
				return true;
			} else {
				// The result set is empty, so return false
				return false;
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean UnlistListing(String listing_id){
		try{
			String queryListing = "UPDATE listing SET listed = 0 "
							+ " WHERE id = %s;";
			queryListing = String.format(queryListing, listing_id);
			this.stmt.executeUpdate(queryListing);

			String queryAvailability = "DELETE FROM availability"
							+ " WHERE listing_id = %s;";
			queryAvailability = String.format(queryAvailability, listing_id);
			Integer numAvail = this.stmt.executeUpdate(queryAvailability);
			
			String queryBookings = "UPDATE bookings SET status = 'host_cancelled' "
							+ " WHERE listing_id = %s AND finish_date > CURRENT_DATE();";
			queryBookings = String.format(queryBookings, listing_id);
			Integer numCancelled = this.stmt.executeUpdate(queryBookings);

			System.out.println("Number of bookings cancelled: " + numCancelled);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}

	public ResultSet GetlistingBookingsAvailable(String listing_id){
		ResultSet rs = null;
		try{
			String query = "SELECT * FROM bookings where listing_id = %s AND status = 'normal'";
			query = String.format(query,listing_id);
			rs = this.stmt.executeQuery(query);
			return rs;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return rs;
		}
	}

	public boolean BookingsAvailableIsNotEmpty(String listing_id, String booking_id){
		ResultSet rs = null;
		try{
			String query = "SELECT * FROM bookings where listing_id = %s AND id = %s";
			query = String.format(query,listing_id, booking_id);
			rs = this.stmt.executeQuery(query);
			if (rs.next()) {
				// The result set is not empty, so return true
				return true;
			} else {
				// The result set is empty, so return false
				return false;
			}
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}

	public boolean HostCancelBookingOne(String booking_id){
		try{

			String queryBookings = "UPDATE bookings SET status = 'host_cancelled' "
							+ " WHERE id = %s;";
			queryBookings = String.format(queryBookings, booking_id);
			Integer numCancelled = this.stmt.executeUpdate(queryBookings);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return false;
		}
	}
}