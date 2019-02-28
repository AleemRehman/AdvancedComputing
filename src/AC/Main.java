package AC;

public class Main {
    public static void main(String[] args) throws Exception{
    	//instantiate data reader
        DataReader reader = new DataReader();
        reader.read();
        //run all jobs
        JobController job1 = new JobController(reader.getAirports(), reader.getPassengers(), 1);
        job1.CentralJob();
        JobController job2 = new JobController(reader.getAirports(), reader.getPassengers(), 2);
        job2.CentralJob();
        JobController job3 = new JobController(reader.getAirports(), reader.getPassengers(), 3);
        job3.CentralJob();
    }

}
