
package fdt.core;

import fdt.utils.Tuple;
import java.util.HashMap;

/**
 *
 * The decision / leaf node. It is used to represent the leaf node 
 * and contains text (name of the class) 
 * and support (the classification support for the class) properties. 
 * 
 * @author Najlaa Maaroof
 */

public class DecisionNode extends Node {

    private static final long serialVersionUID = 5241099508522414606L;

    private double support;

    /**
     *
     */
    public DecisionNode() {
    }

    /**
     *
     * @param text
     * @param support
     */
    public DecisionNode(String text, double support) {
        super(text);
        this.support = support;
    }

    /**
     *
     * @return
     */
    public double getSupport() {
        return support;
    }

    /**
     *
     * @param support
     */
    public void setSupport(double support) {
        this.support = support;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isLeaf() {
        return true;
    }

}
