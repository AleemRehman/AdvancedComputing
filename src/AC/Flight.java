package AC;

import java.util.Date;

/***
 * Flight Object, this will act as a general flight object and finds its basis with one passenger per one flight object
 * @author Aleem Ur-Rehman
 *
 */
public class Flight {


    private String pId;
    private String oFAA;
    private String dFAA;
    private String dTime;
    private String aTime;
    private String totalAirTime;

    //constructor for the flight object used to instantiate
    public Flight(String pId, String oAirport,String dAirport,String dTime,String aTime, String airTime){
        this.pId = pId;
        this.oFAA = oAirport;
        this.dFAA = dAirport;
        this.dTime = dTime;
        this.aTime = aTime;
        this.totalAirTime = airTime;

    }

    //GET METHODS for all attributes
    public String getPassengerId() {
        return pId;
    }

    public String getArrivalTime() {
        return aTime;
    }
    
    public String getDepartureTime() {
        return dTime;
    }


    public String getTotalFlightTime() {
        return totalAirTime;
    }
    
    public String getOAirport() {
        return oFAA;
    }
    
    //setting for the airports // will be used at a later stage
    public void setOAirport(String faa){
    	oFAA = faa;
    }
    public void setDAirport(String faa){
    	dFAA = faa;
    }
    
    public String getDestinationAirport() {
        return dFAA;
    }


}
