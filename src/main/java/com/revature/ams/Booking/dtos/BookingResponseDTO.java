package com.revature.ams.Booking.dtos;

import com.revature.ams.Booking.Booking;
import com.revature.ams.Flight.Flight;

import java.math.BigDecimal;


/**
 * TODO: DOCUMENT ME
 * Making a class called BookingsResponseDTO used as a Data Transfer Object for other
 * classes. This class declares data as well as implements 3 constructors each with 
 * its own way of setting data.
 */
public class BookingResponseDTO {
    private int bookingId;
    private Flight flight;
    private boolean carryOnAllowed;
    private short checkedLuggage;
    private String seatType;
    private BigDecimal price;

    public BookingResponseDTO() {
    }

    public BookingResponseDTO(int bookingId, Flight flight, boolean carryOnAllowed, short checkedLuggage, String seatType, BigDecimal price) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.carryOnAllowed = carryOnAllowed;
        this.checkedLuggage = checkedLuggage;
        this.seatType = seatType;
        this.price = price;
    }

    /**
     * TODO: DOCUMENT ME
     * This constructor is called whenever we want to make a new BookingResponseDTO Object given the
     * parameter booking. It gets the booking data and sets the data into the newly created Object.
     * @param booking
     */
    public BookingResponseDTO(Booking booking){
        this.flight = booking.getFlight();
        this.bookingId = booking.getBookingId();
        this.carryOnAllowed = booking.isCarryOnAllowed();
        this.seatType = booking.getSeatType().name();
        this.price = booking.getPrice();
        this.checkedLuggage = booking.getCheckedLuggage();
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public boolean isCarryOnAllowed() {
        return carryOnAllowed;
    }

    public void setCarryOnAllowed(boolean carryOnAllowed) {
        this.carryOnAllowed = carryOnAllowed;
    }

    public short getCheckedLuggage() {
        return checkedLuggage;
    }

    public void setCheckedLuggage(short checkedLuggage) {
        this.checkedLuggage = checkedLuggage;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    @Override
    public String toString() {
        return "BookingResponseDTO{" +
                "bookingId=" + bookingId +
                ", carryOnAllowed=" + carryOnAllowed +
                ", checkedLuggage=" + checkedLuggage +
                ", seatType='" + seatType + '\'' +
                ", price=" + price +
                '}';
    }
}
