package com.revature.ams.Booking;

import com.revature.ams.Booking.dtos.BookingResponseDTO;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * The {@code BookingService} class provides methods to manage flight bookings.
 * It interacts with the {@link BookingRepository} to perform CRUD operations.
 */
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    /**
     * Books a flight with the given {@link Booking} details.
     * <p>
     * This method calculates the total price for the booking and sets the carry-on allowance
     * based on the seat type. It then creates the booking using the repository and returns
     * a {@link BookingResponseDTO} if successful.
     * </p>
     *
     * @param newBooking the booking details for the new flight.
     * @return a BookingResponseDTO object containing the details of the booked flight
     * @throws InvalidInputException if the booking creation fails
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
     * TODO: DOCUMENT ME
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
