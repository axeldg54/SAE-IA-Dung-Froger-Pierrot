package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.State;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class AlphaBetaPlayer extends Player {

    private int depth;
    private final int maxDepth;

    /**
     * Représente un joueur utilisant l'algorithme Minimax.
     *
     * @param g          l'instance du jeu
     * @param player_one indique si c'est le joueur 1
     */
    public AlphaBetaPlayer(Game g, boolean player_one, int maxDepth) {
        super(g, player_one);
        this.maxDepth = maxDepth;
        this.depth = 0;
        name = "MinMaxAlphaBeta";
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair bestMove;
        this.depth = maxDepth;

        if (player == PLAYER1) bestMove = maxVal(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxDepth);
        else bestMove = minVal(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, maxDepth);

        System.out.println("Profondeur maximale atteinte : " + this.depth);

        return bestMove.getAction();
    }

    /**
     * Fonction Minimax pour maximiser le score.
     *
     * @param state l'état actuel du jeu
     * @return le couple action-valeur optimal pour le joueur MAX
     */
    public ActionValuePair maxVal(GameState state, double alpha, double beta) {
        // Vérifie si l'état est final
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
            State nextState = game.doAction(state, action);

            this.incStateCounter(); // compteur d'états visités

            // Évalue la valeur minimale de l'adversaire
            ActionValuePair nextActionValuePair = minVal((GameState) nextState, alpha, beta);

            if (nextActionValuePair.getValue() >= maxValue) {
                maxValue = nextActionValuePair.getValue();

                if (maxValue > alpha) {
                    alpha = maxValue;
                    bestAction = action;
                }
            }

            if (maxValue >= beta) {
                return new ActionValuePair(bestAction, maxValue);
            }
        }

        return new ActionValuePair(bestAction, maxValue);
    }

    /**
     * Fonction Minimax pour minimiser le score.
     *
     * @param state l'état actuel du jeu
     * @return le couple action-valeur optimal pour le joueur MIN
     */
    public ActionValuePair minVal(GameState state, double alpha, double beta) {
        // Vérifie si l'état est final
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double minValue = Double.POSITIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            State nextState = game.doAction(state, action);

            this.incStateCounter(); // compteur d'états visités

            // Évalue la valeur maximale de l'adversaire
            ActionValuePair nextActionValuePair = maxVal((GameState) nextState, alpha, beta);

            if (nextActionValuePair.getValue() <= minValue) {
                minValue = nextActionValuePair.getValue();

                if (minValue < beta) {
                    beta = minValue;
                    bestAction = action;
                }
            }

            if (minValue <= alpha) {
                return new ActionValuePair(bestAction, minValue);
            }
        }

        return new ActionValuePair(bestAction, minValue);
    }

    /**
     * Fonction Minimax pour maximiser le score.
     *
     * @param state
     * @param alpha
     * @param beta
     * @param depth Profondeur de recherche
     * @return
     */
    public ActionValuePair maxVal(GameState state, double alpha, double beta, int depth) {
        // Vérifie si l'état est final
        if (state.isFinalState() || depth == 0) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double maxValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            State nextState = game.doAction(state, action);

            this.incStateCounter(); // compteur d'états visités

            // Évalue la valeur minimale de l'adversaire
            ActionValuePair nextActionValuePair = minVal((GameState) nextState, alpha, beta, depth - 1);

            if (nextActionValuePair.getValue() >= maxValue) {
                maxValue = nextActionValuePair.getValue();

                if (maxValue > alpha) {
                    alpha = maxValue;
                    bestAction = action;
                }
            }

            if (maxValue >= beta) {
                return new ActionValuePair(bestAction, maxValue);
            }
        }

        return new ActionValuePair(bestAction, maxValue);
    }

    /**
     * Fonction Minimax pour minimiser le score.
     *
     * @param state
     * @param alpha
     * @param beta
     * @param depth Profondeur de recherche
     * @return
     */
    public ActionValuePair minVal(GameState state, double alpha, double beta, int depth) {
        // Vérifie si l'état est final
        if (state.isFinalState() || depth == 0) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double minValue = Double.POSITIVE_INFINITY;
        Action bestAction = null;

        // Parcourt toutes les actions possibles
        for (Action action : game.getActions(state)) {
            State nextState = game.doAction(state, action);

            this.incStateCounter(); // compteur d'états visités

            // Évalue la valeur maximale de l'adversaire
            ActionValuePair nextActionValuePair = maxVal((GameState) nextState, alpha, beta, depth - 1);

            if (nextActionValuePair.getValue() <= minValue) {
                minValue = nextActionValuePair.getValue();

                if (minValue < beta) {
                    beta = minValue;
                    bestAction = action;
                }
            }

            if (minValue <= alpha) {
                return new ActionValuePair(bestAction, minValue);
            }
        }

        return new ActionValuePair(bestAction, minValue);
    }
}