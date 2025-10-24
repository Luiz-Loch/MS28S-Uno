package CardModel;
/*
Code created by Josh Braza 
*/
import java.awt.Color;
import java.util.LinkedList;

import Interfaces.GameConstants;
import ServerController.MyCardListener;
import View.UNOCard;

/**
 * This Class contains standard 108-Card stack
 */
public class CardDeck implements GameConstants {
    private static final int WILD_CARD_COUNT = 4;
    private LinkedList<UNOCard> UNOcards;

    public CardDeck(){
        UNOcards = new LinkedList<UNOCard>();

        addCards();
        addCardListener(CARDLISTENER);
    }

    //Create 108 cards for this CardDeck
    private void addCards() {
        for (Color color : UNO_COLORS) {
            addNumberCards(color);
            addActionCards(color);
        }
        addWildCards();
    }
    /**
     * Create 76 NumberCards --> doubles except 0s
     */
    private void addNumberCards(Color color) {
        for (int num : UNO_NUMBERS) {
            int i = 0;
            do {
                UNOcards.add(new NumberCard(CardSpec.of(color, NUMBERS, Integer.toString(num))));
                i++;
            } while (num != 0 && i < 2);
        }
    }

    /**
     * Create 24 ActionCards --> everything twice
     */
    private void addActionCards(Color color) {
        for (String type : ActionTypes) {
            for (int i = 0; i < 2; i++) {
                // 'type' aqui é o cardValue (String), ACTION é a constante int do tipo
                UNOcards.add(new ActionCard(CardSpec.of(color, ACTION, type)));
            }
        }
    }

    /**
     * Create 8 WildTypes --> 4 of each
     */
    private void addWildCards() {
        for (String type : WildTypes) {
            for (int i = 0; i < WILD_CARD_COUNT; i++) {
                // Wild cards usam cor BLACK e tipo WILD
                UNOcards.add(new WildCard(CardSpec.of(BLACK, WILD, type)));
            }
        }
    }

    //Cards have MouseListener
    public void addCardListener(MyCardListener listener){
        for(UNOCard card: UNOcards)
            card.addMouseListener(listener);
    }

    public LinkedList<UNOCard> getCards(){
        return UNOcards;
    }   
}
