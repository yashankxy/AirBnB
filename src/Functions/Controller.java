package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Users.Host;
import Users.Renter;
import Users.Users;

public class Controller {
    Scanner sc = null;
    sqlFunctions db = null;

//______________________ Basic Operation ______________________ \\

    /*  Start */
    public boolean run (){
        if (sc == null)
            sc = new Scanner(System.in);
        if (db == null)
            db = new sqlFunctions();
            db.connect();
        return true;
    }
    
    /** Shut down */
    public boolean close(){
        if (db != null)
            db.disconnect();
        return true;
    }    
    
//___________________________ Menu ____________________________ \\

    
    /** Opens up Menu */
    public void Menu() throws SQLException {
        if (sc != null && db != null) {
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Login\n"+
                                "        3. Sign-Up\n");
                System.out.print("Select:");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) {
                        case 1:
                            break;
                        case 2:
                            login();
                            break;
                        case 3:
                            signup();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (!val.equals("1") && !val.equals("2") && !val.equals("3"));

            if (val.equals("1")) close();

        } 
        else {
            System.out.println("\nConnection Failed");
        }
    }
    

//______________________ Authentication _______________________ \\
    
    
    /* Logs in User */
    // TODO Get user info and store in memory
    public void login() throws SQLException {
        String email, password;
    
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            System.out.print("Password: ");
            password = sc.nextLine();

        } while (!verify_login(email, password));
        List<String> userDetails = db.getUser(email);
        
        Users user;
        if (userDetails.get(8) == "true") { // Renter Dashboard
            System.out.println(userDetails);
            List<String> ccdetails = db.getcc(Integer.valueOf(userDetails.get(0)));
            System.out.println(ccdetails);
            	user = new Renter(userDetails.get(1), userDetails.get(2), userDetails.get(3), userDetails.get(4), userDetails.get(5), userDetails.get(6), userDetails.get(7), ccdetails.get(0), ccdetails.get(1), ccdetails.get(2), ccdetails.get(3));
            	System.out.println("\nWelcome " + user.getName());
            	renterDashboard();
        } else { // Host dashboard
            	user = new Host(userDetails.get(0), userDetails.get(1), userDetails.get(2), userDetails.get(3), userDetails.get(4), userDetails.get(5), userDetails.get(6));
            	System.out.println("\nWelcome " + user.getName());
            	hostDashboard();
		}
    }

    /** Sign up as a Host or customer account */
    public void signup() throws SQLException{
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. sign up as Host\n"+
                                "        3. Sign up as renter \n");
                System.out.print("Select:");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) {
                        case 1:
                            break;
                        case 2:
                            host();
                            break;
                        case 3:
                            renter();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (!val.equals("1") && !val.equals("2") && !val.equals("3"));
         
            if (val.equals("1")) close();
            sc.close();
        }    
        else {
            System.out.println("\nConnection Failed");
        }
    }

    /** Create a new customer account*/
    public void renter() throws SQLException{
        String name, email, password, dob, address, occup;
        String cc_num, cc_name, cc_exp, cc_cvv; 
        String sin = "";
        Boolean verify = true;
        
         // Getting user input
        do { // Name
            System.out.println("\nEnter your name: ");
            name = sc.nextLine();
            if (name.length() == 0) System.out.println("\nName cannot be empty");
        }while(name.length() == 0);
        do{// Address
            System.out.println("\nEnter your address: ");          
            address = sc.nextLine();
            if (address.length()==0) System.out.println("\nAddress cannot be empty");
        }while(name.length() == 0);
        do{// Occupation
            System.out.println("\nEnter your occupation: ");
            occup = sc.nextLine();
            if (address.length()==0) System.out.println("\nAddress cannot be empty");
        }while(name.length() == 0);
        do{ // Date of Birth
            System.out.println("\nEnter your Date of Birth (dd/mm/yyyy): ");          // Add do while
            dob = sc.nextLine();
            try{
                Period period; // For date format
                String[] splitUrl = dob.split("/");
                if (splitUrl[2].length() != 4) throw new Exception("Invalid date format");
                LocalDate birthdate = LocalDate.of(Integer.parseInt(splitUrl[2]), Integer.parseInt(splitUrl[1]), Integer.parseInt(splitUrl[0]));
                LocalDate today = LocalDate.now();

                period = Period.between(birthdate, today);
                verify= period.getYears()<18;
                if (verify) System.out.println("\nYou must be 18 years or older to be a host");
            }catch(Exception e){
                System.out.println("Invalid date format");
            }
        }while(verify);
        do{ // SIN number
            System.out.println("\nEnter your SIN number: ");          // Add do while
            try{
                sin = sc.nextLine().trim();
                Integer.parseInt(sin);    
            }catch(Exception e){
                System.out.println("Invalid input. Please enter SIN without any spaces");
            }
        }while(checkuser("user", "sin", sin) || sin.length()!=9);
        do { // Email
            System.out.println("\nEnter your email: ");         // Add do while
            email = sc.nextLine();
			if(email.equals("")) {
				System.out.println("Email cannot be empty");
			}
		} while (checkuser("user", "email", email) || email.equals(""));
        System.out.println("\nEnter your password: ");          // Add do while
        password = sc.nextLine();
        

        //___________________________Renter Credit card Information________________________________
        boolean check = true;
        do{ // Credit Card Number
            System.out.println("\nEnter your credit card number: ");          // Add do while
            cc_num = sc.nextLine();
            try{
                cc_num = cc_num.trim();
                long num = Long.parseLong(cc_num);
                if (cc_num.length() == 16) {
                    check = false;
                } else {
                    check = true;
                    System.out.println("Wrong values for credit card number");
                }
            }catch(Exception e){
                System.out.println("Invalid input. Please enter credit card number without any spaces");
            }
        }while(check);
        do{ // Credit Card Name
            System.out.println("\nEnter your credit card name (fullname): ");          // Add do while
            cc_name = sc.nextLine();
            if(cc_name.equals("")) System.out.println("\nName cannot be empty");
        }while(cc_name.equals(""));
        check = true; 
        do{ // Expiry Date
            System.out.println("\nEnter your credit card expiry date (mm/yy): ");          // Add do while
            cc_exp = sc.nextLine();
            try{
                String[] splitUrl = cc_exp.split("/");
                int m=Integer.parseInt(splitUrl[0]);
                int y=Integer.parseInt(splitUrl[1]);
                if (m <= 12 && m >= 1 && y >= 23 && y <= 99) {
                    check = false;
                } else {
                    check = true;
                    System.out.println("Wrong values for month or year");
                }
            }catch(Exception e){
                check = true;
                System.out.println("Invalid format");
            }
        }while(check);
        check = true;
        do{ // Credit Card CVV
            System.out.println("\nEnter your credit card cvv: ");          // Add do while
            cc_cvv = sc.nextLine().trim();
            try{
                int cvv = Integer.parseInt(cc_cvv);
                if (cc_cvv.length() == 3) {
                    check = false;
                } else {
                    check = true;
                    System.out.println("Wrong values for cvv");
                }
            }catch(Exception e){
                System.out.println("Invalid input. Please enter cvv without any spaces");
            }
        }while(check);
        
        // Inserting into database
        int val = db.createuser1("user", name, email, password, address, occup, sin, dob);
        if (val == -1){
            System.out.println("Unable to Create user");
        }
        else{
            System.out.println("\nAccount created successfully");
        }
        // Credit information inserting into CC table
        Boolean val1 = db.link_cc(cc_num, cc_name, cc_exp, cc_cvv, val);
        if (val1){
            System.out.println("\nCredit card details added successfully");
        }
        else{
            System.out.println("Unable to add credit card details");
            // Todo: Delete user from user table
            // renter();
        }

        signup();
    }

    /** Create a new host account */
    public void host() throws SQLException{
        String name, email, password, dob, address, occup;
        String sin = "";
        Boolean verify = true;

        // Getting user input
        do { // Name
            System.out.println("\nEnter your name: ");
            name = sc.nextLine();
            if (name.length() == 0) System.out.println("\nName cannot be empty");
        }while(name.length() == 0);
        do{// Address
            System.out.println("\nEnter your address: ");          
            address = sc.nextLine();
            if (address.length()==0) System.out.println("\nAddress cannot be empty");
        }while(name.length() == 0);
        do{// Occupation
            System.out.println("\nEnter your occupation: ");
            occup = sc.nextLine();
            if (address.length()==0) System.out.println("\nAddress cannot be empty");
        }while(name.length() == 0);
       do{ // Date of Birth
            System.out.println("\nEnter your Date of Birth (dd/mm/yyyy): ");          // Add do while
            dob = sc.nextLine();
            try{
                Period period; // For date format
                String[] splitUrl = dob.split("/");
                if (splitUrl[2].length() != 4) throw new Exception("Invalid date format");
                LocalDate birthdate = LocalDate.of(Integer.parseInt(splitUrl[2]), Integer.parseInt(splitUrl[1]), Integer.parseInt(splitUrl[0]));
                LocalDate today = LocalDate.now();

                period = Period.between(birthdate, today);
                verify= period.getYears()<18;
                if (verify) System.out.println("\nYou must be 18 years or older to be a host");
            }catch(Exception e){
                System.out.println("Invalid date format");
            }
        }while(verify);
        do{ // SIN number
            System.out.println("\nEnter your SIN number: ");          // Add do while
            try{
                sin = sc.nextLine().trim();
                Integer.parseInt(sin);    
            }catch(Exception e){
                System.out.println("Invalid input. Please enter SIN without any spaces");
            }
        }while(checkuser("user", "sin", sin) || sin.length()!=9);
       
        do { // Email
            System.out.println("\nEnter your email: ");         // Add do while
            email = sc.nextLine();
			if(email.equals("")) {
				System.out.println("Email cannot be empty");
			}
		} while (checkuser("user", "email", email) || email.equals(""));
        System.out.println("\nEnter your password: ");          // Add do while
        password = sc.nextLine();
        


        // Inserting into database
        Boolean val = db.createuser("user", name, email, password, address, occup, sin, dob);
        if (val){
            System.out.println("\nAccount created successfully");
        }
        else{
            System.out.println("\nUnable to Create user");
        }
        Menu();

    }



//_________________________ Dashboard __________________________ \\

    private boolean hostDashboard(){
        System.out.println("\nWelcome to the Host Dashboard");
        return true;
    }
    
    private boolean renterDashboard() throws SQLException{
        System.out.println("\nWelcome to the Renter Dashboard");
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Make Booking\n"+
                                "        3. Cancel Booking\n"+
                                "        4. Search Listings\n"+
                                "        5. Rate my Bookings\n"+
                                "        6. View Profile\n"+
                                "        7. Logout \n");
                System.out.print("Select: ");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) { 
                        case 1:
                            break;
                        case 2:
                            renterDashboard();
                            break;
                        case 3:
                            renterDashboard();
                            break;
                        case 4:
                            renterDashboard();
                            break;
                        case 5:
                            renterDashboard();
                            break;
                        case 6:
                            renterDashboard();
                            break;
                        case 7:
                            Menu();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (val.compareTo("1") != 0 && val.compareTo("2") != 0 && val.compareTo("3)") != 0 && val.compareTo("4") != 0 && val.compareTo("5") != 0 && val.compareTo("6") != 0 && val.compareTo("7") != 0);
            if (val.equals("1")) close();    
            
        }else {
            System.out.println("\nConnection Failed");
        }

        return true;
    }


//______________________ Helper Functions ______________________ \\


    /* Checks if User Exists */
    private boolean verify_login(String email, String password) throws SQLException {
		List<String> vals = db.select("user", "password", "email", email);
		boolean found = vals.size() == 1 && vals.get(0).equals(password);
		if (!found) { // Check if user exists
            System.out.println("\nInvalid username or password. Please try again.");
		}
		return found;
	}

    private boolean checkuser(String table, String col, String value) throws SQLException{
        List<String> vals = db.select(table, col, col, value);
        if (vals.size() == 1) {
            System.out.println("\nAccount already exists");
            return true;
        } 
        return false;
    }

}
