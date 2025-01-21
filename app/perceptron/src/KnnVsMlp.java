import lib.MLP;
import me.tongfei.progressbar.ProgressBar;
import transferFunction.Sigmoide;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class KnnVsMlp {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Paramètres
        String mlpPath = "./app/perceptron/data/mlp.ser";
        int nbTrainImages = 1000; // Maximum 60000
        int nbTestImages = 999; // Maximum 999
        int nbIterations = 10;
        boolean load = false;
        boolean train = true;
        boolean save = true;
        boolean generateData = false;

        // Chargement des images et des étiquettes
        Image imagesEntrainement = new Image(nbTrainImages);
        imagesEntrainement.load("./app/mnist/data/train-images.idx3-ubyte");
        Label labelsEntrainement = new Label();
        labelsEntrainement.load("./app/mnist/data/train-labels.idx1-ubyte");
        Image imagesTest = new Image(nbTestImages);
        imagesTest.load("./app/mnist/data/t10k-images.idx3-ubyte");
        Label labelsTest = new Label();
        labelsTest.load("./app/mnist/data/t10k-labels.idx1-ubyte");

        // Création de l'objet Donnees pour stocker les images et les étiquettes
        Donnees donneesTest = new Donnees(imagesTest.imagettes);
        donneesTest.attribuerEtiquettes(labelsTest.etiquettes);
        Donnees donneesEntrainement = new Donnees(imagesEntrainement.imagettes);
        donneesEntrainement.attribuerEtiquettes(labelsEntrainement.etiquettes);

        // Test de l'algorithme KNN
        double tauxKnn = executeKNN(donneesEntrainement, donneesTest, nbTestImages);
        System.out.println("Taux de réussite de l'algorithme KNN : " + tauxKnn + "%");

        // chargement ou création d'un réseau de neurones, entraînement et test
        MLP mlp;
        if (load) {
            mlp = MLP.load(mlpPath);
        } else {
            mlp = new MLP(new int[]{784, 100, 10}, 0.1, new Sigmoide());
        }

        if (train) {
            trainWithImages(donneesEntrainement, mlp, nbTrainImages, nbIterations);
        }

        double tauxMlp = testWithImages(donneesTest, mlp, nbTestImages);
        System.out.println("Taux de réussite du réseau de neurones : " + tauxMlp + "%");

        // Serialization du réseau de neurones
        if (save) {
            mlp.save(mlpPath);
        }

        if (generateData) {
            generateData(donneesEntrainement, nbTestImages, nbIterations, donneesTest);
        }
    }

    private static void generateData(Donnees donneesEntrainement, int nbImages, int nbIterations, Donnees donneesTest) throws IOException {
        BufferedWriter file = new BufferedWriter(new FileWriter("./app/perceptron/data/mlp3.csv"));
        file.write("nbNeuronesCaches1;nbNeuronesCaches2;taux;resultat en %");
        file.newLine();

        double[] l = {0.01, 0.05, 0.1, 0.3, 0.5};

        //for (int i = 10; i <= 10000; i += 100) { // parcours de différents nombres d'images
        //for (int i = 0; i <= 1; i++) { // Données mélangées ou non
        for (int j = 25; j <= 101; j += 25) { // parcours de différents nombres de neurones cachés (première couche cachée)
            for (int k = 0; k <= 100; k += 25) { // parcours de différents nombres de neurones cachés (seconde couche cachée)
                for (double lCourant : l) { // parcours de différents taux d'apprentissage
                    MLP mlp2 = new MLP(new int[]{784, j, k, 10}, lCourant, new Sigmoide());
                    //if (i == 1) {
                    //donneesEntrainement = donneesEntrainement.getRandom();
                    //}
                    trainWithImages(donneesEntrainement, mlp2, nbImages, nbIterations);
                    double res2 = testWithImages(donneesTest, mlp2, nbImages);
                    file.write("""
                            %d;%d;%f;%f
                            """.formatted(j, k, lCourant, res2));
                    System.out.println("l = " + l);
                }
                System.out.println("k = " + k);
            }
            System.out.println("j = " + j);
        }
        //}
        //}
        file.close();
    }

    /**
     * Retourne l'indice du maximum d'un tableau de double
     *
     * @param result tableau de double contenant les valeurs résultat de réseau de neurone
     * @return indice du maximum d'un tableau de double
     */
    private static int getMax(double[] result) {
        double max = 0;
        int resultInt = -1;
        int i = 0;
        for (double val : result) {
            if (val > max) {
                max = val;
                resultInt = i;
            }
            i++;
        }
        return resultInt;
    }

    /**
     * Teste le réseau de neurones avec un nombre d'images
     *
     * @param donneesTest
     * @param mlp
     * @param nbImages
     */
    private static double testWithImages(Donnees donneesTest, MLP mlp, int nbImages) {
        int nbReussites = 0;
        try (ProgressBar pb = new ProgressBar("Test of the MLP", nbImages)) {
            for (int i = 0; i < nbImages; i++) {
                double[] inputs = Image.imagetteToInput(donneesTest.imagettes.get(i));
                double[] result = mlp.execute(inputs);
                int resultInt = getMax(result);
                //System.out.println("Attendu : " + donneesTest.imagettes.get(i).etiquette + " - Trouvé : [" + resultInt + "] " + Arrays.toString(Arrays.stream(result).map(Math::round).toArray()));
                if (donneesTest.imagettes.get(i).etiquette == resultInt) {
                    nbReussites++;
                }
                pb.step();
            }
        }
        double pourcentageDeReussite = (double) nbReussites / nbImages * 100;
        return pourcentageDeReussite;
    }

    /**
     * Entraîne le réseau de neurones avec un nombre d'images et un nombre d'itérations
     *
     * @param donneesEntrainement
     * @param mlp
     * @param nbImages
     * @param nbIterations
     */
    private static void trainWithImages(Donnees donneesEntrainement, MLP mlp, int nbImages, int nbIterations) {
        try (ProgressBar pb = new ProgressBar("Train of the MLP", nbIterations)) {
            for (int i = 0; i < nbIterations; i++) {
                for (int j = 0; j < nbImages; j++) {
                    double[] inputs = Image.imagetteToInput(donneesEntrainement.imagettes.get(j));
                    double[] outputs = new double[10];
                    outputs[donneesEntrainement.imagettes.get(j).etiquette] = 1;
                    mlp.backPropagate(inputs, outputs);
                }
                pb.step();
            }
        }
    }

    /**
     * Exécute l'algorithme KNN
     *
     * @param donneesEntrainement
     * @param donneesTest
     * @param nbImages
     * @return
     */
    private static double executeKNN(Donnees donneesEntrainement, Donnees donneesTest, int nbImages) {
        PlusProche algo = new PlusProche(donneesEntrainement);
        Statistiques statistiques = new Statistiques(algo, donneesTest);
        return statistiques.tauxReussiteKnn(nbImages);
    }
}
