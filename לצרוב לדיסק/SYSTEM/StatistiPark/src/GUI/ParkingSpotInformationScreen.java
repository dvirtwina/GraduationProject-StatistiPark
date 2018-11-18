package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import Database.DButils;
import MainModule.Controller;
import Objects.Interval;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import java.awt.SystemColor;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.GridLayout;

public class ParkingSpotInformationScreen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel rightPanel;
	
	public ParkingSpotInformationScreen() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setSize(720, 550);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, (dim.height / 2 - this.getSize().height / 2));
		
		setTitle("Parking Spot Information");
		getContentPane().setLayout(null);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBounds(0, 0, 190, 511);
		getContentPane().add(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel label = new JLabel("StatistiPark");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		label.setBounds(0, 0, 190, 71);
		leftPanel.add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 69, 190, 2);
		leftPanel.add(separator);
		
		JTextPane txtpn = new JTextPane();
		txtpn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtpn.setEditable(false);
		txtpn.setText("Please select a parking spot in the Drop-down menu below, to view its information.");
		txtpn.setBounds(10, 82, 170, 74);
		txtpn.setBackground(SystemColor.control);
		leftPanel.add(txtpn);
		
		rightPanel = new JPanel();
		rightPanel.setBounds(190, 0, 515, 511);
		getContentPane().add(rightPanel);
		rightPanel.setLayout(new BorderLayout(0, 0));
		
		ArrayList<String> PSNames = DButils.getParkingSpotsNames();
		String[] a = PSNames.toArray(new String[PSNames.size()]);
		JComboBox<?> comboBox = new JComboBox<>(a);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String parkingSpot = String.valueOf(comboBox.getSelectedItem());
				if (!parkingSpot.equals("null")) {
					showRightPanel(parkingSpot);
				}
			}
		});
		comboBox.setBounds(10, 167, 140, 20);
		comboBox.setSelectedItem(null);
		leftPanel.add(comboBox);
		
		JTextPane txtpnTheTable = new JTextPane();
		txtpnTheTable.setText("* The table shows all timeframes in which the selected parking spot was unavailable, including ones that strech-out of simulation's timeframe.");
		txtpnTheTable.setEditable(false);
		txtpnTheTable.setBackground(SystemColor.menu);
		txtpnTheTable.setBounds(10, 198, 170, 108);
		leftPanel.add(txtpnTheTable);
	}
	
	private void showRightPanel(String parkingSpotName) {
		ResultSet rs;
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		JScrollPane scrollPane = new JScrollPane();
		int durationCounter = 0;
		
		rightPanel.removeAll();
		rightPanel.add(scrollPane, BorderLayout.CENTER);
				
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		model = new DefaultTableModel();
		model.addColumn("Park Start");
		model.addColumn("Minutes Occupied");
		
		int lastIntervalIndex = DButils.getLastInterval();
		LocalDateTime simEnd = DButils.getSimulationEnd();
		
		// Get last interval's data
		ArrayList<Interval> intervalList = DButils.getIntervals();
		Interval lastInterval = null;
		for (int i = 0; i<intervalList.size(); i++) {
			if (intervalList.get(i).getID() == lastIntervalIndex) {
				lastInterval = intervalList.get(i);
			}
		}
		
		rs = Controller.DBcon.get_ParkingSpotState_Data(parkingSpotName, false);
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned enpty.");
			}
			while (rs.next()) {
				LocalDateTime parkStart = rs.getTimestamp(1).toLocalDateTime();
				int minutesOccupied = rs.getInt(3);
				int intervalId = rs.getInt(5);
				
				if (intervalId != lastIntervalIndex) { // if this is not the last interval
					durationCounter += minutesOccupied;
				}
				// it is the last interval
				else {
					if (parkStart.plusMinutes(minutesOccupied).isAfter(lastInterval.getStart().plusMinutes(lastInterval.getDuration()))) { // if the current park ends after the end of the simulation
						durationCounter += (int) DButils.calcDifference(parkStart, simEnd);
					}
					else {
						durationCounter += minutesOccupied;
					}
				}

				model.addRow(new Object[]{parkStart, minutesOccupied});			
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
		
		table.setModel(model);
		// Sort the table by datetime.
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
        table.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // primary sort param
//      sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING)); // secondary sort param
        sorter.setSortKeys(sortKeys);
        
		scrollPane.setViewportView(table);
		
		ResultSet rs_1 = Controller.DBcon.get_ParkingSpot_Data(parkingSpotName);
		String PSName = null;
		String area = null;
		int isHandicapped = 0;
		try {
			if (!rs_1.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned enpty.");
			}
			rs_1.next();
			PSName = rs_1.getString(1);
			area = rs_1.getString(2);
			isHandicapped = rs_1.getInt(3);

			if (rs_1 != null) {
				rs_1.close();
			}
		} catch (SQLException e) {
			class Local {
			}
			String methodName = Local.class.getEnclosingMethod().getName();
			System.out.println("AN EXCEPTION WAS COUGHT IN METHOD " + methodName + ":");
			System.out.println(e.getMessage());
		}
		
		JPanel infoPanel = new JPanel();
		rightPanel.add(infoPanel, BorderLayout.NORTH);
		infoPanel.setLayout(new GridLayout(4, 3, 0, 0));
		
		JLabel lblName = new JLabel("Name:");
		infoPanel.add(lblName);
		
		JLabel lblValname = new JLabel(""+PSName);
		infoPanel.add(lblValname);
		
		JPanel panel13 = new JPanel();
		infoPanel.add(panel13);
		
		JLabel lblArea = new JLabel("Area:");
		infoPanel.add(lblArea);
		
		JLabel lblValarea;
		switch (area) {
		case "N":
			lblValarea = new JLabel("North");
			break;
			
		case "E":
			lblValarea = new JLabel("East");
			break;
			
		case "S":
			lblValarea = new JLabel("South");
			break;
			
		case "W":
			lblValarea = new JLabel("West");
			break;
			
		default:
			lblValarea = new JLabel("valArea");
			break;
		}
		infoPanel.add(lblValarea);
		
		JPanel panel23 = new JPanel();
		infoPanel.add(panel23);
		
		JLabel lblHandicapped = new JLabel("Handicapped:");
		infoPanel.add(lblHandicapped);
		
		JLabel lblValishandicapped = null;
		if (isHandicapped == 1) {
			lblValishandicapped = new JLabel("True");
		}
		else if (isHandicapped == 0) {
			lblValishandicapped = new JLabel("False");
		}
		infoPanel.add(lblValishandicapped);
		
		JPanel panel33 = new JPanel();
		infoPanel.add(panel33);
		
		String toolTip = "The percentage of time this parking spot was unavailable within the simulation's timeframe.";
		
		JLabel lblOccupationPercenrtage = new JLabel("Occupation Percenrtage");
		lblOccupationPercenrtage.setToolTipText(toolTip);
		infoPanel.add(lblOccupationPercenrtage);
		
		int totalTimeOfSimulation = DButils.getTotalTimeOfSimulation();
		double percentage = 100*(((double)durationCounter)/totalTimeOfSimulation);
		if (percentage > 100) {
			percentage = 100;
		}
		JLabel lblValpercentage = new JLabel(""+ percentage + " %");
		lblValpercentage.setToolTipText(toolTip);
		infoPanel.add(lblValpercentage);
		
		JPanel panel43 = new JPanel();
		infoPanel.add(panel43);
		
		rightPanel.revalidate();
		rightPanel.repaint();		
	}
}
