import me.tongfei.progressbar.ProgressBar;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Label {

    public ArrayList<Integer> etiquettes;

    public Label() {
        etiquettes = new ArrayList<>();
    }

    public void load(String file) throws IOException {
        DataInputStream input = new DataInputStream(new FileInputStream(file));
        int type = input.readInt();
        int nbLabels = input.readInt();
        try (ProgressBar pb = new ProgressBar("Load Labels", nbLabels)) {
            for (int i = 0; i < nbLabels; i++) {
                int label = input.readUnsignedByte();
                etiquettes.add(label);
                pb.step();
            }
        }
    }
}
