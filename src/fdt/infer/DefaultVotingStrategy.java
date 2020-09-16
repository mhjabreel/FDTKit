
package fdt.infer;

import fdt.core.DecisionSet;
import fdt.core.Edge;
import fdt.utils.RandomFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The default voting strategy method, i.e., the maximum is the winner.
 * 
 * @author Najlaa Maaroof
 */
public class DefaultVotingStrategy implements VotingStrategy {

    private static final long serialVersionUID = -8629068401800286834L;

    /**
     *
     */
    protected int delta2 = 0;

    /**
     *
     * @param decisionSets
     * @return
     */
    @Override
    public PredictionResult vote(List<DecisionSet> decisionSets) {
        PredictionResult result = new PredictionResult();

        double maxSupport = -1;
        String bestClass = "";
        
        for(DecisionSet s: decisionSets) {
            double m = s.getDecisionNodes()
                    .stream()
                    .mapToDouble(d -> d.getSupport() * ((Edge)d.getEdge()).getAccValue())
                    .max()
                    .getAsDouble();
            if(m > maxSupport) {
                maxSupport = m;
                bestClass = s.getName();
            }
        }
        
        result.setClassName(bestClass);
        result.setSupport(maxSupport);
        
        return result;        
    }

    /**
     *
     */
    public enum AggregationType {

        /**
         *
         */
        COUNT_OF_VOTES,

        /**
         *
         */
        MEAN_OF_SUPPORTS,
    }

    private AggregationType aggregationType = AggregationType.COUNT_OF_VOTES;

    /**
     *
     */
    public DefaultVotingStrategy() {
    }

    /**
     *
     * @param aggregationType
     */
    public DefaultVotingStrategy(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    /**
     *
     * @param predictionResults
     * @return
     */
    @Override
    public PredictionResult ensambleVote(List<PredictionResult> predictionResults) {

        PredictionResult result = new PredictionResult();

        HashMap<String, List<Double>> classVotes = new HashMap<>();

        double maxSupport = Double.MIN_VALUE;

        predictionResults.stream()
                .forEachOrdered((voteResult) -> {
                    List<Double> values = classVotes
                            .getOrDefault(voteResult.getClassName(),
                                    new ArrayList<>());
                    values.add(voteResult.getSupport());
                    classVotes.put(voteResult.getClassName(), values);
                });

        final Map<String, List<Double>> sortedVotes = classVotes.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size())
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        Map.Entry<String, List<Double>> e1 = sortedVotes.entrySet().stream().findFirst().get();
        
        if (sortedVotes.size() == 1) {
            result.setClassName(e1.getKey());
            result.setSupport(e1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
        } else {
            Map.Entry<String, List<Double>> e2 = sortedVotes.entrySet().stream().skip(1).findFirst().get();

            if (e1.getKey().equals("UnKnown")) {
                
                result.setClassName(e1.getKey());
                result.setSupport(e1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
            } else {
                if (e2.getKey().equals("UnKnown")) {
                    
                    if (e1.getValue().size() == e2.getValue().size()) {
                        result.setClassName(e2.getKey());
                        result.setSupport(e2.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
                    } else {
                        result.setClassName(e1.getKey());
                        result.setSupport(e1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
                    }
                } else {
//                    if (e1.getValue().size() == e2.getValue().size()) {
//                        
//                        result.setClassName(RandomFactory.nextDouble() > 0.5 ? e1.getKey() : e2.getKey());
//                        result.setSupport(e2.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
//                    } else 
                    
                    if (Math.abs(e1.getValue().size() - e2.getValue().size()) < delta2) {
                        
                        result.setClassName("UnKnown");
                    } else {
                        result.setClassName(e1.getKey());
                        result.setSupport(e1.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0));
                    }
                }
            }
        }

        return result;
    }

    /**
     *
     * @param delta2
     */
    public void setDelta2(int delta2) {
        this.delta2 = delta2;
    }

    /**
     *
     * @return
     */
    public int getDelta2() {
        return delta2;
    }

    /**
     *
     * @return
     */
    public AggregationType getAggregationType() {
        return aggregationType;
    }

    /**
     *
     * @param aggregationType
     */
    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

}
