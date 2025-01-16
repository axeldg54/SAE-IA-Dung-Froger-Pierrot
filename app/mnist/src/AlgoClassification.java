public abstract class AlgoClassification {
    public Donnees donnees;
    public AlgoClassification(Donnees donnees) {
        this.donnees = donnees;
    }

    abstract public int compare(Imagette imagette);
}
