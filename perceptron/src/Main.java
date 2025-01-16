import lib.MLP;
import operator.AndOperator;
import operator.OrOperator;
import operator.XorOperator;
import transferFunction.Sigmoide;
import transferFunction.Tanh;

/**
 * Ce programme est un exemple d'utilisation d'un réseau de neurones
 * pour résoudre les problèmes logiques AND, OR et XOR.
 */
public class Main {

    public static void main(String[] args) {

        // Création d'un réseau de neurones avec 2 entrées, 2 neurones cachés et 1 sortie
        MLP mlp = new MLP(
                new int[]{
                        2,        // 2 neurones d'entrée
                        2,        // 2 neurones cachés
                        1},       // 1 neurone de sortie
                0.1,              // pas d'apprentissage de 0.1 (c'est-à-dire que les poids sont mis à jour de 0.1 * erreur)
                new Sigmoide()    // fonction de transfert utilisée
        );

        System.out.println(new AndOperator());
        // Entraînement du réseau de neurones
        train(mlp, AndOperator.INPUTS, AndOperator.OUTPUTS, 1000);
        // Test du réseau de neurones
        test(mlp, AndOperator.INPUTS, "AND");

        System.out.println(new OrOperator());
        train(mlp, OrOperator.INPUTS, OrOperator.OUTPUTS, 1000);
        test(mlp, OrOperator.INPUTS, "OR");

        System.out.println(new XorOperator());
        // XOR est un problème non linéaire, il faut donc une fonction de transfert non linéaire
        mlp.setTransferFunction(new Tanh());
        train(mlp, XorOperator.INPUTS, XorOperator.OUTPUTS, 1000);
        test(mlp, XorOperator.INPUTS, "XOR");
    }

    /**
     * Entraîne le réseau de neurones
     * @param mlp réseau de neurones
     * @param inputs données d'entrée
     * @param outputs données de sortie (résultats attendus)
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
     * @param mlp réseau de neurones
     * @param inputs données d'entrée
     * @param operation nom de l'opération (AND, OR, XOR)
     * Affiche le résultat de l'opération pour chaque donnée d'entrée
     */
    private static void test(MLP mlp, double[][] inputs, String operation) {
        for (double[] input : inputs) {
            System.out.println(input[0] + " " + operation + " " + input[1] + " => " + Math.round(mlp.execute(input)[0]));
        }
    }
}
