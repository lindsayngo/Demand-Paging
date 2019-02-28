import java.util.*;
import java.io.*;

public class LRU extends Lab4 {
	//we want to use extends because we need all the input
	Frame currentf = new Frame();
	public void execute(ArrayList <Frame> frametable, Scanner random) {
		ArrayList <Frame> framet = new ArrayList <Frame> ();
		//making a copy of the frametable
		//so we do not mess up the original frametable for other replacement algorithms
		framet.addAll(frametable);
		
		HashMap <Process, Integer> done= new HashMap <Process, Integer> ();
		int doneprocs = 0;
		int time = 1;
		int quantum = 3;
		int totalevict = 0;
		
		//we must make sure that we stop executing once all processes are DONE
		while(doneprocs < processes.size()){
			
		for(int i = 0; i < processes.size(); i ++) {
			Process cp = processes.get(i); //process cp stands for current process
			for(int j = 0; j < quantum; j++) {
					 
				//time += 1;//simulate run time
				
				if(cp.done == true) continue;
				
				//we only check the process IF it is not yet done
				if(cp.done == false) {	
					
			//-------------------------DEFINE PAGE NUMBER--------------------------------
					//first define the page number and determine the word the process references at the pagenumber
					int pagenumber = cp.word / pagesize;
					System.out.println(cp.processID + " references word " + cp.word + "(page " + pagenumber + ") at time " + time);
					
			//-------------------------PAGE FAULT CHECK--------------------------------
					//we then check if there is a page fault for the current process
					//by going through all the frames in the frameTable and checking 
					//if the referenced page is currently in memory (in the frame table)
					
					
					boolean alreadyfound = false; //a page fault has not been found
					
					for(int f = 0; f < framet.size(); f++) {
						//if a frame's process and page do NOT match the current process and page, a page fault occurs
						if(alreadyfound == false && framet.get(f).currentProcID == cp.processID && framet.get(f).currentPageID == pagenumber) {
							//we know there is NO page fault and can set the current frame
							alreadyfound = true;
							currentf = framet.get(f);
						}
						
					}
					
					
					
					if(alreadyfound == false) {
						
						//we have 2 options if there is a pagefault: find a free frame OR replace a frame
						//-------------FREE FRAME --> PUT THE PAGE INTO THE FREE FRAME
						//we use method freeFrameCheck so that it returns the ACTUAL frame 
						//in the frame table and not some copy of it
						//we want the changes to show up in the frame table itself
						if(freeFrameCheck(framet) != null) {
							freeFrameCheck(framet).currentProcID = cp.processID;
							freeFrameCheck(framet).currentPageID = pagenumber;
							//we can load the process cp
							//set the loadtime = runtime
//							cp.loadtime = time;
							System.out.println("Fault, using free frame " + freeFrameCheck(framet).frameID);
							currentf = freeFrameCheck(framet);
							
						}
	
						//if no new frame has been chosen, it means there were
						//-------------NO FREE FRAMES --> USE REPLACEMENT ALGORITHM	
						else {
							currentf = lru(framet);
							System.out.println("Fault, using lru");
							System.out.println("Evicting page " + currentf.currentPageID + " of "+ currentf.currentProcID + " from Frame " + currentf.frameID);
							//we need to EVICT the page from the current frame
							//processes.get(currentf.currentProcID - 1).evict(time, currentf.frameID);
						processes.get(currentf.currentProcID - 1).evictiontime++;
						processes.get(currentf.currentProcID - 1).residency += (time - processes.get(currentf.currentProcID-1).loadtime[currentf.frameID]);
							totalevict += 1;
							//cp.evictiontime = time;
							//cp.residency = cp.evictiontime - cp.loadtime[currentf.frameID];
						}
						
						currentf.currentProcID = cp.processID;
						currentf.currentPageID = pagenumber;
						currentf.free = false;
						
						cp.loadtime[currentf.frameID] = time;
						cp.totalpagefaults += 1;
						
					}//end of if there is a page fault
					
					else {
						System.out.println("Hit in frame " + currentf.frameID);
					}
					
				}//end of if cp.done == false
				
				cp.completed ++;
				//-----------CHECK IF PROCESS IS NOW DONE-------------
				if(cp.completed == numrefsperproc) {
					cp.done = true;
//					if(done.containsKey(cp) == false) {
//						done.put(cp, 0);
						doneprocs += 1;
					//}
				}
				
				//-----------GIVE THE PROCESS A NEW WORD--------------
				int ranint = random.nextInt();
				System.out.println(cp.processID + " is using random number " + ranint);
				double doub = ranint / (Integer.MAX_VALUE + 1d);//from spec (4)
				//below is from the spec: if the current word referenced by a process is w, then
				//the next reference by the process is to the word with address...
				//w+1 mod processsize with prob A
				if(doub < cp.A) { cp.word = (cp.word + 1) % processsize; }
				//w-5 mod processsize with prob B
				else if (doub < (cp.A + cp.B)) { cp.word = (cp.word - 5 + processsize) % processsize; }
				//w+4 mod processsize with prob C
				else if (doub < (cp.A + cp.B + cp.C)) { cp.word = (cp.word + 4) % processsize; }
				//random value in 0...processsize-1 each with probability (1-A-B-C)/S
				else { ranint = random.nextInt(); cp.word = ranint % processsize; }
				
				//----------UPDATE THE FRAME'S LAST USED TIME-----------
				currentf.lastusedtime = time;
				
				time ++;
				
			}//end of quantum for loop
			
		}//end of going through all the processes
		
		}//end of WHILE loop
		int overallfaults = 0;
		double overallresidency = 0;
		System.out.println();
		for(int p = 0; p < processes.size(); p++) {
			System.out.print("\nProcess " + processes.get(p).processID + " had " + processes.get(p).totalpagefaults + " faults and");
			overallfaults += processes.get(p).totalpagefaults;
			
			if(processes.get(p).evictiontime == 0) { System.out.println(" with no evictions, the average residency is undefined "); }
			else {
				double r = processes.get(p).residency;
				double e = processes.get(p).evictiontime;
				double div = r/e;
				//System.out.println(div);
				
				System.out.println(" "+  div + " average residency");
				overallresidency += processes.get(p).residency;
			}
		}
		System.out.println();
		System.out.println("The total number of faults is " + overallfaults);
		
		if(totalevict == 0) {
			System.out.println("With no evictions, the overall average residency is undefined");
		}
		
		else {
			System.out.println("The overall residency is " + (double) (overallresidency / totalevict));
		}
	}//end of execute method


	public Frame freeFrameCheck(ArrayList <Frame> framet) {
		Frame current = new Frame();
		for(int g = framet.size()-1; g >= 0; g--) {
			current = framet.get(g);
			if(current.free == true) {
				return framet.get(g);
				//a new FREE frame has been found and we can stop
				//checking the rest of the frames
			}
		}
		return null;
	}
	
	public Frame lru(ArrayList <Frame> framet) {
		//this is the replacement algorithm Last Recently Used
		//we want to evict the last recently used frame contents
		Frame starter = framet.get(0);
		//start from the beginning
		for(int g = 0; g < framet.size(); g++) {
			//keep comparing the current starting frame with all other frames in the frame table
			//to see which one's last usage is higher
			//so we can evict the highest last usage time (meaning the frame used longer ago/the older frame)
			if(starter.lastusedtime > framet.get(g).lastusedtime) {
				starter = framet.get(g);
			}
		}
		return starter;
	}
	

	
}