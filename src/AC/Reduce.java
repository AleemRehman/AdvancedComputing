package AC;

import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

/***
 * Processing data from the mapper ready for output
 * @author Aleem Ur-Rehman
 *
 */
public class Reduce {
	//create both jobs reducers
    Map.Entry<String,Integer> job1; //first job requires a map entry
    ArrayList<ArrayList<String>> job2; //second job can be done with a nest array list

    
    //actual reduce constructor -- allows me to port the jobs so we can determine output
    public Reduce(String key, ArrayList<Merge> merger, int j){
        switch (j){
            case 1:
            	valuesForJob1(key, merger);
            case 2:
            	valuesForJob2(key, merger);
        }
    }
    
    public Map.Entry<String, Integer> getJob1() {
        return job1;
    }

    public ArrayList<ArrayList<String>> getJob2() {
        return job2;
    }

    /***
     * prepare values for job 1 - will also be used in job 3
     * @param key
     * @param merge
     */
    private void valuesForJob1(String key,ArrayList<Merge> merge){
        int values = 0;
        for(Merge merger : merge){ //grab all merger threads for sifting
            if(merger.getJob1Hashmap().get(key) != null){ //if the merged hashmap contains the primary key to pass
            	values = values + merger.getJob1Hashmap().get(key).size(); //add the values
            }
        }
        job1 = new SimpleEntry<>(key, values); //create a new simple entry tuple with the key and the values
    }

    /***
     * values for job 2
     * @param primaryKey
     * @param mergers
     */
    private void valuesForJob2(String key, ArrayList<Merge> merge){ // reduce primary key hashmap to array of all data

        ArrayList<Flight> flights = new ArrayList<>();
        for(Merge merger : merge) { //grab all merger threads for sifting
            if(merger.getJob2Hashmap().get(key) != null){ //if the hashmap isnt null with the primary key
                flights.addAll(merger.getJob2Hashmap().get(key)); //add all flights from the hashmap
            }

            ArrayList<ArrayList<String>> flightProfiles = new ArrayList<>();
            for(Flight flight: flights){ //grab every flight from the list of flights
                ArrayList<String> flightProfile = new ArrayList<>(); //new flight profile
                //build flight profile
                flightProfile.add(key);
                flightProfile.add(flight.getPassengerId());
                flightProfile.add(flight.getOAirport());
                flightProfile.add(flight.getDestinationAirport());
                flightProfile.add(flight.getDepartureTime());
                flightProfile.add(flight.getArrivalTime());
                flightProfile.add(flight.getTotalFlightTime());

                //add flight profile to list of flights
                flightProfiles.add(flightProfile);
            }
            job2 = flightProfiles;
        }
    }
}
