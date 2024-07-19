package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingRequestDTO;
import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.MemberService;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Handles incoming requests & responses from the client. Converts data to the Booking Model to be delivered to the BookingService.
 */
public class BookingController implements Controller {
    private final BookingService bookingService;
    private final MemberService memberService;
    private final FlightService flightService;

    public BookingController(BookingService bookingService, MemberService memberService, FlightService flightService) {
        this.bookingService = bookingService;
        this.memberService = memberService;
        this.flightService = flightService;
    }

    /**
     *  Takes in a Javalin application to which we will attach specific request handlers along with their url path
     * @param app the Javalin application to which we will register request handlers to.
     */
    @Override
    public void registerPaths(Javalin app) {
        app.post("/bookings", this::postBookFlight);
        app.get("/bookings", this::findAllBookings);
        app.get("/bookings/my", this::getMembersBookings);
        app.delete("/bookings/{booking_id}", this::deleteBooking);
    }

    /**
     * The request handler for creating a new booking. Takes in the information for a new booking and sends the information to the BookingService. Returns a response with the created booking if successfully created.
     * @param ctx contains the information of the booking that will be created in the database.
     */
    private void postBookFlight(Context ctx) {
        BookingRequestDTO bookingRequestDTO = ctx.bodyAsClass(BookingRequestDTO.class);
        Booking newBooking = new Booking(bookingRequestDTO);

        newBooking.setFlight(flightService.findById(bookingRequestDTO.getFlightNumber()));
        newBooking.setMember(memberService.findById(bookingRequestDTO.getMemberId()));

        BookingResponseDTO bookingResponseDTO = bookingService.bookFlight(newBooking);
        ctx.status(201);
        ctx.json(bookingResponseDTO);
    }

    /**
     * The request handler for finding all bookings in the system. Sends a response containing all bookings, given the user has the appropriate member type.
     * @param ctx contains the memberType of the user attempting to access all bookings.
     */
    private void findAllBookings(Context ctx){
        String memberType = ctx.header("memberType");
        if(memberType == null || !memberType.equals("ADMIN")) {
            ctx.status(403);
            ctx.result("You do not have sufficient permission to perform this action, as you are not logged in as an Admin");
            return;
        }

        ctx.json(bookingService.findAll());
    }

    /**
     * The request handler for finding all bookings a member has. Sends a response containing all the user's bookings, given they are logged in and have bookings.
     * @param ctx
     */
    private void getMembersBookings(Context ctx) {
        int memberId = loggedInCheck(ctx);
        if (memberId == -1) return;

        ctx.json(bookingService.findAllBookingsByMemberId(memberId));
    }

    // TODO: Implement Me
    private void deleteBooking(Context ctx) {

    }

    /**
     * Method that checks if there is a member logged in.
     * @param ctx contains the memberId of the member logged in.
     * @return returns the memberId of the member logged in, or -1 if there are no members logged in.
     */
    private int loggedInCheck(Context ctx) {
        String headerMemberId = ctx.header("memberId");
        if (headerMemberId == null) {
            ctx.status(400);
            ctx.result("You are not logged in.");
            return -1;
        }
        return Integer.parseInt(headerMemberId);
    }

}
