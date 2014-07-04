package web;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * a socket that communicates with a client from the side of the server.
 * this class is incharge of forwarding all the commands to the client so he could execute them
 * @author Or Magen
 *
 */
public class WebbedServersideSocket {

	//web variables
	/**
	 * the input data stream (data reader from another socket)
	 */
	private DataInputStream in;
	
	/**
	 * the output data stream (data writer to another socket)
	 */
	private DataOutputStream out;
	
	/**
	 * the socket of the client
	 */
	private Socket socket;
	

	/**
	 * CONSTRUCTOR
	 * @param s the socket connected to the server
	 */
	public WebbedServersideSocket(Socket s){
		super();
		try {
			socket = s;
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {}
	}


	/**
	 * closes the socket and the data transferring channels
	 * @throws IOException
	 */
	public void close() throws IOException{
		write("quit");
		in.close();
		out.close();
		socket.close();
	}


	/**
	 * uses the output data stream to talk to the server
	 * @param s - the message sent to the server
	 * @throws IOException
	 */
	public void write(String s) throws IOException{
		out.writeUTF(s);
	}

	/**
	 * uses the input data stream to talk to the server
	 * @return the message received from the server
	 * @throws IOException
	 */
	public String read() throws IOException{
		return in.readUTF();
	}

	/**
	 * forwards the command to the client and executes it
	 * @param command the command to execute
	 * @return the message ("state") of the client
	 * @throws IOException
	 */
	public String doCommand(String command) throws IOException{
		if(!command.equals("quit"))
			write(command);
		else
			close();
		return read();
	}
}
