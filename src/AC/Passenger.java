package AC;

/***
 * Passenger object - contains all the information from the first CSV file
 * @author Aleem Ur-Rehman
 *
 */
public class Passenger {
	//the relevant data attributes - these objects will be treated like rows of a table
    private String passengerId; 
    private String flightId;
    private String originAirport;
    private String destinationAirport;
    private int departureTime;
    private int totalAirTime;

    /***
     * Passenger constructor, this will allow us to create passenger objects and make array lists of passengers
     * @param passengerID
     * @param flightID
     * @param departureAirport
     * @param destinationAirport
     * @param departure
     * @param airTime
     */
    public Passenger(String passengerID, String flightID, String departureAirport, String destinationAirport, int departure, int airTime){
        this.passengerId = passengerID;
        this.flightId = flightID;
        this.originAirport = departureAirport;
        this.destinationAirport = destinationAirport;
        this.departureTime = departure;
        this.totalAirTime = airTime;
    }

    
    //get methods for all attributes
    public String getPassengerId() {
        return passengerId;
    }
    
    public String getFlightId() {
        return flightId;
    }

    public String getOriginAirport() {
        return originAirport;
    }
    
    public String getDestinationAirport() {
        return destinationAirport;
    }
    
    public int getDepartureTime() {
        return departureTime;
    }
    
    public int getAirTime() {
        return totalAirTime;
    }
    
    //set methods for all attributes
    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }
    
    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    
    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

 
    public void setDepartureTime(int departureTime) {
        this.departureTime = departureTime;
    }

    public void setTotalAirTime(int totalFlightTime) {
        this.totalAirTime = totalFlightTime;
    }
}
