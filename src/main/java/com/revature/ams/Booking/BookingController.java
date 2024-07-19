package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingRequestDTO;
import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.MemberService;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * <h5>The BookingController class handles HTTP requests related to flight bookings.</h5>
 * <p>
 * This controller provides endpoints for booking a flight, retrieving bookings,
 * retrieving member-specific bookings, and deleting bookings.
 * </p>
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
     *
     * <h5>Registers the endpoints and handlers of the Booking controller.</h5>
     *
     * @param app Instance of Javalin where the paths will be registered to.
     *
     *
     */
    @Override
    public void registerPaths(Javalin app) {
        app.post("/bookings", this::postBookFlight);
        app.get("/bookings", this::findAllBookings);
        app.get("/bookings/my", this::getMembersBookings);
        app.delete("/bookings/{booking_id}", this::deleteBooking);
    }

    /**
     * <h5>Handles the HTTP POST request to book a flight</h5>
     * <p>
     *     This method processes the booking request by converting the request body
     *     into a BookingRequestDTO object. It also creates a new Booking object with that BookingRequestDTO.
     *     It sets the flight and members, then uses the bookingService to book the flight.
     *     Finally it responds with the booking details in the response body.
     * </p>
     * @param ctx the Javalin Context object containing the request and response objects.
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
     * <h5>Handles the HTTP GET request to retrieve all bookings</h5>
     *
     * <p>
     *     Retrieves the Member type from the request headers.
     *     Checks to see if the Member is an Admin.
     *     If the member is not an admin it will not Authorize the use of this endpoint.
     *     If the member is an admin it will return a list of all bookings.
     * </p>
     * @param ctx the Javalin Context object containing the request and response objects.
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
     * <h5>Handles the HTTP GET request to retrieve all bookings of a specific member by ID.</h5>
     *
     * <p>
     *     Grabs the memberId from the request headers.
     *     If the memberId is null the user is not logged in
     *     and the method exits.
     *     If the user is logged in the method uses the booking service
     *     to retrieve all the members bookings.
     * </p>
     * @param ctx the Javalin Context object containing the request and response objects.
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
     * <h5>This method checks to see if a member is logged in.</h5>
     * <p>
     *     The memberId is retrieved from the request headers.
     *     If memberId is null the user is not logged in and
     *     the method will set the response status to 400 and
     *     let the user know they are not logged in.
     *     If the memberId is available the method will return
     *     the id.
     * </p>
     * @param ctx the Javalin Context object containing the request and response objects.
     * @return Integer representing the memberId, is null if memberId was not found.
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
