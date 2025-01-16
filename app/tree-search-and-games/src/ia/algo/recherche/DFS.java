package ia.algo.recherche;

import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.LinkedList;

public class DFS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public DFS(SearchProblem p, State s) {
        super(p, s);
    }

    @Override
    public boolean solve() {
        var etatsConnus = new HashSet<State>();
        var noeudsEtendre = new LinkedList<SearchNode>();

        noeudsEtendre.add(SearchNode.makeRootSearchNode(this.initial_state));
        etatsConnus.add(this.initial_state);

        while (!noeudsEtendre.isEmpty()) {
            var noeud = noeudsEtendre.removeFirst();
            var etat = noeud.getState();

            if (problem.isGoalState(etat)) {
                this.end_node = noeud;
                return true;
            }

            var actions = problem.getActions(etat);

            for (var action : actions) {
                var nouveauNoeud = SearchNode.makeChildSearchNode(problem, noeud, action);
                var nouvelEtat = nouveauNoeud.getState();

                if (!etatsConnus.contains(nouvelEtat)/* && !noeudsEtendre.contains(nouveauNoeud)*/) {
                    noeudsEtendre.addFirst(nouveauNoeud); // First difference
                    etatsConnus.add(nouvelEtat);
                }
            }
        }

        end_node = SearchNode.makeRootSearchNode(this.initial_state);
        return false;
    }
}
