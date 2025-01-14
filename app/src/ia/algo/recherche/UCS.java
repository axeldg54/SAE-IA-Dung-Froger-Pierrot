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

        while (!noeudsEtendre.isEmpty()) {
            var noeud = noeudsEtendre.removeFirst(); // 1
            var etat = noeud.getState();

            if (problem.isGoalState(etat)) {
                this.end_node = noeud;
                return true;
            }

            var actions = problem.getActions(etat);

            for (var action : actions) {
                var nouveauNoeud = SearchNode.makeChildSearchNode(problem, noeud, action);
                var nouvelEtat = nouveauNoeud.getState();

    /*
    On définit la fonction g qui donne, pour un nœud donné n, le coût du chemin de n0 jusqu'à n.
    La stratégie peut se résumer ainsi :
    1) quand il faut choisir un nœud de la frontière, il faut toujours prendre le nœud avec la valeur de g la plus petite
    2) au moment d'ajouter un nouveau nœud à la frontière, on vérifie s'il n'y a pas déjà un nœud avec ce même état.
       Si c'est le cas et si ce dernier a une plus grande valeur de g que le nouveau, on le remplace par le nouveau (on vient de trouver un chemin moins coûteux vers ce même état).
       Ces décisions peuvent être implantées par la structure de donnée File de Priorité, mais peuvent très bien être implantés par une liste triée par ordre de g (qu'il faudra retrier à chaque insertion).
     */

                if (!etatsConnus.contains(nouvelEtat)/* && !noeudsEtendre.contains(nouveauNoeud)*/) {
                    noeudsEtendre.add(nouveauNoeud);
                    etatsConnus.add(nouvelEtat);
                }
            }
        }

        end_node = SearchNode.makeRootSearchNode(this.initial_state);
        return false;
    }
}
