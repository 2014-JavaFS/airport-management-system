package com.revature.ams.Flight;

import com.revature.ams.util.ScannerValidator;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Predicate;

/**
 * Used to handle the content for a request by a user and return any pertinent information back. Initialization requires
 * a Scanner & MemberService to be injected in.
 */
public class FlightController {
    // Attributes

    public Scanner scanner;
    private final FlightService flightService;
    ScannerValidator anyInt = (scanner, errorMessage) -> {
        if (!scanner.hasNextInt()) {
            System.out.println(errorMessage);
            scanner.next();
            return false;
        }
        return true;
    };

    ScannerValidator anyShort = (scanner, errorMessage) -> {
        if (!scanner.hasNextShort()) {
            System.out.println(errorMessage);
            scanner.next();
            return false;
        }
        return true;
    };

    private Predicate<String> isNotEmpty = str -> str != null && !str.isBlank();

    // Constructors - Dependency Injection - any dependent objects are provided at initiliazation
    public FlightController(Scanner scanner, FlightService flightService) {
        this.scanner = scanner;
        this.flightService = flightService;
    }

    // Method
    public void getFlightInfo() {
        Flight[] flights = flightService.findAll();
        if(flights != null){
            for (int i = 0; i < flights.length; i++) {
                if (flights[i] != null) { // incase any random nulls in the array, we won't print out null
                    System.out.println(flights[i].toString());
                }
            }
        }
    }

    public void addFlight() {
        Flight flightToAdd;

        System.out.println("Please enter flight info, starting with Flight Number: ");
        if(!anyInt.isValid(scanner, "Invalid data type enter, please enter a number")) return;
        int flightNumber = scanner.nextInt();

        System.out.println("Enter origin airport three letter code (Ex. PHL): ");
        String originAirport = scanner.next();

        System.out.println("Enter destination airport three letter code (Ex. PHL): ");
        String destinationAirport = scanner.next();

        System.out.println("Enter number of seats: ");
        // Generally, when you cast a value you should only cast smaller to larger
        if(!anyShort.isValid(scanner, "Invalid data type enter, please enter a number between 0 & 32,767")) return;
        short seatCount = scanner.nextShort(); // taking the int and converting it's datatype to short

        flightToAdd = new Flight(flightNumber, originAirport, destinationAirport, seatCount);
        try{ // "risky" code execution
            flightService.create(flightToAdd);
        }  catch (InvalidInputException e){ // handles any reasonable exceptions
            e.printStackTrace();
            System.out.println(e.getMessage()); // TODO: REPLACED WITH A LOGGER
        } catch(RuntimeException e){
            e.printStackTrace();
            System.out.println("You've been caught by a random RuntimeException");
        }

    }
    // TODO: Implement a method to update flight information by ID
    public void updateFlightInformation(){
        Flight flightToUpdate;

        System.out.println("Please enter flight to update, starting with Flight Number: ");
        int flightNumber = scanner.nextInt();
        scanner.nextLine();

        flightToUpdate = flightService.findById(flightNumber);
        if(flightToUpdate == null) {
            System.out.println("Flight not found");
            return;
        }

        System.out.println("Enter time of departure date & time formatted as \"2024-07-03 15:48:00\" ");
        String timeDeparture = scanner.nextLine();
        if(!isNotEmpty.test(timeDeparture)){
            return;
        }

        System.out.println("Enter time of arrival date & time formatted as \"2024-07-03 15:48:00\" ");
        String timeArrival = scanner.nextLine();

        System.out.println("Enter pilot ID: ");
        if(!anyInt.isValid(scanner, "Invalid data type enter, please enter a number")) return;
        int pilot = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter airline ID: ");
        if(!anyInt.isValid(scanner, "Invalid data type enter, please enter a number")) return;
        int airline = scanner.nextInt();
        scanner.nextLine();

        try {
            System.out.printf("Flight update status: ", flightService.update(flightToUpdate, timeArrival, timeDeparture, pilot, airline));
        } catch (InvalidInputException | DateTimeParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch(RuntimeException e){
            e.printStackTrace();
            System.out.println("Random Runtime");
        }
    }



}
