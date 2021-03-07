package Entities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import BankExceptions.InvalidEmailException;
import BankExceptions.LimitAccountsException;
import BankExceptions.UnmatchingPasswordsException;
import BankExceptions.UserNotFoundException;
import DBConnection.JDBConnection;
import DBConnection.SQLCommands;
import Encryption.Encryption;

public class Admin implements User{
	
	private List<String> ibanList = new ArrayList<>();
	private String[] ibanlist = new String[3];
	
	private void fillList() {//ready
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
				ResultSet rs = stmt.executeQuery(String.format(SQLCommands.SELECT_CONCRETE_IBAN, Encryption.getLogin()));
			){
				while(rs.next()) {
					ibanList.add(rs.getString("iban"));
				}
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	public String[] getIbanList() {
		fillList();
		ibanlist=ibanList.toArray(ibanlist);
		return ibanlist;
	}
	
	public void deposit(double amount, String iban) {//ready
		new Thread(() -> {
			synchronized(Admin.class) {
				try(
					Connection conn = JDBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(SQLCommands.UPDATE_MONEY);
					){
						stmt.executeUpdate(String.format(SQLCommands.UPDATE_MONEY, amount, iban));
					} catch (SQLException e) {
						System.err.println(e);
					}
			}
		}).start();
	}
	
	public void withdraw(double amount, String iban) {//ready
		new Thread(() -> {
			synchronized(Admin.class) {
				try(
					Connection conn = JDBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement(SQLCommands.UPDATE_MONEY);
					){
						stmt.executeUpdate(String.format(SQLCommands.UPDATE_MONEY, -amount, iban));
					} catch (SQLException e) {
						System.err.println(e);
					}
			}
		}).start();
	}
	
	private int getLastAccountID() {//ready
		int id = 0;
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_ID_FROM_ACCOUNTS_ASC);
			){
				rs.last();
				id = Integer.parseInt(rs.getString("id"))+1;
		} catch (SQLException e) {
			System.err.println(e);
			return 1;
		}
		return id;
	}
	
	public void changePass(String newpass1,String newpass2) {//ready
		new Thread(() ->{
			try {
				if(newpass1.equals(newpass2)) {
					try (
							Connection conn = JDBConnection.getConnection();
							PreparedStatement stmt = conn.prepareStatement(SQLCommands.CHANGE_PASS);
						){
							if(stmt.executeUpdate(String.format(SQLCommands.CHANGE_PASS, 
									Encryption.encrypt(newpass1), Encryption.getLogin())) == 1) {
								JOptionPane.showMessageDialog(null, "Passwords changed succesfully",
										"Succeed", JOptionPane.INFORMATION_MESSAGE);
							}
					} catch(SQLException e) {
						System.err.println(e);
					}
				}else {
					throw new UnmatchingPasswordsException();
				}
			} catch (UnmatchingPasswordsException e) {
				return;
			}
		}).start();
	}
	
	public void recoverPass(String user, String newPass) {
		try(
				Connection conn = JDBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLCommands.UPDATE_USER_PASSWORD);
			){
			if(stmt.executeUpdate(String.format(SQLCommands.UPDATE_USER_PASSWORD, Encryption.encrypt(newPass), user)) == 1) {
				JOptionPane.showMessageDialog(null, "Done", "Succesfull", JOptionPane.INFORMATION_MESSAGE);
			}else {
				throw new UserNotFoundException();
			}
		}catch(UserNotFoundException unte) {
			return;
		}catch(Exception e) {
			System.err.println(e);
		}
	}
	
	public void foundNewBalance() {//ready
		String owner = Encryption.getLogin();
		String iban = Encryption.getLastIBAN();
		
		int id = getLastAccountID();
		int accAmount = getPersonAccsAmount();
		
		try {
			if(accAmount == 3) {
				throw new LimitAccountsException();
			}
		} catch (LimitAccountsException e) { return; }
		
		try
		(
			Connection conn = JDBConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(SQLCommands.FOUND_NEW_BALANCE);
			PreparedStatement stmt2 = conn.prepareStatement(SQLCommands.INCREMENT_ACCS);
		){
			try{
				conn.setAutoCommit(false);
				stmt.executeUpdate(String.format(SQLCommands.FOUND_NEW_BALANCE, id, owner, iban));
				stmt2.executeUpdate(String.format(SQLCommands.INCREMENT_ACCS, accAmount+1, owner));
				conn.commit();
			} catch(Exception e) {
				conn.rollback();
				System.err.println(e);
			}
		} catch(Exception e){
			System.err.println(e);
		}	
	}
	public void deleteBalance() {
		
	}
	private int getPersonAccsAmount() {//ready
		int count = 0;
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(String.format(SQLCommands.GET_ACCOUNTS_AMOUNT, Encryption.getLogin()));
			){	
				rs.next();
				count = rs.getInt("accs");
		} catch (Exception e) {
			System.err.println(e);
		}
		return count;
	}
	public void transfer(double amount, String senderIBAN, String receiverIBAN) {
		new Thread(() ->{
			synchronized(Admin.class) {
				try (
						Connection conn = JDBConnection.getConnection();
						PreparedStatement stmt = conn.prepareStatement(SQLCommands.UPDATE_MONEY);
					){
						try {
							conn.setAutoCommit(false);
							stmt.executeUpdate(String.format(SQLCommands.UPDATE_MONEY, -amount, senderIBAN));
							stmt.executeUpdate(String.format(SQLCommands.UPDATE_MONEY, amount, receiverIBAN));
							conn.commit();
						} catch(SQLException es) {
							System.err.println(es);
						} catch (Exception e) {
							conn.rollback();
						}
				} catch (Exception e) {
					System.err.println(e);
				}
			}
		}).start();
	}
	
	public void changeRole(String login, String role) {//ready
		try (
				Connection conn = JDBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLCommands.CHANGE_ROLE);
			){
				if(stmt.executeUpdate(String.format(SQLCommands.CHANGE_ROLE, role, login)) == 1) {
				}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void changeEmail(String newEmail) {//ready
		try {
			boolean bool = false;
			for(char c : newEmail.toCharArray()) {
				if(c == '@')
					bool = true;
			}
			if(bool == false) {
				throw new InvalidEmailException();
			}
		} catch (InvalidEmailException e) {
			return;
		}
		try (
				Connection conn = JDBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLCommands.CHANGE_EMAIL);
			){
				if(stmt.executeUpdate(String.format(SQLCommands.CHANGE_EMAIL, newEmail, Encryption.getLogin())) == 1) {
					System.out.println("done");
				}
		} catch (Exception e) {
			System.err.println(e);
		}
	}
}