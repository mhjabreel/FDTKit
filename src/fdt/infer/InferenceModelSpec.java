
package fdt.infer;

import fdt.core.DecisionSet;
import fdt.core.Edge;
import fdt.core.EdgeBase;
import fdt.core.IntermediateNode;
import fdt.core.Node;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * An inference model specification has the following properties.
 * 
 * name: the name of the estimator.
 * tree: the root of the tree.
 * decisionSets: the list of the decision sets in the tree.
 * 
 * @author Mohammed Jabreel
 */
public class InferenceModelSpec implements Serializable {

    private static final long serialVersionUID = -7297717108654827038L;

    /**
     *
     */
    protected String name;

    /**
     *
     */
    protected IntermediateNode tree;

    /**
     *
     */
    protected final List<DecisionSet> decisionSets = new ArrayList<>();

    /**
     *
     */
    protected int numberOfRules = 0;

    /**
     *
     */
    public InferenceModelSpec() {
    }

    /**
     *
     * @param name
     * @param tree
     */
    public InferenceModelSpec(String name, IntermediateNode tree) {
        this.name = name;
        this.tree = tree;
    }

    /**
     *
     * @param decisionSet
     */
    public void addDecisionSet(DecisionSet decisionSet) {
        this.decisionSets.add(decisionSet);
        numberOfRules += decisionSet.getDecisionNodes().size();
    }
    
    /**
     *
     * @return
     */
    public List<DecisionSet> getDecisionSets() {
        return decisionSets;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public IntermediateNode getTree() {
        return tree;
    }

    /**
     *
     * @param tree
     */
    public void setTree(IntermediateNode tree) {
        this.tree = tree;
    }
    
    /**
     * To get the prediction given the input and the voting strategy.
     * @param inputSpec a dictionary represents the input specification. 
     *       The key is a string of form Attribute.Term, e.g., Sex.Man, 
     *       and the value is the membership value of the term of that attribute.
     * @param votingStrategy The voting strategy that will be used to aggregate the decisions of the rules in the model.
     *       It could be the default strategy or a fuzzy integral based one.
     * @return an instance of PredictionResult
     */
    public PredictionResult predict(HashMap<String, Double> inputSpec, VotingStrategy votingStrategy) {
        
        Stack<Node> predictionStack = new Stack<>();

        
        // First, we traverse through the tree until we rich each decision node (DFS technique).
        
        predictionStack.push(tree);
        
        while (!predictionStack.isEmpty()) {
            IntermediateNode node = (IntermediateNode) predictionStack.pop();
            
            for (EdgeBase e : node.getEdges()) {
                Edge e2 = (Edge) e;
                String k = String.format("%s.%s", node.getText(), e2.getText());
                double v = inputSpec.getOrDefault(k, 1.0);
                e2.setValue(v);
                if (!node.isRoot()) {
                    Edge e1 = (Edge) node.getEdge();
                    v = Math.min(v, e1.getAccValue());
                }
                e2.setAccValue(v);
                if(!e2.getChild().isLeaf()) {
                    predictionStack.push(e2.getChild());
                }
                
            }            
        }  
        
        //Now, the decision sets hold the required information to be aggregated or to perform the voting and get the final prediction result.
        
        return votingStrategy.vote(decisionSets);
    }

    /**
     *
     * @return
     */
    public int getNumberOfRules() {
        return numberOfRules;
    }

    
}
