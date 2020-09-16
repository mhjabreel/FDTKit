
package fdt.utils;

import static fdt.utils.Utils.swap;
import java.util.stream.IntStream;

/**
 *
 * @author Najlaa Maaroof
 */
public class Random extends java.util.Random {

    /**
     *
     */
    public Random() {
    }

    /**
     *
     * @param seed
     */
    public Random(long seed) {
        super(seed);
    }    

    /**
     *
     * @param n
     * @return
     */
    public int[] getPermutation(int n) {
        int[] p = IntStream.range(0, n).toArray();
        permutate(p);
        return p;
    }

    /**
     *
     * @param a
     */
    public void permutate(int[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = i + nextInt(a.length - i);
            swap(a, i, j);
        }
    }
    
    /**
     *
     * @param a
     */
    public void permutate(float[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = i + nextInt(a.length - i);
            swap(a, i, j);
        }
    }  
    
    /**
     *
     * @param a
     */
    public void permutate(double[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = i + nextInt(a.length - i);
            swap(a, i, j);
        }
    } 
    
    /**
     *
     * @param a
     */
    public void permutate(String[] a) {
        for (int i = 0; i < a.length; i++) {
            int j = i + nextInt(a.length - i);
            swap(a, i, j);
        }
    }     
}
