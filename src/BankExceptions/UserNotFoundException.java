package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UserNotFoundException extends RuntimeException{
	public UserNotFoundException() {
		JOptionPane.showMessageDialog(null, "User not found", "User not found exception", JOptionPane.OK_OPTION);
	}
}
