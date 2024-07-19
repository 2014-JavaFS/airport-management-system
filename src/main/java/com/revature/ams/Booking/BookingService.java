package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * TODO: DOCUMENT ME
 */
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * TODO: DOCUMENT ME
     *
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
     * @return
     */
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * This service method retrieves all bookings made by a specific member based on their memberId.
     * 
     * @param memberId
     * @return List generated from the bookingRepository and sends it to the bookingController.
     */
    public List<BookingResponseDTO> findAllBookingsByMemberId(int memberId){
       return bookingRepository.findAllBookingsByMemberId(memberId)
               .stream()
               .map(BookingResponseDTO::new)
               .toList();
    }

    /**
     * The method calculates the total price of the booking, including seat price and luggage price.
     * Utilizes calculateSeatPrice and calculateLuggagePrice
     * 
     * @param booking
     * @return Base price of the flight, plus the seat and luggage price
     */
    public BigDecimal calculateTotalPrice(Booking booking) {
        BigDecimal seatPrice = calculateSeatPrice(booking.getSeatType());
        BigDecimal luggagePrice = calculateLuggagePrice(booking.getCheckedLuggage());
        return new BigDecimal("299.99").add(seatPrice).add(luggagePrice);
    }

    /**
     * The method assigns the seat price based on the seat type.
     * @param seatType
     * @throws IllegalArgumentException when the seat type is invalid.
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
     * The method calculates the total price of luggage based on the number of checked luggage, including discounts.
     * Checked luggage is capped at 4 to prevent unnecessary discounts.
     * The baseLuggagePrice assigns a base price of 30.00 per checked luggage.
     * Discounts are applied based on the percentage of the base luggage price with the maximum discount being 25% when 4 luggages are booked.
     * 
     * @param checkedLuggage
     * @throws IllegalArgumentException when the number of checked luggage is less than 0.
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
