import java.util.*;
import java.io.*;

public class Lab4 {
	
	static ArrayList <Process> processes = new ArrayList <Process> ();
	
	static int machinesize, pagesize, processsize, jobmixnum, numrefsperproc;
	
	public static void main(String[] args) throws FileNotFoundException{
		
		Scanner random = new Scanner(new FileReader("random-numbers"));
		
		machinesize = Integer.parseInt(args[0]);
		pagesize = Integer.parseInt(args[1]);
		processsize = Integer.parseInt(args[2]);
		jobmixnum = Integer.parseInt(args[3]);
		numrefsperproc = Integer.parseInt(args[4]);
		String algo = args[5];
		
		System.out.println("The machine size is " + machinesize);
		System.out.println("The page size is " + pagesize);
		System.out.println("The process size is " + processsize);
		System.out.println("The job mix number is " + jobmixnum);
		System.out.println("The number of references per process is " + numrefsperproc);
		System.out.println("The replacement algorithm is " + algo);
		
		//Create the frame table
		//each frame in the frame table has a current page and a current process
		int frames = machinesize/pagesize;
		ArrayList <Frame> frametable = new ArrayList <Frame> ();
		System.out.println("\nTotal number of frames: " + frames);
		for(int i = 0; i < frames; i++) {
			Frame f = new Frame(i);
			frametable.add(f);
		}
		
		//Creating the set of processes depending on the job mix number
		
		if(jobmixnum == 1) {
			//create ONE new process with ID = 1
			//the simplest, fully sequential case
			Process process = new Process(1, frames);
			process.A = 1; process.B = 0; process.C = 0;
			processes.add(process);
		}
		else if(jobmixnum == 2) {
			for(int i = 0; i < 4; i++) {
				Process process = new Process(i+1, frames);
				process.A = 1; process.B = 0; process.C = 0;
				processes.add(process);
			}
		}
		else if(jobmixnum == 3) {
			//fully random references
			for(int i = 0; i < 4; i++) {
				Process process = new Process(i+1, frames);
				process.A = 0; process.B = 0; process.C = 0;
				processes.add(process);
			}
		}
		else if(jobmixnum == 4) {
			Process p1 = new Process(1, frames);
			p1.A = .75; p1.B = .25; p1.C = 0;
			processes.add(p1);
			Process p2 = new Process(2, frames);
			p2.A = .75; p2.B = 0; p2.C = .25;
			processes.add(p2);
			Process p3 = new Process(3, frames);
			p2.A = .75; p2.B = .125; p2.C = .125;
			processes.add(p3);
			Process p4 = new Process(4, frames);
			p2.A = .5; p2.B = .125; p2.C = .125;
			processes.add(p4);
		}
		
		if(algo.equals("lru")) {
			System.out.println("-----LRU-----");
			LRU lru = new LRU();
			lru.execute(frametable, random);
		}
		
		else if(algo.equals("random")) {
			System.out.println("-----RANDOM-----");
			RANDOM ran = new RANDOM();
			ran.execute(frametable, random);
		}
		
		else if(algo.equals("lifo")) {
			System.out.println("-----LIFO-----");
			LIFO lifo = new LIFO();
			lifo.execute(frametable, random);
		}

	}//end of main method

}
