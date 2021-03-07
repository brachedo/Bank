package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class IBANNotFoundException extends RuntimeException{
	public IBANNotFoundException() {
		JOptionPane.showMessageDialog(null, "IBAN not found", "IBAN not found", JOptionPane.OK_OPTION);
	}
}
