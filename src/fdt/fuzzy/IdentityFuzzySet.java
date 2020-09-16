
package fdt.fuzzy;

/**
 *
 * @author Najlaa Maaroof
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
