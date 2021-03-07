package BankExceptions;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class MaximumTransactionException extends RuntimeException {

	public MaximumTransactionException() {
		JLabel label = new JLabel("<html>Maximum transaction is 2000$<br>For bigger transaction went into our Bank clone</html>");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Calibri", Font.ITALIC, 15));
		JOptionPane.showMessageDialog(null, label,"Maximum transaction", JOptionPane.WARNING_MESSAGE);
	}
}
