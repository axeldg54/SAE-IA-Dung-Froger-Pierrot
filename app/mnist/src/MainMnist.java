import java.io.IOException;

public class MainMnist {
    public static void main(String[] args) throws IOException {
        int nbImages = 1000;

        // Chargement des images et des etiquettes à partir des fichiers
        Image imagesEntrainement = new Image(nbImages);
        imagesEntrainement.load("./data/number/train-images.idx3-ubyte");
        Label labelsEntrainement = new Label();
        labelsEntrainement.load("./data/number/train-labels.idx1-ubyte");
        Image imagesTest = new Image(nbImages);
        imagesTest.load("./data/number//t10k-images.idx3-ubyte");
        Label labelsTest = new Label();
        labelsTest.load("./data/number/t10k-labels.idx1-ubyte");

        // Création de l'objet Donnees pour stocker les imagesEntrainement et les etiquettes
        Donnees donneesTest = new Donnees(imagesTest.imagettes);
        donneesTest.attribuerEtiquettes(labelsTest.etiquettes);

        Donnees donneesEntrainement = new Donnees(imagesEntrainement.imagettes);
        donneesEntrainement.attribuerEtiquettes(labelsEntrainement.etiquettes);


        // Test de l'algorithme de comparaison
        PlusProche algo = new PlusProche(donneesEntrainement);
        //System.out.println("Doit renvoyer 5 => " + algo.compare(imagesTest.imagettes.get(0)));

        Statistiques statistiques = new Statistiques(algo, donneesTest);
        System.out.println(statistiques.tauxReussiteKnn(100));
    }
}
