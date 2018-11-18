package GUI;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.JTextField;
import MainModule.Controller;
import Objects.User;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class LoginScreen extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField textField;
	private JPasswordField passwordField;
	
	public LoginScreen() {
		ArrayList<String> a = new ArrayList<>();
		a.add("Alive");
		Controller.guiScheduler.put("LoginScreen", a);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setSize(440, 300);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
		
		this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
            	Controller.DBcon.closeConnection();
                System.exit(0);
            }
        });
		
		getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		JLabel lblStatistipark = new JLabel("StatistiPark");
		lblStatistipark.setForeground(Color.RED);
		lblStatistipark.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 22));
		lblStatistipark.setBounds(136, 11, 153, 33);
		getContentPane().add(lblStatistipark);
		
		JLabel lblWelcomeToStatistipark = new JLabel("Welcome to StatistiPark.");
		lblWelcomeToStatistipark.setBounds(10, 66, 153, 14);
		getContentPane().add(lblWelcomeToStatistipark);
		
		JLabel lblPleaseLogIn = new JLabel("Please log In.");
		lblPleaseLogIn.setBounds(10, 91, 153, 14);
		getContentPane().add(lblPleaseLogIn);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 139, 125, 14);
		getContentPane().add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(136, 136, 86, 20);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(10, 164, 125, 14);
		getContentPane().add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(136, 161, 86, 20);
		getContentPane().add(passwordField);
		
		
		JButton btnLogin = new JButton("Log-In");
		
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		passwordField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					btnLogin.doClick();
				}
			}
		});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs = null;
				rs = Controller.DBcon.get_Users_data();
				boolean isUserExist = false;
				
				try {
					if (!rs.isBeforeFirst()) {
						System.out.println("No data. ResultSet returned enpty.");
					}
					String inputUser = textField.getText();
					String inputPass = new String(passwordField.getPassword());
					while (rs.next()) {
						String username = rs.getString(1);
						String password = rs.getString(2);
						String role = rs.getString(3);

						if (username.equals(inputUser) && password.equals(inputPass)) {
							JOptionPane.showMessageDialog(null,"Welcome, "+username+"!");
							isUserExist = true;
							Controller.loggedInUser = new User(username, role);
							
							Controller.guiScheduler.remove("LoginScreen");
							closeFrame();
						}
					}
					if (isUserExist == false) {
						JOptionPane.showMessageDialog(null,"Username or Password incorrect");
					}

					if (rs != null) {
						rs.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnLogin.setBounds(325, 227, 89, 23);
		getContentPane().add(btnLogin);
		setTitle("Log In");
	}
	
	private void closeFrame() {
		super.dispose();
	}
}
