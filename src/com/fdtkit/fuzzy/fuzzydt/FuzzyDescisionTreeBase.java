/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.data.Dataset;
import java.util.List;

/**
 *
 * @author MHJ
 */
public abstract class FuzzyDescisionTreeBase implements IFuzzyDescisionTree {

    protected double truthLevel = 0.5;
    protected String className;
    protected Dataset dataset;
    
    @Override
    public TreeNode buildTree() {
        String[] attrs = new String[dataset.getAttributesCount() - 1];
        
        for(int i = 0; i < dataset.getAttributesCount(); i++) {
            if(!dataset.getAttribute(i).getName().equals(className)) {
                attrs[i] = dataset.getAttribute(i).getName();
            }
        }
        
        return growTree(null, attrs);         
    }

    protected TreeNode growTree(Object object, String[] attrs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
