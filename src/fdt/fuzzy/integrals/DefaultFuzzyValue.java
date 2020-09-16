
package fdt.fuzzy.integrals;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.Edge;

/**
 *
 * To compute the fuzzy value from the decision nodes. 
 * @author Mohammed Jabreel
 */
public class DefaultFuzzyValue implements FuzzyValue {

    private static final long serialVersionUID = -5520176508323712873L;

    /**
     * The default fuzzy value is obtained by multiplying the classification 
     * support by the fuzzy membership value.
     * 
     * @param item
     * @return the fuzzy value.
     */
    @Override
    public double evaluate(DecisionNode item) {
        return item.getSupport() * ((Edge) item.getEdge()).getAccValue();
    }

    /**
     * To get the fuzzy value from the decision node given the decision set in which that node is a member of.
     * @param item
     * @param universeSet
     * @return 
     */
    @Override
    public double evaluate(DecisionNode item, DecisionSet universeSet) {
        return item.getSupport() * ((Edge) item.getEdge()).getAccValue();
    }

}
