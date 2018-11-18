package SimulationEngine;

import eduni.simjava.*;
import eduni.simjava.distributions.*;

public class ExitingSource extends Sim_entity {
	private Sim_port out;
    private Sim_negexp_obj delay;

    ExitingSource(String name, double carsExitingMean) {
        super(name);
        out = new Sim_port("Out");
        add_port(out);
        // Create the source's distribution and add it
        delay = new Sim_negexp_obj("Delay", carsExitingMean);
        add_generator(delay);
      }
}
