package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing flight bookings.
 *  * This class contains business logic for creating bookings,
 *  * calculating prices, and retrieving booking information.
 */
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Method: below creates new flight booking and setting Carry On Allowance.
     * Input: newBooking - details of the new booking
     * process: - calculate total price of booking
     *          - set carry-on allowed if seat type is Business or Firstclass
     *          - create the booking in the repo.
     * output: the booking response DTO with the booking information.
     * @Throws InvalidInputException if the booking could not be created.
     */
    public BookingResponseDTO bookFlight(Booking newBooking) {
        newBooking.setPrice(calculateTotalPrice(newBooking));
        if(newBooking.getSeatType().name().equals("BUSINESS") || newBooking.getSeatType().name().equals("FIRSTCLASS")){
            newBooking.setCarryOnAllowed(true);
        }

        Optional<Booking> booking = bookingRepository.create(newBooking);
        booking.orElseThrow(() -> new InvalidInputException("Double-Check "));

        return booking.map(BookingResponseDTO::new).get();
    }

    /**
     * method: Retrieves all bookings from the repository.
     *output: List<Booking> - a lsit of all bookings
     * @return a list of all bookings
     */
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * method: Find all bookings by member ID
     * Input: memberId - hte Id of the member
     * Output: List<BookingRsponseDTO> - a list of booking response DTOs for the member
     * @param memberId
     * @return
     */
    public List<BookingResponseDTO> findAllBookingsByMemberId(int memberId){
       return bookingRepository.findAllBookingsByMemberId(memberId)
               .stream()
               .map(BookingResponseDTO::new)
               .toList();
    }

    /**
     * Method: Calculate total price of booking
     * Input: booking- the booking details
     * Output: BigDecimal - the total price of the booking
     * @param booking
     * @return
     */
    public BigDecimal calculateTotalPrice(Booking booking) {
        BigDecimal seatPrice = calculateSeatPrice(booking.getSeatType());
        BigDecimal luggagePrice = calculateLuggagePrice(booking.getCheckedLuggage());
        return new BigDecimal("299.99").add(seatPrice).add(luggagePrice);
    }

    /**
     * Method: Calculate the price of a seat
     * Input: seatType- the type of a seat
     * output: BigDecimal - the price of seat
     * @param seatType
     * @return
     */
    private BigDecimal calculateSeatPrice(Booking.SeatType seatType) {
        return switch (seatType) {
            case SEATSOPTIONAL -> new BigDecimal("50.00");
            case ECONOMY -> new BigDecimal("150.00");
            case BUSINESS -> new BigDecimal("400.00");
            case FIRSTCLASS -> new BigDecimal("1000.00");
            default -> throw new IllegalArgumentException("Invalid seat type");
        };
    }

    /**
     * method: Calculate the price of a checked luggage
     * Input: checkedLuggage - the number of checked luggage
     * Output: Bigdecimal - the price of the luggage
     * Throws: IllegalArgumentException if the number of checked luggage is negative
     * @param checkedLuggage
     * @return
     */
    private BigDecimal calculateLuggagePrice(short checkedLuggage) {
        if (checkedLuggage < 0) {
            throw new IllegalArgumentException("Number of checked luggage cannot be negative");
        }

        checkedLuggage = (short) Math.min(checkedLuggage, (short) 4);

        BigDecimal baseLuggagePrice = new BigDecimal(checkedLuggage * 30.00);

        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(new BigDecimal(checkedLuggage).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP));

        return baseLuggagePrice.multiply(discountMultiplier);
    }

}
