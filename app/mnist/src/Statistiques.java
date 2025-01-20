import me.tongfei.progressbar.ProgressBar;

public class Statistiques {
    public AlgoClassification algo;
    public Donnees donneesTest;

    public Statistiques(AlgoClassification algo, Donnees donneesTest) {
        this.algo = algo;
        this.donneesTest = donneesTest;
    }

    public String tauxReussiteKnn(int nbImages) {
        int reussite = 0;
        int echec = 0;
        if (donneesTest.imagettes.size() < nbImages) {
            System.out.println("Attention, le nombre d'images demandé est supérieur au nombre d'images disponibles");
            nbImages = donneesTest.imagettes.size();
        }
        try (ProgressBar pb = new ProgressBar("Test of KNN", nbImages)) {
            for (int i = 0; i < nbImages; i++) {
                if (algo.compare(donneesTest.imagettes.get(i)) == donneesTest.imagettes.get(i).etiquette) {
                    reussite += 1;
                    pb.step();
                } else {
                    echec += 1;
                    pb.step();
                }
            }
        }
        if (echec == 0) {
            return "[KNN] => 100% de réussite";
        } else {
            double taux = (double) reussite / (reussite + echec) * 100;
            taux = Math.round(taux * 100.0) / 100.0;
            return " [KNN] => " + taux + "% de réussite";
        }
    }
}
