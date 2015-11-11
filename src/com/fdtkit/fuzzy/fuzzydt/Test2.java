/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.data.Attribute;
import com.fdtkit.fuzzy.data.Dataset;
import com.fdtkit.fuzzy.data.Row;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Mohammed
 */
public class Test2 {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Dataset d = new Dataset("Sample1");
        
        BufferedReader br = new BufferedReader(new FileReader("data/test.txt"));
        
        String line = "";
        String className = "";
        while((line = br.readLine()) != null) {
            if(line.startsWith("#")) {
                String [] terms = line.substring(line.indexOf(":") + 1).split(" ");
                d.addAttribute(new Attribute(line.substring(1, line.indexOf(":")), terms));
            }
            else if(line.startsWith("@")) {
                String [] terms = line.substring(line.indexOf(":") + 1).split(" ");
                d.addAttribute(new Attribute(line.substring(1, line.indexOf(":") ), terms));                
                className = line.substring(1, line.indexOf(":"));
            }
            else if(line.startsWith("=ROW=")) {
                String [] data = line.split(" ");
                Object[] crispRow = new Object[d.getAttributesCount()];
                double[][] fuzzyValues = new double[d.getAttributesCount()][];
                int k = 1;
                for(int i = 0; i < crispRow.length; i++) {
                    crispRow[i] = "Dummy";
                    fuzzyValues[i] = new double[d.getAttribute(i).getLinguisticTermsCount()];
                    for(int j = 0; j < fuzzyValues[i].length; j++) {
                        fuzzyValues[i][j] = Double.parseDouble(data[k++]);
                    }
                }
                
                d.addRow(new Row(crispRow, fuzzyValues));
                
                
            }
        }
        
        br.close();
        
        GFID3 gfid3 = new GFID3(0.8, new QuadraticMappingFunction());
        
        TreeNode root = gfid3.buildTree(d, "Plan");
        
        gfid3.printTree(root, "");  
        
        String[] rules = gfid3.generateRules(root);
        for(String rule : rules) {
            System.out.println(rule);
        }
        
        System.out.println("");
        System.out.println("Simplifying rules:");
        for(String rule : rules) {
            System.out.println(gfid3.simplifyRule(d, rule, "Plan"));
        }         
        
        for(int i = 0; i < rules.length;i++) {
            rules[i] = gfid3.simplifyRule(d, rules[i], "Plan");
        }
        System.out.println("Trainingset Prediction:");
        for(int j = 0; j < 16; j++) {
            double[] cVals = gfid3.classify(j, d, "Plan", rules);
            for(int i = 0; i < cVals.length; i++) {
                System.out.print(String.format("%.2f     ", cVals[i]));
            }
            System.out.println("");
        }        
    }
}
