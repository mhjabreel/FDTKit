
package fdt.fuzzy.integrals;

import fdt.fuzzy.measures.FuzzyMeasure;
import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.utils.Sorter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * An abstract class for the fuzzy integral methods. 
 * Each fuzzy integral needs a fuzzy measure method and a fuzzy value method.
 * 
 * @author Mohammed Jabreel
 */
public abstract class FuzzyIntegralBase implements FuzzyIntegral {

    private static final long serialVersionUID = 3931734251442980554L;

    /**
     * The fuzzy measure method.
     */
    protected FuzzyMeasure fuzzyMeasure;
    
    /**
     * The fuzzy value method. The default is @DefaultFuzzyValue.
     */
    protected FuzzyValue fuzzyValue;

    /**
     * Constructor to set the fuzzy measure that will be used in this integral.
     * @param fuzzyMeasure 
     */
    public FuzzyIntegralBase(FuzzyMeasure fuzzyMeasure) {
        this(fuzzyMeasure, new DefaultFuzzyValue());
    }

    /**
     * Constructor to set the fuzzy measure and the fuzzy value that will be used in this integral.
     * @param fuzzyMeasure
     * @param fuzzyValue 
     */
    public FuzzyIntegralBase(FuzzyMeasure fuzzyMeasure, FuzzyValue fuzzyValue) {
        this.fuzzyMeasure = fuzzyMeasure;
        this.fuzzyValue = fuzzyValue;
    }

    /**
     * Implementation of the abstract evaluate method.
     * @param set
     * @return 
     */
    @Override
    public double evaluate(DecisionSet set) {

        List<DecisionNode> filteredNodes = new ArrayList<>();
        List<Double> filteredValues = new ArrayList<>();
   
        set.getBag().clear();
        
        // First, we obtain the fuzzy values from all the decision nodes based on the fuzzy value method.
        
        double[] fuzzyValues = set.getDecisionNodes().
                stream().
                mapToDouble(n -> fuzzyValue.evaluate(n)).
                toArray();

        
        // We sort the nodes based on the fuzzy values.
        
        List<DecisionNode> sortedNodes = new Sorter<DecisionNode>().sortBy(set.getDecisionNodes(), fuzzyValues);


        // We sort the fuzzy values.
        double[] sortedFuzzyValues = Arrays.copyOf(fuzzyValues, fuzzyValues.length);
        Arrays.sort(sortedFuzzyValues);

        
        double previousF = 0;
        double integralRes = 0;

        for (int i = 0; i < sortedFuzzyValues.length; i++) {
            if (sortedFuzzyValues[i] > 0) {
                filteredValues.add(sortedFuzzyValues[i]);
                filteredNodes.add(sortedNodes.get(i));
                set.addToBag(sortedNodes.get(i));
            }
        }

        fuzzyValues = filteredValues.stream().mapToDouble(v -> v).toArray();
        int n = fuzzyValues.length;

        for (int i = 0; i < n; i++) {

            List<DecisionNode> subset = new ArrayList<>();
            for (int j = i; j < n; j++) {
                subset.add(filteredNodes.get(j));
            }

            // Get the weight of the subset based on the fuzzy measure method.
            double m = fuzzyMeasure.evaluate(subset, set);
            double v = fuzzyValues[i];
            
            // Get the aggrigated value based on the aggrigation method.
            integralRes = evaluate(v, previousF, m, integralRes);
            previousF = v;

        }

        return integralRes;

    }

    /**
     * An abstracted method to get the aggrigated value. Each fuzzy integral must implement this method.
     * see ChoquetIntegral, SugenoIntegral
     * 
     * @param x the current fuzzy value, v(i)
     * @param y the previous fuzzy value, v(i-1).
     * @param m the fuzzy measure value m(i).
     * @param previousIntegral the previous aggrigated value.
     * @return 
     */
    protected abstract double evaluate(double x, double y, double m, double previousIntegral);

}
