/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.utils;

import com.fdtkit.fuzzy.data.Dataset;
import java.util.List;

/**
 *
 * @author MHJ
 */
public class FuzzyPartitionEntropyMeasure extends MinimumPreferenceMeasure {
    
    @Override
    public double getPreferenceValue(Dataset dataset, String attribute, String[] evidances) {
        
        String className = dataset.getClassName();
        List<String> terms = dataset.getAttribute(attribute).getLinguisticTerms();
        List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
        
        double [] a = null;
        if(evidances != null) {
            for(int i = 0; i < evidances.length - 1; i += 2) {
                if(a == null) {
                    a = dataset.getAttribute(evidances[i]).getFuzzyValues(evidances[i + 1]);

                }
                else {
                    double [] aPrime = dataset.getAttribute(evidances[i]).getFuzzyValues(evidances[i + 1]);

                    for(int j = 0; j < aPrime.length; j++) {
                        a[j] = Math.min(a[j], aPrime[j]);
                    }
                }
            }
        }
        

        
        double [] mij = new double[terms.size()];
        double [] entropies = new double[terms.size()];
        int i = 0;
        for(String term : terms) {
            double [] vals = dataset.getAttribute(attribute).getFuzzyValues(term);
            if(a != null) {
                vals = Utils.min(a, vals);
            }
            
            mij[i] = Utils.sum(vals);
            
            double [] mijk = new double[classTerms.size()];
            int j = 0;
            for(String ck : classTerms) {
                double [] vals2 = dataset.getAttribute(className).getFuzzyValues(ck);
                
                vals2 = Utils.min(vals, vals2);
                
                mijk[j++] = Utils.sum(vals2);
            }
            
            Utils.normalizeWith(mijk, Utils.sum(mijk));
            entropies[i++] = Utils.entropy(mijk);
        }
        Utils.normalizeWith(mij, Utils.sum(mij));
        double s = 0;
        for(i = 0; i < mij.length; i++) {
            s += entropies[i] * mij[i];
        }
        return s;        
        
    }

    
    
}
