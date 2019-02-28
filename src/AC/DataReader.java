package AC;
import java.io.*;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.LinkedHashSet;



public class DataReader {
	String passengerDataLoc = "C:\\Users\\Aleem Ur-Rehman\\Desktop\\advanced_computing_map_reduce-master\\AComp_Passenger_data.csv";
    String airportsLatLongLoc = "C:\\Users\\Aleem Ur-Rehman\\Desktop\\advanced_computing_map_reduce-master\\Top30_airports_LatLong.csv";

    private ArrayList<Passenger> passengers = new ArrayList<Passenger>();
    private ArrayList<Airport> airports = new ArrayList<Airport>();

    /**Returns all of the passengers from the DataReader
     * @return Returns all of the passengers list
     */
    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    /**Returns all of the airports from the DataReader
     * @return Returns all of the airports list
     */
    public ArrayList<Airport> getAirports() {
        return airports;
    }
    
    /** checking for empty data lines
     * 
     * @param array of data
     */
    public boolean dataChecker(String[] array) {  //checks for empty strings and 0
        for (int i = 0; i < array.length; i++) { //iterate through the array
            if (array[i] == null || array[i].equals("0")) { 
            	//if that line in the array is 0 or empty returns true
                return true;
            }
        }
        return false; //line is not empty or 0
    }    
    
    /**Read all the information from the data files and attribute it to the two list variables,
     * this can then be accessed from outside the 
     * 
     */
    public void read(){
        BufferedReader pReader = null; //create the reader
        BufferedReader aReader = null; //create the reader
        Passenger passenger; //instantiate the passenger
        Airport airport; //instantiate the airport 
        String dataLine = ""; //current line
        
        //begin attempting to read the passenger data
        try{
        	pReader = new BufferedReader(new FileReader(passengerDataLoc)); //instantiate new buffer reader
            int sLines = 0;
            //while loop to iterate through lines
            while((dataLine = pReader.readLine()) != null) {
            	dataLine = dataLine.toUpperCase(); //converting all data to uppercase
                String[] data = dataLine.split(","); //splitting all data into an array
                // remove empty lines and missing fields
                if (data.length == 6 && !dataChecker(data))
                {
                	//instantiate both departure and flight times
                    int departureTime = 0 ;
                    int flightTime= 0;
                    //try to parse the departure time into an integer from the 4th field
                    try
                    {
                    	departureTime = Integer.parseInt(data[4]);
                    } catch (NumberFormatException ex)
                    {
                        System.out.print(ex + "time " + departureTime + "is wrong"); //catch any conversion issues
                    }
                    //try to parse the total flight time into an integer from the 4th field
                    try
                    {
                    	flightTime = Integer.parseInt(data[5]);
                    } catch (NumberFormatException ex)
                    {
                        System.out.print(ex + "time " + flightTime + "is wrong"); //catch any conversion issues
                    }
                    //create new passenger with the relevant data fields
                    passenger = new Passenger(data[0], data[1], data[2], data[3], departureTime, flightTime);
                    passengers.add(passenger); //add the passenger to the passenger fields 
                    passengers = deleteDuplicates(); //deletes any duplicates
                    passengers = regExOnPassengers(); //regex and remove all ireelveant passengers
                }
                else
                {
                    System.out.println("error"); //print out any errors
                    sLines = sLines++; //iterate up the amount of skipped lines
                }
            }
            System.out.println("Total skipped lines: " + sLines); //total amount of skipped lines printed
            pReader.close(); //closing the passenger reader

            //create the reader for the airport 
            aReader = new BufferedReader(new FileReader(airportsLatLongLoc));
            while((dataLine = aReader.readLine()) != null) {
            	//iterate through the airports
                String[] airportDataLine = dataLine.split(",");
                //check if lines are empty and within a maximum length of 4
                if(airportDataLine.length == 4 && !dataChecker(airportDataLine)){
                    //create the airport to be passed into the list
                	airport = new Airport(airportDataLine[0], 
                    		airportDataLine[1],Double.parseDouble(airportDataLine[2]), 
                    		Double.parseDouble(airportDataLine[3]));
                	//add an airport to the list of airports
                    airports.add(airport);
                }
                //print out error message regarding to missing lines
                else
                {
                    System.out.print("error");
                }

            }
        //error handling for file errors
        } catch (FileNotFoundException e){
            System.out.println(e);
        } catch (IOException e){
        	System.out.println(e);
        }
    }
    
    /***
     * delete any duplicate passenger objects to clean up data
     * @return the passengers array list
     */
	private ArrayList<Passenger> deleteDuplicates()
	{
		Set<Passenger> set = new LinkedHashSet<>(); //create new linked hash set
        set.addAll(passengers); //add all the passengers to the set
        passengers.clear(); //clear all the passengers
        passengers.addAll(set); //add all the passengers from the linked hash set back into the list
        return passengers; 
	}
	
	/***
	 * regex on all the passengers and remove corrupt data
	 * @return
	 */
	private ArrayList<Passenger> regExOnPassengers(){
		//iterate through every passenger object in the list
		for(Passenger p : passengers){
			String passengerId = p.getPassengerId();
			String flightId = p.getFlightId();
			Pattern pIdPattern = Pattern.compile("[A-Z]{3}\\d{4}[A-Z]{2}\\d");
			Pattern fIdPattern = Pattern.compile("[A-Z]{3}\\d{4}[A-Z]");
			Matcher pIdMatcher = pIdPattern.matcher(passengerId);
		   	Matcher fIdMatcher = fIdPattern.matcher(flightId); 
		   	if(!pIdMatcher.matches()){
		   		passengers.remove(p);
		   		return passengers;
		   	}
		   	if(!fIdMatcher.matches()){
		   		passengers.remove(p);
		   		return passengers;
		   	}
		   	if(passengerId == null){
		   		passengers.remove(p);
		   		return passengers;
		   	}
		}
		return passengers;
	}
}
