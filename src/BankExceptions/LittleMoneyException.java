package BankExceptions;

import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class LittleMoneyException extends RuntimeException{
	public LittleMoneyException(){
		JOptionPane.showMessageDialog(null, "Minimal transaction is 1$", "Less transaction", JOptionPane.OK_OPTION);
	}
}
