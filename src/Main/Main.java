//BqCloud > BqCloud-Client > Main.java -by Berkay

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
import java.net.Socket;

class Client
{	
	//Address
	private static String IP;
	private static int port;
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
	 * This method sends the parameter request to the server and 
	 * returns the response from the server
	 * 
	 * If the first 5 character of the passed parameter request is
	 * 		<USR> : the rest of the request is in the format : @username@password
	 * 			returns "true" or "false" - to check if the user is registered
	 * 		<DIR> : that is all, returns in format %filename1%filename2%...%
	 * 			-to get available files to download from the server
	 * 		<GET> : the rest of the request is in the format : filepath%filename
	 * 			-to download the file from the server
	 * 		<SND> : the rest of the request is in the format : filepath%filename
	 * 			-to upload the file to the server
	 * 		<ADL> : that is all, returns a string to be printed
	 * 			-to print the audit log
	 * 		<RAL> : that is all, resets the audit log
	 * 			-to clear the file Auditlog.txt
	 * 
	 * @param request
	 * @return
	 */
	public static String DataTransfer(String request)
	{
		//Connect to the server
		String response_from_the_server = null;   
		try
		{
			socket = new Socket(IP, port);
			//To send the data
			output_stream = socket.getOutputStream(); 
			print_writer = new PrintWriter(output_stream, true);
			for (int i = 0; i < 1; i++)
		    {
				//Send
				print_writer.println(request);
				print_writer.flush();		
			    System.out.println("Sent \"" + request + "\" to " + IP + ":" + port);
			    //Receive file
			    if (request.substring(0, 5).equals("<GET>"))
			    {
			    	//To receive file
			    	downloadFile(request);
			        response_from_the_server = "<GOT>";
			    }
			    //Receive string
			    else
			    {
			    	//To receive a string
					input_stream = socket.getInputStream();
					buffered_reader = new BufferedReader(new InputStreamReader(input_stream));
			    	response_from_the_server = buffered_reader.readLine();
			    }
		    } 
			if (request.substring(0, 5).equals("<SND>"))
			{
				uploadFile(request);
			}
			//Close the socket
			socket.close();
			//Return the received data	
			System.out.println("Received \"" + response_from_the_server + "\" from /" + IP + ":" + port);
		    return response_from_the_server;
		}
		catch (Exception e)
		{
			System.out.println(e);
			return "<ERR>";
		}
	}
	
	//Method to download a file from the server
	private static void downloadFile(String request) throws IOException
	{
		//DataInputStream and FileOutputStream
		data_input_stream = new DataInputStream(socket.getInputStream());
		file_output_stream = new FileOutputStream(request.substring(5, request.indexOf('%'))); 
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
	
	//Method to upload a file to the server
	private static void uploadFile(String request) throws IOException
	{
		//Open the file
	    File file = new File(request.substring(5, request.indexOf('%')));
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
	
	//Set the IP address and the port no
	public static void setAddress(String IP, int port)
	{
		Client.IP = IP;
		Client.port = port;
	}
}

//Main
public class Main
{
	public static void main(String[] args)
	{
		ApplicationFrame loginUi = new ApplicationFrame();
	}
}
