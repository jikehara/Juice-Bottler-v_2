/**
 * Class pulled from Nate Williams: https://github.com/YogoGit/JuiceBottler
 * 
 * @author Joseph Ikehara significant changes made by Joseph, but there is still
 *         some of Nate's code ~ 70% my code Some edits from Nate Williams
 */

public class Worker implements Runnable {

	private final Plant plant;
	// tells the worker which task it performs: fetch, peel, juice, bottle, or
	// finish
	private Orange.State job;
	private AssemblyLine aline;
	private volatile boolean timeToWork;
	private Thread t;

	/**
	 * constructor assigns a worker to a plant and gives him a role depending on the
	 * state of the orange when he is spawned
	 * 
	 * @param plant
	 * @param os
	 */
	public Worker(Plant plant, Orange.State os) {
		this.plant = plant;
		this.job = os;

		switch (job) {
		case Fetched:
			aline = new AssemblyLine(null, plant.getOrangesFetched());
			break;
		case Peeled:
			aline = new AssemblyLine(plant.getOrangesFetched(), plant.getOrangesPeeled());
			break;
		case Squeezed:
			aline = new AssemblyLine(plant.getOrangesPeeled(), plant.getOrangesJuiced());
			break;
		case Bottled:
			aline = new AssemblyLine(plant.getOrangesJuiced(), plant.getOrangesBottled());
			break;
		case Processed:
			aline = new AssemblyLine(plant.getOrangesBottled(), plant.getOrangesFinished());
			break;
		}
		setTimeToWork(true);
		t = new Thread(this, "PlantNum[" + plant.plantNum + "][" + os.ordinal() + " ]");
		t.start();
	}

	/**
	 * takes an orange from the "in" arraylist and pushes it to the "out" queue
	 */
	public void moveOrange() {
		if (job.Fetched != null)
			aline.fetchOrange();
		else
			aline.processOrange();
	}

	/**
	 * takes an orange and puts it in next step of state
	 * 
	 * @param o
	 */
	public void processOrange(Orange o) {
		o.runProcess();
	}

	@Override
	public void run() {
		while (timeToWork) {
			moveOrange();
		}
	}

	/**
	 * should end the run() method
	 */
	public void stopWorker() {
		setTimeToWork(false);
	}

	/**
	 * should join all the threads back together
	 */
	public void waitToStop() {
		try {
			t.join();
		} catch (InterruptedException e) {
			System.err.println(t.getName() + " stop malfunction");
		}
	}

	/**
	 * check whether the program is running or not
	 * 
	 * @return
	 */
	public boolean isTimeToWork() {
		return timeToWork;
	}

	/**
	 * begin or end work for the worker
	 * 
	 * @param timeToWork
	 */
	public void setTimeToWork(boolean timeToWork) {
		this.timeToWork = timeToWork;
	}
}
