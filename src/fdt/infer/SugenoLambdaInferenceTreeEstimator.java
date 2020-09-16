
package fdt.infer;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.TrainableIntermediateNode;
import fdt.utils.Polynomial;
import fdt.utils.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An inference estimator based on Sugeno Lambda method. This inference estimator requires an IntegralBasedVotingStrategy based on SugenoLambdaMeasure.
 * 
 * @author Najlaa Maaroof
 */
public class SugenoLambdaInferenceTreeEstimator extends InferenceTreeEstimator {

    /**
     *
     */
    protected boolean normalizeSupports = false;

    /**
     *
     */
    protected double sumOfSupports = 1;

    /**
     * We override the method fit to compute the lambdas, which will be used in the inference phase.
     * 
     * @param root
     * @return 
     */
    @Override
    public InferenceModelSpec fit(TrainableIntermediateNode root) {

        InferenceModelSpec model = super.fit(root);

        if (normalizeSupports) {
            double s = 0;

            for (DecisionSet ds : model.decisionSets) {
                for (DecisionNode n : ds.getDecisionNodes()) {
                    s += n.getSupport();
                }
            }

            for (DecisionSet ds : model.decisionSets) {
                for (DecisionNode n : ds.getDecisionNodes()) {
                    n.setSupport(n.getSupport() / s);
                }
            }

            sumOfSupports = s;
        }

        computeLambdas(model);
        return model;
    }

    /**
     * To compute the lambdas.
     * @param model 
     */
    protected void computeLambdas(InferenceModelSpec model) {
        List<Double> values = new ArrayList<>();

        model.decisionSets.forEach((s) -> {
            //            
            double[] vals = s.getDecisionNodes().stream().mapToDouble(n -> n.getSupport()).toArray();
            double lambda = getLambda(vals);
            model.tree.setLambda(s.getName(), lambda);
            for (double v : vals) {
                values.add(v);
            }
        });

        double lambda = getLambda(values.stream().mapToDouble(v -> v).toArray());
        model.tree.setLambda(lambda);
    }

    /**
     * For a list of values, get the lambda.
     * @param values
     * @return 
     */
    protected double getLambda(double[] values) {

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
            if (p.getDegree() > 0) {
                double[] coef = new double[p.getDegree()];
                coef[0] = -1;
                for (int i = 1; i <= coef.length; i++) {
                    coef[i - 1] += p.getCoef()[i];
                }

                Polynomial p2 = Polynomial.getPolynomial(coef);
                p2.setDomain(new Tuple<>(-1.0, 100000000.0));
                p2.addUndefinedValue(0);
                p2.addUndefinedValue(-1);

                lambda = p2.solve();
            }

        }
        return lambda;
    }

    /**
     *
     * @return
     */
    public boolean isNormalizeSupports() {
        return normalizeSupports;
    }

    /**
     *
     * @param normalizeSupports
     */
    public void setNormalizeSupports(boolean normalizeSupports) {
        this.normalizeSupports = normalizeSupports;
    }

}
