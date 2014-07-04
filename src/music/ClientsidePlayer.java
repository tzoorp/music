package music;

import java.util.ArrayList;
import javax.sound.midi.MidiUnavailableException;

/**
 * a class that is run by the client - 
 * contains all the client side activities (playing & web communication)
 * 
 *  PLAYING FORMAT:
 * phase variable (commands):
 * "?" - noteOn - plays the note
 * "@" - noteOff - mutes the note
 * "$" - sleep - the player awaits till it needs to play again or finish the song 	
 * format - phase variable + value (either note or amount of time). then next command.
 * i.e. ?65?33$100?54@65.....
 * ***note that upon sending\receiving the format, it shall be in the form of:
 * 
 * ONLY NUMBERS AND ACCEPTABLE FORMAT VARIABLES
 * @author Or Magen
 *
 */
public class ClientsidePlayer {

	/**
	 * the notes of the song about to be played
	 */
	protected String inputPlaying;
	/**
	 * the player used in the computer (connects to the default Sound Card)
	 */
	private Player p;
	/**
	 * a list of the notes divided to commands
	 */
	private ArrayList<String> commands; 

	/**
	 * FORMAT VARIABLE - note on
	 */
	final char noteOn = '?';
	/**
	 * FORMAT VARIABLE - note off
	 */
	final char noteOff = '@';
	/**
	 * FORMAT VARIABLE - sleep
	 */
	final char noteSleep = '$';

	/**
	 * CONSTRUCTOR
	 */
	public ClientsidePlayer(){
		commands = new ArrayList<String>();

		try {
			p = new Player();
		} catch (MidiUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * plays the notes of the received part
	 * ASSUMES THAT THE ARRAYLIST IS NOT EMPTY
	 */
	public void play(){
		String command;
		char type;
		for(int i = 0; i < commands.size(); i++){
			command = commands.get(i);
			type = command.charAt(0);
			command = command.substring(1); 

			switch(type){
			case noteOn: p.play(Integer.parseInt(command));break;
			case noteOff: p.stop(Integer.parseInt(command));break;
			default:try {
				Thread.sleep(Integer.parseInt(command));
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};break;
			}				
		}
	}


	/**
	 * creates an array list of commands
	 * @param com - the string containing the commands
	 */
	public void createCommandsFromString(String com){
		inputPlaying = com;
		String temp = "", allCommands;
		allCommands = copyOfString(com);
		while(allCommands.length() > 1){
			temp += allCommands.charAt(0);
			allCommands = allCommands.substring(1);
			if(allCommands.charAt(0) == noteOn || allCommands.charAt(0) == noteOff || allCommands.charAt(0) == noteSleep){
				System.out.println(temp);
				commands.add(temp);
				temp = "";
			}
		}
		commands.add(temp + allCommands);
	}

	/**
	 * adds a command to the command list
	 * @param command the command itself
	 */
	public void addCommand(String command){
		commands.add(command);
	}

	/**
	 * creates a copy of the string for string editing (without destroying the old string)
	 * @param str the original string
	 * @return the copy of the string
	 */
	public String copyOfString(String str){
		return str.substring(0);
	}

	/**
	 * GETTER
	 * @return the song received by the server
	 */
	public String getInputPlaying() {
		return inputPlaying;
	}

	/**
	 * GETTER
	 * @param inputPlaying the notes of the song about to be played
	 */
	public void setInputPlaying(String inputPlaying) {
		this.inputPlaying = inputPlaying;
	}

	/**
	 * GETTER
	 * @return the player used in the computer (connects to the default Sound Card)
	 */
	public Player getP() {
		return p;
	}

	/**
	 * GETTER
	 * @return a list of the notes divided to commands
	 */
	public ArrayList<String> getCommands() {
		return commands;
	}

	/**
	 * SETTER
	 * @param p the player used in the computer (connects to the default Sound Card)
	 */
	public void setP(Player p) {
		this.p = p;
	}


	/**
	 * SETTER
	 * @param commands a list of the notes divided to commands
	 */
	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}
}