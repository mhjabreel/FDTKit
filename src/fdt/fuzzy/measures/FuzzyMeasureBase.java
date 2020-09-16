
package fdt.fuzzy.measures;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.IntermediateNode;
import java.util.List;

/**
 *
 * A base class for the fuzzy measure methods.
 * 
 * @author Najlaa Maaroof
 */
public abstract class FuzzyMeasureBase implements FuzzyMeasure {

    private static final long serialVersionUID = 692379574106832903L;

    
    @Override
    public double evaluate(List<DecisionNode> subset, DecisionSet universeSet) {
        double[] singletones = subset.stream().mapToDouble(n -> evaluate(n)).toArray();
        return evaluate(singletones, universeSet);
    }
    
    /**
     * The fuzzy measure of a single item/DecicionNode is assumed to be its classification support.
     * @param item
     * @return 
     */

    protected double evaluate(DecisionNode item) {
        return item.getSupport();
    }

    
    /**
     * Get the fuzzy measure value given the fuzzy measure values of the singletones and the universe set, i.e., the decision set.
     * @param singletones
     * @param universeSet
     * @return 
     */
    protected abstract double evaluate(double[] singletones, DecisionSet universeSet);
}
