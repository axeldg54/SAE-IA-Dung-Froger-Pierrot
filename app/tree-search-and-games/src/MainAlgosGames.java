import ia.framework.common.ArgParse;
import ia.framework.common.State;
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
