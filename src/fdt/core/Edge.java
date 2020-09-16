
package fdt.core;

/**
 *
 * @author Mohammed Jabreel
 */
public class Edge extends EdgeBase {

    private static final long serialVersionUID = 7620880090977147378L;

    /**
     *
     */
    protected double value;

    /**
     *
     */
    protected double accValue;
    
    /**
     *
     */
    public Edge() {
    }

    /**
     *
     * @param text
     */
    public Edge(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     */
    public double getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public double getAccValue() {
        return accValue;
    }

    /**
     *
     * @param accValue
     */
    public void setAccValue(double accValue) {
        this.accValue = accValue;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("--[%s(%.3f)]-->", text, value);
    }
    
    
}
