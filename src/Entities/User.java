package Entities;

public interface User {
	public void withdraw(double amount, String iban);//done
	public void deposit(double amount, String iban);//done
	public void changePass(String newpass1,String newpass2);//done
	public void changeEmail(String newEmail);//done
	public void recoverPass(String user, String newPass);
	public void foundNewBalance();//done
	public void deleteBalance();
	public void transfer(double amount, String senderIBAN, String receiverIBAN);//done
	public String[] getIbanList();//done
	public void changeRole(String login, String role);//done
}
