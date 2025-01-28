package ia.algo.recherche;

import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.HashSet;
import java.util.LinkedList;

public class BFS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param p Le problème à résoudre
     * @param s L'état initial
     */
    public BFS(SearchProblem p, State s) {
        super(p, s);
    }

    @Override
    public boolean solve() {
        var etatsConnus = new HashSet<State>();
        var frontiere = new LinkedList<SearchNode>();

        // Un noeud correspondant à l'état initial
        frontiere.add(SearchNode.makeRootSearchNode(this.initial_state));
        etatsConnus.add(this.initial_state);

        while (!frontiere.isEmpty()) {
            var noeud = frontiere.removeFirst();
            var etat = noeud.getState();

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
                }
            }
        }

        end_node = SearchNode.makeRootSearchNode(this.initial_state);
        return false;
    }
}
