package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingRequestDTO;
import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.MemberService;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: DOCUMENT ME
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
     * TODO: DOCUMENT ME
     *
     * @param app
     */
    @Override
    public void registerPaths(Javalin app) {
        app.post("/bookings", this::postBookFlight);
        app.get("/bookings", this::findAllBookings);
        app.get("/bookings/my", this::getMembersBookings);
        app.delete("/bookings/{booking_id}", this::deleteBooking);
    }

    /**
     * TODO: DOCUMENT ME
     * @param ctx
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
     * If the member is an admin and memberType is not null, retrieve all bookings.
     * @param ctx The context from HTTP request. Used to get the member type.
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
     * If member is logged in, will retrieve all the bookings for the member.
     * @param ctx The context from HTTP request. Used to get the member id.
     */
    private void getMembersBookings(Context ctx) {
        int memberId = loggedInCheck(ctx);
        if (memberId == -1) return;

        ctx.json(bookingService.findAllBookingsByMemberId(memberId));
    }

    /**
     * If member is logged in, will delete the booking specified in the ctx.
     * @param ctx The context from HTTP request. Used to get the member id and booking id.
     */
    private void deleteBooking(Context ctx) {

    }

    /**
     * Checks if the member id is null.
     * If not null then the member is logged in and returns their member id.
     * @param ctx The context from HTTP request. Used to get the member id.
     * @return memberId if member is logged in. -1 if member is not logged in.
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
