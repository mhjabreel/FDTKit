
package fdt.infer;

import fdt.core.DecisionSet;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Mohammed Jabreel
 */
public interface VotingStrategy extends Serializable {
    
    /**
     *
     * @param decisionSets
     * @return
     */
    PredictionResult vote(List<DecisionSet> decisionSets);

    /**
     *
     * @param predictionResults
     * @return
     */
    PredictionResult ensambleVote(List<PredictionResult> predictionResults);
}
