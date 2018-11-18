package SimulationModule;

import java.time.LocalDateTime;

import MainModule.Controller;
import eduni.simjava.*;
import eduni.simjava.distributions.*;

public class Gate extends Sim_entity {
	private Sim_port in;
	private Sim_random_obj prob;// Random probability for each parking-space to
								// be chosen
	private Sim_negexp_obj processTime; // Exponential Distribution variable
										// that generates the time for which a
										// ParkingSpot to be unavailable.
	private int numOfParkingSpots;

	public Gate(String name, double processTimeRate, int numOfParkingSpots) {
		super(name);
		this.numOfParkingSpots = numOfParkingSpots;
		in = new Sim_port("In");
		add_port(in);
		for (int i = 1; i<=numOfParkingSpots; i++) {
        	add_port(new Sim_port("Out"+i));
        }
		// Create the gate's distribution and generator and add them
		prob = new Sim_random_obj("Probability");
		processTime = new Sim_negexp_obj("ProcessTime", processTimeRate);
		add_generator(prob);
		add_generator(processTime);
	}

	public void body() {
		while (Sim_system.running()) {
			Sim_event e = new Sim_event();
			sim_get_next(e);
			// Sample the distribution
			sim_completed(e);

			// Get the next probability and process-time
			double spotUnavailbeTime = Math.ceil(processTime.sample());
			double p = prob.sample();

			// Insert activity do DB
			double elapsedTimeUnits = Sim_system.sim_clock();
			LocalDateTime realisticDateTime = Controller.getSimulationStartTime();
			MainModule.Controller.DBcon.insertInto_GateActivity_CarEnter(1, realisticDateTime.plusMinutes((long) elapsedTimeUnits));
			
			// Selecting the parking spot to which the car is sent.
			int selectedParkingSpot = Math.round((float)(p*(numOfParkingSpots-1))+1);
			sim_schedule("Out"+selectedParkingSpot, 0.0, SimulationEngine.EVENT_TYPE_CAR_ENTER, spotUnavailbeTime);
		}
	}

}
