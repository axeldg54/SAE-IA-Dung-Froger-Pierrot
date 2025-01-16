import java.util.ArrayList;

public class PlusProche extends AlgoClassification {
    public PlusProche(Donnees donnees) {
        super(donnees);
    }

    public int compare(Imagette imagette) {
        int min = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < donnees.imagettes.size(); i++) {
            int distance = 0;
            for (int j = 0; j < imagette.tab.length; j++) {
                for (int k = 0; k < imagette.tab[0].length; k++) {
                    distance += Math.abs(imagette.tab[j][k] - donnees.imagettes.get(i).tab[j][k]);
                }
            }
            if (distance < min) {
                min = distance;
                index = i;
            }
        }

        return donnees.imagettes.get(index).etiquette;
    }

    /**
     * Retourne les k imagettes les plus proche de l'imagette passée en paramètre
     * 1 -> Initialiser les 10 premières imagettes par défault dans la liste
     * 2 -> Parcourir la liste des imagettes de test
     * 3 -> Si l'imagette est plus proche que l'imagette la plus loin de ma liste alors on la remplace
     * @param k
     * @param imagette
     * @return
     */
    public ArrayList<Imagette> getPlusProche(int k, Imagette imagette) {
        ArrayList<Imagette> imagettesPlusProche = new ArrayList<>();
        return imagettesPlusProche;
    }
}
