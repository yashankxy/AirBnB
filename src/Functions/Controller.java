package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import Users.Host;
import Users.Renter;
import Users.Users;

public class Controller {
    Scanner sc = null;
    sqlFunctions db = null;

    private Users user;
    private int id;

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

    public void Menu() throws SQLException, InterruptedException, ParseException {
        if (sc != null && db != null) {
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Login\n"+
                                "        3. Sign-Up\n" +
                                "        4. Reports\n");
                System.out.print("Select: ");
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
                        case 4:
                            report();
                            Menu();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (!val.equals("1") && !val.equals("2") && 
            !val.equals("3")  && !val.equals("4"));

            if (val.equals("1")) close();

        } 
        else {
            System.out.println("\nConnection Failed");
        }
    }
    

//______________________ Authentication _______________________ \\
    
    public void report() throws SQLException, InterruptedException, ParseException {
        System.out.println("\nWelcome to Reports");
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Total Number of Bookings\n"+
                                "        3. Total Number of Listings\n"+
                                "        4. Rank hosts\n"+
                                "        5. Report Commercial listings\n"+
                                "        6. Rank renters\n"+
                                "        7. Rank cancelation\n"+
                                "        8. Delete Profile\n"+
                                "        9. Logout \n");
                System.out.print("Select: ");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) { 
                        case 1:
                            break;
                        case 2:
                            // Total Number of Bookings
                            reportTotalBookings();
                            break;
                        case 3:
                            // Total Number of Listings
                            reportTotalListing();
                            break;
                        case 4:
                            // Rank hosts
                            reportRankHosts();
                            break;
                        case 5:
                            // Report Commercial listings
                            reportCommercialLiistings();
                            break;
                        case 6:
                            // Rank renters
                            reportRankRenters();
                            break;
                        case 7:
                            // Rank cancelation

                            reportHighesstCancelation();
                            break;
                        case 8:
                            Menu();
                            break;
                        case 9:
                            Menu();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (val.compareTo("1") != 0 && val.compareTo("2") != 0 
            && val.compareTo("3)") != 0 && val.compareTo("4") != 0 
            && val.compareTo("5") != 0 && val.compareTo("6") != 0 
            && val.compareTo("7") != 0 && val.compareTo("8") != 0 
            && val.compareTo("9") != 0 );
            
            if (val.equals("1")) close();    
            
        }else {
            System.out.println("\nConnection Failed");
        }
    }

    public void login() throws SQLException, InterruptedException, ParseException {
        String email, password;
    
        do {
            System.out.print("Email: ");
            email = sc.nextLine().trim();
            System.out.print("Password: ");
            password = sc.nextLine();

        } while (!verify_login(email, password));
        List<String> userDetails = db.getUser(email);
        
        if (userDetails.get(8) == "true") { // Renter Dashboard
            id = Integer.valueOf(userDetails.get(0));
            List<String> ccdetails = db.getcc(id);
            user = new Renter(userDetails.get(1), userDetails.get(2), userDetails.get(3), 
                            userDetails.get(4), userDetails.get(5), userDetails.get(6), 
                            userDetails.get(7), ccdetails.get(0), ccdetails.get(1), ccdetails.get(2), 
                            ccdetails.get(3));
            System.out.println("\nWelcome " + user.getName());
            renterDashboard(email);
        } else { // Host dashboard
            this.id = Integer.valueOf(userDetails.get(0));
            user = new Host(userDetails.get(0), userDetails.get(1), userDetails.get(2), userDetails.get(3), userDetails.get(4), userDetails.get(5), userDetails.get(6));
            System.out.println("\nWelcome " + user.getName());
            hostDashboard(email);
		}
    }

    /** Sign up as a Host or customer account */
    public void signup() throws SQLException, InterruptedException, ParseException{
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Sign up as Host\n"+
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
    public void renter() throws SQLException, InterruptedException, ParseException{
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
    public void host() throws SQLException, InterruptedException, ParseException{
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

    private boolean hostDashboard(String email) throws SQLException, InterruptedException, ParseException{
        System.out.println("\nWelcome to the Host Dashboard");
        String host_id = db.getIdFromEmail(email);
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Add Listing\n"+
                                "        3. My Listings\n"+
                                "        4. Manage Listings\n"+
                                "        5. Cancel Bookings\n"+
                                "        6. Host Toolkit\n"+
                                "        7. Rate my Renters\n"+
                                "        8. View Profile\n"+
                                "        9. Delete Profile\n"+
                                "        10. Logout \n");
                System.out.print("Select: ");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) { 
                        case 1:
                            break;
                        case 2:
                            // Add Listing;
                            HDashAddListing(host_id);
                            hostDashboard(email);
                            break;
                        case 3:
                            // Show listings 
                            HDshowListing(host_id);
                            hostDashboard(email);
                            break;
                        case 4:
                            // Manage Listings;
                            HDmanageListings(host_id);
                            hostDashboard(email);
                            break;
                        case 5:
                            // rateBookings();
                            HDcancelBookings(host_id);
                            hostDashboard(email);
                            break;
                        case 6:
                            // Host Toolkit
                            hostToolkitReport();
                            hostDashboard(email);
                            break;
                        case 7:
                            // Rate my Renters
                            rateRenters(host_id);
                            hostDashboard(email);
                            break;
                        case 8:
                            // View Profile
                            viewProfileHost(email);
                            hostDashboard(email);
                            break;
                        case 9:
                            // Delete Profile
                            deleteHost(host_id);
                            Menu();
                            break;
                        case 10:
                            // Logout
                            Menu();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (val.compareTo("1") != 0 && val.compareTo("2") != 0 
            && val.compareTo("3)") != 0 && val.compareTo("4") != 0 
            && val.compareTo("5") != 0 && val.compareTo("6") != 0 
            && val.compareTo("7") != 0 && val.compareTo("8") != 0 
            && val.compareTo("9") != 0 );
            
            if (val.equals("1")) close();    
            
        }else {
            System.out.println("\nConnection Failed");
        }

        return true;
    }
    
    

    private boolean renterDashboard(String email) throws SQLException, InterruptedException, ParseException{
        System.out.println("\nWelcome to the Renter Dashboard");
        String renter_id = db.getIdFromEmail(email);
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Make Booking\n"+
                                "        3. Cancel Booking\n"+
                                "        4. Search Listings\n"+
                                "        5. Rate my Bookings and Host\n"+
                                "        6. View Profile\n"+
                                "        7. Delete User \n"+
                                "        8. Logout\n");
                System.out.print("Select: ");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) { 
                        case 1:
                            break;
                        case 2:
                            HDshowListinALL();
                            makeBooking();
                            renterDashboard(email);
                            break;
                        case 3:
                            cancelBooking(true);
                            renterDashboard(email);
                            break;
                        case 4:
                            searchListings();
                            renterDashboard(email);
                            break;
                        case 5:
                            rateBookings();
                            renterDashboard(email);
                            break;
                        case 6:
                            viewProfile(email);
                            renterDashboard(email);
                            break;
                        case 7:
                            deleteRenter(renter_id);
                            Thread.sleep(1000);
                            user = null;
                            Menu();
                            break;
                        case 8:
                            System.out.println("\nLogging out...");
                            Thread.sleep(1000);
                            user = null;
                            Menu();
                            break;
                        default:
                            System.out.println("Invalid option");
                            break;
                    }
                } catch (NumberFormatException e) {
                    val = "-1";
                }
            } while (val.compareTo("1") != 0 && val.compareTo("2") != 0 
                && val.compareTo("3)") != 0 && val.compareTo("4") != 0 
                && val.compareTo("5") != 0 && val.compareTo("6") != 0 
                && val.compareTo("7") != 0 && val.compareTo("8") != 0);
            if (val.equals("1")) close();    
            
        }else {
            System.out.println("\nConnection Failed");
        }

        return true;
    }


// _________________________ Listing ___________________________ \\

    /** New Listing */
    private void HDashAddListing(String host_id) throws SQLException {
        float latitude, longitude;
        String type_of_listing, postal_code, city, country;
        
        // Loop until valid city input is provided or user inputs "exit"
        do {
            System.out.print("City: ");
            city = sc.nextLine().trim();
            if (city.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            if (city.isEmpty()){
                System.out.print("Please complete the field \n");
            }
        } while (city.isEmpty()); // Continue asking if city is empty

        // Loop until valid country input is provided or user inputs "exit"
        do {
            System.out.print("Country: ");
            country = sc.nextLine().trim();
            if (country.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            if (country.isEmpty()){
                System.out.print("Please complete the field \n");
            }
        } while (country.isEmpty()); // Continue asking if country is empty

        // Loop until valid postal code input is provided or user inputs "exit"
        do {
            System.out.print("Postal Code: ");
            postal_code = sc.nextLine().trim();
            if (postal_code.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            if (postal_code.length() > 9){
                System.out.print("Invalid input: too many characters\n");
                postal_code = "";
            }
            if (postal_code.isEmpty()){
                System.out.print("Please complete the field \n");
            }
        } while (postal_code.isEmpty()); // Continue asking if postal code is empty

        // Loop until valid latitude input is provided or user inputs "exit"
        do {
            System.out.print("Latitude: ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            try {
                latitude = Float.parseFloat(input);
            } catch (NumberFormatException e) {
                latitude = 200; // Set latitude to null if invalid number format
            }
            if (latitude > 90 || latitude < -90){
                System.out.print("Please input a valid value \n");
            }
        } while (latitude > 90 || latitude < -90); // Continue asking if latitude is not a valid integer

        // Loop until valid longitude input is provided or user inputs "exit"
        do {
            System.out.print("Longitude: ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            try {
                longitude = Float.parseFloat(input);
            } catch (NumberFormatException e) {
                longitude = 200; // Set longitude to null if invalid number format
            }
            if (longitude > 180 || longitude < -180){
                System.out.print("Please input a valid value \n");
            }
        } while (longitude > 180 || longitude < -180); // Continue asking if longitude is not a valid integer
        
        // Loop until valid type_of_listing input is provided or user inputs "exit"
        do {
            System.out.print("Type of Listing (full house, apartment, room): ");
            type_of_listing = sc.nextLine().trim().toLowerCase();
            if (type_of_listing.equalsIgnoreCase("exit")) {
                return; // Return if the user inputs "exit"
            }
            if (!type_of_listing.equals("full house") &&
                 !type_of_listing.equals("apartment") &&
                 !type_of_listing.equals("room")){
                    System.out.print("Please input one of the valid values: full house, apartment or room\n");
                 }
        } while (!type_of_listing.equals("full house") &&
                 !type_of_listing.equals("apartment") &&
                 !type_of_listing.equals("room")); // Continue asking if invalid type_of_listing

        // Inserting into listing
        String listingId = db.createListing(host_id, latitude, longitude, 
                    type_of_listing, postal_code, city, country);
        if (listingId == ""){
            System.out.println("Failed to generate id \n");
            return;
        }

        // Assign the amenities
        List<String> listOfAmenities = new ArrayList<>(Collections.nCopies(10, "0"));

        System.out.println("Select amenities for this listing:\n"
                + " 1. wifi\n" + " 2. washer\n" + " 3. air_conditioning\n"
                + " 4. dedicated_workspace\n" + " 5. hair_dryer\n"
                + " 6. kitchen\n" + " 7. dryer\n" + " 8. heating\n"
                + " 9. tv\n" + " 10. iron\n");
        System.out.println("Input example: '1,2,7'");
        String input = "";
        do {
            System.out.print("Enter your selection: ");
            input = sc.nextLine().trim();
            if (!input.isEmpty()) {
                // Split the input string into individual amenity choices
                String[] selectedAmenities = input.split(",");

                // Update the list of amenities accordingly
                for (String choice : selectedAmenities) {
                    try {
                        int amenityChoice = Integer.parseInt(choice.trim());
                        if (amenityChoice >= 1 && amenityChoice <= 10) {
                            // Update the corresponding amenity in the list
                            listOfAmenities.set(amenityChoice - 1, "1");
                        } else {
                            System.out.println("Invalid amenity choice: " + choice);
                            input = "";
                            listOfAmenities = new ArrayList<>(Collections.nCopies(10, "0"));
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input format: \n" + choice);
                        input = "";
                        listOfAmenities = new ArrayList<>(Collections.nCopies(10, "0"));
                    }
                }
            }
            else{
                input = "done";
            }

        } while (input.isEmpty());
        
        // Inserting into amenities
        db.addAmenities(listingId, listOfAmenities);

        // Assign calendar availability
        Boolean done = false;
        do{
            List<String> calendar_availability = new ArrayList<>();
            Boolean added_date = false;
            String startDateStr;
            String endDateStr;
            String price = "";

            // assign dates
            do{
                System.out.print("Enter start date (yyyy-MM-dd): ");
                startDateStr = sc.nextLine().trim();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                endDateStr = sc.nextLine().trim();
                try {
                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate startDate = LocalDate.parse(startDateStr, dateFormat);
                        LocalDate endDate = LocalDate.parse(endDateStr, dateFormat);
                        if (startDate.isBefore(endDate)) {
                            LocalDate currentDate = startDate;
                            while (!currentDate.isAfter(endDate)) {
                                calendar_availability.add(dateFormat.format(currentDate));
                                currentDate = currentDate.plusDays(1);
                            }
                            added_date = true;
                        } else {
                            System.out.println("Error: Start date must be before the end date.");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: Wrong Input." + e);;
                }

            } while(!added_date);
            
            // assign price 
            float priceF = -1;
            do{
                System.out.print("Price: ");
                price = sc.nextLine().trim().toLowerCase();
                try {
                    priceF = Float.parseFloat(price);
                } catch (NumberFormatException e) {
                    System.out.print("Please input a valid value \n");
                    priceF = -1; // Set latitude to null if invalid number format
                }
            } while(priceF < 0);

            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                db.addAvailability(listingId, calendar_availability, price);
            }
            System.out.println("Completed");
            String answerContinue;
            do{
                System.out.println("Do you want to add more availability days? \n"+
                                "    1. Yes\n"+ "    2. No");
                System.out.print("Select: ");
                answerContinue = sc.nextLine().trim().toLowerCase();
                if (answerContinue.equals("2")){
                    done = true;
                }
                if (!answerContinue.equals("1") && !answerContinue.equals("2")){
                    System.out.println("Invalid Input");
                }
            } while (!answerContinue.equals("1") && !answerContinue.equals("2"));
        } while(!done);

        System.out.println("Added New Listing!");
    }

    private void HDshowListing(String host_id) throws SQLException{
        ResultSet rs = db.GetAllActiveListings(host_id);
        System.out.printf("-----------------------------------------------------------" +
        "-----------------------------------------------------------\n" );
        System.out.printf("%-4s %-15s %-10s %-10s %-10s %-25s %-20s%n",
                "ID", "Type of Listing", "Latitude", "Longitude", "Postal Code", "City", "Country");

        while (rs.next()) {
            int id = rs.getInt("id");
            String typeOfListing = rs.getString("type_of_listing");
            float latitude = rs.getFloat("latitude");
            float longitude = rs.getFloat("longitude");
            String postalCode = rs.getString("postal_code");
            String city = rs.getString("city");
            String country = rs.getString("country");

            System.out.printf("%-4d %-15s %-10.4f %-10.4f %-10s %-25s %-20s %n",
                    id, typeOfListing, latitude, longitude, postalCode, city, country);
        }
        System.out.printf("-----------------------------------------------------------" +
        "-----------------------------------------------------------" );
    }

    private void HDshowListinALL() throws SQLException{
        ResultSet rs = db.GetEveryActiveListings();
        System.out.println(rs.toString());
        System.out.printf("-----------------------------------------------------------" +
        "-----------------------------------------------------------\n" );
        System.out.printf("%-4s %-15s %-10s %-10s %-10s %-25s %-20s %-15s %-10s%n",
                "ID", "Type of Listing", "Latitude", "Longitude", "Postal Code", 
                "City", "Country", "Date", "Price");

        while (rs.next()) {
            int id = rs.getInt("id");
            String typeOfListing = rs.getString("type_of_listing");
            float latitude = rs.getFloat("latitude");
            float longitude = rs.getFloat("longitude");
            String postalCode = rs.getString("postal_code");
            String city = rs.getString("city");
            String country = rs.getString("country");
            String date = rs.getString("date");
            String price = rs.getString("price");

            System.out.printf("%-4d %-15s %-10.4f %-10.4f %-10s %-25s %-20s %-15s %-10s%n",
                    id, typeOfListing, latitude, longitude, postalCode, city, country, date, price);
        }
        System.out.printf("-----------------------------------------------------------" +
        "-----------------------------------------------------------\n" );
    }

    private void HDbookingsAvailable(String list_id) throws SQLException{
        ResultSet rs = db.GetlistingBookingsAvailable(list_id);
        System.out.printf("-------------------------------------------\n" );
        System.out.printf("%-4s %-15s %-15s %-15s %n",
                "ID", "Start Date", "End Date", "Pricing");

        while (rs.next()) {
            int id = rs.getInt("id");
            String start_date = rs.getString("start_date");
            String finish_date = rs.getString("finish_date");
            String pricing = rs.getString("pricing");

            System.out.printf("%-4s %-15s %-15s %-15s %n",
                    id, start_date, finish_date, pricing);
        }
        System.out.printf("-------------------------------------------\n" );
    }

    private void HDshowListingAvailability(String listing_id) throws SQLException{
        ResultSet rs = db.GetListingAvailability(listing_id);
        System.out.printf("------------------------------------------\n");
        System.out.printf("%-10s %-15s %-15s %n",
                "listing_id", "date", "price");

        while (rs.next()) {
            int id = rs.getInt("listing_id");
            String date = rs.getString("date");
            float price = rs.getFloat("price");

            System.out.printf("%-10s %-15s %-15s %n",
                    id, date, price);
        }
        System.out.printf("------------------------------------------\n");
    }

    private void HDaddListingAvailability(String listing_id) throws SQLException{
       Boolean done = false;
        do{
            List<String> calendar_availability = new ArrayList<>();
            Boolean added_date = false;
            String startDateStr;
            String endDateStr;
            String price = "";

            // assign dates
            do{
                System.out.print("Enter start date (yyyy-MM-dd): ");
                startDateStr = sc.nextLine().trim();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                endDateStr = sc.nextLine().trim();
                try {
                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate startDate = LocalDate.parse(startDateStr, dateFormat);
                        LocalDate endDate = LocalDate.parse(endDateStr, dateFormat);
                        if (startDate.isBefore(endDate)) {
                            LocalDate currentDate = startDate;
                            while (!currentDate.isAfter(endDate)) {
                                calendar_availability.add(dateFormat.format(currentDate));
                                currentDate = currentDate.plusDays(1);
                            }
                            added_date = true;
                        } else {
                            System.out.println("Error: Start date must be before the end date.");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: Wrong Input." + e);;
                }

            } while(!added_date);
            
            // assign price 
            float priceF = -1;
            do{
                System.out.print("Price: ");
                price = sc.nextLine().trim().toLowerCase();
                try {
                    priceF = Float.parseFloat(price);
                } catch (NumberFormatException e) {
                    System.out.print("Please input a valid value \n");
                    priceF = -1; // Set latitude to null if invalid number format
                }
            } while(priceF < 0);

            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                db.addAvailability(listing_id, calendar_availability, price);
            }
            System.out.println("Completed");
            String answerContinue;
            do{
                System.out.println("Do you want to add more availability days? \n"+
                                "    1. Yes\n"+ "    2. No");
                System.out.print("Select: ");
                answerContinue = sc.nextLine().trim().toLowerCase();
                if (answerContinue.equals("2")){
                    done = true;
                }
                if (!answerContinue.equals("1") && !answerContinue.equals("2")){
                    System.out.println("Invalid Input");
                }
            } while (!answerContinue.equals("1") && !answerContinue.equals("2"));
        } while(!done);

       System.out.println("Added!");
       HDshowListingAvailability(listing_id);
   }

    private void HDchangeListingPricing(String listing_id) throws SQLException{
       Boolean done = false;
        do{
            List<String> calendar_availability = new ArrayList<>();
            Boolean added_date = false;
            String startDateStr;
            String endDateStr;
            String price = "";

            // assign dates
            do{
                System.out.print("Enter start date (yyyy-MM-dd): ");
                startDateStr = sc.nextLine().trim();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                endDateStr = sc.nextLine().trim();
                try {
                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate startDate = LocalDate.parse(startDateStr, dateFormat);
                        LocalDate endDate = LocalDate.parse(endDateStr, dateFormat);
                        if (startDate.isBefore(endDate)) {
                            LocalDate currentDate = startDate;
                            while (!currentDate.isAfter(endDate)) {
                                calendar_availability.add(dateFormat.format(currentDate));
                                currentDate = currentDate.plusDays(1);
                            }
                            added_date = true;
                        } else {
                            System.out.println("Error: Start date must be before the end date.");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: Wrong Input." + e);;
                }

            } while(!added_date);
            
            // assign price 
            float priceF = -1;
            do{
                System.out.print("Price: ");
                price = sc.nextLine().trim().toLowerCase();
                try {
                    priceF = Float.parseFloat(price);
                } catch (NumberFormatException e) {
                    System.out.print("Please input a valid value \n");
                    priceF = -1; // Set latitude to null if invalid number format
                }
            } while(priceF < 0);

            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                db.changePrice(listing_id, startDateStr, endDateStr, price);
            }
            System.out.println("Completed");
            String answerContinue;
            do{
                System.out.println("Do you want to change more prices? \n"+
                                "    1. Yes\n"+ "    2. No");
                System.out.print("Select: ");
                answerContinue = sc.nextLine().trim().toLowerCase();
                if (answerContinue.equals("2")){
                    done = true;
                }
                if (!answerContinue.equals("1") && !answerContinue.equals("2")){
                    System.out.println("Invalid Input");
                }
            } while (!answerContinue.equals("1") && !answerContinue.equals("2"));
        } while(!done);

       System.out.println("Added!");
       HDshowListingAvailability(listing_id);
   }

    private void HDdeleteAvailability(String listing_id) throws SQLException{
       Boolean done = false;
        do{
            List<String> calendar_availability = new ArrayList<>();
            Boolean added_date = false;
            String startDateStr;
            String endDateStr;
            String price = "";

            // assign dates
            do{
                System.out.print("Enter start date (yyyy-MM-dd): ");
                startDateStr = sc.nextLine().trim();
                System.out.print("Enter end date (yyyy-MM-dd): ");
                endDateStr = sc.nextLine().trim();
                try {
                    if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate startDate = LocalDate.parse(startDateStr, dateFormat);
                        LocalDate endDate = LocalDate.parse(endDateStr, dateFormat);
                        if (startDate.isBefore(endDate)) {
                            LocalDate currentDate = startDate;
                            while (!currentDate.isAfter(endDate)) {
                                calendar_availability.add(dateFormat.format(currentDate));
                                currentDate = currentDate.plusDays(1);
                            }
                            added_date = true;
                        } else {
                            System.out.println("Error: Start date must be before the end date.");
                        }
                    }

                } catch (Exception e) {
                    System.out.println("Error: Wrong Input." + e);;
                }

            } while(!added_date);
            

            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                db.deleteAvailability(listing_id, startDateStr, endDateStr);
            }
            System.out.println("Completed");
            String answerContinue;
            do{
                System.out.println("Do you want to delete more available dates? \n"+
                                "    1. Yes\n"+ "    2. No");
                System.out.print("Select: ");
                answerContinue = sc.nextLine().trim().toLowerCase();
                if (answerContinue.equals("2")){
                    done = true;
                }
                if (!answerContinue.equals("1") && !answerContinue.equals("2")){
                    System.out.println("Invalid Input");
                }
            } while (!answerContinue.equals("1") && !answerContinue.equals("2"));
        } while(!done);

       System.out.println("Completed!");
       HDshowListingAvailability(listing_id);
   }

    private void HDremoveListing(String listing_id) throws SQLException{
        String answer = "";
        System.out.println("This action will unlist your listing and cancel all pending bookings: ");
        System.out.println("   1. Exit\n" + "   2. Confirm");
            do{
                System.out.print("Select: ");
                answer = sc.nextLine().trim();

                if (answer.equals("1")){
                    return;
                }
                else if (answer.equals("2")){
                    db.UnlistListing(listing_id);
                    System.out.println("Completed!");
                }
                else{
                    System.out.println("Invalid Input");
                    answer = "";
                }
               
            } while (answer.isEmpty());

   }

    private void HDmanageListings(String host_id) throws SQLException{
        HDshowListing(host_id);
        if(!db.ListingNotEmpty(host_id)){
            System.out.println("No listings present, exiting:");
            return;
        }
        System.out.println("\n Select the ID of the listing for changes:");
        String selectedID = "";
        do {
            System.out.print("Select: ");
            selectedID = sc.nextLine().trim();
            try {
                Integer intID = Integer.parseInt(selectedID);
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid value");
                selectedID = "";
            }
            if (!selectedID.isEmpty()){
                if(!db.ListingIDNotEmpty(host_id, selectedID)){
                    System.out.println("Please input a valid value");
                    selectedID = "";
                }
            }
        } while (selectedID.isEmpty());

        String val;
        int choice;
        do {
            System.out.println("\n Operation to perform: \n"+
                            "        1. Exit \n"+
                            "        2. Add availability and assign price\n"+
                            "        3. Change price\n"+
                            "        4. Remove listing availability\n"+
                            "        5. Remove Listing\n");
            System.out.print("Select: ");
            val = sc.nextLine();
            try {
                choice = Integer.parseInt(val);
                switch (choice) { 
                    case 1:
                        break;
                    case 2:
                        // Add availability and assign price;
                        HDshowListingAvailability(selectedID);
                        HDaddListingAvailability(selectedID);
                        break;
                    case 3:
                        // Change price
                        HDshowListingAvailability(selectedID);
                        HDchangeListingPricing(selectedID);
                        break;
                    case 4:
                        // Delete available dates
                        HDshowListingAvailability(selectedID);
                        HDdeleteAvailability(selectedID);
                        break;
                    case 5:
                        // Remove Listing;
                        HDshowListingAvailability(selectedID);
                        HDremoveListing(selectedID);
                        break;
                    default:
                        System.out.println("Invalid option");
                        break;
                }
            } catch (NumberFormatException e) {
                val = "-1";
            }
        } while (val.compareTo("1") != 0 && val.compareTo("2") != 0 && val.compareTo("3)") != 0 && val.compareTo("4") != 0 && val.compareTo("5") != 0);
        if (val.equals("1")) close();  
    }

    private void HDcancelBookings(String host_id) throws SQLException{
        HDshowListing(host_id);
        if(!db.ListingNotEmpty(host_id)){
            System.out.println("No listings present, exiting:");
            return;
        }
        System.out.println("\n Select the ID of the listing for booking changes:");
        String selectedID = "";
        do {
            System.out.print("Select: ");
            selectedID = sc.nextLine().trim();
            try {
                Integer intID = Integer.parseInt(selectedID);
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid value");
                selectedID = "";
            }
            if (!selectedID.isEmpty()){
                if(!db.ListingIDNotEmpty(host_id, selectedID)){
                    System.out.println("Please input a valid value");
                    selectedID = "";
                }
            }
        } while (selectedID.isEmpty());

        HDbookingsAvailable(selectedID); 

        if(!db.BookingNotEmpty(selectedID)){
            System.out.println("No bookings present, exiting:");
            return;
        }

        System.out.print("Select the id of the booking to cancel\n");
        String bookingID = "";
        do {
            System.out.print("Select: ");
            bookingID = sc.nextLine().trim();
            try {
                Integer bID = Integer.parseInt(bookingID);
            } catch (NumberFormatException e) {
                System.out.println("Please input a valid value");
                bookingID = "";
            }
            if (!bookingID.isEmpty()){
                if(!db.BookingsAvailableIsNotEmpty(selectedID, bookingID)){
                    System.out.println("Please input a valid value");
                    bookingID = "";
                }
            }
        } while (bookingID.isEmpty());

        db.HostCancelBookingOne(bookingID);
        System.out.println("Completed!");
    }

    private void searchListings() throws SQLException, InterruptedException, ParseException {
        if (sc != null && db != null){
            String val;
            int choice;
            do {
                System.out.println("\n Options: \n"+
                                "        1. Exit \n"+
                                "        2. Type of Listing\n"+
                                "        3. Latitude Longitude\n"+
                                "        4. Postal code\n"+
                                "        5. City\n"+
                                "        6. Logout \n");
                System.out.print("Select: ");
                val = sc.nextLine();
                try {
                    choice = Integer.parseInt(val);
                    switch (choice) { 
                        case 1:
                            close();
                            break;
                        case 2:
                            search(val);
                            break;
                        case 3:
                            search(val);
                            break;
                        case 4:
                            search(val);
                            break;
                        case 5:
                            search(val);
                            break;
                        case 6:
                            System.out.println("\nLogging out...");
                            Thread.sleep(1000);
                            user = null;
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
    }
    
    private void search(String search) throws SQLException, InterruptedException, ParseException {
		String[] val;
		String[] dates = new String[2];
        LocalDate enteredStartDate;

		dates[0] = "";

		String[] amenities = new String[10];
        
		List<String> col = new ArrayList<String>();
		List<String> col_val = new ArrayList<String>();
        
        List<String> semi_final_lids = new ArrayList<String>();
        List<String> final_lids = new ArrayList<String>();
        boolean check_lat_long = false;

        /**Case 1: Type of Listing
         * val[0] = Type of Listing
         */
        if(search.equals("2")){  // Type of Listing
            val = new String[1];
            do {
                System.out.println("Enter type of listing: ");
                System.out.println("1. Full House");
                System.out.println("2. Apartment");
                System.out.println("3. Room");
                val[0] = sc.nextLine().trim();
                if (val[0].equals("1") || val[0].equals("2") || val[0].equals("3")) {
                    break;
                }
                else {
                    System.out.println("Invalid type of listing");
                }
            } while (true);
            String type_of_listing;
            if (val[0].equals("1")){
                type_of_listing = "full house";
            }else if(val[0].equals("2")){
                type_of_listing = "apartment";
            }else{
                type_of_listing = "room";
            }         
            semi_final_lids = db.select("listing", "id", "type_of_listing", type_of_listing); 
        }
        /**
         * Case 3: Latitude, Longitude and Distance
         * 
         * val[0] = Latitude
         * val[1] = Longitude
         * val[2] = Distance
         */
		else if(search.equals("3")) { // Latitude and Longitude
			
            val = new String[3];

            do { // get lat
                System.out.print("Enter latitude: ");
                val[0] = sc.nextLine().trim();
                Double num = Double.parseDouble(val[0]);
                if (num < -90 || num > 90) {
                    System.out.println("Invalid latitude");
                }
                else{
                    break;
                }
            } while (true);
            do { // get lon
                System.out.print("Enter longitude: ");
                val[1] = sc.nextLine().trim();
                Double num = Double.parseDouble(val[1]);
                if (num < -180 || num > 180) {
                    System.out.println("Invalid longitude");
                }
                else{
                    break;
                }
            } while (true);
			do { // get Distance
				System.out.print("Distance (Defaults 50Km): ");
				val[2] = sc.nextLine().trim();
				if(val[2].equals("")) {
					val[2] = "50";
					break;
				}
                try{
                    Double.parseDouble(val[2]);
                    break;
                }catch(Exception e){
                    System.out.println("Invalid distance");
                }
			} while (true);
            
            check_lat_long = true;
        
        } 
        /**
         * Case 4: Postal Code
         * val[0] = Postal Code
         */
        else if(search.equals("4")) { // Postal Code
				val = new String[10];
				do { // Postal code
					System.out.print("Postal Code: ");
					val[0] = sc.nextLine().trim();
					if(val[0].equals("")) {
                        System.out.println("Invalid postal code, Enter Postal Code again");
                    } else {
                        break;
                    }
				} while(true);

            semi_final_lids = db.select("listing", "id", "postal_code", val[0]); 

		} 
        /**
         * Case 5: City
         * val[0] = City
         */
        else if(search.equals("5")) { // City
            val = new String[1];
            do {
                System.out.print("Enter city: ");
                val[0] = sc.nextLine().trim();
                if(val[0].equals("")) {
                    System.out.println("Invalid city");
                } else {
                    break;
                }
            } while(true);
            semi_final_lids = db.select("listing", "id", "city", val[0]); 

        }
	    
		else{
            System.out.println("Error: Invalid Search Type");
            searchListings();
            return;
        }
		
        // Dates and Amenities
        Boolean check_dates = false;
        Boolean check_amenities = false;
            String temp;
            do{
                System.out.println("Do you want to filter dates? (y/n): ");
                temp = sc.nextLine().trim();

                
                if(temp.equals("n")){
                    dates[0] = "";
                    dates[1] = "";
                }
                else if(!temp.equals("y")){
                    System.out.println("Invalid input");
                }
            }while(!temp.equals("y") && !temp.equals("n"));
            if(temp.equals("y")){
                    System.out.println("\nFilter DATES\n");
                    do{            // --- Start Date
                        System.out.println("Enter start date (yyyy/mm/dd): ");
                        dates[0] = sc.nextLine().trim();
                        try{
                            LocalDate tmr = LocalDate.now().plusDays(1);
                            enteredStartDate = LocalDate.parse(dates[0], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
                            
                            if (enteredStartDate.isBefore(tmr)) {
                                System.out.println("Start date should be after the present date.");
                            }
                            else{ 
                                check_dates = true;
                                break;
                            }
                        }catch(Exception e){
                            System.out.println("Invalid date format");
                        }
                    }while(true);
                    do{            // --- End Date
                    System.out.println("Enter end date (yyyy/mm/dd): ");
                    dates[1] = sc.nextLine().trim();
                    try{
                        LocalDate enteredEndDate = LocalDate.parse(dates[1], DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        
                        if (enteredEndDate.isBefore(enteredStartDate)) {
                            System.out.println("End date should be after the start date.");
                        }else{break;}
                    }catch(Exception e){
                        System.out.println("Invalid date format");
                    }
                    // Check other things
                }while(true);
                }
            if(dates[0].equals("")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate today = LocalDate.now();
                dates[0] = formatter.format(today);
                dates[1] = "";
            }
            

            do{
                System.out.println("Do you want to filter amenities? (y/n): ");
                temp = sc.nextLine().trim();
                if (temp.equals("y")) {
                    check_amenities = true;
                }
            }while(!temp.equals("y") && !temp.equals("n"));
            List<String> amenities_y = new ArrayList<>();
            if(temp.equals("y")){
                System.out.println("\nFilter AMENITIES\n");
                /*
                    wifi BOOLEAN NOT NULL DEFAULT 0,
                    washer BOOLEAN NOT NULL DEFAULT 0,
                    air_conditioning BOOLEAN NOT NULL DEFAULT 0,
                    dedicated_workspace BOOLEAN NOT NULL DEFAULT 0,
                    hair_dryer BOOLEAN NOT NULL DEFAULT 0,
                    kitchen BOOLEAN NOT NULL DEFAULT 0,
                    dryer BOOLEAN NOT NULL DEFAULT 0,
                    heating BOOLEAN NOT NULL DEFAULT 0,
                    tv BOOLEAN NOT NULL DEFAULT 0,
                    iron BOOLEAN NOT NULL DEFAULT 0,
                */
                do {
                    System.out.print("wifi Included? (y/n or leave blank): ");
                    amenities[0] = sc.nextLine().trim();
                    if(amenities[0].equals("")) {
                        break;
                    }
                    if(!amenities[0].equalsIgnoreCase("y") && !amenities[0].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("wifi");
                        col_val.add(amenities[0]);
                    }
                } while(!amenities[0].equalsIgnoreCase("y") && !amenities[0].equalsIgnoreCase("n"));
                do {
                    System.out.print("washer Included? (y/n or leave blank): ");
                    amenities[1] = sc.nextLine().trim();
                    if(amenities[1].equals("")) {
                        break;
                    }
                    if(!amenities[1].equalsIgnoreCase("y") && !amenities[1].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("washer");
                        col_val.add(amenities[1]);
                    }
                } while(!amenities[1].equalsIgnoreCase("y") && !amenities[1].equalsIgnoreCase("n"));
                do {
                    System.out.print("air_conditioning Included? (y/n or leave blank): ");
                    amenities[2] = sc.nextLine().trim();
                    if(amenities[2].equals("")) {
                        break;
                    }
                    if(!amenities[2].equalsIgnoreCase("y") && !amenities[2].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("air_conditioning");
                        col_val.add(amenities[2]);
                    }
                } while(!amenities[2].equalsIgnoreCase("y") && !amenities[2].equalsIgnoreCase("n"));
                do {
                    System.out.print("dedicated_workspace Included? (y/n or leave blank): ");
                    amenities[3] = sc.nextLine().trim();
                    if(amenities[3].equals("")) {
                        break;
                    }
                    if(!amenities[3].equalsIgnoreCase("y") && !amenities[3].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("dedicated_workspace");
                        col_val.add(amenities[3]);
                    }
                } while(!amenities[3].equalsIgnoreCase("y") && !amenities[3].equalsIgnoreCase("n"));
                do {
                    System.out.print("hair_dryer Included? (y/n or leave blank): ");
                    amenities[4] = sc.nextLine().trim();
                    if(amenities[4].equals("")) {
                        break;
                    }
                    if(!amenities[4].equalsIgnoreCase("y") && !amenities[4].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("hair_dryer");
                        col_val.add(amenities[4]);
                    }
                } while(!amenities[4].equalsIgnoreCase("y") && !amenities[4].equalsIgnoreCase("n"));
                do {
                    System.out.print("kitchen Included? (y/n or leave blank): ");
                    amenities[5] = sc.nextLine().trim();
                    if(amenities[5].equals("")) {
                        break;
                    }
                    if(!amenities[5].equalsIgnoreCase("y") && !amenities[5].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("kitchen");
                        col_val.add(amenities[5]);
                    }
                } while(!amenities[5].equalsIgnoreCase("y") && !amenities[5].equalsIgnoreCase("n"));
                do {
                    System.out.print("dryer Included? (y/n or leave blank): ");
                    amenities[6] = sc.nextLine().trim();
                    if(amenities[6].equals("")) {
                        break;
                    }
                    if(!amenities[6].equalsIgnoreCase("y") && !amenities[6].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("dryer");
                        col_val.add(amenities[6]);
                    }
                } while(!amenities[6].equalsIgnoreCase("y") && !amenities[6].equalsIgnoreCase("n"));
                do {
                    System.out.print("heating Included? (y/n or leave blank): ");
                    amenities[7] = sc.nextLine().trim();
                    if(amenities[7].equals("")) {
                        break;
                    }
                    if(!amenities[7].equalsIgnoreCase("y") && !amenities[7].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("heating");
                        col_val.add(amenities[7]);
                    }
                } while(!amenities[7].equalsIgnoreCase("y") && !amenities[7].equalsIgnoreCase("n"));
                do {
                    System.out.print("tv Included? (y/n or leave blank): ");
                    amenities[8] = sc.nextLine().trim();
                    if(amenities[8].equals("")) {
                        break;
                    }
                    if(!amenities[8].equalsIgnoreCase("y") && !amenities[8].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("tv");
                        col_val.add(amenities[8]);
                    }
                } while(!amenities[8].equalsIgnoreCase("y") && !amenities[8].equalsIgnoreCase("n"));
                do {
                    System.out.print("iron Included? (y/n or leave blank): ");
                    amenities[9] = sc.nextLine().trim();
                    if(amenities[9].equals("")) {
                        break;
                    }
                    if(!amenities[9].equalsIgnoreCase("y") && !amenities[9].equalsIgnoreCase("n")) {
                        System.out.println("Invalid entry");
                    } else {
                        col.add("iron");
                        col_val.add(amenities[9]);
                    }
                } while(!amenities[9].equalsIgnoreCase("y") && !amenities[9].equalsIgnoreCase("n"));
            }
            else{
                amenities_y = null;
            }
        
        // SQL Queries
        // Get all the listings with following amenities and availability dates

        // Extract names with "yes" and add them to the new list
        for (int i = 0; i < col.size(); i++) {
            if (col_val.get(i).equalsIgnoreCase("y")) {
                amenities_y.add(col.get(i));
            }
        }
        List<Integer> filtered_lid;

        // Todo Add a case for when dates are not given but amenities are given

        if (!check_dates && !check_amenities){ // Dates = not given and amenities = not given
            filtered_lid = db.Getid_ActiveListings();
        }
        else if(!check_dates && check_amenities){
            
            filtered_lid = db.findListingsWithAmenities(amenities_y);
        }
        else{ 
            filtered_lid = db.findListingsWithAvailability(dates[0], dates[1], amenities_y);   
        }
        
        Set<Integer> uniqueSet = new HashSet<>(filtered_lid);
        List<Integer> filtered_lid_without_duplicates = new ArrayList<>(uniqueSet);
        Set<Integer> filteredSet = new HashSet<>(filtered_lid_without_duplicates);


        if (check_lat_long){
            double lat = Double.parseDouble(val[0]);
            double lon = Double.parseDouble(val[1]);
            double distance_threshold = Double.parseDouble(val[2]);
            double lat_1;
            double lon_1;
            for (int fid : filtered_lid_without_duplicates){
                List<Float> lat_lon = new ArrayList<>();
                lat_lon = db.getLatandLon(fid);
                lat_1 = lat_lon.get(0);
                lon_1 = lat_lon.get(1);
                double distance_1 = haversine(lat, lon, lat_1, lon_1);
                if (distance_1 <= distance_threshold) {
                    // The current fid is inside the specified distance
                    final_lids.add(Integer.toString(fid));
                }
            }
        }else{
            for (String lid : semi_final_lids) {
                int lidInt = Integer.parseInt(lid);
                if (filteredSet.contains(lidInt)) {
                    final_lids.add(lid);
                }
            }
        }
        // Print the final list of listings
        System.out.println("---------------------------------");
        System.out.println("Listings found: ");
        System.out.println("---------------------------------");
        for(String lid : final_lids){

            List<String> x = db.select1(lid);
            System.out.println("       Listing :" +  x);
        }
        System.out.println("---------------------------------");

	}



    //_________________________ Bookings _________________________ \\
   
    private void makeBooking() throws SQLException, ParseException {
        String lid;
		do {
			System.out.print("\nEnter listing number to book: ");
			lid = sc.nextLine();
		} while (!verifylisting(lid));

        double lid_price;
        int timePeriod =0;
        String[] dates = null;
        String startdate, enddate;
        LocalDate enteredStartDate, enteredEndDate; 

        // Date sd;
        // Date ed;

        do{            // --- Start Date
            System.out.println("Enter start date (dd/mm/yyyy): ");
            startdate = sc.nextLine().trim();
            try{
                LocalDate tmr = LocalDate.now().plusDays(1);
                enteredStartDate = LocalDate.parse(startdate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                if (enteredStartDate.isBefore(tmr)) {
                    System.out.println("Start date should be after the present date.");
                }
                else{ break;}
            }catch(Exception e){
                System.out.println("Invalid date format");
            }
        }while(true);

        do{            // --- End Date
            System.out.println("Enter end date (dd/mm/yyyy): ");
            enddate = sc.nextLine().trim();
             try{
                enteredEndDate = LocalDate.parse(enddate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if (enteredEndDate.isBefore(enteredStartDate)) {
                    System.out.println("End date should be after the start date.");
                }else{break;}
            }catch(Exception e){
                System.out.println("Invalid date format");
            }
            // Check other things
        }while(true);


        boolean check  = verifyavailability(startdate, enddate, lid);
        if (check){

            // Calculate Number of date: timeperiod = enddate - startdate // timePeriod in days;
            timePeriod = (int) ChronoUnit.DAYS.between(enteredStartDate, enteredEndDate);
            List<LocalDate> allDatesBetween = new ArrayList<>();
         
            for (int i = 0; i <= timePeriod; i++) {
                allDatesBetween.add(enteredStartDate.plusDays(i));
            }
            List<String> allDatesAsString = new ArrayList<>();
            for (LocalDate date : allDatesBetween) {
                allDatesAsString.add(date.toString());
            }
         

            List<String> prices = db.getPrice(allDatesAsString, lid);
            System.out.println(prices);
            // Calculate Total Price: timePeriod * price
            Double total=(double) 0;
            for(String price : prices){
                total += Double.parseDouble(price);
            }
            // round off total
            total = Math.round(total * 100.0) / 100.0;
        
            
            
            // Print Invoice:
            System.out.println("\nInvoice: ");
            System.out.println("Listing ID: " + lid);
            System.out.println("Start Date: " + startdate);
            System.out.println("End Date: " + enddate);
            System.out.println("Total Days: " + timePeriod);
            System.out.println("Total Price: " + total);
            System.out.println("Confirm booking? (y/n)");
            String confirm = sc.nextLine();
            if (confirm.equals("y")){
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd");
                java.util.Date sdate = inputFormat.parse(startdate);
                java.util.Date edate = inputFormat.parse(enddate);
                String fstartdate = outputFormat.format(sdate);
                String fenddate = outputFormat.format(edate);
                int bid = db.bookListings(Integer.parseInt(lid), this.id, fstartdate, fenddate, total, "normal");       
                String booking_id = Integer.toString(bid);
                
                // Update Availability
                List<String> s_d = db.select("bookings", "start_date", "id", booking_id);
                List<String> e_d = db.select("bookings", "finish_date", "id", booking_id);
                LocalDate startDate = LocalDate.parse(s_d.get(0));
                LocalDate endDate = LocalDate.parse(e_d.get(0));
                // Create a list to store all the dates between start_date and finish_date
                List<String> allDates = new ArrayList<>();
                // Loop from start_date to finish_date and add each date to the list
                LocalDate currentDate = startDate;
                while (!currentDate.isAfter(endDate)) {
                    allDates.add(currentDate.toString());
                    currentDate = currentDate.plusDays(1);
                }
                int x = Integer.parseInt(db.select("bookings", "listing_id", "id", booking_id).get(0));
                for (String date: allDates){
                    db.deleteAvailability("availability", x , date);
                }
        
            }
            else{
                System.out.println("Booking cancelled");
            }
        }
        else{
            System.out.println("No availability for the selected dates");
            // Print all the available dates close by
            return;
        }

		
    }

    private void cancelBooking(boolean isRenter) throws SQLException {
        String booking_id;
       
        do {
            System.out.print("Enter booking number to cancel: ");
            booking_id = sc.nextLine().trim();
            if (!db.verifybooking(booking_id, this.id)){
                System.out.println("Invalid booking number");
            }
        } while (!db.verifybooking(booking_id, this.id));
        // Update Availability
            List<String> startdate = db.select("bookings", "start_date", "id", booking_id);
            List<String> enddate = db.select("bookings", "finish_date", "id", booking_id);
            LocalDate startDate = LocalDate.parse(startdate.get(0));
            LocalDate endDate = LocalDate.parse(enddate.get(0));
            // Create a list to store all the dates between start_date and finish_date
            List<String> allDates = new ArrayList<>();
            // Loop from start_date to finish_date and add each date to the list
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                allDates.add(currentDate.toString());
                currentDate = currentDate.plusDays(1);
            }
            // System.out.println(allDates);
        int x = Integer.parseInt(db.select("bookings", "listing_id", "id", booking_id).get(0));
        
        // Todo: instead deleting change the status
        // Delete Booking
        db.renterCancelBookingOne(booking_id);
		
        System.out.println("Your Booking was Cancelled !");
    }




    //_________________________ Ratings __________________________ \\

    private void rateRenters(String host_id) throws SQLException {
        String booking_id;
        String rating;
        String review;
        int check;
        System.out.println("this.id=" + this.id);
        do {
            System.out.print("Enter the Booking id with the Renter: ");
            booking_id = sc.nextLine().trim();
            if (this.id != db.verifybooking1(booking_id)){
                System.out.println("Invalid booking number");
            }
        } while (this.id != db.verifybooking1(booking_id));

        System.out.println("1. Comment On Renter");
        System.out.println("2. Rate Renter");
    
        check = sc.nextInt();
        if(check == 1 ) {
            commentRenter(booking_id);
        }
        else if(check == 2 ) {
            rateRenter(booking_id);
        }else{
            System.out.println("Invalid Input");
            return;
        }

        System.out.println("Your Rating was Submitted !");

    }

    private void rateBookings() throws SQLException {

        String booking_id;
        String rating;
        String review;
        int check;
        // Get Booking Id
        do {
            System.out.print("Enter booking number to rate: ");
            booking_id = sc.nextLine().trim();
            if (!db.verifybooking(booking_id, this.id)){
                System.out.println("Invalid booking number");
            }
        } while (!db.verifybooking(booking_id, this.id));

        System.out.println("1. Comment On Listing");
        System.out.println("2. Rate Listing");
        System.out.println("3. Comment On the Host");
        System.out.println("4. Rate Host");
    
        check = sc.nextInt();
        if(check == 1 ) {
            commentListing(booking_id);
        }
        else if(check == 2 ) {
            rateListing(booking_id);
        }
        else if(check == 3 ) {
            commentHost(booking_id);
        }
        else if(check == 4 ) {
            rateHost(booking_id);
        }else{
            System.out.println("Invalid Input");
            return;
        }

        System.out.println("Your Rating was Submitted !");

    }




//______________________ Helper Functions ______________________ \\


    private void commentListing(String booking_id) throws SQLException {
		String comment;
		System.out.println("Enter your comment for the listing");
		sc.nextLine();
		comment = sc.nextLine();
		
		db.updateBooking("renter_comment_listing", comment, booking_id);
    }
    private void rateListing(String booking_id) throws SQLException {
        int comment;
		do
		{
            try {
                System.out.println("Enter your Rating for the listing");
                comment = sc.nextInt();
                if (comment < 0 || comment > 5) {
                    System.out.println("Rating should be between 0-5");
                }
            } catch (Exception e) {
                System.out.println("Rating should be a number between 0-5");
                sc.nextLine(); // Consume the invalid input to avoid infinite loop
                comment = -1;
            }
		}while(comment < 0 || comment > 5);
		
        db.updateBooking("listing_rating", Integer.toString(comment), booking_id);
	}
    private void commentHost(String booking_id) throws SQLException {
        String comment;
		System.out.println("Enter your comment for the Host");
		sc.nextLine().trim();
		comment = sc.nextLine().trim();
		
        db.updateBooking("renter_comment_host", comment, booking_id);
	}
    private void rateHost(String booking_id) throws SQLException {
        int comment;
		do
		{
            try {
                System.out.println("Enter your Rating for the Host");
                comment = sc.nextInt();
                if (comment < 0 || comment > 5) {
                    System.out.println("Rating should be between 0-5");
                }
            } catch (Exception e) {
                System.out.println("Rating should be a number between 0-5");
                sc.nextLine(); // Consume the invalid input to avoid infinite loop
                comment = -1;
            }
		}while(comment < 0 || comment > 5);
		
        db.updateBooking("host_rating", Integer.toString(comment), booking_id);
		// db.update("booking", new String[] {"booking_num"}, new String[] {bookingNum}, new String[] {"renter_rating"} , new String[] {String.valueOf(comment)});
	}

    private void commentRenter(String booking_id) throws SQLException {
		String comment;
		System.out.println("Enter your comment for the Renter");
		sc.nextLine();
		comment = sc.nextLine();
		
		db.updateBooking("renter_rating", comment, booking_id);
    }

    private void rateRenter(String booking_id) throws SQLException {
        int comment;
		do
		{
            try {
                System.out.println("Enter your Rating for the Renter");
                comment = sc.nextInt();
                if (comment < 0 || comment > 5) {
                    System.out.println("Rating should be between 0-5");
                }
            } catch (Exception e) {
                System.out.println("Rating should be a number between 0-5");
                sc.nextLine(); // Consume the invalid input to avoid infinite loop
                comment = -1;
            }
		}while(comment < 0 || comment > 5);
		
        db.updateBooking("host_comment_renter", Integer.toString(comment), booking_id);
	}


    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }


    private void hostToolkitReport(){
        db.calculate_avg_price_per_city_and_type();
        db.calculate_amenities_per_city_and_type();
        return;
    }

    /* Verify Availability of listing */
    private boolean verifyavailability(String startdate, String enddate, String lid) {
        List<String> availabledates = db.getAvailableDates(Integer.parseInt(lid));
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        LocalDate startDate = LocalDate.parse(startdate, inputFormatter);
        LocalDate endDate = LocalDate.parse(enddate, inputFormatter);
        List<String> unavailableDates = new ArrayList<>();
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            String dateString = date.format(outputFormatter);
            if (!availabledates.contains(dateString)) {
                System.out.println("Date not available: " + date.format(inputFormatter));
                unavailableDates.add(date.format(inputFormatter));
            }
        }
        return unavailableDates.isEmpty();
    }

    /* Verify Listing id */
    private boolean verifylisting(String lid) throws SQLException {
        // Todo lid should be INT not string
        List<String> availableListings = db.select("availability", "listing_id", "listing_id", lid);
        boolean result = availableListings.size() > 0;
        
        if(result) {return true;}
        
        System.out.println("Unable to find listing or no available days for booking, Try again!");
        return false;
    }

    /* View profile Details */
    private void viewProfile(String email) throws InterruptedException{

        List<String> userDetails = db.getUser(email);
        Thread.sleep(2000);
        if (userDetails.get(8) == "true") { // Renter Dashboard
            List<String> ccdetails = db.getcc(Integer.valueOf(userDetails.get(0)));
            System.out.println("User details: " + userDetails);
            System.out.println("Payment details: " + ccdetails);
        } else { // Host dashboard
            	System.out.println("User details: " + userDetails);
        }
        Thread.sleep(2000);
    }

    /* View profile Details */
    private void viewProfileHost(String email) throws InterruptedException{

        List<String> userDetails = db.getUser(email);
        Thread.sleep(2000);
        if (userDetails.get(8) == "true") { // Host Dashboard
            System.out.println("User details: " + userDetails);
        } else { // Host dashboard
            	System.out.println("User details: " + userDetails);
        }
        Thread.sleep(2000);
    }

    /* Verify Login */
    private boolean verify_login(String email, String password) throws SQLException {
		List<String> vals = db.select("user", "password", "blocked = 0 AND email", email);
		boolean found = vals.size() == 1 && vals.get(0).equals(password);
		if (!found) { // Check if user exists
            System.out.println("\nInvalid username or password. Please try again.");
		}
		return found;
	}
    
    /* Checks if User Exists */
    private boolean checkuser(String table, String col, String value) throws SQLException{
        List<String> vals = db.select(table, col, col, value);
        if (vals.size() == 1) {
            System.out.println("\nAccount already exists");
            return true;
        } 
        return false;
    }

    private Date addDays(Date date, int days) {
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(java.util.Calendar.DAY_OF_YEAR, days);
        return (Date) calendar.getTime();
    }

    private void deleteHost(String host_id) throws SQLException{
        try{
            ResultSet rs1 = db.GetAllActiveListings(host_id);
            List<String> list_listing = new ArrayList<>();
            while (rs1.next()) {
                String listing_id = rs1.getString("id");
                list_listing.add(listing_id);
            }
            
            for ( String lID : list_listing){
                db.UnlistListing(lID);
            }
            db.setUserBlock(host_id);
        } catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
    }

    private void deleteRenter(String renter_id) throws SQLException{
        try{
            db.CancelAllRenterBookings(renter_id);
            db.setUserBlock(renter_id);
        } catch(Exception e){
			System.err.println( e.getClass().getName() + ": " + e.getMessage());
		}
    }

    private void reportTotalBookings(){
        // Get user input for city or zip
        String city = "";
        String zip = "";
        System.out.print("Enter City or press Enter to skip: ");
        String inputCity = sc.nextLine().trim();
        if (!inputCity.isEmpty()) {
            city = inputCity;
        } 
        System.out.print("Enter Zip or press Enter to skip: ");
        String inputZip = sc.nextLine().trim();
        if (!inputZip.isEmpty()) {
            zip = inputZip;
        }
    

        Boolean added_date = false;
        String startDateStr;
        String endDateStr;
            // assign dates
        do{
            System.out.print("Enter start date (yyyy-MM-dd): ");
            startDateStr = sc.nextLine().trim();
            System.out.print("Enter end date (yyyy-MM-dd): ");
            endDateStr = sc.nextLine().trim();
            try {
                if (!startDateStr.isEmpty() && !endDateStr.isEmpty()){
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate = LocalDate.parse(startDateStr, dateFormat);
                    LocalDate endDate = LocalDate.parse(endDateStr, dateFormat);
                    if (startDate.isBefore(endDate)) {
                        added_date = true;
                    } else {
                        System.out.println("Error: Start date must be before the end date.");
                    }
                }

            } catch (Exception e) {
                System.out.println("Error: Wrong Input." + e);;
            }

        } while(!added_date);

        db.TotalBookingsQuery(city, zip, startDateStr, endDateStr);
    }

    private void reportTotalListing(){
        db.TotalListing();
    }

    private void reportRankHosts(){
        db.RankHosts();
    }

    private void reportCommercialLiistings(){
        db.CommercialListings();
    }

    private void reportRankRenters(){
        System.out.print("Enter the year: ");
        int year = 0;
        while (true) {
            try {
                year = Integer.parseInt(sc.nextLine());
                if (year >= 1000 && year <= 9999) {
                    // The year is valid, break the loop
                    break;
                } else {
                    System.out.println("Invalid year. Please enter a four-digit year.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid four-digit year.");
            }
        }
        db.RankRenters(year);
    }

    private void reportHighesstCancelation(){
        System.out.print("Enter the year: ");
        int year = 0;
        while (true) {
            try {
                year = Integer.parseInt(sc.nextLine());
                if (year >= 1000 && year <= 9999) {
                    // The year is valid, break the loop
                    break;
                } else {
                    System.out.println("Invalid year. Please enter a four-digit year.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid four-digit year.");
            }
        }
        db.HighestCancelation(year);
    }
}
