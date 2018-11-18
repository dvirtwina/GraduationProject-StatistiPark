package Objects;

import java.time.LocalDateTime;

public class Interval {
	private LocalDateTime start;
	private int duration;
	private double enterRate;
	private double exitRate;
	private int ID;
	
	public Interval(LocalDateTime start, int duration, double enterRate, double exitRate, int id) {
		super();
		this.start = start;
		this.duration = duration;
		this.enterRate = enterRate;
		this.exitRate = exitRate;
		this.ID = id;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public int getDuration() {
		return duration;
	}

	public double getEnterRate() {
		return enterRate;
	}

	public double getExitRate() {
		return exitRate;
	}

	public int getID() {
		return ID;
	}
	
	

}
