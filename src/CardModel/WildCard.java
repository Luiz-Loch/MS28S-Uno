package CardModel;
/*
Code created by Josh Braza 
*/
import java.awt.Color;
import View.UNOCard;

@SuppressWarnings("serial")
public class WildCard extends UNOCard {
    private Color chosenColor;

    public WildCard() {
    }

    // Construtor principal que aceita CardSpec (Introduce Parameter Object)
    public WildCard(CardSpec spec) {
        super(spec);
    }

    // Overload compatível com chamadas que passaram só o tipo (ex: new WildCard(type))
    public WildCard(int cardType) {
        super(CardSpec.of(BLACK, cardType, null));
    }

    // Overload compatível com versões anteriores que recebiam String
    public WildCard(String cardValue){
        super(CardSpec.of(BLACK, WILD, cardValue));
    }

    public void useWildColor(Color wildColor){
        chosenColor = wildColor;
    }

    public Color getWildColor(){
        return chosenColor;
    }
}
