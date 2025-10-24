package CardModel;

/*
Code created by Josh Braza
*/
import View.UNOCard;

@SuppressWarnings("serial")
public class NumberCard extends UNOCard {

    public NumberCard(){
    }

    // agora recebe um CardSpec
    public NumberCard(CardSpec spec){
        super(spec);
    }

}
