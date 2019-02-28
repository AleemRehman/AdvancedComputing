package AC;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * Job controller, this is where all the jobs will be handled
 * @author Aleem Ur-Rehman
 *
 */

public class JobController {
	
	//create variables for job
    int j = 1;
    int threads = 4;
    private ArrayList<Passenger> passengers;
    private ArrayList<Airport> airports;

    /***
     * Constructor for a job, passing in the two data sets from the buffer and the current job
     * @param airports
     * @param passengers
     * @param currentJob
     */
    public JobController(ArrayList<Airport> airports, ArrayList<Passenger> passengers, int currentJob){
    	this.airports = airports;
    	this.passengers = passengers;
    	j = currentJob;
    }
    
    /***
     * output jobs 1 and 3 to file, this needs serious cleaning to make it readable
     * @param data
     * @param jobId
     */
    public void outputJob1and3(ArrayList<Reduce> data, int jobId)
    {
        List<Map.Entry<String,Integer>> tempList = new ArrayList<>();

        // get each outputJob1 from reducers
        for(Reduce reducer : data) {
        	tempList.add(reducer.getJob1()); //these may cause overflow errors down the line
        }
        System.out.print(tempList); // print reducers combined outputJob1

        // write combined outputJob1 to CSV
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter("Objective"+ jobId +".csv")); //new file based on job
            StringBuilder string = new StringBuilder();
            //TODO: NEEDS TO BE UNIQUE FOR EACH JOB
            string.append("Key"); //these titles HAHAH
            string.append(",");
            string.append("Value");
            string.append("\n");
            
            //iterate through the object
            for (Map.Entry<String,Integer> object : tempList) {
            	string.append(object.getKey()); //get the key
            	string.append(","); //delimit
            	string.append(object.getValue()); //get the value
            	string.append("\n"); //newline
            }
            buffer.write(string.toString());
            buffer.close();
        }
        catch (java.io.IOException ex){ //catch any lurking exceptions
            System.out.println(ex); //print to cosnsole
        }
    }

    /***
     * output job 2 to the file
     * @param data
     */
    public void outputJob2(ArrayList<Reduce> data)
    {
        StringBuilder string = new StringBuilder(); //create new string builder -- can this be moved out of function scope to save memory??
        //for every reduced object in the list
        for(Reduce r : data) { //iterate through the objects
                for(ArrayList<String> f : r.getJob2()){ //get all of the flights from the reduced content
                    System.out.print(f + "\n"); // print each flight to console
                    for(String value : f){
                    	string.append(value); //print profile
                    	string.append(","); //append new attribute
                    }
                    string.append("\n");// end
                }
        }
        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter("Objective2.csv")); //write to the file
            buffer.write(string.toString()); 
            buffer.close();
        }
        catch (java.io.IOException e){ //catch any lurking exceptions
            e.printStackTrace();
        }
    }

    /***
     * Split the chunks of data for the threads
     * @param passData
     * @param noOfChunks
     * @return
     */
    public ArrayList<ArrayList<Passenger>> splitChunks(ArrayList<Passenger> passnegers, int chunks)
    {
        int cSize = passnegers.size() / chunks; //determine each chunk size based on the size of the data - better to assign dynamically
        ArrayList<ArrayList<Passenger>> data = new ArrayList<ArrayList<Passenger>>(); //create the data to be returned
        
        //initiate size of the indexes
        int end = cSize;
        int start = 0;

        ///iterate through chunks
        for (int i = 0; i < chunks; i++){
            ArrayList<Passenger> passengersT = new ArrayList<Passenger>(passnegers.subList(start, end)); //create sub lists of data based on the size of indexes
            data.add(passengersT); //add the passenger data
            start = start + cSize;
            end = end + cSize;
        }
        return data;
    }
    
    /***
     * Running the central job, this links to the mapper and the reducer so we can send data in from one place - keeping OOP and seperation of process in mind
     */
    public void CentralJob() {
    	//TODO: This needs to be more generic
        ArrayList<ArrayList<Passenger>> dataChunks = splitChunks(passengers, threads); //begin by splitting the data into chunks, in this case we want to split passengers based on threads

        //lists to go to reducer
        ArrayList<Mapper> mapper = new ArrayList<>(); //instantiate mapper list
        ArrayList<Thread> mThread = new ArrayList<>(); //instantiate mapped threads list
        ArrayList<Merge> merger = new ArrayList<>(); //instatiate the merger for later

        //create thread for the map
        for(int th = 0; th < threads; th++){
            ArrayList<Passenger> passengers = dataChunks.get(th); //get all the passenger list from the data chunks according to the thread
            String tName = String.format("map ", th); //instiate a thread name --used for debugging
            Mapper map = new Mapper(passengers, airports, j, tName); //create the new mapper
            Thread mappedThread = new Thread(map, tName); //creat the new mapped thread
            //start the thread
            mappedThread.start();
            mapper.add(map); //add the map to the mapper list 
            Merge merge = new Merge(j); //create merge for the job
            mThread.add(mappedThread); //add mapped thread to list of mapped threads
            merger.add(merge); //add the merge to the list of merges
        }

        //MAPPING AND MERGING
        //iterate through every thread
        //TODO: remove size function from loop 
        for(int i = 0; i < mThread.size(); i++){
            //TRY THE MAPPING
        	try {
            	mThread.get(i).join();
               switch (j){
               		//OBJECTIVE 1
                   case 1:
                	   List<Entry<String, String>> tempMap = mapper.get(i).getJob1Map(); //mapping ran on the first job
                	   merger.get(i).mergeJob1Hashmap(tempMap); //merge the hashmap
                	//OBJECIVE 2
                   case 2:
                	   List<Entry<String, Flight>> tempMap2 = mapper.get(i).getJob2Map(); //mapping
                	   merger.get(i).mergeJob2Hashmap(tempMap2);
                	//OBJECTIVE 3
                   case 3:
                	   List<Entry<String, String>> tempMap3 = mapper.get(i).getJob1Map(); //mapping ran on the last job
                	   merger.get(i).mergeJob1Hashmap(tempMap3); //merge the hashmap
//                   case 4:
               }

            }
        	//catch any interrputed exceptions
            catch (InterruptedException ex){
                System.out.println(ex); //print the error to the console
            }
        }

        //REDUCING STAGE
        ArrayList<Reduce> reducer = new ArrayList<>(); //CREATE REDUCER
        switch (j){
        	//OBJECTIVE 1: Determine the number of flights from each airport; include a list of any airports not used.
            case 1:
                ArrayList<String> codes = new ArrayList<>(); //create a list for the primary key
                // serach through all the eiports to derive all keys
                for (Airport airport : airports)
                {
                	//get the codes if they dont already exist in the codes list
                    if(!codes.contains(airport.getAirportCode())){
                    	codes.add(airport.getAirportCode());
                    }
                }
                //reduce every key with the merge data
                for(String tempAirport : codes){
                    Reduce r = new Reduce(tempAirport, merger, j); //new reduce object
                    reducer.add(r); //add the reducer
                }
                outputJob1and3(reducer,1); //output the job with the job number and the reduced data
                break;

            //OBJECTIVE 2: Create a list of flights based on the Flight id, this output should include the passenger Id, relevant IATA/FAA codes, the departure time, the arrival time (times to be converted to HH:MM:SS format), and the flight times.
            case 2:
                ArrayList<String> fIds = new ArrayList<>(); //flight ids for the profile primary key
                for(Passenger p: passengers){ //go through the passngers by using flight ID
                    if(!fIds.contains(p.getFlightId())){
                    	fIds.add(p.getFlightId()); //add the flight ID to the list
                    }
                }
                //REDUCE PRIMARY KEYS for flightID
                for(String flightId : fIds) {
                    Reduce reduce = new Reduce(flightId, merger, j);
                    reducer.add(reduce);
                }
                outputJob2(reducer); //output the jobs
                break;

            case 3: //OBJECTIVE 3: Calculate the number of passengers on each flight
            	//creat time list for flight ids
                ArrayList<String> jobList3 = new ArrayList<>();
                for(Passenger P: passengers){ //sift through passengers to gain access to all flight IDS
                    if(!jobList3.contains(P.getFlightId())){ 
                    	jobList3.add(P.getFlightId()); //susbsequently add flight ID to list
                    }
                }
                //REDUCE PRIMARY KEYS
                for(String fId : jobList3){
                    Reduce reduce = new Reduce(fId, merger, 1); //using reducer from objective 1 --save on duplicating code
                    reducer.add(reduce);
                }
                outputJob1and3(reducer,3); //output job
                break;
//                
                //TODO: ALL OUTPUT METHODS NEED TO BE GENERIC
//            case 4: 
//            	ArrayList<String> flightIdsJobFinal = new ArrayList();
//            	for(Passenger passdata : passengers){
//            		
//            	};
            default:
                break;

        }
    }

}
