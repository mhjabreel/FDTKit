
package fdt.utils;

/**
 *
 * @author Najlaa Maaroof
 */
public class TestBagging {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Random random = new Random();
        int numInstances = 10;
        double sampleSize = 2.0 / 3;
        boolean representUsingWeights = false;
        double[] P = new double[10];
        for (int i = 0; i < 10; i++) {
            P[i] = 1.0 / 10.0;
        }

        double[] weights = P;
        double[] Q = new double[weights.length];
        int[] A = new int[weights.length];
        int[] W = new int[weights.length];
        int M = weights.length;
        int NN = -1;
        int NP = M;
        for (int I = 0; I < M; I++) {
            if (P[I] < 0) {
                throw new IllegalArgumentException("Weights have to be positive.");
            }
            Q[I] = M * P[I];
            if (Q[I] < 1.0) {
                W[++NN] = I;
            } else {
                W[--NP] = I;
            }
        }
        if (NN > -1 && NP < M) {
            for (int S = 0; S < M - 1; S++) {
                int I = W[S];
                int J = W[NP];
                A[I] = J;
                Q[J] += Q[I] - 1.0;
                if (Q[J] < 1.0) {
                    NP++;
                }
                if (NP >= M) {
                    break;
                }
            }
            // A[W[M]] = W[M];
        }

        for (int I = 0; I < M; I++) {
            Q[I] += I;
        }

        // Do we need to keep track of how many copies to use?
        int[] counts = null;
        if (representUsingWeights) {
            counts = new int[M];
        }

        int numToBeSampled = (int) (numInstances * (sampleSize));

        for (int i = 0; i < numToBeSampled; i++) {
            int ALRV;
            double U = M * random.nextDouble();
            int I = (int) U;
            if (U < Q[I]) {
                ALRV = I;
            } else {
                System.out.println("HERE");
                ALRV = A[I];
            }
            if (representUsingWeights) {
                counts[ALRV]++;
            } else {
                System.out.println(ALRV + ",");
            }
//      if (sampled != null) {
//        sampled[ALRV] = true;
//      }
//      if (!representUsingWeights) {
//        newData.instance(newData.numInstances() - 1).setWeight(1);
//      }
        }

        // Add data based on counts if weights should represent numbers of copies.
        if (representUsingWeights) {
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > 0) {
                    System.out.println(i);
//          newData.add(instance(i));
//          newData.instance(newData.numInstances() - 1).setWeight(counts[i]);
                }
            }
        }
    }
}
