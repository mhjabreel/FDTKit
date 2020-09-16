
package fdt.fuzzy;

/**
 *
 * @author Mohammed Jabreel
 */
public class IdentityFuzzySet implements FuzzySet {

    /**
     *
     * @param x
     * @return
     */
    @Override
    public double getMembership(double x) {
        return x;
    }
    
}
