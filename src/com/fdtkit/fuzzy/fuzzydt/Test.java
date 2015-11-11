/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fdtkit.fuzzy.fuzzydt;

import com.fdtkit.fuzzy.data.Attribute;
import com.fdtkit.fuzzy.data.Dataset;
import com.fdtkit.fuzzy.data.Row;

/**
 *
 * @author Mohammed
 */
public class Test {
    public static void main(String[] args) {
        //Create a dataset
        Dataset d = new Dataset("Sample1");
        
        // Add the attributes with Linguistic terms
        d.addAttribute(new Attribute("Outlook", new String[] {"Sunny", "Cloudy", "Rain"}));
        d.addAttribute(new Attribute("Temprature", new String[] {"Hot", "Mild", "Cool"}));
        d.addAttribute(new Attribute("Humidity", new String[] {"Humid", "Normal"}));
        d.addAttribute(new Attribute("Wind", new String[] {"Windy", "Not_Windy"}));
        d.addAttribute(new Attribute("Plan", new String[] {"Volleyball", "Swimming", "W_lifting"}));
        
        

        
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.9, 0.1, 0.0}, {1.0, 0.0, 0.0}, {0.8, 0.2}, {0.6, 0.4}, {0.0, 0.8, 0.2  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.8, 0.2, 0.0}, {0.6, 0.4, 0.0}, {0.0, 1.0}, {0.5, 0.5}, {1.0, 0.7, 0.0  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.0, 0.7, 0.3}, {0.8, 0.2, 0.0}, {0.1, 0.9}, {0.6, 0.4}, {0.1, 0.8, 0.1  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.2, 0.7, 0.1}, {0.3, 0.7, 0.0}, {0.2, 0.8}, {0.4, 0.6}, {0.9, 0.1, 0.0  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.0, 0.1, 0.9}, {0.7, 0.3, 0.0}, {0.5, 0.5}, {0.5, 0.5}, {0.0, 0.8, 0.5  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.0, 0.7, 0.3}, {0.0, 0.3, 0.7}, {0.7, 0.3}, {0.7, 0.3}, {0.2, 0.0, 0.8  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 0.0, 0.3, 0.7}, {0.0, 0.0, 1.0}, {0.0, 1.0}, {0.5, 0.5}, {0.0, 0.2, 1.0  }}));
        d.addRow(new Row(new Object[]{"Dummy", "Dummy", "Dummy", "Dummy", "Dummy"}, new double[][] {{ 1.0, 0.0, 0.0}, {0.5, 0.5, 0.0}, {0.2, 0.8}, {0.6, 0.4}, {0.8, 0.6, 0.0  }}));
        
        GFID3 gfid3 = new GFID3(0.5);
        System.out.println(gfid3.getFuzzyPartitionEntropy(d, "Plan", "Outlook"));
        System.out.println(gfid3.getFuzzyPartitionEntropy(d, "Plan", "Temprature"));
        System.out.println(gfid3.getFuzzyPartitionEntropy(d, "Plan", "Wind"));
        
        System.out.println("***************");
        
        System.out.println(gfid3.getFuzzyPartitionEntropy(d, "Plan", "Outlook", new String[] {"Temprature", "Hot"}));
        System.out.println(gfid3.getFuzzyPartitionEntropy(d, "Plan", "Wind", new String[] {"Temprature", "Hot"}));
    }
}
