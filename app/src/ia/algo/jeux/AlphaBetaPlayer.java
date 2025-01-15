package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.common.State;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class AlphaBetaPlayer extends Player {
	
	/**
	 * Représente un joueur utilisant l'algorithme Minimax.
	 *
	 * @param g          l'instance du jeu
	 * @param player_one indique si c'est le joueur 1
	 */
	public AlphaBetaPlayer(Game g, boolean player_one) {
		super(g, player_one);
		name = "MinMaxAlphaBeta";
	}
	
	@Override
	public Action getMove(GameState state) {
		ActionValuePair bestMove;
		
		if (player == PLAYER1) {
			bestMove = maxVal(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		} else {
			bestMove = minVal(state, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
		}
		
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
		
		double maxValue = Double.NEGATIVE_INFINITY;
		Action bestAction = null;
		
		// Parcourt toutes les actions possibles
		for (Action action : game.getActions(state)) {
			State nextState = game.doAction(state, action);
			
			// Évalue la valeur minimale de l'adversaire
			ActionValuePair nextActionValuePair = minVal((GameState) nextState, alpha, beta);
			
			if (nextActionValuePair.getValue() >= maxValue) {
				maxValue = nextActionValuePair.getValue();
				bestAction = action;
			}
			
			if (maxValue > alpha) {
				alpha = maxValue;
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
			
			// Évalue la valeur maximale de l'adversaire
			ActionValuePair nextActionValuePair = maxVal((GameState) nextState, alpha, beta);
			
			if (nextActionValuePair.getValue() <= minValue) {
				minValue = nextActionValuePair.getValue();
				bestAction = action;
			}
			
			if (minValue < beta) {
				beta = minValue;
			}
			
			if (minValue <= alpha) {
				return new ActionValuePair(bestAction, minValue);
			}
		}
		
		return new ActionValuePair(bestAction, minValue);
	}
}
