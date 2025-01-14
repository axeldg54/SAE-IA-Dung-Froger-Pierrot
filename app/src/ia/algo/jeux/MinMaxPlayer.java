package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class MinMaxPlayer extends Player {

    /**
     * Represente un joueur
     *
     * @param g          l'instance du jeu
     * @param player_one si joueur 1
     */
    public MinMaxPlayer(Game g, boolean player_one) {
        super(g, player_one);
    }

    @Override
    public Action getMove(GameState state) {
        ActionValuePair actionValeur;

        if (state.getPlayerToMove() == PLAYER1) {
            actionValeur = maxValeur(state);
        } else {
            actionValeur = minValeur(state);
        }
        return actionValeur.getAction();
    }

    public ActionValuePair maxValeur(GameState state) { // le meilleur coup du point de vue de max
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }

        double V_max = Integer.MIN_VALUE;
        Action C_max = null;

        for (Action c : game.getActions(state)) {
            GameState S_suivant = (GameState) game.doAction(state, c); /*jouer c dans S*/                 // On joue le coup et récupère un nouvel état
            ActionValuePair actionValue = minValeur(S_suivant);              // On alterne à min

            if (actionValue.getValue() > V_max) {                               // Mise à jour de la meilleure valeur et du coup
                V_max = actionValue.getValue();
                C_max = actionValue.getAction();
            }
        }
        return new ActionValuePair(C_max, V_max);
    }

    public ActionValuePair minValeur(GameState state) {
        if (state.isFinalState()) {
            return new ActionValuePair(null, state.getGameValue());
        }
        double V_min = Integer.MAX_VALUE;                              // Différence par rapport à MaxValeur
        Action C_min = null;

        for (Action c : game.getActions(state)) {
            GameState S_suivant = (GameState) game.doAction(state, c);
            ActionValuePair actionValue = maxValeur(S_suivant);

            if (actionValue.getValue() < V_min) {                            // Différence par rapport à MaxValeur
                V_min = actionValue.getValue();
            }
            C_min = actionValue.getAction();
        }
        return new ActionValuePair(C_min, V_min);
    }
}
