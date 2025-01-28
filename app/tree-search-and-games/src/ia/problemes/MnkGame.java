package ia.problemes;

import ia.framework.jeux.GameState;

/**
 * Représente un jeu du m,n,k (cf. https://en.wikipedia.org/wiki/M,n,k-game)
 */

public class MnkGame extends AbstractMnkGame {

    public MnkGame(int r, int c, int s) {
        super(r, c, s);
    }

    /* {@inheritDoc}
     * <p>Crée une grille vide</p>
     */
    public GameState init() {
        MnkGameState s = new MnkGameState(this.rows, this.cols, this.streak);
        s.updateGameValue();
        return s;
    }
}
