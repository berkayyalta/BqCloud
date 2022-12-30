//BqCloud > BqCloud-Server > Users.java -by Berkay

package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Users 
{
	//Direction of the file Users.txt - Server/Data/Users.txt
	private static String users_txt_file_directory = Paths.get("").toAbsolutePath().toString() + File.separator + "Data" + File.separator + "Users.txt";

	/**
	 * The method checkUser returns true if the user whose data is received 
	 * from the client is registered in Users.txt, otherwise returns false
	 * 
	 * @param received_login_data : received data from the client
	 * @return boolean
	 */
	public static boolean checkUser(String received_login_data)
	{
		boolean exists = false;
		try
		{
			//Open the file Users.txt and read the lines
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(users_txt_file_directory)));
			String current_line = buffered_reader.readLine();
			while (current_line != null)
			{
				lines.add(current_line);
				current_line = buffered_reader.readLine();
			}
			//Check
			for (String line : lines)
			{
				if (line.toUpperCase().contains(received_login_data.toUpperCase()))
				{
					exists = true;
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Exception : " + e);
		}		
		//Return
		return exists;
	}
	
	/**
	 * The method addUser adds a new user with the passed user information
	 * to Users.txt returns true if the user is successfully
	 * added, otherwise returns false
	 * 
	 * @param username : username information
	 * @param password : password information
	 * @return boolean
	 */
	public static boolean addUser(String username, String password)
	{
		//Check if the passed username or password contains invalid characters
		char[] valid_characters = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','0','1','2','3','4','5','6','7','8','9'};
		boolean valid_un = true;
		boolean valid_pw = true;
		//If the username is valid
		for (int i = 0; i < username.length(); i++)
		{
			if (new String(valid_characters).indexOf(username.toUpperCase().charAt(i)) == -1)
			{
				valid_un = false;
			}
		}
		//If the password is valid
		for (int i = 0; i < password.length(); i++)
		{
			if (new String(valid_characters).indexOf(password.toUpperCase().charAt(i)) == -1)
			{
				valid_pw = false;
			}
		}
		//Add if both username and password are valid and the user is not already registered
		boolean user_added = false;
		String username_data = "@" + username;
		if (valid_un && valid_pw && !(checkUser(username_data.toUpperCase())))
		{
			try
			{
				//Open the file and read the lines
				BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(users_txt_file_directory)));
				String file = "";
				String current_line = buffered_reader.readLine();
				while (current_line != null)
				{
					file += current_line + "\n";
					current_line = buffered_reader.readLine();
				}
				//Add the new data to the string file to be written in the file
				String user_data = "@" + username + "@" + password + "\n";
				file += user_data.toUpperCase();			
				//Open the file with BufferedWriter and write the string file into the file
				BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(new File(users_txt_file_directory)));
				buffered_writer.write(file);
				buffered_writer.flush();
				user_added = true;
				buffered_writer.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.out.println("Exception : " + e);
			}
		}
		//Return
		if (user_added)
		{
			AuditLog.printAndSaveLog("User added : username:" + username + ", password:" + password);
		}
		return user_added;
	}
	
	/**
	 * The method removeUser removes the user whose username is the passed
	 * parameter username from Users.txt, returns true if the user is successfully
	 * removed, otherwise returns false
	 * 
	 * @param username : the name of the user who will be removed
	 * @return boolean
	 */
	public static boolean removeUser(String username)
	{
		boolean is_line_deleted = false;
		try
		{
			//Read all lines in the file
			ArrayList<String> lines = new ArrayList<String>();
			BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(users_txt_file_directory)));
			String current_line = buffered_reader.readLine();
			while(current_line != null)
			{
				lines.add(current_line);
				current_line = buffered_reader.readLine();
			}		
			//Remove the content of the file
			BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(new File(users_txt_file_directory)));
		    buffered_writer.write("");
		    buffered_writer.flush();
		    //Write the lines except the line to be deleted into the file again 
		    String file = "";
			String line_to_be_deleted = "@" + username + "@";
			for (String line : lines)
			{
				if (!line.toUpperCase().contains(line_to_be_deleted.toUpperCase()))
				{
					file += line + "\n";
				}
				else
				{
					is_line_deleted = true;
				}
			}
			buffered_writer.write(file);
			buffered_writer.flush();
			buffered_writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Exception : " + e);
		}
		//Return
		if (is_line_deleted)
		{
			AuditLog.printAndSaveLog("User removed : username:" + username);
		}
		return is_line_deleted;
	}
	
	/**
	 * The method printUsers prints the content of the file Users.txt
	 * line by line
	 */
	public static void printUsers()
	{
		try  
		{
			//Open the file : File -> FileReader -> BufferedReader
			BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(users_txt_file_directory)));
			//Print all lines
			String current_line = buffered_reader.readLine();
			while (current_line != null)
			{
				//Print the line
				System.out.println(current_line);
				//Read the next line
				current_line = buffered_reader.readLine();
			}
			//Close the reader
			buffered_reader.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("Exception : " + e);
		} 
	}
}
