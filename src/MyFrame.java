
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.NumberFormatter;

import BankExceptions.EmptyFieldsException;
import BankExceptions.IBANNotFoundException;
import BankExceptions.IncorrectDataException;
import BankExceptions.LittleMoneyException;
import BankExceptions.NotSelectedIBANException;
import BankExceptions.UnconfirmedTransactionException;
import DBConnection.JDBConnection;
import DBConnection.SQLCommands;
import DocumentListener.MyDocumentListener;
import Encryption.Encryption;
import Entities.Admin;
import Entities.User;

public class MyFrame {

	public MyFrame() {

		mainFrame();
		
	}
	private String[] ibans;
	JComboBox<String> ibanBox = new JComboBox<>();
	private String selectedIBAN = "";
	private User user;
	@SuppressWarnings("deprecation")
	private void mainFrame() {		
		JFrame mainFrame = new JFrame();
//---------------------------------------------------------------------------------------------------------------------------				
		JLabel titleLabel = new JLabel();
		
		JLabel loginLabel = new JLabel();
		JLabel loginLoginLabel = new JLabel("login:");
		JLabel loginPasswordLabel = new JLabel("password:");
		
		JLabel logedLabel = new JLabel();
		JLabel transferLabel = new JLabel("transfer");
		JLabel toLabel = new JLabel("to");
		JLabel amountLabel = new JLabel("amount");
		JLabel fromLabel = new JLabel("from:");
		JLabel balanceLabel = new JLabel("Balance: ");
		
		JLabel registerLabel = new JLabel();
		JLabel regNameLabel = new JLabel("Name:");
		JLabel regFNameLabel = new JLabel("Last name:");
		JLabel regLoginLabel = new JLabel("Login:");
		JLabel regPassLabel = new JLabel("Password:");
		JLabel regMailLabel = new JLabel("Email:");
//---------------------------------------------------------------------------------------------------------------------------
		JButton homeButton = new JButton("Home");
		JButton logoutButton = new JButton("logout");
		JButton loginButton = new JButton("login");
		JButton loginSubmitButton = new JButton("submit");
		
		JButton registerButton = new JButton("register");
		JButton regSubmitButton = new JButton("submit");
		
		JButton manageAccountButton = new JButton("<html>Manage<br>account</html>");
		
		JButton transferButton = new JButton("transfer");
		JButton depositButton = new JButton("deposit");
		JButton withdrawButton = new JButton("withdraw");
		
		JButton clearButton = new JButton("Clear");
//---------------------------------------------------------------------------------------------------------------------------
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		
		JTextField regUsernameField = new JTextField();
		JPasswordField regPasswordField = new JPasswordField();
		JTextField regNameField = new JTextField();
		JTextField regLastnameField = new JTextField();
		JTextField regEmailField = new JTextField();
		
		JTextField transferToField = new JTextField();
//---------------------------------------------------------------------------------------------------------------------------		
	    NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
//---------------------------------------------------------------------------------------------------------------------------
	    JFormattedTextField amountTransferField = new JFormattedTextField(formatter);
	    JFormattedTextField amountCoinsTransferField = new JFormattedTextField(formatter);
	    JFormattedTextField amountMoneyDWField = new JFormattedTextField(formatter);
	    JFormattedTextField amountCoinsDWField = new JFormattedTextField(formatter);
//---------------------------------------------------------------------------------------------------------------------------	
	    JCheckBox iKnowWhatIAmDoing = new JCheckBox("I know what I am doing!");
//---------------------------------------------------------------------------------------------------------------------------		
	    
		
		manageAccountButton.setBounds(535, 0, 90, 40);
		manageAccountButton.setFocusable(false);
		manageAccountButton.addActionListener(e ->{
			mainFrame.dispose();
			if(user.getClass() == Admin.class) {
				adminAccountManagement(mainFrame, user);
			}else {
				clientAccountManagement(mainFrame, user);
			}
		});
		
		homeButton.setBounds(537, 462, 89, 40);
		homeButton.setFocusable(false);
		homeButton.addActionListener(e -> {
			titleLabel.setVisible(true);
			registerButton.setVisible(true);
			loginButton.setVisible(true);
			loginLabel.setVisible(false);
			registerLabel.setVisible(false);
		});
		logoutButton.setBounds(537, 462, 89, 40);
		logoutButton.setFocusable(false);
		logoutButton.addActionListener(e ->{
			logoutButton.setVisible(false);
			homeButton.setVisible(true);
			loginButton.setVisible(true);
			registerButton.setVisible(true);
			titleLabel.setVisible(true);
			manageAccountButton.setVisible(false);
			logedLabel.setVisible(false);
			ibanBox.removeAllItems();
			transferToField.setText("BGBGGKPBEU12345678");
			balanceLabel.setText("Balance:");
			selectedIBAN = "";
			amountMoneyDWField.setText("0");
			amountCoinsDWField.setText("0");
			amountTransferField.setText("0");
			amountCoinsTransferField.setText("0");
		});

		titleLabel.setText("We care about your money");
		titleLabel.setForeground(Color.lightGray);
		titleLabel.setBounds(30, 50, 640, 320);
		titleLabel.setFont(new Font("Castellar", Font.ITALIC, 32));
		titleLabel.setIcon(new ImageIcon("bank.png"));
//		ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("bank.png"));
//		titleLabel.setIcon(icon);
		titleLabel.setVerticalTextPosition(JLabel.TOP);
		titleLabel.setHorizontalTextPosition(JLabel.CENTER);
		
		logedLabel.setBounds(0, 0, 640, 540);
			logedLabel.add(manageAccountButton);
			logedLabel.add(transferLabel);
			logedLabel.add(toLabel);
			logedLabel.add(amountLabel);
			logedLabel.add(fromLabel);
			logedLabel.add(transferToField);
			logedLabel.add(amountTransferField);
			logedLabel.add(transferButton);
			logedLabel.add(amountCoinsTransferField);
			logedLabel.add(amountMoneyDWField);
			logedLabel.add(amountCoinsDWField);
			logedLabel.add(balanceLabel);
			logedLabel.add(depositButton);
			logedLabel.add(withdrawButton);
			logedLabel.add(clearButton);
			logedLabel.add(iKnowWhatIAmDoing);

			ibanBox.addActionListener(e -> {
				try {
					if(amountTransferField.getText().length()!=0 && amountCoinsTransferField.getText().length()!=0) {
			    		transferButton.setEnabled(true);
			    	}
					selectedIBAN = ibanBox.getSelectedItem().toString();
					balanceLabel.setText("Balance: " + Encryption.getPersonBalance(selectedIBAN));
				}catch(NullPointerException e1) {

				}catch (Exception e2) {
					System.out.println(e2);
				}
			});
			
			transferLabel.setBounds(160, 100, 100, 40);
			transferLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			transferLabel.setForeground(Color.white);
			toLabel.setBounds(55, 140, 45, 30);
			toLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			toLabel.setForeground(Color.white);
			amountLabel.setBounds(30, 170, 70, 30);
			amountLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			amountLabel.setForeground(Color.white);
			fromLabel.setBounds(430, 100, 100, 40);
			fromLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			fromLabel.setForeground(Color.white);
			transferToField.setBounds(120, 143, 175, 23);
			transferToField.setText("BGBGGKPBEU12345678");
			amountTransferField.setBounds(120, 173, 100, 23);
			amountCoinsTransferField.setBounds(235, 173, 60, 23);
			amountCoinsTransferField.setValue(0);
			transferButton.setBounds(160, 201, 85, 27);
			transferButton.setFont(new Font("Calibri", Font.ITALIC, 15));
			transferButton.setFocusable(false);
			transferButton.setEnabled(false);
			transferButton.addActionListener(e ->{
				try {	
					if(checkIsExistingIBAN(transferToField.getText()) == false) 
						throw new IBANNotFoundException();
				} catch (IBANNotFoundException inf) {	return;	}
				try {
					if(Integer.parseInt(amountTransferField.getValue().toString()) == 0)
						throw new LittleMoneyException();
				} catch (LittleMoneyException e3) {	return;	}
				
				user.transfer(convertMoney(amountTransferField.getValue().toString(),
						amountCoinsTransferField.getValue().toString()),
						selectedIBAN, transferToField.getText());
				amountTransferField.setValue(0);
				amountCoinsTransferField.setValue(0);
				transferButton.setEnabled(false);
				try {
	    			Thread.sleep(10);
		    		balanceLabel.setText("Balance: " + Encryption.getPersonBalance(selectedIBAN));
				} catch (Exception e2) {
					System.err.println(e2);
				}
			});
			balanceLabel.setBounds(380, 135, 150, 30);
			balanceLabel.setFont(new Font("Calibri", Font.PLAIN, 18));
			balanceLabel.setForeground(Color.green);
			
		    amountTransferField.getDocument().addDocumentListener((MyDocumentListener) e -> {
		    	if(amountTransferField.getText().length()!=0 && 
		    			amountCoinsTransferField.getText().length()!=0 && 
		    			selectedIBAN.length()==20) 
		    	{
		    		transferButton.setEnabled(true);
		    	}
		    });

		    amountMoneyDWField.setBounds(120, 320, 100, 23);
		    amountCoinsDWField.setBounds(235, 320, 60, 23);
		    amountCoinsDWField.setValue(0);
		    
			depositButton.setBounds(310, 320, 90, 25);
			depositButton.setEnabled(false);
			depositButton.addActionListener(e ->{
				try {
					if(selectedIBAN.length() == 0)
						throw new NotSelectedIBANException();
				} catch (NotSelectedIBANException e3) {	return;	}
				try {
					if(!iKnowWhatIAmDoing.isSelected()) 
						throw new UnconfirmedTransactionException();
				} catch(UnconfirmedTransactionException ute) {	return;	}
				try {
					if(Integer.parseInt(amountMoneyDWField.getValue().toString()) == 0)
						throw new LittleMoneyException();
				} catch (LittleMoneyException e3) {	return;	}
				user.deposit(convertMoney(amountMoneyDWField.getValue().toString(),
						amountCoinsDWField.getValue().toString()), selectedIBAN);
				amountCoinsDWField.setValue(0);
				amountMoneyDWField.setValue(null);
				depositButton.setEnabled(false);
	    		withdrawButton.setEnabled(false);
	    		iKnowWhatIAmDoing.setSelected(false);
	    		try {
	    			Thread.sleep(10);
		    		balanceLabel.setText("Balance: " + Encryption.getPersonBalance(selectedIBAN));
				} catch (Exception e2) {
					System.err.println(e2);
				}
			});
			amountMoneyDWField.getDocument().addDocumentListener((MyDocumentListener) e2 -> {
		    	if(amountMoneyDWField.getText().toString().length()!=0 && amountCoinsDWField.getText().toString().length()!=0) {
		    		depositButton.setEnabled(true);
		    		withdrawButton.setEnabled(true);
		    	}
		    });
			withdrawButton.setEnabled(false);
			withdrawButton.setBounds(310, 350, 90, 25);
			withdrawButton.addActionListener(e ->{
				try {
					if(selectedIBAN.length() == 0)
						throw new NotSelectedIBANException();
				} catch (NotSelectedIBANException e3) {	return;	}
				try {
					if(!iKnowWhatIAmDoing.isSelected()) 
						throw new UnconfirmedTransactionException();
				} catch(UnconfirmedTransactionException ute) {	return;	}
				try {
					if(Integer.parseInt(amountMoneyDWField.getValue().toString()) == 0)
						throw new LittleMoneyException();
				} catch (LittleMoneyException e3) {	return;	}

				user.withdraw(convertMoney(amountMoneyDWField.getValue().toString(), amountCoinsDWField.getValue().toString())
						, selectedIBAN);
				
				amountCoinsDWField.setValue(0);
				amountMoneyDWField.setValue(0);
				depositButton.setEnabled(false);
	    		withdrawButton.setEnabled(false);
	    		iKnowWhatIAmDoing.setSelected(false);
	    		try {
	    			Thread.sleep(10);
		    		balanceLabel.setText("Balance: " + Encryption.getPersonBalance(selectedIBAN));
				} catch (Exception e2) {
					System.err.println(e2);
				}
			});
			
			iKnowWhatIAmDoing.setBounds(120, 345, 180, 23);
			iKnowWhatIAmDoing.setFont(new Font("Calibri", Font.ITALIC, 15));
			iKnowWhatIAmDoing.setForeground(Color.white);
			iKnowWhatIAmDoing.setBackground(new Color(36, 39, 171));
			iKnowWhatIAmDoing.setFocusable(false);
			
			clearButton.setBounds(420, 335, 80, 23);
			clearButton.setFocusable(false);
			clearButton.addActionListener(e ->{
				amountCoinsTransferField.setValue(null);
				amountMoneyDWField.setValue(null);
				transferButton.setEnabled(false);
				depositButton.setEnabled(false);
				withdrawButton.setEnabled(false);
				transferToField.setText("BGBGGKPBEU12345678");
			});
			
		usernameField.setBounds(170, 170, 150, 25);
		passwordField.setBounds(170, 220, 150, 25);
		
		loginLabel.setBounds(0, 0, 640, 500);
		loginLabel.setIcon(new ImageIcon("hello.png"));
		loginLabel.setHorizontalAlignment(JLabel.CENTER);
		loginLabel.setVerticalAlignment(JLabel.BOTTOM);
		loginLabel.add(usernameField);
		loginLabel.add(passwordField);
		loginLabel.add(loginLoginLabel);
		loginLabel.add(loginPasswordLabel);
		loginLabel.add(loginSubmitButton);
			loginButton.addActionListener(e ->{
				loginLabel.setVisible(true);
				titleLabel.setVisible(false);
				registerButton.setVisible(false);
				loginButton.setVisible(false);
			});
			loginButton.setBounds(165, 410, 140, 80);
			loginButton.setFocusable(false);
			loginButton.setFont(new Font("Bell MT", Font.ITALIC, 32));
			loginButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
			
			loginLoginLabel.setForeground(Color.white);
			loginLoginLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			loginLoginLabel.setBounds(90, 167, 65, 35);
			loginPasswordLabel.setForeground(Color.white);
			loginPasswordLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
			loginPasswordLabel.setBounds(90, 217, 80, 35);
			loginSubmitButton.setBounds(350, 190, 80, 35);
			loginSubmitButton.setFocusable(false);
			loginSubmitButton.addActionListener(e ->{
				String login = usernameField.getText();
				String password = passwordField.getText();
				try {
					user = Encryption.logIn(login, password);
					if(user == null) {
						throw new IncorrectDataException();
					}else {
						ibans = user.getIbanList();
					for(int i=0 ; i<ibans.length ; i++) {
						ibanBox.insertItemAt(ibans[i], i);
					}
					ibanBox.setBounds(370, 172, 180, 25);
					logedLabel.add(ibanBox);
					usernameField.setText("");
					passwordField.setText("");
					loginLabel.setVisible(false);
					homeButton.setVisible(false);
					logoutButton.setVisible(true);
					manageAccountButton.setVisible(true);
					logedLabel.setVisible(true);
					}
				} catch (IncorrectDataException e2) {
					return;
				}
			});
		
		registerLabel.setBounds(0, 0, 640, 500);
		registerLabel.setIcon(new ImageIcon("welcome.png"));
		registerLabel.setHorizontalAlignment(JLabel.CENTER);
		registerLabel.setVerticalAlignment(JLabel.BOTTOM);

		regUsernameField.setBounds(200, 120, 150, 25);
		regPasswordField.setBounds(200, 160, 150, 25);
		regNameField.setBounds(200, 200, 150, 25);
		regLastnameField.setBounds(200, 240, 150, 25);
		regEmailField.setBounds(200, 280, 150, 25);
		registerLabel.add(regUsernameField);
		registerLabel.add(regPasswordField);
		registerLabel.add(regNameField);
		registerLabel.add(regLastnameField);
		registerLabel.add(regEmailField);
		regNameLabel.setForeground(Color.white);
		regNameLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
		regNameLabel.setBounds(90, 200, 95, 35);
		regFNameLabel.setForeground(Color.white);
		regFNameLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
		regFNameLabel.setBounds(90, 240, 95, 35);
		regLoginLabel.setForeground(Color.white);
		regLoginLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
		regLoginLabel.setBounds(90, 120, 95, 35);
		regPassLabel.setForeground(Color.white);
		regPassLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
		regPassLabel.setBounds(90, 160, 95, 35);
		regMailLabel.setForeground(Color.white);
		regMailLabel.setFont(new Font("Calibri", Font.ITALIC, 19));
		regMailLabel.setBounds(90, 280, 95, 35);
		registerLabel.add(regNameLabel);
		registerLabel.add(regFNameLabel);
		registerLabel.add(regLoginLabel);
		registerLabel.add(regPassLabel);
		registerLabel.add(regMailLabel);
		registerLabel.add(regSubmitButton);

		registerButton.addActionListener(e -> {
			titleLabel.setVisible(false);
			registerButton.setVisible(false);
			loginButton.setVisible(false);
			registerLabel.setVisible(true);
		});
		registerButton.setBounds(320, 410, 140, 80);
		registerButton.setFocusable(false);
		registerButton.setFont(new Font("Bell MT", Font.ITALIC, 32));
		registerButton.setBorder(BorderFactory.createLineBorder(Color.gray, 1));
		
		regSubmitButton.setBounds(380, 195, 80, 35);
		regSubmitButton.setFocusable(false);
		regSubmitButton.addActionListener(e ->{
			try {
				if(	regUsernameField.getText().length() == 0 ||
						regPasswordField.getText().length() == 0 ||
						regNameField.getText().length() == 0 ||
						regLastnameField.getText().length() == 0 ||
						regEmailField.getText().length() == 0) {
						throw new EmptyFieldsException();
					} else {
						String login = regUsernameField.getText();
						String password = regPasswordField.getText();
						String name = regNameField.getText();
						String fname = regLastnameField.getText();
						String mail = regEmailField.getText();
						Encryption.register(name, fname, login, password, mail);
						regUsernameField.setText("");
						regPasswordField.setText("");
						regNameField.setText("");
						regLastnameField.setText("");
						regEmailField.setText("");
						registerLabel.setVisible(false);
						try {
							user = Encryption.logIn(login, password);
							if(user == null) {
								throw new IncorrectDataException();
							}else {
								user.foundNewBalance();
								ibans = user.getIbanList();
								for(int i=0 ; i<ibans.length ; i++) 
									ibanBox.insertItemAt(ibans[i], i);
								
								ibanBox.setBounds(370, 172, 180, 25);
								logedLabel.add(ibanBox);
								logoutButton.setVisible(true);
								manageAccountButton.setVisible(true);
								logedLabel.setVisible(true);
								homeButton.setVisible(false);
							}
						} catch (IncorrectDataException e2) {	return;	}
					}
				} catch (EmptyFieldsException e2) {	return;	}
		});
		mainFrame.add(logedLabel).setVisible(false);;
		mainFrame.add(loginLabel).setVisible(false);
		mainFrame.add(registerLabel).setVisible(false);
		mainFrame.add(registerButton);
		mainFrame.add(loginButton);
		mainFrame.add(homeButton);
		mainFrame.add(titleLabel);
		mainFrame.add(logoutButton).setVisible(false);
		mainFrame.add(manageAccountButton).setVisible(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(null);
		mainFrame.getContentPane().setBackground(new Color(36, 39, 171));
		mainFrame.setSize(640, 540);
		mainFrame.setResizable(false);
		mainFrame.setVisible(true);
	}
	private double convertMoney(String m, String c) {
		double money;
		if(c == null)
			c = "0";
		if(Integer.parseInt(m) == 0) {
			throw new LittleMoneyException();
		}
		if(Integer.parseInt(c) == 0) {
			money = Double.parseDouble(m);
			return money;
		}else {
			money = Double.parseDouble(String.format("%.2f", Double.parseDouble(m) + Double.parseDouble(c)/100));
			return money;
		}
	}
	
	private boolean checkIsExistingIBAN(String iban) {
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
				ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_IBAN);
			){
				while(rs.next())
					if(rs.getString("iban").equals(iban))
						return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	private void changePassword(JFrame parrentFrame, User user) {
		JFrame frame = new JFrame();
		JTextField field1 = new JTextField("New password");
		JTextField field2 = new JTextField("New password");
		JButton button = new JButton("Confirm");
		
		field1.setBounds(20, 20, 180, 27);
		field2.setBounds(20, 53, 180, 27);
		button.setFocusable(false);
		button.setBounds(220, 35, 90, 31);
		button.addActionListener(e ->{
			user.changePass(field1.getText(), field2.getText());
			frame.dispose();
			parrentFrame.setVisible(true);
		});
		
		frame.setVisible(true);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(36, 39, 171));
		frame.setSize(350, 130);
		frame.setResizable(false);
		frame.add(field1);
		frame.add(field2);
		frame.add(button);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                parrentFrame.setVisible(true);
            }
		});
	}
	
	private void changeEmail(JFrame parrentFrame, User user) {
		JFrame frame = new JFrame();
		JTextField field1 = new JTextField("New email");
		JButton button = new JButton("Confirm");
		
		field1.setBounds(20, 30, 180, 27);
		button.setFocusable(false);
		button.setBounds(220, 35, 90, 31);
		button.addActionListener(e ->{
			user.changeEmail(field1.getText());
			frame.dispose();
			parrentFrame.setVisible(true);
		});
		
		frame.setVisible(true);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(36, 39, 171));
		frame.setSize(350, 130);
		frame.setResizable(false);
		frame.add(field1);
		frame.add(button);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                parrentFrame.setVisible(true);
            }
		});
	}
	
//	private void deleteBankAccount(JFrame parrentFrame, User user) {
//	
//	}
	
	private void recoverPassword(JFrame parrentFrame, User user) {
		JFrame frame = new JFrame();
		JTextField field1 = new JTextField("Username");
		JTextField field2 = new JTextField("New password");
		JButton button = new JButton("Confirm");
		
		field1.setBounds(20, 20, 180, 27);
		field2.setBounds(20, 53, 180, 27);
		button.setFocusable(false);
		button.setBounds(220, 35, 90, 31);
		button.addActionListener(e ->{
			user.recoverPass(field1.getText(), field2.getText());
			frame.dispose();
			parrentFrame.setVisible(true);
		});
		
		frame.setVisible(true);
		frame.setLayout(null);
		frame.getContentPane().setBackground(new Color(36, 39, 171));
		frame.setSize(350, 130);
		frame.setResizable(false);
		frame.add(field1);
		frame.add(field2);
		frame.add(button);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                parrentFrame.setVisible(true);
            }
		});
	}
	
	private void adminAccountManagement(JFrame parrentFrame, User user) {
		JFrame accountManagement = new JFrame();

		JButton changePassButton = new JButton("Change password");
		JButton changeEmailButton = new JButton("Change Email");
		JButton deleteBankAccountButton = new JButton("Delete bank account");
		JButton recoverSomeonesPassButton = new JButton("Recover user's password");
		JButton backButton = new JButton("Back");
		
		changePassButton.setBounds(50, 30, 200, 30);
		changePassButton.setFocusable(false);
		changePassButton.addActionListener(e ->{
			accountManagement.dispose();
			changePassword(accountManagement, user);
		});
		
		changeEmailButton.setBounds(50, 70, 200, 30);
		changeEmailButton.setFocusable(false);
		changeEmailButton.addActionListener(e ->{
			accountManagement.dispose();
			changeEmail(accountManagement, user);
		});
		
		deleteBankAccountButton.setBounds(50, 110, 200, 30);
		deleteBankAccountButton.setFocusable(false);
		deleteBankAccountButton.addActionListener(e ->{
			accountManagement.dispose();
			parrentFrame.setVisible(true);
		});
		
		recoverSomeonesPassButton.setBounds(50, 150, 200, 30);
		recoverSomeonesPassButton.setFocusable(false);
		recoverSomeonesPassButton.addActionListener(e ->{
			accountManagement.dispose();
			recoverPassword(accountManagement, user);
		});
		
		backButton.setBounds(50, 190, 200, 30);
		backButton.setFocusable(false);
		backButton.addActionListener(e ->{
			accountManagement.dispose();
			parrentFrame.setVisible(true);
		});
		
		accountManagement.setVisible(true);
		accountManagement.setLayout(null);
		accountManagement.getContentPane().setBackground(new Color(36, 39, 171));
		accountManagement.setSize(300, 280);
		accountManagement.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		accountManagement.setResizable(false);
		accountManagement.add(changePassButton);
		accountManagement.add(changeEmailButton);
		accountManagement.add(deleteBankAccountButton);
		accountManagement.add(recoverSomeonesPassButton);
		accountManagement.add(backButton);
	}
	
	private void clientAccountManagement(JFrame parrentFrame, User user) {
		JFrame accountManagement = new JFrame();

		JButton changePassButton = new JButton("Change password");
		JButton changeEmailButton = new JButton("Change Email");
		JButton deleteBankAccountButton = new JButton("Delete bank account");
		JButton backButton = new JButton("Back");
		
		changePassButton.setBounds(50, 30, 200, 30);
		changePassButton.setFocusable(false);
		changePassButton.addActionListener(e ->{
			accountManagement.dispose();
			changePassword(accountManagement, user);
		});
		
		changeEmailButton.setBounds(50, 70, 200, 30);
		changeEmailButton.setFocusable(false);
		changeEmailButton.addActionListener(e ->{
			accountManagement.dispose();
			changeEmail(accountManagement, user);
		});
		
		deleteBankAccountButton.setBounds(50, 110, 200, 30);
		deleteBankAccountButton.setFocusable(false);
		deleteBankAccountButton.addActionListener(e ->{
			accountManagement.dispose();
			recoverPassword(accountManagement, user);
		});
		
		backButton.setBounds(50, 150, 200, 30);
		backButton.setFocusable(false);
		backButton.addActionListener(e ->{
			accountManagement.dispose();
			parrentFrame.setVisible(true);
		});
		
		accountManagement.setVisible(true);
		accountManagement.setLayout(null);
		accountManagement.getContentPane().setBackground(new Color(36, 39, 171));
		accountManagement.setSize(300, 240);
		accountManagement.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		accountManagement.setResizable(false);
		accountManagement.add(changePassButton);
		accountManagement.add(changeEmailButton);
		accountManagement.add(deleteBankAccountButton);
		accountManagement.add(backButton);
	}
}
