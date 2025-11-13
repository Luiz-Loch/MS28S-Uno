package main.java.CardModel;
/*
Code created by Josh Braza 
*/
import java.awt.Color;

import main.java.View.UNOCard;

@SuppressWarnings("serial")
public class NumberCard extends UNOCard {

	public NumberCard(){
	}
	
	public NumberCard(Color cardColor, String cardValue){
		super(cardColor, NUMBERS, cardValue);		
	}

}
