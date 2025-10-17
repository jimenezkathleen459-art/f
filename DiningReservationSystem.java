import java.util.ArrayList; //for using dynamic lists/storing reservations
import java.util.Scanner;   //for reading user input
import java.time.LocalDate; //for getting the current date
import java.time.LocalTime; //for getting the current time
import java.time.format.DateTimeFormatter; //for formatting date and time display

public class DiningReservationSystem { //main class of the program

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); //create scanner object to read user input

        //create a list to store all active/current reservations
        ArrayList<ArrayList<String>> reservations = new ArrayList<>();

        //create a list to store all reservations, including deleted ones
        ArrayList<ArrayList<String>> allReservations = new ArrayList<>();

        //start the reservation number count at 1
        int reservationCount = 1;

        //loop that keeps the program running until user chooses to exit
        while (true) {
            //display menu options
            System.out.println("\n=== RESTAURANT DINING RESERVATION SYSTEM ===");
            System.out.println("k. View All Reservations");
            System.out.println("l. Make A Reservation");
            System.out.println("m. Delete A Reservation");
            System.out.println("n. Generate Reservation Report");
            System.out.println("o. Exit");
            System.out.print("> ");

            //read user's menu choice, convert it to lowercase to avoid case issues
            String choice = sc.nextLine().trim().toLowerCase();

            //switch statement to handle each menu option
            switch (choice) {
                case "k" -> viewReservations(reservations); //view all current reservations
                case "l" -> reservationCount = makeReservation(sc, reservations, allReservations, reservationCount); //add new reservation
                case "m" -> deleteReservation(sc, reservations); //delete existing reservation
                case "n" -> generateReport(allReservations); //generate report for all reservations
                case "o" -> { //exit program
                    System.out.println("\nThank you!");
                    sc.close(); //close scanner
                    return; //exit the program
                }
                default -> System.out.println("\nInvalid choice. Please try again.\n"); //if input is not valid
            }
        }
    }

    //method that display active reservations
    static void viewReservations(ArrayList<ArrayList<String>> reservations) {
        //if there are no active reservations
        if (reservations.isEmpty()) {
            System.out.println("\nNo active reservations found.\n");
            return; //stop method
        }

        //print the table header with aligned columns
        System.out.printf("\n%-5s %-15s %-10s %-20s %-8s %-10s%n",
                "#", "Date", "Time", "Name", "Adults", "Children");

        //loop through all reservations and print their details
        for (ArrayList<String> r : reservations) {
            System.out.printf("%-5s %-15s %-10s %-20s %-8s %-10s%n",
                    r.get(0), r.get(1), r.get(2), r.get(3), r.get(4), r.get(5));
        }
        System.out.println(); //blank line for spacing
    }

    //method that create a new reservation
    static int makeReservation(Scanner sc, ArrayList<ArrayList<String>> reservations,
                               ArrayList<ArrayList<String>> allReservations, int reservationCount) {
        System.out.println("\n=== MAKE A RESERVATION ===");

        //ask for user's name
        String name = getInfo(sc, "Enter Name: ");

        //ask for number of adults (must be at least 1)
        String adults = getPositiveInt(sc, "Enter number of adults (min 1): ", 1);

        //ask for number of children (can be 0 or more)
        String children = getPositiveInt(sc, "Enter number of children (min 0): ", 0);

        //get the current date and time
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();

        //format date and time ex. Oct 17, 2025, 08:45 PM
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        DateTimeFormatter tf = DateTimeFormatter.ofPattern("hh:mm a");

        //create a list to store reservation info
        ArrayList<String> newRes = new ArrayList<>();
        newRes.add(String.valueOf(reservationCount++)); //add reservation number
        newRes.add(date.format(df));                    //add formatted date
        newRes.add(time.format(tf));                    //add formatted time
        newRes.add(name);                               //add customer's name
        newRes.add(adults);                             //add adult count
        newRes.add(children);                           //add children count

        //add reservation to the active list
        reservations.add(newRes);

        //also add to the full list (for report purposes)
        allReservations.add(new ArrayList<>(newRes));

        System.out.println("\nReservation successfully added!\n");
        return reservationCount; //return updated count to main
    }

    //method that delete a reservation
    static void deleteReservation(Scanner sc, ArrayList<ArrayList<String>> reservations) {
        //if there are no reservations
        if (reservations.isEmpty()) {
            System.out.println("\nNo reservations to delete.\n");
            return;
        }

        //show all current active reservations
        viewReservations(reservations);

        //ask for reservation number to delete
        String num = getInfo(sc, "Enter reservation number to delete: ");

        boolean found = false; //check if reservation exists

        //loop through active reservations to find and remove
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).get(0).equals(num)) { //compare reservation number
                reservations.remove(i); //remove it from active list
                found = true;
                break;
            }
        }

     

        //print result message
        if (found)
            System.out.println("\nReservation " + num + " deleted.\n");
        else
            System.out.println("\nReservation not found.\n");
    }

    // method that generate reservation report
    static void generateReport(ArrayList<ArrayList<String>> allReservations) {
        //if there are no records
        if (allReservations.isEmpty()) {
            System.out.println("\nNo reservations to report.\n");
            return;
        }

        //print header of the report
        System.out.println("\n=================== RESERVATION REPORT ===================");
        System.out.printf("%-5s %-15s %-10s %-20s %-8s %-10s %-10s%n",
                "#", "Date", "Time", "Name", "Adults", "Children", "Subtotal");

        int totalAdults = 0;   //counter for total adults
        int totalChildren = 0; //counter for total children
        int grandTotal = 0;    //sum of all subtotals

        //loop through all reservations including deleted
        for (ArrayList<String> r : allReservations) {
            int adults = Integer.parseInt(r.get(4));   //convert adult count to int
            int children = Integer.parseInt(r.get(5)); //convert child count to int

            //compute total cost per reservation
            int subtotal = (adults * 500) + (children * 300);

            //update running totals
            totalAdults += adults;
            totalChildren += children;
            grandTotal += subtotal;

            //print each reservation record with subtotal
            System.out.printf("%-5s %-15s %-10s %-20s %-8s %-10s %-10d%n",
                    r.get(0), r.get(1), r.get(2), r.get(3),
                    r.get(4), r.get(5), subtotal);
        }

        //print total summary
        System.out.println("----------------------------------------------------------");
        System.out.println("Total Adults: " + totalAdults);
        System.out.println("Total Children: " + totalChildren);
        System.out.println("Grand Total: PHP " + grandTotal);
        System.out.println("==========================================================\n");
        return;
    }

    //method to get valid string input
    static String getInfo(Scanner sc, String prompt) {
        while (true) { //repeat until valid input
            System.out.print(prompt);
            String input = sc.nextLine().trim(); //read user input and remove spaces
            if (input.isEmpty()) { //if blank
                System.out.println("Please enter a valid input.\n");
            } else {
                return input; //return valid input
            }
        }
    }

    //help to get valid int input
    static String getPositiveInt(Scanner sc, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim(); //read user input

            try {
                int num = Integer.parseInt(input); //try converting to integer
                if (num >= min) //check if it meets minimum value
                    return String.valueOf(num); //return valid number as string
                else
                    System.out.println("Must be at least " + min + ".\n");
            } catch (NumberFormatException e) { //if not a number
                System.out.println("Invalid input. Please enter a number.\n");
            }
        }
    }
}
