package com.revature.ams.Booking.dtos;

/**
 * TODO: DOCUMENT ME
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
