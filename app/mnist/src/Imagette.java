import java.io.*;

public class Imagette {
    public int[][] tab;
    public int type;
    public int nbImages;
    public int nbLignes;
    public int nbColonne;
    public int etiquette;

    public Imagette(int type, int nbImages, int nbLignes, int nbColonne, int[][] tab) {
        this.type = type;
        this.nbImages = nbImages;
        this.nbLignes = nbLignes;
        this.nbColonne = nbColonne;
        this.tab = tab;
    }

    public String toString() {
        return "Type: " + type + "\n" +
                "Nombre d'images: " + nbImages + "\n" +
                "Nombre de lignes: " + nbLignes + "\n" +
                "Nombre de colonnes: " + nbColonne + "\n" +
                "Etiquette: " + etiquette;
    }
}
