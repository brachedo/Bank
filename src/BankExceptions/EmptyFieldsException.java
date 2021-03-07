package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class EmptyFieldsException extends RuntimeException {
	public EmptyFieldsException() {
		JOptionPane.showMessageDialog(null, "Can't have empty fields", "Empty fields", JOptionPane.WARNING_MESSAGE);
	}
}
