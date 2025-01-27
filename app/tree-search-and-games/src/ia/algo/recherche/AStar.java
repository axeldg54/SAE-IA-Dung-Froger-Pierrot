package ia.algo.recherche;

import ia.framework.common.State;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

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
	
	/**
	 * Lance la recherche pour résoudre le problème
	 * <p>A concrétiser pour chaque algorithme.</p>
	 * <p>La solution devra être stockée dans end_node.</p>
	 *
	 * @return Vrai si solution trouvé
	 */
	@Override
	public boolean solve() {
		return false;
	}
}
