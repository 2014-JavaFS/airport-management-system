package com.revature.ams.Flight;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.exceptions.InvalidInputException;
import com.revature.ams.util.interfaces.Serviceable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;

import static com.revature.ams.AirportFrontController.logger;


public class FlightService implements Serviceable<Flight> {
    // -> lamba: format () -> {}, defining any parameteres used by the function and it's execution. Parenthesis not necessary for oen parameter
    private Predicate<String> isNotEmpty = str -> str != null && !str.isBlank();
    private FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository){
        this.flightRepository = flightRepository;
    }

    @Override
    public List<Flight> findAll(){
        List<Flight> flights = flightRepository.findAll();
        if (flights.isEmpty()) {
            throw new DataNotFoundException("No flight information available");
        } else {
            return flights;
        }
    }

    @Override
    public Flight create(Flight flight) throws InvalidInputException {
        validateMinFlight(flight);
        return flightRepository.create(flight);
    }

    @Override
    public Flight findById(int flightNumber) throws DataNotFoundException{
        logger.info("Flight number was sent to service as {}", flightNumber);
        Flight foundFlight = flightRepository.findById(flightNumber);
        logger.info("The flight was found and is {}", foundFlight);
        return foundFlight;
    }

    public boolean update(Flight flightToUpdate) throws InvalidInputException {
        validateFullFlight(flightToUpdate);
        Flight foundFlight = flightRepository.findById(flightToUpdate.getFlightNumber());
        if(foundFlight == null){
            throw new DataNotFoundException("Flight with that ID not in database, please check again");
        }
        return flightRepository.update(flightToUpdate);
    }


    public boolean delete(Flight flight){
        validateFullFlight(flight);
        return flightRepository.delete(flight);
    }

    public boolean isEmpty(Flight[] arr) { // defining the parameter of a string array to be included when executing this mehtod
        for (Flight element : arr) { // enhanced for each loop, that iterates through the arrays elements returning & assigning the value to the declared variable flight
            if (element != null) {
                return false;
            }
        }
        return true;
    }

    public int getFirstNull(Flight[] arr) { //get the first null index to replace, otherwise return -1
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == null) {
                return i;
            }
        }
        return -1;
    }

    // ducking the exception
    public void validateMinFlight(Flight flight) throws InvalidInputException {
        if (flight == null) {
            throw new InvalidInputException("Flight is null as it has not been instantiated in memory");
        }

        // potentially bad errors with using public attributes, both reassigning the value & reading with the same expression
        // we've reassigned the value
        //flight.flightNumber = 55555;
        //flight.setFlightNumber(55555);
        // Now assigned a new variable by 'getting' the flight number using dot (.) syntax
        //int flightTempNumber = flight.flightNumber;
        int flightTempNumber = flight.getFlightNumber();

        // alt + shift : multiple cursors
        if (flight.getFlightNumber() < 1000 || flight.getFlightNumber() > 999999) {
            throw new InvalidInputException("Flight number needs to be a minimum of 4 digits to 6 digits long");
        }

        if (!isNotEmpty.test(flight.getOriginAirport()) || !isNotEmpty.test(flight.getDestinationAirport())
                || flight.getOriginAirport().length() != 3 || flight.getDestinationAirport().length() != 3) {
            throw new InvalidInputException("Values empty or not exactly 3 characters in length i.e. PHL");
        }

        if (flight.getSeatCount() < 0) {
            throw new InvalidInputException("Seatcount cannot be less than 0");
        }

    }

    public void validateFullFlight(Flight flight) throws InvalidInputException {
        validateMinFlight(flight);

        OffsetDateTime currentDate = OffsetDateTime.now();
        if (flight.getTimeDeparture().isBefore(currentDate)) {
            throw new InvalidInputException("Time of departure must be before the current date");
        }

        if (flight.getTimeArrival().isBefore(flight.getTimeDeparture())) {
            throw new InvalidInputException("Time arrival is before time of departure");
        }

        if (flight.getPilot() < 100000 || flight.getPilot() > 999999) {
            throw new InvalidInputException("Pilot IDs are 6 digits long");
        }

        if (flight.getAirline() < 1000 || flight.getAirline() > 9999) {
            throw new InvalidInputException("Airlines are 4 digits long");
        }
    }

}
