
package fdt.utils;

import java.util.HashSet;
import java.util.stream.LongStream;

/**
 *
 * @author Najlaa Maaroof
 */
public class RandomFactory {

    private static Random seedRNG = new Random();
    private static HashSet<Long> seeds = new HashSet<>();
//    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RandomFactory.class);

    private static ThreadLocal<Random> random = new ThreadLocal<Random>() {
        protected synchronized Random initialValue() {
            // For the first RNG, we use the default seed so that we can
            // get repeatable results for random algorithms.
            // Note that this may or may not be the main thread.
            long seed = 19650218L;

            // Make sure other threads not to use the same seed.
            // This is very important for some algorithms such as random forest.
            // Otherwise, all trees of random forest are same except the main thread one.
            if (!seeds.isEmpty()) {
                do {
                    seed = probablePrime(19650218L, 256, seedRNG);
                } while (seeds.contains(seed));
            }

//            logger.info(String.format("Set RNG seed %d for thread %s", seed, Thread.currentThread().getName()));
            seeds.add(seed);
            return new Random(seed);
        }
    };

    /**
     *
     */
    public static void setSeed() {
        java.security.SecureRandom sr = new java.security.SecureRandom();
        byte[] bytes = sr.generateSeed(Long.BYTES);
        long seed = 0;
        for (int i = 0; i < Long.BYTES; i++) {
            seed <<= 8;
            seed |= (bytes[i] & 0xFF);
        }

        setSeed(seed);
    }

    /**
     *
     * @param seed
     */
    public static void setSeed(long seed) {
        if (seeds.isEmpty()) {
            seedRNG.setSeed(seed);
            random.get().setSeed(seed);
            seeds.clear();
            seeds.add(seed);
        } else {
            random.get().setSeed(seed);
            seeds.add(seed);
        }
    }

    /**
     * Returns a probably prime number greater than n.
     *
     * @param n the returned value should be greater than n.
     * @param k a parameter that determines the accuracy of the primality test
     * @return 
     */
    public static long probablePrime(long n, int k) {
        return probablePrime(n, k, random.get());
    }

    /**
     * Returns a probably prime number.
     */
    private static long probablePrime(long n, int k, Random rng) {
        long seed = n + rng.nextInt(899999963); // The largest prime less than 9*10^8
        for (int i = 0; i < 4096; i++) {
            if (isProbablePrime(seed, k, rng)) {
                break;
            }
            seed = n + rng.nextInt(899999963);
        }

        return seed;
    }

    /**
     * Returns a stream of prime numbers to be used as RNG seeds.
     *
     * @param n the returned value should be greater than n.
     * @param k a parameter that determines the accuracy of the primality test
     * @return 
     */
    public static LongStream seeds(long n, int k) {
        return LongStream.generate(() -> probablePrime(n, k, seedRNG));
    }

    /**
     * Returns true if x is a power of 2.
     * @param x
     * @return 
     */
    public static boolean isPower2(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    /**
     * Returns true if n is probably prime, false if it's definitely composite.
     * This implements Miller-Rabin primality test.
     *
     * @param n an odd integer to be tested for primality
     * @param k a parameter that determines the accuracy of the test
     * @return 
     */
    public static boolean isProbablePrime(long n, int k) {
        return isProbablePrime(n, k, random.get());
    }

    /**
     * Returns true if n is probably prime, false if it's definitely composite.
     * This implements Miller-Rabin primality test.
     *
     * @param n an odd integer to be tested for primality
     * @param k a parameter that determines the accuracy of the test
     * @param rng random number generator
     */
    private static boolean isProbablePrime(long n, int k, Random rng) {
        if (n <= 1 || n == 4) {
            return false;
        }
        if (n <= 3) {
            return true;
        }

        // Find r such that n = 2^d * r + 1 for some r >= 1
        int s = 0;
        long d = n - 1;
        while (d % 2 == 0) {
            s++;
            d = d / 2;
        }

        for (int i = 0; i < k; i++) {
            long a = 2 + rng.nextLong() % (n - 4);
            long x = power(a, d, n);
            if (x == 1 || x == n - 1) {
                continue;
            }

            int r = 0;
            for (; r < s; r++) {
                x = (x * x) % n;
                if (x == 1) {
                    return false;
                }
                if (x == n - 1) {
                    break;
                }
            }

            // None of the steps made x equal n-1.
            if (r == s) {
                return false;
            }
        }
        return true;
    }

    private static long power(long x, long y, long p) {
        long res = 1;      // Initialize result
        x = x % p;  // Update x if it is more than or
        // equal to p
        while (y > 0) {
            // If y is odd, multiply x with result
            if ((y & 1) == 1) {
                res = (res * x) % p;
            }

            // y must be even now
            y = y >> 1; // y = y/2
            x = (x * x) % p;
        }
        return res;
    }
    
    /**
     *
     * @param n
     * @return
     */
    public static int[] getPermutation(int n) {
        return random.get().getPermutation(n);
    }
    
    /**
     *
     * @return
     */
    public static double nextDouble() {
        return random.get().nextDouble();
    }
    
    /**
     *
     * @return
     */
    public static double nextGaussian() {
        return random.get().nextGaussian();
    }
    
    /**
     *
     * @param scale
     * @param offset
     * @return
     */
    public static double nextGaussian(double scale, double offset) {
        return random.get().nextGaussian() * scale + offset;
    }    
}
