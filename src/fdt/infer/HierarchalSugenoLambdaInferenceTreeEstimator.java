
package fdt.infer;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.IntermediateNode;
import fdt.core.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An inference estimator based on Hierarchal Sugeno Lambda method. 
 * This inference estimator requires an IntegralBasedVotingStrategy based on HierarchicallyDecomposableFuzzyMeasure.
 * @author Mohammed Jabreel
 */
public class HierarchalSugenoLambdaInferenceTreeEstimator extends SugenoLambdaInferenceTreeEstimator {

    @Override
    protected void computeLambdas(InferenceModelSpec model) {

        model.decisionSets.forEach(this::computeLambdas);

//        Stack<IntermediateNode> stack = new Stack<>();
//
//        stack.push(model.getTree());
//
//        while (!stack.isEmpty()) {
//            IntermediateNode node = stack.pop();
//            lambda(node);
//
//            for (EdgeBase e : node.getEdges()) {
//                Node n = e.getChild();
//                if (!n.isLeaf()) {
//                    stack.push((IntermediateNode) n);
//                }
//            }
//        }
    }

    /**
     * To compute the lambdas for each node in the tree.
     * @param set 
     */
    private void computeLambdas(DecisionSet set) {
        List<Node> nodes = new ArrayList<>();

        HashMap<Node, Double> map = new HashMap<>();

        for (DecisionNode n : set.getDecisionNodes()) {

            nodes.add(n);
            map.put(n, mu(n));

        }

        while (nodes.size() != 1) {
            int maxDepth = nodes
                    .stream()
                    .mapToInt(n -> n.getDepth())
                    .max()
                    .getAsInt();

            List<Node> nodesAtMaxDepth = nodes
                    .stream()
                    .filter(n -> n.getDepth() == maxDepth)
                    .collect(Collectors.toList());

            List<IntermediateNode> parents = getParents(nodesAtMaxDepth);

            for (IntermediateNode p : parents) {
                List<Node> childNodes = getChildren(p);

                childNodes.retainAll(nodesAtMaxDepth);

                double[] singletones = childNodes
                        .stream()
                        .mapToDouble(n -> map.get(n))
                        .toArray();

                double lambd = getLambda(singletones);
                p.setLambda(set.getName(), lambd);
                map.put(p, p.getAmbiguity());

            }

            nodes = nodes
                    .stream()
                    .filter(n -> !nodesAtMaxDepth.contains(n))
                    .collect(Collectors.toList());
            nodes.addAll(parents);

        }
    }

    /**
     * To compute the lambda for a given node. 
     * @param node
     * @return 
     */
    private double lambda(IntermediateNode node) {

        if (!node.hasLambda()) {

            List<Double> mus = new ArrayList<>();

            node.getEdges().forEach((edge) -> {
                mus.add(mu(edge.getChild()));
            });
            double lambda = getLambda(mus.stream().mapToDouble(v -> v).toArray());
            node.setLambda(lambda);

//            Polynomial p = null;
//
//            Polynomial one = new Polynomial(0, 1);
//
//            for (double m : mus) {
//                Polynomial p1 = new Polynomial(1, m);
//                p1 = p1.plus(one);
//                if (p == null) {
//                    p = p1;
//                } else {
//                    p = p.times(p1);
//                }
//            }
//
//            if (p != null) {
//
//                p = p.minus(one);
//
//                double[] coef = new double[p.getDegree()];
//                coef[0] = -1;//node.getAmbiguity();
//                for (int i = 1; i <= coef.length; i++) {
//                    coef[i - 1] += p.getCoef()[i];
//                }
//
//                Polynomial p2 = Polynomial.getPolynomial(coef);
//
//                p2.setDomain(new Tuple<>(-1.0, 100000.0));
//                p2.addUndefinedValue(0);
//                p2.addUndefinedValue(-1);
//
//                double lambda = p2.solve();
//                node.setLambda(lambda);
//
//            }
        }
        return node.getLambda();

    }

    /**
     *
     * @param nodes
     * @return
     */
    protected List<IntermediateNode> getParents(List<Node> nodes) {
        HashSet<IntermediateNode> parents = new HashSet<>();

        nodes.forEach((n) -> {
            parents.add((IntermediateNode) n.getParent());
        });

        return parents.stream().collect(Collectors.toList());
    }

    /**
     *
     * @param node
     * @return
     */
    protected List<Node> getChildren(IntermediateNode node) {
        List<Node> nodes = new ArrayList<>();
        node.getEdges().forEach(e -> nodes.add(e.getChild()));
        return nodes;
    }

    private double mu(Node node) {

        if (node.isRoot()) {
            return 1.0;
        }

        if (node.isLeaf()) {
            DecisionNode dn = (DecisionNode) node;
            IntermediateNode ln = (IntermediateNode) node.getParent();
            double s = dn.getSupport();
            double g = ln.getAmbiguity();
            g = Math.max(g, 1e-6);
            return s * g;
        }

        double g = ((IntermediateNode) node).getAmbiguity();
        return Math.max(g, 1e-6);
    }

}
