package Functions;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
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
                            // login()
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
        String name, email, password, dob, address, occup, sin;
        String cc_num, cc_name, cc_exp, cc_cvv;

        // Getting user input
        System.out.println("\nEnter your name: ");
        name = sc.nextLine();
        System.out.println("\nEnter your Date of Birth (dd/mm/yy): ");          // Add do while
        dob = sc.nextLine();
        System.out.println("\nEnter your address: ");          // Add do while
        address = sc.nextLine();
        System.out.println("\nEnter your occupation: ");
        occup = sc.nextLine();
        System.out.println("\nEnter your SIN number: ");          // Add do while
        sin = sc.nextLine();
        System.out.println("\nEnter your email: ");         // Add do while
        email = sc.nextLine();
        System.out.println("\nEnter your password: ");          // Add do while
        password = sc.nextLine();

        //___________________________Renter Credit card Information________________________________
        System.out.println("\nEnter your credit card number: ");          // Add do while
        cc_num = sc.nextLine();
        System.out.println("\nEnter your credit card name: ");          // Add do while
        cc_name = sc.nextLine();
        System.out.println("\nEnter your credit card expiry date (mm/yy): ");          // Add do while
        cc_exp = sc.nextLine();
        System.out.println("\nEnter your credit card cvv: ");          // Add do while
        cc_cvv = sc.nextLine();
        // Check if already exists
            // if (db.checkCustomer(email, password)){
            //     System.out.println("\nAccount already exists");

        // Inserting into database
        Boolean val = db.createuser(name, email, password, address, occup, sin, dob, false);
        if (val){
            System.out.println("\nAccount created successfully");
        }
        else{
            System.out.println("Unable to Create user");
        }
        signup();
    }

    /** Create a new host account */
    public void host() throws SQLException{
        String name, email, password, dob, address, occup, sin;
        Boolean verify = true;
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
            // Checking dob
            // Check Valid string entered or not:
                //
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

        System.out.println("\nEnter your SIN number: ");          // Add do while
        sin = sc.nextLine();
        System.out.println("\nEnter your email: ");         // Add do while
        email = sc.nextLine();
        System.out.println("\nEnter your password: ");          // Add do while
        password = sc.nextLine();
        
        
        
        // Inserting into database
        Boolean val = db.createuser(name, email, password, address, occup, sin, dob, true);
        if (val){
            System.out.println("\nAccount created successfully");
        }
        else{
            System.out.println("\nUnable to Create user");
        }
        signup();

    }



}
