
package fdt.core;

import fdt.utils.Tuple;
import java.util.HashMap;

/**
 *
 * @author Mohammed Jabreel
 */
public class TrainableEdge extends EdgeBase {

    private static final long serialVersionUID = 1865428303994838795L;

    private double[] evidences;
    private HashMap<String, Double> classSupports = new HashMap<>();

    /**
     *
     */
    public TrainableEdge() {
    }
    
    /**
     *
     * @param edge
     */
    public TrainableEdge(TrainableEdge edge) {
        this.evidences = edge.evidences;
        this.ambiguity = edge.ambiguity;
        this.classSupports = edge.classSupports;
        this.text = edge.text;
    }

    /**
     *
     * @param text
     */
    public TrainableEdge(String text) {
        super(text);
    }

    /**
     *
     * @param className
     * @param support
     */
    public void setClassSupport(String className, double support) {
        classSupports.put(className, support);
    }

    /**
     *
     * @return
     */
    public HashMap<String, Double> getClassSupports() {
        return classSupports;
    }

    /**
     *
     * @return
     */
    public Tuple<String, Double> getBestClass() {
        double best = -1;
        String bestClass = "";
        for (String c : classSupports.keySet()) {
            if (classSupports.get(c) > best) {
                best = classSupports.get(c);
                bestClass = c;
            }
        }
        return new Tuple<>(bestClass, best);
    }

    /**
     *
     * @return
     */
    public double[] getEvidences() {
        return evidences;
    }

    /**
     *
     * @param evidences
     */
    public void setEvidences(double[] evidences) {
        this.evidences = evidences;
    }
    
    
}
