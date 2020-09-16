
package fdt.infer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A generic Inference engine that can be used with fuzzy decision trees or fuzzy random forests.
 * 
 * @author Mohammed Jabreel
 */
public class InferenceEngine implements Serializable {

    private static final long serialVersionUID = -7605476541321735848L;

    /**
     * The list of inference models that will be used in the inference phase. 
     * This can be a single model obtained from a FDT or the list of estimators obtained from a Fuzzy random forest.
     */
    protected List<InferenceModelSpec> models = new ArrayList<>();

    /**
     * The voting strategy. It is used to aggregate the decisions of the rules in a single FDT 
     * and get the final results based on the voting on the final results of each individual FDT.
     */
    private VotingStrategy votingStrategy;

    /**
     *
     * @param votingStrategy
     */
    public InferenceEngine(VotingStrategy votingStrategy) {
        this.votingStrategy = votingStrategy;
    }

    /**
     *
     * @param model
     */
    public void addModel(InferenceModelSpec model) {
        models.add(model);
    }

    /** 
     * Given an input specification, get the final prediction.
     * @param inputSpec a dictionary represents the input specification. 
     *       The key is a string of form Attribute.Term, e.g., Sex.Man, 
     *       and the value is the membership value of the term of that attribute.
     * @return an instance of PredictionResult
     */
    public PredictionResult predict(HashMap<String, Double> inputSpec) {

        // First, we get the list of predictions from each model in the list of models we have.
        
        List<PredictionResult> predictionResults = models
                .stream()
                .map(m -> m.predict(inputSpec, votingStrategy))
                .collect(Collectors.toList());

        // Perform the voting whenever it is needed.
        
        if (predictionResults.size() == 1) {
            return predictionResults.get(0);
        } else {
            return votingStrategy.ensambleVote(predictionResults);
        }
    }

    /**
     *
     * @return
     */
    public VotingStrategy getVotingStrategy() {
        return votingStrategy;
    }

    /**
     *
     * @return
     */
    public List<InferenceModelSpec> getModels() {
        return models;
    }
    
    /**
     *
     * @param votingStrategy
     */
    public void setVotingStrategy(VotingStrategy votingStrategy) {
        this.votingStrategy = votingStrategy;
    }
    
    

}
