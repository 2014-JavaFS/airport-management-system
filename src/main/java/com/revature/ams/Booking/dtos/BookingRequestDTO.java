package com.revature.ams.Booking.dtos;

/**
 *
 * Looks like its a simplified object that is partially implementing the Booking Object.
 * It looks like it is made to be given from client to server, so things left out are probably
 * to keep the user from modifying them in error inducing or malicious ways.
 * It leaves out Carry On Allowed, Booking Number, and Price variables.
 * It is setup to no arg construct, use gets/sets, and full args construct.
 * No To String, unlike the other DTO, but since this is from client to server
 * this makes some level of sense, as the server doesn't need a To String, it would
 * just save the Information or convert to an actuall Bookings and use its To String.
 */
public class BookingRequestDTO {
    private int flightNumber;
    private int memberId;
    private short checkedLuggage;
    private String seatType;

    public BookingRequestDTO() {
    }

    public BookingRequestDTO(int flightNumber, int memberId, short checkedLuggage, String seatType) {
        this.flightNumber = flightNumber;
        this.memberId = memberId;
        this.checkedLuggage = checkedLuggage;
        this.seatType = seatType;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
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

}
