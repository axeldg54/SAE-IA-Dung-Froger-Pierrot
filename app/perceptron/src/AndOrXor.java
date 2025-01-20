import lib.MLP;
import lib.TransferFunction;
import operator.AndOperator;
import operator.Operator;
import operator.OrOperator;
import operator.XorOperator;
import transferFunction.Sigmoide;
import transferFunction.Tanh;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Ce programme est un exemple d'utilisation d'un réseau de neurones
 * pour résoudre les problèmes logiques AND, OR et XOR.
 */
public class AndOrXor {
	
	public static void main(String[] args) throws IOException {
		
		//enregistrerDonnees();
		
		// Création d'un réseau de neurones avec 2 entrées, 2 neurones cachés et 1 sortie
		MLP mlp = new MLP(
				new int[]{
						2,        // 2 neurones d'entrée
						2,        // 2 neurones cachés
						1},       // 1 neurone de sortie
				0.3,              // pas d'apprentissage de 0.1 (c'est-à-dire que les poids sont mis à jour de 0.1 * erreur)
				new Sigmoide()    // fonction de transfert utilisée
		);
		
		System.out.println(new AndOperator());
		// Entraînement du réseau de neurones
		train(mlp, AndOperator.INPUTS, AndOperator.OUTPUTS, 10000);
		// Test du réseau de neurones
		test(mlp, AndOperator.INPUTS, "AND");
		
		System.out.println(new OrOperator());
		train(mlp, OrOperator.INPUTS, OrOperator.OUTPUTS, 10000);
		test(mlp, OrOperator.INPUTS, "OR");
		
		System.out.println(new XorOperator());
		mlp.setTransferFunction(new Tanh());
		train(mlp, XorOperator.INPUTS, XorOperator.OUTPUTS, 10000);
		test(mlp, XorOperator.INPUTS, "XOR");
	}
	
	/**
	 * Entraîne le réseau de neurones
	 *
	 * @param mlp          réseau de neurones
	 * @param inputs       données d'entrée
	 * @param outputs      données de sortie (résultats attendus)
	 * @param nbIterations nombre de fois où les données d'entrée sont utilisées pour l'entraînement
	 */
	private static void train(MLP mlp, double[][] inputs, double[][] outputs, int nbIterations) {
		for (int i = 0; i < nbIterations; i++) {
			for (int j = 0; j < inputs.length; j++) {
				mlp.backPropagate(inputs[j], outputs[j]);
			}
		}
	}
	
	/**
	 * Teste le réseau de neurones
	 *
	 * @param mlp       réseau de neurones
	 * @param inputs    données d'entrée
	 * @param operation nom de l'opération (AND, OR, XOR)
	 *                  Affiche le résultat de l'opération pour chaque donnée d'entrée
	 */
	private static void test(MLP mlp, double[][] inputs, String operation) {
		for (double[] input : inputs) {
			System.out.println(input[0] + " " + operation + " " + input[1] + " => " + mlp.execute(input)[0]);
		}
	}
	
	/**
	 * Méthode qui permet d'enregistrer les données générées dans un fichier pour permettre la création de grahiques
	 */
	private static void enregistrerDonnees() throws IOException {
		// vérification de la présence du fichier dans lequel on veut écrire
		
		// premier enregistrement :
		//      - tanh avec 2 couches cachées et 2 neurones par couches,
		//      - test de 10 à 100 000 itérations d'entrainement et
		//      - vérification des résultats obtenus pour toutes les opérations (AND, OR et XOR)
		
		// lancement de différents scénarios de test avec :
		//   - les 2 types de fonctions de transfert (tanh et sigmoïde),
		//   - toutes les opérations possibles : AND, OR et XOR,
		//   - différents nombres d'itérations pour l'entrainement du réseau de neurone (10, 100, 1000, 10000),
		//   - différents nombres de couches cachées (1, 2, 4, 10 ?) et de neurone par couche
		//   - et pour finir, le temps d'entrainement
		
		List<TransferFunction> listFonctions = Arrays.asList(new Tanh(), new Sigmoide());
		List<Operator> listOperators = Arrays.asList(new AndOperator(), new OrOperator(), new XorOperator());
		int nbIteration = 1000;
		
		// on fait des graphiques pour tous les types de fonctions de transfert possibles
		for (Operator operator : listOperators) {
			for (TransferFunction function : listFonctions) {
				MLP mlp = new MLP(new int[]{2, 2, 1}, 0.1, function);
				
				BufferedWriter file = new BufferedWriter(new FileWriter("./app/perceptron/data/" + function.toString() + "/" + operator.toString() + ".csv"));
				
				file.write("Fonction;Operation;nbIteration;Marge d'erreur (%);Temps d'entrainement(nanosecondes);");
				file.newLine();
				
				// on veut tester différents nombres d'itérations d'entrainement (de 10 à 10 000) pour visioner l'amélioration des performances
				for (int nbIterCourant = 10; nbIterCourant <= nbIteration; nbIterCourant++) {
					
					var t_debut = System.nanoTime();
					
					// entrainement du réseau de neurone avec le nombre d'itérations courant
					for (int i = 0; i < nbIterCourant; i++) {
						for (int j = 0; j < operator.getInputs().length; j++) {
							mlp.backPropagate(operator.getInputs()[j], operator.getOutputs()[j]);
						}
					}
					
					var t_fin = System.nanoTime();
					var temps = t_fin - t_debut; // on calcule le temps d'entrainement pour ensuite le comparer aux performances
					
					double marge = 0;
					
					// test du réseau de neurone et sauvegarde des résultats dans le fichier csv
					for (int k = 0; k < operator.getInputs().length; k++) {
						var result = mlp.execute(operator.getInputs()[k])[0];
						var expected = operator.getOutputs()[k][0];
						
						marge += Math.abs(result - expected);
					}
					
					double margeDerreur = (marge / operator.getInputs().length) * 100;
					
					file.write("""
							%s;%s;%d;%f;%d;
							""".formatted(function.toString(), operator.toString(), nbIterCourant,
							margeDerreur, temps));
				}
				file.close();
			}
		}
	}
}
