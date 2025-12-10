package main.java.CardModel;
/*
Code created by Josh Braza 
*/
import java.awt.Color;

import main.java.View.UNOCard;

@SuppressWarnings("serial")
public class WildCard extends UNOCard {
	private Color chosenColor;
	
	public WildCard() {
	}
	
	public WildCard(String cardValue){
		super(BLACK, WILD, cardValue);		
	}
	
	public void useWildColor(Color wildColor){
		chosenColor = wildColor;
	}
	
	public Color getWildColor(){
		return chosenColor;
	}
}
