package com.revature.ams;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.ams.Booking.BookingController;
import com.revature.ams.Booking.BookingRepository;
import com.revature.ams.Booking.BookingService;
import com.revature.ams.Flight.FlightController;
import com.revature.ams.Flight.FlightRepository;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.Member;
import com.revature.ams.Member.MemberController;
import com.revature.ams.Member.MemberRepository;
import com.revature.ams.Member.MemberService;
import com.revature.ams.util.auth.AuthController;
import com.revature.ams.util.auth.AuthService;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// FrontController Example
public class AirportFrontController {

    public static final Logger logger = LoggerFactory.getLogger(AirportFrontController.class);

    public static void main(String[] args) {
        logger.info("Airport Management System is up and running.....");

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson());
        });

        FlightRepository flightRepository = new FlightRepository();
        FlightService flightService = new FlightService(flightRepository);
        FlightController flightController = new FlightController(flightService); // Instiating a FlightController Object
        flightController.registerPaths(app);

        Member memberLoggedIn = null; // Storing the Users Session
        MemberRepository memberRepository = new MemberRepository();
        MemberService memberService = new MemberService(memberRepository);
        MemberController memberController = new MemberController(memberService);
        memberController.registerPaths(app);

        AuthService authService = new AuthService(memberService);
        AuthController authController = new AuthController(authService);
        authController.registerPaths(app);

        BookingRepository bookingRepository = new BookingRepository();
        BookingService bookingService = new BookingService(bookingRepository);
        BookingController bookingController = new BookingController(bookingService, memberService, flightService);
        bookingController.registerPaths(app);

        app.start(8080);
    }

}
