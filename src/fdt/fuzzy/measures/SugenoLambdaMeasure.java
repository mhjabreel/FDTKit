
package fdt.fuzzy.measures;

import fdt.core.DecisionSet;
import java.util.Arrays;

/**
 * To compute the fuzzy measure value using the Sugeno Lambda Measure method.
 * @author Mohammed Jabreel
 */
public class SugenoLambdaMeasure extends FuzzyMeasureBase {

    private static final long serialVersionUID = -3475896576086728079L;

    @Override
    protected double evaluate(double[] singletones, DecisionSet universeSet) {
        if (singletones.length == 1) {
            return singletones[0];
        }
        double lambda = universeSet.getEstimator().getLambda(universeSet.getName());

        if (lambda == 0) {
            return Arrays.stream(singletones).sum();
        }

        double v = 1;
        for (double i : singletones) {
            v *= (1 + lambda * i);
        }
        v -= 1;
        v /= lambda;
        return v;
    }

}
