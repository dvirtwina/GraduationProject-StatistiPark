package SimulationModule;

import java.time.LocalDateTime;
import MainModule.Controller;
import eduni.simjava.*;

public class ParkingSpot extends Sim_entity {
	private Sim_port in;

	public ParkingSpot(String name) {
		super(name);
		in = new Sim_port("In");
		add_port(in);
	}

	public void body() {
		while (Sim_system.running()) {
			try {
				Sim_event e = new Sim_event();
				if (e != null) {
					super.sim_get_next(e);
					double carTimeOfArrival = Sim_system.sim_clock();
					double timeToProcess = (double) e.get_data();
					int tag = e.get_tag();
					sim_pause(timeToProcess);
					sim_completed(e);

					// Insert activity do DB.
					LocalDateTime realisticDateTime = Controller.getSimulationStartTime();
					System.out.println("ParkingSpot " + get_name() + " arrived at PS at: "
							+ realisticDateTime.plusMinutes((long) carTimeOfArrival) + ", with processTime of: "
							+ timeToProcess);

					if (tag == SimulationEngine.EVENT_TYPE_CAR_ENTER) {
						MainModule.Controller.DBcon.insertInto_ParkingSpotState_CarEnter(
								realisticDateTime.plusMinutes((long) carTimeOfArrival), get_name(), timeToProcess,
								null);
					} else { // EVENT_TAG_CONTINUED
						MainModule.Controller.DBcon.insertInto_ParkingSpotState_CarEnter(
								realisticDateTime.plusMinutes((long) carTimeOfArrival), get_name(), timeToProcess,
								"continued");
					}
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

}
