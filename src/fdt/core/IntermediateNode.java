
package fdt.core;

import fdt.utils.Nullable;
import java.util.HashMap;


/**
 * An intermediate node. It represents an attribute.
 * This class is used in the inference phase by removing some information 
 * used only in the training phase. @see TrainableIntermediateNode
 * 
 * @author Mohammed Jabreel
 */
public class IntermediateNode extends IntermediateNodeBase {

    private static final long serialVersionUID = -1668837612685717472L;
    
    
    private final HashMap<String, Double> classesLambdas = new HashMap<>();
    
    private Nullable<Double> lambda = Nullable.empty();
    
    /**
     *
     * @return
     */
    public double getLambda() {
        return lambda.orElse(0.0);
    }

    /**
     *
     * @param lambda
     */
    public void setLambda(double lambda) {
        this.lambda = Nullable.of(lambda);
}    
    
    /**
     *
     * @param className
     * @return
     */
    public boolean hasLambda(String className) {
        return this.classesLambdas.containsKey(className);
    }
    
    /**
     *
     * @return
     */
    public boolean hasLambda() {
        return lambda.isPresent();
    }
    
    /**
     *
     * @param className
     * @return
     */
    public double getLambda(String className) {
        return this.classesLambdas.get(className);
    }
    
    /**
     *
     * @param className
     * @param lambda
     */
    public void setLambda(String className, double lambda) {
        this.classesLambdas.put(className, lambda);
    }    
    
}
