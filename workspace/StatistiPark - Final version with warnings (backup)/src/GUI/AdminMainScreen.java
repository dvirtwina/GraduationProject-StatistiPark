package GUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;

import MainModule.Controller;
import PredictionModule.Predictor;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import Database.DButils;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.BorderLayout;
import javax.swing.JScrollBar;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JRadioButton;

public class AdminMainScreen extends JFrame {
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JRadioButton rdbtnTableView;
	private JRadioButton rdbtnMapView;
	
	private JPanel rightPanel;
	private JPanel scrollBarPanel;
	private int pointIntimeValue;
	private JLabel valMinutesPassed;
	private JLabel val_PSGetAvailable;
	
	public AdminMainScreen() {
		ArrayList<String> a = new ArrayList<>();
		a.add("Alive");
		Controller.guiScheduler.put("MainAdminScreen", a);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(911, 716);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, (dim.height / 2 - this.getSize().height / 2)-10);
		
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	Controller.DBcon.closeConnection();
                System.exit(0);
            }
        });
		
		getContentPane().setBackground(SystemColor.menu);
		getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, 895, 594);
		getContentPane().add(splitPane);
		
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel label = new JLabel("StatistiPark");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(0, 0, 230, 82);
		leftPanel.add(label);
		label.setForeground(Color.RED);
		label.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 80, 230, 8);
		leftPanel.add(separator);
		
		JLabel lblLoggedin = new JLabel("Logged-in:");
		lblLoggedin.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblLoggedin.setBounds(10, 93, 73, 14);
		leftPanel.add(lblLoggedin);
		
		JLabel lblRole = new JLabel("Role:");
		lblRole.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRole.setBounds(10, 110, 73, 14);
		leftPanel.add(lblRole);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 135, 230, 8);
		leftPanel.add(separator_1);
		
		JLabel lblNewLabel = new JLabel("MENU");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 144, 230, 25);
		leftPanel.add(lblNewLabel);
		
		JButton btnNewButton = new JButton("Run Simulation");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new SimConfigScreen();
			}
		});
		btnNewButton.setBounds(10, 169, 210, 31);
		leftPanel.add(btnNewButton);
		
		JLabel lblNewLabel_1 = new JLabel(Controller.loggedInUser.getUsername());
		lblNewLabel_1.setBounds(83, 93, 99, 14);
		leftPanel.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel(Controller.loggedInUser.getRole());
		lblNewLabel_2.setBounds(83, 110, 99, 14);
		leftPanel.add(lblNewLabel_2);
		
		JButton btnShowResults = new JButton("Show Results");
		btnShowResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setSimulationResultsToRightPanel();
				showScrollBarPanel();
			}
		});
		btnShowResults.setBounds(10, 211, 121, 45);
		leftPanel.add(btnShowResults);
		
		rdbtnTableView = new JRadioButton("Table view");
		rdbtnTableView.setBounds(137, 207, 109, 23);
		leftPanel.add(rdbtnTableView);
		buttonGroup.add(rdbtnTableView);
		
		rdbtnTableView.setSelected(true);
		
		rdbtnMapView = new JRadioButton("Map view");
		rdbtnMapView.setBounds(137, 233, 109, 23);
		leftPanel.add(rdbtnMapView);
		buttonGroup.add(rdbtnMapView);
		
		JLabel lblStatisticsAndReports = new JLabel("Statistics and Reports");
		lblStatisticsAndReports.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatisticsAndReports.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblStatisticsAndReports.setBounds(0, 267, 230, 25);
		leftPanel.add(lblStatisticsAndReports);
		
		JButton btnPsUsageHeatmap = new JButton("PS Usage Heatmap");
		btnPsUsageHeatmap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HashMap<String, Double> parkingSpotUsageMap = DButils.getParkingSpotUsageMap();
				new UsageHeatmapScreen(parkingSpotUsageMap);
			}
		});
		btnPsUsageHeatmap.setBounds(10, 303, 210, 31);
		leftPanel.add(btnPsUsageHeatmap);
		
		JButton btnPsspecificInformation = new JButton("PS-Specific Information");
		btnPsspecificInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ParkingSpotInformationScreen();
			}
		});
		btnPsspecificInformation.setBounds(10, 345, 210, 31);
		leftPanel.add(btnPsspecificInformation);
		
		rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		rightPanel.setLayout(new BorderLayout(0, 0));

		setTitle("StatistiPark");
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		splitPane.setDividerLocation(label.getWidth());
		
		
		//////////////////////////////////////////

		//////////////////////////////////////////
	}

	public void setSimulationResultsToRightPanel() {
		ResultSet rs;
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		JScrollPane scrollPane = new JScrollPane();
		
		rightPanel.removeAll();
		rightPanel.add(scrollPane, BorderLayout.CENTER);
				
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		model = new DefaultTableModel();
		model.addColumn("Start DateTime");
		model.addColumn("Parking Spot");
		model.addColumn("Minutes Occupied");
		
		rs = Controller.DBcon.get_ParkingSpotState_Data(false);
		
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. ResultSet returned enpty.");
			}
			while (rs.next()) {
				LocalDateTime tempDate = rs.getTimestamp(1).toLocalDateTime();
				String PSid = rs.getString(2);
				double minutesOccupied = rs.getDouble(3);

				model.addRow(new Object[]{tempDate, PSid, minutesOccupied});			
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
        sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING)); // secondary sort param
        sorter.setSortKeys(sortKeys);
        
		scrollPane.setViewportView(table);
		
		rightPanel.revalidate();
		rightPanel.repaint();
	}
	
	public void showScrollBarPanel() {
		if (scrollBarPanel != null) {
			scrollBarPanel.removeAll();
			getContentPane().remove(scrollBarPanel);
		}
		scrollBarPanel = new JPanel();
		scrollBarPanel.setBounds(0, 595, 895, 90);
		scrollBarPanel.setLayout(null);
		getContentPane().add(scrollBarPanel);

		JLabel lblTimeLine = new JLabel("Time Line");
		lblTimeLine.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblTimeLine.setBounds(10, 11, 166, 14);
		scrollBarPanel.add(lblTimeLine);
				
		int step   = 1;
		int extent = (int)(0.1*DButils.getTotalTimeOfSimulation());
		int min    = 0;
		int max    = DButils.getTotalTimeOfSimulation();
		pointIntimeValue  = min;
		
		JLabel lblPrediction = new JLabel("");
		lblPrediction.setBounds(590, 62, 305, 14);
		scrollBarPanel.add(lblPrediction);
		
		JScrollBar scrollbar = new JScrollBar(JScrollBar.HORIZONTAL, pointIntimeValue, extent, min, max+extent);
		scrollbar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				pointIntimeValue = arg0.getValue();
				valMinutesPassed.setText(""+pointIntimeValue+"  ("+(pointIntimeValue/60)+"h "+(pointIntimeValue%60)+"m)");

				// Check for parking spots freeing up.
				ArrayList<String> freeingUp = DButils.getFreeingUpParkingSpots(pointIntimeValue+1);
				val_PSGetAvailable.setText(""+ freeingUp.size());
				
				// Predict availability.
				int numOfParkingSpots = Controller.DBcon.getNumOfActiveParkingSpots();
				// TODO check Predictor works (after we create a map of the parking lot)
				if (DButils.occupiedParkingSpotsInDatetime(DButils.getSimulationStart().plusMinutes(pointIntimeValue)).size() == numOfParkingSpots) {// if parking lot is full
					int prediction = 0;
					try {
						prediction = Predictor.getPrediction(pointIntimeValue, numOfParkingSpots);
					} catch (Exception e) {
						e.printStackTrace();
					}
					lblPrediction.setForeground(Color.GREEN);
					lblPrediction.setText("A spot will be free in " +prediction+ " minute");
					if (prediction == 1) {
						lblPrediction.setText(lblPrediction.getText()+".");
					}
					else if(prediction > 1) {
						lblPrediction.setText(lblPrediction.getText()+"s.");
					}
				}
				else {
					lblPrediction.setForeground(Color.BLACK);
					lblPrediction.setText("Parking lot is not full.");
				}
				
				// Set rightPanel to be a new map with relevant value.
				if (rdbtnMapView.isSelected()) {
					setMapToRightPanel(pointIntimeValue);
				}
			}
		});
		scrollbar.setUnitIncrement(step);
			
		scrollbar.setBounds(10, 53, 570, 23);
		scrollBarPanel.add(scrollbar);
		
		JPanel panel = new JPanel();
		panel.setBounds(591, 0, 304, 61);
		scrollBarPanel.add(panel);
		panel.setLayout(new GridLayout(3, 2, 0, 0));
		
		JLabel lblSimulationStart = new JLabel("Simulation Start:");
		panel.add(lblSimulationStart);
		
		JLabel lblSimStart = new JLabel(""+DButils.getSimulationStart());
		panel.add(lblSimStart);
		
		JLabel lblMinutesPassed = new JLabel("Minutes passed:");
		panel.add(lblMinutesPassed);

		valMinutesPassed = new JLabel(""+pointIntimeValue+"  ("+(pointIntimeValue/60)+"h "+(pointIntimeValue%60)+"m)");
		panel.add(valMinutesPassed);
		
		JLabel lblPsGetAvailable = new JLabel("PS get available:");
		lblPsGetAvailable.setToolTipText("The number of parking spots geting available after this minute.");
		panel.add(lblPsGetAvailable);
		
		ArrayList<String> freeingUp = DButils.getFreeingUpParkingSpots(pointIntimeValue+1);
		val_PSGetAvailable = new JLabel(""+ freeingUp.size());
		panel.add(val_PSGetAvailable);
		
		// Set rightPanel to the relevant view.
		if (rdbtnTableView.isSelected()) {
			setSimulationResultsToRightPanel();
		}
		else if (rdbtnMapView.isSelected()) {
			setMapToRightPanel(pointIntimeValue);
		}
						
//		scrollBarPanel.revalidate();
//		scrollBarPanel.repaint();
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	private void setMapToRightPanel(int pointInTime) {
		rightPanel.removeAll();
		rightPanel.setLayout(new BorderLayout());
		
		int numOfParkingSpots = Controller.DBcon.getNumOfActiveParkingSpots();
		LocalDateTime simStart = DButils.getSimulationStart();
		ArrayList<String> occupiedParkingSpots = DButils.occupiedParkingSpotsInDatetime(simStart.plusMinutes(pointIntimeValue));
		
		rightPanel.add(new MapCanvas(numOfParkingSpots, occupiedParkingSpots, 125, 125), BorderLayout.CENTER);
		
		rightPanel.revalidate();
		rightPanel.repaint();
	}
}
