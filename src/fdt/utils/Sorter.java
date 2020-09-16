
package fdt.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Mohammed Jabreel
 * @param <T>
 */
public class Sorter<T> {

    /**
     *
     * @param target
     * @param source
     * @return
     */
    public List<T> sortBy(List<T> target, double[] source) {

        Tuple<Integer, Double>[] map = new Tuple[source.length];
        for (int i = 0; i < source.length; i++) {
            map[i] = new Tuple<>(i, source[i]);
        }

        Arrays.sort(map, (a, b) -> a.getSecond().compareTo(b.getSecond()));

        List<T> sortedTarget = new ArrayList<>();

        for (int i = 0; i < target.size(); i++) {
            sortedTarget.add(target.get(map[i].getFirst()));
        }

        return sortedTarget;
    }
}
