package Functions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Collections;
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
            user = new Renter(userDetails.get(1), userDetails.get(2), userDetails.get(3), userDetails.get(4), userDetails.get(5), userDetails.get(6), userDetails.get(7), ccdetails.get(0), ccdetails.get(1), ccdetails.get(2), ccdetails.get(3));
            System.out.println("\nWelcome " + user.getName());
            renterDashboard(email);
        } else { // Host dashboard
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
                            // Add Listing;
                            HDashAddListing(host_id);
                            hostDashboard(email);
                            break;
                        case 3:
                            cancelBooking(false);
                            hostDashboard(email);
                            break;
                        case 4:
                            // searchListings();
                            hostDashboard(email);
                            break;
                        case 5:
                            // rateBookings();
                            hostDashboard(email);
                            break;
                        case 6:
                            viewProfile(email);
                            hostDashboard(email);
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
    
    private boolean renterDashboard(String email) throws SQLException, InterruptedException, ParseException{
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
                            makeBooking();
                            renterDashboard(email);
                            break;
                        case 3:
                            cancelBooking(true);
                            renterDashboard(email);
                            break;
                        case 4:
                            // searchListings();
                            renterDashboard(email);
                            break;
                        case 5:
                            // rateBookings();
                            renterDashboard(email);
                            break;
                        case 6:
                            viewProfile(email);
                            renterDashboard(email);
                            break;
                        case 7:
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
                answerContinue = sc.nextLine().trim().toLowerCase();
                System.out.println(answerContinue);
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





//_________________________ Bookings _________________________ \\
    // Todo: Update availability after adding a booking
    private void makeBooking() throws SQLException, ParseException {
        String lid;
		do {
			System.out.print("Enter listing number to book: ");
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
            
            // Calculate Total Price: timePeriod * price
            lid_price = Double.parseDouble(db.select("listing", "pricing", "id", lid).get(0));
            Double total = timePeriod * lid_price;
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
                db.bookListings(Integer.parseInt(lid), this.id, fstartdate, fenddate, total, "normal");                
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

        // Delete Booking
        // Update Availability


        System.out.println("Your Booking was Cancelled !");

    }


    private void rateBookings() {

    }



//______________________ Helper Functions ______________________ \\


    



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
        
        System.out.println("Unable to find listing, Try again!");
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

    /* Verify Login */
    private boolean verify_login(String email, String password) throws SQLException {
		List<String> vals = db.select("user", "password", "email", email);
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
}
