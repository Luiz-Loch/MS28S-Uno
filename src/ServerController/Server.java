package ServerController;
/*
Code created by Josh Braza
*/
import java.awt.Color;

import java.util.Random;
import java.util.Stack;

import javax.swing.JOptionPane;

import CardModel.WildCard;
import GameModel.Game;
import GameModel.Player;
import Interfaces.GameConstants;
import View.Session;
import View.UNOCard;

public class Server implements GameConstants {
    private Game game;
    private Session session;
    private Stack<UNOCard> playedCards;
    public boolean canPlay;
    private int mode;
    private Observer observer;

    // Prompter injetável para isolar dependência de UI (JOptionPane)
    private final Prompter prompter;
    private final Random random = new Random();

    // Prompter: interface simples para obter inputs do usuário
    private interface Prompter {
        int chooseGameMode(Object[] options, Object defaultOption);
        String chooseWildColor(Object[] options, Object defaultOption);
        int confirmNewRound(Object[] options, Object defaultOption);
    }

    // Implementação padrão usando JOptionPane (mantém comportamento atual)
    private static class DefaultPrompter implements Prompter {
        @Override
        public int chooseGameMode(Object[] options, Object defaultOption) {
            int n = JOptionPane.showOptionDialog(null,
                    "Choose a Game Mode to play", "Game Mode",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, defaultOption);
            // Preserva comportamento histórico: se cancelar, encerra
            if (n == 2 || n < 0) {
                System.exit(1);
            }
            return n;
        }

        @Override
        public String chooseWildColor(Object[] options, Object defaultOption) {
            Object chosen = JOptionPane.showInputDialog(null,
                    "Choose a color", "Wild Card Color",
                    JOptionPane.DEFAULT_OPTION, null, options, defaultOption);
            return chosen == null ? null : chosen.toString();
        }

        @Override
        public int confirmNewRound(Object[] options, Object defaultOption) {
            int n = JOptionPane.showOptionDialog(null,
                    "Choose how to proceed", "select",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, defaultOption);
            return n;
        }
    }

    // Construtor padrão (usa DefaultPrompter -> mantém comportamento com UI)
    public Server() {
        this(new DefaultPrompter());
    }

    // Construtor para injeção de Prompter (útil em testes)
    public Server(Prompter prompter) {
        this.prompter = prompter;
        this.mode = requestMode();
        startGame();
    }

    public void startGame() {
        game = new Game(mode);
        playedCards = new Stack<UNOCard>();

        // First Card
        UNOCard firstCard = game.getCard();
        // garante que a primeira carta não seja wild (usa helper)
        while (isWildValue(firstCard)) {
            firstCard = game.getCard();
        }

        modifyFirstCard(firstCard);

        playedCards.add(firstCard);
        session = new Session(game, firstCard);

        game.whoseTurn();
        canPlay = true;
    }

    // obtém o modo via Prompter e converte para valor de GAMEMODES
    private int requestMode() {
        Object[] options = { "vs PC", "Manual", "Cancel" };
        int choice = prompter.chooseGameMode(options, options[0]);
        // DefaultPrompter já lida com cancel; aqui convertemos índice para GAMEMODES
        return GAMEMODES[choice];
    }

    // custom settings for the first card
    private void modifyFirstCard(UNOCard firstCard) {
        firstCard.removeMouseListener(CARDLISTENER);
        if (firstCard.getType() == WILD) {
            int idx = random.nextInt(4); // CORREÇÃO: evita uso inseguro de %
            try {
                ((WildCard) firstCard).useWildColor(UNO_COLORS[idx]);
            } catch (Exception ex) {
                System.out.println("something wrong with modifyFirstCard");
            }
        }
    }

    // return Main Panel
    public Session getSession() {
        return this.session;
    }


    // request to play a card
    public void playThisCard(UNOCard clickedCard) {

        // Check player's turn
        if (!isHisTurn(clickedCard)) {
            infoPanel.setError("It's not your turn");
            infoPanel.repaint();
        } else {

            // Card validation
            if (isValidMove(clickedCard)) {
                boolean cardConfirmed = true;
                // function cards ??
                switch (clickedCard.getType()) {
                    case ACTION:
                        performAction(clickedCard);
                        break;
                    case WILD:
                        cardConfirmed = performWild((WildCard) clickedCard);
                        break;
                    default:
                        break;
                }
                if(cardConfirmed) {
                    playClickedCard(clickedCard);
                }
            } else {
                infoPanel.setError("invalid move");
                infoPanel.repaint();
            }

        }

        if(mode==vsPC && canPlay){
            if(game.isPCsTurn()){
                game.playPC(peekTopCard());
            }
        }
    }

    private void playClickedCard(UNOCard clickedCard) {
        clickedCard.removeMouseListener(CARDLISTENER);
        playedCards.add(clickedCard);
        game.removePlayedCard(clickedCard);

        game.switchTurn();
        clickedCard.setShowValue(true);
        session.updatePanel(clickedCard);
        checkResults();
    }

    // Check if the game is over
    private void checkResults() {

        if (game.isOver()) {
            canPlay = false;
            infoPanel.updateText("GAME OVER");
            gameOverNewSession();
        }
    }

    // check player's turn
    public boolean isHisTurn(UNOCard clickedCard) {

        for (Player p : game.getPlayers()) {
            if (p.hasCard(clickedCard) && p.isMyTurn())
                return true;
        }
        return false;
    }

    // check if it is a valid card
    public boolean isValidMove(UNOCard playedCard) {
        UNOCard topCard = peekTopCard();

        // color or value matches (null-safe) via helper
        if (cardsMatch(playedCard, topCard)) {
            return true;
        }

        // played card is wild
        if (playedCard.getType() == WILD) {
            return true;
        }

        // top card is wild and chosen color matches
        if (topCard.getType() == WILD) {
            Color color = ((WildCard) topCard).getWildColor();
            if (color != null && color.equals(playedCard.getColor()))
                return true;
        }
        return false;
    }

    // ActionCards
    private void performAction(UNOCard actionCard) {
        // Draw2PLUS
        if (actionCard.getValue().equals(DRAW2PLUS))
            game.drawPlus(2);

        game.switchTurn();
    }

    // perform wild card: usa Prompter para escolher cor quando jogador humano
    private boolean performWild(WildCard functionCard) {

        if(mode==vsPC && game.isPCsTurn()){
            int randomIdx = random.nextInt(4);
            functionCard.useWildColor(UNO_COLORS[randomIdx]);
        } else {

            Object[] colors = {"RED","BLUE","GREEN","YELLOW"};
            String chosenColor = prompter.chooseWildColor(colors, colors[0]);

            if (chosenColor == null) {
                return false;
            }

            int idx = java.util.Arrays.asList(colors).indexOf(chosenColor);
            if (idx < 0) idx = 0;
            functionCard.useWildColor(UNO_COLORS[idx]);

        }

        if (functionCard.getValue().equals(W_DRAW4PLUS)) {
            game.drawPlus(4);
            game.switchTurn();
        }

        return true;
    }

    public void requestCard() {
        game.drawCard(peekTopCard());

        if(mode==vsPC && canPlay){
            if(game.isPCsTurn())
                game.playPC(peekTopCard());
        }

        session.refreshPanel();
    }

    public UNOCard peekTopCard() {
        return playedCards.peek();
    }

    public void submitSaidUNO() {
        game.setSaidUNO();
    }

    public void setObserver(Observer newObserver) {
        observer = newObserver;
    }

    public Observer observer() {
        return observer;
    }

    public void gameOverNewSession() {

        Object[] options = { "New round", "Cancel" };

        int n = prompter.confirmNewRound(options, options[0]);

        if (n == 0) {
            observer.runFunc();
        } else {
            System.exit(1);
        }
    }

    // --- Helpers extraídos para esconder delegações e comparar cartas ---

    /** Retorna true se card tem value igual a W_COLORPICKER ou W_DRAW4PLUS (null-safe) */
    private boolean isWildValue(UNOCard card) {
        if (card == null) return false;
        String v = card.getValue();
        return v != null && (v.equals(W_COLORPICKER) || v.equals(W_DRAW4PLUS));
    }

    /** Compara cor/valor entre duas cartas (null-safe) */
    private boolean cardsMatch(UNOCard a, UNOCard b) {
        if (a == null || b == null) return false;
        Color ca = a.getColor();
        Color cb = b.getColor();
        String va = a.getValue();
        String vb = b.getValue();

        boolean colorMatch = (ca != null && cb != null && ca.equals(cb));
        boolean valueMatch = (va != null && vb != null && va.equals(vb));
        return colorMatch || valueMatch;
    }
}
