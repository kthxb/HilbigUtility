package com.ash.util;

import java.awt.HeadlessException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.ash.util.ServerConnection.User;

/**
 * Opens a simple Login-Window for logging onto a Server-Account<br>
 * After successfull login, the user-object can be used to get user information.
 * 
 * How to use:
 * <pre>
 * LoginWindow lw = new LoginWindow();
 ServerConnection.User user = lw.login();
 ServerConnection sc = new ServerConnection();
 System.out.println(sc.getValue(user, "email"));
 * </pre>
 * 
 * @author Aaron Hilbig
 *
 */
public class LoginWindow {

	protected Shell shlLogin;
	private Text username;
	private Text password;
	private Text email;
	private Text username_reg;
	private Text password_reg;
	private Text password_reg_2;
	private boolean registering;
	
	public ServerConnection.User user;
	public String user_email;
	public boolean user_premium;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LoginWindow window = new LoginWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*public ServerConnection.User login(){
		try {
			LoginWindow window = new LoginWindow();
			window.open();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public ServerConnection.User login(){
		try {
			open();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlLogin.open();
		shlLogin.layout();
		while (!shlLogin.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlLogin = new Shell();
		shlLogin.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				if(!registering) shlLogin.setSize(288, 210);
				else shlLogin.setSize(288, 272);
			}
		});
		shlLogin.setImage(SWTResourceManager.getImage(LoginWindow.class, "/com/ash/login/ts_refresh.png"));
		shlLogin.setSize(288, 210);
		shlLogin.setText("Login");
		
		Label lblUsername = new Label(shlLogin, SWT.NONE);
		lblUsername.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblUsername.setBounds(10, 10, 68, 15);
		lblUsername.setText("Username");
		
		username = new Text(shlLogin, SWT.BORDER);
		username.setBounds(10, 31, 252, 21);
		
		Label lblPassword = new Label(shlLogin, SWT.NONE);
		lblPassword.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblPassword.setBounds(10, 58, 55, 15);
		lblPassword.setText("Password");
		
		password = new Text(shlLogin, SWT.BORDER | SWT.PASSWORD);
		password.setBounds(10, 79, 252, 21);
		
		Button login = new Button(shlLogin, SWT.NONE);
		login.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(username.getText().trim().length()>3 && password.getText().trim().length()>2){
					ServerConnection s = new ServerConnection();
					@SuppressWarnings("deprecation")
					User u = new User(username.getText().trim());
					boolean success = true;
					try {
						//String mail = s.getValue(u, "email");
						String pwHash = s.getValue(u, "passwordHash");
						user_premium = s.getValueAsBoolean(u, "premium");
						
						
						if(!pwHash.trim().equals(ServerConnection.sha512(password.getText().trim()))){
							JOptionPane.showMessageDialog(null, "Username or password incorrect", "Error", JOptionPane.ERROR_MESSAGE);
							success = false;
						}
					} catch(Exception e){
						JOptionPane.showMessageDialog(null, "Couldn´t reach server:\nAn error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
						success = false;
					}
					if(success){
						try {
							JOptionPane.showMessageDialog(null, "Logged in!" + (user_premium ? "\n- VIP User -" : ""), "Login", JOptionPane.INFORMATION_MESSAGE);
							user = u;
							user_email = s.getValue(u, "email");
							shlLogin.dispose();
						} catch (HeadlessException e) {
							JOptionPane.showMessageDialog(null, "Server responded, but an  error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Server responded, but an error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					} else {
						password.setText("");
						password.forceFocus();
					}
				}
			}
		});
		login.setBounds(10, 106, 252, 25);
		login.setText("Login");
		
		Label lblEmail = new Label(shlLogin, SWT.NONE);
		lblEmail.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblEmail.setBounds(10, 193, 55, 15);
		lblEmail.setText("E-Mail");
		
		email = new Text(shlLogin, SWT.BORDER);
		email.setEnabled(false);
		email.setBounds(10, 214, 252, 21);
		
		Label lblUsername_reg = new Label(shlLogin, SWT.NONE);
		lblUsername_reg.setBounds(10, 241, 252, 15);
		lblUsername_reg.setText("Username (used to log in)");
		
		username_reg = new Text(shlLogin, SWT.BORDER);
		username_reg.setEnabled(false);
		username_reg.setBounds(10, 262, 252, 21);
		
		Label lblPassword_reg = new Label(shlLogin, SWT.NONE);
		lblPassword_reg.setBounds(10, 289, 55, 15);
		lblPassword_reg.setText("Password");
		
		password_reg = new Text(shlLogin, SWT.BORDER | SWT.PASSWORD);
		password_reg.setEnabled(false);
		password_reg.setBounds(10, 310, 252, 21);
		
		Label lblPassword_reg_2 = new Label(shlLogin, SWT.NONE);
		lblPassword_reg_2.setBounds(10, 337, 132, 15);
		lblPassword_reg_2.setText("Repeat Password");
		
		password_reg_2 = new Text(shlLogin, SWT.BORDER | SWT.PASSWORD);
		password_reg_2.setEnabled(false);
		password_reg_2.setBounds(10, 358, 252, 21);
		
		Button register_cont = new Button(shlLogin, SWT.NONE);
		
		Button register = new Button(shlLogin, SWT.NONE);
		register.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				registering = true;
				
				shlLogin.setSize(288, 272);
				
				email.setEnabled(true);
				username_reg.setEnabled(true);
				password_reg.setEnabled(true);
				password_reg_2.setEnabled(true);
				password.setEnabled(false);
				username.setEnabled(false);
				login.setEnabled(false);
				register.setEnabled(false);
				
				password.setVisible(false);
				username.setVisible(false);
				login.setVisible(false);
				register.setVisible(false);
				lblPassword.setVisible(false);
				lblUsername.setVisible(false);
				
				email.setBounds(email.getBounds().x, email.getBounds().y - 183, email.getBounds().width, email.getBounds().height);
				username_reg.setBounds(username_reg.getBounds().x, username_reg.getBounds().y - 183, username_reg.getBounds().width, username_reg.getBounds().height);
				password_reg.setBounds(password_reg.getBounds().x, password_reg.getBounds().y - 183, password_reg.getBounds().width, password_reg.getBounds().height);
				password_reg_2.setBounds(password_reg_2.getBounds().x, password_reg_2.getBounds().y - 183, password_reg_2.getBounds().width, password_reg_2.getBounds().height);
				lblEmail.setBounds(lblEmail.getBounds().x, lblEmail.getBounds().y - 183, lblEmail.getBounds().width, lblEmail.getBounds().height);
				lblPassword_reg.setBounds(lblPassword_reg.getBounds().x, lblPassword_reg.getBounds().y - 183, lblPassword_reg.getBounds().width, lblPassword_reg.getBounds().height);
				lblPassword_reg_2.setBounds(lblPassword_reg_2.getBounds().x, lblPassword_reg_2.getBounds().y - 183, lblPassword_reg_2.getBounds().width, lblPassword_reg_2.getBounds().height);
				lblUsername_reg.setBounds(lblUsername_reg.getBounds().x, lblUsername_reg.getBounds().y - 183, lblUsername_reg.getBounds().width, lblUsername_reg.getBounds().height);
				register_cont.setBounds(register_cont.getBounds().x, register_cont.getBounds().y - 183, register_cont.getBounds().width, register_cont.getBounds().height);
			}
		});
		register.setBounds(10, 137, 252, 25);
		register.setText("Register");
		
		register_cont.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("deprecation")
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				String mail = email.getText().trim();
				String username = username_reg.getText().trim();
				String password = password_reg.getText().trim();
				String password2 = password_reg_2.getText().trim();
				if(checkValidRegister(mail, username, password, password2)){
					ServerConnection s = new ServerConnection();
					User u = new User(username);
					boolean success = true;
					try {
						//s.newValue(u, "account", "1");
						s.newValue(u, "email", mail.trim().replaceAll("\n", ""));
						s.newValue(u, "passwordHash", ServerConnection.sha512(password));
						s.newValue(u, "premium", "false");
						//TODO All keys to reg
					} catch(Exception e){
						JOptionPane.showMessageDialog(null, "An error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
						success = false;
					}
					if(success){
						try {
							JOptionPane.showMessageDialog(null, ("Account created!\nName:\t" + username + "\nE-Mail:\t" + s.getValue(u,"email") + "passwordHash (30-character excerpt):\t" + s.getValue(u, "passwordHash")).substring(10, 40), "Success!", JOptionPane.INFORMATION_MESSAGE);
							user = u;
							user_email = s.getValue(u, "email");
							System.exit(0);
						} catch (HeadlessException e) {
							JOptionPane.showMessageDialog(null, "Account was created, but couldn´t reconnect to Server\nAn error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "Account was created, but couldn´t reconnect to Server\nAn error occurred:\n" + e.getMessage() + "\n" + e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
				} else {
					username_reg.setText("");
					password_reg.setText("");
					password_reg_2.setText("");
					username_reg.forceFocus();
				}
			}
		});
		register_cont.setBounds(10, 385, 252, 25);
		register_cont.setText("Register");
	}
	
	@SuppressWarnings("deprecation")
	public boolean checkValidRegister(String mail, String username, String password, String password2){
		mail = mail.trim().replaceAll("\n", "");
		if(!password.equals(password2)){
			JOptionPane.showMessageDialog(null, "Passwords must be identical", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(password.length()<3){
			JOptionPane.showMessageDialog(null, "Password too short", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(username.length()<4){
			JOptionPane.showMessageDialog(null, "Username must be at least 4 characters long", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		


		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(username);
		if(m.find() || username.contains(" ")){
			JOptionPane.showMessageDialog(null, "Username can´t contain spaces or symbols", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(!(mail.contains("@") && mail.split("@")[0].length() > 0 && mail.split("@")[1].length() > 0 && mail.split("@")[1].contains("."))){
			JOptionPane.showMessageDialog(null, "E-Mail not valid", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		try {
			new ServerConnection().newValue(new User(username), "account", "1");
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "Username has already been taken", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
}
