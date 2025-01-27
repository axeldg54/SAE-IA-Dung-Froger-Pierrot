import lib.MLP;
import lib.TransferFunction;
import me.tongfei.progressbar.ProgressBar;
import transferFunction.Sigmoide;
import transferFunction.Tanh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class KnnVsMlp {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Paramètres
		String mlpPath = "./app/perceptron/data/mlp.ser";
		int nbImages = 10000; // Maximum 60000
		int nbIterations = 100;
		boolean save = true;
		boolean load = false;
		boolean train = true;
		
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
		double resKnn = executeKNN(donneesEntrainement, donneesTest, nbImages);
		System.out.println(resKnn);
		
		// chargement ou création d'un réseau de neurones, entraînement et test
		//		MLP mlp = load ? MLP.load(mlpPath) : new MLP(new int[]{784, 100, 10}, 0.1, new Sigmoide());
		//		if (train) {
		//			trainWithImages(donneesEntrainement, mlp, nbImages, nbIterations);
		//		}
		//		double resNeurones = testWithImages(donneesTest, mlp, nbImages);
		//		System.out.println(resNeurones);
		//
		//		// Serialization du réseau de neurones
		//		if (save) {
		//			mlp.save(mlpPath);
		//		}
		
		BufferedWriter file = new BufferedWriter(new FileWriter("./app/perceptron/data/mlp_10000EntrainementEtTest_100iterations2.csv"));
		file.write("Fonction de transfert;nbNeuronesCaches1;nbNeuronesCaches2;taux;resultat mlp en %");
		file.newLine();
		
		double[] l = {0.01, 0.05, 0.1, 0.3, 0.5};
		TransferFunction[] func = {new Sigmoide(), new Tanh()};
		
		// plus de 24h d'exécution pour ces boucles imbriquées
		for (int j = 200; j <= 300; j += 50) { // parcours de différents nombres de neurones cachés (première couche cachée)
			for (int k = 0; k <= 300; k += 100) { // parcours de différents nombres de neurones cachés (seconde couche cachée)
				for (double lCourant : l) { // parcours de différents taux d'apprentissage
					for (TransferFunction functionCourante : func) {
						
						MLP mlp2 = new MLP(new int[]{784, j, k, 10}, lCourant, functionCourante);
						
						if (k == 0) { // dans le cas où la deuxième couche cachée de neurones est vide : on ne l'écrit tout simplement pas
							mlp2 = new MLP(new int[]{784, j, 10}, lCourant, functionCourante);
						}
						
						// on mélange les données
						donneesEntrainement = donneesEntrainement.shuffle();
						donneesTest = donneesTest.shuffle();
						
						// entrainement du réseau de neurones
						trainWithImages(donneesEntrainement, mlp2, nbImages, nbIterations);
						
						// test du réseau de neurones
						double resmlp2 = testWithImages(donneesTest, mlp2, nbImages);
						
						// on écrit les résultats obtenus dans le fichier de sauvegarde
						file.write("""
								%s;%d;%d;%f;%f
								""".formatted(functionCourante.toString(), j, k, lCourant, resmlp2));
						
						System.out.println("l = " + functionCourante);
					}
					System.out.println("l = " + lCourant);
				}
				System.out.println("k = " + k);
			}
			System.out.println("j = " + j);
		}
		
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
		int resultInt = - 1;
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
		if (nbImages > donneesTest.imagettes.size()) {
			System.out.println("Attention, le nombre d'images demandé est supérieur au nombre d'images disponibles");
			nbImages = donneesTest.imagettes.size();
		}
		int nbReussites = 0;
		//		try (ProgressBar pb = new ProgressBar("Test of the MLP", nbImages)) {
		for (int i = 0; i < nbImages; i++) {
			double[] inputs = Image.imagetteToInput(donneesTest.imagettes.get(i));
			double[] result = mlp.execute(inputs);
			int resultInt = getMax(result);
			//				System.out.println("Attendu : " + donneesTest.imagettes.get(i).etiquette + " - Trouvé : [" + resultInt + "] " + Arrays.toString(Arrays.stream(result).map(Math::round).toArray()));
			if (donneesTest.imagettes.get(i).etiquette == resultInt) {
				nbReussites++;
			}
			//				pb.step();
		}
		//		}
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
		if (nbImages > donneesEntrainement.imagettes.size()) {
			System.out.println("Attention, le nombre d'images demandé est supérieur au nombre d'images disponibles");
			nbImages = donneesEntrainement.imagettes.size();
		}
		//		try (ProgressBar pb = new ProgressBar("Train of the MLP", nbIterations)) {
		for (int i = 0; i < nbIterations; i++) {
			for (int j = 0; j < nbImages; j++) {
				double[] inputs = Image.imagetteToInput(donneesEntrainement.imagettes.get(j));
				double[] outputs = new double[10];
				outputs[donneesEntrainement.imagettes.get(j).etiquette] = 1;
				mlp.backPropagate(inputs, outputs);
			}
			//				pb.step();
		}
		//		}
	}
	
	
	private static double executeKNN(Donnees donneesEntrainement, Donnees donneesTest, int nbImages) {
		PlusProche algo = new PlusProche(donneesEntrainement);
		Statistiques statistiques = new Statistiques(algo, donneesTest);
		return statistiques.tauxReussiteKnn(nbImages);
	}
	
}
