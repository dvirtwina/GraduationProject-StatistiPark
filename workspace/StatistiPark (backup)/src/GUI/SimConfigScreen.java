package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JSeparator;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import MainModule.Controller;
import SimulationModule.SimStats;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SimConfigScreen extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int INFINITY_VAL = 9999;

	private JTextField textField;
	private JTextField textField_1;
	private JTable table;
	private DefaultTableModel model;

	public SimConfigScreen() {
		ArrayList<String> a = new ArrayList<>();
		a.add("Alive");
		if (!Controller.guiScheduler.containsKey("SimConfigScreen")) {
			Controller.guiScheduler.put("SimConfigScreen", a);
		} else if (Controller.guiScheduler.get("SimConfigScreen").contains("Dead")) {
			Controller.guiScheduler.get("SimConfigScreen").remove("Dead");
			Controller.guiScheduler.get("SimConfigScreen").add("Alive");
		}

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setSize(786, 403);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);

		setTitle("Simulation Configuration");
		getContentPane().setLayout(null);

		JPanel fieldsPanel = new JPanel();
		fieldsPanel.setBounds(0, 0, 320, 364);
		getContentPane().add(fieldsPanel);
		fieldsPanel.setLayout(null);

		JLabel lblFillInThe = new JLabel("Fill in the following fields to create a simulation");
		lblFillInThe.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFillInThe.setBounds(10, 11, 255, 26);
		fieldsPanel.add(lblFillInThe);

		JLabel lblNumberOfParking = new JLabel("Number of parking spots:");
		lblNumberOfParking.setBounds(10, 48, 154, 14);
		fieldsPanel.add(lblNumberOfParking);

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] { "5", "50" }));
		comboBox.setBounds(245, 48, 65, 20);
		fieldsPanel.add(comboBox);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 73, 300, 2);
		fieldsPanel.add(separator);

		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 35, 300, 2);
		fieldsPanel.add(separator_1);

		JLabel lblConfigureIntervalsIn = new JLabel("Configure intervals in the simulation.");
		lblConfigureIntervalsIn.setBounds(10, 85, 300, 14);
		fieldsPanel.add(lblConfigureIntervalsIn);

		JLabel lblMakeSureThe = new JLabel("Make sure the intervals you add are consistent ");
		lblMakeSureThe.setBounds(10, 99, 300, 14);
		fieldsPanel.add(lblMakeSureThe);

		JLabel lblAndSuccessive = new JLabel("and successive.");
		lblAndSuccessive.setBounds(10, 110, 300, 14);
		fieldsPanel.add(lblAndSuccessive);

		DatePickerSettings settings_1 = new DatePickerSettings();
		settings_1.setFirstDayOfWeek(DayOfWeek.SUNDAY);
		settings_1.setFormatForDatesCommonEra("dd/MM/yyyy");
		DatePicker datePicker = new DatePicker(settings_1);
		datePicker.setBounds(163, 141, 147, 27);
		datePicker.getComponentToggleCalendarButton().setBounds(98, 0, 39, 17);
		datePicker.getComponentDateTextField().setBounds(0, 0, 95, 17);
		datePicker.setDateToToday();
		fieldsPanel.add(datePicker);

		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(10, 147, 98, 14);
		fieldsPanel.add(lblDate);

		TimePickerSettings settings_2 = new TimePickerSettings();
		settings_2.setFormatForDisplayTime("hh:mm");
		settings_2.setDisplaySpinnerButtons(true);
		settings_2.setInitialTimeToNow();
		settings_2.use24HourClockFormat();
		TimePicker timePicker = new TimePicker(settings_2);
		timePicker.getComponentDecreaseSpinnerButton().setBounds(-10178, -10064, 0, 0);
		timePicker.getComponentIncreaseSpinnerButton().setBounds(-10178, -10064, 0, 0);
		timePicker.getComponentSpinnerPanel().setBounds(76, 0, 0, 23);
		timePicker.getComponentToggleTimeMenuButton().setBounds(98, 0, 39, 17);
		timePicker.getComponentTimeTextField().setBounds(0, 0, 90, 17);
		timePicker.setBounds(163, 179, 147, 26);
		timePicker.setTimeToNow();
		fieldsPanel.add(timePicker);

		JLabel lblTime = new JLabel("Time:");
		lblTime.setBounds(10, 184, 98, 14);
		fieldsPanel.add(lblTime);

		JComboBox<String> comboBox_1 = new JComboBox<String>();
		comboBox_1.setModel(new DefaultComboBoxModel<String>(new String[] { "0.5", "1", "1.5", "2", "2.5", "3", "3.5",
				"4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10", "10.5", "11", "11.5",
				"12", "12.5", "13", "13.5", "14", "14.5", "15", "15.5", "16", "16.5", "17", "17.5", "18", "18.5", "19",
				"19.5", "20", "20.5", "21", "21.5", "22", "22.5", "23", "23.5", "24" }));
		comboBox_1.setBounds(163, 216, 147, 26);
		comboBox_1.setSelectedIndex(1);
		fieldsPanel.add(comboBox_1);

		JLabel lblDurationhours = new JLabel("Duration (hours):");
		lblDurationhours.setBounds(10, 222, 98, 14);
		fieldsPanel.add(lblDurationhours);

		textField = new JTextField();
		textField.setBounds(163, 253, 147, 26);
		fieldsPanel.add(textField);
		textField.setColumns(10);

		JLabel lblMeanTimeminutes = new JLabel("Mean time (minutes)");
		lblMeanTimeminutes.setToolTipText("The mean time, in minutes, between cars entering the parking lot.");
		lblMeanTimeminutes.setBounds(10, 253, 141, 14);
		fieldsPanel.add(lblMeanTimeminutes);

		JLabel lblNewLabel = new JLabel("of car entrance:");
		lblNewLabel.setToolTipText("The mean time, in minutes, between cars entering the parking lot.");
		lblNewLabel.setBounds(10, 265, 143, 14);
		fieldsPanel.add(lblNewLabel);

		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(163, 290, 147, 26);
		fieldsPanel.add(textField_1);

		JLabel label = new JLabel("Mean time (minutes)");
		label.setToolTipText("The mean time, in minutes, it takes for a car to leave the parking lot.");
		label.setBounds(10, 290, 141, 14);
		fieldsPanel.add(label);

		JLabel lblOfCarExit = new JLabel("of car exit:");
		lblOfCarExit.setToolTipText("The mean time, in minutes, it takes for a car to leave the parking lot.");
		lblOfCarExit.setBounds(10, 302, 141, 14);
		fieldsPanel.add(lblOfCarExit);

		JButton btnAddInterval = new JButton("Add Interval");
		btnAddInterval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					int day = datePicker.getDate().getDayOfMonth();
					int month = datePicker.getDate().getMonthValue();
					int year = datePicker.getDate().getYear();
					int hour = timePicker.getTime().getHour();
					int minute = timePicker.getTime().getMinute();
					LocalDateTime datetime = LocalDateTime.of(year, month, day, hour, minute);

					double duration = Double.parseDouble(String.valueOf(comboBox_1.getSelectedItem()));

					double enterRate = Double.parseDouble(textField.getText());
					double exitRate = Double.parseDouble(textField_1.getText());

					if (checkModelDiscrepancis(datetime, duration)) {
						if (enterRate > 0 && exitRate > 0) {
							model.addRow(new Object[] { datetime, duration, enterRate, exitRate });
							table.setModel(model);
	
							// Sort the table by datetime.
							TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(table.getModel());
							table.setRowSorter(sorter);
							List<RowSorter.SortKey> sortKeys = new ArrayList<>();
							sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING)); // primary sort param
							// sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING)); // secondary sort param
							sorter.setSortKeys(sortKeys);
						}
						else {
							JOptionPane.showMessageDialog(null, "Rates should be filled with a positive number.");
						}
					}
				} catch (NumberFormatException | NullPointerException e) {
					JOptionPane.showMessageDialog(null, "Rates should be filled with a positive number.");
				}
			}

			private boolean checkModelDiscrepancis(LocalDateTime datetime, double duration) {
				// TODO CHECK THAT DATETIME AND DURATION DOESN'T OVERLAP MODEL'S
				// EXISTING DATETIMES.
				return true;
			}
		});
		btnAddInterval.setBounds(194, 327, 116, 26);
		fieldsPanel.add(btnAddInterval);

		JButton btnDeleteSelected = new JButton("Delete Selected");
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int a[] = table.getSelectedRows();
				List<Integer> l = Arrays.stream(a).boxed().collect(Collectors.toList());
				ArrayList<Integer> list = new ArrayList<>(l);

				if (model.getRowCount() > 0) {
					for (int i = model.getRowCount() - 1; i > -1; i--) {
						if (list.contains(i)) {
							model.removeRow(i);
						}

					}
				}
			}
		});
		btnDeleteSelected.setBounds(56, 327, 128, 26);
		fieldsPanel.add(btnDeleteSelected);

		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(319, 0, 451, 329);
		getContentPane().add(tablePanel);
		tablePanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 431, 307);
		tablePanel.add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		model = new DefaultTableModel();
		model.addColumn("Start DateTime");
		model.addColumn("Duration");
		model.addColumn("Entrance Rate");
		model.addColumn("Exit Rate");

		JButton btnRunSimulation = new JButton("Run Simulation");
		btnRunSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (model.getRowCount() > 0) {
					// Create a simulation series:
					ArrayList<SimStats> simScheduler = new ArrayList<SimStats>();
					Controller.setSchedulerIntervalIndex(0);

					// Set the number of parking spots in the simulation.
					Controller.setNumOfParkingSpots(Integer.parseInt(String.valueOf(comboBox.getSelectedItem())));

					// Create a list of intervals.
					for (int i = 0; i < model.getRowCount(); i++) {
						LocalDateTime startDate = (LocalDateTime) model.getValueAt(i, 0);
						int duration = (int) ((double) model.getValueAt(i, 1) * 60);
						double enterRate = (double) model.getValueAt(i, 2);
						double exitRate = (double) model.getValueAt(i, 3);
						
						// Defining 0 rate is infinity.
						// TODO USAGE OF INFINITY_VAL IS
						// BAD PRACTICE!!! IT SHOULD BE
						// CHANGED. UNTILL THEN,
						// INPUTTING 0 SHOULD BE
						// AVOIDED!
						if (enterRate == 0) { 
							enterRate = INFINITY_VAL;
						}
						if (exitRate == 0) {
							exitRate = INFINITY_VAL;
						}

						simScheduler.add(new SimStats(startDate, duration, enterRate, exitRate));
					}

					Controller.setSimScheduler(simScheduler);
					Controller.guiScheduler.get("SimConfigScreen").add("RunSimulation");
					closeFrame();
				} else {
					JOptionPane.showMessageDialog(null, "Please add at least one interval.");
				}
			}
		});
		btnRunSimulation.setBounds(629, 330, 131, 23);
		getContentPane().add(btnRunSimulation);

		// Load previous intervals to table
		ResultSet rs = Controller.DBcon.get_Intervals_data();
		try {
			if (!rs.isBeforeFirst()) {
				System.out.println("No data. INTERVALS table is empty.");
			} else {
				while (rs.next()) {
					LocalDateTime intervalStart = rs.getTimestamp(1).toLocalDateTime();
					double duration = rs.getDouble(2) / 60;
					double rateEnter = rs.getDouble(3);
					double rateExit = rs.getDouble(4);
					if (rateEnter == INFINITY_VAL) {
						rateEnter = 0;
					}
					if (rateExit == INFINITY_VAL) {
						rateExit = 0;
					}

					model.addRow(new Object[] { intervalStart, duration, rateEnter, rateExit });
					table.setModel(model);
				}
				table.setModel(model);
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
	}

	private void closeFrame() {
		super.dispose();
	}
}
