package SimulationEngine;

import java.time.LocalDateTime;
import java.time.Month;

import MainModule.Controller;
import eduni.simjava.*;

public class SimulationEngine {
	public static final int EVENT_TYPE_CAR_ENTER = 0;
	public static final int EVENT_TAG_CONTINUED = 1; 
	/** A phase for running a single simulation or the first in a series simulations.*/
	public static final String RUNNING_SIM_PHASE = "running"; 
	/** A phase for configuring and sewing multiple simulations.*/
	public static final String CONFIGURATION_PHASE = "configuration";

	private double rateCarEnter;
	private double rateCarExit;
	/** Assumption: a time unit equals to 1 minute at realistic time. */
	private double totalMinutesOfSimulation;
	private LocalDateTime simulationStart;
	private int numOfParkingSpots;

	public SimulationEngine(int numOfParkingSpots, double totalMinutesOfSimulation, double rateCarEnter, double rateCarExit, String startingPhase, LocalDateTime startingDateTime) {
		this.numOfParkingSpots = numOfParkingSpots;
		this.totalMinutesOfSimulation = totalMinutesOfSimulation;
		this.rateCarEnter = rateCarEnter;
		this.rateCarExit = rateCarExit;
		this.simulationStart = startingDateTime;
		
		Sim_system.initialise();
		
        EnteringSource source = new EnteringSource("Source", rateCarEnter, startingPhase, numOfParkingSpots);
        
        Gate gate = new Gate("Gate", rateCarExit, numOfParkingSpots);
        
        // Create parking spots
        for (int i = 1; i<=numOfParkingSpots; i++) {
        	new ParkingSpot("ParkingSpot"+i);
        }
        
        // Linking Source's ports
        Sim_system.link_ports("Source", "Out", "Gate", "In");  
        for (int i = 1; i<=numOfParkingSpots; i++) {
        	Sim_system.link_ports("Source", "Out"+i, "ParkingSpot"+i, "In");
        }
        
        // Linking Gate's ports
        for (int i = 1; i<=numOfParkingSpots; i++) {
        	Sim_system.link_ports("Gate", "Out"+i, "ParkingSpot"+i, "In");
        }
        
        Sim_system.set_termination_condition(Sim_system.TIME_ELAPSED, totalMinutesOfSimulation, true);
        
        Sim_system.run();
        
        // ONLY FOR DEBUGING. SHOULD BE REMOVED!
        //Controller.DBcon.get_ParkingSpotState_Data();
        }

	public int getNumOfParkingSpots() {
		return numOfParkingSpots;
	}

	public LocalDateTime getSimulationStart() {
		return simulationStart;
	}

	public double getTotalMinutesOfSimulation() {
		return totalMinutesOfSimulation;
	}

}
