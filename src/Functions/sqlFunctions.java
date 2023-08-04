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

	/** Initiate **/
	public boolean initiate_tables_and_db() {
		boolean result = true;
		String CON_url = "jdbc:mysql://127.0.0.1";
		try {
			Scanner sc = new Scanner(System.in);

			Class.forName(DBc);
			con = DriverManager.getConnection(CON_url, USER, PASS);
			System.out.println("Connected to MySQL Server");
			this.stmt = con.createStatement();
			// sc.close();
		} catch ( Exception e ) {
			result = false;
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			e.printStackTrace();
			System.exit(0);
		}

		try {
            // Check if the database exists
            ResultSet resultSet = this.stmt.executeQuery("SHOW DATABASES LIKE 'airbnb'");
            if (!resultSet.next()) {
                // If the database doesn't exist, create it
                this.stmt.executeUpdate("CREATE DATABASE airbnb");
                System.out.println("Database airbnb created successfully.");
            } else {
                System.out.println("Database airbnb already exists.");
            }
        } 
		catch (SQLException e) {
            e.printStackTrace();
        }

        String createTableuser = "CREATE TABLE IF NOT EXISTS airbnb.user("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "username VARCHAR(50) NOT NULL,"
                + "email VARCHAR(100) NOT NULL UNIQUE,"
                + "password VARCHAR(100) NOT NULL"
                + ");";
		
		try {
			this.stmt.executeUpdate(createTableuser);
			System.out.println("Table user created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		String createTablelisting = "CREATE TABLE IF NOT EXISTS airbnb.listing("
                + "id INT AUTO_INCREMENT PRIMARY KEY,"
                + "renter_id INT NOT NULL,"
                + "type_of_listing ENUM('full house', 'apartment', 'room') NOT NULL,"
                + "latitude INT NOT NULL,"
				+ "longitude INT NOT NULL,"
				+ "postal_code VARCHAR(10),"
				+ "city VARCHAR(100),"
				+ "country VARCHAR(100),"
				+ "pricing INT NOT NULL DEFAULT 0,"
				+ "FOREIGN KEY (renter_id) REFERENCES user (id)" + //
						"    ON DELETE CASCADE"
                + ");";
		try {
			this.stmt.executeUpdate(createTablelisting);
			System.out.println("Table listing created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		String createTablelistingamenities = "CREATE TABLE IF NOT EXISTS airbnb.listing_amenities("
                + "listing_id INT PRIMARY KEY,"
				+ "wifi BOOLEAN NOT NULL DEFAULT 0,"
				+ "washer BOOLEAN NOT NULL DEFAULT 0,"
				+ "air_conditioning BOOLEAN NOT NULL DEFAULT 0,"
				+ "dedicated_workspace BOOLEAN NOT NULL DEFAULT 0,"
				+ "hair_dryer BOOLEAN NOT NULL DEFAULT 0,"
				+ "kitchen BOOLEAN NOT NULL DEFAULT 0,"
				+ "dryer BOOLEAN NOT NULL DEFAULT 0,"
				+ "heating BOOLEAN NOT NULL DEFAULT 0,"
				+ "tv BOOLEAN NOT NULL DEFAULT 0,"
				+ "iron BOOLEAN NOT NULL DEFAULT 0,"
				+ "FOREIGN KEY (listing_id) REFERENCES listing (id)" + //
						"    ON DELETE CASCADE"
                + ");";
		try {
			this.stmt.executeUpdate(createTablelistingamenities);
			System.out.println("Table listing_amenities created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Create the availability table
		String createTableAvailability = "CREATE TABLE IF NOT EXISTS airbnb.availability ("
		+ "listing_id INT NOT NULL,"
		+ "date Date NOT NULL,"
		+ "PRIMARY KEY (listing_id, date),"
		+ "FOREIGN KEY (listing_id) REFERENCES listing (id) ON DELETE CASCADE"
		+ ");";
		
		try {
			this.stmt.executeUpdate(createTableAvailability);
			System.out.println("Table availability created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Create the booked table
		String createTableBookings = "CREATE TABLE IF NOT EXISTS airbnb.bookings ("
		+ "id INT AUTO_INCREMENT PRIMARY KEY,"
		+ "listing_id INT NOT NULL,"
		+ "renter_id INT NOT NULL,"
		+ "start_date Date NOT NULL,"
		+ "finish_date Date NOT NULL,"
		+ "pricing INT NOT NULL DEFAULT 0,"
		+ "status ENUM('user_cancelled', 'renter_cancelled', 'normal') NOT NULL DEFAULT 'normal',"
		+ "FOREIGN KEY (listing_id) REFERENCES listing (id),"
		+ "FOREIGN KEY (renter_id) REFERENCES user (id)"
		+ ");";

		try {
			this.stmt.executeUpdate(createTableBookings);
			System.out.println("Table bookings created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
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

	/** Create a host */
	public boolean createhost(String table, String name, String email, String password, String address, String occupation, String sin, String dob ) throws SQLException{ 
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

	/** Create a renter */
	public boolean createrenter(String table, String name, String email, String password, String address, String occupation, String sin, String dob, String cc_num, String cc_name, String cc_exp, String cc_cvv ) throws SQLException{ // Add customer details, or host details create a class if required
		try{
			String query = "INSERT INTO `%s` (name, email, password, address, occupation, sin, dob, cc_num, cc_name, cc_exp, cc_cvv) VALUES ('%s', '%s','%s', '%s', '%s', %s, '%s', '%s', '%s', '%s', '%s')";
			query = String.format(query, table, name, email, password, address, occupation, sin, dob, cc_num, cc_name, cc_exp, cc_cvv);
			this.stmt.execute(query);
			return true;
		}catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			return false;
		}
	}



	/* Find User */
	public List<String> getUser(String email){
		List<String> info = new ArrayList<String>();
		String query = "SELECT * FROM user WHERE email = '%s'";
		query = String.format(query, email);
		try{
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				info.add(rs.getString("name"));
				info.add(rs.getString("email"));
				info.add(rs.getString("password"));
				info.add(rs.getString("address"));
				info.add(rs.getString("occupation"));
				info.add(rs.getString("sin"));
				info.add(rs.getString("dob"));
				info.add(rs.getString("cc_num"));
				info.add(rs.getString("cc_name"));
				info.add(rs.getString("cc_exp"));
				info.add(rs.getString("cc_cvv"));
			}
			rs.close();
		}catch(Exception e){
			System.out.println("Unable to extract information");
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		}
		return info;
	}

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