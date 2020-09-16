
package fdt.fuzzy.measures;

import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.IntermediateNode;
import fdt.core.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * To compute the fuzzy measure value using the Hierarchically Decomposable Fuzzy Measure method.
 * @author Najlaa Maaroof
 */
public class HierarchicallyDecomposableFuzzyMeasure implements FuzzyMeasure {

    private static final long serialVersionUID = 1348975180874749515L;

    @Override
    public double evaluate(List<DecisionNode> subset, DecisionSet universeSet) {

        HashMap<String, Double> map = new HashMap<>();

        List<Node> nodes = new ArrayList<>();

        for (DecisionNode n : subset) {

            nodes.add(n);
            map.put(n.getText(),
                    n.getSupport()
                    * ((IntermediateNode) n.getParent()).getAmbiguity());
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
                        .mapToDouble(n -> map.get(n.getText()))
                        .toArray();

                double m = mu(singletones, p.getLambda(universeSet.getName()));
                map.put(p.getText(), m * p.getAmbiguity());

            }

            nodes = nodes
                    .stream()
                    .filter(n -> !nodesAtMaxDepth.contains(n))
                    .collect(Collectors.toList());
            nodes.addAll(parents);
        }

        return map.get(nodes.get(0).getText());
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

    /**
     *
     * @param singletones
     * @param lambda
     * @return
     */
    protected double mu(double[] singletones, double lambda) {
        if (singletones.length == 1) {
            return singletones[0];
        }

        if (lambda == 0) {
            return Arrays.stream(singletones).sum();
        }

        double v = 1;
        for (double i : singletones) {
            v *= (1 + lambda * i);
        }
        v -= 1;
        v /= lambda;
        return v;
    }

}
