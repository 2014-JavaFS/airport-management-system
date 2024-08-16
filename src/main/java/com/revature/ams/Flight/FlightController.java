package com.revature.ams.Flight;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:5173", "http://ec2-3-141-3-182.us-east-2.compute.amazonaws.com:5005/"})
@RestController
@RequestMapping("/flights")
public class FlightController {
    private final FlightService flightService;

    @Autowired // Reminder this is optional IF it's your only form of DI
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public @ResponseBody List<Flight> getAllFlights(){
        return flightService.findAll();
    }

    @PostMapping
    public ResponseEntity<Flight> postNewFlight(@RequestBody Flight flight, @RequestHeader String memberType){
        if(!memberType.equals("ADMIN")) throw new UnauthorizedException("You are not logged in as an admin!");
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.create(flight));
    }

    @GetMapping("/{id}")
    private ResponseEntity<Flight> getFlightById(@PathVariable int id) {
//        return ResponseEntity
//                .status(HttpStatus.I_AM_A_TEAPOT)
//                .body(flightService.findById(id));
        return ResponseEntity.ok(flightService.findById(id));
    }


    @PutMapping
    private ResponseEntity<Boolean> putUpdateFlight(@RequestBody Flight updatedFlight) {
        return ResponseEntity.ok(flightService.update(updatedFlight));
    }



}
