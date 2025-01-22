import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.io.IOException;

/**
 * Lance un algorithme de recherche
 * sur un problème donné et affiche le résultat
 */
public class LancerRecherche {

    public static void main(String[] args) throws IOException {

        // fixer le message d'aide
        ArgParse.setUsage
                ("""
                        Utilisation :
                        
                        java LancerRecherche [-prob problem] [-algo algoname]\
                         [-n int] [-k int] [-g int] [-v] [-h]
                        
                        -prob : Le nom du problem {dum, map, vac, puz}. Par défaut vac
                        -algo : L'algorithme {rnd, bfs, dfs, ucs, gfs, astar}. Par défault rnd
                        -n    : La taille du problème Dummy. Par défaut 50
                        -k    : Le facteur de branchement pour Dummy. Par défaut 2
                        -r    : La graine aléatoire pour Dummy (0 à générer) par défaut 1234
                        -v    : Rendre bavard (mettre à la fin)
                        -h    : afficher ceci (mettre à la fin)
                        """
                );

        // récupérer les options de la ligne de commande
        String prob_name = ArgParse.getProblemFromCmd(args);
        String algo_name = ArgParse.getAlgoFromCmd(args);

        // créer un problem, un état initial et un algo
        SearchProblem p = ArgParse.makeProblem(prob_name, args);
        State s = ArgParse.makeInitialState(prob_name);
        TreeSearch algo = ArgParse.makeAlgo(algo_name, p, s);

        // afficher quelques infos
        if (ArgParse.DEBUG)
            System.out.println(p.getDescription());

        // afficher le graphe quand c'est possible (Instances de Problem)
        if (ArgParse.DEBUG)
            System.out.println(p.toDot(s,
                    algo.getSolution(),
                    algo.getVisited()));
        // résoudre
        long startTime = System.currentTimeMillis();
        boolean solved = algo.solve();
        long estimatedTime = System.currentTimeMillis() - startTime;

        if (solved)
            algo.printSuccess();
        else
            algo.printFailure();

        System.out.println("Temps nécessaire " + estimatedTime / 1000. + " sec.");
    }
}
