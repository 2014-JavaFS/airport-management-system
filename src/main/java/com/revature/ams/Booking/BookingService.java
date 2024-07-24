package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Handles all the business logic for the BookingController class
 * It contains methods that validate any and all information provided for creating and finding flight bookings
 */
@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Handles the creation of a new flight booking
     * Sets the price of the new booking
     * Checks whether a carry on is allowed based on the seat type
     * If it is unable to create a booking, an InvalidInputException is thrown
     *
     * @param newBooking The information needed to create a new booking
     *
     * @return The newly created booking
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
     * Retrieves a list of all the flight bookings
     *
     * @return A list of all the booked flights and their corresponding information
     */
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * Finds and returns all flights booked by the member matching the provided member Id
     * Once returned, the list of flights is converted into a Stream of objects
     * Each booking is mapped to a BookingResponseDTO object and then returned as a list
     *
     * @param memberId the id of the member
     *
     * @return  A list of BookingResponseDTO objects representing the flights booked by the specified member
     */
    public List<BookingResponseDTO> findAllBookingsByMemberId(int memberId){
        return bookingRepository.findAllBookingsByMemberId(memberId)
                .stream()
                .map(BookingResponseDTO::new)
                .toList();
    }

    /**
     * Calculates total price of a ticket given seat price and luggage price
     * @param booking The booking object that contains the seatType and checkedLuggage
     * @return The total price of the ticket given seat price and luggage price
     */
    public BigDecimal calculateTotalPrice(Booking booking) {
        BigDecimal seatPrice = calculateSeatPrice(booking.getSeatType());
        BigDecimal luggagePrice = calculateLuggagePrice(booking.getCheckedLuggage());
        return new BigDecimal("299.99").add(seatPrice).add(luggagePrice);
    }

    /**
     * A helper method that returns the price of a given seat type
     * @param seatType an enum used to choose which price to charge (e.g. Economy, Business, etc.)
     * @return The price of the seat
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
     * A helper method that calculates luggage price given the number of checked luggage minus a discount
     * @param checkedLuggage The amount of checked luggage
     * @return The luggage price minus a discount
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
