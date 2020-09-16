
package fdt.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Najlaa Maaroof
 */
public abstract class IntermediateNodeBase extends Node {

    private static final long serialVersionUID = 3265697474911865827L;

    /**
     *
     */
    protected double ambiguity;

    /**
     *
     */
    protected List<EdgeBase> edges = new ArrayList<>();

    /**
     *
     */
    public IntermediateNodeBase() {
    }

    /**
     *
     * @param text
     */
    public IntermediateNodeBase(String text) {
        super(text);
    }

    /**
     *
     * @return
     */
    public double getAmbiguity() {
        return ambiguity;
    }

    /**
     *
     * @param ambiguity
     */
    public void setAmbiguity(double ambiguity) {
        this.ambiguity = ambiguity;
    }

    /**
     *
     * @param child
     * @param edge
     */
    public void connect(Node child, EdgeBase edge) {
        child.setEdge(edge);
        this.edges.add(edge);
        child.depth = this.depth + 1;
    }

    /**
     *
     * @return
     */
    public List<EdgeBase> getEdges() {
        return edges;
    }
    
    /**
     *
     * @return
     */
    @Override
    public boolean isLeaf() {
        return false;
    }

}
