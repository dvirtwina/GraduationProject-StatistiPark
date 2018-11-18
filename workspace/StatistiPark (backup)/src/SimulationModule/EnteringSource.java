package SimulationModule;

import java.time.LocalDateTime;
import Database.DButils;
import MainModule.Controller;
import eduni.simjava.*;
import eduni.simjava.distributions.*;

public class EnteringSource extends Sim_entity {
	private Sim_port out;
	private Sim_negexp_obj delay; // Exponential Distribution variable generating the mean time of car-entering.
	
	private String currentPhase;
	private int numOfParkingSpots;

	public EnteringSource(String name, double carsEnteringMean, String currentPhase, int numOfParkingSpots) {
		super(name);
		this.currentPhase = currentPhase;
		this.numOfParkingSpots = numOfParkingSpots;
		
		out = new Sim_port("Out");
		add_port(out);
		for (int i = 1; i<=numOfParkingSpots; i++) {
        	add_port(new Sim_port("Out"+i));
        }
		
		// Create the source's distribution and add it.
		delay = new Sim_negexp_obj("Delay", carsEnteringMean);
		add_generator(delay);
	}

	public void body() {
		if (currentPhase.equals(SimulationEngine.CONFIGURATION_PHASE)) {
			for (int ps = 1; ps <= numOfParkingSpots; ps++) {
				LocalDateTime start = Controller.getPreviousSimulationStartTime();
				LocalDateTime end = start.plusMinutes((long)Controller.getPreviousSimulationDuration());
				double remainingTimeToProcess = DButils.getRemainingTimeOfParkingSpot("ParkingSpot"+ps, start, end);
				if (remainingTimeToProcess > 0) {
					sim_schedule("Out"+ps, 0.0, SimulationEngine.EVENT_TAG_CONTINUED, remainingTimeToProcess);
				}
			}
			runStandardScheduling();
		}
		else { // RUNNING_SIM_PHASE
			runStandardScheduling();
		}
	}
	
	private void runStandardScheduling() {
		while (Sim_system.running()) { 
			sim_schedule(out, 0.0, SimulationEngine.EVENT_TYPE_CAR_ENTER);
			// Sample the distribution
			sim_pause(delay.sample());
		}
	}
}
