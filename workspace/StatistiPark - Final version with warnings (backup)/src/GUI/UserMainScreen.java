package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JFrame;

import MainModule.Controller;
import PredictionModule.Predictor;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SwingConstants;

import Database.DButils;

import javax.swing.JSplitPane;
import javax.swing.JSeparator;

public class UserMainScreen extends JFrame{
	private int pointIntimeValue;
	private JPanel rightPanel;
	
	private int numOfParkingSpots;
	
	private JLabel lblFree;
	private JLabel lblOccupied;
	
	public UserMainScreen() {
		setTitle("StatistiPark");
		ArrayList<String> a = new ArrayList<>();
		a.add("Alive");
		Controller.guiScheduler.put("UserMainScreen", a);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(850, 680);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, (dim.height / 2 - this.getSize().height / 2));
		getContentPane().setLayout(null);
		
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	Controller.DBcon.closeConnection();
                System.exit(0);
            }
        });
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBounds(0, 0, 834, 550);
		getContentPane().add(splitPane);
		
		JPanel leftPanel = new JPanel();
		splitPane.setLeftComponent(leftPanel);
		leftPanel.setLayout(null);
		
		JLabel label = new JLabel("StatistiPark");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(Color.RED);
		label.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		label.setBounds(0, 0, 223, 74);
		leftPanel.add(label);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 72, 223, 2);
		leftPanel.add(separator);
		
		JLabel label_1 = new JLabel("Logged-in:");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(10, 85, 73, 14);
		leftPanel.add(label_1);
		
		JLabel label_2 = new JLabel("Role:");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_2.setBounds(10, 104, 73, 14);
		leftPanel.add(label_2);
		
		JLabel label_3 = new JLabel(Controller.loggedInUser.getUsername());
		label_3.setBounds(93, 85, 99, 14);
		leftPanel.add(label_3);
		
		JLabel label_4 = new JLabel(Controller.loggedInUser.getRole());
		label_4.setBounds(93, 104, 99, 14);
		leftPanel.add(label_4);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(0, 129, 223, 2);
		leftPanel.add(separator_1);
		
		JLabel lblStatus = new JLabel("STATUS");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblStatus.setBounds(0, 135, 223, 25);
		leftPanel.add(lblStatus);
		
		JLabel lblParkingSpots = new JLabel("Parking spots:");
		lblParkingSpots.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblParkingSpots.setBounds(10, 171, 146, 14);
		leftPanel.add(lblParkingSpots);
		
		numOfParkingSpots =  Controller.getNumOfParkingSpots();
		JLabel lblNumOfParkingSpots = new JLabel(""+numOfParkingSpots);
		lblNumOfParkingSpots.setBounds(166, 171, 46, 14);
		leftPanel.add(lblNumOfParkingSpots);
		
		JLabel lblFreeParkingSpots = new JLabel("Free:");
		lblFreeParkingSpots.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFreeParkingSpots.setBounds(10, 196, 146, 14);
		leftPanel.add(lblFreeParkingSpots);
		
		ArrayList<String> occupiedPS = DButils.occupiedParkingSpotsInDatetime(DButils.getSimulationStart());
		lblFree = new JLabel("" + (numOfParkingSpots - occupiedPS.size()));
		lblFree.setBounds(166, 196, 46, 14);
		leftPanel.add(lblFree);
		
		lblOccupied = new JLabel("" + occupiedPS.size());
		lblOccupied.setBounds(166, 221, 46, 14);
		leftPanel.add(lblOccupied);
		
		JLabel lblOccupied_1 = new JLabel("Occupied:");
		lblOccupied_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblOccupied_1.setBounds(10, 221, 146, 14);
		leftPanel.add(lblOccupied_1);
		
		rightPanel = new JPanel();
		splitPane.setRightComponent(rightPanel);
		
		try {
			Thread.sleep(50);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		splitPane.setDividerLocation(label.getWidth());
			
		showScrollBarPanel();
	}
	
	public void showScrollBarPanel() {
//		if (scrollBarPanel != null) {
//			scrollBarPanel.removeAll();
//			getContentPane().remove(scrollBarPanel);
//		}
		JPanel scrollBarPanel = new JPanel();
		scrollBarPanel.setBounds(0, 550, 834, 90);
		scrollBarPanel.setLayout(null);
		getContentPane().add(scrollBarPanel);

		JLabel lblTimeLine = new JLabel("Time Line");
		lblTimeLine.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblTimeLine.setBounds(10, 11, 166, 14);
		scrollBarPanel.add(lblTimeLine);
		
		JLabel lblMinutesPassed = new JLabel("Minutes passed:");
		lblMinutesPassed.setBounds(591, 37, 110, 14);
		scrollBarPanel.add(lblMinutesPassed);
				
		int step   = 1;
		int extent = (int)(0.1*DButils.getTotalTimeOfSimulation());
		int min    = 0;
		int max    = DButils.getTotalTimeOfSimulation();
		pointIntimeValue  = min;

		JLabel lblNewLabel_3 = new JLabel(""+pointIntimeValue+"  ("+(pointIntimeValue/60)+"h "+(pointIntimeValue%60)+"m)");
		lblNewLabel_3.setBounds(711, 37, 87, 14);
		scrollBarPanel.add(lblNewLabel_3);
		
		JLabel lblPrediction = new JLabel("");
		lblPrediction.setBounds(590, 62, 234, 14);
		scrollBarPanel.add(lblPrediction);
		
		JLabel lblSimulationStart = new JLabel("Simulation Start:");
		lblSimulationStart.setBounds(591, 12, 110, 14);
		scrollBarPanel.add(lblSimulationStart);
		
		JLabel lblSimStart = new JLabel(""+DButils.getSimulationStart());
		lblSimStart.setBounds(711, 12, 87, 14);
		scrollBarPanel.add(lblSimStart);
		
		JScrollBar scrollbar = new JScrollBar(JScrollBar.HORIZONTAL, pointIntimeValue, extent, min, max+extent);
		scrollbar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				pointIntimeValue = arg0.getValue();
				
				// Update left panel 
				ArrayList<String> currentlyOccupied = DButils.occupiedParkingSpotsInDatetime(DButils.getSimulationStart().plusMinutes(pointIntimeValue));
				lblFree.setText("" + (numOfParkingSpots - currentlyOccupied.size()));
				lblOccupied.setText("" + currentlyOccupied.size());
				
				// Update scrollbar panel
				lblNewLabel_3.setText(""+pointIntimeValue+"  ("+(pointIntimeValue/60)+"h "+(pointIntimeValue%60)+"m)");
				
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
				setMapToRightPanel(pointIntimeValue);
				
			}
		});
		scrollbar.setUnitIncrement(step);
			
		scrollbar.setBounds(10, 53, 570, 23);
		scrollBarPanel.add(scrollbar);
		
		// Set rightPanel to a map.
		setMapToRightPanel(pointIntimeValue);
						
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
		
		rightPanel.add(new MapCanvas(numOfParkingSpots, occupiedParkingSpots, 100, 100), BorderLayout.CENTER);
		
		rightPanel.revalidate();
		rightPanel.repaint();
	}
}
