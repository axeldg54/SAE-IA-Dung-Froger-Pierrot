package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.State;
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
        this.depth = 0;

        if (player == PLAYER1) bestMove = maxVal(state, 0);
        else bestMove = minVal(state, 0);

        System.out.println("Profondeur maximale atteinte : " + depth);

        return bestMove.getAction();
    }

    /**
     * Fonction Minimax pour maximiser le score.
     *
     * @param state l'état actuel du jeu
     * @return le couple action-valeur optimale pour le joueur MAX
     */
    public ActionValuePair maxVal(GameState state, int depth) {
        // Vérifie si l'état est final
//        System.out.println("Profondeur : " + depth);

        if (depth > this.depth) {
            this.depth = depth;
        }

        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double maxValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            this.incStateCounter(); // compteur d'états visités
            State nextState = game.doAction(state, action);

            // Évalue la valeur minimale de l'adversaire
            ActionValuePair nextActionValuePair = minVal((GameState) nextState, depth + 1);

            if (nextActionValuePair.getValue() >= maxValue) {
                maxValue = nextActionValuePair.getValue();
                bestAction = action;
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
//        System.out.println("Profondeur : " + depth);

        if (depth > this.depth) {
            this.depth = depth;
        }

        // Vérifie si l'état est final
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double minValue = Double.POSITIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            this.incStateCounter(); // compteur d'états visités
            State nextState = game.doAction(state, action);

            // Évalue la valeur maximale de l'adversaire
            ActionValuePair nextActionValuePair = maxVal((GameState) nextState, depth + 1);

            if (nextActionValuePair.getValue() <= minValue) {
                minValue = nextActionValuePair.getValue();
                bestAction = action;
            }
        }
        return new ActionValuePair(bestAction, minValue);
    }
}
