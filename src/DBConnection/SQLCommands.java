package DBConnection;


public class SQLCommands {
	//creating tables, some of them aren't actual

	public static final String CREATE_TABLE_USERS = "CREATE TABLE users (id int, name varchar(255) ,lastname varchar(255) "
			+ ", username varchar(255), password varchar(255), email varchar(255), role varchar(255), PRIMARY KEY(username))";
	public static final String CREATE_TABLE_BANK_ACCOUNTS = "CREATE TABLE accounts (owner varchar(255), iban varchar(255)"
			+ ", balance int, PRIMARY KEY(iban))";
	
	public static final String UPDATE_MONEY = "UPDATE accounts SET balance = balance + '%s' WHERE iban = '%s'";
	
	public static final String SELECT_FROM_USERS = "SELECT * FROM users";
	public static final String SELECT_IBAN = "SELECT iban FROM accounts";
	public static final String SELECT_CONCRETE_IBAN= "SELECT iban FROM accounts WHERE owner = '%s'";
	public static final String SELECT_BALANCE_FROM_ACCOUNTS = "SELECT balance FROM accounts WHERE iban = '%s'";
	public static final String SELECT_ID_FROM_USERS_ASC = "SELECT id FROM users ORDER BY id ASC";
	public static final String SELECT_ID_FROM_ACCOUNTS_ASC = "SELECT id FROM accounts ORDER BY id ASC";
	public static final String SELECT_IBAN_FROM_ACCOUNTS_ASC = "SELECT iban FROM accounts ORDER BY iban ASC";
	public static final String UPDATE_USER_PASSWORD = "UPDATE users SET password = '%s' WHERE username = '%s'";

	public static final String INSERT_INTO_USERS = "INSERT INTO users (id, name, lastname, username, password, email, role) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')";	
	public static final String FOUND_NEW_BALANCE = "INSERT INTO accounts (id, owner, iban, balance) VALUES ('%s', '%s', '%s', 0)";
	public static final String INCREMENT_ACCS = "UPDATE users SET accs = '%s' WHERE username = '%s'";
	public static final String CHANGE_ROLE = "UPDATE users SET role = '%s' WHERE username = '%s'";
	public static final String CHANGE_PASS = "UPDATE users SET password = '%s' WHERE username = '%s'";
	public static final String CHANGE_EMAIL = "UPDATE users SET email = '%s' WHERE username = '%s'";
	
	public static final String GET_ACCOUNTS_AMOUNT = "SELECT * FROM users WHERE username = '%s'";

}
