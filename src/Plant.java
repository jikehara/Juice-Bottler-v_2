import java.util.ArrayList;
import java.util.List;

/**
 * Taken from Nate Williams: https://github.com/YogoGit/JuiceBottler
 * Modifications from Joseph Ikehara ~100% my code
 */

public class Plant implements Runnable {
	// How long do we want to run the juice processing
	public static final long PROCESSING_TIME = 5 * 1000;
	// Number of plants and Oranges per bottle
	public static final int ORANGES_PER_BOTTLE = 4;
	private static final int NUM_PLANTS = 3;

	private static final int NUM_LINES = 1;
	private static final int WORKERS_PER_PLANT = 5;
	private static final int NUM_WORKERS = NUM_LINES * WORKERS_PER_PLANT;

	// assembly lines for each queue
	private final ArrayList<Orange> orangesFetched = new ArrayList<Orange>();
	private final ArrayList<Orange> orangesPeeled = new ArrayList<Orange>();
	private final ArrayList<Orange> orangesJuiced = new ArrayList<Orange>();
	private final ArrayList<Orange> orangesBottled = new ArrayList<Orange>();
	private final ArrayList<Orange> orangesFinished = new ArrayList<Orange>();

	private final Worker[] workers;
	public final int plantNum;
	private volatile int orangesProcessed;

	public static void main(String[] args) {
		// Startup the plants
		Plant[] plants = new Plant[NUM_PLANTS];
		for (int i = 0; i < NUM_PLANTS; i++) {
			plants[i] = new Plant(i + 1);
			plants[i].startPlant();
		}

		// Give the plants time to do work
		delay(PROCESSING_TIME, "Plant malfunction");

		// Stop the plant, and wait for it to shutdown
		for (Plant p : plants) {
			p.stopPlant();
			p.waitToStop();
		}

		// Summarize the results
		int oranges = 0;
		int bottles = 0;
		int waste = 0;
		for (Plant p : plants) {
			oranges += p.getOranges();
			bottles += p.getBottles();
			waste += p.getWaste();
		}
		System.out.println("Total processed = " + oranges);
		System.out.println("Created " + bottles + ", wasted " + waste + " oranges");
	}

	// simulates the plant running
	private static void delay(long time, String errMsg) {
		long sleepTime = Math.max(1, time);
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			System.err.println(errMsg);
		}
	}

	// constructor initializes plants with their work threads
	Plant(int plantNum) {
		this.plantNum = plantNum;
		workers = new Worker[NUM_WORKERS];
		orangesProcessed = 0;
	}

	public void startPlant() {
		int i = 0;
		for (Orange.State os : Orange.State.values()) {
			workers[i] = new Worker(this, os);
			i++;
		}
	}

	// Ends run() method
	public void stopPlant() {
		for (Worker worker : workers) {
			worker.stopWorker();
		}
	}

	// Stops all the worker threads
	public void waitToStop() {
		for (Worker worker : workers) {
			worker.waitToStop();
		}
	}

	// This is run once per worker
	public void run() {
		System.out.print(Thread.currentThread().getName() + " Processing oranges");
		System.out.println("");
	}

	/**
	 * getters for each ArrayList type
	 * 
	 * @return ArrayList<Orange>
	 */
	public ArrayList<Orange> getOrangesPeeled() {
		return orangesPeeled;
	}

	public ArrayList<Orange> getOrangesJuiced() {
		return orangesJuiced;
	}

	public ArrayList<Orange> getOrangesBottled() {
		return orangesBottled;
	}

	public ArrayList<Orange> getOrangesFinished() {
		return orangesFinished;
	}

	public void completeOrange(Orange o) {
		// Do one final check on the orange
		if (o.getState() == Orange.State.Bottled) {
			o.runProcess();
			orangesProcessed++;
		}
	}

	/**
	 * getters for all the primitive types for plants
	 * 
	 * @return
	 */
	public int getOranges() {
		orangesProcessed = orangesFinished.size();
		return orangesProcessed;
	}

	public int getBottles() {
		return orangesProcessed / ORANGES_PER_BOTTLE;
	}

	public int getWaste() {
		return orangesProcessed % ORANGES_PER_BOTTLE;
	}

	public ArrayList<Orange> getOrangesFetched() {
		return orangesFetched;
	}
}
