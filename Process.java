
public class Process extends Lab4{
	int processID;
	int totalframes;
	double A, B, C;
	boolean done; //keeps track of whether the process is finished
	int word;
	int []loadtime;//to keep track of load time of each frame, so the index is the FRAME ID
	int completed;//used to keep track of number of references completed 
	int totalpagefaults;
	int evictiontime;
	int residency;
	
	public Process(int ID, int frames) {
		//remember, ID is always greater than 0
		processID = ID;
		
		totalframes = frames;
		
		done = false;
		
		//set default word to (111 * process ID) % process size
		//as specified in spec Notes (3)
		//keep in mind that processID is always going to be > 0
		word = (111 * processID) % processsize;
		loadtime = new int[frames];
		completed = 0;
		totalpagefaults = 0;
		evictiontime = 0;
		residency = 0;
	}
	

}
