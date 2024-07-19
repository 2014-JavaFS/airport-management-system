package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingRequestDTO;
import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.MemberService;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * Controller layer for the Booking model which utilizes BookingService, MemberService, and FlightService objects to
 * implement methods used in the endpoints.
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
     * Creates connections to the specified endpoints so that Postman can execute queries to the database through them.
     * GETs: findALlBookings, getMembersBookings
     * POST: postBookgFlight
     * DELETE: deleteBooking
     *
     * @param app - Javalin instance that sets up the endpoints and connects them with the corresponding method.
     */
    @Override
    public void registerPaths(Javalin app) {
        app.post("/bookings", this::postBookFlight);
        app.get("/bookings", this::findAllBookings);
        app.get("/bookings/my", this::getMembersBookings);
        app.delete("/bookings/{booking_id}", this::deleteBooking);
    }

    /**
     * Receives the Postman request body as a json object to create a new Booking object which is given a Flight and
     * a Member object and then is passed into bookingerService.
     * @param ctx - Context instance that retrieves the request's body as a BookingRequestDTO object and sends back a
     *            BookingResponseDTO object.
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
     * TODO: DOCUMENT ME
     * @param ctx
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
     * TODO: DOCUMENT ME
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
     * TODO: DOCUMENT ME
     * @param ctx
     * @return
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
