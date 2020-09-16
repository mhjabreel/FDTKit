
package fdt.fuzzy;

import fdt.utils.Tuple;

/**
 *
 * @author Mohammed Jabreel
 */
public class PiecewiseLinearFunction implements FuzzySet {

    /**
     *
     */
    protected Tuple<Double, Double>[] points;

    /**
     *
     */
    public PiecewiseLinearFunction() {
        this.points = null;
    }

    /**
     *
     * @param points
     */
    public PiecewiseLinearFunction(Tuple<Double, Double>[] points) {

        if (points[0].getSecond() < 0 || points[0].getSecond() > 1) {
            throw new IllegalArgumentException("Y value of points must be in the range of [0, 1].");
        }

        for (int i = 1; i < points.length; i++) {
            if (points[i].getSecond() < 0 || points[i].getSecond() > 1) {
                throw new IllegalArgumentException("Y value of points must be in the range of [0, 1].");
            }

            if (points[i - 1].getFirst() > points[i].getFirst()) {
                throw new IllegalArgumentException("Points must be in crescent order on X axis.");
            }
        }

        this.points = points;

    }

    /**
     *
     * @param x
     * @return
     */
    @Override
    public double getMembership(double x) {
        if (this.points.length == 0) {
            return 0.0d;
        }
        if (x < this.points[0].getFirst()) {
            return this.points[0].getSecond();
        }
        for (int i = 1; i < this.points.length; i++) {
            if (x <= this.points[i].getFirst()) {
                double y1 = this.points[i].getSecond();
                double y0 = this.points[i - 1].getSecond();
                double x1 = this.points[i].getFirst();
                double x0 = this.points[i - 1].getFirst();

                double m = (y1 - y0) / (x1 - x0);
                return m * (x - x0) + y0;

            }
        }

        return this.points[this.points.length - 1].getSecond();
    }

}
