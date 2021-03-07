package Encryption;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import BankExceptions.InvalidEmailException;
import DBConnection.JDBConnection;
import DBConnection.SQLCommands;
import Entities.Admin;
import Entities.Client;
import Entities.User;

public class Encryption {
	 
    private static SecretKeySpec secretKey;
    private static byte[] key;  
	private static final String myKey = "NoOneShouldKnewThat";
	private static String login;

    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); 
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt) {
        try
        {
            setKey(myKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt) {
        try
        {
            setKey(myKey);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static String getLogin() {
    	return login;
    }
    
	private static User createPerson(String role) {

		if(role.equals("admin")) {
			User admin  = new Admin();
			return admin;
		}
		else if(role.equals("client")) {
			User client = new Client();
			return client;
		}
		return null;
	}	
	
	public static void register(String name, String lastName, String login, String password, String email) {
		try {
			boolean bool = false;
			for(char c : email.toCharArray()) {
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
				PreparedStatement stmt = conn.prepareStatement(SQLCommands.INSERT_INTO_USERS);
			)
		{
			password = Encryption.encrypt(password);
			if(stmt.executeUpdate(
					String.format(SQLCommands.INSERT_INTO_USERS, getLastPersonID(), name, lastName, 
											login, password, email, "client")
			   ) == 1) {
				JOptionPane.showMessageDialog(null, "Registration succesfull", "Registration succesfull", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("Registration succesfull");
			} else {
				JOptionPane.showMessageDialog(null, "Registration succesfull", "Registration succesfull", JOptionPane.OK_OPTION);
				System.err.println("Error with registration");
			}
		}catch(Exception e) {
			System.err.println(e);
		}
	}
	
	private static int getLastPersonID() {
		int id = 0;
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_ID_FROM_USERS_ASC);
			){
				rs.last();
				 id = Integer.parseInt(rs.getString("id"))+1;
		} catch (SQLException e) {
			System.err.println(e);
			return 1;
		}
		return id;
	}
	
	public static String getLastIBAN() {
		String iban;
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(SQLCommands.SELECT_IBAN);
			){
				rs.last();
				iban = rs.getString("iban");
		
		} catch (SQLException e) {
			System.err.println(e);
			return null;
		}
		char[] ch = new char[10];
		for(int i=0 ; i<10 ; i++) {
			ch[i] = iban.charAt(10+i);
		}
		int numbers = 0;
		try {
			numbers = Integer.parseInt(new String(ch));
		} catch(Exception e) { 
			System.err.println(e);
		}
		String newIban = iban.substring(0, 10) + ++numbers;
		return newIban;
	}

    public static User logIn(String loginn, String password) {
    	login = loginn;
		String role = null;
		try (
				Connection conn = JDBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQLCommands.SELECT_FROM_USERS);
				ResultSet rs = stmt.executeQuery();
			)
		{		
			password= Encryption.encrypt(password);
			while(rs.next()) {
				if(rs.getString("username").equals(loginn) && rs.getString("password").equals(password)) {
					System.out.println("Loged in succesfully");
					role = rs.getString("role");
					System.out.println("You logged as "+role);
				}
			}			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		try {
			return createPerson(role);
		} catch (NullPointerException e) {
			System.err.println("Wrong login data");
		}
		return null;
	}
    
	public static double getPersonBalance(String iban) {
		
		try (
				Connection conn = JDBConnection.getConnection();
				Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery(String.format(SQLCommands.SELECT_BALANCE_FROM_ACCOUNTS, iban));
			){
				rs.next();
				return rs.getDouble("balance");
		} catch (Exception e) {
			System.err.println(e);
		}
		return 0;
	}
}