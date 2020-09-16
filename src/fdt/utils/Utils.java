
package fdt.utils;

import com.google.common.collect.Streams;
import fdt.core.DecisionNode;
import fdt.core.DecisionSet;
import fdt.core.EdgeBase;
import fdt.core.IntermediateNode;
import fdt.core.Node;
import fdt.data.Attribute;
import fdt.data.Dataset;
import fdt.data.Term;
import fdt.infer.InferenceEngine;
import fdt.infer.InferenceModelSpec;
import fdt.infer.PredictionResult;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.ejml.data.Complex_F64;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.factory.DecompositionFactory_DDRM;
import org.ejml.interfaces.decomposition.EigenDecomposition_F64;

/**
 *
 * @author Mohammed Jabreel
 */
public class Utils {

    /**
     *
     */
    public static double EPSILON = 1e-9;

    /**
     *
     * @param target
     * @param source
     * @return
     */
    public static double[] sortBy(double[] target, double[] source) {

        Tuple<Integer, Double>[] map = new Tuple[source.length];
        for (int i = 0; i < source.length; i++) {
            map[i] = new Tuple<>(i, source[i]);
        }

        Arrays.sort(map, (a, b) -> b.getSecond().compareTo(a.getSecond()));

        double[] sortedTarget = new double[target.length];

        for (int i = 0; i < target.length; i++) {
            sortedTarget[i] = target[map[i].getFirst()];
        }

        return sortedTarget;
    }

    /**
     *
     * @param condition
     * @param conclusion
     * @return
     */
    public static double subsethood(double[] condition, double[] conclusion) {

        double s = Arrays.stream(condition).boxed().mapToDouble(Double::doubleValue).sum();
//        if(s == 0) {
//            return 1;
//        }
        double ret = Streams.zip(
                Arrays.stream(condition).boxed(),
                Arrays.stream(conclusion).boxed(),
                (c1, c2) -> Math.min(c1, c2)
        ).mapToDouble(Double::doubleValue).sum() / s;

        return ret;
    }

    /**
     *
     * @param a
     * @return
     */
    public static boolean embpty(double[] a) {
        double s = Arrays.stream(a).sum();
//        System.out.println(s + " " + (s == 0));
        return s == 0;
    }

    /**
     *
     * @param distribution
     * @param normalize
     * @return
     */
    public static double ambiguity(double[] distribution, Optional<Boolean> normalize) {

        //Eqs (2, 3, 4)
        Supplier<Stream<Double>> streamSupplier = () -> Arrays.stream(distribution).boxed();
        Stream<Double> dStream = streamSupplier.get();

        if (normalize.orElse(Boolean.TRUE)) {
            double maxValue = streamSupplier
                    .get()
                    .mapToDouble(Double::doubleValue)
                    .max()
                    .getAsDouble() + EPSILON;
            dStream = streamSupplier.get().map((t) -> t / maxValue);
        }

        List<Double> distributionList = dStream.sorted(Comparator.reverseOrder()).collect(Collectors.toList());

        distributionList.add(0d);
        Supplier<Stream<Double>> streamSupplier2 = () -> distributionList.stream();
        List<Tuple<Double, Double>> collected = Streams.zip(streamSupplier2.get().limit(distributionList.size() - 1),
                streamSupplier2.get().skip(1), (u, t) -> new Tuple<>(u, t)).collect(Collectors.toList());

        double g = IntStream.range(0, collected.size())
                .mapToDouble(i -> Math.log(i + 1) * (collected.get(i).getFirst() - collected.get(i).getSecond())).sum();

        return g;

    }
    
    /**
     *
     * @param model
     */
    public static void printTree(InferenceModelSpec model) {
        System.out.println(getStringTree(model.getTree(), ""));
    }
    private static String getStringTree(Node node, String prefix) {
        
        String s = "";
        
        if(node.isLeaf()) {
            s += String.format("%s\t\t[%s (%.3f)]\n", prefix, node.getText(), ((DecisionNode)node).getSupport());
        }
        else {
            s += String.format("%s|%s, %.3f, %.3f|\n", prefix, node.getText(), ((IntermediateNode)node).getAmbiguity(), ((IntermediateNode)node).getLambda());
            for(EdgeBase e : ((IntermediateNode) node).getEdges()) {
                s += String.format("%s\t<%s, %.3f>\n", prefix, e.getText(), e.getAmbiguity());
                s += getStringTree(e.getChild(), "\t\t" + prefix);
            }
        }
        return s;

    }

    /**
     *
     * @param model
     */
    public static void printRules(InferenceModelSpec model) {
        for (DecisionSet s : model.getDecisionSets()) {
            for(DecisionNode leaf : s.getDecisionNodes()) {
                printRule(leaf);
            }
        }
    }
    
    private static void printRule(DecisionNode leaf) {
        List<String> conditions = new ArrayList<>();
        fdt.core.Node node2 = leaf.getParent();
        fdt.core.Node node = leaf;
        while(node2 != null) {
            conditions.add(0, String.format("%s IS %s", node2.getText(), node.getEdge().getText()));
            node = node2;
            node2 = node2.getParent();
        }
        String cond = String.join(" AND ", conditions);
        
        System.out.println(String.format("IF %s THEN %s [%.3f, %.3f]", cond, leaf.getText(), leaf.getSupport(), ((IntermediateNode) leaf.getParent()).getAmbiguity()));
    }

    /**
     *
     * @param confusionMatrix
     * @return
     */
    public static double printClassificationReport(HashMap<String, HashMap<String, Integer>> confusionMatrix) {
        System.out.println(String.format("\t\t\t%s", String.join("\t", confusionMatrix.keySet())));

        confusionMatrix.keySet().stream().map((c) -> {
            System.out.println(String.format("%s\t\t:", c));
            return c;
        }).map((c) -> {
            confusionMatrix.keySet().forEach((c2) -> {
                System.out.print(String.format("\t%d\t", confusionMatrix.get(c).get(c2)));
            });
            System.out.print(String.format("\t%d\t", confusionMatrix.get(c).get("UnKnown")));

            return c;
        }).forEachOrdered((__) -> {
            System.out.println("");
        });

        double sensitivity = (confusionMatrix.get("Class0").get("Class0") * 1.0) / ((double) confusionMatrix.get("Class0").get("Class0") + confusionMatrix.get("Class0").get("Class1"));
        double specifity = (confusionMatrix.get("Class1").get("Class1") * 1.0) / ((double) confusionMatrix.get("Class1").get("Class1") + confusionMatrix.get("Class1").get("Class0"));

        double npv = (confusionMatrix.get("Class0").get("Class0") * 1.0) / ((double) confusionMatrix.get("Class0").get("Class0") + confusionMatrix.get("Class1").get("Class0"));
        double accuracy = (confusionMatrix.get("Class0").get("Class0") * 1.0 + confusionMatrix.get("Class1").get("Class1"))
                / ((double) confusionMatrix.get("Class0").get("Class0")
                + confusionMatrix.get("Class1").get("Class1")
                + confusionMatrix.get("Class1").get("Class0")
                + confusionMatrix.get("Class0").get("Class1"));

        double precision = (confusionMatrix.get("Class1").get("Class1") * 1.0) / ((double) confusionMatrix.get("Class1").get("Class1") + confusionMatrix.get("Class0").get("Class1"));

        double hm = 2 * (specifity * sensitivity) / (specifity + sensitivity);

        DecimalFormat df = new DecimalFormat("0.000");
        System.out.println(String.format("Accuracy: %s", df.format(accuracy)));
        System.out.println(String.format("Specifity: %s", df.format(specifity)));
        System.out.println(String.format("Sensitivity: %s", df.format(sensitivity)));
        System.out.println(String.format("HM: %s", df.format(hm)));
        System.out.println(String.format("Precision: %s", df.format(precision)));
        System.out.println(String.format("Negative Predicted Value (NPV): %s", df.format(npv)));
        return hm;

    }

    /**
     *
     * @param dataset
     * @param inferenceEngine
     * @return
     */
    public static double evaluate(Dataset dataset, InferenceEngine inferenceEngine) {

        Attribute targetAttr = dataset.getTarget();

        HashMap<String, HashMap<String, Integer>> confusionMatrix = new HashMap<>();

        targetAttr.getTerms().stream().map((term1) -> {
            HashMap<String, Integer> entry = new HashMap<>();
            confusionMatrix.put(term1.getName(), entry);
            return entry;
        }).forEachOrdered((entry) -> {
            targetAttr.getTerms().forEach((term2) -> {
                entry.put(term2.getName(), 0);
            });
            entry.put("UnKnown", 0);
        });

        //IntStream.range(0, dataset.getNumberOfRows()).parallel().forEach(i ->{
        
        for (int i = 0; i < dataset.getNumberOfRows(); i++) {
            HashMap<String, Double> inputSpec = new HashMap<>();
            for (Attribute att : dataset.getInputs()) {
                for (Term term : att.getTerms()) {
                    String k = String.format("%s.%s", att.getName(), term.getName());
                    inputSpec.put(k, term.get(i));
                }
            }

            PredictionResult res = inferenceEngine.predict(inputSpec);
            int classIndex = (int) targetAttr.get(i);
            String expectedClass = targetAttr.getTerm(classIndex).getName();
            HashMap<String, Integer> map = confusionMatrix.get(expectedClass);
            int v = map.get(res.getClassName());
            confusionMatrix.get(expectedClass).put(res.getClassName(), v + 1);
        //});
        
        }
        return printClassificationReport(confusionMatrix);
    }

    /**
     *
     * @param x
     * @param i
     * @param j
     */
    public static void swap(int[] x, int i, int j) {
        int s = x[i];
        x[i] = x[j];
        x[j] = s;
    }

    /**
     *
     * @param x
     * @param i
     * @param j
     */
    public static void swap(float[] x, int i, int j) {
        float s = x[i];
        x[i] = x[j];
        x[j] = s;
    }

    /**
     *
     * @param x
     * @param i
     * @param j
     */
    public static void swap(double[] x, int i, int j) {
        double s = x[i];
        x[i] = x[j];
        x[j] = s;
    }

    /**
     *
     * @param x
     * @param i
     * @param j
     */
    public static void swap(String[] x, int i, int j) {
        String s = x[i];
        x[i] = x[j];
        x[j] = s;
    }

    /**
     *
     * @param coefficients
     * @return
     */
    public static Complex_F64[] findRoots(double[] coefficients) {
        int n = coefficients.length - 1;

        // Construct the companion matrix
        DMatrixRMaj c = new DMatrixRMaj(n, n);

        double a = coefficients[n];
        for (int i = 0; i < n; i++) {
            c.set(i, n - 1, -coefficients[i] / a);
        }
        for (int i = 1; i < n; i++) {
            c.set(i, i - 1, 1);
        }

        // use generalized eigenvalue decomposition to find the roots
        EigenDecomposition_F64<DMatrixRMaj> evd = DecompositionFactory_DDRM.eig(n, false);

        evd.decompose(c);

        Complex_F64[] roots = new Complex_F64[n];

        for (int i = 0; i < n; i++) {
            roots[i] = evd.getEigenvalue(i);
        }

        return roots;
    }
    
    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double cosineSimilarity(double [] a, double [] b) {
        double s1 = 0, s2 = 0, s3 = 0;
        
        for(int i = 0; i < a.length; i++) {
            s1 += a[i] * b[i];
            s2 += a[i] * a[i];
            s3 += b[i] * b[i];
        }
        
        return s1 / (Math.sqrt(s2) * Math.sqrt(s3));
    }
    
    /**
     *
     * @param a
     * @param b
     * @return
     */
    public static double getNormalizedSimilarity(double [] a, double [] b) {
        double s = cosineSimilarity(a, b);
        s += 1;
        s /= 2;
        return s;
    }
    
    /**
     *
     * @param vec
     * @param numberOfSamples
     * @param scales
     * @param offsets
     * @param sampleAroundInstance
     * @return
     */
    public double [][] getSamples(double [] vec, int numberOfSamples, double [] scales, double [] offsets, boolean sampleAroundInstance) {
        
        
        double [][] samples = new double[numberOfSamples][vec.length];
        
        double [] weights = new double[numberOfSamples];
        
        for(int i = 0; i < numberOfSamples; i++) {
            for(int j = 0; j < vec.length; j++) {
                double v = RandomFactory.nextGaussian() * scales[j];
                v += sampleAroundInstance ? vec[j] : offsets[j];
                samples[i][j] = v;
            }
            double w = getNormalizedSimilarity(vec, samples[i]);
            weights[i] = w;
        }
        
        
        
        return samples;
    }
}
