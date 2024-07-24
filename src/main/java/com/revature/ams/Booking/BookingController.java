
package com.revature.ams.Booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.Flight.FlightService;
import com.revature.ams.Member.MemberService;



/**
 * BookingController is the control layer of the booking functional group. It must be injected with bookingService,
 * memberService, and flightService dependencies at instantiation [all three are declared private final].
 * The class implements the ams.util.interfaces.Controller interface.
 */
@RestController
@RequestMapping("/booking")
public class BookingController {
    private final BookingService bookingService;
    private final MemberService memberService;
    private final FlightService flightService;

    @Autowired
    public BookingController(BookingService bookingService, MemberService memberService, FlightService flightService) {
        this.bookingService = bookingService;
        this.memberService = memberService;
        this.flightService = flightService;
    }



    /**
     * postBookFlight accepts a Context object before utilizing context.bodyAsClass to parse a
     * BookingRequestDTO object before creating a new Booking object by passing the bookingRequestDTO object.
     * The booking object is then passed to bookingService.bookFlight to create a bookingResponseDTO object.
     *
     * If the method is successful it returns a 201 (Created) status as well as a json of the bookingResponseDTO object.
     */
    @PostMapping("/bookings/create")
    public ResponseEntity<BookingResponseDTO> createBook(@RequestBody Booking book) {
        return ResponseEntity.status(201).body(bookingService.bookFlight(book));
    }

    /**
     * findAllBookings accepts a Context object before parsing the header to get the memberType attribute to
     * verify that the memberType is set to ADMIN. If it is null or not ADMIN a 403 Forbidden HTTP status is returned,
     * along with a status message.
     * If the memberType is ADMIN it responds with a call to bookingService.findAll via ctx.json.
     */
    @GetMapping("/bookings/findAll")
    private ResponseEntity<List<Booking>> findAllBookings(){
        return ResponseEntity.status(200).body(bookingService.findAll());
    }

    /**
     * getMemberBookings accepts a Context object as a parameter and verifies that the member is logged in.
     * If a member is logged in it will then call bookingService.findAllBookingsByMemberId to return the flights
     * with that specific memberId via a json response.
     */
    @GetMapping("/bookings/{memberId}")
    private ResponseEntity<List<BookingResponseDTO>> getMembersBookings(@PathVariable int memberId) {
        return ResponseEntity.status(200).body(bookingService.findAllBookingsByMemberId(memberId));
    }

    // TODO: Implement Me
    private void deleteBooking() {
        
    }

    /**
     * loggedInCheck accepts a Context object as a parameter and verifies that a member is logged in through its
     * header. If the memberId is null it responds with a 400 (bad request) status code and a response. Otherwise
     * it returns the memberId via Integer.parseInt.
     * @return member's memberId as it exists in the context header
     */
    private int loggedInCheck() {

            return -1;

    }

}
