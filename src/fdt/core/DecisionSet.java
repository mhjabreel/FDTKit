
package fdt.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DecisionSet class is used to represent a set of decision nodes have the same class
 * with different supports. It is useful when we want to apply an aggregation technique and get 
 * a value represents the overall support of each class.
 * It has three attributes: 
 * i) name = the set name (it should be equal to the class name, e.g. DR).
 * ii) decisionNodes: the list of the decision nodes that belong to this set.
 * iii) estimator: an intermediate node that represents the root of the estimator in which the list of nodes come from.
 * 
 * @author Najlaa Maaroof
 */
public class DecisionSet implements Serializable {

    private static final long serialVersionUID = -2414492555910250695L;

    private String name;
    private final List<DecisionNode> decisionNodes = new ArrayList<>();
    private IntermediateNode estimator;
    
    private final List<Object> bag = new ArrayList<>();
    
    /**
     *
     */
    public DecisionSet() {
    }

    /**
     *
     * @param name
     */
    public DecisionSet(String name) {
        this.name = name;
    }

    /**
     *
     * @param name
     * @param estimator
     */
    public DecisionSet(String name, IntermediateNode estimator) {
        this.name = name;
        this.estimator = estimator;
    }

    /**
     *
     * @return
     */
    public IntermediateNode getEstimator() {
        return estimator;
    }

    /**
     *
     * @param estimator
     */
    public void setEstimator(IntermediateNode estimator) {
        this.estimator = estimator;
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
    @Override
    public String toString() {
        return name;
    }

    /**
     *
     * @return
     */
    public List<DecisionNode> getDecisionNodes() {
        return decisionNodes;
    }
    
    /**
     *
     * @param val
     */
    public void addToBag(Object val) {
        bag.add(val);
    }

    /**
     *
     * @return
     */
    public List<Object> getBag() {
        return bag;
    }
    
    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        return super.equals(obj); //To change body of generated methods, choose Tools | Templates.
    }

}
