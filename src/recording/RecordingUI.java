package recording;

import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;

public class RecordingUI extends JFrame implements ActionListener{

	private Recorder recorder;
	private JFileChooser chooser;
	private JTextArea fileDisplay;
	
	public RecordingUI() throws LineUnavailableException{
		super("Recording Phase");
		recorder = new Recorder();
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    chooser.setAcceptAllFileFilterUsed(false);
		
		init_graphics();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setSize(600,600);
	}
	
	void init_graphics(){
		
		JButton choose = new JButton("Browse Files");
		choose.setActionCommand("choose file");
		choose.addActionListener(this);
		JButton start = new JButton("Start");
		start.setActionCommand("start recording");
		start.addActionListener(this);
		JButton stop = new JButton("Stop");
		stop.setActionCommand("stop recording");
		stop.addActionListener(this);
		fileDisplay = new JTextArea();
		
		setLayout(new GridLayout(2,2));
		add(start);
		add(stop);
		add(choose);
		add(fileDisplay);
	}
	
	public void actionPerformed(ActionEvent e){
		String command = e.getActionCommand();
		if(command.equals("choose file")){
			int r = chooser.showOpenDialog(new JFrame());
			if (r == JFileChooser.APPROVE_OPTION) {
			      String name = chooser.getSelectedFile().getAbsolutePath();
			      recorder.setFile(name);
			}
			fileDisplay.setText(recorder.getFile());
		}
		else if(command.equals("start recording")){
			try {
				recorder.record();
			} catch (LineUnavailableException e1) {
				e1.printStackTrace();
			}
		}
		else if(command.equals("stop recording")){
			recorder.stopRecording();
		}
	}
	
	public static void main(String[] args) throws LineUnavailableException{
		new RecordingUI();
	}
}
