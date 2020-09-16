
package fdt.fuzzy.integrals;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import java.io.Serializable;

/**
 *
 * An interface to compute the fuzzy value from a decision node.
 * @see DefaultFuzzyValue
 * @author Mohammed Jabreel
 */
public interface FuzzyValue extends Serializable {

    /**
     * To get the fuzzy value from the decision node without any information about the decision set in which that node is a member of.
     * @param item
     * @return 
     */    
    double evaluate(DecisionNode item);
    
    
    /**
     * To get the fuzzy value from the decision node given the decision set in which that node is a member of.
     * @param item
     * @param universeSet
     * @return 
     */    
    double evaluate(DecisionNode item, DecisionSet universeSet);
}
