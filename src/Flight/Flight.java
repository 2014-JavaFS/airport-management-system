package Flight;

import java.util.Date; //If you want to see a class you didn't implement ctrl+left-click

public class Flight {
    // Define Attributes: information contained within every instance of this class (object)
    int flightNumber;
    String originAirport; // Airport identification number or String
    String destinationAirport;
    Date timeDeparture;
    Date timeArrival;
    short seatCount;
    int pilot;
    int airline;

    // Constructors: Initializes(fills in the informaiton for the class attribute or variables) or Instantiates(creation of the object) a class,
    // What if don't define constructors? There is an implicit/default constructor that sets everything to null, NoArgs Constructor
    // What happens to our default/NoArgs constructor if we create a constructor?
    // OOP - Polymorphism - Method Overloading, changing the number of parameters defined & the actions that take place
    public Flight(){}


    // Reason behind naming the parameters in our constructor the same as our class attributes is to shadow the names,
    // so you know EXACTLY what's being initiliazed
    // example: new Flight(1, "PHL", "BOS".....)
    // AllArgs constructor
    public Flight(int flightNumber, String originAirport, String destinationAirport,
                  Date timeDeparture, Date timeArrival, short seatCount, int pilot, int airline){
        this.flightNumber = flightNumber;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
        this.seatCount = seatCount;
        this.pilot = pilot;
        this.airline = airline;
    }

    public Flight(int flightNumber, String originAirport, String destinationAirport, short seatCount){
        this.flightNumber = flightNumber;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.seatCount = seatCount;
    }

    // Methods - functionality applied to every class
    // Getters & Setters >> Overrides >> Custom Methods
    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getOriginAirport() {
        return originAirport;
    }

    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public Date getTimeDeparture() {
        return timeDeparture;
    }

    public void setTimeDeparture(Date timeDeparture) {
        this.timeDeparture = timeDeparture;
    }

    public Date getTimeArrival() {
        return timeArrival;
    }

    public void setTimeArrival(Date timeArrival) {
        this.timeArrival = timeArrival;
    }

    public short getSeatCount() {
        return seatCount;
    }

    public void setSeatCount(short seatCount) {
        this.seatCount = seatCount;
    }

    public int getPilot() {
        return pilot;
    }

    public void setPilot(int pilot) {
        this.pilot = pilot;
    }

    public int getAirline() {
        return airline;
    }

    public void setAirline(int airline) {
        this.airline = airline;
    }

    // OOP - Inheritence - Object is the parent class, this child class of Flight has access to the toString()
    // OOP - Polymorphism - Method Overriding, taking a methods implementation from an inherited class and changing specifically for the child class
    @Override // annotation - metadata to let java know this is Overriding a method: At the root of EVERY OBJECT IN JAVA is the Object Class
    public String toString() {
        return "Flight{" +
                "flightNumber=" + flightNumber +
                ", originAirport='" + originAirport + '\'' +
                ", destinationAirport='" + destinationAirport + '\'' +
                ", timeDeparture=" + timeDeparture +
                ", timeArrival=" + timeArrival +
                ", seatCount=" + seatCount +
                ", airline=" + airline +
                '}';
    }

    // Custom Methods - you always would put a custom method below the generated methods
    // OOP - Polymorphism - Method Overloading
    public long calculateTravelTime(){
        return this.timeDeparture.getTime() - this.timeArrival.getTime();
    }

    public long calculateTravelTime(Date timeDeparture, Date timeArrival){
        return timeDeparture.getTime() - timeArrival.getTime();
    }
}
