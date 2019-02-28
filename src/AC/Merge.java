package AC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/***
 * Merging platform, where data gets combined into hashmaps
 * @author Aleem Ur-Rehman
 *
 */
public class Merge {
    private HashMap<String, ArrayList<String>> job1Hashmap = new HashMap<>();
    private HashMap<String, ArrayList<Flight>> job2Hashmap = new HashMap<>();
    private HashMap<ArrayList<Flight>, ArrayList<Airport>> passengerAirMilesHM = new HashMap<>(); //task 4 unimplemented
    int job;

    //overload with the job number to combine
    public Merge(int j) {
    	job = j;
    }

    
    /***
     * merge the job 1 hashmap - so we can get the pair of airports with the numbers of flights from each one
     * @param job1PassList
     */
    public void mergeJob1Hashmap(List<Map.Entry<String,String>> job1PassList){
        ArrayList<String> oPorts = new ArrayList<>(); //instantiate the list of new origin aiports
        for(int i = 0; i < job1PassList.size(); i++){
            Map.Entry<String,String> data = job1PassList.get(i); //create a map entry for every map thats passsed in
            String airport = data.getKey(); //get the airport by the relevant key in data
            //add the airport to the list
            if(!oPorts.contains(airport)){ 
            	oPorts.add(airport);
            }
        }

        // same process with passenger IDS
        for(String orgAirport : oPorts){
            ArrayList<String> passengerIDs = new ArrayList<>(); //instantiatw nwq list of ids
            for(int i = 0; i < job1PassList.size(); i++){ //iterate through the list of maps
                if(orgAirport.equals(job1PassList.get(i).getKey())){ //if the origin aipport matches the ones from the index
                    passengerIDs.add(job1PassList.get(i).getValue()); //add this passenger id to the list
                }
                job1Hashmap.put(orgAirport,passengerIDs); //put both the list of ids and the origin aiports list into the hashmap
            }
        }
    }

    /***
     * return the job 1 hashmap
     * @return
     */
    public HashMap<String, ArrayList<String>> getJob1Hashmap() {
        return job1Hashmap;
    }
    

    public void mergeJob2Hashmap(List<Map.Entry<String,Flight>> flightList){
        ArrayList<String> fIds = new ArrayList<>(); //create new list of flight IDS

        //add all of the flight ID's from the passed in list into the new arrayList
        for(Map.Entry<String,Flight> map: flightList ){
            String everyFlightId = map.getKey();
            if(!fIds.contains(everyFlightId)){ //only add if it doesn't already contain it
            	fIds.add(everyFlightId);
            }
        }
        
        //iterate trhough all the flight IDS
        for(String id : fIds){
            ArrayList<Flight> profiles = new ArrayList<>(); //create new list of profiles
            for (Map.Entry<String,Flight> map: flightList ){ //iterate through every mapped flight
                if(id.equals(map.getKey())){ //condtional if the flight matches
                	profiles.add(map.getValue()); //add the profile to the key
                }
                job2Hashmap.put(id,profiles); //push the profiles and the flight keys onto the hashmap
            }
        }
    }

    /***
     * return the job 2 hashmap
     * @return
     */
    public HashMap<String, ArrayList<Flight>> getJob2Hashmap() {
        return job2Hashmap;
    }
}
