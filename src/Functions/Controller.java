package Functions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {
    Scanner sc = null;
    sqlFunctions db = null;

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
    /* Logs in User */
    // TODO Get user info and store in memory
    public void login() throws SQLException {
        String[] cred = new String[2];
        int attempts = 0; // Variable to keep track of login attempts.
    
        do {
            System.out.print("Email: ");
            cred[0] = sc.nextLine().trim();
            System.out.print("Password: ");
            cred[1] = sc.nextLine();

        } while (!verify_login(cred[0], cred[1]));
        List<String> userInfo = db.getUser(cred[0]);
        // TODO: Find if user is a host or renter
    }

    /* Checks if User Exists */
    private boolean verify_login(String email, String password) throws SQLException {
		List<String> vals = db.select("user", "password", "email", email);
		boolean found = vals.size() == 1 && vals.get(0).equals(password);
		if (!found) { // Check if user exists
            System.out.println("\nInvalid username or password. Please try again.");
		}
		return found;
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
    // TODO
    public void renter() throws SQLException{
        // String name, email, password, dob, address, occup, sin;
        // String cc_num, cc_name, cc_exp, cc_cvv;

        // // Getting user input
        // System.out.println("\nEnter your name: ");
        // name = sc.nextLine();
        // System.out.println("\nEnter your Date of Birth (dd/mm/yy): ");          // Add do while
        // dob = sc.nextLine();
        // System.out.println("\nEnter your address: ");          // Add do while
        // address = sc.nextLine();
        // System.out.println("\nEnter your occupation: ");
        // occup = sc.nextLine();
        // System.out.println("\nEnter your SIN number: ");          // Add do while
        // sin = sc.nextLine();
        // System.out.println("\nEnter your email: ");         // Add do while
        // email = sc.nextLine();
        // System.out.println("\nEnter your password: ");          // Add do while
        // password = sc.nextLine();

        // //___________________________Renter Credit card Information________________________________
        // System.out.println("\nEnter your credit card number: ");          // Add do while
        // cc_num = sc.nextLine();
        // System.out.println("\nEnter your credit card name: ");          // Add do while
        // cc_name = sc.nextLine();
        // System.out.println("\nEnter your credit card expiry date (mm/yy): ");          // Add do while
        // cc_exp = sc.nextLine();
        // System.out.println("\nEnter your credit card cvv: ");          // Add do while
        // cc_cvv = sc.nextLine();
        // // Check if already exists
        //     // if (db.checkCustomer(email, password)){
        //     //     System.out.println("\nAccount already exists");

        // // Inserting into database
        // Boolean val = db.createuser(name, email, password, address, occup, sin, dob, false);
        // if (val){
        //     System.out.println("\nAccount created successfully");
        // }
        // else{
        //     System.out.println("Unable to Create user");
        // }
        // signup();
    }

    /** Create a new host account */
    public void host() throws SQLException{
        String name, email, password, dob, address, occup;
        String sin = "";
        Boolean verify = true;
        int sin_number = 0;
        // Getting user input
        System.out.println("\nEnter your name: ");
        name = sc.nextLine();
        System.out.println("\nEnter your address: ");          
        address = sc.nextLine();
        System.out.println("\nEnter your occupation: ");
        occup = sc.nextLine();

        do{ // Date of Birth
            System.out.println("\nEnter your Date of Birth (dd/mm/yy): ");          // Add do while
            dob = sc.nextLine();
            try{
                Period period; // For date format
                String[] splitUrl = dob.split("/");
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
                sin_number = Integer.parseInt(sin);    
            }catch(Exception e){
                System.out.println("Invalid input. Please enter SIN without any spaces");
            }
        }while(sin.length()!=9);
        
        System.out.println("\nEnter your email: ");         // Add do while
        email = sc.nextLine();
        System.out.println("\nEnter your password: ");          // Add do while
        password = sc.nextLine();
        
        
        
        // Inserting into database
        Boolean val = db.createuser("user", name, email, password, address, occup, sin, dob, true);
        if (val){
            System.out.println("\nAccount created successfully");
        }
        else{
            System.out.println("\nUnable to Create user");
        }
        Menu();

    }



}
