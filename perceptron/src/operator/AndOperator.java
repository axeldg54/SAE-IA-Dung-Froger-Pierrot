package operator;

public class AndOperator implements Operator {
    public static double[][] INPUTS = new double[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    public static double[][] OUTPUTS = new double[][] {{0}, {0}, {0}, {1}};

    public String toString() {
        return "AND";
    }
    
    public double[][] getInputs() {return INPUTS;}
    
    public double[][] getOutputs() {return OUTPUTS;}
}
