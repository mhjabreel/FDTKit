
package fdt.data;

import fdt.fuzzy.FuzzySet;
import fdt.fuzzy.IdentityFuzzySet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * To represent a linguistic term, e.g. Old.
 * 
 * @author Mohammed Jabreel
 */
public class Term {
    
    /**
     * The list of the fuzzy values.
     */
    private final List<Double> storage = new ArrayList<>();
    
    /**
     * The term name.
     */
    private final String name;
    
    /**
     * The fuzzy set of the term that will be used to convert the crisp value into a fuzzy membership value.
     */
    private FuzzySet fuzzySet = new IdentityFuzzySet();
    
    /**
     * The index of the term.
     */
    int index;

    /**
     *
     * @param name
     */
    public Term(String name) {
        this.name = name;
    }

    /**
     *
     * @param name
     * @param fuzzySet
     */
    public Term(String name, FuzzySet fuzzySet) {
        this.name = name;
        this.fuzzySet = fuzzySet;
    }

    /**
     *
     * @param x
     */
    public void add(double x) {
        storage.add(fuzzySet.getMembership(x));
    }  
    
    /**
     *
     * @param x
     * @param fuzzified
     */
    public void add(double x, boolean fuzzified) {
        if(!fuzzified) {
            x = fuzzySet.getMembership(x);
        }
        storage.add(x);
    }      
    
    /**
     *
     * @return
     */
    public double [] getValues() {
        return storage.stream().mapToDouble(d -> d).toArray(); 
    }
    
    /**
     *
     * @param index
     * @return
     */
    public double get(int index) {
        return storage.get(index);
    }
    
    /**
     *
     * @param index
     * @param x
     */
    public void set(int index, double x) {
        storage.set(index, fuzzySet.getMembership(x));
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
    public FuzzySet getFuzzySet() {
        return fuzzySet;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof Term) {
            return name.equals(((Term)obj).name);
        }
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.name);
        return hash;
    }
    
    
    
}
