//BqCloud > BqCloud-Server > AuditLog.java -by Berkay

package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

class Respond
{
	/**
	 * The method respondToStringRequest returns the proper respond 
	 * according to the data received from the client
	 * 
	 * @param received_data : received data from the client
	 * @return String : response to be sent to the clients
	 */
	public static String respondToStringRequest(String received_data)
	{
		String send = null;
		String request = received_data.substring(0, 5);
		//If the received data is login data
	    if (request.equals("<USR>"))
	    {
	    	if (Users.checkUser(received_data.substring(5)))
	    	{
	    		send = "true";
	    	}
	    	else
	    	{
	    		send = "false";
	    	}
	    }
	    //If the received data is to get available files to download
	    else if (request.equals("<DIR>"))
	    {
	    	send = "%";
	 		File dir = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "Files" + File.separator);
	 		for (File file : dir.listFiles())
	 		{
	 			send += file.getName() + "%";
	 		}
	    }
	    //If the received data is to get the audit log
	    else if (request.equals("<ADL>"))
	    {
	    	send = AuditLog.returnAuditLog();
	    }
	    //If the received data is to reset the audit log
	    else if (request.equals("<RAL>"))
	    {
	    	send = "Auditlog.txt cleared";
	    	AuditLog.resetAuditLog();
	    }
	    //Return the response
	    return send;
	}
}

public class AuditLog 
{	
	//Direction of the file Auditlog.txt - Server/Data/Auditlog.txt
	private static String auditlog_txt_file_directory = Paths.get("").toAbsolutePath().toString() + File.separator + "Data" + File.separator + "Auditlog.txt";
	
	/**
	 * The method returnAuditLog returns the content of the file Auditlog.txt
	 * in a string
	 * 
	 * @return : String
	 */
	public static String returnAuditLog()
	{
		String file = "";
		try  
		{
			//Open the file to write : File -> FileReader -> BufferedReader
			BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(auditlog_txt_file_directory)));
			//Read all lines
			String current_line = buffered_reader.readLine();
			while (current_line != null)
			{
				//Add the line to the string to be returned
				file += current_line + "\n";
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
		//Return
		return file;
	}
	
	/**
	 * The method printAndSaveLog first prints the parameter log then saves it
	 * to the file Auditlog.txt
	 * 
	 * @param log : log to be printed and saved
	 */
	public static void printAndSaveLog(String log)
	{
		//Print the log
		System.out.println(log);
		try
		{
			//Read the file
			String file = "";
			//Open the file to write : File -> FileReader -> BufferedReader
			BufferedReader buffered_reader = new BufferedReader(new FileReader(new File(auditlog_txt_file_directory)));
			//Read all lines
			String current_line = buffered_reader.readLine();
			while (current_line != null)
			{
				//Add the line to the string to be returned
				file += current_line + "\n";
				//Read the next line
				current_line = buffered_reader.readLine();
			}
			//Close the reader
			buffered_reader.close();
			//Open the file to read : File -> FileWriter -> BufferedWriter
			BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(new File(auditlog_txt_file_directory)));
			//Write the new data
			buffered_writer.write(file + log + "\n");
			buffered_writer.flush();
			buffered_writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Exception : " + e);
		}
	}
	
	/**
	 * The method resetAuditLog resets the audit log by
	 * clearing the file Auditlog.txt
	 */
	public static void resetAuditLog()
	{
		try
		{
			//Open the file to write in : File -> FileWriter -> BufferedWriter
			BufferedWriter buffered_writer = new BufferedWriter(new FileWriter(new File(auditlog_txt_file_directory)));
			//Clear the file
			buffered_writer.write("");
			buffered_writer.flush();
			buffered_writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.out.println("Exception : " + e);
		}
	}
}
