import me.tongfei.progressbar.ProgressBar;

public class Statistiques {
    public AlgoClassification algo;
    public Donnees donneesTest;

    public Statistiques(AlgoClassification algo, Donnees donneesTest) {
        this.algo = algo;
        this.donneesTest = donneesTest;
    }

    public String tauxReussite(int nbTests) {
        int reussite = 0;
        int echec = 0;
        if (donneesTest.imagettes.size() < nbTests) {
            System.out.println("Nombre de tests supérieur au nombre d'images, réduction à " + donneesTest.imagettes.size() + " tests");
            nbTests = donneesTest.imagettes.size();
        }
        try (ProgressBar pb = new ProgressBar("Calcul Taux", nbTests)) {
            for (int i = 0; i < nbTests; i++) {
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
            return "100% de réussite";
        } else {
            double taux = (double) reussite / (reussite + echec) * 100;
            taux = Math.round(taux * 100.0) / 100.0;
            return taux + "% de réussite";
        }
    }
}
