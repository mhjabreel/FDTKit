
package fdt.fuzzy.integrals;

import fdt.core.DecisionSet;
import java.io.Serializable;

/**
 *
 * The interface of the fuzzy integrals.
 * 
 * @see FuzzyIntegralBase
 * 
 * @author Mohammed Jabreel
 */
public interface FuzzyIntegral extends Serializable {
    
    /**
     * Given a decision set, get the aggregated value of the values of 
     * decision nodes (based on @FuzzyValue and @FuzzyMeasure methods).
     * 
     * @param set
     * @return 
     */
    double evaluate(DecisionSet set);
}
