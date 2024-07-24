//package com.revature.ams.Booking;
//
//import com.revature.ams.Booking.dtos.BookingRequestDTO;
//import com.revature.ams.Booking.dtos.BookingResponseDTO;
//import com.revature.ams.Flight.FlightService;
//import com.revature.ams.Member.MemberService;
//import com.revature.ams.util.interfaces.Controller;
//
///**
// * TODO: DOCUMENT ME
// */
//public class BookingController implements Controller {
//    private final BookingService bookingService;
//    private final MemberService memberService;
//    private final FlightService flightService;
//
//    public BookingController(BookingService bookingService, MemberService memberService, FlightService flightService) {
//        this.bookingService = bookingService;
//        this.memberService = memberService;
//        this.flightService = flightService;
//    }
//
//
//
//    /**
//     * TODO: DOCUMENT ME
//     * @param ctx
//     */
//    private void postBookFlight() {
//
//        Booking newBooking = new Booking(bookingRequestDTO);
//
//        newBooking.setFlight(flightService.findById(bookingRequestDTO.getFlightNumber()));
//        newBooking.setMember(memberService.findById(bookingRequestDTO.getMemberId()));
//
//        BookingResponseDTO bookingResponseDTO = bookingService.bookFlight(newBooking);
//
//    }
//
//    /**
//     * TODO: DOCUMENT ME
//     * @param
//     */
//    private void findAllBookings(){
//
//        if(memberType == null || !memberType.equals("ADMIN")) {
//            ctx.status(403);
//            ctx.result("You do not have sufficient permission to perform this action, as you are not logged in as an Admin");
//            return;
//        }
//
//        ctx.json(bookingService.findAll());
//    }
//
//    /**
//     * TODO: DOCUMENT ME
//     * @param ctx
//     */
//    private void getMembersBookings(Context ctx) {
//        int memberId = loggedInCheck(ctx);
//        if (memberId == -1) return;
//
//        ctx.json(bookingService.findAllBookingsByMemberId(memberId));
//    }
//
//    // TODO: Implement Me
//    private void deleteBooking(Context ctx) {
//
//    }
//
//    /**
//     * TODO: DOCUMENT ME
//     * @param ctx
//     * @return
//     */
//    private int loggedInCheck(Context ctx) {
//        String headerMemberId = ctx.header("memberId");
//        if (headerMemberId == null) {
//            ctx.status(400);
//            ctx.result("You are not logged in.");
//            return -1;
//        }
//        return Integer.parseInt(headerMemberId);
//    }
//
//}
