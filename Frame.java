
public class Frame {
	int frameID = 0;
	int currentProcID; //represents the frame's current process
	int currentPageID; //represents the frame's current page
	boolean free = true; 	   //represents if the frame is currently free
	int lastusedtime = 0;
	Frame(){
		
	}
	
	public Frame(int ID) {
		frameID = ID;
		//meaning there are no current process or pages
		currentProcID = 0;
		currentPageID = 0;
		//meaning the frame is free upon initiation/the frametable is empty
		free = true;
		lastusedtime = 0;
	}
	
}
