import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Image {

    private int nbImages;
    public ArrayList<Imagette> imagettes;

    public Image(int nbImages) {
        imagettes = new ArrayList<>();
        this.nbImages = nbImages;
    }

    public void load(String file) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        int type = input.readInt();
        int nbImages = input.readInt();
        if (this.nbImages < nbImages) {
            nbImages = this.nbImages;
        }
        int nbLignes = input.readInt();
        int nbColonne = input.readInt();
        int[][] tab;
        try (ProgressBar pb = new ProgressBar("Load Images", nbImages)) {
            for (int i = 0; i < nbImages; i++) {
                tab = new int[nbLignes][nbColonne];
                for (int j = 0; j < nbLignes; j++) {
                    for (int k = 0; k < nbColonne; k++) {
                        int nivGris = input.readUnsignedByte();
                        tab[k][j] = nivGris;
                    }
                }
                Imagette imagette = new Imagette(type, nbImages, nbLignes, nbColonne, tab);
                imagettes.add(imagette);
                pb.step();
            }
        }
    }

    public void saveImage(Imagette imagette, int nb) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(imagette.nbLignes, imagette.nbColonne, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < imagette.tab.length; i ++) {
            for (int j = 0; j < imagette.tab[0].length; j ++) {
                bufferedImage.setRGB(i,j, grayToRGB(imagette.tab[i][j]));
            }
        }
        ImageIO.write(bufferedImage, "png", new File("./app/mnist/img/img"+nb+".png"));
    }

    public static int grayToRGB(int value){
        return (value<<16) | (value<<8) | value;
    }

    public static double[] imagetteToInput(Imagette imagette) {
        double[] inputs = new double[imagette.tab.length * imagette.tab[0].length];
        for (int i = 0; i < imagette.tab.length; i++) {
            for (int j = 0; j < imagette.tab[0].length; j++) {
                inputs[i * imagette.tab[0].length + j] = imagette.tab[i][j];
                inputs[i * imagette.tab[0].length + j] /= 255;
            }
        }
        return inputs;
    }

    public static void main(String[] args) throws IOException {
        Image model = new Image(1000);
        model.load("./data/number/train-images.idx3-ubyte");
        model.saveImage(model.imagettes.get(0), 0);
        model.saveImage(model.imagettes.get(model.imagettes.size()-1), model.imagettes.size());
        System.out.println(model.imagettes.get(0).etiquette + " -> " + model.imagettes.get(model.imagettes.size()-1).etiquette);
    }
}
