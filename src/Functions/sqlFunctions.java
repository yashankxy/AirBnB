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

	public ResultSet GetEveryActiveListings(){
		ResultSet rs = null;
		try{
			String query = "SELECT l.id, l.host_id, l.type_of_listing, l.latitude, l.longitude," +
							" l.postal_code, l.city, l.country, a.date, a.price" + //
							" FROM listing l " + 
							" INNER JOIN availability a ON l.id = a.listing_id" + 
							" WHERE l.listed = 1;";
			query = String.format(query);
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
							+ " WHERE listing_id = %s AND finish_date > CURRENT_DATE() AND status = 'normal';";
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
			String query = "SELECT * FROM bookings where listing_id = %s AND status = 'normal';";
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
			String query = "SELECT * FROM bookings where listing_id = %s AND id = %s" 
					+ " AND finish_date > CURRENT_DATE()";
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

	public void calculate_avg_price_per_city_and_type(){
		try{

			String query = "SELECT city, type_of_listing, AVG(price) AS avg_price" +
           				   " FROM listing" +
            			   " INNER JOIN availability ON listing.id = availability.listing_id" +
            			   " WHERE listed = 1" +
                           " GROUP BY city, type_of_listing;";
			ResultSet rs = this.stmt.executeQuery(query);

			System.out.println("\n Pricing of Active Listings" );
			System.out.printf("-------------------------------------------\n" );
        	System.out.printf("%-15s %-15s %-15s %n",
                	"City", "Type of Listing", "Average Price per Day");

       		 while (rs.next()) {
				String formatPattern = "%.2f";
				String city = rs.getString("city");
				String type_of_listing = rs.getString("type_of_listing");
				Float avg_price = rs.getFloat("avg_price");

				System.out.printf("%-15s %-15s %-15s %n",
						city, type_of_listing, String.format(formatPattern,avg_price));
			}
        	System.out.printf("-------------------------------------------\n" );

			query = "SELECT l.city, l.type_of_listing, "+
				"AVG(b.pricing / DATEDIFF(b.finish_date, b.start_date)) AS avg_price_per_day "+
	 			"FROM listing l "+
	 			"JOIN bookings b ON l.id = b.listing_id "+
	 			"GROUP BY l.city, l.type_of_listing;";
			rs = this.stmt.executeQuery(query);

			System.out.println("\n Pricing of Bookings" );
			System.out.printf("-------------------------------------------\n" );
        	System.out.printf("%-15s %-15s %-15s %n",
                	"City", "Type of Listing", "Average Price per Day");

       		 while (rs.next()) {
				String formatPattern = "%.2f";
				String city = rs.getString("l.city");
				String type_of_listing = rs.getString("l.type_of_listing");
				Float avg_price = rs.getFloat("avg_price_per_day");

				System.out.printf("%-15s %-15s %-15s %n",
						city, type_of_listing, avg_price);
			}
        	System.out.printf("-------------------------------------------\n" );
			return;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
			return;
		}
	}

	public void calculate_amenities_per_city_and_type(){
		try {
			// Calculate the average price and percentage of amenities for listed listings
			String listedListingsQuery = "SELECT city, type_of_listing, " +
					"SUM(wifi) * 100.0 / COUNT(DISTINCT l.id) AS wifi_percentage, " +
					"SUM(washer) * 100.0 / COUNT(DISTINCT l.id) AS washer_percentage, " +
					"SUM(air_conditioning) * 100.0 / COUNT(DISTINCT l.id) AS air_conditioning_percentage, " +
					"SUM(dedicated_workspace) * 100.0 / COUNT(DISTINCT l.id) AS dedicated_workspace_percentage, " +
					"SUM(hair_dryer) * 100.0 / COUNT(DISTINCT l.id) AS hair_dryer_percentage, " +
					"SUM(kitchen) * 100.0 / COUNT(DISTINCT l.id) AS kitchen_percentage, " +
					"SUM(dryer) * 100.0 / COUNT(DISTINCT l.id) AS dryer_percentage, " +
					"SUM(heating) * 100.0 / COUNT(DISTINCT l.id) AS heating_percentage, " +
					"SUM(tv) * 100.0 / COUNT(DISTINCT l.id) AS tv_percentage, " +
					"SUM(iron) * 100.0 / COUNT(DISTINCT l.id) AS iron_percentage " +
					"FROM listing l " +
					"LEFT JOIN listing_amenities la ON l.id = la.listing_id " +
					"WHERE listed = 1 " +
					"GROUP BY city, type_of_listing;";
	
			ResultSet rsListedListings = this.stmt.executeQuery(listedListingsQuery);
	
			System.out.println("\nListed Listings Amenities");
			System.out.printf("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.printf("%-15s %-15s %-15s %-15s %-20s %-30s %-15s %-15s %-15s %-15s %-15s %-15s%n",
					"City", "Type of Listing", "Wifi (%)", "Washer (%)", "Air Conditioning (%)",
					"Dedicated Workspace (%)", "Hair Dryer (%)", "Kitchen (%)", "Dryer (%)", "Heating (%)",
					"TV (%)", "Iron (%)");
	
			while (rsListedListings.next()) {
				String formatPattern = "%.2f";
				String city = rsListedListings.getString("city");
				String type_of_listing = rsListedListings.getString("type_of_listing");
				double wifi_percentage = rsListedListings.getDouble("wifi_percentage");
				double washer_percentage = rsListedListings.getDouble("washer_percentage");
				double air_conditioning_percentage = rsListedListings.getDouble("air_conditioning_percentage");
				double dedicated_workspace_percentage = rsListedListings.getDouble("dedicated_workspace_percentage");
				double hair_dryer_percentage = rsListedListings.getDouble("hair_dryer_percentage");
				double kitchen_percentage = rsListedListings.getDouble("kitchen_percentage");
				double dryer_percentage = rsListedListings.getDouble("dryer_percentage");
				double heating_percentage = rsListedListings.getDouble("heating_percentage");
				double tv_percentage = rsListedListings.getDouble("tv_percentage");
				double iron_percentage = rsListedListings.getDouble("iron_percentage");
				
				String wifi_percentage_str = String.format(formatPattern, wifi_percentage);
				String washer_percentage_str = String.format(formatPattern, washer_percentage);
				String air_conditioning_percentage_str = String.format(formatPattern, air_conditioning_percentage);
				String dedicated_workspace_percentage_str = String.format(formatPattern, dedicated_workspace_percentage);
				String hair_dryer_percentage_str = String.format(formatPattern, hair_dryer_percentage);
				String kitchen_percentage_str = String.format(formatPattern, kitchen_percentage);
				String dryer_percentage_str = String.format(formatPattern, dryer_percentage);
				String heating_percentage_str = String.format(formatPattern, heating_percentage);
				String tv_percentage_str = String.format(formatPattern, tv_percentage);
				String iron_percentage_str = String.format(formatPattern, iron_percentage);

				System.out.printf("%-15s %-15s %-15s %-15s %-20s %-30s %-15s %-15s %-15s %-15s %-15s %-15s%n",
					city, type_of_listing, wifi_percentage_str, washer_percentage_str, air_conditioning_percentage_str,
					dedicated_workspace_percentage_str, hair_dryer_percentage_str, kitchen_percentage_str, dryer_percentage_str,
					heating_percentage_str, tv_percentage_str, iron_percentage_str);
			}
			System.out.printf("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
	
			// Calculate the average price and percentage of amenities for listings with bookings
			String listingsWithBookingsQuery = "SELECT l.city, l.type_of_listing, " +
					"SUM(la.wifi) * 100.0 / COUNT(DISTINCT l.id) AS wifi_percentage, " +
					"SUM(la.washer) * 100.0 / COUNT(DISTINCT l.id) AS washer_percentage, " +
					"SUM(la.air_conditioning) * 100.0 / COUNT(DISTINCT l.id) AS air_conditioning_percentage, " +
					"SUM(la.dedicated_workspace) * 100.0 / COUNT(DISTINCT l.id) AS dedicated_workspace_percentage, " +
					"SUM(la.hair_dryer) * 100.0 / COUNT(DISTINCT l.id) AS hair_dryer_percentage, " +
					"SUM(la.kitchen) * 100.0 / COUNT(DISTINCT l.id) AS kitchen_percentage, " +
					"SUM(la.dryer) * 100.0 / COUNT(DISTINCT l.id) AS dryer_percentage, " +
					"SUM(la.heating) * 100.0 / COUNT(DISTINCT l.id) AS heating_percentage, " +
					"SUM(la.tv) * 100.0 / COUNT(DISTINCT l.id) AS tv_percentage, " +
					"SUM(la.iron) * 100.0 / COUNT(DISTINCT l.id) AS iron_percentage " +
					"FROM listing l " +
					"JOIN bookings b ON l.id = b.listing_id " +
					"LEFT JOIN listing_amenities la ON l.id = la.listing_id " +
					"GROUP BY l.city, l.type_of_listing;";
	
			rsListedListings = this.stmt.executeQuery(listingsWithBookingsQuery);
	
			System.out.println("\nBooked Listings Amenities");
			System.out.printf("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
			System.out.printf("%-15s %-15s %-15s %-15s %-20s %-30s %-15s %-15s %-15s %-15s %-15s %-15s%n",
					"City", "Type of Listing", "Wifi (%)", "Washer (%)", "Air Conditioning (%)",
					"Dedicated Workspace (%)", "Hair Dryer (%)", "Kitchen (%)", "Dryer (%)", "Heating (%)",
					"TV (%)", "Iron (%)");
	
			while (rsListedListings.next()) {
				String formatPattern = "%.2f";
				String city = rsListedListings.getString("city");
				String type_of_listing = rsListedListings.getString("type_of_listing");
				double wifi_percentage = rsListedListings.getDouble("wifi_percentage");
				double washer_percentage = rsListedListings.getDouble("washer_percentage");
				double air_conditioning_percentage = rsListedListings.getDouble("air_conditioning_percentage");
				double dedicated_workspace_percentage = rsListedListings.getDouble("dedicated_workspace_percentage");
				double hair_dryer_percentage = rsListedListings.getDouble("hair_dryer_percentage");
				double kitchen_percentage = rsListedListings.getDouble("kitchen_percentage");
				double dryer_percentage = rsListedListings.getDouble("dryer_percentage");
				double heating_percentage = rsListedListings.getDouble("heating_percentage");
				double tv_percentage = rsListedListings.getDouble("tv_percentage");
				double iron_percentage = rsListedListings.getDouble("iron_percentage");
				
				String wifi_percentage_str = String.format(formatPattern, wifi_percentage);
				String washer_percentage_str = String.format(formatPattern, washer_percentage);
				String air_conditioning_percentage_str = String.format(formatPattern, air_conditioning_percentage);
				String dedicated_workspace_percentage_str = String.format(formatPattern, dedicated_workspace_percentage);
				String hair_dryer_percentage_str = String.format(formatPattern, hair_dryer_percentage);
				String kitchen_percentage_str = String.format(formatPattern, kitchen_percentage);
				String dryer_percentage_str = String.format(formatPattern, dryer_percentage);
				String heating_percentage_str = String.format(formatPattern, heating_percentage);
				String tv_percentage_str = String.format(formatPattern, tv_percentage);
				String iron_percentage_str = String.format(formatPattern, iron_percentage);
				
				System.out.printf("%-15s %-15s %-15s %-15s %-20s %-30s %-15s %-15s %-15s %-15s %-15s %-15s%n",
					city, type_of_listing, wifi_percentage_str, washer_percentage_str, air_conditioning_percentage_str,
					dedicated_workspace_percentage_str, hair_dryer_percentage_str, kitchen_percentage_str, dryer_percentage_str,
					heating_percentage_str, tv_percentage_str, iron_percentage_str);
			}
			System.out.printf("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
	
			return;
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			return;
		}
	}

	public void setUserBlock(String user_id){
		try{
			String queryBookings = "UPDATE user SET blocked = 1 "
							+ " WHERE id = %s;";
			queryBookings = String.format(queryBookings, user_id);
			this.stmt.executeUpdate(queryBookings);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
	}

	public ResultSet CancelAllRenterBookings(String renter_id){
		ResultSet rs = null;
		try{
			String query = "UPDATE bookings SET status = 'renter_cancelled' where renter_id = %s AND"+
							" status = 'normal' AND finish_date > CURRENT_DATE();";
			query = String.format(query,renter_id);
			this.stmt.executeUpdate(query);
			return rs;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return rs;
		}
	}

	public boolean BookingNotEmpty(String selectedID) {
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM bookings where listing_id = %s AND status = 'normal'" 
					+ " AND finish_date > CURRENT_DATE();";
			query = String.format(query, selectedID);
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

	public void TotalBookingsQuery(String city, String zip, String startDateStr, String endDateStr){
		try{
			String query = String.format(
                "SELECT COUNT(*) AS total_bookings FROM bookings " +
                "JOIN listing ON bookings.listing_id = listing.id " +
                "WHERE city = '%s' AND start_date >= '%s' AND finish_date <= '%s'" +
                (!zip.isEmpty() ? " AND postal_code = '%s'" : ""),
                city, startDateStr, endDateStr, zip);

   
		ResultSet resultSet = this.stmt.executeQuery(query);
		if (resultSet.next()) {
			System.out.println("Total bookings: " + resultSet.getInt("total_bookings"));
		}

        return;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
	}

	public void TotalListing(){
		try{
		String totalListingsByCountryQuery = "SELECT country, COUNT(*) AS total_listings FROM listing GROUP BY country";
		System.out.println("\nTotal listings per country:");
		ResultSet resultSet = this.stmt.executeQuery(totalListingsByCountryQuery);
		while (resultSet.next()) {
			String country = resultSet.getString("country");

			int totalListings = resultSet.getInt("total_listings");

			String result = country;
			result += ": " + totalListings;

			System.out.println(result);
		}
		// Total number of listings per country and city
		String totalListingsByCountryCityQuery = "SELECT country, city, COUNT(*) AS total_listings FROM listing GROUP BY country, city";
		System.out.println("\nTotal listings per country and city:");
		resultSet = this.stmt.executeQuery(totalListingsByCountryCityQuery);
		while (resultSet.next()) {
			String country = resultSet.getString("country");
			String city = resultSet.getString("city");
			int totalListings = resultSet.getInt("total_listings");

			String result = country;
			if (city != null) {
				result += ", " + city;
			}
			result += ": " + totalListings;

			System.out.println(result);
		}
		// Total number of listings per country, city, and postal code
		String totalListingsByCountryCityPostalCodeQuery = "SELECT country, city, postal_code, COUNT(*) AS total_listings FROM listing GROUP BY country, city, postal_code";
		System.out.println("\nTotal listings per country, city, and postal code:");
		resultSet = this.stmt.executeQuery(totalListingsByCountryCityPostalCodeQuery);
		while (resultSet.next()) {
			String country = resultSet.getString("country");
			String city = resultSet.getString("city");
			String postalCode = resultSet.getString("postal_code");
			int totalListings = resultSet.getInt("total_listings");

			String result = country;
			if (city != null) {
				result += ", " + city;
			}
			if (postalCode != null) {
				result += ", " + postalCode;
			}
			result += ": " + totalListings;

			System.out.println(result);
		}
		resultSet = this.stmt.executeQuery(totalListingsByCountryCityPostalCodeQuery);
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
	}

	public void RankHosts(){
		try{
			String overallRankQuery = "SELECT host_id, country, COUNT(*) AS total_listings " + 
									" FROM listing GROUP BY host_id, country ORDER BY total_listings DESC";
            System.out.println("\nHost Rank by Total Listings Overall per Country:");
			ResultSet resultSet = this.stmt.executeQuery(overallRankQuery);

			while (resultSet.next()) {
                int hostId = resultSet.getInt("host_id");
                String country = resultSet.getString("country");
                int totalListings = resultSet.getInt("total_listings");

                String result = "Country: " + country + " , Host ID: " + hostId ;
                result += ", Total Listings: " + totalListings;

                System.out.println(result);
            }

            // Rank hosts by the total number of listings they have per city
            String cityRankQuery = "SELECT host_id, country, city, COUNT(*) AS total_listings FROM "  + 
								 " listing GROUP BY host_id, country, city ORDER BY total_listings DESC";
            System.out.println("\nHost Rank by Total Listings per City:");
			resultSet = this.stmt.executeQuery(cityRankQuery);

			while (resultSet.next()) {
                int hostId = resultSet.getInt("host_id");
                String country = resultSet.getString("country");
                String city = resultSet.getString("city"); // If city column exists in the listing table
                int totalListings = resultSet.getInt("total_listings");

                String result = "Country: " + country ;
                if (city != null) {
                    result += ", City: " + city;
                }
                result += " , Host ID: " + hostId + ", Total Listings: " + totalListings;

                System.out.println(result);
            }
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
	}

	public void CommercialListings(){
		try{
			String query = "SELECT l.city, l.country, l.host_id, COUNT(l.id) AS total_listings, (COUNT(l.id) / t.total_listings * 100) AS percentage " +
							"FROM listing AS l " +
							"JOIN (SELECT city, country, COUNT(id) AS total_listings " +
									"FROM listing WHERE listed = 1 " +
									"GROUP BY city, country) AS t " +
							"ON l.city = t.city AND l.country = t.country WHERE l.listed = 1 " +
							"GROUP BY l.city, l.country, l.host_id, t.total_listings " +
							"HAVING (COUNT(l.id) / t.total_listings * 100) > 10";

			System.out.println("Hosts with More Than 10% Listings per City and Country:");
			ResultSet resultSet = this.stmt.executeQuery(query);
			while (resultSet.next()) {
				String city = resultSet.getString("city");
				String country = resultSet.getString("country");
				int hostId = resultSet.getInt("host_id");
				int totalListings = resultSet.getInt("total_listings");
				double percentage = resultSet.getDouble("percentage");

				System.out.print("City: " + city);
				System.out.print(", Country: " + country);
				System.out.print(", Host ID: " + hostId);
				System.out.print(", Total Listings: " + totalListings);
				System.out.println(", Percentage: " + percentage);
			}
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
	}

	public void RankRenters(int year){
		try{
			String startDate = year + "-01-01";
        	String endDate = year + "-12-31";
			String query = "SELECT renter_id, COUNT(*) AS num_bookings " +
						"FROM bookings " +
						"WHERE start_date  >= '" + startDate + "' AND start_date  <= '" + endDate + "' " +
						"GROUP BY renter_id " +
						"ORDER BY num_bookings DESC;";

			ResultSet resultSet = this.stmt.executeQuery(query);
			while (resultSet.next()) {
				int renterId = resultSet.getInt("renter_id");
				int numBookings = resultSet.getInt("num_bookings");
				System.out.println("Renter ID: " + renterId + ", Number of Bookings: " + numBookings);
			}

			query = "SELECT renter_id, city, COUNT(*) AS num_bookings " +
               "FROM bookings " +
               "INNER JOIN listing ON bookings.listing_id = listing.id " +
               "WHERE start_date  >= '" + startDate + "' AND start_date  <= '" + endDate + "' " +
               "GROUP BY renter_id, city " +
               "HAVING COUNT(*) >= 2 " +
               "ORDER BY num_bookings DESC;";

			resultSet = this.stmt.executeQuery(query);
			while (resultSet.next()) {
				int renterId = resultSet.getInt("renter_id");
				String city = resultSet.getString("city");
				int numBookings = resultSet.getInt("num_bookings");
				System.out.println("Renter ID: " + renterId + ", City: " + city + ", Number of Bookings: " + numBookings);
			}
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );

		}
	}

	public void HighestCancelation(int year){
		try {
			String startDate = year + "-01-01";
			String endDate = year + "-12-31";
	
			// Query to get the number of cancellations for each renter
			String query = "SELECT renter_id, COUNT(*) AS num_cancellations " +
						   "FROM bookings " +
						   "WHERE status = 'renter_cancelled' AND finish_date >= '" + startDate + "' AND finish_date <= '" + endDate + "' " +
						   "GROUP BY renter_id " +
						   "ORDER BY num_cancellations DESC;";
	
			ResultSet resultSet = this.stmt.executeQuery(query);
			while (resultSet.next()) {
				int renterId = resultSet.getInt("renter_id");
				int numCancellations = resultSet.getInt("num_cancellations");
				System.out.println("Renter ID: " + renterId + ", Number of Cancellations: " + numCancellations);
			}
	
			// Query to get the renters with the largest number of cancellations in each city
			query = "SELECT host_id, " +
					"COUNT(*) AS num_cancellations " +
					"FROM bookings b " +
					"JOIN listing l ON b.listing_id = l.id " +
					"WHERE b.status = 'host_cancelled' AND finish_date >= '" + startDate + "' AND finish_date <= '" + endDate + "' " +
					"GROUP BY host_id " +
					"ORDER BY num_cancellations DESC";
	
			resultSet = this.stmt.executeQuery(query);
			while (resultSet.next()) {
				int host_id = resultSet.getInt("host_id");
				int numCancellations = resultSet.getInt("num_cancellations");
				System.out.println("Host ID: " + host_id + ", Number of Cancellations: " + numCancellations);
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}
	}
}