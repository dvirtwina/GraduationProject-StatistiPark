package MainModule;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import Database.DBconnector;
import Database.DButils;
import GUI.LoginScreen;
import GUI.UserMainScreen;
import GUI.AdminMainScreen;
import Objects.User;
import SimulationModule.SimStats;
import SimulationModule.SimulationEngine;

public class Controller {
	public static User loggedInUser = null;
	
	public static DBconnector DBcon;
	
	public static SimulationEngine simulation;
	private static int numOfParkingSpots;
	private static ArrayList<SimStats> simScheduler;
	private static int SchedulerIntervalIndex;
	
	private AdminMainScreen adminMainScreen;
	
	public static HashMap<String, ArrayList<String>> guiScheduler;

	public Controller() {
		guiScheduler = new HashMap<>();
		DBcon = new DBconnector();
				
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new LoginScreen();
			}
		});	
		do { // wait for the screen and get its data. This cannot be in a separate method.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (guiScheduler.containsKey("LoginScreen"));
		
		// Load data from DB.
		numOfParkingSpots = DBcon.getNumOfActiveParkingSpots();
		
		// Open GUI components.
		if (loggedInUser.getRole().equals(User.ROLE_ADMINISTRATOR)) { // Admin Screen
			adminMainScreen = new AdminMainScreen();
			
			do { // wait for the screen and get its data. This cannot be done in a separate method.
				try {
					// Check for jobs from SimConfigScreen.
					if (guiScheduler.containsKey("SimConfigScreen")) {
						// Check for RunSimulation job.
						if (guiScheduler.get("SimConfigScreen").contains("RunSimulation")) {
							
							// Delete all previous simulations data from DB
							DButils.deleteSimulationData();
							
							// Update active parking spots.
							DButils.updateParkingSpots(numOfParkingSpots);

							// Execute all simulations.
							DButils.updateCurrentSimulationIntervals(simScheduler);
							
							simulation = new SimulationEngine(numOfParkingSpots, 
									simScheduler.get(SchedulerIntervalIndex).getDuration(), 
									simScheduler.get(SchedulerIntervalIndex).getRateCarEnter(), 
									simScheduler.get(SchedulerIntervalIndex).getRateCarExit(),
									SimulationEngine.RUNNING_SIM_PHASE, 
									simScheduler.get(SchedulerIntervalIndex).getStart());
							SchedulerIntervalIndex++;
							
							for (int i = SchedulerIntervalIndex; i < simScheduler.size(); i++) {
								simulation = new SimulationEngine(numOfParkingSpots, 
										simScheduler.get(SchedulerIntervalIndex).getDuration(), 
										simScheduler.get(SchedulerIntervalIndex).getRateCarEnter(), 
										simScheduler.get(SchedulerIntervalIndex).getRateCarExit(),
										SimulationEngine.CONFIGURATION_PHASE, 
										simScheduler.get(SchedulerIntervalIndex).getStart());
							SchedulerIntervalIndex++;
							}
							
							showSimulationResults();
							
							guiScheduler.get("SimConfigScreen").remove("RunSimulation");
						}
					}
					
					// TODO Check for jobs from MainAdminScreen. If there should be any, attend to it here.
					
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (guiScheduler.containsKey("MainAdminScreen"));
		}
		else if (loggedInUser.getRole().equals(User.ROLE_USER)) { // User Screen
			new UserMainScreen();
			
			do { // wait for the screen and get its data. This cannot be done in a separate method.
				try {
				
				
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (guiScheduler.containsKey("UserMainScreen"));
		}	
	}

	private void showSimulationResults() {
		if (guiScheduler.containsKey("MainAdminScreen") && guiScheduler.get("MainAdminScreen").contains("Alive")) {
			adminMainScreen.setSimulationResultsToRightPanel();
			adminMainScreen.showScrollBarPanel();;
		}
	}

	public static SimulationEngine getSimulation() {
		return simulation;
	}

	public static int getNumOfParkingSpots() {
		return numOfParkingSpots;
	}
	
	public static ArrayList<SimStats> getSimScheduler() {
		return simScheduler;
	}

	public static void setSimScheduler(ArrayList<SimStats> simScheduler) {
		Controller.simScheduler = simScheduler;
	}

	public static int getSchedulerIntervalIndex() {
		return SchedulerIntervalIndex;
	}

	public static void setSchedulerIntervalIndex(int schedulerIndex) {
		SchedulerIntervalIndex = schedulerIndex;
	}

	public static void setNumOfParkingSpots(int numOfParkingSpots) {
		Controller.numOfParkingSpots = numOfParkingSpots;
	}

	public static double getSimulationDuration() {
		return simScheduler.get(SchedulerIntervalIndex).getDuration();
	}
	
	public static double getPreviousSimulationDuration() {
		return simScheduler.get(SchedulerIntervalIndex-1).getDuration();
	}

	public static LocalDateTime getSimulationStartTime() {
		return simScheduler.get(SchedulerIntervalIndex).getStart();
	}
	
	public static LocalDateTime getPreviousSimulationStartTime() {
		return simScheduler.get(SchedulerIntervalIndex-1).getStart();
	}

	@SuppressWarnings("unused")
	private void printFullParkingLotTUs() {
		int totalTimeOfAllSimulations = DButils.getTotalTimeOfSimulation();
			
		for (int i = 0; i < totalTimeOfAllSimulations; i++) {
			ArrayList<String> PL = DButils.occupiedParkingSpotsInDatetime(DButils.getSimulationStart().plusMinutes(i));
			int carsInPL = PL.size();
			if (carsInPL >= simulation.getNumOfParkingSpots()) {
				System.out.println("Full parking lot TU: " + i);
				System.out.println(PL);
			}
		}
	}

}
