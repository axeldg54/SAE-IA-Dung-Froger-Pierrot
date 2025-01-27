package ia.algo.recherche;

import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.LinkedList;

public class AStar extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public AStar(SearchProblem p, State s) {
        super(p, s);
    }

    @Override
    public boolean solve() {
        var etatsConnus = new HashSet<State>();
        var frontiere = new LinkedList<SearchNode>();

        // Un nœud correspondant à l'état initial
        frontiere.add(SearchNode.makeRootSearchNode(this.initial_state));
        etatsConnus.add(this.initial_state);

        while (!frontiere.isEmpty()) {
            SearchNode noeud = null;
            var minG = Double.MAX_VALUE;

            for (SearchNode n : frontiere) {
                // cout = g + h
                double g = n.getHeuristic() + n.getCost();

                if (g < minG) {
                    minG = g;
                    noeud = n;
                }
            }

            var etat = noeud.getState();
            frontiere.remove(noeud);

            if (problem.isGoalState(etat)) {
                this.end_node = noeud;
                return true;
            }

            etatsConnus.add(etat);

            for (var action : problem.getActions(etat)) {
                var nouveauNoeud = SearchNode.makeChildSearchNode(problem, noeud, action);
                var nouvelEtat = nouveauNoeud.getState();

                if (!etatsConnus.contains(nouvelEtat) && !frontiere.contains(nouveauNoeud)) {
                    frontiere.add(nouveauNoeud);
                } else {
                    if (frontiere.contains(nouveauNoeud)) {
                        SearchNode jsp = frontiere.get(frontiere.indexOf(nouveauNoeud));
                        double coutNoeudDejaPresent = jsp.getHeuristic() + jsp.getCost();

                        if (coutNoeudDejaPresent > nouveauNoeud.getHeuristic() + nouveauNoeud.getCost()) {
                            // on le remplace par le nouveau nœud
                            frontiere.set(frontiere.indexOf(nouveauNoeud), nouveauNoeud);
                        }
                    }
                }
            }
        }

        end_node = SearchNode.makeRootSearchNode(this.initial_state);
        return false;
    }
}
