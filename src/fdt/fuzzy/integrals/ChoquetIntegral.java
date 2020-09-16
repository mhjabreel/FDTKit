
package fdt.fuzzy.integrals;

import fdt.fuzzy.measures.FuzzyMeasure;

/**
 *
 * @author Najlaa Maaroof
 */
public class ChoquetIntegral extends FuzzyIntegralBase {

    private static final long serialVersionUID = -6984666774235620648L;

    /**
     *
     * @param fuzzyMeasure
     */
    public ChoquetIntegral(FuzzyMeasure fuzzyMeasure) {
        super(fuzzyMeasure);
    }

    /**
     *
     * @param fuzzyMeasure
     * @param fuzzyValue
     */
    public ChoquetIntegral(FuzzyMeasure fuzzyMeasure, FuzzyValue fuzzyValue) {
        super(fuzzyMeasure, fuzzyValue);
    }

    @Override
    protected double evaluate(double x, double y, double m, double previousIntegral) {
        return previousIntegral + ((x - y) * m);
    }

}
