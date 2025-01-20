package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.ArgParse;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.ArrayList;

// Indication : Une simple liste chaînée est une structure FIFO (First In First Out) on ajoute les éléments en début fin de liste   .

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
    
    /**
     * Lance la recherche pour résoudre le problème
     * <p>A concrétiser pour chaque algorithme.</p>
     * <p>La solution devra être stockée dans end_node.</p>
     *
     * @return Vrai si solution trouvé
     */
    @Override
    public boolean solve() {
        
        ArrayList<State> listeDesEtatsAVisiter = new ArrayList<>();
        ArrayList<State> listeDesEtatsVisites = new ArrayList<>();
        
        // On commence à létat initial
        SearchNode node = SearchNode.makeRootSearchNode(initial_state);
        State state = node.getState();
        listeDesEtatsAVisiter.addFirst(state);
        
        if (ArgParse.DEBUG)
            System.out.print("["+listeDesEtatsAVisiter.getFirst());
        
        while( !listeDesEtatsAVisiter.isEmpty() && !problem.isGoalState(listeDesEtatsAVisiter.getFirst())) {
            
            State etatInitial = listeDesEtatsAVisiter.getFirst();
            
            // Les actions possibles depuis cet état
            ArrayList<Action> actions = problem.getActions(etatInitial);
            
            for(var actionCourante : actions) {
                var newNode = SearchNode.makeChildSearchNode(problem, node, actionCourante);
                var newState = newNode.getState();
                
                if(!listeDesEtatsVisites.contains(newState)){
                    listeDesEtatsAVisiter.addFirst(newState);
                    listeDesEtatsVisites.add(etatInitial);
                }
                
                if (ArgParse.DEBUG)
                    System.out.print(" + " + actionCourante + "] -> ["+newState);
            }
            
            listeDesEtatsAVisiter.remove(etatInitial);
        }
        
        // Enregistrer le noeud final
        end_node = node;
        
        if (ArgParse.DEBUG)
            System.out.println("]");
        
        return false;
    }
}
