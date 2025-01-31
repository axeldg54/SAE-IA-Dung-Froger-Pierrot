package ia.problemes;

/**
 * Représente un état d'un jeu générique m,n,k Game
 */

public class MnkGameState extends AbstractMnkGameState {


    /**
     * Construire une grille vide de la bonne taille
     *
     * @param r nombre de lignes
     * @param c nombre de colonnes
     */
    public MnkGameState(int r, int c, int s) {
        super(r, c, s);
    }

    public MnkGameState cloneState() {
        MnkGameState new_s = new MnkGameState(this.rows, this.cols, this.streak);
        new_s.board = this.board.clone();
        new_s.player_to_move = player_to_move;
        new_s.game_value = game_value;
        if (this.last_action != null)
            new_s.last_action = this.last_action.clone();
        for (Pair p : this.winning_move)
            new_s.winning_move.add(p.clone());
        return new_s;
    }

    /**
     * Une fonction d'évaluation pour cet état du jeu.
     * Permet de comparer différents états dans le cas où on ne
     * peut pas développer tout l'arbre. Le joueur 1 (X) choisira les
     * actions qui mènent aux états de valeur maximale, Le joueur 2 (O)
     * choisira les valeurs minimales.
     * <p>
     * Cette fonction dépend du jeu.
     *
     * @return la valeur du jeux
     **/
    protected double evaluationFunction() {
        return Double.NaN;
    }
}
