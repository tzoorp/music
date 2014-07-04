package web;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import music.ClientsidePlayer;

/**
 * MAIN RUNNING CLASS - CLIENT
 * the client of the music playing project. contains all the properties of the "client-side" player 
 * and includes all the handling of the web transmission
 * @author Or Magen
 *
 */
public class WebbedClientsidePlayer extends ClientsidePlayer{
	
	//web variables
	/**
	 * the IP(v4) address of the server 
	 */
	private String hostName = "127.0.0.1";
	
	/**
	 * a scanner used to put the IP of the server
	 */
	private Scanner s = new Scanner(System.in);
	/**
	 * the port that is used to establish a connection with the clients
	 */
	private int portNumber = 1234;
	
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
	 * socket running state
	 */
	private boolean running = true;

	//time variables
	/**
	 * a calender used to find the current time
	 */
	private Calendar cal = Calendar.getInstance();
	
	/**
	 * the format of the current time representation
	 */
	private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

	/**
	 * CONSTRUCTOR
	 */
	public WebbedClientsidePlayer(){
		super();
		System.out.println("what is the port of the server (IPv4)");
		hostName = s.nextLine();
		try {
			socket = new Socket(hostName, portNumber);
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			while(running){
				doCommand(read());
			} 
		}catch (Exception e) {}

	}

	/**
	 * does the command received from the server
	 * @param com he command itself
	 * @throws IOException
	 */
	public void doCommand(String com) throws IOException {
		Command c = findCommand(com);
		switch(c){
		//closes the program
		case QUIT:{
			write("logging off");
			close();
			break;}

		//plays when the computer's clock shows the received time
		case TIME:{
			String s = read();
			write("time received:" + s);
			
			while(!s.equals(getTime())){
				System.out.println(getTime() + "!=" + s + ":" +!s.equals(getTime()));
			}
			write("post while");
			play();
			break;
		}

		//receives the notes
		case SEND_NOTES:{
			System.out.println("right place, right time");
			createCommandsFromString(read());
			write("ready");
			break;
		}

		//self explanatory - invalid command
		default:{
			write("Error  404- Command Not Found");
			break;
		}
		}

	}

	/**
	 * SUBFUNCTION
	 * identifies the command received
	 * @param com - the string the translates to a command
	 * @return the identified command
	 */
	public  Command findCommand(String com) {
		for(Command c : Command.values()){
			if(c.getCommand().equals(com))
				return c;
		}
		return null;
	}

	/**
	 * SUBFUNCTION
	 * @return the current time in the computer
	 */
	public String getTime() {
		cal = Calendar.getInstance();
		return df.format(cal.getTime());
	}



	/**
	 * closes the socket and the data transferring channels
	 * @throws IOException
	 */
	public void close() throws IOException{
		in.close();
		out.close();
		socket.close();
		running = false;
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
	 *  MAIN FUNCTION - construct the client and runs
	 * @param itsok
	 */
	public static void main(String[]itsok){
		WebbedClientsidePlayer w = new WebbedClientsidePlayer();
	}
}