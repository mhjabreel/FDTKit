/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.data.Dataset;


/**
 *
 * @author MHJ
 */
public interface IFuzzyDescisionTree {
    
    TreeNode buildTree();
    
    String getNodeToBranch(String[] evidences, String[] attrs);
    
    boolean canBelongeToClass(String node, String label);
    
    double degreeOfClassificationTruth(String attribute, String term, String [] evidences);
    
    
}
