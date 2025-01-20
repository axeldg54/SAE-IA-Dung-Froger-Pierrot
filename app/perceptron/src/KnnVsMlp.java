import lib.MLP;
import me.tongfei.progressbar.ProgressBar;
import transferFunction.Sigmoide;
import java.io.IOException;
import java.util.Arrays;

public class KnnVsMlp {
    public static void main(String[] args) throws IOException {
        // Paramètres
        int nbImages = 1000;
        String mlpPath = "./app/perceptron/data/mlp.ser";
        boolean save = true;

        // Chargement des images et des étiquettes
        Image imagesEntrainement = new Image(nbImages);
        imagesEntrainement.load("./app/mnist/data/train-images.idx3-ubyte");
        Label labelsEntrainement = new Label();
        labelsEntrainement.load("./app/mnist/data/train-labels.idx1-ubyte");
        Image imagesTest = new Image(nbImages);
        imagesTest.load("./app/mnist/data/t10k-images.idx3-ubyte");
        Label labelsTest = new Label();
        labelsTest.load("./app/mnist/data/t10k-labels.idx1-ubyte");

        // Création de l'objet Donnees pour stocker les images et les étiquettes
        Donnees donneesTest = new Donnees(imagesTest.imagettes);
        donneesTest.attribuerEtiquettes(labelsTest.etiquettes);
        Donnees donneesEntrainement = new Donnees(imagesEntrainement.imagettes);
        donneesEntrainement.attribuerEtiquettes(labelsEntrainement.etiquettes);

        // Test de l'algorithme KNN
        PlusProche algo = new PlusProche(donneesEntrainement);
        Statistiques statistiques = new Statistiques(algo, donneesTest);
        System.out.println(statistiques.tauxReussiteKnn(nbImages));

        // chargement ou création d'un réseau de neurones, entraînement et test
        //MLP mlp = MLP.load(mlpPath);
        MLP mlp = new MLP(new int[]{784, 100, 10}, 0.1, new Sigmoide());
        trainWithImages(donneesEntrainement, mlp, nbImages, 100);
        testWithImages(donneesTest, mlp, nbImages);

        // Serialization du réseau de neurones
        if (save) {
            mlp.save(mlpPath);
        }
    }

    /**
     * Retourne l'indice du maximum d'un tableau de double
     * @param result
     * @return
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
     * @param donneesTest
     * @param mlp
     * @param nbImages
     */
    private static void testWithImages(Donnees donneesTest, MLP mlp, int nbImages) {
        if (nbImages > donneesTest.imagettes.size()) {
            System.out.println("Attention, le nombre d'images demandé est supérieur au nombre d'images disponibles");
            nbImages = donneesTest.imagettes.size();
        }
        int nbReussites = 0;
        try (ProgressBar pb = new ProgressBar("Test of the MLP", nbImages)) {
            for (int i = 0; i < nbImages; i++) {
                double[] inputs = Image.imagetteToInput(donneesTest.imagettes.get(i));
                double[] result = mlp.execute(inputs);
                int resultInt = getMax(result);
                System.out.println("Attendu : " + donneesTest.imagettes.get(i).etiquette + " - Trouvé : [" + resultInt + "] " + Arrays.toString(Arrays.stream(result).map(Math::round).toArray()));
                if (donneesTest.imagettes.get(i).etiquette == resultInt) {
                    nbReussites++;
                }
                pb.step();
            }
        }
        System.out.println(" [MLP] => " + (double) nbReussites / nbImages * 100 + "% de réussite");
    }

    /**
     * Entraîne le réseau de neurones avec un nombre d'images et un nombre d'itérations
     * @param donneesEntrainement
     * @param mlp
     * @param nbImages
     * @param nbIterations
     */
    private static void trainWithImages(Donnees donneesEntrainement, MLP mlp, int nbImages, int nbIterations) {
        if (nbImages > donneesEntrainement.imagettes.size()) {
            System.out.println("Attention, le nombre d'images demandé est supérieur au nombre d'images disponibles");
            nbImages = donneesEntrainement.imagettes.size();
        }
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
    
    private static void 
}
