package com.revature.ams.Flight;

import com.revature.ams.util.ConnectionFactory;
import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.exceptions.InvalidInputException;
import com.revature.ams.util.interfaces.Crudable;
import com.revature.ams.util.interfaces.Serviceable;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Flight repository follows the Data Access Object (DAO) pattern
 */
public class FlightRepository implements Crudable<Flight>{

    // TODO: IMPLEMENT ME!!!!!
    @Override
    public boolean update(Flight updatedFlight) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {

            String sql = "update flights set origin_airport = ?, destination_airport = ?, time_departure = ?, time_arrival = ?, pilot = ?, airline = ? where flight_number = ?";
            //String sql = "update flights set origin_airport = ?, destination_airport = ?, pilot = ?, airline = ? where flight_number = ?";

            // PreparedStatements SANITIZE any user input before execution
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // DO NOT FORGET SQL is 1-index, not 0-index. They made preparedStatement 1-index
            preparedStatement.setString(1, updatedFlight.getOriginAirport());
            preparedStatement.setString(2, updatedFlight.getDestinationAirport());
            preparedStatement.setObject(3, Timestamp.valueOf(LocalDateTime.now())); // LocalDateTime vs. Date
            preparedStatement.setObject(4, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(5, updatedFlight.getPilot());
            preparedStatement.setInt(6, updatedFlight.getAirline());
            preparedStatement.setInt(7, updatedFlight.getFlightNumber());
            /*
            preparedStatement.setInt(3, updatedFlight.getPilot());
            preparedStatement.setInt(4, updatedFlight.getAirline());
            preparedStatement.setInt(5, updatedFlight.getFlightNumber());
            */

            int checkInsert = preparedStatement.executeUpdate();
            System.out.println("Updating....");
            if (checkInsert == 0){
                throw new RuntimeException("Flight was not inserted into the datbase");
            }

            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // TODO: IMPLEMENT ME!!!!!
    @Override
    public boolean delete(int flightNumber) {
        Flight result = findById(flightNumber);
        if(result != null){
            try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
                String sql = "delete from flights where flight_number = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                preparedStatement.setInt(1, flightNumber);

                ResultSet resultSet = preparedStatement.executeQuery();

                return true;

            } catch (SQLException e){
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    @Override
    public List<Flight> findAll() {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            List<Flight> flights = new ArrayList<>();

            String sql = "select * from flights";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while(rs.next()){

                flights.add(generateFlightFromResultSet(rs));
            }

            return flights;
        } catch (SQLException e) {
           e.printStackTrace();
           return null;
        }
    }

    @Override
    public Flight create(Flight newFlight) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            // DO NOT USE statement for any DML
//            String sql = "insert into flights(flight_number, origin_airport, destination_airport, seat_count) values ("
//                    + newFlight.getFlightNumber() + "," + newFlight.getOriginAirport() + "," + ;
//            Statement statement = conn.createStatement();

            String sql = "insert into flights(flight_number, origin_airport, destination_airport, seat_count) values (?,?,?,?)";
            // PreparedStatements SANITIZE any user input before execution
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            // DO NOT FORGET SQL is 1-index, not 0-index. They made preparedStatement 1-index
            preparedStatement.setInt(1, newFlight.getFlightNumber());
            preparedStatement.setString(2, newFlight.getOriginAirport());
            preparedStatement.setString(3, newFlight.getDestinationAirport());
            preparedStatement.setShort(4, newFlight.getSeatCount());


            int checkInsert = preparedStatement.executeUpdate();
            System.out.println("Inserting information....");
            if (checkInsert == 0){
                throw new RuntimeException("Flight was not inserted into the datbase");
            }

            return newFlight;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Flight findById(int number) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "select * from flights where flight_number = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, number);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                throw new DataNotFoundException("No flight with that id " + number + " exists in our database.");
            }

            return generateFlightFromResultSet(resultSet);

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private Flight generateFlightFromResultSet(ResultSet rs) throws SQLException {
        Flight flight = new Flight();

        flight.setFlightNumber(rs.getInt("flight_number"));
        flight.setOriginAirport(rs.getString("origin_airport"));
        flight.setDestinationAirport(rs.getString("destination_airport"));
        flight.setSeatCount(rs.getShort("seat_count"));
        flight.setPilot(rs.getInt("pilot"));

        return flight;
    }

}
