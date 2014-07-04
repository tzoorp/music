package web;

/**
 * An enum of all the possible commands - server/client, server only, and secret commands
 * @author Or Magen
 *
 */
public enum Command {

	////////server/client commands
	/**
	 * COMMAND - sends a sarcastic comment to the user
	 */
	HELP ("Help!"), 

	/**
	 * COMMAND - exits the program
	 */
	QUIT ("quit"),

	/**
	 * COMMAND - sends the notes to all the clients
	 */
	SEND_NOTES ("send notes"),

	/**
	 * COMMAND - decides the time when the synchronized playing shall begin
	 */
	TIME("time"),

	/**
	 * COMMAND - error in case a command was not understood
	 */
	E404(""),

	///server only commands
	/**
	 * COMMAND - divides the song to the amount of clients
	 */
	DIVIDE("divide"),

	/**
	 * COMMAND - awaits for a new client to connect (adds a new client) 
	 */
	AWAIT_NEW_CLIENT("await new client"),

	/**
	 * COMMAND - disconnects from a certain client
	 */
	KILL("kill connection"),

	/**
	 * COMMAND - disconnects all clients
	 */
	KILL_ALL("kill all"),

	/**
	 * COMMAND - receives a new song from the user 
	 */
	NEW_NOTES("new song"),

	/**
	 * COMMAND - reads a new song from a given file
	 */
	READ_FILE("load song"),


	//////////EASTEREGGS//////////
	EASTEREGG("easter egg"),
	//there are no "easter" eggs in this project. go away...
	SITHIS("what is the music of life?"),
	//"Silence, my brother" + alert that says it + crickets in the background
	RICKROLL("what is the meaning of life?"),
	//name says it all, doesn't it...?
	DIE("die"),
	//"I WILL SURVIVE"
	;

	/**
	 * the string equivalent of the command
	 */
	private final String command;

	/**
	 * CONSTRUCTOR
	 */
	private Command(String command){
		this.command = command;
	}

	/**
	 * GETTER
	 * @return the string equivalent of the command
	 */
	public String getCommand() {
		return command;
	}
}