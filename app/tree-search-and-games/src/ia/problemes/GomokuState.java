package ia.problemes;

/**
 * Représente un état du jeu Gomoku
 */

public class GomokuState extends AbstractMnkGameState {

    public GomokuState() {
        super(15, 15, 5);
    }

    public GomokuState cloneState() {
        GomokuState new_s = new GomokuState();
        new_s.board = this.board.clone();
        new_s.player_to_move = this.player_to_move;
        new_s.game_value = this.game_value;
        if (this.last_action != null)
            new_s.last_action = this.last_action.clone();
        for (Pair p : this.winning_move)
            new_s.winning_move.add(p.clone());

        return new_s;
    }

    /**
     * {@inheritDoc}
     *
     * @return la valeur du jeu
     **/
    public double evaluationFunction() {
        return Double.NaN;
    }
}
