package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class NotSelectedIBANException extends RuntimeException {
	public NotSelectedIBANException() {
		JOptionPane.showMessageDialog(null, "Please select IBAN!", "Empty IBAN", JOptionPane.OK_OPTION);
	}
}
