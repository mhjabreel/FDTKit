package fdt.utils;

import java.util.HashSet;
import org.ejml.data.Complex_F64;

/**
 *
 * @author Mohammed Jabreel
 */
public class Polynomial {

    private double[] coef;
    private double bias = 0;
    private int degree;

    private Tuple<Double, Double> domain;
    private final HashSet<Double> undefinedValues = new HashSet<>();

    private Polynomial() {

    }

    /**
     *
     * @param n
     * @param a
     */
    public Polynomial(int n, double a) {
        this.coef = new double[n + 1];
        this.coef[n] = a;
        reduce();
//        this.degree = coef.length;
    }

    /**
     *
     * @param coef
     */
    public Polynomial(double[] coef) {

        this.coef = coef;
        this.degree = coef.length;
    }

    /**
     *
     * @param coef
     * @param bias
     */
    public Polynomial(double[] coef, double bias) {
        this.coef = coef;
        this.degree = coef.length;
        this.bias = bias;
    }

    private void reduce() {
        degree = -1;
        for (int i = coef.length - 1; i >= 0; i--) {
            if (coef[i] != 0) {
                degree = i;
                return;
            }
        }
    }

    /**
     *
     * @param x
     * @return
     */
    public double evaluate(double x) {
        double v = bias;
        for (int i = degree - 1; i >= 0; i--) {
            v += coef[i] * Math.pow(x, i);
        }
        return v;
    }

    /**
     *
     * @param that
     * @return
     */
    public Polynomial times(Polynomial that) {
        Polynomial poly = new Polynomial(this.degree + that.degree, 0);
        for (int i = 0; i <= this.degree; i++) {
            for (int j = 0; j <= that.degree; j++) {
                poly.coef[i + j] += (this.coef[i] * that.coef[j]);
            }
        }
        poly.reduce();
        return poly;
    }

    /**
     *
     * @param that
     * @return
     */
    public Polynomial plus(Polynomial that) {
        Polynomial poly = new Polynomial(Math.max(this.degree, that.degree), 0);
        for (int i = 0; i <= this.degree; i++) {
            poly.coef[i] += this.coef[i];
        }
        for (int i = 0; i <= that.degree; i++) {
            poly.coef[i] += that.coef[i];
        }
        poly.reduce();
        return poly;
    }

    /**
     *
     * @param that
     * @return
     */
    public Polynomial minus(Polynomial that) {
        Polynomial poly = new Polynomial(Math.max(this.degree, that.degree), 0);
        for (int i = 0; i <= this.degree; i++) {
            poly.coef[i] += this.coef[i];
        }
        for (int i = 0; i <= that.degree; i++) {
            poly.coef[i] -= that.coef[i];
        }
        poly.reduce();
        return poly;
    }

    /**
     *
     * @return
     */
    public Polynomial differentiate() {
        if (degree == 0) {
            return new Polynomial(new double[]{});
        }

        Polynomial poly = new Polynomial(this.degree - 1, 0);

//        poly.coef = new double[degree - 1];
//        poly.bias = coef[0];
//
//        poly.degree = degree - 1;
        for (int i = 0; i < degree; i++) {
            poly.coef[i - 1] = (i + 1) * coef[i];
        }
        poly.reduce();
        return poly;
    }

    /**
     *
     * @param domain
     */
    public void setDomain(Tuple<Double, Double> domain) {
        this.domain = domain;
    }

    /**
     *
     * @param value
     */
    public void addUndefinedValue(double value) {
        undefinedValues.add(value);
    }

    /**
     *
     * @return
     */
    public double solve() {

        double[] coefficients = this.coef;

        Complex_F64[] roots = Utils.findRoots(coefficients);

        for (Complex_F64 root : roots) {
            if (root.isReal()) {
                double r = root.getReal();
//                System.out.println(r);
                if (r >= domain.getFirst() && r <= domain.getSecond()) {
                    if (!undefinedValues.contains(r)) {
                        return r;
                    }
                }

            }
        }

        return 0;//Double.NaN;
    }

    /**
     *
     * @param coef
     * @param bias
     * @return
     */
    public static Polynomial getPolynomial(double[] coef, double bias) {
        Polynomial p = new Polynomial(0, bias);
        for (int i = 0; i < coef.length; i++) {
            Polynomial pi = new Polynomial(i + 1, coef[i]);
            p = p.plus(pi);
            p.reduce();
        }
        return p;
    }

    /**
     *
     * @param coef
     * @return
     */
    public static Polynomial getPolynomial(double[] coef) {
        Polynomial p = new Polynomial(0, 0);
        for (int i = 0; i < coef.length; i++) {
            Polynomial pi = new Polynomial(i, coef[i]);
            p = p.plus(pi);
            p.reduce();
        }
        return p;
    }

    /**
     *
     * @return
     */
    public double[] getCoef() {
        return coef;
    }

    /**
     *
     * @return
     */
    public int getDegree() {
        return degree;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {

        switch (degree) {
            case -1:
                return "0";
            case 0:
                return "" + coef[0];
            case 1:
                return coef[1] + "x + " + coef[0];
            default:
                break;
        }

        String s = coef[degree] + "x^" + degree;
        for (int i = degree - 1; i >= 0; i--) {
            if (coef[i] == 0) {
                continue;
            } else if (coef[i] > 0) {
                s = s + " + " + (coef[i]);
            } else if (coef[i] < 0) {
                s = s + " - " + (-coef[i]);
            }
            if (i == 1) {
                s = s + "x";
            } else if (i > 1) {
                s = s + "x^" + i;
            }
        }
        return s;
//        StringBuilder sb = new StringBuilder();
//        sb.append("ρ(x) = ");
    }

    /**
     *
     * @param values
     * @return
     */
    protected static double getLambda(double[] values) {

        double lambda = 0;

        Polynomial p = null;

        Polynomial one = new Polynomial(0, 1);

        for (double v : values) {
            Polynomial p1 = new Polynomial(1, v);
            p1 = p1.plus(one);
            if (p == null) {
                p = p1;
            } else {
                p = p.times(p1);
            }
        }

        if (p != null) {
            p = p.minus(one);

            double[] coef = new double[p.getDegree()];
            coef[0] = -1;
            for (int i = 1; i <= coef.length; i++) {
                coef[i - 1] += p.getCoef()[i];
            }

            Polynomial p2 = Polynomial.getPolynomial(coef);
            p2.setDomain(new Tuple<>(-1.0, 100000.0));
            p2.addUndefinedValue(0);
            p2.addUndefinedValue(-1);

            lambda = p2.solve();
        }
        return lambda;
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        double lambda = getLambda(new double[]{0, 0.3, 0.5});

        Polynomial p0 = getPolynomial(new double[]{0.8107, 0.1454436});
        Polynomial g = new Polynomial(0, 0.67);
        System.out.println(p0);
        System.out.println(p0.minus(g));

        Polynomial p = new Polynomial(new double[]{-0.3684, 0.13245277, 0.0092176795});
//        p.setDomain(new Tuple<>(-1.0, 100000.0));
//        p.addUndefinedValue(0);
//        p.addUndefinedValue(-1);
//
//        System.out.println(p);
//        System.out.println(p.differentiate());
//        System.out.println(p.solve());

        Polynomial one = new Polynomial(0, 1);
        Polynomial zero = new Polynomial(0, 0);
        Polynomial p1 = new Polynomial(1, 0.5427);
        Polynomial p2 = new Polynomial(1, 0.268);

        p1 = p1.plus(one);
        p2 = p2.plus(one);
        System.out.println(p1);
        System.out.println(p2);
        Polynomial p3 = p2.times(p1);

        System.out.println(p3);

    }

}
