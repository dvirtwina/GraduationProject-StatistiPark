package SimulationEngine;

import java.time.LocalDateTime;

public class SimStats {
	
	private LocalDateTime start;
	/**
	 * Duration of the simulation, in minutes.*/
	private int duration;
	private double rateCarEnter;
	private double rateCarExit;

	public SimStats(LocalDateTime start, int duration, double rateCarEnter, double rateCarExit) {
		this.start = start;
		this.duration = duration;
		this.rateCarEnter = rateCarEnter;
		this.rateCarExit = rateCarExit;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public int getDuration() {
		return duration;
	}

	public double getRateCarEnter() {
		return rateCarEnter;
	}

	public double getRateCarExit() {
		return rateCarExit;
	}

}
