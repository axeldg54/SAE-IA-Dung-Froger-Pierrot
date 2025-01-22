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
        int nbTrainImages = 60000; // Nombre d'images pour l'entraînement (maximum 60000)
        int nbTestImages = 10000; // Nombre d'images à tester (maximum 10000)
        int nbIterations = 10; // Nombre d'itérations pour l'entraînement
        boolean number = false; // true pour les chiffres, false pour les vêtements
        boolean load = false; // true pour charger un réseau de neurones, false pour en créer un nouveau
        boolean train = true; // true pour entraîner le réseau de neurones, false pour ne pas l'entraîner
        boolean save = false; // true pour sauvegarder le réseau de neurones, false pour ne pas le sauvegarder
        boolean generateData = false; // true pour générer les données, false pour ne pas les générer

        // Chargement des images et des étiquettes
        Image imagesEntrainement = new Image(nbTrainImages);
        Label labelsEntrainement = new Label();
        Image imagesTest = new Image(nbTestImages);
        Label labelsTest = new Label();
        loadImagesLabels(number, imagesEntrainement, labelsEntrainement, imagesTest, labelsTest);

        // Création de l'objet Donnees pour stocker les images et les étiquettes
        Donnees donneesTest = new Donnees(imagesTest.imagettes);
        donneesTest.attribuerEtiquettes(labelsTest.etiquettes);
        Donnees donneesEntrainement = new Donnees(imagesEntrainement.imagettes);
        donneesEntrainement.attribuerEtiquettes(labelsEntrainement.etiquettes);

        // Test de l'algorithme KNN
        //double tauxKnn = executeKNN(donneesEntrainement, donneesTest, nbTestImages);
        //System.out.println("Taux de réussite de l'algorithme KNN : " + tauxKnn + "%");

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

    private static void loadImagesLabels(boolean number, Image imagesEntrainement, Label labelsEntrainement, Image imagesTest, Label labelsTest) throws IOException {
        if (number) {
            imagesEntrainement.load("./data/number/train-images.idx3-ubyte");
            labelsEntrainement.load("./data/number/train-labels.idx1-ubyte");
            imagesTest.load("./data/number//t10k-images.idx3-ubyte");
            labelsTest.load("./data/number/t10k-labels.idx1-ubyte");
        } else {
            imagesEntrainement.load("./data/fashion/train-images-idx3-ubyte");
            labelsEntrainement.load("./data/fashion/train-labels-idx1-ubyte");
            imagesTest.load("./data/fashion/t10k-images-idx3-ubyte");
            labelsTest.load("./data/fashion/t10k-labels-idx1-ubyte");
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
