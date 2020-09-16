
package fdt.fuzzy.integrals;

import fdt.fuzzy.measures.FuzzyMeasure;

/**
 *
 * @author Najlaa Maaroof
 */
public class SugenoIntegral extends FuzzyIntegralBase {

    private static final long serialVersionUID = 1093651719134261601L;

    /**
     *
     * @param fuzzyMeasure
     */
    public SugenoIntegral(FuzzyMeasure fuzzyMeasure) {
        super(fuzzyMeasure);
    }

    /**
     *
     * @param fuzzyMeasure
     * @param fuzzyValue
     */
    public SugenoIntegral(FuzzyMeasure fuzzyMeasure, FuzzyValue fuzzyValue) {
        super(fuzzyMeasure, fuzzyValue);
    }

    @Override
    protected double evaluate(double x, double y, double m, double previousIntegral) {
        double vi = Math.min(x, m);
        return Math.max(vi, previousIntegral);
    }

}
