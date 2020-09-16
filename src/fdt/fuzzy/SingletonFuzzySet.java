
package fdt.fuzzy;

/**
 *
 * @author Mohammed Jabreel
 */
public class SingletonFuzzySet implements FuzzySet {

    // The unique point where the membership value is 1.

    /**
     *
     */
    protected double support;

    /**
     *
     * @param support
     */
    public SingletonFuzzySet(double support) {
        this.support = support;
    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public double getMembership(double x) {
        return (this.support == x) ? 1 : 0;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Singletone Function";
    }

    /**
     *
     * @return
     */
    public double getSupport() {
        return support;
    }

}
