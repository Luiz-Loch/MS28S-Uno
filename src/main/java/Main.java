/*
Code created by Josh Braza 
*/

import javax.swing.JFrame;

import javax.swing.SwingUtilities;
import View.MainFrame;

import ServerController.Observer;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {					
			public void run() {
				startNewGame();			
			}	
		});	
	}
	
	static public void startNewGame() {
		Observer observer = new Observer();		
		MainFrame frame = new MainFrame(observer);

		observer.setFunc(new Runnable() {
			public void run() {
				frame.setVisible(false);
				startNewGame();
			}
		});
		
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocation(200, 100);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
