package CardModel;

import java.awt.Color;

public class CardSpec {
    private final Color color;
    private final int type;
    private final String value;

    public CardSpec(Color color, int type, String value) {
        this.color = color;
        this.type = type;
        this.value = value;
    }

    public Color getColor() { return color; }
    public int getType() { return type; }
    public String getValue() { return value; }

    public static CardSpec of(Color color, int type, String value){
        return new CardSpec(color, type, value);
    }
}
