
package fdt.estimators;

import com.google.common.collect.Streams;
import fdt.core.DecisionNode;
import fdt.core.Node;
import fdt.core.TrainableEdge;
import fdt.core.TrainableIntermediateNode;
import fdt.data.Attribute;
import fdt.data.Dataset;
import fdt.data.Term;
import fdt.utils.RandomFactory;
import fdt.utils.Tuple;
import fdt.utils.Utils;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Najlaa Maaroof
 */

/**
 * This class is used to train a fuzzy decision tree. 
 
 */
public class TreeEstimator implements Estimator {

    /**
     * The significance level parameter (alpha). 
     * It is used to filter out the fuzzy partitions 
     * that have values less than alpha (i.e., the significance level) 
     * by setting these values to zero.
     */
    private double significanceLevel = 0.5;
    
    /**
     * The level of truth parameter (beta).
     * It is used to stop the growing and determine the leaves nodes.
     * A node is a leaf if it has a classification support greater or equal to beta.
     */
    private double levelOfTruth = 0.7;
    
    /**
     * The maximum depth of the tree. The value -1 means this parameter is not used.
     */
    private int depthMax = -1;
    
    /**
     * To specify the number of attributes that can be used in the growing process to chose the best attribute.
     * The value -1 means, we use all the attributes.
     */
    private int attributesSamplingSize = -1;
    
    

   /**
   * Constructor that sets default parameters' values.
   */    
    public TreeEstimator() {
    }

    /**
     *
     * @param significanceLevel
     * @param levelOfTruth
     */
    public TreeEstimator(double significanceLevel, double levelOfTruth) {
        this.significanceLevel = significanceLevel;
        this.levelOfTruth = levelOfTruth;
    }

    /**
     * This function is used to train a fuzzy decision tree on a dataset.
     * @param dataset: the training dataset to be used. @see fdt.data.Dataset
     * @return an instance of fdt.core.Node represents the root of the obtained tree.
     */
    public Node fit(Dataset dataset) {
        Attribute targetAttribute = dataset.getTarget();
        Tuple<Attribute, Double> bestAttributeValues = getBestAttribute(dataset, null);
        if (bestAttributeValues != null) {

            TrainableIntermediateNode root = new TrainableIntermediateNode();
            root.setText(bestAttributeValues.getFirst().getName());
            root.setAmbiguity(bestAttributeValues.getSecond());
            root.setAttribute(bestAttributeValues.getFirst());
            Attribute attribute = bestAttributeValues.getFirst();
            Dataset newDataset = dataset.dropAttribute(attribute.getName());

            for (Term source : attribute.getTerms()) {

                double[] nodeEvidence = source.getValues();
                nodeEvidence = Arrays
                        .stream(nodeEvidence)
                        .map(v -> v < significanceLevel ? 0 : v)
                        .toArray();

                TrainableEdge edge = new TrainableEdge(source.getName());
                edge.setEvidences(nodeEvidence);

                double g = getAmbiguity(targetAttribute, nodeEvidence);
                edge.setAmbiguity(g);

                computeClassSupports(dataset.getTarget(), edge, nodeEvidence);

                growTree(newDataset, root, edge);

            }

            return root;
        }
        return null;

    }

    /**
     * Compute the class supports for each class. Each class support is stored in the edge.
     * @param targetAttribute: the target attribute, i.e., the label.
     * @param edge: the current edge.
     * @param evidence The fuzzy evidence.
     */
    private void computeClassSupports(Attribute targetAttribute, TrainableEdge edge, double[] evidence) {
        for (Term target : targetAttribute.getTerms()) {

            double truthOfClassification = getClassificationTruth(evidence, target);
            edge.setClassSupport(target.getName(), truthOfClassification);
        }
    }

    /**
     * To create decision nodes when we end up with a leaf.
     * @param parent: the parent of the leaf. It is a fuzzy attribute, e.g. Age.
     * @param edge: represents one of the parent linguistic values, e.g. Old.
     */
    private void createDecisionNodes(TrainableIntermediateNode parent, TrainableEdge edge) {
//        Tuple<String, Double> bestClassSpec = edge.getBestClass();
//        DecisionNode leaf = new DecisionNode();
//        leaf.setText(bestClassSpec.getFirst());
//        leaf.setSupport(bestClassSpec.getSecond());
//        edge.connect(parent, leaf);
        edge.getClassSupports().forEach((c, s) -> {
            DecisionNode leaf = new DecisionNode();
            leaf.setText(c);
            leaf.setSupport(s);
            new TrainableEdge(edge).connect(parent, leaf);            
        }); 

    }

    /**
     * This function is used to grow the tree recursively. 
     * @param dataset: the training set.
     * @param node: the selected node to grow from. It is a fuzzy attribute, e.g. Age.
     * @param edge the connection. represents one of the parent linguistic values, e.g. Old.
     */
    private void growTree(Dataset dataset, TrainableIntermediateNode node, TrainableEdge edge) {

        Attribute targetAttribute = dataset.getTarget();

        Tuple<String, Double> bestClassSpec = edge.getBestClass();

        boolean needsToGrow = true;

        needsToGrow &= !dataset.getInputs().isEmpty(); // 1)
        needsToGrow &= (depthMax == -1 || node.getDepth() < depthMax); //2)
        needsToGrow &= (node.getDepth() == 0 || bestClassSpec.getSecond() < levelOfTruth); //3)
        /**
         * We check if we can stop growing.
         * We can not grow if one of the following conditions hold:
         * 1) The current branch is empty. 
         * 2) The maximum depth is set and the depth of the current node greater than the maximum depth.
         * 3) The classification truth of the current node is greater than or equal to beta and the current node is not a root.
         */
        if (!needsToGrow) {
            
            // if we can not grow, create the decsion nodes and return.
            
            createDecisionNodes(node, edge);
            return;
        }

        double[] evidence = edge.getEvidences();
        
        // In the case of the growing, we find the best attribute. 
        // Initially, at the root node, the fuzzy evidence is null.

        Tuple<Attribute, Double> bestAttributeValues = getBestAttribute(dataset, evidence);

        if (bestAttributeValues != null) {

            TrainableIntermediateNode node2 = new TrainableIntermediateNode();

            node2.setText(bestAttributeValues.getFirst().getName());
            node2.setAmbiguity(bestAttributeValues.getSecond());
            node2.setAttribute(bestAttributeValues.getFirst());
            edge.connect(node, node2);

            Attribute attribute = bestAttributeValues.getFirst();
            Dataset newDataset = dataset.dropAttribute(attribute.getName());

            for (Term source : attribute.getTerms()) {

                double[] nodeEvidence = source.getValues();
                nodeEvidence = Arrays
                        .stream(nodeEvidence)
                        .map(v -> v < significanceLevel ? 0 : v)
                        .toArray();

                boolean isEmptyBrnach = Utils.embpty(nodeEvidence);
                if (!isEmptyBrnach) {
                    if (evidence != null) {
                        double[] evidence2 = Arrays.stream(evidence)
                                .map(v -> v < significanceLevel ? 0 : v).toArray();

                        nodeEvidence = Streams.zip(Arrays.stream(nodeEvidence).boxed(),
                                Arrays.stream(evidence2).boxed(),
                                (v1, v2) -> Math.min(v1, v2)
                        ).mapToDouble(Double::doubleValue).toArray();
                    }

                    TrainableEdge edge2 = new TrainableEdge(source.getName());

                    double g = getAmbiguity(targetAttribute, nodeEvidence);
                    edge2.setAmbiguity(g);
                    edge2.setEvidences(nodeEvidence);

                    computeClassSupports(dataset.getTarget(), edge2, nodeEvidence);

                    growTree(newDataset, node2, edge2);
                } else {

                    createDecisionNodes(node, edge);
                }

            }

        } else {
            createDecisionNodes(node, edge);
        }
    }

    /**
     * Gets the best attribute from the dataset.
     * @param dataset
     * @return a tuple of fdt.data.Attribute and its ambiguity value.
     */
    public Tuple<Attribute, Double> getBestAttribute(Dataset dataset) {
        return getBestAttribute(dataset, null);
    }

    /**
     * Gets the best attribute given a fuzzy evidences and the minimum ambiguity.
     * @param dataset training set.
     * @param evidence fuzzy evidences.
     * @param minAmbiguity minimum ambiguity.
     * @return a tuple of fdt.data.Attribute and its ambiguity value.
     */
    public Tuple<Attribute, Double> getBestAttribute(Dataset dataset, double[] evidence, double minAmbiguity) {

        Attribute targetAttribute = dataset.getTarget();

        Attribute bestAttribute = null;

        final List<Attribute> inputs = dataset.getInputs();
        List<Attribute> tmp = dataset.getInputs();

        if (attributesSamplingSize > -1) {
            int[] permutation = RandomFactory.getPermutation(inputs.size());
            tmp = Arrays.stream(permutation)
                    .limit(attributesSamplingSize)
                    .mapToObj(i -> inputs.get(i))
                    .collect(Collectors.toList());
        }

        for (Attribute attribute : tmp) {

            double g = getAmbiguity(attribute, targetAttribute, evidence);

            if (g < minAmbiguity) {
                bestAttribute = attribute;
                minAmbiguity = g;
            } else if (g == minAmbiguity) {

            }
        }
        if (bestAttribute == null) {
            return null;
        }
        return new Tuple<>(bestAttribute, minAmbiguity);
    }

    /**
     * Gets the best attribute given a fuzzy evidences.
     * @param dataset
     * @param evidence
     * @return a tuple of fdt.data.Attribute and its ambiguity value.
     */
    public Tuple<Attribute, Double> getBestAttribute(Dataset dataset, double[] evidence) {

        Attribute targetAttribute = dataset.getTarget();
        double minAmbiguity
                = evidence == null ? Double.MAX_VALUE
                        : getAmbiguity(targetAttribute, evidence);
        Attribute bestAttribute = null;

        final List<Attribute> inputs = dataset.getInputs();
        List<Attribute> tmp = dataset.getInputs();

        if (attributesSamplingSize > -1) {
            int[] permutation = RandomFactory.getPermutation(inputs.size());
            tmp = Arrays.stream(permutation)
                    .limit(attributesSamplingSize)
                    .mapToObj(i -> inputs.get(i))
                    .collect(Collectors.toList());
        }

        for (Attribute attribute : tmp) {
            double g = getAmbiguity(attribute, targetAttribute, evidence);

            if (g < minAmbiguity) {
                bestAttribute = attribute;
                minAmbiguity = g;
            } else if (g == minAmbiguity) {

            }
        }
        if (bestAttribute == null) {
            return null;
        }
        return new Tuple<>(bestAttribute, minAmbiguity);
    }

    /**
     * To compute the ambiguity given the target attribute and a fuzzy evidences.
     * @param target the target attribute.
     * @param evidence the fuzzy evidences.
     * @return the ambiguity.
     */
    public double getAmbiguity(Attribute target, double[] evidence) {

        double[] classificationPossibilities = target.getTerms().stream()
                .map(t -> Utils.subsethood(evidence, t.getValues()))
                .mapToDouble(Double::doubleValue).toArray();

        return Utils.ambiguity(classificationPossibilities, Optional.empty());

    }

    /**
     * To compute the ambiguity of an attribute given a target.
     * @param attribute
     * @param target
     * @return the ambiguity.
     */
    public double getAmbiguity(Attribute attribute, Attribute target) {
        return getAmbiguity(attribute, target, null);
    }

        /**
     * To compute the ambiguity of an attribute given a target and a fuzzy evidences.
     * @param attribute
     * @param target
     * @param evidence
     * @return the ambiguity.
     */
    
    public double getAmbiguity(Attribute attribute, Attribute target, double[] evidence) {

        double[] weights = new double[attribute.getTerms().size()];
        double[] ambiguities = new double[weights.length];

        int i = 0;
        for (Term term : attribute.getTerms()) {
            double[] conditions = term.getValues();
            conditions = Arrays.stream(conditions).map(v -> v < significanceLevel ? 0 : v).toArray();

            if (evidence != null) {
                evidence = Arrays.stream(evidence).map(v -> v < significanceLevel ? 0 : v).toArray();

                conditions = Streams.zip(Arrays.stream(conditions).boxed(),
                        Arrays.stream(evidence).boxed(), (a, b) -> Math.min(a, b))
                        .mapToDouble(Double::doubleValue).toArray();
            }

            weights[i] = Arrays.stream(conditions).sum();

            double[] classificationPossibilities = new double[target.getTerms().size()];
            int j = 0;

            for (Term conclusionTerm : target.getTerms()) {

                classificationPossibilities[j++] = Utils.subsethood(conditions, conclusionTerm.getValues());
            }

            ambiguities[i++] = Utils.ambiguity(classificationPossibilities, Optional.empty());
        }
        double sumOfWeights = Arrays.stream(weights).sum() + Utils.EPSILON;
        weights = Arrays.stream(weights).map(w -> w / sumOfWeights).toArray();
        return Streams.zip(
                Arrays.stream(weights).boxed(),
                Arrays.stream(ambiguities).boxed(),
                (w, g) -> w * g)
                .mapToDouble(Double::doubleValue).sum();

    }

    /**
     * To compute the classification truth of a Term given the target and fuzzy evidences.
     * @param source
     * @param target
     * @param evidence
     * @return the classification truth.
     */    
    public double getClassificationTruth(Term source, Term target, double[] evidence) {
        double[] conditions = source.getValues();
        double[] conclusion = target.getValues();
        evidence = Arrays.stream(evidence).map(v -> v < significanceLevel ? 0 : v).toArray();
        conditions = Arrays.stream(conditions).map(v -> v < significanceLevel ? 0 : v).toArray();

        if (evidence != null) {
            conditions = Streams.zip(
                    Arrays.stream(conditions).boxed(), Arrays.stream(evidence).boxed(),
                    (c, e) -> Math.min(c, e)).mapToDouble(Double::doubleValue).toArray();
        }

        return Utils.subsethood(conditions, conclusion);
    }
    
    /**
     * To compute the classification truth of given the target and a condition.
     * @param conditions
     * @param target
     * @return the classification truth.
     */
    public double getClassificationTruth(double[] conditions, Term target) {

        double[] conclusion = target.getValues();
        conditions = Arrays.stream(conditions).map(v -> v < significanceLevel ? 0 : v).toArray();
        return Utils.subsethood(conditions, conclusion);
    }

    /**
     *
     * @return
     */
    public int getDepthMax() {
        return depthMax;
    }

    /**
     *
     * @param depthMax
     */
    public void setDepthMax(int depthMax) {
        this.depthMax = depthMax;
    }

    /**
     *
     * @return
     */
    public double getLevelOfTruth() {
        return levelOfTruth;
    }

    /**
     *
     * @param levelOfTruth
     */
    public void setLevelOfTruth(double levelOfTruth) {
        this.levelOfTruth = levelOfTruth;
    }

    /**
     *
     * @return
     */
    public double getSignificanceLevel() {
        return significanceLevel;
    }

    /**
     *
     * @param significanceLevel
     */
    public void setSignificanceLevel(double significanceLevel) {
        this.significanceLevel = significanceLevel;
    }

    /**
     *
     * @return
     */
    public int getAttributesSamplingSize() {
        return attributesSamplingSize;
    }

    /**
     *
     * @param attributesSamplingSize
     */
    public void setAttributesSamplingSize(int attributesSamplingSize) {
        this.attributesSamplingSize = attributesSamplingSize;
    }

}
