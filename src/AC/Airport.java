package AC;

/**
 * This will serve as an airport object which will help in the latter stages
 * @author Aleem Ur-Rehman
 */
public class Airport {
    private String name; //airports name
    private String code; //airports code
    //longtitude and latitude coordinates of the airports
    private Double latititude;
    private Double longitiude;

    //constructor for the airport - where we create the object and assign the attributes from outside the class
    public Airport(String name,String code, double lat, double longs){
        this.name = name;
        this.code = code;
        this.longitiude = longs;
        this.latititude = lat;
    }
    
    //get and set longtitude
    public Double getLongitiude() {
        return longitiude;
    }

    public void setLongitiude(Double longitiude) {
        this.longitiude = longitiude;
    }
    
    
    //get + set airport name
    public String getAirportName() {
        return name;
    }

    public void setAirportName(String airportName) {
        this.name = airportName;
    }

    //get + set airport code 
    public String getAirportCode() {
        return code;
    }

    public void setAirportCode(String airportCode) {
        this.code = airportCode;
    }
    
    //get and set latitude
    public Double getLatitude() {
        return latititude;
    }

    public void setLatitude(Double latititude) {
        this.latititude = latititude;
    }


}
