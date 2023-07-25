package Functions;

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
    public void Menu() {
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
                            // sign-Up();
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

    /** Sign up as a Host or customer account */
    public void signup(){
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. sign up as Host\n"+
                                "        3. Sign up as customer \n");
                System.out.print("Select:");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) {
                        case 1:
                            break;
                        case 2:
                            // host()
                            break;
                        case 3:
                            // customer();
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
    public void customer(){
        String name, email, password, dob, address, occup, sin;
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

        // Check if already exists
            // if (db.checkCustomer(email, password)){
            //     System.out.println("\nAccount already exists");

        // Inserting into database
            // db.insertCustomer(name, email, password, dob, address, occup, sin);
        
        System.out.println("\nAccount created successfully");
        signup();
    }

    /** Create a new host account*/
    public void host(){
        String name, email, password, dob, address, occup, sin;
        String cc_num, cc_name, cc_exp, cc_cvv;
        String rental_date, hostName, propertyDetails;

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
        
        //___________________________Renter History________________________________
        System.out.println("\nEnter previous rental date: ");          // Add do while
        rental_date = sc.nextLine();
        System.out.println("\nEnter previous hostname: ");          // Add do while
        hostName = sc.nextLine();
        System.out.println("\nEnter previous propery details: ");          // Add do while
        propertyDetails = sc.nextLine();

        //___________________________Renter Future________________________________
        // System.out.println("\nEnter available rental date: ");          // Add do while
        // rental_date = sc.nextLine();
        // System.out.println("\nEnter hostname: ");          // Add do while
        // hostName = sc.nextLine();
        // System.out.println("\nEnter previous propery details: ");          // Add do while
        // propertyDetails = sc.nextLine();
        
        // Inserting into database
            // db.insertRenter(name, email, password, dob, address, occup, sin);

        System.out.println("\nAccount created successfully");
        signup();
    }



}
