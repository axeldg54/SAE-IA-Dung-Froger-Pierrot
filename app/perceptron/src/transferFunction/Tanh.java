package transferFunction;

public class Tanh implements lib.TransferFunction {
    
    @Override
    public double evaluate(double value) {
        return Math.tanh(value);
    }

    @Override
    public double evaluateDer(double value) {
        return 1 - (value * value);
    }
    
    @Override
    public String toString() {
        return "Tanh";
    }
}
