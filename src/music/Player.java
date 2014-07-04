package music;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * A class that contains all the low-level music handling 
 * this class is used to play note-per note and all the related attributes (syth, channels, instruments and such)
 * @author Or Magen
 *
 */
public class Player {
	
	/**
	 * the synthesizer used to make sounds using the Sound Card
	 */
	private Synthesizer syn;
	
	/**
	 * the instrument the synthesizer mimics to play sounds
	 */
	private int instrument = DEFAULT_INSTRUMENT;
	
	/**
	 * the velocity of each note
	 */
	private int velocity = DEFAULT_NOTE_VELOCITY;
	
	/**
	 * the MIDI channels used to play
	 */
	private final MidiChannel[] mc;
	
	/**
	 * an array of all the playable instruments
	 */
	private Instrument[] instr;

	/**
	 * CONSTRUCTOR
	 * @param instrument the instrument the synthesizer mimics to play sounds
	 * @throws MidiUnavailableException
	 */
	public Player(int instrument) throws MidiUnavailableException{
		syn = MidiSystem.getSynthesizer();
		syn.open();
		this.instrument = instrument;
		mc = syn.getChannels();
		instr = syn.getDefaultSoundbank().getInstruments();
		syn.loadInstrument(instr[this.instrument]);
	}

	/**
	 * CONSTRUCTOR
	 * @throws MidiUnavailableException
	 */
	public Player() throws MidiUnavailableException{
		Synthesizer syn;
		syn = MidiSystem.getSynthesizer();
		syn.open();mc = syn.getChannels();
		Instrument[] instr = syn.getDefaultSoundbank().getInstruments();
		syn.loadInstrument(instr[instrument]);
	}

	/**
	 * plays a given note
	 * @param note the given note
	 */
	public void play(int note){
		mc[1].noteOn(note, velocity);
	}

	/**
	 * plays a given note
	 * @param note the given note
	 * @param velocity the velocity of the note
	 */
	public void play(int note, int velocity){
		mc[1].noteOn(note, velocity);
	}

	/**
	 * mutes a give note
	 * @param note the given note
	 */
	public void stop(int note){
		mc[1].noteOff(note,velocity);
	}

	/**
	 * mutes a give note
	 * @param note the given note
	 * @param velocity the velocity of the note
	 */
	public void stop(int note, int velocity){
		mc[1].noteOff(note,velocity);
	}
	
	/**
	 * changes the instrument the synthesizer mimics to play sounds
	 * @param newInstrument the new instrument the synthesizer mimics to play sounds
	 */
	public void changeInstrument(int newInstrument){
		instrument = newInstrument;
		syn.loadInstrument(instr[instrument]);
	}
	
	/**
	 * changes the default velocity of all the notes
	 * @param velocity the new velocity
	 */
	public void changeVelocity(int velocity){
		this.velocity = velocity;
	}

	/**
	 * plays a major chord that the note is his root (base/bass) note
	 * @param note the root note
	 * @param velocity the velocity of the chord
	 */
	public void majorChord(int note, int velocity){
		play(note, velocity);
		play(note + 4, velocity);
		play(note + 7, velocity);
	}

	/**
	 * plays a major chord that the note is his root (base/bass) note
	 * @param note the root note
	 */
	public void majorChord(int note){
		play(note, velocity);
		play(note + 4, velocity);
		play(note + 7, velocity);
	}
	
	/**
	 * plays a minor chord that the note is his root (base/bass) note
	 * @param note the root note
	 * @param velocity the velocity of the chord
	 */
	public void minorChord(int note, int velocity){
		play(note, velocity);
		play(note + 3, velocity);
		play(note + 7, velocity);
	}
	
	/**
	 * plays a minor chord that the note is his root (base/bass) note
	 * @param note the root note
	 */
	public void minorChord(int note){
		play(note, velocity);
		play(note + 3, velocity);
		play(note + 7, velocity);
	}
	
	/**
	 * plays a diminished chord that the note is his root (base/bass) note
	 * @param note the root note
	 * @param velocity the velocity of the chord
	 */
	public void dimChord(int note, int velocity){
		play(note,velocity);
		play(note + 3, velocity);
		play(note + 6, velocity);
	}

	/**
	 * plays a diminished chord that the note is his root (base/bass) note
	 * @param note the root note
	 */
	public void dimChord(int note){
		play(note, velocity);
		play(note + 3, velocity);
		play(note + 6, velocity);
	}

	/**
	 * plays an augmented chord that the note is his root (base/bass) note
	 * @param note the root note
	 * @param velocity the velocity of the chord
	 */
	public void augChord(int note, int velocity){
		play(note,velocity);
		play(note + 4, velocity);
		play(note + 8, velocity);
	}

	/**
	 * plays an augmented chord that the note is his root (base/bass) note
	 * @param note the root note
	 */
	public void augChord(int note){
		play(note, velocity);
		play(note + 4, velocity);
		play(note + 8, velocity);
	}

	
	///GETTERS & SETTERS
	/**
	 * GETTER
	 * @return the synthesizer used to make sounds using the Sound Card
	 */
	public Synthesizer getSyn() {
		return syn;
	}

	/**
	 * SETTER
	 * @param syn the new synthesizer used to make sounds using the Sound Card
	 */
	public void setSyn(Synthesizer syn) {
		this.syn = syn;
	}

	/**
	 * GETTER
	 * @return the instrument the synthesizer mimics to play sounds
	 */
	public int getInstrument() {
		return instrument;
	}

	/**
	 * SETTER
	 * @param instrument the new instrument the synthesizer shall mimic to play sounds
	 */
	public void setInstrument(int instrument) {
		this.instrument = instrument;
	}

	/**
	 * GETTER
	 * @return the velocity of each note
	 */
	public int getVelocity() {
		return velocity;
	}

	/**
	 * SETTER
	 * @param velocity the velocity of each note
	 */
	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	/**
	 * GETTER
	 * @return  an array of all the playable instruments
	 */
	public Instrument[] getInstr() {
		return instr;
	}

	/**
	 * SETTER
	 * @param instr  an array of all the playable instruments
	 */
	public void setInstr(Instrument[] instr) {
		this.instr = instr;
	}

	/**
	 * GETTER
	 * @return the MIDI channels used to play
	 */
	public MidiChannel[] getMc() {
		return mc;
	}




	/******** CONSTANTS ************/	
	///////REGULAR CONSTANTS////////
	/**
	 * 0.6 seconds
	 */
	public static final int DEFAULT_NOTE_VELOCITY = 600;

	/**
	 * piano
	 */
	public static final int DEFAULT_INSTRUMENT = 90;


	///Notes	
	public static final int Cminus1=0,
			Cminus1S = 1,Dminus1b = Cminus1S,
			Dminus1=2,
			Dminus1S = 3,Eminus1b = Dminus1S,
			Eminus1=4,
			Fminus1=5,
			Fminus1S =6,Gminus1b = Fminus1S,
			Gminus1=7,
			Gminus1S = 8,Aminus1b = Gminus1S,
			Aminus1=9,
			Aminus1S = 10,Bminus1b = Aminus1S,
			Bminus1=11,
			C0=12,
			C0S = 13,D0b = C0S,
			D0=14,
			D0S = 15,E0b = D0S,
			E0=16,
			F0=17,
			F0S =18,G0b = F0S,
			G0=19,
			G0S = 20,A0b = G0S,
			A0=21,
			A0S = 22,B0b = A0S,
			B0=23,
			C1=24,
			C1S = 25,D1b = C1S,
			D1=26,
			D1S = 27,E1b = D1S,
			E1=28,
			F1=29,
			F1S =30,G1b = F1S,
			G1=31,
			G1S = 32,A1b = G1S,
			A1=33,
			A1S = 34,B1b = A1S,
			B1=35,
			C2=36,
			C2S = 37,D2b = C2S,
			D2=38,
			D2S = 39,E2b = D2S,
			E2=40,
			F2=41,
			F2S =42,G2b = F2S,
			G2=43,
			G2S = 44,A2b = G2S,
			A2=45,
			A2S = 46,B2b = A2S,
			B2=47,
			C3=48,
			C3S = 49,D3b = C3S,
			D3=50,
			D3S = 51,E3b = D3S,
			E3=52,
			F3=53,
			F3S =54,G3b = F3S,
			G3=55,
			G3S = 56,A3b = G3S,
			A3=57,
			A3S = 58,B3b = A3S,
			B3=59,
			C4=60,
			C4S = 61,D4b = C4S,
			D4=62,
			D4S = 63,E4b = D4S,
			E4=64,
			F4=65,
			F4S =66,G4b = F4S,
			G4=67,
			G4S = 68,A4b = G4S,
			A4=69,
			A4S = 70,B4b = A4S,
			B4=71,
			C5=72,
			C5S = 73,D5b = C5S,
			D5=74,
			D5S = 75,E5b = D5S,
			E5=76,
			F5=77,
			F5S =78,G5b = F5S,
			G5=79,
			G5S = 80,A5b = G5S,
			A5=81,
			A5S = 82,B5b = A5S,
			B5=83,
			C6=84,
			C6S = 85,D6b = C6S,
			D6=86,
			D6S = 87,E6b = D6S,
			E6=88,
			F6=89,
			F6S =90,G6b = F6S,
			G6=91,
			G6S = 92,A6b = G6S,
			A6=93,
			A6S = 94,B6b = A6S,
			B6=95,
			C7=96,
			C7S = 97,D7b = C7S,
			D7=98,
			D7S = 99,E7b = D7S,
			E7=100,
			F7=101,
			F7S =102,G7b = F7S,
			G7=103,
			G7S = 104,A7b = G7S,
			A7=105,
			A7S = 106,B7b = A7S,
			B7=107,
			C8=108,
			C8S = 109,D8b = C8S,
			D8=110,
			D8S = 111,E8b = D8S,
			E8=112,
			F8=113,
			F8S =114,G8b = F8S,
			G8=115,
			G8S = 116,A8b = G8S,
			A8=117,
			A8S = 118,B8b = A8S,
			B8=119,
			C9=120,
			C9S = 121,D9b = C9S,
			D9=122,
			D9S = 123,E9b = D9S,
			E9=124,
			F9=125,
			F9S =126,G9b = F9S,
			G9=127;

}