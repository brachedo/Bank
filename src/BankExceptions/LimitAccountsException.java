package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class LimitAccountsException extends RuntimeException{
	public LimitAccountsException() {
		JOptionPane.showMessageDialog(null, "You've reached the limit of accounts", "Error", JOptionPane.OK_OPTION);
	}
}
