package operator;

public class XorOperator implements Operator {
	public static double[][] INPUTS = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
	public static double[][] OUTPUTS = new double[][]{{0}, {1}, {1}, {0}};
	
	public String toString() {
		return "XOR";
	}
	
	public double[][] getInputs() {return INPUTS;}
	
	public double[][] getOutputs() {return OUTPUTS;}
}
