package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import MainModule.Controller;
import Objects.Interval;
import SimulationModule.SimStats;

public abstract class DButils {

	public static ArrayList<String> occupiedParkingSpotsInDatetime(LocalDateTime dateTime) {
		ArrayList<String> occupiedList = new ArrayList<>();
		ResultSet rs;

		rs = MainModule.Controller.DBcon.get_ParkingSpotState_Data(false);
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				Timestamp tempDate = rs.getTimestamp(1);
				String PSid = rs.getString(2);
				double minutesOccupied = rs.getDouble(3);

				LocalDateTime parkStart = tempDate.toLocalDateTime();
				LocalDateTime parkEnd = parkStart.plusMinutes((long) minutesOccupied);

				if ((dateTime.isEqual(parkStart) || dateTime.isAfter(parkStart)) && (dateTime.isBefore(parkEnd))) {
					occupiedList.add(PSid);
				}
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return occupiedList;
	}

	public static double getRemainingTimeOfParkingSpot(String PSname, LocalDateTime simStartTime, LocalDateTime simEndTime) {
		ResultSet rs;

		rs = MainModule.Controller.DBcon.get_ParkingSpotState_ByName(PSname, simStartTime);

		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				String d = rs.getString(1);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
				LocalDateTime tempDate = LocalDateTime.parse(d, formatter);

				// String PSid = rs.getString(2);
				double minutesOccupied = rs.getDouble(3);

				LocalDateTime parkStart = tempDate;
				LocalDateTime parkEnd = parkStart.plusMinutes((long) minutesOccupied);

				if (parkEnd.isAfter(simEndTime)) {
					return calcDifference(simEndTime, parkEnd);
				}
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return 0;
	}

	/**
	 * Returns the number of minutes from 'earlier' to 'later'.
	 */
	public static double calcDifference(LocalDateTime earlier, LocalDateTime later) {
		long minutes = ChronoUnit.MINUTES.between(earlier, later);
		return minutes;
	}

	public static void deleteSimulationData() {
		try {
			Controller.DBcon.truncateTable("GATE_ACTIVITY");
			System.out.println("GATE_ACTIVITY truncated");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Controller.DBcon.drop_table_parkingSpotState();
			System.out.println("PARKING_SPOT_STATE truncated");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Restart INTERVAL's sequence
		try {
			Controller.DBcon.drop_sequence_INTERVAL_ID_SEQ();
			System.out.println("INTERVAL_ID_SEQ dropped.");
			
			Controller.DBcon.create_sequence_INTERVAL_ID_SEQ();
			System.out.println("INTERVAL_ID_SEQ created.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateCurrentSimulationIntervals(ArrayList<SimStats> simScheduler) {
		Controller.DBcon.truncateIntervals();
		Controller.DBcon.create_table_parkingSpotState();
		for (int i = 0; i < simScheduler.size(); i++) {
			Controller.DBcon.insertInterval(simScheduler.get(i));
		}
	}

	public static int getTotalTimeOfSimulation() {
		ResultSet rs = null;
		int totalTimeOfAllSimulations = 0;

		rs = Controller.DBcon.get_Intervals_data();

		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				int d = rs.getInt(2);
				totalTimeOfAllSimulations += d;
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return totalTimeOfAllSimulations;
	}

	public static LocalDateTime getSimulationStart() {
		ResultSet rs = null;
		LocalDateTime start = null;

		rs = Controller.DBcon.get_Intervals_data();

		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			} else {
				rs.next();
				start = rs.getTimestamp(1).toLocalDateTime();
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return start;
	}
	
	public static LocalDateTime getSimulationEnd() {
		ResultSet rs = null;
		LocalDateTime End = null;

		rs = Controller.DBcon.get_Intervals_data();

		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			} else {
				rs.last();
				LocalDateTime lastInterval = rs.getTimestamp(1).toLocalDateTime();
				int lastDuration = rs.getInt(2);
				End = lastInterval.plusMinutes(lastDuration);
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return End;
	}

	/**
	 * Returns the exit rate of an interval in a given point in time (in
	 * minutes). Returns -1 if pointInTime is not within existing intervals.
	 */
	public static double getExitRate(int pointInTime) {
		ResultSet rs = null;
		LocalDateTime simStart = null;
		LocalDateTime simEnd = getSimulationEnd();

		rs = Controller.DBcon.get_Intervals_data();
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				LocalDateTime intervalStart = rs.getTimestamp(1).toLocalDateTime();
				int duration = rs.getInt(2);
				LocalDateTime intervalEnd = intervalStart.plusMinutes(duration);
				double exitRate = rs.getDouble(4);

				if (rs.getRow() == 1) {
					simStart = intervalStart;
				}

				LocalDateTime requiredDateTime = simStart.plusMinutes(pointInTime);
				
				if (requiredDateTime.equals(simEnd)) {//pointInTime is at the end of intervals list
					if (		(requiredDateTime.isEqual(intervalStart) || requiredDateTime.isAfter(intervalStart))
							&& 	(requiredDateTime.isBefore(intervalEnd) || requiredDateTime.isEqual(intervalEnd))) {
						return exitRate;
					}
				}
				else { //pointInTime is somewhere in the middle of intervals list
					if ((requiredDateTime.isEqual(intervalStart) || requiredDateTime.isAfter(intervalStart))
							&& (requiredDateTime.isBefore(intervalEnd))) {
						return exitRate;
					}
				}
				
				
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}

		return -1;
	}

	/**
	 * Updates the "IS_ACTIVE" field in Parking_Spots table at the DB. The
	 * update is carried out sequentially, which means it start from the first
	 * row of parking spot in the table. The user does not have an influence on
	 * which row to change.
	 */
	public static void updateParkingSpots(int numOfParkingSpots) {
		int PLsize = Controller.DBcon.getParkingLotSize();
		for (int i = 1; i<=PLsize; i++) {
			if (i >=1 && i <= numOfParkingSpots) {
				Controller.DBcon.updateActiveParkingSpot(i, true);
			}
			else {
				Controller.DBcon.updateActiveParkingSpot(i, false);
			}
		}
	}

	public static HashMap<String, Double> getParkingSpotUsageMap() {
		int numOfParkingSpots = Controller.DBcon.getNumOfActiveParkingSpots();
		HashMap<String, Double> usageMap = new HashMap<>();
		int totalTimeOfSimulation = getTotalTimeOfSimulation();
		LocalDateTime simEnd = getSimulationEnd();
		int lastIntervalIndex = getLastInterval();
		
		// Get last interval's data
		ArrayList<Interval> intervalList = getIntervals();
		Interval lastInterval = null;
		for (int i = 0; i<intervalList.size(); i++) {
			if (intervalList.get(i).getID() == lastIntervalIndex) {
				lastInterval = intervalList.get(i);
			}
		}
		
		for (int ps = 1; ps <=numOfParkingSpots; ps++) {
			ResultSet rs = Controller.DBcon.get_ParkingSpotState_Data("ParkingSpot"+ps, false);
			
			try {
				if (!rs.isBeforeFirst()) {
					System.out.println("No data. ResultSet returned empty.");
				}
				int durationCounter = 0;
				while (rs.next()) {
					LocalDateTime parkStart = rs.getTimestamp(1).toLocalDateTime();
					int duration = rs.getInt(3);
					int intervalId = rs.getInt(5);
					
					if (intervalId != lastIntervalIndex) { // if this is not the last interval
						durationCounter += duration;
					}
					// it is the last interval
					else {
						if (parkStart.plusMinutes(duration).isAfter(lastInterval.getStart().plusMinutes(lastInterval.getDuration()))) { // if the current park ends after the end of the simulation
							durationCounter += (int) calcDifference(parkStart, simEnd);
						}
						else {
							durationCounter += duration;
						}
					}
				}
				
				double percentage = ((double)durationCounter)/totalTimeOfSimulation;
				if (percentage>1) {
					percentage = 1;
				}
				usageMap.put("ParkingSpot"+ps, percentage);

				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				class Local {
				}
				String methodName = Local.class.getEnclosingMethod().getName();
				System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
				System.out.println(e.getMessage());
			}
		}
		
		return usageMap;
	}

	public static ArrayList<Interval> getIntervals() {
		ResultSet rs = Controller.DBcon.get_Intervals_data();
		ArrayList<Interval> intervalList = new ArrayList<>();
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				LocalDateTime start = rs.getTimestamp(1).toLocalDateTime();
				int duration = rs.getInt(2);
				double enterRate = rs.getDouble(3);
				double exitRate = rs.getDouble(4);
				int id = rs.getInt(5);
				
				Interval i = new Interval(start, duration, enterRate, exitRate, id);
				intervalList.add(i);
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return intervalList;
	}

	public static int getLastInterval() {
		ResultSet rs = Controller.DBcon.get_Intervals_data();
		int last = 0;
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			} else {
				rs.last();
				last = rs.getInt(5);
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return last;
	}

	public static ArrayList<String> getParkingSpotsNames() {
		ResultSet rs = Controller.DBcon.get_ParkingSpot_Data();
		ArrayList<String> ParkingSpotNames = new ArrayList<>();
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				String id = rs.getString(1);
				int isActive = rs.getInt(4);
				
				if (isActive == 1) {
					ParkingSpotNames.add(id);
				}
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return ParkingSpotNames;
	}

	public static ArrayList<String> getFreeingUpParkingSpots(int pointIntimeValue) {
		ArrayList<String> freeingUpParkingSpot = new ArrayList<>();
		LocalDateTime currentTime = getSimulationStart().plusMinutes(pointIntimeValue);
		ResultSet rs = Controller.DBcon.get_ParkingSpotState_Data(false);
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned empty.");
			}
			while (rs.next()) {
				LocalDateTime parkStart = rs.getTimestamp(1).toLocalDateTime();
				String PSName = rs.getString(2);
				int duration = rs.getInt(3);
				LocalDateTime parkEnd = parkStart.plusMinutes(duration);
				
				if (parkEnd.isEqual(currentTime)) {
					freeingUpParkingSpot.add(PSName);
				}
			}

			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return freeingUpParkingSpot;
	}
	
	
}
