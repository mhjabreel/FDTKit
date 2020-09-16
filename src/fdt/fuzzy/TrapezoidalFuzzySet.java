
package fdt.fuzzy;

import fdt.utils.Tuple;

/**
 *
 * @author Mohammed Jabreel
 * 
 */ 

public class TrapezoidalFuzzySet extends PiecewiseLinearFunction {

    /**
     *
     * @param size
     */
    public TrapezoidalFuzzySet(int size) {
        this.points = new Tuple[size];
    }

    /**
     *
     * @param m1
     * @param m2
     * @param m3
     * @param m4
     * @param max
     * @param min
     */
    public TrapezoidalFuzzySet(double m1, double m2, double m3, double m4, double max, double min) {
        this(4);
        this.points[0] = new Tuple<>(m1, min);
        this.points[1] = new Tuple<>(m2, max);
        this.points[2] = new Tuple<>(m3, max);
        this.points[3] = new Tuple<>(m4, min);
    }

    /**
     *
     * @param m1
     * @param m2
     * @param m3
     * @param m4
     */
    public TrapezoidalFuzzySet(double m1, double m2, double m3, double m4) {
        this(m1, m2, m3, m4, 1.0, 0.0);
    }

    /**
     *
     * @param m1
     * @param m2
     * @param m3
     * @param max
     * @param min
     */
    public TrapezoidalFuzzySet(double m1, double m2, double m3, double max, double min) {
        this(3);
        this.points[0] = new Tuple<>(m1, min);
        this.points[1] = new Tuple<>(m2, max);
        this.points[2] = new Tuple<>(m3, min);
    }

    /**
     *
     * @param m1
     * @param m2
     * @param m3
     */
    public TrapezoidalFuzzySet(double m1, double m2, double m3 ) {
        this(m1, m2, m3, 1.0, 0.0);
    }

    /**
     *
     * @param m1
     * @param m2
     * @param max
     * @param min
     * @param edge
     */
    public TrapezoidalFuzzySet(double m1, double m2, double max, double min, EdgeType edge) {
        this(2);
        if(edge == EdgeType.Left) {
            this.points[0] = new Tuple<>(m1, min );
            this.points[1] = new Tuple<>( m2, max );
        }
        else {
            this.points[0] = new Tuple<>( m1, max );
            this.points[1] = new Tuple<>( m2, min );
        }
    }

    /**
     *
     * @param m1
     * @param m2
     * @param edge
     */
    public TrapezoidalFuzzySet(double m1, double m2, EdgeType edge) {
        this(m1, m2, 1.0, 0.0, edge);
    }
    
    /**
     *
     * @return
     */
    public double getA() {
        return this.points[0].getFirst();
    }
    
    /**
     *
     * @return
     */
    public double getB() {
        return this.points[1].getFirst();
    }    
    
    /**
     *
     * @return
     */
    public double getC() {
        return this.points[2].getFirst();
    }    
    
    /**
     *
     * @return
     */
    public double getD() {
        return this.points[3].getFirst();
    }    

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return "Trapezodial Function";
    }
    
    
    
}
