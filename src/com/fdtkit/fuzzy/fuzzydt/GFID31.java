/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.utils.MappingFunction;
import com.fdtkit.fuzzy.data.Dataset;
import com.fdtkit.fuzzy.utils.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mohammed
 */
public class GFID31 {
    
    private double truthLevel;

    private String className;
    
    private MappingFunction mappingFunction;
    
    public GFID31(double alpha) {
        this.truthLevel = alpha;
        mappingFunction = new MappingFunction();
    }

    public GFID31(double truthLevel, MappingFunction mappingFunction) {
        this.truthLevel = truthLevel;
        this.mappingFunction = mappingFunction;
    }
    
    

    public double getAlpha() {
        return truthLevel;
    }

    public void setAlpha(double alpha) {
        this.truthLevel = alpha;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    
    public TreeNode buildTree(Dataset dataset, String className) {
        String[] attrs = new String[dataset.getAttributesCount() - 1];
        
        for(int i = 0; i < dataset.getAttributesCount(); i++) {
            if(!dataset.getAttribute(i).getName().equals(className)) {
                attrs[i] = dataset.getAttribute(i).getName();
            }
        }
        
        return growTree(dataset, null, attrs, className);        
    } 
    
    
    private TreeNode growTree(Dataset dataset, String[] args, String[] attrs, String className) {
        if(attrs.length == 0) {
            return new TreeNode(NodeType.LEAF, "UnKnown");
        }
        if(args == null) {
            double mfpe = Double.MAX_VALUE;
            String bestAttr = "";
            for(String atr : attrs) {
                double fpe = this.getFuzzyPartitionEntropy(dataset, className, atr);
                if(fpe < mfpe) {
                    mfpe = fpe;
                    bestAttr = atr;
                }
            }
            
            TreeNode root = new TreeNode(NodeType.ATTRIBUTE, bestAttr);
            List<String> terms = dataset.getAttribute(bestAttr).getLinguisticTerms();
            List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
            for(String term : terms) {
                boolean canBelongeToClass = false;
                String bestClass = "";
                double bestDot = Double.MIN_VALUE;
                for (String classTerm : classTerms) {
                    double dot = degreeOfClassificationTruth(dataset, new String[] {bestAttr, term, className, classTerm });
                    if (dot > bestDot) {
                        bestDot = dot;
                        bestClass = classTerm;
                    }
                }
                if(bestDot >= truthLevel) {
                    TreeNode c = new TreeNode(NodeType.VALUE, term);
                    TreeNode c2 = new TreeNode(NodeType.LEAF, bestClass);
                    c2.setValue(bestDot);
                    c.addChild(c2);
                    root.addChild(c);
                    canBelongeToClass = true;
                }                
                
                if(!canBelongeToClass) {
                    //Remove the best attribute
                    String[] newAttrs = new String[attrs.length - 1];
                    for(int j = 0, k = 0; j < attrs.length; j++) {
                        if(!attrs[j].equals(bestAttr)) {
                            newAttrs[k++] = attrs[j];
                        }
                    }

                    //get the new args
                    String[] newArgs = new String[2];
                    newArgs[0] = bestAttr;
                    newArgs[1] = term;
                    
                    TreeNode node = new TreeNode(NodeType.VALUE, term);
                    root.addChild(node);
                    TreeNode c = growTree(dataset, newArgs, newAttrs, className);
                    node.addChild(c);
                   
                }
            }
            return root;
                    
        }
        else {
            String bestAttr = "";
            if(attrs.length > 1) {
                String[] args1 = new String[args.length + 1];
                System.arraycopy(args, 0, args1, 0, args.length);
                args1[args.length] = className;

                double fpe = Double.MAX_VALUE;
                
                for(String atr : attrs) {
                    String[] args2 = new String[args1.length + 1];
                    System.arraycopy(args1, 0, args2, 1, args1.length);
                    args2[0] = atr;
                    
                    double fpe2 = this.getFuzzyPartitionEntropy(dataset, className, atr, args);

                    if(fpe2 < fpe) {
                        bestAttr = atr;
                        fpe = fpe2;
                    }
                    
                }
                if("".equals(bestAttr)) {
                    args1 = new String[args.length + 2];
                    System.arraycopy(args, 0, args1, 0, args.length);
                    args1[args.length] = className;
                    List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
                    String bestClass = "";
                    double bestDOT = -1;
                    for (String classTerm : classTerms) {
                        args1[args.length + 1] = classTerm;                    
                        double dot = degreeOfClassificationTruth(dataset, args1);
                        if(dot > bestDOT) {
                            bestDOT = dot;
                            bestClass = classTerm;
                        }
                    }
                    TreeNode c2 = new TreeNode(NodeType.LEAF, bestClass);
                    c2.setValue(bestDOT);
                    return c2;
                }
                else {
                    TreeNode root = new TreeNode(NodeType.ATTRIBUTE, bestAttr);
                    List<String> terms = dataset.getAttribute(bestAttr).getLinguisticTerms();
                    List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
                    String [] args2 = new String[args.length + 4];
                    System.arraycopy(args, 0, args2, 2, args.length);
                    args2[0] = bestAttr;
                    args2[args2.length - 2] = className;

                    for(String term : terms) {
                        boolean canBelongeToClass = false;
                        args2[1] = term;
                        for (String classTerm : classTerms) {
                            args2[args2.length - 1] = classTerm;
                            double dot = degreeOfClassificationTruth(dataset, args2);
                            if (dot > this.truthLevel) {
                                TreeNode c = new TreeNode(NodeType.VALUE, term);
                                TreeNode c2 = new TreeNode(NodeType.LEAF, classTerm);
                                c2.setValue(dot);
                                c.addChild(c2);
                                root.addChild(c);
                                canBelongeToClass = true;
                                break;
                            }                         
                        }

                        if(!canBelongeToClass) {
                            //Remove the best attribute
                            String[] newAttrs = new String[attrs.length - 1];
                            for(int j = 0, k = 0; j < attrs.length; j++) {
                                if(!attrs[j].equals(bestAttr)) {
                                    newAttrs[k++] = attrs[j];
                                }
                            }
                            //get the new args
                            String[] newArgs = new String[args.length + 2];
                            System.arraycopy(args, 0, newArgs, 2, args.length);
                            newArgs[0] = bestAttr;
                            newArgs[1] = term;

                            TreeNode node = new TreeNode(NodeType.VALUE, term);
                            root.addChild(node);
                            TreeNode c = growTree(dataset, newArgs, newAttrs, className);
                            if(c != null) { 
                                node.addChild(c);
                            }

                        }            

                    } 

                    return root;
                }
            }
            else {
                bestAttr = attrs[0];
                TreeNode root = new TreeNode(NodeType.ATTRIBUTE, bestAttr);
                List<String> terms = dataset.getAttribute(bestAttr).getLinguisticTerms();
                List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
                String [] args2 = new String[args.length + 4];
                System.arraycopy(args, 0, args2, 2, args.length);
                args2[0] = bestAttr;
                args2[args2.length - 2] = className;
                
                terms.stream().map((term) -> {
                    args2[1] = term;
                    return term;
                }).forEach((term) -> {
                    double maxTruth = Double.MIN_VALUE;
                    String bestClass = "";
                    for (String classTerm : classTerms) {
                        args2[args2.length - 1] = classTerm;
                        double dot = degreeOfClassificationTruth(dataset, args2);
                        if (dot > maxTruth) {
                            maxTruth = dot;
                            bestClass = classTerm;
                        }                    
                    }
                    
                    TreeNode node = new TreeNode(NodeType.VALUE, term);
                    root.addChild(node);
                    TreeNode c = new TreeNode(NodeType.LEAF, bestClass);
                    
                    c.setValue(maxTruth);
                    node.addChild(c);
                });
                return root;
            }

            
        }            
    }
    
    public double getFuzzyPartitionEntropy(Dataset dataset, String className, String attrName) {

        List<String> terms = dataset.getAttribute(attrName).getLinguisticTerms();
        List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
        double mi = 0;
        double [] mij = new double[terms.size()];
        double [] entropies = new double[terms.size()];
        int i = 0;
        for(String term : terms) {
            double [] vals = dataset.getAttribute(attrName).getFuzzyValues(term);
            vals = mappingFunction.map(vals);
            mij[i] = Utils.sum(vals);
            
            double [] mijk = new double[classTerms.size()];
            int j = 0;
            for(String ck : classTerms) {
                double [] vals2 = dataset.getAttribute(className).getFuzzyValues(ck);
                vals2 = mappingFunction.map(vals2);
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

    // Claculate fuzzy partition entropy
    public double getFuzzyPartitionEntropy(Dataset dataset, String className, String attrName, String []evidence) {
        List<String> terms = dataset.getAttribute(attrName).getLinguisticTerms();
        List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
        
        double [] a = null;
        for(int i = 0; i < evidence.length - 1; i += 2) {
            if(a == null) {
                a = dataset.getAttribute(evidence[i]).getFuzzyValues(evidence[i + 1]);
                a = mappingFunction.map(a);
            }
            else {
                double [] aPrime = dataset.getAttribute(evidence[i]).getFuzzyValues(evidence[i + 1]);
                aPrime = mappingFunction.map(aPrime);
                for(int j = 0; j < aPrime.length; j++) {
                    a[j] = Math.min(a[j], aPrime[j]);
                }
            }
        }
        
        double [] mij = new double[terms.size()];
        double [] entropies = new double[terms.size()];
        int i = 0;
        for(String term : terms) {
            double [] vals = dataset.getAttribute(attrName).getFuzzyValues(term);
            vals = mappingFunction.map(vals);
            vals = Utils.min(a, vals);
            mij[i] = Utils.sum(vals);
            
            double [] mijk = new double[classTerms.size()];
            int j = 0;
            for(String ck : classTerms) {
                double [] vals2 = dataset.getAttribute(className).getFuzzyValues(ck);
                vals2 = mappingFunction.map(vals2);
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
    
   
    
    public double degreeOfClassificationTruth(Dataset dataset, String [] args) {
        if(args.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid arguments. ");
        }
        
        double [] a = dataset.getAttribute(args[0]).getFuzzyValues(args[1]); 
        a = mappingFunction.map(a);
        int n = a.length;
        
        for(int i = 0; i < n; i++) {
            for(int j = 2; j < args.length - 2; j += 2) {
                double v = mappingFunction.map(dataset.getAttribute(args[j]).getFuzzyValue(i, args[j + 1])); 
                
                a[i] = Math.min(v, a[i]);
            }
        }
        double[] b = dataset.getAttribute(args[args.length - 2]).getFuzzyValues(args[args.length - 1]);      
        return Utils.subSetHood(a, b);
    } 
    
    public void printTree(TreeNode root, String tabs) {
        if(root.getNodeType() == NodeType.ATTRIBUTE ) {
            System.out.println(tabs + "|" + root.getTitle() + "|");
        }
        List<TreeNode> children = root.getChildren();
        for(int i =0; i < root.getChildrenCount(); i++) {
            TreeNode node = children.get(i);
            if(node.isLeaf()) {
                System.out.println(tabs + "\t\t" + "[" + node.getTitle() + "](" + String.format("%.2f", node.getValue()) + ")");
            }
            else if(node.getNodeType() == NodeType.VALUE) {
                System.out.println(tabs + "\t" + "<" + node.getTitle() + ">");
                printTree(node, "\t" + tabs);
            }
            else {
                printTree(node, "\t" + tabs);
            }
            
            
        }
    }
    
    public void saveTreeToFile(TreeNode root, String tabs, String fileName) {
        FileWriter fw = null;
        try {
            String s = getStringTree(root, tabs);
            File f = new File(fileName);
            fw = new FileWriter(f);
            fw.write(s, 0, s.length());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FuzzyDecisionTree.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FuzzyDecisionTree.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(FuzzyDecisionTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
    } 
    
    private String getStringTree(TreeNode root, String tabs) {
        String s = "";
        if(root.getNodeType() == NodeType.ATTRIBUTE ) {
            s += (tabs + "|" + root.getTitle() + "|\r\n");
        }
        List<TreeNode> children = root.getChildren();
        
        for(int i =0; i < root.getChildrenCount(); i++) {
            TreeNode node = children.get(i);
            if(node.isLeaf()) {
                s+=(tabs + "\t\t" + "[" + node.getTitle() + "](" + String.format("%.2f", node.getValue()) + ")") + "\r\n";
            }
            else if(node.getNodeType() == NodeType.VALUE) {
                s += tabs + "\t" + "<" + node.getTitle() + ">\r\n";
                s += getStringTree(node, "\t" + tabs);
            }
            else {
                s+= getStringTree(node, "\t" + tabs);
            }
            
            
        }
        return s;
    }    
     public String[] generateRules(TreeNode node) {

        String rules = generateRules(node, "");
        return rules.substring(0, rules.length() - 1).split("\n");            
        
    }
    
    private String generateRules(TreeNode node, String prefix) {
        if(node == null) {
            return null;
        }
        if(node.isLeaf()) {
            return String.format("%sTHEN %s (%.2f)\n" , prefix, node.getTitle(), node.getValue());
        }
        else if(node.isRoot()) {
            String rules = "";
            List<TreeNode> childs = node.getChildren();
            for(TreeNode child : childs) {
                rules += generateRules(child.getChildren().get(0), String.format("IF %s IS %s ", node.getTitle(), child.getTitle()));
            }
            return rules;
        }
        else if(node.getNodeType() == NodeType.ATTRIBUTE) {
            String rules = "";
            List<TreeNode> childs = node.getChildren();
            for(TreeNode child : childs) {
                rules += generateRules(child.getChildren().get(0), prefix + String.format("AND %s IS %s ", node.getTitle(), child.getTitle()));
            }
            return rules;                       
        }
        return "";
    }    
    
    public String simplifyRules() {
        return null;
    }
    
    public String simplifyRule(Dataset dataset, String rule, String className) {
        
        String terms = rule.substring(3, rule.indexOf(" THEN")).trim();
        String cls = rule.substring(rule.indexOf("THEN") + 5, rule.indexOf("(")).trim();
        
        String[] condations = terms.split(" AND ");
        String [] evidinces = new String[condations.length * 2 + 2];
        int i = 0;
        for(int k = 0; k < condations.length; i+=2, k++) {
            String[] condation = condations[k].split(" IS ");
            evidinces[i] = condation[0];
            evidinces[i + 1] = condation[1];
        }
        evidinces[i] = className;
        evidinces[i + 1] = cls;

        
        double dot = degreeOfClassificationTruth(dataset, evidinces);
        double dot2 = dot;
        boolean isSimplified = false;
        while((dot >= this.truthLevel && dot >= dot2) && evidinces.length >= 6) {
            String[] newEvidinces = new String[evidinces.length - 2];
            newEvidinces[newEvidinces.length - 2] = evidinces[evidinces.length - 2];
            newEvidinces[newEvidinces.length - 1] = evidinces[evidinces.length - 1];
            i = 0;
            for(; i < evidinces.length - 2; i+=2) {
                for(int k = 0, j = 0; k < evidinces.length - 2; k+=2) {
                    if(k != i) {
                        newEvidinces[j] = evidinces[k];
                        newEvidinces[j + 1] = evidinces[k + 1];
                        j += 2;
                    }
                }
                
                dot = degreeOfClassificationTruth(dataset, newEvidinces);
                if(dot > this.truthLevel && dot > dot2) {
                    isSimplified = true;
                    break;
                }
            }            
            
            if(isSimplified) {
                evidinces = newEvidinces;
            }
            else
                break;
        } 
        if(isSimplified) {
            String newRule = "IF " + evidinces[0] + " IS " + evidinces[1];
            for (i = 2; i < evidinces.length - 2; i+= 2) {
                newRule += " AND " + evidinces[i] + " IS " + evidinces[i + 1];
            }
            newRule += " THEN " + cls + String.format(" (%.2f)", dot);
        
            return newRule;
        }
        else {
            return rule;
        }
    }
    
    public double[] classify(int inputIdx, Dataset dataset, String className, String[] rules) {
        List<String> classTerms = dataset.getAttribute(className).getLinguisticTerms();
        double[] classVals = new double[classTerms.size()];
        for(String rule : rules) {
            String terms = rule.substring(3, rule.indexOf(" THEN")).trim();
            String cls = rule.substring(rule.indexOf("THEN") + 5, rule.indexOf("(")).trim();
            int classIdx = dataset.getAttribute(className).getLinguisticTermIndex(cls);
            String[] condations = terms.split(" AND ");
            String [] evidinces = new String[condations.length * 2];        
            
            for(int k = 0, i = 0; k < condations.length; i+=2, k++) {
                String[] condation = condations[k].split(" IS ");
                evidinces[i] = condation[0];
                evidinces[i + 1] = condation[1];
            }     
            double v = dataset.getFuzzyValue(inputIdx, evidinces[0], evidinces[1]);
            for(int i = 2; i < evidinces.length; i+=2) {
                v = Math.min(v, dataset.getFuzzyValue(inputIdx, evidinces[i], evidinces[i + 1]));
            }   
            classVals[classIdx] = Math.max(v, classVals[classIdx]);
        }
        return classVals;
    }
   
    
}
