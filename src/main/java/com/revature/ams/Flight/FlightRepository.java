package com.revature.ams.Flight;

import com.revature.ams.util.ConnectionFactory;
import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.interfaces.Crudable;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static com.revature.ams.AirportFrontController.logger;

/**
 * Flight repository follows the Data Access Object (DAO) pattern
 */
public class FlightRepository implements Crudable<Flight>{

    /**
     * Updates a flight's information in the database.
     * @param updatedFlight the flight to be updated
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean update(Flight updatedFlight) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            //"insert into flights(flight_number, origin_airport, destination_airport, seat_count) set (?, ?, ?, ?)";

            //String sql = "UPDATE flights SET time_departure = ?, time_arrival = ?, pilot = ?, airline = ?, origin_airport = ?, destination_airport = ?, seat_count = ? WHERE flight_number = ?";
            String sql = "call update_flight(?,?,?,?,?,?,?,?, 1)";
            CallableStatement callableStatement = conn.prepareCall(sql);
  
            OffsetDateTime dateTime = updatedFlight.getTimeDeparture();
            Timestamp departure = Timestamp.valueOf(dateTime.toLocalDateTime());
            OffsetDateTime date = updatedFlight.getTimeArrival();
            Timestamp arrival = Timestamp.valueOf(date.toLocalDateTime());
            // DO NOT FORGET SQL is 1-index, not 0-index. They made preparedStatement 1-index
            callableStatement.setInt(1, updatedFlight.getFlightNumber());
            callableStatement.setString(2, updatedFlight.getOriginAirport());
            callableStatement.setString(3, updatedFlight.getDestinationAirport());
            callableStatement.setTimestamp(4, departure);
            callableStatement.setTimestamp(5, arrival);
            callableStatement.setShort(6, updatedFlight.getSeatCount());
            callableStatement.setInt(7, updatedFlight.getPilot());
            callableStatement.setInt(8, updatedFlight.getAirline());

            System.out.println(callableStatement);

            int checkUpdate = callableStatement.executeUpdate();
            logger.info("Updating information, respond given was {}", checkUpdate);


            return true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Depending on user input for which flight number to be deleted, delete the flight from the database.
     * return true if the flight was deleted, false otherwise.
     */
    @Override
    public boolean delete(Flight flight) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            String sql = "DELETE FROM flights WHERE flight_number = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, flight.getFlightNumber());
            boolean checkDelete = preparedStatement.executeUpdate() == 1;
            if(checkDelete){
                System.out.println("Flight record was deleted.");
            } else{
                System.out.println("Flight record was not deleted.");
            }
            return checkDelete;

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Flight> findAll() {
        try (Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
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

            logger.info(preparedStatement.toString());
            int checkInsert = preparedStatement.executeUpdate();
            if (checkInsert == 0){
                throw new RuntimeException("Flight was not inserted into the database");
            }

            return newFlight;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Flight findById(int number) throws DataNotFoundException{
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            logger.info("Flight number provided to the Repository by the service is {}", number);
            String sql = "select * from flights where flight_number = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, number);


            logger.info(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                logger.warn("Information not found within database given flight number {}", number);
                throw new DataNotFoundException("No flight with that id " + number + " exists in our database.");
            }
            Flight foundFlight = generateFlightFromResultSet(resultSet);
            logger.info("Sending back flight information {}", foundFlight);
            return foundFlight;

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

        Timestamp timeArrival = rs.getObject("time_arrival", Timestamp.class);
        if(timeArrival != null)
            flight.setTimeArrival(timeArrival.toLocalDateTime().atOffset(ZoneOffset.UTC));

        Timestamp timeDeparture = rs.getObject("time_departure", Timestamp.class);
        if(timeDeparture != null)
            flight.setTimeDeparture(timeDeparture.toLocalDateTime().atOffset(ZoneOffset.UTC));

        return flight;
    }

}
