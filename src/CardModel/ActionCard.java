package CardModel;

/*
Code created by Josh Braza 
*/

import View.UNOCard;
import java.awt.Color;

public class ActionCard extends UNOCard {

    private static final long serialVersionUID = 1L;

    public ActionCard() {
    }

    // agora recebe CardSpec e delega para o construtor de UNOCard
    public ActionCard(CardSpec spec){
        super(spec);
    }
}
