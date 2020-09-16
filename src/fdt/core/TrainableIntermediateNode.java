
package fdt.core;

import fdt.data.Attribute;
import fdt.utils.Tuple;
import java.util.HashMap;

/**
 *
 * @author Najlaa Maaroof
 */
public class TrainableIntermediateNode extends IntermediateNodeBase {

    private static final long serialVersionUID = 8452734258033681941L;
    
    private Attribute attribute;

    /**
     *
     * @return
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     *
     * @param attribute
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }
      
    
}
