//BqCloud > BqCloud-Server > Main.java -by Berkay

package Main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Scanner;

class Server
{
	//Address and sockets
	private static ServerSocket server_socket = null;
	private static Socket socket = null;
	
	//To string data transfer
	private static OutputStream output_stream = null;
	private static PrintWriter print_writer = null;
	private static InputStream input_stream = null;
	private static BufferedReader buffered_reader = null;
	
	//To download file
	private static DataInputStream data_input_stream = null;
	private static FileOutputStream file_output_stream = null;
	
	//To upload file
	private static DataOutputStream data_output_stream = null;
	private static FileInputStream file_input_stream = null;
	
	/**
	 * The method respond starts a server, receives data from the connected
	 * client, sends a response and closes the server
	 * 
	 * If the first 5 character of the string received from the client is
	 * 		<USR> : the rest of the received string is in the format : @username@password
	 * 			responds "true" or "false" -to check if the user is registered
	 * 		<DIR> : that is all, responds in format : %filename1%filename2%...%
	 * 			-to get available files to download from the server
	 * 		<GET> : the rest of the received string is in the format : filepath%filename
	 * 			-to download the file from the server
	 * 		<SND> : the rest of the received string is in the format : filepath%filename
	 * 			-to upload the file to the server
	 * 		<ADL> : that is all, responds a string to be printed
	 * 			-to print the audit log
	 * 		<RAK> : that is all, resets the audit log
	 * 			-to clear the file Auditlog.txt
	 */
	public static void respond(int port)
	{ 
	    try
	    {
	    	//Start the server
	    	server_socket = new ServerSocket(port);
		    AuditLog.printAndSaveLog("Server is started on port " + port + ", waiting any client to connect...");
		    socket = server_socket.accept( );                         	    
			//To send the data
		    output_stream = socket.getOutputStream(); 
		    print_writer = new PrintWriter(output_stream, true);
		    //To receive the data
		    input_stream = socket.getInputStream();
		    buffered_reader = new BufferedReader(new InputStreamReader(input_stream));
	    	//Receive and send
		    String received_data = null;  
		    for (int i = 0; i < 1; i++)
			{
			    //Receive
			    received_data = buffered_reader.readLine();
			    AuditLog.printAndSaveLog("Received \"" + received_data + "\" from " + socket.getInetAddress() + ":" + port);
			    //Send
			    if (received_data.substring(0, 5).equals("<GET>"))
			    {
			    	uploadFile(received_data);			    
			        AuditLog.printAndSaveLog("Sent \"" + received_data.substring(received_data.indexOf('%')+1) + "\" to " + socket.getInetAddress() + ":" + port + "\n");
			    }
			    else
			    {
			        String response = Respond.respondToStringRequest(received_data);
				    print_writer.println(response);             
				    print_writer.flush();			 
				    AuditLog.printAndSaveLog("Sent \"" + response + "\" to " + socket.getInetAddress() + ":" + port + "\n");
			    }
			} 
		    if (received_data.substring(0, 5).equals("<SND>"))
		    {
		    	downloadFile(received_data);
		    }
	    	socket.close();
	    	server_socket.close();
	    }
	    catch (Exception i)
	    {
	    	System.out.println("Cannot received any data or the data cannot be sent.");
	    	System.out.println("Exception : " + i);
	    }
	}      
	
	//Method to download a file from the client
	private static void downloadFile(String request) throws IOException
	{
		//DataInputStream and FileOutputStream
		data_input_stream = new DataInputStream(socket.getInputStream());
		file_output_stream = new FileOutputStream(Paths.get("").toAbsolutePath().toString() + "/Files/" + request.substring(request.indexOf('%')+1));
		//Receive the file
		long size = data_input_stream.readLong(); 
		byte[] buffer = new byte[4 * 1024];
		int bytes = data_input_stream.read(buffer, 0, (int)Math.min(buffer.length, size));
		while (bytes != -1 && size > 0) 
		{
			file_output_stream.write(buffer, 0, bytes);
			size -= bytes;
			bytes = data_input_stream.read(buffer, 0, (int)Math.min(buffer.length, size));
		}
		//Close the stream
		file_output_stream.close();
	}
	
	//Method to upload a file to the client
	private static void uploadFile(String request) throws IOException
	{
		//Auditlog.txt is in the folder /Data/ instead of /Files/
		String dir = "/Files/";
		if (request.substring(request.indexOf('%')+1).equals("Auditlog.txt"))
		{
			dir = "/Data/";
		}
		//Open the file
		File file = new File(Paths.get("").toAbsolutePath().toString() + dir + request.substring(request.indexOf('%')+1));
		//DataOutputStream and FileInputStream
		data_output_stream = new DataOutputStream(socket.getOutputStream());	
		data_output_stream.writeLong(file.length());
		file_input_stream = new FileInputStream(file);
		//Send the file
		byte[] buffer = new byte[4 * 1024];
		int bytes = file_input_stream.read(buffer);
		while (bytes != -1) 
		{
			data_output_stream.write(buffer, 0, bytes);
			data_output_stream.flush();
			bytes = file_input_stream.read(buffer);
		}
		//Close the stream
		file_input_stream.close();
	}
}

//Main
public class Main
{	
	public static void main(String[] args) throws UnknownHostException
	{
		//Check the files exist
		checkFilesExist();
		//Main menu
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to the server");
		System.out.println(" [0] to start the server");
		System.out.println(" [1] to add a new user");
		System.out.println(" [2] to remove an user");
		System.out.println(" [3] to print the users");
		System.out.println(" [4] to print the audit log");
		System.out.println(" [5] to reset the audit log");
		System.out.println(" [6] to exit");
		String input;
		while (true)
		{
			System.out.print("\nEnter : ");
			input = scan.nextLine().trim();
			//Start the server
			if (input.equals("0"))
			{
				System.out.println("\nStarting the server...\n");
				break;
			}
			//Add a new user
			else if (input.equals("1"))
			{
				//New user's username
				System.out.print("\nEnter the new user's username : ");
				String username = scan.nextLine().trim();
				//New user's password
				System.out.print("\nEnter the new user's password : ");
				String password = scan.nextLine().trim();
				//Add the user
				boolean user_added = Users.addUser(username, password);
				//Print the result
				if (user_added)
				{
					System.out.println("\nNew user added successfully.");
				}
				else
				{
					System.out.println("\nUsername or password contains invalid characters");
					System.out.println("or user with this username is already exist.");
				}
			}
			//Remove an existing user
			else if (input.equals("2"))
			{
				//New user's username
				System.out.print("\nEnter the username of the user to be removed : ");
				String username = scan.nextLine().trim();
				//Remove the user
				boolean user_removed = Users.removeUser(username);
				//Print the result
				if (user_removed)
				{
					System.out.println("\nUser removed successfully.");
				}
				else
				{
					System.out.println("\nThere is no user with this username.");
				}
			}
			//Print the users
			else if (input.equals("3"))
			{
				//Print the file users.txt
				System.out.println();
				Users.printUsers();
			}
			//Print the audit log
			else if (input.equals("4"))
			{
				//Print the file Auditlog.txt
				System.out.println("\n" + AuditLog.returnAuditLog());
			}
			//Reset the audit log
			else if (input.equals("5"))
			{
				//Clear the file Auditlog.txt
				System.out.println("\nAuditlog.txt cleared.");
				AuditLog.resetAuditLog();
			}
			//Exit
			else if (input.equals("6"))
			{
				//Exit
				System.out.println("\nExit...");
				System.exit(0);
			}
			//If the input option is invalid
			else
			{
				System.out.println("\nInvalid input, try again.");
			}
		}
		//Ask the port no
		System.out.println("\nSelect the port no to be used");
		System.out.println(" [1] to use default port no 28280");
		System.out.println(" [2] to select another port no ");
		int port_no = 28280;
		while (true)
		{
			System.out.println("\nEnter : ");
			String selection = scan.nextLine();
			if (selection.equals("1"))
			{
				System.out.println("\nPort no set to : " + port_no);
				break;
			}
			else if (selection.equals("2"))
			{
				while (true)
				{
					System.out.println("\nEnter the port no : ");
					port_no = scan.nextInt();
					if (port_no > 1024 && port_no <= 65536)
					{
						System.out.println("\nPort no set to : " + port_no);
						break;
					}
					else
					{
						System.out.println("\nInvalid input, port no should be a number 1025 to 65536, inclusive.");
					}
				}
				break;
			}		
			else
			{
				System.out.println("\nInvalid input, try again.");
			}
		}
		//Close the scanner and start the server
		scan.close();
		while (true)
		{
			Server.respond(port_no);
		}
	}
	
	/**
	 * The method checkFilesExist checks if the files necessary for the server is exist
	 * if the files don't exist, creates the files in the same directory as the server
	 */
	private static void checkFilesExist()
	{
		//Data folder exists
		String data_folder_path = Paths.get("").toAbsolutePath().toString() + File.separator + "Data" + File.separator;
		File data_folder = new File(data_folder_path);
		if (!data_folder.exists()) 
		{
			//Create the folder Data
			data_folder.mkdir();
			//Create the files Users.txt and Auditlog.txt inside the folder Data
			try
			{
				File users_txt = new File(data_folder_path + "Users.txt");
				users_txt.createNewFile();
				Users.addUser("admin", "password");
				System.out.println("Users.txt is created with 1 user : user:admin, password:password");
				File auditlog_txt = new File(data_folder_path + "Auditlog.txt");
				auditlog_txt.createNewFile();
				System.out.println("Auditlog.txt is created.");
			}
			catch (IOException e)
			{
				System.out.println("Couln't create the files");
				System.out.println("Exception : " + e);
			}
		}
		//Files folder exists
		File files_folder = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "Files" + File.separator);
		if (!files_folder.exists()) 
		{
			files_folder.mkdir();
		}
	}
}