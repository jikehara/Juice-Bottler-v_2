/**
 * AssemblyLine class taken from Nate Williams: https://github.com/YogoGit/JuiceBottler
 * @author Joseph Ikehara
 * This program, out of all in JuiceBottler v_2, is the most changed from Nate William's source.
 * 100% my code
 */
import java.util.ArrayList;

public class AssemblyLine {
	
	// queues that orange is pulled from and pushed to
    private final ArrayList<Orange> in;
    private final ArrayList<Orange> out;

    // constructor initializes a set of empty arraylists for the oranges at various states
    AssemblyLine(ArrayList<Orange> in, ArrayList<Orange> out) {
    	this.in = in;
    	this.out = out;
    }
    
    //special case for fetching new oranges - no "in" arraylist
    public void fetchOrange() {
    	out.add(new Orange());
    }
    
    // process the orange by moving it from one 
    public void processOrange() {
    	Orange o = in.get(0);
    	o.runProcess();
    	out.add(o);
    }
}
