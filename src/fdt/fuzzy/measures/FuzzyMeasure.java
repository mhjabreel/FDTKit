
package fdt.fuzzy.measures;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import java.io.Serializable;
import java.util.List;

/**
 *
 * An interface for the fuzzy measures.
 * 
 * @author Mohammed Jabreel
 */
public interface FuzzyMeasure extends Serializable {
    
    /**
     * To calculate the fuzzy measure of a decision nodes S_i that is a subset of S.
     * @param subset S_i
     * @param universeSet S
     * @return the fuzzy measure value.
     */
    double evaluate(List<DecisionNode> subset, DecisionSet universeSet);
    
}
