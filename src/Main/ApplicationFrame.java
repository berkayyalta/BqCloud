//BqCloud > BqCloud-Client > ApplicationFrame.java -by Berkay

package Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class ApplicationFrame extends JFrame 
{
	//Default serial version ID -?
	private static final long serialVersionUID = 1L;

	public ApplicationFrame()
	{
		//Initialize the JFrame
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Add the Login panel
		addLoginPanel();
	}
	
	/**
	 * The following method and class are about the login 
	 * screen of BqCloud Client
	 */
	//A Method to change the panel of the JFrame to LoginPanel
	private void addLoginPanel()
	{
		//Remove the existing panel
		getContentPane().removeAll();
		//Resize for the panel to fit and change the title
		setSize(400, 195);
		setTitle("BqCloud-Client Login");
		//Add the panel and revalidate
		add(new LoginPanel(this));
		revalidate();
	}
	
	//Class LoginPanel to be used in this JFrame
	private class LoginPanel extends JPanel
	{
		//Default serial version ID -?
		private static final long serialVersionUID = 1L;
		
		//Initialize the panel to be added to passed JFrame
		private LoginPanel(ApplicationFrame frame)
		{
			//Absolute layout
			setLayout(null);
			
			//Login title
			JLabel lblLogin = new JLabel("Login");
			lblLogin.setFont(new Font("Lucida Sans", Font.BOLD, 20));
			lblLogin.setBounds(20, 15, 60, 25);
			add(lblLogin);
			
			//Login username label
			JLabel login_username_label = new JLabel("Username : ");
			login_username_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
			login_username_label.setBounds(20, 45, 120, 25);
			add(login_username_label);
			
			//Login password label
			JLabel login_password_label = new JLabel("Password  : ");
			login_password_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
			login_password_label.setBounds(20, 75, 120, 25);
			add(login_password_label);
			
			//IP address label
			JLabel ip_address_label = new JLabel("IP Address :");
			ip_address_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
			ip_address_label.setBounds(20, 115, 120, 25);
			add(ip_address_label);
			
			//Login username textfield
			JTextField login_username_textfield = new JTextField();
			login_username_textfield.setBounds(135, 45, 150, 25);
			login_username_textfield.setColumns(10);
			add(login_username_textfield);
			
			//Login password textfield
			JPasswordField login_password_textfield = new JPasswordField ();
			login_password_textfield.setBounds(135, 75, 150, 25);
			login_password_textfield.setColumns(10);
			login_password_textfield.setEchoChar('*');
			add(login_password_textfield);
			
			//IP address textfield
			JTextField ip_address_textfield = new JTextField();
			ip_address_textfield.setBounds(135, 115, 150, 25);
			ip_address_textfield.setText("192.168.12.139");
			ip_address_textfield.setColumns(10);
			add(ip_address_textfield);
			
			//Port no spinner
			JSpinner port_no_spinner = new JSpinner();
			port_no_spinner.setBounds(295, 115, 80, 25);
			port_no_spinner.setModel(new SpinnerNumberModel(Integer.valueOf(28280), null, null, Integer.valueOf(1)));
			add(port_no_spinner);
			
			//Login send button
			JButton login_send = new JButton("Send");
			login_send.setBounds(295, 75, 80, 25);
			login_send.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String login_data = "<USR>@" + login_username_textfield.getText() + "@" + String.valueOf(login_password_textfield.getPassword());
					Client.setAddress(ip_address_textfield.getText(), (int)port_no_spinner.getValue());
					String correctPassword = Client.DataTransfer(login_data);
					if (correctPassword.equals("true"))
					{
						login_username_textfield.setText("");
						login_password_textfield.setText("");
						frame.addApplicationPanel();
						JOptionPane.showMessageDialog(frame, "You loggined to the system.", "Login successful", JOptionPane.INFORMATION_MESSAGE);
					}
					else if (correctPassword.equals("<ERR>"))
					{
						JOptionPane.showMessageDialog(frame, "IP address or port no you \nentered is incorrect or the server is offline.", "Connection Failed", JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						login_username_textfield.setText("");
						login_password_textfield.setText("");
						JOptionPane.showMessageDialog(frame, "Username or password you \nentered is incorrect.", "Login Failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			add(login_send);
			
			//Information label
			JLabel info_label = new JLabel("?");
			info_label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
			info_label.setForeground(Color.BLUE);
			info_label.setBounds(355, 15, 20, 25);
			info_label.setToolTipText("The login panel of BqCloud-Client by Berkay");
			add(info_label);
		}
	}
	
	/**
	 * The following method and class are about the Application 
	 * screen of BqCloud Client
	 */
	//A Method to change the panel of the JFrame to ApplicationPanel
	private void addApplicationPanel()
	{
		//Remove the existing panel
		getContentPane().removeAll();
		//Resize for the panel to fit and change the title
		setSize(470, 290);
		setTitle("BqCloud-Client");
		//Add the panel and revalidate
		add(new ApplicationPanel(this));
		revalidate();
	}
	
	//Class LogApplicationPanelinPanel to be used in this JFrame
	private class ApplicationPanel extends JTabbedPane
	{
		//Default serial version ID -?
		private static final long serialVersionUID = 1L;
				
		//Initialize the panel to be added to passed JFrame
		private ApplicationPanel(ApplicationFrame frame)
		{
			addTab("Download", new DownloadPanel(frame));
			addTab("Upload", new UploadPanel(frame));
			addTab("Audit Log", new AuditLogPanel(frame));
		}
		
		//Download tab in this JTabbedPane
		private class DownloadPanel extends JPanel
		{
			//Default serial version ID -?
			private static final long serialVersionUID = 1L;
			
			//Selections
			ArrayList<String> available_files;
			private String selected_file = null;
			private String selected_download_location = null;
			
			//Initialize the JPanel DownloadPanel
			private DownloadPanel(ApplicationFrame frame)
			{
				setLayout(null);	
				
				//Get available files from the server
				String DirRes = Client.DataTransfer("<DIR>");
				if (DirRes.equals("<ERR>"))
				{
					JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					available_files = DirResponseToArrayList(DirRes);
				}
				
				//Download title 
				JLabel login_title = new JLabel("Download File");
				login_title.setFont(new Font("Lucida Sans", Font.BOLD, 20));
				login_title.setBounds(20, 15, 185, 25);
				add(login_title);
				
				//File selection label
				JLabel file_selection_label = new JLabel("Select the File");
				file_selection_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
				file_selection_label.setBounds(20, 45, 185, 25);
				add(file_selection_label);
				
				//Download location label
				JLabel download_location_label = new JLabel("Download Location");
				download_location_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
				download_location_label.setBounds(20, 105, 185, 25);
				add(download_location_label);
				
				//Information label
				JLabel info_label = new JLabel("?");
				info_label.setToolTipText("The download panel of BqCloud-Client by Berkay");
				info_label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
				info_label.setForeground(Color.BLUE);
				info_label.setBounds(415, 15, 20, 25);
				add(info_label);
				
				//Check download location label
				JLabel check_location = new JLabel("???");
				check_location.setFont(new Font("Lucida Sans", Font.BOLD | Font.ITALIC, 18));
				check_location.setBounds(415, 135, 40, 25);
				add(check_location);
				
				//Select file combobox
				JComboBox<String> select_file_combobox = new JComboBox<String>();
				select_file_combobox.setBounds(20, 75, 295, 25);
				for (String file_name : available_files)
				{
					select_file_combobox.addItem(file_name);
				}
				add(select_file_combobox);
				
				//Select download location textbox
				JTextField select_download_loc_textbox = new JTextField();
				select_download_loc_textbox.setText(System.getProperty("user.home") + File.separator + "Downloads");
				select_download_loc_textbox.setColumns(10);
				select_download_loc_textbox.setBounds(20, 135, 295, 25);
				add(select_download_loc_textbox);
				
				//Select button
				JButton select_button = new JButton("Select");
				select_button.setBounds(325, 75, 80, 25);
				select_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selected_file = (String) select_file_combobox.getSelectedItem();
						select_file_combobox.setEnabled(false);
						select_button.setEnabled(false);
					}
				});
				add(select_button);
				
				//Check button
				JButton check_button = new JButton("Check");
				check_button.setBounds(325, 135, 80, 25);
				check_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File file = new File(select_download_loc_textbox.getText());
						if (file.exists())
						{
							check_location.setForeground(Color.GREEN);
							check_location.setText("Y");
							selected_download_location = select_download_loc_textbox.getText();
							select_download_loc_textbox.setEnabled(false);
							check_button.setEnabled(false);
						}
						else
						{
							check_location.setForeground(Color.RED);
							check_location.setText("N");
						}
					}
				});
				add(check_button);
				
				//Download button
				JButton download_button = new JButton("Download");
				download_button.setBounds(170, 175, 130, 40);
				download_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (selected_file != null && selected_download_location != null)
						{
							String Res = Client.DataTransfer("<GET>" + selected_download_location + File.separator + selected_file + "%" + selected_file);
							if (Res.equals("<ERR>"))
							{
								JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								//Check label
								check_location.setForeground(Color.BLACK);
								check_location.setText("???");
								//Re enable the objects
								select_file_combobox.setEnabled(true);
								select_button.setEnabled(true);
								select_download_loc_textbox.setEnabled(true);
								check_button.setEnabled(true);
							}							
						}
						else
						{
							JOptionPane.showMessageDialog(frame, "Download location or \nfile is not selected.", "Download Failed", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				add(download_button);
				
				//Refresh button
				JButton refresh_button = new JButton("Refresh");
				refresh_button.setBounds(325, 15, 80, 25);
				refresh_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try
						{
							//Update the available files
							String Res = Client.DataTransfer("<DIR>");
							if (Res.equals("<ERR>"))
							{
								throw new Exception();
							}
							available_files = DirResponseToArrayList(Res);
							select_file_combobox.removeAllItems();
							for (String file_name : available_files)
							{
								select_file_combobox.addItem(file_name);
							}
							select_file_combobox.revalidate();
							//Check label
							check_location.setForeground(Color.BLACK);
							check_location.setText("???");
							//Re enable the objects
							select_file_combobox.setEnabled(true);
							select_button.setEnabled(true);
							select_download_loc_textbox.setText(System.getProperty("user.home") + File.separator + "Downloads");
							select_download_loc_textbox.setEnabled(true);
							check_button.setEnabled(true);
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				add(refresh_button);
			}
			
			/**
			 * The method DirResponseToArrayList returns the file names in the
			 * response received from the server in an ArrayList
			 * 
			 * @param response : <DIR> response from the server
			 * @return ArrayList<String>
			 */
			private ArrayList<String> DirResponseToArrayList(String response)
			{
				ArrayList<String> file_names = new ArrayList<String>();
				response = response.substring(1);
				//Add all file names to the ArrayList
				int index_p = response.indexOf('%');
				while (index_p != -1)
				{
					//Add the current filename
					file_names.add(response.substring(0, response.indexOf('%')));
					//Update the variables response and index_p
					response = response.substring(response.indexOf('%')+1);
					index_p = response.indexOf('%');
				}
				//Return the ArrayList
				return file_names;
			}
		} 
		
		//Upload tab in this JTabbedPane
		private class UploadPanel extends JPanel
		{
			//Default serial version ID -?
			private static final long serialVersionUID = 1L;
			
			//Selections
			ArrayList<String> available_files;
			private String selected_file = null;
			private String selected_upload_location = null;
			
			//Initialize the JPanel UploadPanel
			private UploadPanel(ApplicationFrame frame)
			{
				setLayout(null);		
				//Upload title 
				JLabel login_title = new JLabel("Upload File");
				login_title.setFont(new Font("Lucida Sans", Font.BOLD, 20));
				login_title.setBounds(20, 15, 185, 25);
				add(login_title);
				
				//Location selection label
				JLabel location_selection_label = new JLabel("Select the Location");
				location_selection_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
				location_selection_label.setBounds(20, 45, 185, 25);
				add(location_selection_label);
						
				//File selection label
				JLabel file_selection_label = new JLabel("Select the File");
				file_selection_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
				file_selection_label.setBounds(20, 105, 185, 25);
				add(file_selection_label);
						
				//Information label
				JLabel info_label = new JLabel("?");
				info_label.setToolTipText("The upload panel of BqCloud-Client by Berkay");
				info_label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
				info_label.setForeground(Color.BLUE);
				info_label.setBounds(415, 15, 20, 25);
				add(info_label);
					
				//Check upload location label
				JLabel check_location = new JLabel("???");
				check_location.setFont(new Font("Lucida Sans", Font.BOLD | Font.ITALIC, 18));
				check_location.setBounds(415, 75, 40, 25);
				add(check_location);
				
				//Select upload location textbox
				JTextField select_upload_loc_textbox = new JTextField();
				select_upload_loc_textbox.setText(System.getProperty("user.home") + File.separator + "Downloads");
				select_upload_loc_textbox.setColumns(10);
				select_upload_loc_textbox.setBounds(20, 75, 295, 25);
				add(select_upload_loc_textbox);
						
				//Select file combobox
				JComboBox<String> select_file_combobox = new JComboBox<String>();
				select_file_combobox.setBounds(20, 135, 295, 25); 
				select_file_combobox.setEnabled(false);
				add(select_file_combobox);
				
				//Select button
				JButton select_button = new JButton("Select");
				select_button.setBounds(325, 135, 80, 25); 
				select_button.setEnabled(false);
				select_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						selected_file = (String) select_file_combobox.getSelectedItem();
						select_file_combobox.setEnabled(false);
						select_button.setEnabled(false);
					}
				});
				add(select_button);
				
				//Check button
				JButton check_button = new JButton("Check");
				check_button.setBounds(325, 75, 80, 25);
				check_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						File file = new File(select_upload_loc_textbox.getText());
						if (file.exists())
						{
							check_location.setForeground(Color.GREEN);
							check_location.setText("Y");
							selected_upload_location = select_upload_loc_textbox.getText();						
							//Get the files in the location
							File dir = new File(selected_upload_location + File.separator);
							available_files = new ArrayList<String>();
							for (File current_file : dir.listFiles())
							{
								available_files.add(current_file.getName());
							}
							//Set the combobox
							for (String file_name : available_files)
							{
								select_file_combobox.addItem(file_name);
							}	
							select_file_combobox.revalidate();
							//Enable and disable the objects
							select_upload_loc_textbox.setEnabled(false);
							check_button.setEnabled(false);
							select_file_combobox.setEnabled(true);
							select_button.setEnabled(true);
						}
						else
						{
							check_location.setForeground(Color.RED);
							check_location.setText("N");
						}
					}
				});
				add(check_button);
						
				//Upload button
				JButton upload_button = new JButton("Upload");
				upload_button.setBounds(170, 175, 130, 40);
				upload_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (selected_file != null && selected_upload_location != null)
						{
							String Res = Client.DataTransfer("<SND>" + selected_upload_location + File.separator + selected_file + "%" + selected_file);
							if (Res.equals("<ERR>"))
							{
								JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
							}
							else
							{
								//Check label
								check_location.setForeground(Color.BLACK);
								check_location.setText("???");
								//Re enable the objects
								select_file_combobox.setEnabled(true);
								select_button.setEnabled(true);
								select_upload_loc_textbox.setEnabled(true);
								check_button.setEnabled(true);
							}													
						}
						else
						{
							JOptionPane.showMessageDialog(frame, "Download location or \nfile is not selected.", "Download Failed", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				add(upload_button);
				
				//Refresh button
				JButton refresh_button = new JButton("Refresh");
				refresh_button.setBounds(325, 15, 80, 25);
				refresh_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try
						{
							//Check label
							check_location.setForeground(Color.BLACK);
							check_location.setText("???");
							//Re enable the objects
							select_upload_loc_textbox.setText(System.getProperty("user.home") + File.separator + "Downloads");
							select_upload_loc_textbox.setEnabled(true);
							check_button.setEnabled(true);
							select_file_combobox.removeAllItems();
							select_file_combobox.setEnabled(false);
							select_button.setEnabled(false);
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				add(refresh_button);
			}
		} 
		
		//Audit log tab in this JTabbedPane
		private class AuditLogPanel extends JPanel
		{
			//Default serial version ID -?
			private static final long serialVersionUID = 1L;
			
			//Initialize the JPanel AuditLogPanel
			private AuditLogPanel(ApplicationFrame frame)
			{
				setLayout(null);
				
				//Audit log label
				JLabel audit_log_label = new JLabel("Audit Log");
				audit_log_label.setFont(new Font("Lucida Sans", Font.BOLD, 20));
				audit_log_label.setBounds(20, 15, 185, 25);
				add(audit_log_label);
				
				//Data transfer history label
				JLabel dt_history_label = new JLabel("Data transfer history");
				dt_history_label.setFont(new Font("Lucida Sans", Font.PLAIN, 18));
				dt_history_label.setBounds(20, 45, 210, 25);
				add(dt_history_label);
						
				//Information label
				JLabel info_label = new JLabel("?");
				info_label.setToolTipText("The audit log panel of BqCloud-Client by Berkay");
				info_label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
				info_label.setForeground(Color.BLUE);
				info_label.setBounds(415, 15, 20, 25);
				add(info_label);
				
				//Audit log text area
				JTextArea audit_log_text_area = new JTextArea();
				audit_log_text_area.setEditable(false);
				audit_log_text_area.setBounds(20, 75, 295, 140);
				add(audit_log_text_area);
				
				//Refresh button
				JButton refresh_button = new JButton("Refresh");
				refresh_button.setBounds(325, 75, 115, 25);
				refresh_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try
						{
							
							//download the file Auditlog.txt
							String download_location = System.getProperty("user.home") + File.separator + "Downloads";
							String Res = Client.DataTransfer("<GET>" + download_location + File.separator + "Auditlog.txt%Auditlog.txt");
							if (Res.equals("<ERR>"))
							{
								throw new Exception();
							}			
							//Read the file
							BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(download_location + File.separator + "Auditlog.txt")));
							String file = "";
							String current_line = buffered_reader.readLine();
							while (current_line != null) 
							{
								file += current_line + "\n";
								current_line = buffered_reader.readLine();
							}
							buffered_reader.close();
							//Print to the text area
							audit_log_text_area.setText(file);
							//Delete the file
							File temp_auditlog_txt = new File(download_location + File.separator + "Auditlog.txt");
							boolean is_file_deleted = temp_auditlog_txt.delete();
							if (!is_file_deleted)
							{
								System.out.println("Auditlog.txt cannot be deleted.");
							}
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				add(refresh_button);
				
				//Reset (audit log) button
				JButton reset_button = new JButton("Reset");
				reset_button.setBounds(325, 111, 115, 25);
				reset_button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String Res = Client.DataTransfer("<RAL>");
						if (Res.equals("<ERR>"))
						{
							JOptionPane.showMessageDialog(frame, "Server is no longer online.", "Server is offline", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							audit_log_text_area.setText("");
						}						
					}
				});
				add(reset_button);
			}
		}
	}
}
