package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class UnconfirmedTransactionException extends RuntimeException{
	public UnconfirmedTransactionException() {
		JOptionPane.showMessageDialog(null, "Please confirm transaction", "Unconfirmed transaction", JOptionPane.OK_OPTION);
	}
}
