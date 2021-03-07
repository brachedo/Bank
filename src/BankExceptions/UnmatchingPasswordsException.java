package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UnmatchingPasswordsException extends RuntimeException{
	public UnmatchingPasswordsException() {
		JOptionPane.showMessageDialog(null, "Non matching passwords", "Non matching passwords", JOptionPane.OK_OPTION);
	}
}
