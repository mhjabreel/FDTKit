
package fdt.infer;

import java.text.DecimalFormat;

/**
 * To represent the prediction result.
 * 
 * @author Najlaa Maaroof
 */
public class PredictionResult {

    /**
     * The class name.
     */
    private String className;
    
    /**
     * The support.
     */
    private double support;

    /**
     *
     */
    public PredictionResult() {
    }

    /**
     *
     * @param className
     * @param support
     */
    public PredictionResult(String className, double support) {
        this.className = className;
        this.support = support;
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
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
    public String toString() {
        return String.format("%s (%s)", className, new DecimalFormat("0.00").format(support));
    }

}
