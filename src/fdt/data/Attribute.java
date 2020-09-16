


package fdt.data;

import fdt.fuzzy.FuzzySet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * To create a fuzzy variable, e.g. Age.
 * 
 * @author Najlaa Maaroof
 */
public class Attribute {

    /**
     * The name of the attribute.
     */
    private final String name;
    
    /**
     * The index of the attribute in the dataset.
     */
    int index;

    /**
     * The dictionary of the terms.
     */
    private final HashMap<String, Term> nameToTerm = new LinkedHashMap<>();
    
    /**
     * The list of the crisp values.
     */
    private final List<Double> crispValues = new ArrayList<>();
    
    /**
     * The list of the terms of the attribute.
     */
    private final List<Term> terms = new ArrayList<>();
    
    /**
     *
     * @param name
     */
    public Attribute(String name) {
        this.name = name;
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
     * @param termName
     * @param fuzzySet
     */
    public void addTerm(String termName, FuzzySet fuzzySet) {
        addTerm(new Term(termName, fuzzySet));
    }

    /**
     *
     * @param termName
     */
    public void addTerm(String termName) {
        addTerm(new Term(termName));
    }

    /**
     *
     * @param term
     */
    public void addTerm(Term term) {
        term.index = terms.size();
        nameToTerm.put(term.getName(), term);
        terms.add(term);
    }

    /**
     *
     * @param index
     * @return
     */
    public double get(int index) {
        return crispValues.get(index);
    }

    /**
     *
     * @param index
     * @param value
     */
    public void set(int index, double value) {
        crispValues.set(index, value);
        terms.forEach(t -> {
            t.set(index, value);
        });
    }
    
    /**
     *
     * @param rowIndex
     * @param termName
     * @return
     */
    public double get(int rowIndex, String termName) {
        return nameToTerm.get(termName).get(rowIndex);
    }

    /**
     *
     * @param rowIndex
     * @param termName
     * @param value
     */
    public void set(int rowIndex, String termName, double value) {
        nameToTerm.get(termName).set(index, value);
    }

    /**
     *
     * @param rowIndex
     * @param termIndex
     * @return
     */
    public double get(int rowIndex, int termIndex) {
        return terms.get(termIndex).get(rowIndex);
    }

    /**
     *
     * @param rowIndex
     * @param termIndex
     * @param value
     */
    public void set(int rowIndex, int termIndex, double value) {
        terms.get(termIndex).set(index, value);
    }

    /**
     *
     * @param termName
     * @return
     */
    public Term getTerm(String termName) {
        return nameToTerm.get(termName);
    }

    /**
     *
     * @param index
     * @return
     */
    public Term getTerm(int index) {
        return terms.get(index);
    }

    /**
     *
     * @param value
     */
    public void add(double value) {
        crispValues.add(value);
        terms.forEach(t -> {
            t.add(value);
        });
    }

    /**
     *
     * @return
     */
    public int getIndex() {
        return index;
    }

    /**
     *
     * @param values
     */
    public void add(double[] values) {
        double s = 0;
        for (int i = 0; i < values.length; i++) {
            terms.get(i).add(values[i], true /*fuzzified*/);
            s += values[i];
        }
        s /= values.length;
        crispValues.add(s);
    }

    /**
     *
     * @return
     */
    public int size() {
        return crispValues.size();
    }

    /**
     *
     * @return
     */
    public List<Term> getTerms() {
        return terms;
    }

    /**
     *
     * @return
     */
    public double[] getValues() {
        return crispValues.stream().mapToDouble(d -> d).toArray();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {

        return String.format("%s: [%s]", name, String.join(",", terms.stream()
                .map(t -> t.getName()).collect(Collectors.toList())));
    }

}
