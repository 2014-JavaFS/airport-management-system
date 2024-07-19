package com.revature.ams.Booking;

import com.revature.ams.Flight.Flight;
import com.revature.ams.Member.Member;
import com.revature.ams.util.ConnectionFactory;
import com.revature.ams.util.exceptions.InvalidInputException;

import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TODO: DOCUMENT ME
 * The BookingRepository class is part of the Data Access Layer, in which all database operations
 * are handled. In other words, a layer between the business logic and data storage, part of the
 * broader repository pattern.
 */
public class BookingRepository {

    /**
     * TODO: DOCUMENT ME
     * This code is ysed to create new bookings im the database, using preparedStatements in order to
     * implement sql statements into the database, these insets are than run through a check for Invalid
     * statements and errors.
     * @param newBooking
     * @return
     */
    public Optional<Booking> create(Booking newBooking) {
        try (Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            String sql = "insert into bookings(flight_number, member_id, carry_on_allowed, checked_luggage, seat_type, price) values (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, newBooking.getFlight().getFlightNumber());
            preparedStatement.setInt(2, newBooking.getMember().getMemberId());
            preparedStatement.setBoolean(3, newBooking.isCarryOnAllowed());
            preparedStatement.setShort(4, newBooking.getCheckedLuggage());
            preparedStatement.setObject(5, newBooking.getSeatType(), Types.OTHER);
            preparedStatement.setBigDecimal(6, newBooking.getPrice());

            int checkInsert = preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (checkInsert == 0 || !resultSet.next()) {
                throw new InvalidInputException("Something was wrong when entering " + newBooking + " into the database");
            }

            newBooking.setBookingId(resultSet.getInt(1));
            return Optional.of(newBooking);

        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * TODO: DOCUMENT ME
     * This layer joins the bookings class with the members class, and executes database query
     * similair to the previous class (Booking new Booking) utilizing a findAll() for both bookings
     * and members.
     * @return
     */
    public List<Booking> findAll() {
        try (Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            List<Booking> bookings = new ArrayList<>();
            String sql = "select * from bookings b join members m on m.member_id = b.member_id join flights f on f.flight_number = b.flight_number";
            ResultSet resultSet = conn.createStatement().executeQuery(sql);

            while (resultSet.next()) {
                Flight flight = generateFlightFromResultSet(resultSet);

                Member member = new Member();

                member.setMemberId(resultSet.getInt("member_id"));
                member.setFirstName(resultSet.getString("first_name"));
                member.setLastName(resultSet.getString("last_name"));
                member.setEmail(resultSet.getString("email"));
                member.setType(resultSet.getString("member_type"));

                Booking booking = new Booking();
                booking.setBookingId(resultSet.getInt("booking_id"));
                booking.setFlight(flight);
                booking.setMember(member);
                booking.setCarryOnAllowed(resultSet.getBoolean("carry_on_allowed"));
                booking.setPrice(resultSet.getBigDecimal("price"));
                booking.setCheckedLuggage(resultSet.getShort("checked_luggage"));
                booking.setSeatType(resultSet.getString("seat_type"));

                bookings.add(booking);
            }
            return bookings;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO: DOCUMENT ME
     * This method is similar but specifically searches for Bookings by member id.
     * @param memberId
     * @return
     */
    public List<Booking> findAllBookingsByMemberId(int memberId) {
        try (Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {

            List<Booking> bookings = new ArrayList<>();
            String sql = "select * from bookings b \n" +
                    "join flights f on f.flight_number = b.flight_number\n" +
                    "where b.member_id = ?;";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1,memberId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Flight flight = generateFlightFromResultSet(resultSet);

                Booking booking = new Booking();
                booking.setBookingId(resultSet.getInt("booking_id"));
                booking.setFlight(flight);
                booking.setCarryOnAllowed(resultSet.getBoolean("carry_on_allowed"));
                booking.setPrice(resultSet.getBigDecimal("price"));
                booking.setCheckedLuggage(resultSet.getShort("checked_luggage"));
                booking.setSeatType(resultSet.getString("seat_type"));

                bookings.add(booking);
            }
            return bookings;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO: DOCUMENT ME
     * This takes a ResultSet as an input and potentially throws and exception
     * if there are issues accessing the result, if the exception is not thrown
     * than the Flight object is populated with the ResultSet, also accounts for
     * time handling (time_arrival, time_departure) and error handling.
     * @param rsyee
     * @return
     * @throws SQLException
     */
    private Flight generateFlightFromResultSet(ResultSet rs) throws SQLException {
        Flight flight = new Flight();

        flight.setFlightNumber(rs.getInt("flight_number"));
        flight.setOriginAirport(rs.getString("origin_airport"));
        flight.setDestinationAirport(rs.getString("destination_airport"));
        flight.setSeatCount(rs.getShort("seat_count"));
        flight.setPilot(rs.getInt("pilot"));

        Timestamp timeArrival = rs.getObject("time_arrival", Timestamp.class);
        if (timeArrival != null)
            flight.setTimeArrival(timeArrival.toLocalDateTime().atOffset(ZoneOffset.UTC));

        Timestamp timeDeparture = rs.getObject("time_departure", Timestamp.class);
        if (timeDeparture != null)
            flight.setTimeDeparture(timeDeparture.toLocalDateTime().atOffset(ZoneOffset.UTC));

        return flight;
    }
}
