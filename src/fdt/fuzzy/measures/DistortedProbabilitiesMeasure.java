
package fdt.fuzzy.measures;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;

/**
 *
 * To compute the fuzzy measure value using the Distorted Probability method.
 * 
 * 
 * @author Mohammed Jabreel
 */
public class DistortedProbabilitiesMeasure extends FuzzyMeasureBase {

    private static final long serialVersionUID = 5248213971134859082L;

    /**
     *
     */
    protected double tau;

    /**
     *
     */
    public DistortedProbabilitiesMeasure() {
        this(1);
    }

    /**
     *
     * @param tau
     */
    public DistortedProbabilitiesMeasure(double tau) {
        this.tau = tau;
    }

    @Override
    protected double evaluate(double[] singletones, DecisionSet universeSet) {
        double denominator = universeSet.getDecisionNodes().stream().mapToDouble(n -> evaluate(n)).sum();
//        double denominator = universeSet.getBag().stream().mapToDouble(n -> evaluate((DecisionNode) n)).sum();
        double s = 0;
        for (int i = 0; i < singletones.length; i++) {
            s += singletones[i];
        }
        return Math.pow(s / denominator, tau);
    }

    /**
     *
     * @return
     */
    public double getTau() {
        return tau;
    }

    /**
     *
     * @param tau
     */
    public void setTau(double tau) {
        this.tau = tau;
    }

}
