
package fdt.core;

import java.io.Serializable;

/**
 *
 * @author Najlaa Maaroof
 */

public abstract class Node implements TreeItem {

    private static final long serialVersionUID = 7147528097960598493L;

    /**
     *
     */
    protected String text;
    private EdgeBase edge = null;

    /**
     *
     */
    protected int depth;

    /**
     *
     */
    public Node() {
    }

    /**
     *
     * @param text
     */
    public Node(String text) {
        this.text = text;
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
    @Override
    public String getText() {
        return text;
    }

    /**
     *
     * @return
     */
    public EdgeBase getEdge() {
        return edge;
    }

    /**
     *
     * @param edge
     */
    public void setEdge(EdgeBase edge) {
        this.edge = edge;
    }

    /**
     *
     * @return
     */
    public boolean isRoot() {
        return edge == null;
    }

    /**
     *
     * @return
     */
    public Node getParent() {
        if (!isRoot()) {
            return edge.getParent();
        }

        return null;
    }

    /**
     *
     * @return
     */
    public int getDepth() {
        return depth;
    }

    /**
     *
     * @param depth
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     *
     * @return
     */
    public abstract boolean isLeaf();

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return text;
    }

}
