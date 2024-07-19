package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * TODO: DOCUMENT ME
 * This documentation is created by Arjun Ramsinghani
 * We want a booking repository so that we can make adjustments to the database
 */
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * TODO: DOCUMENT ME
     * First we calculate the price
     * Then we want to know if we can have carry on luggage for our ticket, if conditions are met then we can carry on, otherwise it is false
     * Optional is used if there is a clear need to represent a null value and placing a null value can cause an error, very useful class
     * As long as the create method in repository doesnt throw anything than we can map the new booking to our DTO, otherwise it returns a null value
     * map method returns a stream of results after applying the function to its elements
     * @param newBooking
     * @return
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
     * TODO: DOCUMENT ME
     * Returns a list of all bookings
     * @return
     */
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * TODO: DOCUMENT ME
     * Returns all bookings created by a single person
     * stream method will return a stream with this collection as the source
     * map method returns a stream of results after applying the function to its elements
     * toList method returns a list of the stream elements
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
     * TODO: DOCUMENT ME
     * will calculate the price of the seat and luggage then return the sum in accordance with the value provided in below method
     * @param booking
     * @return
     */
    public BigDecimal calculateTotalPrice(Booking booking) {
        BigDecimal seatPrice = calculateSeatPrice(booking.getSeatType());
        BigDecimal luggagePrice = calculateLuggagePrice(booking.getCheckedLuggage());
        return new BigDecimal("299.99").add(seatPrice).add(luggagePrice);
    }

    /**
     * TODO: DOCUMENT ME
     * this method calculates the price based on the seat option and its provided value
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
     * TODO: DOCUMENT ME
     * checks if we have our checked in luggage, accounts for negative numbers
     * will do math to calculate the base luggage price and discounts
     * @param checkedLuggage
     * @return - final calculation
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
