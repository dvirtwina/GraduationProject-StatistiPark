package PredictionModule;

import java.time.LocalDateTime;

import Database.DButils;
import SimulationEngine.SimStats;

public class Predictor {
	private static final double MINIMAL_POSSIBILITY = 0.85; 
	private static double previousMu = -1;
	private static int previousPrediction;

	/**
	 * Returns the minimal number of minutes it would take for a single parking
	 * spot to be available, based on the MINIMAL_POSSIBILITY constant, set in
	 * this class. param pointInTime sets the specific time from the start of
	 * the simulation that the prediction will be made for. param
	 * numOfParkingSpots asks for the number of parking spots that the parking
	 * lot contains.
	 * 
	 * Exceptions are thrown if the parking lot is not full at the given
	 * pointInTime OR if the given pointInTime is not within the simulation
	 * intervals.
	 */
	public static int getPrediction(int pointInTime, int numOfParkingSpots) throws Exception {
		LocalDateTime simStart = DButils.getSimulationStart();
		boolean possFlag = false;

		double probability;
		double e = Math.E;
		double mu = -1;
		int c = numOfParkingSpots;
		int t = 1;

		// if the parking lot is full in the point of time given -> do
		// prediction
		if (DButils.occupiedParkingSpotsInDatetime(simStart.plusMinutes(pointInTime)).size() == numOfParkingSpots) {
			System.out.println(
					"Predicting... params: pointInTime:" + pointInTime + "\tnumOfParkingSpots:" + numOfParkingSpots);

			while (!possFlag) {
				mu = DButils.getExitRate(pointInTime);
				if (mu == -1) {
					throw new Exception("Exit Rate not found!");
				}

				// if the relevant interval was already predicted return
				// previous result
				if (previousMu != -1 && previousMu == mu) {
					return previousPrediction;
				}

				probability = 1 - (Math.pow(e, -((1 / mu) * c * t)));

				if (probability >= MINIMAL_POSSIBILITY) {
					possFlag = true;
					previousMu = mu;
					previousPrediction = t;
					return t;
				} else {
					t++;
				}
			}
		} else { // Parking lot is not full.
			// System.out.println("PL is NOT full! params:
			// pointInTime:"+pointInTime +
			// "\tnumOfParkingSpots:"+numOfParkingSpots);
			throw new Exception("Predition cannot be made when parking lot is not full.");
		}

		return -1;
	}

}
