package web;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * MAIN RUNNING CLASS - SERVER
 * the server of the music playing project. contains all the UI & web communication (from the server side) of the entire project 
 * @author Or Magen
 *
 */
public class MainServer{

	///////web variables
	/**
	 * the socket of the server
	 */
	private ServerSocket serverSocket;
	
	/**
	 * an array list of all the connected clients
	 */
	private ArrayList<WebbedServersideSocket> clientSockets;
	
	/**
	 * an array list of all the notes divided to parts (by the amount of clients)
	 */
	private ArrayList<String> notes;
	
	/**
	 * the port that is used to establish a connection with the clients
	 */
	private int port = 1234;
	
	/**
	 * server running state
	 */
	private boolean running = true;

	///////user interface variables
	/**
	 * a reader used to read songs from files
	 */
	private BufferedReader bf;
	
	/**
	 * a scanner used to read commands from the server-master
	 */
	private Scanner s = new Scanner(System.in);

	
	/**
	 * the entire song that is about to be played (whole song - undivided)
	 */
	private String linesOfMusic = "?67?48?52?55$400";

	/**
	 * CONSTRUCTOR
	 * @throws Exception
	 */
	public MainServer()  throws Exception{
		serverSocket = new ServerSocket(port);
		clientSockets = new ArrayList<WebbedServersideSocket>();
		notes = new ArrayList<String>();
		System.out.println("server is up. awaiting commands");
		runServer();
	}

	/**
	 * sends all the notes to all the clients
	 * @throws IOException
	 */
	public void sendNotes() throws IOException {
		for(int i = 0; i < clientSockets.size(); i++){
			try{ 
				System.out.println("sending to client " + (i+1));
				System.out.println("his part:");
				System.out.println(notes.get(i));
				clientSockets.get(i).write(Command.SEND_NOTES.getCommand());
				System.out.println("sent the order to notes");
				clientSockets.get(i).write(notes.get(i));
				System.out.println("actually sent the notes");
				System.out.println("client " + (i+1) + ":" +clientSockets.get(i).read());
			}
			catch(Exception e){//clientSockets.get(i).close();
			}
		}
	}

	/**
	 * adds a new client to the server 
	 * @throws IOException
	 */
	public void addClient() throws IOException{
		Socket s = serverSocket.accept();
		clientSockets.add(new WebbedServersideSocket(s));
		System.out.println("connection established - client added to the server. client number: " + clientSockets.size());
	}

	/**
	 * SUBFUNCTION - makes an exact copy of a string (to edit without damaging the original string)
	 * @param str the original string
	 * @return a copy of the string
	 */
	public static String copyOfString(String str){
		return str.substring(0);
	}

	/**
	 * begin playing at the given time
	 * @param time in the format of "hh:mm:ss". i.e. - "22:30:07"
	 * @throws IOException
	 */
	public void beginAt(String time) throws IOException{
		for(int i = 0; i < clientSockets.size(); i++){
			clientSockets.get(i).write("time");
			clientSockets.get(i).write(time);
		}
	}

	/**
	 * SUBFUNCTION - finds the lowest note in the song
	 * @param song
	 * @return the lowest note in the song
	 */
	public static int findMinNote(String song){
		String s = copyOfString(song),temp;
		int min = 127;
		while(!s.equals("")){
			if(s.contains("?"))
				s = s.substring(s.indexOf('?')+1);
			else
				break;
			if(s.length()>=3)
				temp = s.substring(0,3);
			else
				temp = s;
			if(temp.contains("@") || temp.contains("$") || temp.contains("?"))
				temp = temp.substring(0,2);
			if(Integer.parseInt(temp)< min)
				min = Integer.parseInt(temp);
		}
		return min;
	}

	/**
	 * SUBFUNCTION - finds the highest note in the song
	 * @param song
	 * @return the highest note in the song
	 */
	public static int findMaxNote(String song){
		String s = copyOfString(song),temp = s;
		int max = 0;
		while(!s.equals("")){
			if(s.indexOf('?') != -1)
				s = s.substring(s.indexOf('?')+1);
			else
				break;
			if(s.length()>=3)
				temp = s.substring(0,3);
			else
				temp = s;
			if(temp.contains("@") || temp.contains("$") || temp.contains("?")){
				temp = temp.substring(0,2);
			}
			if(Integer.parseInt(temp)> max)
				max = Integer.parseInt(temp);
		}
		return max;
	}


	/**
	 * SUBFUNCTION - finds the distance between the current command and the next one
	 * @param s what's left of the song (that needs to be processed)
	 * @return the distance between the current command and the next one
	 */
	public static int tempMin(String s){
		int tempMin = s.length();
		if(s.indexOf('@',1) > -1){
			tempMin = Math.min(tempMin, s.indexOf('@',1));
			//System.out.println("@-" + s.indexOf('@',1));
		}
		if(s.indexOf('?',1) > -1){
			tempMin = Math.min(tempMin, s.indexOf('?',1));
			//System.out.println(" ?-" + s.indexOf('?',1));
		}
		if(s.indexOf('$',1) > -1){
			tempMin = Math.min(tempMin, s.indexOf('$',1));
			//System.out.println(" $- " +s.indexOf('$',1));
		}
		return tempMin;
	}


	/**
	 * divides the song into tasks for each of the players
	 * @param song the song that needs to be divided
	 * @return an array list of the commands of the song divided to parts
	 */
	public ArrayList<ArrayList<String>> divide(String song){
		System.out.println("in");
		ArrayList<ArrayList<String>> ar = new ArrayList<ArrayList<String>>();
		for(int i = 0; i < clientSockets.size(); i++)
			ar.add(new ArrayList<String>());
		int min = findMinNote(song), max = findMaxNote(song);
		int tempMin;
		int ammountOfPlayers = clientSockets.size();
		String s = copyOfString(song), temp;
		char mark;
		while(!s.equals("")){
			tempMin = tempMin(s);
			temp = s.substring(0,tempMin);
			mark = temp.charAt(0);
			temp = temp.substring(1);
			if (mark != '$'){
				for(int i = 0; i < ammountOfPlayers; i++)
					//min + i*range/size <= num <= min + (i+1)*range/size
					if(Integer.parseInt(temp) >= Math.round(min + i*(max-min)/ammountOfPlayers) && Integer.parseInt(temp) <= Math.round(min + (i+1)*(max-min)/ammountOfPlayers))
						ar.get(i).add(mark + temp);
			}
			else
				for(int i = 0; i < ammountOfPlayers; i++)
					ar.get(i).add(mark + temp);
			s = s.substring(tempMin);
		}

		return ar;
	}

	/**
	 * divides the song into tasks for each of the players
	 * @param song the song that needs to be divided
	 * @return an array  of the commands of the song divided to parts
	 */
	public String[] devideToStrings(String song){
		System.out.println("in");
		String[] parts = new String[clientSockets.size()];
		for(int i = 0; i < clientSockets.size(); i++)
			parts[i] = new String("");
		int min = findMinNote(song), max = findMaxNote(song);
		int tempMin;
		int ammountOfPlayers = clientSockets.size();
		String s = copyOfString(song), temp;
		char mark;
		while(!s.equals("")){
			tempMin = tempMin(s);
			temp = s.substring(0,tempMin);
			mark = temp.charAt(0);
			temp = temp.substring(1);
			if (mark != '$'){
				for(int i = 0; i < ammountOfPlayers; i++)
					//min + i*range/size <= num <= min + (i+1)*range/size
					if(Integer.parseInt(temp) >= Math.round(min + i*(max-min)/ammountOfPlayers) && Integer.parseInt(temp) <= Math.round(min + (i+1)*(max-min)/ammountOfPlayers))
						parts[i]+= mark + temp;
			}
			else
				for(int i = 0; i < ammountOfPlayers; i++)
					parts[i]+= mark + temp;
			s = s.substring(tempMin);
		}

		return parts;
	}

	/**
	 * runs the server until it closes
	 */
	public void runServer() {
		while(running){
			try {
				doCommand(findCommand(s.nextLine()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * closes all the connections with the server
	 * @throws IOException
	 */
	private void closeAll() throws IOException {
		for(int i = clientSockets.size()-1; i >= 0; i--){
			close(i);
		}
		System.out.println("all connections terminated");
	}

	/**
	 * closes a specific connection with a specific client
	 * @param i - the ID of the client
	 * @throws IOException
	 */
	public void close(int i) throws IOException{
		clientSockets.get(i).close();
		clientSockets.remove(i);
		System.out.println("connection " + (i+1) + " terminated");
	}

	/**
	 * plays the song at a given time in the format of "hh:mm:ss". i.e. "12:50:36"
	 * @throws IOException
	 */
	public void playAt() throws IOException {
		System.out.println("when?");
		String time = s.nextLine();
		for(int i = 0; i < clientSockets.size(); i++){
			clientSockets.get(i).write("time");
			clientSockets.get(i).write(time);
			System.out.println("client " + (i+1) + ":" +clientSockets.get(i).read());
		}
		System.out.println(clientSockets.get(0).read());
	}

	/**
	 * loads a song from a given file
	 * @param fileName a name of a file from the "songs" package
	 */
	public void readSong(String fileName) {
		try {
			BufferedReader bf = new BufferedReader(new FileReader("songs/" + fileName));
			linesOfMusic = bf.readLine();
			System.out.println("new song:\n" + linesOfMusic );
			System.out.println("read successfully");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("404 - File not found");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO Auto-generated method stub
		

	}

	/**
	 * SUBFUNCTION - identifies the command received
	 * @param com - the string the translates to a command
	 * @return the identified command
	 */
	public  Command findCommand(String com) {
		System.out.println("com is: " + com);
		for(Command c : Command.values()){
			if(c.getCommand().equals(com))
				return c;
		}

		return Command.E404;
	}

	/**
	 * executes the given identified command
	 * @param command the command itself
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void doCommand(Command command) throws IOException, InterruptedException{
		switch (command){

		//kind of self explanatory... 
		case QUIT:{ 
			System.out.println("are you sure you want to quit?");
			if(s.nextLine().equals("yes")){
				System.out.println("bye");
				closeAll();
				System.exit(0);
			}
			else
				System.out.println("then back to music!");
			;break;}

		//returns a list of all the commands (non-secret ones)
		case HELP:{
			System.out.println("this is a player of music. i shall be your guide. ask me stuff, i'm standing rght next to you...");
			break;}

		//receives a new song from the server-master
		case NEW_NOTES:{
			System.out.println("awaiting notes");
			linesOfMusic = s.nextLine();
			System.out.println("notes received");
			break;}

		//divides the song into parts
		case DIVIDE:{
			System.out.println("began dividing");
			String[] parts = devideToStrings(linesOfMusic);
			notes.clear();
			for(int i = 0; i < parts.length; i++)
				notes.add(parts[i]);
			System.out.println("dividing completed");
			break;
		}

		//sends the notes to all the clients
		case SEND_NOTES:{
			System.out.println("sending...");
			sendNotes();
			break;
		}
		
		//disconnects a single client
		case KILL:{
			System.out.println("which socket to kill (ID)?");
			close(Integer.parseInt(s.nextLine())-1);
			break;
		}

		//disconnects all clients
		case KILL_ALL:{
			closeAll();
			break;
		}

		//plays in time X in format "hh:mm:ss"
		case TIME:{
			playAt();
			break;}

		//awaiting a new client to connect
		case AWAIT_NEW_CLIENT:{
			System.out.println("yey");
			addClient();
			break;
		}
		
		//loads a file from 
		case READ_FILE:{
			System.out.println("file name?");
			readSong(s.nextLine());
		}

		//////////EASTEREGGS//////////
		case EASTEREGG:;{break;}
		case SITHIS:;{break;}
		case RICKROLL:;{break;}
		case DIE:;{break;}

		
		case E404:
		default:System.err.println("Error 404: \n command not found - invalid command");}
	}

/**
 * MAIN FUNCTION - construct the server and runs
 * @param args
 * @throws IOException
 */
	public static void main(String[]args) throws IOException{

		System.out.println("hi");
		try {
			MainServer s = new MainServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}