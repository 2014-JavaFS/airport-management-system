package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing bookings.
 *
 * <p>This class provides methods for creating bookings, calculating prices, and retrieving bookings
 * from the repository. It handles business logic related to bookings, such as price calculations and
 * setting additional properties based on the seat type.
 */
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Books a flight by creating a booking object.
     *
     * <p>This method sets the price of the booking and determines if carry-on luggage is allowed based on the seat type.
     * If the seat type is either "BUSINESS" or "FIRSTCLASS", carry-on luggage is allowed. The method then attempts to
     * save the booking to the repository. If the booking creation fails, an {@link InvalidInputException} is thrown.
     *
     * @param newBooking the {@link Booking} object containing the details of the new booking.
     * @return a {@link BookingResponseDTO} object representing the created booking.
     * @throws InvalidInputException if the booking creation fails.
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
     * Finds all {@link Booking} objects in the repository.
     *
     * <p>This method calls the {@link BookingRepository#findAll()} method to retrieve all booking records from the repository.
     *
     * @return a list of {@link Booking} objects representing all bookings in the repository.
     */
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    /**
     * finds all bookings for a provided member id
     *
     *
     <p>This method retrieves all bookings associated with the provided member ID from the repository,
     * converts them into {@link BookingResponseDTO} objects, and returns them as a list.
     *
     * @param memberId the ID of the member whose bookings are to be retrieved.
     * @return a list of {@link BookingResponseDTO} objects representing the bookings of the specified member
     */
    public List<BookingResponseDTO> findAllBookingsByMemberId(int memberId){
       return bookingRepository.findAllBookingsByMemberId(memberId)
               .stream()
               .map(BookingResponseDTO::new)
               .toList();
    }

    /**
     * Calculates the total price for a booking.
     *
     * <p>This method calculates the total price for a booking by summing the base price, the seat price, and the luggage price.
     * The base price is fixed at $299.99. The seat price is determined by the seat type, and the luggage price is determined
     * by the number of checked luggage items.
     *
     * @param booking the {@link Booking} object containing the details of the booking, including seat type and checked luggage.
     * @return the total price for the booking as a {@code BigDecimal}.
     */
    public BigDecimal calculateTotalPrice(Booking booking) {
        BigDecimal seatPrice = calculateSeatPrice(booking.getSeatType());
        BigDecimal luggagePrice = calculateLuggagePrice(booking.getCheckedLuggage());
        return new BigDecimal("299.99").add(seatPrice).add(luggagePrice);
    }

    /**
     * Calculates the price of a seat based on the seat type.
     *
     * <p>This method determines the price of a seat by checking the seat type provided as an argument.
     * The prices are predefined as follows:
     * <ul>
     *   <li>{@code SEATSOPTIONAL}: $50.00</li>
     *   <li>{@code ECONOMY}: $150.00</li>
     *   <li>{@code BUSINESS}: $400.00</li>
     *   <li>{@code FIRSTCLASS}: $1000.00</li>
     * </ul>
     *
     * @param seatType the type of the seat for which the price is to be calculated.
     * Must be one of the {@link Booking.SeatType} enum values.
     * @return the price of the seat as a {@code BigDecimal}.
     * @throws IllegalArgumentException if the provided {@code seatType} is not a valid seat type.
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
     * Calculates the price for checked luggage.
     *
     * <p>This method calculates the total price for checked luggage based on the number of checked luggage items.
     * Each piece of luggage has a base price of $30.00. A discount is applied based on the number of checked luggage items:
     * the discount increases linearly, reaching a maximum discount if four pieces of luggage are checked.
     *
     * @param checkedLuggage the number of checked luggage items. Must be a non-negative value.
     * If the number exceeds 4, it will be capped at 4.
     * @return the total price for the checked luggage as a {@code BigDecimal}.
     * @throws IllegalArgumentException if the number of checked luggage is negative.
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
