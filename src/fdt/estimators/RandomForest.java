
package fdt.estimators;

import fdt.core.IntermediateNode;
import fdt.core.Node;
import fdt.core.TrainableIntermediateNode;
import fdt.data.Dataset;
import fdt.infer.InferenceModelSpec;
import fdt.infer.InferenceTreeEstimator;
import fdt.utils.RandomFactory;
import fdt.utils.Tuple;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Najlaa Maaroof
 */


/**
 * Thus class is used to train a fuzzy random forest.
 * 
**/
public class RandomForest implements Estimator{

    /**
     * The number of the trees in the random forest (n).
     */
    protected int numOfEstimators = 100;
    
    /**
     * The maximum depth of each tree. The default value is -1 and it means it is not used.
     */
    protected int depthMax = -1;
    
    /**
     * The sample size of the bagging. 
     */
    protected double sampleSize = 2.0 / 3;
    
    /**
     * The number of attributes will be used in the growing when we select the best attribute (jamma). 
     * The default value is -1 and it means that we use the all attributes.
     */
    protected int attributesSamplingSize = -1;

    /**
     * The base tree estimator that will be used to train the n trees.
     */
    protected TreeEstimator estimator;
    
    /**
     * The inference estimator. It is used to eliminate the unnecessary information 
     * that are used in the training phase 
     * and in some cases compute new properties depending on the Inference Estimator type.
     */
    protected InferenceTreeEstimator inferenceTreeEstimator;
    
    
    /**
     * The list of the models (its size is going to be n) that will be used in the inference phase.
     */
    private final List<InferenceModelSpec> models = new ArrayList<>();

    /**
     *
     */
    public RandomForest() {
    }

    /**
     *
     * @param estimator
     */
    public RandomForest(TreeEstimator estimator) {
        this.estimator = estimator;
    }

    /**
     *
     * @param estimator
     * @param inferenceTreeEstimator
     */
    public RandomForest(TreeEstimator estimator, InferenceTreeEstimator inferenceTreeEstimator) {
        this.estimator = estimator;
        this.inferenceTreeEstimator = inferenceTreeEstimator;
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
        this.estimator.setAttributesSamplingSize(attributesSamplingSize);
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
    public int getNumOfEstimators() {
        return numOfEstimators;
    }

    /**
     *
     * @param numOfEstimators
     */
    public void setNumOfEstimators(int numOfEstimators) {
        this.numOfEstimators = numOfEstimators;
    }

    /**
     *
     * @return
     */
    public double getSampleSize() {
        return sampleSize;
    }

    /**
     *
     * @param sampleSize
     */
    public void setSampleSize(double sampleSize) {
        this.sampleSize = sampleSize;
    }

    /**
     *
     * @return
     */
    public TreeEstimator getEstimator() {
        return estimator;
    }

    /**
     *
     * @param estimator
     */
    public void setEstimator(TreeEstimator estimator) {
        this.estimator = estimator;
    }
    
    /**
     * This function is used to train the fuzzy random forest based on the training dataset passed.
     * @param trainDataset 
     */

    public void fit(Dataset trainDataset) {
        models.clear();
        double [] oobEvaluations = 
        IntStream.range(0, numOfEstimators).mapToDouble(i -> {
            
            RandomFactory.setSeed(i - numOfEstimators);
            
            System.out.println("************* Training the estimator " + (i + 1) + " *************");

            
            // we perform the bagging step and obtained a tuple of datasets, 
            // teh first one is the sampled dataset that will be used to train the current estimator.
            // the second one is the Out-Of-Bag (OOB) partition.
            
            Tuple<Dataset, Dataset> datasets = trainDataset.bagging2(sampleSize);
            
            Dataset sampledDataset = datasets.getFirst();
            Dataset oobDataset = datasets.getSecond();
            
            System.out.println("Train Size: " + sampledDataset.getNumberOfRows());
            Node tree = estimator.fit(sampledDataset);
            InferenceModelSpec model = inferenceTreeEstimator.fit((TrainableIntermediateNode) tree);
            models.add(model);

            // estimate OOB error
//            System.out.println("Estimate OOB performance of the estimator " + (i + 1));
//            System.out.println("OOB Size: " + oobDataset.getNumberOfRows());
//            PredictionEngine eng = new PredictionEngine(new ChoquetIntegralVotingStrategy());
//            eng.addToEngine(tree);
            double oob = 0;//Utils.evaluate(oobDataset, eng);
            return oob;
        }).toArray();
        
        double oobMean = Arrays.stream(oobEvaluations).sum() / numOfEstimators;
        System.out.println(String.format("Average OOB HM: %.4f", oobMean));
    }

    /**
     *
     * @return
     */
    public InferenceTreeEstimator getInferenceTreeEstimator() {
        return inferenceTreeEstimator;
    }

    /**
     *
     * @param inferenceTreeEstimator
     */
    public void setInferenceTreeEstimator(InferenceTreeEstimator inferenceTreeEstimator) {
        this.inferenceTreeEstimator = inferenceTreeEstimator;
    }

    /**
     *
     * @return
     */
    public List<InferenceModelSpec> getModels() {
        return models;
    }
    
    
    
}
