
package fdt.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import fdt.utils.Random;
import fdt.utils.RandomFactory;
import fdt.utils.Tuple;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * To create a fuzzy dataset.
 * 
 * @author Mohammed Jabreel
 */
public class Dataset {

    /**
     * A dictionary of the input attributes. The key is the attribute name and the value is the attribute.
     */
    private final HashMap<String, Attribute> nameToAttribute = new LinkedHashMap<>();
    
    /**
     * List of the attributes of the dataset.
     */
    private final List<Attribute> attributes = new LinkedList<>();
    
    /**
     * A dictionary to count the number of samples of each label in the dataset.
     */
    private final HashMap<Integer, Integer> labelCounts = new HashMap<>();
    
    /**
     * To track the indices of the rows of each label.
     */
    private final HashMap<Integer, List<Integer>> labelSamples = new HashMap<>();

    /**
     * The name of the dataset, e.g., train-set.
     */
    private final String name;
    
    /**
     * The name of the attribute that represents the target, e.g., DR.
     */
    private String className;

    private final Random rng = new Random();

    /**
     *
     * @param name
     * @param className
     */
    public Dataset(String name, String className) {
        this.name = name;
        this.className = className;
    }

    /**
     *
     * @param className
     */
    public Dataset(String className) {
        this.name = UUID.randomUUID().toString();
        this.className = className;
    }

    /**
     *
     * @param attribute
     */
    public void addAttribute(Attribute attribute) {
        attribute.index = attributes.size();
        nameToAttribute.put(attribute.getName(), attribute);
        attributes.add(attribute);
    }

    /**
     *
     * @param values
     */
    public void addRow(double[] values) {

        int targetIndex = getTarget().index;
        int j = (int) values[targetIndex];
        int v = labelCounts.getOrDefault(j, 0);
        labelCounts.put(j, v + 1);
        List<Integer> l = labelSamples.getOrDefault(j, new ArrayList<>());
        l.add(getNumberOfRows());
        labelSamples.put(j, l);

        for (int i = 0; i < values.length; i++) {
            attributes.get(i).add(values[i]);
        }
    }

    /**
     *
     * @param values
     */
    public void addRow(double[][] values) {
        for (int i = 0; i < values.length; i++) {
            attributes.get(i).add(values[i]);
        }
    }

    /**
     *
     * @return
     */
    public int getNumberOfRows() {
        return attributes.get(0).size();
    }

    /**
     *
     * @return
     */
    public int getNumberOfAttributes() {
        return attributes.size();
    }

    /**
     *
     * @return
     */
    public Attribute getTarget() {
        return nameToAttribute.get(className);
    }

    /**
     *
     * @return
     */
    public List<Attribute> getInputs() {
        return nameToAttribute
                .entrySet()
                .stream()
                .filter(es -> !es.getKey().equals(className))
                .map(es -> es.getValue())
                .collect(Collectors.toList());
    }

    /**
     *
     * @param index
     * @return
     */
    public Attribute getAttribute(int index) {
        return attributes.get(index);
    }

    /**
     *
     * @param attributeName
     * @return
     */
    public Attribute getAttribute(String attributeName) {
        return nameToAttribute.get(attributeName);
    }

    /**
     *
     * @param index
     * @return
     */
    public double[] get(int index) {
        double[] values = new double[attributes.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = attributes.get(i).get(index);
        }
        return values;
    }

    /**
     *
     * @param attributeName
     * @return
     */
    public Dataset dropAttribute(String attributeName) {
        Dataset dataset = new Dataset(String.format("%s\\wo-%s", name, attributeName), className);
        attributes
                .stream()
                .filter((attribute) -> (!attribute.getName().equals(attributeName)))
                .forEachOrdered((attribute) -> {
                    dataset.addAttribute(attribute);
                });

        return dataset;
    }

    /**
     * @deprecated 
     * @param numberOfSamples
     * @return 
     */
    public Dataset bagging(int numberOfSamples) {
        Dataset dataset = new Dataset(String.format("bootstrapped-%s", UUID.randomUUID().toString()), className);

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            dataset.addAttribute(newAttr);
        });

        for (int i = 0; i < numberOfSamples; i++) {
            int j = rng.nextInt(getNumberOfRows());
            dataset.addRow(this.get(j));
        }

        return dataset;
    }

    /**
     * This function is used to perfprm the bagging step. 
     * @param samplingRate
     * @return 
     */
    public Tuple<Dataset, Dataset> bagging2(double samplingRate) {

        Dataset dataset = new Dataset(String.format("bootstrapped-%s", UUID.randomUUID().toString()), className);
        Dataset oobDataset = new Dataset(String.format("oob-%s", UUID.randomUUID().toString()), className);

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            dataset.addAttribute(newAttr);
        });

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            oobDataset.addAttribute(newAttr);
        });

        int size = this.getNumberOfRows();

        int samplingSize = (int) Math.round(size * samplingRate);
        
        double [] Q = new double[size];
        Arrays.fill(Q, 1);
        
        IntStream.range(0, size).forEach(i -> Q[i] += i);
        
        for (int i = 0; i < samplingSize; i++) {
            double p = size * RandomFactory.nextDouble();
            int idx = (int)p;
            if(p < Q[idx]) {
                dataset.addRow(this.get(idx));
            }
            else {
                oobDataset.addRow(this.get(idx));
            }
        }

        return new Tuple<>(dataset, oobDataset);
    }

    /**
     * @deprecated 
     * @param samplingRate
     * @return 
     */
    public Tuple<Dataset, Dataset> bagging(double samplingRate) {

        Dataset dataset = new Dataset(String.format("bootstrapped-%s", UUID.randomUUID().toString()), className);
        Dataset oobDataset = new Dataset(String.format("oob-%s", UUID.randomUUID().toString()), className);

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            dataset.addAttribute(newAttr);
        });

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            oobDataset.addAttribute(newAttr);
        });

        int size = labelCounts
                .entrySet()
                .stream()
                .map(e -> e.getValue())
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);

        size = (int) Math.round(size * samplingRate);

        for (int l : labelCounts.keySet()) {

            List<Integer> indexes = labelSamples.get(l);

            int[] permutation = RandomFactory.getPermutation(indexes.size());

            int[] bag = Arrays.stream(permutation).limit(size).toArray();
            int[] oob = Arrays.stream(permutation).skip(size).toArray();

            for (int i : bag) {
                int j = indexes.get(i);
                dataset.addRow(this.get(j));
            }

            for (int i : oob) {
                int j = indexes.get(i);
                oobDataset.addRow(this.get(j));
            }

        }

        return new Tuple<>(dataset, oobDataset);
    }

    /**
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * To create an empty dataset that has the same structure.
     * @param clonedName
     * @return 
     */
    public Dataset cloneWithNoRows(String clonedName) {

        if (clonedName == null) {
            clonedName = String.format("Data_%s", UUID.randomUUID().toString());
        }
        Dataset dataset = new Dataset(clonedName, className);

        attributes.stream().map((attr) -> {
            Attribute newAttr = new Attribute(attr.getName());
            attr.getTerms().forEach((term) -> {
                newAttr.addTerm(term.getName(), term.getFuzzySet());
            });
            return newAttr;
        }).forEachOrdered((newAttr) -> {
            dataset.addAttribute(newAttr);
        });

        return dataset;
    }

    /**
     *
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" {\n");
        attributes.stream().map((a) -> {
            sb.append("\t");
            sb.append(a);
            return a;
        }).forEachOrdered((_item) -> {
            sb.append(";\n");
        });
        sb.append("}");
        return sb.toString();
    }

    /**
     *
     * @return
     */
    public HashMap<String, Attribute> getNameToAttribute() {
        return nameToAttribute;
    }
    
    

}
