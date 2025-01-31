package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class MinMaxPlayer extends Player {

    private int depth;
    private final int maxDepth;

    /**
     * Représente un joueur utilisant l'algorithme Minimax.
     *
     * @param g          l'instance du jeu
     * @param player_one indique si c'est le joueur 1
     */
    public MinMaxPlayer(Game g, boolean player_one, int maxDepth) {
        super(g, player_one);
        this.maxDepth = maxDepth;
        this.depth = 0;
        name = "MinMax";
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair bestMove;
        this.depth = maxDepth;

        if (player == PLAYER1) bestMove = maxVal(state, maxDepth);
        else bestMove = minVal(state, maxDepth);

        System.out.println("Profondeur maximale atteinte : " + this.depth);

        return bestMove.getAction();
    }

    /**
     * Fonction Minimax pour maximiser le score.
     *
     * @param state l'état actuel du jeu
     * @return le couple action-valeur optimale pour le joueur MAX
     */
    public ActionValuePair maxVal(GameState state, int depth) {
//        System.out.println("Profondeur (max) : " + this.depth);

        // Vérifie si l'état est final ou profondeur max atteinte
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        if (depth < this.depth) {
            this.depth = depth;
        }

        double maxValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            this.incStateCounter(); // compteur d'états visités
            GameState nextState = (GameState) game.doAction(state, action);

            // Évalue la valeur minimale de l'adversaire
            if (this.depth > 0) {
                ActionValuePair nextActionValuePair = minVal(nextState, depth--);

                if (nextActionValuePair.getValue() >= maxValue) {
                    maxValue = nextActionValuePair.getValue();
                    bestAction = action;
                }
            } else {
                System.out.println("Profondeur max dépassée !");
//                System.out.println("evaluationFonction : " + nextState.evaluationFunction());

                if (nextState.evaluationFunction() > maxValue) {
                    maxValue = nextState.evaluationFunction();
                    bestAction = action;
                }
            }
        }
        return new ActionValuePair(bestAction, maxValue);
    }

    /**
     * Fonction Minimax pour minimiser le score.
     *
     * @param state l'état actuel du jeu
     * @return le couple action-valeur optimale pour le joueur MIN
     */
    public ActionValuePair minVal(GameState state, int depth) {
//        System.out.println("Profondeur (min) : " + this.depth);

        // Vérifie si l'état est final ou profondeur max atteinte
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        if (depth < this.depth) {
            this.depth = depth;
        }

        double minValue = Double.POSITIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            this.incStateCounter(); // compteur d'états visités
            GameState nextState = (GameState) game.doAction(state, action);

            // Évalue la valeur maximale de l'adversaire
            if (this.depth > 0) {
                ActionValuePair nextActionValuePair = maxVal(nextState, depth--);

                if (nextActionValuePair.getValue() <= minValue) {
                    minValue = nextActionValuePair.getValue();
                    bestAction = action;
                }
            } else {
                System.out.println("Profondeur max dépassée !");
//                System.out.println("evaluationFonction : " + nextState.evaluationFunction());

                if (nextState.evaluationFunction() < minValue) {
                    minValue = nextState.evaluationFunction();
                    bestAction = action;
                }
            }
        }
        return new ActionValuePair(bestAction, minValue);
    }
}
