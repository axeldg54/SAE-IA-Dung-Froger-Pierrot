package ia.algo.recherche;

import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.LinkedList;

public class UCS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public UCS(SearchProblem p, State s) {
        super(p, s);
    }

    @Override
    public boolean solve() {
        var etatsConnus = new HashSet<State>();
        var noeudsEtendre = new LinkedList<SearchNode>();

        noeudsEtendre.add(SearchNode.makeRootSearchNode(this.initial_state));
        etatsConnus.add(this.initial_state);

        var minG = Double.MAX_VALUE;

          /*
    2) au moment d'ajouter un nouveau nœud à la frontière, on vérifie s'il n'y a pas déjà un nœud avec ce même état.
       Si c'est le cas et si ce dernier a une plus grande valeur de g que le nouveau, on le remplace par le nouveau (on vient de trouver un chemin moins coûteux vers ce même état).
       Ces décisions peuvent être implantées par la structure de donnée File de Priorité, mais peuvent très bien être implantés par une liste triée par ordre de g (qu'il faudra retrier à chaque insertion).
     */

        while (!noeudsEtendre.isEmpty()) {
            SearchNode noeud = null; // Ne sera de toute façon jamais null

            // Etape 1
            for (SearchNode n : noeudsEtendre) {
                double g = n.getCost();

                System.out.println(g);
                System.out.println(minG);

                if (g < minG) {
                    noeud = n;
                    minG = g;
                }
            }

            var etat = noeud.getState();

            if (problem.isGoalState(etat)) {
                this.end_node = noeud;
                return true;
            }

            var actions = problem.getActions(etat);

            for (var action : actions) {
                var nouveauNoeud = SearchNode.makeChildSearchNode(problem, noeud, action);
                var nouvelEtat = nouveauNoeud.getState();

                // Ajouter un noeud à la frontiere
                if (!etatsConnus.contains(nouvelEtat)/* && !noeudsEtendre.contains(nouveauNoeud)*/) {
                    noeudsEtendre.add(nouveauNoeud);
                    etatsConnus.add(nouvelEtat);
                } else {
                    // Etape 2, il y a un noeud avec ce même état
                    if (nouveauNoeud.getCost() < minG) {
                        noeud = nouveauNoeud;
                    }
                }
            }
        }

        end_node = SearchNode.makeRootSearchNode(this.initial_state);
        return false;
    }
}
