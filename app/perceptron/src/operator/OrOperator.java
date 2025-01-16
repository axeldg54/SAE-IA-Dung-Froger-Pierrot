package operator;

public class OrOperator implements Operator {
	public static double[][] INPUTS = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
	public static double[][] OUTPUTS = new double[][]{{0}, {1}, {1}, {1}};
	
	public String toString() {
		return "OR";
	}
	
	public double[][] getInputs() {return INPUTS;}
	
	public double[][] getOutputs() {return OUTPUTS;}
}
