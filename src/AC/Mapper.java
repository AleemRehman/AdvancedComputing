package AC;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.DataFormatException;
import java.util.AbstractMap.SimpleEntry;
import java.text.DateFormat;
import java.util.Date;
import java.util.Calendar;

/***
 * The Mapper class, this allows us to create key value pairs for each task
 * @author Aleem Ur-Rehman
 *
 */
public class Mapper implements Runnable {

    private int j; //the job number, this will be passed into the mapper
    //maps for the jobs
    private List<Map.Entry<String,String>> job1Map = new ArrayList<>();
    private List<Map.Entry<String,Flight>> job2Map = new ArrayList<>();
    private List<Map.Entry<String,String>> job3Map = new ArrayList<>();
    //passengers and airports lists
    private List<Passenger> passengers = new ArrayList<Passenger>();
    private List<Airport> airports = new ArrayList<Airport>();
    //thread variables
    private Thread currentThread; //current thread
    private String tName; //will be used for printing 
    
    
    /***
     * Constructor for the Mapper object, passing in the passenger / airports list, the job number and the thread name
     * @param passengers
     * @param airports
     * @param job
     * @param name
     */
    public Mapper(ArrayList<Passenger> passengers,List<Airport> airports, int job, String name){
    	this.passengers = passengers; //passenger list
    	this.airports = airports; //airports
        j = job;
        tName = name;
    }
    
    
    //run mapping on thread
    public void run(){ 
    	//get all the relevant airport information
        ArrayList<Double> allAirportLats = getAllAirportLatitudes(airports); //get all lats
        ArrayList<Double> allAirportLong = getAllAirportLongs(airports); //get all longs
        ArrayList<String> allAirportCodes = getAllAirportCodes(airports); //get all codes
        //iterate through the passengers for the jobs
        for(int i = 0; i < passengers.size(); i++){
        	Passenger tempPassenger = passengers.get(i); //get the passenger at that index and assign to temp passenger
            if(regExPassenger(tempPassenger)){ //redo regex on the passenger object
                // job switch
                switch (j){
                	//job1: Determine the number of flights from each airport; include a list of any airports not used.
                    case 1:
                        if(allAirportCodes.contains(tempPassenger.getOriginAirport())) //this removes any data with wrong airport codes (based on the assumption this is now curupt data so passenger will not be on flight)
                        {
                                Entry<String,String> entryPairJob1 = new SimpleEntry<>(tempPassenger.getOriginAirport(),tempPassenger.getPassengerId()); //create the entry key value paur assigning the origin airport and the passenger ID
                                job1Map.add(entryPairJob1);
                        }
                        break;
                    
                    //job 2: Create a list of flights based on the Flight id, this output should include the passenger Id, relevant IATA/FAA codes, 
                    //the departure time, the arrival time (times to be converted to HH:MM:SS format), and the flight times
                    case 2:
                    	//convert the epoch times into real time formats to be displayed 
                        Integer dInt =  tempPassenger.getDepartureTime(); 
                        long dEpoc = dInt.longValue() * 1000;
                        Date dDate = new Date(dEpoc);
                        String dTime = " ";
                        //instantiate calendar
                        Calendar calendar = Calendar.getInstance();
                        //setting the time format for the resulting date
                        DateFormat format = new SimpleDateFormat("HH:mm:ss");
                       //try catch any errors with the conversion
                        try{
                        	dTime = format.format(dDate);//try to format the date -- rare that we get exceptions this is for future proofing
                        }catch(Exception ex){
                        	System.out.println(ex); //print any excpetions we may get
                        }
                        
                        //set time on the calendar with departure time
                        calendar.setTime(dDate);
                        calendar.add(Calendar.MINUTE,tempPassenger.getAirTime()); //used to now calculate the arrival time

                        Date aDate = calendar.getTime(); //arrival date based on the total flight time
                        String aTime = format.format(aDate); //arrival time based on the total flight time

                        //create flight information for the customer 
                        Flight flightInformationToPrint = new Flight(tempPassenger.getPassengerId(),
                        		tempPassenger.getOriginAirport(),tempPassenger.getDestinationAirport(),
                        		dTime,aTime,String.valueOf(tempPassenger.getAirTime()));

                        //create the key value pair to add to the list
                        Entry<String,Flight> flightProfile = new SimpleEntry<>(tempPassenger.getFlightId(),flightInformationToPrint);
                        job2Map.add(flightProfile);

                    //job 3: Calculate the number of passengers on each flight.
                    case 3:
                        String fId = tempPassenger.getFlightId(); //get the flight of the current passenger
                        String pId = tempPassenger.getPassengerId(); //get the passenger id of the current passenger
                        Entry<String, String> flightAmounts = new SimpleEntry<>(fId, pId); //create the flights pair
                        job3Map.add(flightAmounts); //add the flight amounts to the job map
                    
                    //job 4: Calculate the line-of-sight (nautical) miles for each flight and the total travelled by each passenger and thus output the passenger having earned the highest air miles.
                    case 4:
                    	// unfinished
                    default:
                        break;

                }
            }


        }
    }

    /***
     * regec for the passenger
     * @param passLine
     * @return
     */
    public Boolean regExPassenger(Passenger verify) {

        if(!verify.getPassengerId().matches("([A-Z]{3}\\d{4}[A-Z]{2}\\d)") || verify.getPassengerId().length() != 10){
            return false; //check the length and regex passengerID
        }
        if(!verify.getFlightId().matches("([A-Z]{3}\\d{4}[A-Z])")|| verify.getFlightId().length() != 8){
            return false; //check the length and regex flightID
        }
        if(!verify.getOriginAirport().matches("([A-Z][A-Z][A-Z])") || verify.getOriginAirport().length() !=3){
            return false; //check the length and regex origin airport code
        }
        if(!verify.getDestinationAirport().matches("([A-Z][A-Z][A-Z])") || verify.getDestinationAirport().length() !=3){
            return false; //check the length and regex destination aiport code
        }
        else{
            return true; //return true if all good
        }
    }

    
    /***
     * get all the airport codes
     * @param airData
     * @return
     */
    private ArrayList<String> getAllAirportCodes(List<Airport> aiportList)
    {
        ArrayList<String> codes = new ArrayList<String>(); //create the codes list
        //iterate through the airports
        for (Airport airport : aiportList)
        {
        	//get the codes for the relevant airport
            if(!codes.contains(airport.getAirportCode())){
                //if it doens't already contain the code we add to the list
            	codes.add(airport.getAirportCode());
            }
        }
        return codes;
    }
    
    /***
     * get all the longtitudes for the airports
     * @param airData
     * @return
     */
    private ArrayList<Double> getAllAirportLongs(List<Airport> aiportList)
    {
        ArrayList<Double> longtitudes = new ArrayList<Double>();
        for (Airport airport : aiportList) //iterate through the airports
        {
        	//add the longtitudes if they don't already exist
            if(!longtitudes.contains(airport.getLongitiude())){
            	longtitudes.add(airport.getLongitiude());
            }
        }
        return longtitudes;
    }
    
    /***
     * FUNCTIONS TO GET ALL THE MAPS
     * @return
     */
    public List<Entry<String, String>> getJob1Map() {
        return job1Map;
    }

    public List<Entry<String, Flight>> getJob2Map() {
        return job2Map;
    }

    public List<Entry<String, String>> getJob3Map() {
        return job3Map;
    }
    
    
    /***
     * Get all the aiport latitudes
     * @param airData
     * @return
     */
    private ArrayList<Double> getAllAirportLatitudes(List<Airport> aiportList)
    {
        ArrayList<Double> latitudes = new ArrayList<Double>();
        //iterate through airports
        for (Airport airport : aiportList)
        {
        	//add to the latitudes list if they're not already in there
            if(!latitudes.contains(airport.getLatitude())){
            	latitudes.add(airport.getLatitude());
            }
        }
        return latitudes;
    }
}
