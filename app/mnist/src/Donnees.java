import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Donnees {

    public ArrayList<Imagette> imagettes;

    public Donnees(ArrayList<Imagette> imagettes) {
        this.imagettes = imagettes;
    }

    public void attribuerEtiquettes(ArrayList<Integer> etiquettes) {
        for (int i = 0; i < imagettes.size(); i++) {
            imagettes.get(i).etiquette = etiquettes.get(i);
        }
    }
    
    public Donnees shuffle() {
        Collections.shuffle(this.imagettes);
        return this;
    }

}
