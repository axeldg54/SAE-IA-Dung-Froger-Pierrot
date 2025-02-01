import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameEngine;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MainAlgosGames {
	
	public static final int MIN_K = 2;
	public static final int MIN_N = 1000;
	
	public static void main(String[] args) throws IOException {
		String[] nomsAlgos = new String[]{/*"bfs", "dfs", "ucs", */"gfs", "astar"};
		
		// On change uniquement N, puis uniquement K
		for (String nomAlgo : nomsAlgos) {
			enregistrerFichier(nomAlgo, 100, "N", MIN_N, 10000);
			enregistrerFichier(nomAlgo, 100, "K", MIN_K, 20);
		}
		
		MinmaxVsAlpabetaTempsProfondeur("minmax", 50);
		MinmaxVsAlpabetaTempsProfondeur("alphabeta", 50);
		
	}
	
	public static void MinmaxVsAlpabetaTempsProfondeur(String algorithme, int profondeurMax) throws IOException {
		
		BufferedWriter file = new BufferedWriter(
				new FileWriter("./app/tree-search-and-games/data/MinmaxVsAlphabetaTempsProfondeur/" + "temps-profondeur-" + algorithme + ".csv")
		);
		file.write("Jeu;P1;noeuds P1;P2 noeudsP2;Vainqueur;Profondeur;Temps (ms);");
		file.newLine();
		
		String[] argsCustom;
		
		for (int profondeurCourante = 1; profondeurCourante <= profondeurMax; profondeurCourante++) {
			// -game mnk -p1 random -p2 alphabeta
			argsCustom = new String[]{"-game", "mnk", "-p1", "random", "-p2", algorithme, "-d", String.valueOf(profondeurCourante)};
			
			// récupérer les options de la ligne de commande
			String game_name = ArgParse.getGameFromCmd(argsCustom);
			String p1_type = ArgParse.getPlayer1FromCmd(argsCustom);
			String p2_type = ArgParse.getPlayer2FromCmd(argsCustom);
			
			// créer un jeu, des joueurs et le moteur de jeux
			Game game = ArgParse.makeGame(game_name, argsCustom);
			Player p1 = ArgParse.makePlayer(p1_type, game, true, argsCustom);
			Player p2 = ArgParse.makePlayer(p2_type, game, false, argsCustom);
			GameEngine game_engine = new GameEngine(game, p1, p2);
			
			// on joue jusqu'à la fin
			long startTime = System.currentTimeMillis();
			GameState end_game = game_engine.gameLoop();
			long estimatedTime = System.currentTimeMillis() - startTime;
			
			// Partie finie
			String winner = game_engine.getWinner(end_game) != null ? game_engine.getWinner(end_game).getName() : "égalité";
			
			file.write("%s;%s;%d;%s;%d;%s;%d;%d;".formatted(game_name, p1.getName(), p1.getStateCounter(), p2.getName(), p2.getStateCounter(), winner, profondeurCourante, estimatedTime));
			file.newLine();
			
		}
		
		file.close();
	}
	
	public static void enregistrerFichier(String algorithme, int seed, String parametre, int valInit, int valMax) throws IOException {
		BufferedWriter file = new BufferedWriter(
				new FileWriter("./app/tree-search-and-games/data/" + algorithme + "/" + algorithme + "-" + parametre + ".csv")
		);
		file.write("Algo;Problème;Seed;N;K;Cout;Temps;");
		file.newLine();
		
		String[] argsCustom;
		
		for (int i = valInit; i <= valMax; i += valMax / 10) {
			if (parametre.equals("N")) {
				argsCustom = new String[]{"-prob", "dum", "-algo", algorithme, "-n",
						String.valueOf(i), "-k", String.valueOf(MIN_K), "-r", String.valueOf(seed)};
			} else {
				argsCustom = new String[]{"-prob", "dum", "-algo", algorithme, "-n",
						String.valueOf(MIN_N), "-k", String.valueOf(i), "-r", String.valueOf(seed)};
			}
			
			String prob_name = ArgParse.getProblemFromCmd(argsCustom);
			String algo_name = ArgParse.getAlgoFromCmd(argsCustom);
			
			// créer un problem, un état initial et un algo
			SearchProblem p = ArgParse.makeProblem(prob_name, argsCustom);
			State s = ArgParse.makeInitialState(prob_name);
			TreeSearch algo = ArgParse.makeAlgo(algo_name, p, s);
			
			// résoudre
			long startTime = System.currentTimeMillis();
			boolean solved = algo.solve();
			long estimatedTime = System.currentTimeMillis() - startTime;
			
			if (solved) algo.printSuccess();
			else algo.printFailure();
			
			if (parametre.equals("N")) {
				file.write("""
						%s;%s;%d;%d;%d;%f;%d;
						""".formatted(algo_name, prob_name, seed,
						i, MIN_K, algo.getEndNode().getCost(), estimatedTime));
			} else {
				file.write("""
						%s;%s;%d;%d;%d;%f;%d;
						""".formatted(algo_name, prob_name, seed,
						MIN_N, i, algo.getEndNode().getCost(), estimatedTime));
			}
			System.out.println(algo_name);
			System.out.println(parametre + " : " + i);
		}
		file.close();
	}
}
