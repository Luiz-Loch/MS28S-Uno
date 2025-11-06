package View;
/*
Code created by Josh Braza 
*/
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import GameModel.Game;

@SuppressWarnings("serial")
public class Session extends JPanel {
	private PlayerPanel player1;
	private PlayerPanel player2;
	private TablePanel table;	
	
	private Game game;
	
	public Session(Game newGame, UNOCard firstCard){
		setPreferredSize(new Dimension(960,720));
		setBackground(new Color(30,36,40));
		setLayout(new BorderLayout());
		
		game = newGame;
		
		setPlayers();
		table = new TablePanel(firstCard);
		player1.setOpaque(false);
		player2.setOpaque(false);
		
		add(player1,BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(player2, BorderLayout.SOUTH);		
	}
	
	private void setPlayers() {
		player1 = new PlayerPanel(game.getPlayers()[0]);
		player2 = new PlayerPanel(game.getPlayers()[1]);		
	}
	
	public void refreshPanel(){
		player1.setCards();
		player2.setCards();
		
		table.revalidate();		
		revalidate();
		
		player1.enableButtons();
		player2.enableButtons();
	}
	
	public void updatePanel(UNOCard playedCard){
		table.setPlayedCard(playedCard);
		refreshPanel();
	}	
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
