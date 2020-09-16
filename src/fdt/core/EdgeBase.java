
package fdt.core;

import java.io.Serializable;

/**
 * An abstract class represents an edge in the tree context and 
 * a languistic term in the fuzzy logic context, e.g, Old term of the Age attribute.  
 * 
 * @author Mohammed Jabreel
 */
public abstract class EdgeBase implements TreeItem {

    private static final long serialVersionUID = -3224294597802896360L;
   
    /**
     * The name of the term.
     */
    protected String text;
    
    /**
     * The parent node. It represents the attribute in which the term belongs to, e.g. Age.
     */
    protected Node parent;
    
    /**
     * The next attribute that is going to be connected to the parent through this edge/term.
     */
    protected Node child;
    
    /**
     * The ambiguity.
     */
    protected double ambiguity;
    
    /**
     *
     */
    public EdgeBase() {
    }

    /**
     *
     * @param text
     */
    public EdgeBase(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public Node getNode() {
        return parent;
    }

    /**
     *
     * @param node
     */
    public void setNode(Node node) {
        this.parent = node;
    }

    /**
     *
     * @return
     */
    public Node getChild() {
        return child;
    }

    /**
     *
     * @return
     */
    public Node getParent() {
        return parent;
    }
    
    /**
     *
     * @param parent
     * @param child
     */
    public void connect(IntermediateNodeBase parent, Node child) {
        parent.connect(child, this);
        this.child = child;
        this.parent = parent;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("--[%s]-->", text);
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
    
    
}

