import java.io.IOException;

public class MainMnist {
    public static void main(String[] args) throws IOException {
        // Chargement des images et des etiquettes à partir des fichiers
        Image imagesEntrainement = new Image();
        imagesEntrainement.load("./app/mnist/data/train-images.idx3-ubyte");
        Label labelsEntrainement = new Label();
        labelsEntrainement.load("./app/mnist/data/train-labels.idx1-ubyte");
        Image imagesTest = new Image();
        imagesTest.load("./app/mnist/data/t10k-images.idx3-ubyte");
        Label labelsTest = new Label();
        labelsTest.load("./app/mnist/data/t10k-labels.idx1-ubyte");

        // Création de l'objet Donnees pour stocker les imagesEntrainement et les etiquettes
        Donnees donneesTest = new Donnees(imagesTest.imagettes);
        donneesTest.attribuerEtiquettes(labelsTest.etiquettes);

        Donnees donneesEntrainement = new Donnees(imagesEntrainement.imagettes);
        donneesEntrainement.attribuerEtiquettes(labelsEntrainement.etiquettes);


        // Test de l'algorithme de comparaison
        PlusProche algo = new PlusProche(donneesEntrainement);
        //System.out.println("Doit renvoyer 5 => " + algo.compare(imagesTest.imagettes.get(0)));

        Statistiques statistiques = new Statistiques(algo, donneesTest);
        System.out.println(statistiques.tauxReussite(1000));
    }
}
