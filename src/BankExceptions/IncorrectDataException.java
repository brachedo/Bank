package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class IncorrectDataException extends RuntimeException {
	public IncorrectDataException() {
		JOptionPane.showMessageDialog(null, "Wrong username or password", "Incorrect data", JOptionPane.WARNING_MESSAGE);
	}
}
