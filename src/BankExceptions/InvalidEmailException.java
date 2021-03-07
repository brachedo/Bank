package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class InvalidEmailException extends RuntimeException{
	public InvalidEmailException() {
		JOptionPane.showMessageDialog(null, "Invalid email, please enter real email", "Invali email exception", JOptionPane.OK_OPTION);
	}
}
