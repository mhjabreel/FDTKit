
package fdt.infer;

import fdt.core.DecisionSet;
import fdt.fuzzy.integrals.FuzzyIntegral;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * Fuzzy Integral-based voting strategy. 
 * We get the prediction from the set of the rules in a FDT in this voting 
 * strategy by aggregating these decisions using a fuzzy integral method.
 * 
 * @author Mohammed Jabreel
 */
public class IntegralBasedVotingStrategy extends DefaultVotingStrategy {

    private static final long serialVersionUID = -8771986175920175509L;

    /**
     *
     */
    protected FuzzyIntegral fuzzyIntegral;

    /**
     *
     */
    protected double delta = 0;

    /**
     *
     * @param fuzzyIntegral
     */
    public IntegralBasedVotingStrategy(FuzzyIntegral fuzzyIntegral) {
        this.fuzzyIntegral = fuzzyIntegral;
    }

    /**
     *
     */
    public IntegralBasedVotingStrategy() {
    }

    /**
     *
     * @param fuzzyIntegral
     * @param aggregationType
     */
    public IntegralBasedVotingStrategy(FuzzyIntegral fuzzyIntegral,
            AggregationType aggregationType) {
        super(aggregationType);
        this.fuzzyIntegral = fuzzyIntegral;
    }

    /**
     *
     * @param decisionSets
     * @return
     */
    @Override
    public PredictionResult vote(List<DecisionSet> decisionSets) {
        
        // First, we aggregate the decisons twards each class.
        // The result here is a dictionary in which the key is the class name 
        // and the value is decision support twards that class.
        
        HashMap<String, Double> aggregations = new HashMap<>();
        decisionSets.forEach((ds) -> {
            double v = fuzzyIntegral.evaluate(ds);
            aggregations.put(ds.getName(), v);
        });
        
        
        // We normailze the obtained values, by dividing each value by the summation of all values.
        
        double s = aggregations.values().stream().mapToDouble(Double::doubleValue).sum();
        aggregations.keySet().forEach(k -> {
            double v = aggregations.get(k) / s;
            aggregations.put(k, v);
        });   
        
        
        PredictionResult result = new PredictionResult();
        

        // Sort the decicions from the best to the worst.
        
        final Map<String, Double> sortedAggregations = aggregations.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        

        // if we have more than 1 class
        if (aggregations.size() > 1) {
            
            // we get the two best 
            
            Map.Entry<String, Double> e1 = sortedAggregations.entrySet().stream().findFirst().get();
            Map.Entry<String, Double> e2 = sortedAggregations.entrySet().stream().skip(1).findFirst().get();

            // If the difference is not large enough, make the classification as UNK.
            
            if (delta > 0 && (e1.getValue() - e2.getValue() < delta)) {
                result.setClassName("UnKnown");
                result.setSupport(0);
            } else {
                // make the classification of the best one.
                result.setClassName(e1.getKey());
                result.setSupport(e1.getValue() );
            }
            
        } else if (aggregations.size() == 1) {
            
            Map.Entry<String, Double> e1 = sortedAggregations.entrySet().stream().findFirst().get();
            result.setClassName(e1.getKey());
            result.setSupport(e1.getValue());
            
        } else {
            result.setClassName("UnKnown");
        }

        return result;        
    }

    /**
     *
     * @return
     */
    public double getDelta() {
        return delta;
    }

    /**
     *
     * @param delta
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }

    /**
     *
     * @return
     */
    public FuzzyIntegral getFuzzyIntegral() {
        return fuzzyIntegral;
    }

    /**
     *
     * @param fuzzyIntegral
     */
    public void setFuzzyIntegral(FuzzyIntegral fuzzyIntegral) {
        this.fuzzyIntegral = fuzzyIntegral;
    }

}
