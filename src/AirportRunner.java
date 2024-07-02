import java.util.Scanner;

public class AirportRunner {
    public static void main(String[] args) {
        System.out.println("Airport Management System is up and running.....");
        // PLAN OUT OUR AIRPORT
        // 1. View Flights
        //      - No Flights: Message informing the users no flights are available
        //      - Flights: List of the flights
        // 2. Flight Records
        //      - Airline:Destination - show a simple view of flights
        //      - Add flights to our records

        // Design our Application
        /*
        1. What's the first thing a user should be introduced too?
            - Welcome message & list of options available that they can select from
            - Java: switch case, Scanner, loop, print statements, validation users input
        2. Ability to view flights, if there are any
            - View available flights in our records
            - Java: arrays or collection, validation through boolean checks, loop of print statements through our array
             (print an array it's just a memory location), check for no flights
        3. Flight records
            - Be able to add records to our flights
            - Java: Scanner to take user input, store user input in an array, validate empty input
         */

        // Variables that need to be at the top we need to declare earlier
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

       // Design Step 1
        // declaring to Java what datatype the scanner variable is
        do {

            System.out.println("Welcome to our Airport!");
            System.out.println("1. View Flights"); // ctrl + d on a line will repeat
            System.out.println("2. Add Flights");
            System.out.println("3. Exit"); // sout - shorthand for System.out.println();
            System.out.println();
            System.out.println("Enter your numeric choice from above: ");

            // Validate user's input is indeed an int
            // ! indicates if it's falses then it's condition has been met
            if(!scanner.hasNextInt()){
                System.out.println("Invalid Input, Please enter a number 1-3.");
                // because they entered not a number, we need to put the scanner to the next
                scanner.nextLine();
                continue; // jump back to the beginning of the do-while loop
            }

            choice = scanner.nextInt(); // can only reassign value, not datatype

//            if( choice > 3){
//                System.out.println("Invalid Input, Please enter a number 1-3.");
//                // because they entered not a number, we need to put the scanner to the next
//                scanner.nextLine();// jump back to the beginning of the do-while loop
//            }

            switch (choice) {
                case 1: // If choice == 1
                    System.out.println("Viewing flight information....");
                    break; //include break, otherwise it will fall through to the next case statement
                case 2:
                    System.out.println("Adding a flight, please enter information Airline:Destination: ");
                    break;
                case 3:
                    System.out.println("Thanks for using our airpoirt services, have a wonderful day!");
                    break;
                default:
                    System.out.println("Invalid Input, Please enter a number 1-3.");

            }
           //System.out.printf("Your choice was option %s", choice); // print formater, where %s is replaced with the variable
        } while (choice != 3);
    }
}
