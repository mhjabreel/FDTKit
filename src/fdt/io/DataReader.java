
package fdt.io;

import fdt.data.Attribute;
import fdt.data.Dataset;
import fdt.fuzzy.EdgeType;
import fdt.fuzzy.SingletonFuzzySet;
import fdt.fuzzy.TrapezoidalFuzzySet;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To read the data from the CSV and the variable definition files.
 * 
 * @author Najlaa Maaroof
 */ 


public class DataReader {

    private Dataset dummyDataset;
    
    /**
     *
     * @param varsFile
     */
    public DataReader(String varsFile) {
        parseVars(varsFile);
        
    }
    
    /**
     *
     * @return
     */
    public HashMap<String, Attribute> getAttributes() {
        return dummyDataset.getNameToAttribute();
    }

    private void parseVars(String varsFile) {
        dummyDataset = new Dataset(null);
        try (BufferedReader reader = new BufferedReader(new FileReader(varsFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#LV")) {
                    String[] lvDetails = line.substring(4).split(" ");
                    if ("Target".equals(lvDetails[lvDetails.length - 1])) {
                        dummyDataset.setClassName(lvDetails[0]);
                    }

                    Attribute attribute = new Attribute(lvDetails[0]);

                    if ("Numerical".equals(lvDetails[1])) {
                        String line2 = null;
                        while ((line2 = reader.readLine()) != null) {
                            line2 = line2.trim();
                            if (";".equals(line2)) {
                                break;
                            }

                            String[] fsDetails = line2.split(" ");
                            String[] args = fsDetails[1].split(",");
                            switch (args.length) {
                                case 4:
                                    attribute.addTerm(fsDetails[0],
                                            new TrapezoidalFuzzySet(
                                                    Double.parseDouble(args[0]),
                                                    Double.parseDouble(args[1]),
                                                    Double.parseDouble(args[2]),
                                                    Double.parseDouble(args[3])
                                            ));
                                    break;
                                case 3:
                                    attribute.addTerm(fsDetails[0],
                                            new TrapezoidalFuzzySet(
                                                    Double.parseDouble(args[0]),
                                                    Double.parseDouble(args[1]),
                                                    Double.parseDouble(args[2])
                                            ));
                                    break;
                                case 2:
                                    if (fsDetails.length == 3) {

                                        if ("RE".equals(fsDetails[2])) {
                                            attribute.addTerm(fsDetails[0],
                                                    new TrapezoidalFuzzySet(
                                                            Double.parseDouble(args[0]),
                                                            Double.parseDouble(args[1]),
                                                            EdgeType.Right
                                                    ));
                                        } else if ("LE".equals(fsDetails[2])) {
                                            attribute.addTerm(fsDetails[0],
                                                    new TrapezoidalFuzzySet(
                                                            Double.parseDouble(args[0]),
                                                            Double.parseDouble(args[1]),
                                                            EdgeType.Left
                                                    ));
                                        }
                                    }
                                    break;
                                default:
                                    break;
                            }

                        }
                    } else if ("Categorical".equals(lvDetails[1])) {
                        String line2 = null;
                        while ((line2 = reader.readLine()) != null) {
                            line2 = line2.trim();
                            if (";".equals(line2)) {
                                break;
                            }

                            String[] fsDetails = line2.split(" ");
                            attribute.addTerm(fsDetails[0], 
                                    new SingletonFuzzySet(
                                            Double.parseDouble(fsDetails[1])));
                        }
                    }
                    dummyDataset.addAttribute(attribute);

                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param fileName
     * @param datasteName
     * @param withID
     * @return
     */
    public Dataset getDataset(String fileName, String datasteName, boolean withID) {
        Dataset dataset = dummyDataset.cloneWithNoRows(datasteName);
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String[] attrNames = reader.readLine().split(",");

            String line = null;
            int r = 0;
            while ((line = reader.readLine()) != null) {
                r++;
                line = line.trim();
                if (withID) {
                    double[] values = Arrays.
                            stream(line.split(","))
                            .skip(1)
                            .mapToDouble(v -> Double.parseDouble(v))
                            .toArray();

                    dataset.addRow(values);
                } else {
                    double[] values = Arrays.
                            stream(line.split(","))
                            .mapToDouble(v -> Double.parseDouble(v))
                            .toArray();
                    dataset.addRow(values);
                }

            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return dataset;
    }

    /**
     *
     * @param fileName
     * @param datasteName
     * @return
     */
    public Dataset getDataset(String fileName, String datasteName) {
        return getDataset(fileName, datasteName, false);
    }

    

    
    
}
