package Leetcode;

import java.util.*;
import java.util.stream.Collectors;

public class Leetcode1250 {

    /**
     * #1207
     * <br/>
     * <pre>
     * 输入：arr = [1,2,2,1,1,3]
     * 输出：true
     * 解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。
     * </pre>
     * @param arr int array
     * @return is unique
     */
    @SuppressWarnings("unused")
    public static boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> m = new HashMap<>();
        for(var i : arr){
            m.put(i, m.getOrDefault(i,0) + 1);
        }
        Set<Integer> counts = new HashSet<>(m.values());
        return counts.size() == m.keySet().size();
    }
}
