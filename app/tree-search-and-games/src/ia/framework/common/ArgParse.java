package ia.framework.common;

import ia.algo.jeux.AlphaBetaPlayer;
import ia.algo.jeux.HumanPlayer;
import ia.algo.jeux.MinMaxPlayer;
import ia.algo.jeux.RandomPlayer;
import ia.algo.recherche.BFS;
import ia.algo.recherche.DFS;
import ia.algo.recherche.RandomTreeSearch;
import ia.algo.recherche.UCS;
import ia.framework.jeux.Game;
import ia.framework.jeux.Player;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;
import ia.problemes.*;

import java.util.Arrays;

/**
 * Quelques méthodes rudimentaires pour lire la ligne de commande
 * et lancer le Schmilblick
 */

public class ArgParse {

    /**
     * Pour afficher plus de chose
     */
    public static boolean DEBUG = false;

    /**
     * Stock le message d'aide
     */
    public static String msg = null;

    /**
     * Spécifie le message d'aide
     */
    public static void setUsage(String m) {
        msg = m;
    }

    /**
     * Affiche un message d'utilisation
     */
    public static void usage() {
        System.err.println(msg);
    }

    /**
     * Retourne la valeur d'un champ demandé
     *
     * @param args Le tableau de la ligne de commande
     * @param arg  Le paramètre qui nous intéresse
     * @return La valeur du paramètre
     */
    public static String getArgFromCmd(String[] args, String arg) {
        if (args.length > 0) {
            int idx = Arrays.asList(args).indexOf(arg);
            if (idx % 2 == 0 && idx < args.length - 1)
                return args[idx + 1];
            if (idx < 0)
                return null;
            usage();
        }
        return null;
    }

    /**
     * Pour vérifier l'existence d'une option donnée
     *
     * @param args Le tableau de la ligne de commande
     * @param arg  L'option qui nous intéresse
     * @return vrai si l'option existe
     */
    public static boolean getFlagFromCmd(String[] args, String arg) {
        int idx = Arrays.asList(args).indexOf(arg);
        return idx >= 0;
    }

    /**
     * Retourne le nom problème choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom du problème ou null
     */
    public static String getProblemFromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-prob");
    }

    /**
     * Retourne le nom du jeu choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom du jeu ou null
     */
    public static String getGameFromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-game");
    }

    /**
     * Retourne le nom l'algorithme choisi
     *
     * @param args Le tableau de la ligne de commande
     * @return le nom de l'algorithme ou null
     */
    public static String getAlgoFromCmd(String[] args) {
        handleFlags(args);

        return getArgFromCmd(args, "-algo");
    }

    /**
     * Retourne le type joueur 1
     *
     * @param args Le tableau de la ligne de commande
     * @return le joueur 1 ou null
     */
    public static String getPlayer1FromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-p1");
    }

    /**
     * Retourne le type joueur 2
     *
     * @param args Le tableau de la ligne de commande
     * @return le joueur 2 ou null
     */
    public static String getPlayer2FromCmd(String[] args) {
        handleFlags(args);
        return getArgFromCmd(args, "-p2");
    }


    /**
     * Retourne la valeur d'un argument de la ligne de commande
     *
     * @param args Le tableau de la ligne de commande
     * @param par  le nom de l'argument
     * @param def  la valeur par default
     * @return la valeur spécifiée ou par celle défaut
     */
    public static int getValueOfParam(String[] args, String par, int def) {
        handleFlags(args);
        String s = getArgFromCmd(args, par);
        if (s != null)
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                usage();
                System.exit(2);
            }
        return def; // valeur par défaut 

    }

    /**
     * Traitement des options -v, -h
     *
     * @param args Le tableau de la ligne de commande
     */
    public static void handleFlags(String[] args) {
        DEBUG = getFlagFromCmd(args, "-v");
        if (getFlagFromCmd(args, "-h")) {
            usage();
            System.exit(0);
        }
    }

    /**
     * Factory qui retourne une instance du problème choisie ou celui par défaut
     *
     * @param prob Le nom du problème ou null
     * @param args Le tableau de la ligne de commande
     * @return Une instance du problème
     */

    public static SearchProblem makeProblem(String prob, String[] args) {
        if (prob == null)
            prob = "vac";
        switch (prob) {
            case "dum":
                return new Dummy(getValueOfParam(args, "-n", 50),
                        getValueOfParam(args, "-k", 2),
                        getValueOfParam(args, "-r", 1234));
            case "map":
                return new RomaniaMap();
            case "vac":
                return new Vacuum();
            case "puz":
                return new EightPuzzle();
            //case "rush":
            //return new RushHour();
            //case "abcd":
            //return new ABCD();

            default:
                System.out.println("Problème inconnu");
                usage();
                System.exit(2);
        }

        return null; // inatteignable, faire plaisir a javac
    }

    /**
     * Factory qui retourne une instance du problème choisie ou celui par défaut
     *
     * @param game Le nom du problème ou null
     * @param args Le tableau de la ligne de commande
     * @return Une instance du problème
     */

    public static Game makeGame(String game, String[] args) {
        if (game == null)
            game = "tictactoe";
        switch (game) {
            case "tictactoe":
                return new TicTacToe();
            case "connect4":
                return new ConnectFour();
            case "gomoku":
                return new Gomoku();
            case "mnk":
                int s = getValueOfParam(args, "-s", 5);
                return new MnkGame(s, s, getValueOfParam(args, "-k", s - 1));
            default:
                System.out.println("Jeux inconnu");
                usage();
                System.exit(2);
        }

        return null; // inatteignable, faire plaisir a javac
    }

    /**
     * Factory qui retourne une instance du problème choisie ou celui par défaut
     *
     * @param p_type le type de joueur
     * @param game   instance du jeu
     * @param is_p1  vrai si joueur num 1
     * @param args   les arguments de ligne de commande
     * @return Une instance de player
     */
    public static Player makePlayer(String p_type, Game game, boolean is_p1, String[] args) {
        if (p_type == null) {
            p_type = "random";
        }

        switch (p_type) {
            case "random":
                return new RandomPlayer(game, is_p1);
            case "human":
                return new HumanPlayer(game, is_p1);
            case "minmax":
                return new MinMaxPlayer(game, is_p1, getValueOfParam(args, "-d", Integer.MAX_VALUE));
            case "alphabeta":
                return new AlphaBetaPlayer(game, is_p1, getValueOfParam(args, "-d", Integer.MAX_VALUE));
            default:
                System.out.println("Joueur inconnu");
                usage();
                System.exit(2);
        }

        return null; // inatteignable, faire plaisir a javac
    }

    /**
     * Factory qui retourne une instance de l'algorithme
     * choisi ou celui par défaut
     *
     * @param algo Le nom de l'algorithme ou null
     * @param p    Le problème a résoudre
     * @param s    L'état initial
     * @return Une instance de l'algorithme
     */
    public static TreeSearch makeAlgo(String algo, SearchProblem p, State s) {
        if (algo == null) {
            algo = "rnd";
        }
        switch (algo) {
            case "rnd":
                return new RandomTreeSearch(p, s);
            case "bfs":
                return new BFS(p, s);
            case "dfs":
                return new DFS(p, s);
            case "ucs":
                return new UCS(p, s);
            //case "gfs":
            //return new GFS(p,s);
            //case "astar":
            //return new AStar(p,s);
            default:
                System.out.println("Algorithme inconnu");
                usage();
                System.exit(2);

        }
        return null;  // inatteignable, faire plaisir a javac
    }

    /**
     * Factory qui retourne une instance de l'état initial du problème choisi
     *
     * @param prob Le nom du problème ou null
     * @return L'état initial qui peut être fixé ou généré aléatoirement
     */
    public static State makeInitialState(String prob) {
        if (prob == null)
            prob = "vac";
        return switch (prob) {
            case "dum" -> Dummy.initialState();
            case "map" -> RomaniaMap.ARAD;
            case "puz" -> new EightPuzzleState();
            //case "rush" -> RushHourState.makePuzzle31();
            //case "abcd" -> new ABCDState(new char[]{'A','D','B','C'});
            default -> new VacuumState();
        };
    }
}
