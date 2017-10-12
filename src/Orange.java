/**
 * Pulled from Nate Williams: https://github.com/YogoGit/JuiceBottler
 * 
 * @author Joseph Ikehara This code is nearly identical to Nate William's Orange
 *         codes provided in class.
 */

public class Orange {
	public enum State {
		Fetched(15), Peeled(38), Squeezed(29), Bottled(17), Processed(1);

		private static final int finalIndex = State.values().length - 1;

		final int timeToComplete;

		State(int timeToComplete) {
			this.timeToComplete = timeToComplete;
		}

		State getNext() {
			int currIndex = this.ordinal();
			if (currIndex >= finalIndex) {
				throw new IllegalStateException("Already at final state");
			}
			return State.values()[currIndex + 1];
		}
	}

	private State state;

	/**
	 * constructor sets each new orange at the state "Fetched" and runs for the
	 * amount of time it should take to fetch an Orange
	 */
	public Orange() {
		state = State.Fetched;
		doWork();
	}

	public State getState() {
		return state;
	}

	/**
	 * gets next state of the orange and runs the amount of time it takes to change
	 * states
	 */
	public void runProcess() {
		// Don't attempt to process an already completed orange
		if (state == State.Processed) {
			throw new IllegalStateException("This orange has already been processed");
		}
		state = state.getNext();
		doWork();
	}

	/**
	 * waits for the amount of time for an orange to change state
	 */
	private void doWork() {
		try {
			Thread.sleep(state.timeToComplete);
		} catch (InterruptedException e) {
			System.err.println("Incomplete orange processing, juice may be bad");
		}
	}
}
