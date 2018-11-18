package Database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import MainModule.Controller;
import SimulationEngine.SimStats;

public class DBconnector {
	private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DB_CONNECTION = "jdbc:oracle:thin:@localhost:1521:orcl";// 2909:xe";
	private static final String DB_ADMIN = "system"; // pass_Stefani: system
														// pass_Dvir: 123456
	private static final String DB_PASS = "123456";
	private Connection con;
	private Statement stmt;
	// private CallableStatement procstmt;
	// private PreparedStatement prepstmt;

	public DBconnector() {
		try {
			// step1 load the driver class
			Class.forName(DB_DRIVER);
			System.out.println("Driver loaded");

			// step2 create the connection object
			con = DriverManager.getConnection(DB_CONNECTION, DB_ADMIN, DB_PASS);
			/* stmt = con.createStatement(); */
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			System.out.println("Database connected");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void closeConnection() {
		try {
			Thread.sleep(2000l); // wait for simulation to close its resources
									// at DB.
			stmt.close();
			con.close();
			System.out.println("Database connection closed.");
		} catch (SQLException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void insertInto_GateActivity_CarEnter(int gateId, LocalDateTime localDateTime) {
		try {
			String insertSQL = "insert into GATE_ACTIVITY (GATE_ID, car_enter_timestamp) values (?,?)";
			// System.out.println(insertSQL);
			PreparedStatement prepstmt = con.prepareStatement(insertSQL);
			prepstmt.setInt(1, gateId);
			prepstmt.setTimestamp(2, Timestamp.valueOf(localDateTime));
			// System.out.println("Params: "+gateId+", "+localDateTime);

			// execute insert SQL statement
			prepstmt.executeUpdate();
			// System.out.println("Record is inserted.");

			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public void insertInto_ParkingSpotState_CarEnter(LocalDateTime parkStartTimestamp, String PSid,
			double minutesOccupied, String isContinued) {
		try {
			String insertSQL = "insert into PARKING_SPOT_STATE values (?,?,?,?,?)";

			// System.out.println(insertSQL);
			PreparedStatement prepstmt = con.prepareStatement(insertSQL);
			prepstmt.setTimestamp(1, Timestamp.valueOf(parkStartTimestamp));
			prepstmt.setString(2, PSid);
			prepstmt.setDouble(3, minutesOccupied);
			System.out.println("Params: " + PSid + ", " + parkStartTimestamp + ", " + minutesOccupied);
			if (isContinued == null) {
				prepstmt.setInt(4, 0);
			} else if (isContinued.equals("continued")) {
				prepstmt.setInt(4, 1);
			}
			prepstmt.setInt(5, Controller.getSchedulerIntervalIndex() +1); // +1: SchedulerIntervalIndex starts with 0, Snterval's sequence starts with 1.

			// Execute insert SQL statement
			prepstmt.executeUpdate();
			// System.out.println("Record is inserted.");

			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public ResultSet get_ParkingSpotState_Data(boolean withIsContinued) {
		ResultSet rs = null;
		String query;
		try {
			if (withIsContinued == false) {
				query = "select * from SYSTEM.PARKING_SPOT_STATE where IS_CONTINUED = 0 order by PARKING_START_TIMESTAMP";
			} else {
				query = "select * from SYSTEM.PARKING_SPOT_STATE order by PARKING_START_TIMESTAMP";
			}
			// System.out.println(query);

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		return rs;
	}
	
	public ResultSet get_ParkingSpotState_Data(String parkingSpotId, boolean withIsContinued) {
		ResultSet rs = null;
		String query;
		try {
			if (withIsContinued == false) {
				query = "select * from SYSTEM.PARKING_SPOT_STATE where IS_CONTINUED = 0 and PARKING_SPOT_ID = '" + parkingSpotId +"'  order by PARKING_START_TIMESTAMP";
			} else {
				query = "select * from SYSTEM.PARKING_SPOT_STATE where PARKING_SPOT_ID = '" + parkingSpotId +"' order by PARKING_START_TIMESTAMP";
			}
			// System.out.println(query);

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		return rs;
	}

	public ResultSet get_ParkingSpotState_ByName(String parkingSpotName, LocalDateTime start) {
		ResultSet rs = null;
		String query;

		int day = start.getDayOfMonth();
		int mon = start.getMonthValue();
		int year = start.getYear();
		int hour = start.getHour();
		int min = start.getMinute();

		String d;
		if (day < 10)
			d = "0" + day;
		else
			d = "" + day;

		String mo;
		if (mon < 10)
			mo = "0" + mon;
		else
			mo = "" + mon;

		String y;
		if (year < 10)
			y = "0" + year;
		else
			y = "" + year;

		String h;
		if (hour < 10)
			h = "0" + hour;
		else
			h = "" + hour;

		String m;
		if (min < 10)
			m = "0" + min;
		else
			m = "" + min;

		String dateTimeStr = d + "/" + mo + "/" + y + " " + h + ":" + m;
		try {
			query = "select to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI'), PARKING_SPOT_ID, MINUTS_OCCUPIED "
					+ "from PARKING_SPOT_STATE " + "where PARKING_SPOT_ID = " + "'" + parkingSpotName + "'"
					+ " And to_char(PARKING_START_TIMESTAMP,'DD/MM/YYYY HH24:MI') >= '" + dateTimeStr + "'"
					+ " order by PARKING_START_TIMESTAMP";
			// System.out.println(query);

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		return rs;
	}

	public ResultSet get_Users_data() {
		ResultSet rs = null;
		String query;

		try {
			query = "select * from SYSTEM.USERS";
			// System.out.println(query);

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		return rs;
	}

	public void truncateTable(String tableName) throws Exception {
		ResultSet rs = null;
		String query;
		try {
			query = "select count(*) from all_objects where object_type = 'TABLE' and object_name = '"
					+ tableName.toUpperCase() + "'";
			// execute insert SQL statement
			rs = stmt.executeQuery(query);

			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned enpty.");
			} else {
				rs.next();
				int count = rs.getInt(1);
				if (count != 1) {
					throw new Exception("Invalid Table Name");
				} else {
					String SQL = "truncate table " + tableName;
					stmt.executeUpdate(SQL);
				}
			}

		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public void truncateIntervals() {
		try {
			ResultSet rs = get_Intervals_data();
			if (rs.isBeforeFirst()) { //Intervals table NOT empty. 
				String SQL = "truncate table INTERVALS";
				stmt.executeUpdate(SQL);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertInterval(SimStats SimStats) {
		try {
			// Get current sequence number.
			String sqlIdentifier = "select interval_id_seq.nextval from dual";
			int intervalId = 0; // sequences are supposed to start with 1. This means that if a 0 is inserted to Intervals table as an ID it signifies an error at retrieving the sequence number.
			
			PreparedStatement pst = con.prepareStatement(sqlIdentifier);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
			  intervalId = rs.getInt(1);
			}
				   
			pst.close();
			
			// Set up insertion to DB.
			String insertSQL = "insert into INTERVALS values (?,?,?,?,?)";

			PreparedStatement prepstmt = con.prepareStatement(insertSQL);
			prepstmt.setTimestamp(1, Timestamp.valueOf(SimStats.getStart()));
			prepstmt.setInt(2, SimStats.getDuration());
			prepstmt.setDouble(3, SimStats.getRateCarEnter());
			prepstmt.setDouble(4, SimStats.getRateCarExit());
			prepstmt.setInt(5, intervalId);

			// Execute insert SQL statement
			prepstmt.executeUpdate();

			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public ResultSet get_Intervals_data() {
		ResultSet rs = null;
		String query;
		try {

			query = "select * from SYSTEM.INTERVALS";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		return rs;
	}

	public void updateActiveParkingSpot(int i, boolean isActive) {
		try {
			String SQL;
			
			if (isActive) {
				SQL = "update PARKING_SPOT set IS_ACTIVE = 1 where PARKING_SPOT_ID = ?";
			}
			else {
				SQL = "update PARKING_SPOT set IS_ACTIVE = 0 where PARKING_SPOT_ID = ?";
			}

			PreparedStatement prepstmt = con.prepareStatement(SQL);
			prepstmt.setString(1, "ParkingSpot"+i);

			//stmt.executeQuery("update PARKING_SPOT set IS_ACTIVE = 1 where PARKING_SPOT_ID = 'ParkingSpot"+i+"'");
			// Execute insert SQL statement
			prepstmt.executeQuery();
			// System.out.println("Record is inserted.");

			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}
	
	public int getNumOfActiveParkingSpots() {
		ResultSet rs = null;
		String query;
		int res = 0;
		try {

			query = "select count(*) from PARKING_SPOT where IS_ACTIVE = 1";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
			
			rs.next();
			res =  rs.getInt(1);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return res;
	}

	public int getParkingLotSize() {
		ResultSet rs = null;
		String query;
		int res = 0;
		try {

			query = "select count(*) from PARKING_SPOT";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
			
			rs.next();
			res =  rs.getInt(1);
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return res;
	}

	public ResultSet get_ParkingSpot_Data() {
		ResultSet rs = null;
		String query;
		try {

			query = "select * from PARKING_SPOT";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return rs;
	}
	
	public ResultSet get_ParkingSpot_Data(String ParkingSpot) {
		ResultSet rs = null;
		String query;
		try {

			query = "select * from PARKING_SPOT where PARKING_SPOT_ID = '" + ParkingSpot + "'";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return rs;
	}

	public ResultSet get_AfekaPLData_Data() {
		ResultSet rs = null;
		String query;
		try {

			query = "select to_char(GATE_PASS_TIME,'DD/MM/YYYY HH24:MI'), ALT_LICENSE_NUMBER, REASON "
					+ "from AFEKA_PL_DATA "
					+ "where (to_char(GATE_PASS_TIME,'DD/MM/YYYY HH24:MI') between '01/01/2018 06:00' and '01/01/2018 23:59' "
					+ "and REASON in ('Standard entrance', 'Standard exit')) "
					+ "order by ALT_LICENSE_NUMBER, GATE_PASS_TIME";

			// execute insert SQL statement
			rs = stmt.executeQuery(query);
			
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		return rs;
	}

	public void drop_sequence_INTERVAL_ID_SEQ() {
		try {
			String SQL = "drop sequence INTERVAL_ID_SEQ";
			

			PreparedStatement prepstmt = con.prepareStatement(SQL);

			// Execute insert SQL statement
			prepstmt.executeQuery();
			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public void create_sequence_INTERVAL_ID_SEQ() {
		try {
			String SQL = "CREATE SEQUENCE interval_id_seq "
					+ "START WITH 1 "
					+ "INCREMENT BY 1 "
					+ "NOCYCLE "
					+ "NOCACHE";
			

			PreparedStatement prepstmt = con.prepareStatement(SQL);

			// Execute insert SQL statement
			prepstmt.executeQuery();
			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public void drop_table_parkingSpotState() {
		try {
			String SQL = "drop table PARKING_SPOT_STATE";
			
			PreparedStatement prepstmt = con.prepareStatement(SQL);

			// Execute insert SQL statement
			prepstmt.executeQuery();
			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

	public void create_table_parkingSpotState() {
		try {
			String SQL = "create table PARKING_SPOT_STATE ( "
					+ "parking_start_timestamp date not null, "
					+ "parking_spot_id REFERENCES PARKING_SPOT(parking_spot_id) not null, "
					+ "minuts_occupied number not null, "
					+ "is_continued number(1), "
					+ "interval_id REFERENCES INTERVALS(interval_id), "
					+ "CONSTRAINT PARKING_SPOT_STATE_tbl_unique UNIQUE (parking_start_timestamp, parking_spot_id) "
					+ ")";
			
			PreparedStatement prepstmt = con.prepareStatement(SQL);

			// Execute insert SQL statement
			prepstmt.executeQuery();
			prepstmt.close();
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
	}

}
