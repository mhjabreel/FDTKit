
package fdt.utils;

/**
 *
 * @author Najlaa Maaroof
 * @param <T1>
 * @param <T2>
 */
public class Tuple<T1, T2> {
    private T1 first;
    private T2 second;

    /**
     *
     */
    public Tuple() {
    }

    /**
     *
     * @param first
     * @param second
     */
    public Tuple(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     *
     * @return
     */
    public T1 getFirst() {
        return first;
    }

    /**
     *
     * @return
     */
    public T2 getSecond() {
        return second;
    }

    /**
     *
     * @param first
     */
    public void setFirst(T1 first) {
        this.first = first;
    }

    /**
     *
     * @param second
     */
    public void setSecond(T2 second) {
        this.second = second;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }
    
    
}
