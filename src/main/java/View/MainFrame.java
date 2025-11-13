package main.java.View;
/*
Code created by Josh Braza 
*/

import javax.swing.JFrame;

import main.java.Interfaces.GameConstants;
import main.java.ServerController.Server;

import main.java.ServerController.Observer;


@SuppressWarnings("serial")
public class MainFrame extends JFrame implements GameConstants {
	
	private Session mainPanel;
	private Server server;
	private Observer observer;
	
	public void setObserver(Observer newObserver) {
		observer = newObserver;
	}
	
	public Observer observer() {
		return observer;
	}
	
	public MainFrame(Observer observer){	
		setObserver(observer);
		startGame();
	}
	
	public void startGame() {
		server = new Server();
		CARDLISTENER.setServer(server);
		BUTTONLISTENER.setServer(server);
						
		server.setObserver(observer);
		
		mainPanel = server.getSession();
		add(mainPanel);
	}
}
