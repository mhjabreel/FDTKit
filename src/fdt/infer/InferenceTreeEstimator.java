
package fdt.infer;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.Edge;
import fdt.core.EdgeBase;
import fdt.core.IntermediateNode;
import fdt.core.Node;
import fdt.core.TrainableIntermediateNode;
import java.util.HashMap;
import java.util.Map;

/**
 * This estimator is used to convert the decision tree from the training phase to the inference phase.
 * It is used to eliminate the unnecessary information that were used in the training phase.
 * It is also used to compute new properties that may be required in the inference phase depending on the type of inference estimator.
 * 
 * 
 * @author Najlaa Maaroof
 */
public class InferenceTreeEstimator {

    private HashMap<String, DecisionSet> decisionSets = new HashMap<>();

    /**
     * Given the root of a trainable decision tree, we use the fit method to get the inference version.
     * @param root
     * @return An instance of InferenceModelSpec that represents the specification of the model will be used for the prediction.
     */
    public InferenceModelSpec fit(TrainableIntermediateNode root) {
        
        decisionSets = new HashMap<>();

        IntermediateNode tree = new IntermediateNode();
        tree.setText(root.getText());
        tree.setAmbiguity(root.getAmbiguity());
        
        this.fit(root, tree);
        
        for (Map.Entry<String, DecisionSet> entry : decisionSets.entrySet()) {
            String key = entry.getKey();
            DecisionSet value = entry.getValue();
            value.setEstimator(tree);

        }

        InferenceModelSpec ies = new InferenceModelSpec();
        ies.setTree(tree);

        for (DecisionSet ds : decisionSets.values()) {
            ies.addDecisionSet(ds);
        }
        decisionSets = null;

        return ies;

    }

    /**
     *
     * @return
     */
    public HashMap<String, DecisionSet> getDecisionSets() {
        return decisionSets;
    }

    /**
     *
     * @param trainableRoot
     * @param root
     */
    protected void fit(TrainableIntermediateNode trainableRoot, IntermediateNode root) {

        for (EdgeBase e : trainableRoot.getEdges()) {

            Edge e2 = new Edge(e.getText());
            e2.setAmbiguity(e.getAmbiguity());

            Node child = e.getChild();

            if (child.isLeaf()) {
                DecisionNode cNode = (DecisionNode) child;

                DecisionNode cNode2 = new DecisionNode(cNode.getText(), cNode.getSupport());

                e2.connect(root, cNode2);

                if (!decisionSets.containsKey(cNode2.getText())) {
                    decisionSets.put(cNode2.getText(), new DecisionSet(cNode2.getText()));
                }

                DecisionSet set = decisionSets.get(cNode2.getText());
                set.getDecisionNodes().add(cNode2);

            } else {
                IntermediateNode newChild = new IntermediateNode();
                newChild.setText(child.getText());
                newChild.setAmbiguity(((TrainableIntermediateNode) child).getAmbiguity());
                e2.connect(root, newChild);
                this.fit((TrainableIntermediateNode) e.getChild(), (IntermediateNode) newChild);
            }

        }
    }
}
