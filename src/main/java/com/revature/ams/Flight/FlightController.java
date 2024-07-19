package com.revature.ams.Flight;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.exceptions.InvalidInputException;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Predicate;

import static com.revature.ams.AirportFrontController.logger;

public class FlightController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(FlightController.class);
    // Attributes

    private final FlightService flightService;

    private Predicate<String> isNotEmpty = str -> str != null && !str.isBlank();

    // Constructors - Dependency Injection - any dependent objects are provided at initiliazation
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    // One of the principles of REST is uniform interface, simply means that the PATH in your URL makes sense given the context
    // of what that path is handling

    @Override
    public void registerPaths(Javalin app) {
        app.get("/flights", this::getAllFlights);
        app.post("/flights", this::postNewFlight);
        app.get("/flights/{flightNumber}", this::getFlightById); //Path Parameter
        app.put("flights", this::putUpdateFlight);
    }


    public void getAllFlights(Context ctx){
        logger.info("Accessing all flights");
        List<Flight> flights = flightService.findAll();
        logger.info("Flights found, converting to json");
        ctx.json(flights);
        logger.info("Sending back to user.");
    }

    public void postNewFlight(Context ctx){
        String memberType = ctx.header("memberType");
        if(memberType == null || !memberType.equals("ADMIN")){
            ctx.status(403);
            ctx.result("You do not have sufficient permission to perform this action, as you are not logged in as an Admin");
            return;
        }
        Flight flight = ctx.bodyAsClass(Flight.class); // request body & mapping from JSON to Java Object

        ctx.json(flightService.create(flight)); // Respond with the created flight
        ctx.status(HttpStatus.CREATED); // Responded with a successfull status code
    }

    private void getFlightById(Context ctx) {
        int flightNumber = Integer.parseInt(ctx.pathParam("flightNumber"));
        logger.info("Flight number {}, {}", flightNumber, "was sent in through the path parameter");
        try {
            Flight foundFlight = flightService.findById(flightNumber);
            logger.info("This information should be returned to the user: {}", foundFlight);
            ctx.json(foundFlight);
        } catch (DataNotFoundException e){
            logger.warn("Data was not found and thus responded with a 404");
            ctx.status(HttpStatus.NOT_FOUND);
        } catch (RuntimeException e){
            logger.warn("Something else is amiss");
            e.printStackTrace();
            ctx.status(500);
        }


    }


    private void putUpdateFlight(Context ctx) {
        Flight updatedFlight = ctx.bodyAsClass(Flight.class);

        try {
            if (flightService.update(updatedFlight)) {
                ctx.status(HttpStatus.ACCEPTED);
            } else {
                ctx.status(HttpStatus.BAD_REQUEST);
            }
        } catch (InvalidInputException e){
            e.printStackTrace();
        }
    }
}
