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
    
    // Variável para armazenar a escolha da Regra do 9
    private boolean ruleOfNineEnabled;

    // Acumulador para empilhamento de +2
    private int draw2Accumulator = 0;

    // Prompter injetável para isolar dependência de UI (JOptionPane)
    private final Prompter prompter;
    private final Random random = new Random();

    // Prompter: interface simples para obter inputs do usuário
    private interface Prompter {
        int chooseGameMode(Object[] options, Object defaultOption);
        String chooseWildColor(Object[] options, Object defaultOption);
        int confirmNewRound(Object[] options, Object defaultOption);
         
        // Novo método para perguntar sobre a Regra do 9
        int chooseRuleOfNine(Object[] options, Object defaultOption);
        // Novo método para mostrar notificações (como a da Regra do 9)
        void showNotification(String title, String message);
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

        /**
         * Implementação da pergunta sobre a Regra do 9
         */
        @Override
        public int chooseRuleOfNine(Object[] options, Object defaultOption) {
            int n = JOptionPane.showOptionDialog(null,
                    "Deseja jogar com a Regra do 9?",
                    "Regras Opcionais",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options, defaultOption);
            
            // Se "Cancelar" ou fechar a janela
            if (n == 2 || n < 0) {
                System.exit(1);
            }
            return n; // Retorna 0 (Sim) ou 1 (Não)
        }

        /**
         * Implementação da notificação visual
         */
        @Override
        public void showNotification(String title, String message) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
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
        
        // Pergunta sobre a Regra do 9 logo após escolher o modo
        this.ruleOfNineEnabled = requestRuleOfNine();

        
        startGame();
    }

    public void startGame() {
        game = new Game(mode);
        playedCards = new Stack<UNOCard>();

        // First Card
        UNOCard firstCard = game.getCard();
        // garante que a primeira carta não seja wild
        while (firstCard.getValue().equals(W_COLORPICKER) ||
                firstCard.getValue().equals(W_DRAW4PLUS)) {
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
    

    /**
     * Pergunta ao usuário se a Regra do 9 deve ser ativada.
     * @return true se "Sim" for selecionado, false caso contrário.
     */
    private boolean requestRuleOfNine() {
        Object[] options = { "Sim", "Não", "Cancelar" };
        int choice = prompter.chooseRuleOfNine(options, options[0]);
        // choice == 0 é "Sim (Ativar)"
        return (choice == 0);
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
                        
                        // Verifica se a regra está ativa E se a carta é um "9"
                        if (ruleOfNineEnabled && clickedCard.getValue().equals("9")) {
                            // Mostra a notificação visual usando o Prompter
                            prompter.showNotification("Regra do 9!", "REGRA DO 9! O oponente compra 3 cartas!");
                            // Aplica a penalidade
                            game.drawPlus(3);
                            // Pula o turno do oponente
                            game.switchTurn();
                        }
                        
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

        // Ao jogar uma carta, passa o turno (empilhamentos de +2 são tratados em performAction)
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

        // Se há acumulador de +2 ativo, apenas a carta +2 é permitida (CA-EMP+2-06)
        if (draw2Accumulator > 0) {
            return playedCard.getValue().equals(DRAW2PLUS);
        }

        if (playedCard.getColor().equals(topCard.getColor())
                || playedCard.getValue().equals(topCard.getValue())) {
            return true;
        }

        else if (playedCard.getType() == WILD) {
            return true;
        } else if (topCard.getType() == WILD) {
            Color color = ((WildCard) topCard).getWildColor();
            if (color.equals(playedCard.getColor()))
                return true;
        }
        return false;
    }

    // ActionCards
    private void performAction(UNOCard actionCard) {
        // Draw2PLUS -> empilhar em vez de executar imediatamente
        if (actionCard.getValue().equals(DRAW2PLUS)) {
            // incrementa acumulador e avisa UI (CA-EMP+2-01 / CA-EMP+2-05)
            draw2Accumulator += 2;
            infoPanel.updateText("+" + draw2Accumulator + " acumulado");
            infoPanel.repaint();
            // não chamar game.drawPlus aqui; o próximo jogador pode empilhar novamente
            // o turno será passado em playClickedCard()
            return;
        }

        // Outros ACTIONS: comportamento original (ex.: SKIP / REVERSE)
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
        // Se houver acumulador de +2, o jogador que pediu carta deve comprar o acumulado
        if (draw2Accumulator > 0) {
            // entrega as cartas para o jogador atual (CA-EMP+2-03 / CA-EMP+2-04)
            for (Player p : game.getPlayers()) {
                if (p.isMyTurn()) {
                    for (int i = 0; i < draw2Accumulator; i++) {
                        p.obtainCard(game.getCard());
                    }
                    break;
                }
            }

            draw2Accumulator = 0;
            infoPanel.updateText("Game Started");
            session.refreshPanel();

            // após comprar, passa o turno para o próximo jogador
            game.switchTurn();

            // atualiza UI e, se for PC, executa movimento
            session.refreshPanel();
            if(mode==vsPC && canPlay){
                if(game.isPCsTurn())
                    game.playPC(peekTopCard());
            }
            return;
        }

        // comportamento original quando não há acumulador
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
}