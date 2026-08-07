package Leetcode;

import java.util.*;

public class Leetcode2800 {
    /**
     * #2768
     * 
     * @param m
     * @param n
     * @param coordinates
     * @return
     */
    public long[] countBlackBlocks(int m, int n, int[][] coordinates) {
        long[] res = new long[5];
        res[0] = ((long) m - 1) * ((long) n - 1);
        Map<Coord, Integer> count = new HashMap<>();
        int[] dx = new int[] { 0, -1, 0, -1 };
        int[] dy = new int[] { 0, 0, -1, -1 };
        for (var c : coordinates) {
            for (int i = 0; i < 4; i++) {
                var x = c[0] + dx[i];
                var y = c[1] + dy[i];
                if (x >= 0 && y >= 0 && x < m - 1 && y < n - 1) {
                    var coord = new Coord(x, y);
                    var cc = count.getOrDefault(coord, 0);
                    res[cc]--;
                    res[cc + 1]++;
                    count.put(coord, cc + 1);
                }
            }
        }
        return res;
    }

    static record Coord(int x, int y) {
        @Override
        public final int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Coord o)
                return o.x == x && o.y == y;
            return false;
        }
    }

    /**
     * #2788
     * 
     * @param words
     * @param separator
     * @return
     */
    public List<String> splitWordsBySeparator(List<String> words,
            char separator) {
        List<String> res = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (var w : words) {
            for (var c : w.toCharArray()) {
                if (c != separator)
                    sb.append(c);
                else if (!sb.isEmpty()) {
                    res.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
            if (!sb.isEmpty()) {
                res.add(sb.toString());
                sb.delete(0, sb.length());
            }
        }
        return res;
    }

    /**
     * #2799
     * 
     * @param nums
     * @return
     */
    public int countCompleteSubarrays(int[] nums) {
        Set<Integer> totalCount = new HashSet<>();
        for (var n : nums)
            totalCount.add(n);
        Map<Integer, Integer> currentCount = new HashMap<>();
        int l = 0, res = 0;
        for (int r = 0; r < nums.length; r++) {
            currentCount.put(nums[r],
                    1 + currentCount.getOrDefault(nums[r], 0));
            while (l <= r && currentCount.size() == totalCount.size()) {
                res += nums.length - r;
                var leftNum = nums[l++];
                var prevLeftCount = currentCount.put(leftNum,
                        currentCount.get(leftNum) - 1);
                if (prevLeftCount == 1)
                    currentCount.remove(leftNum);
            }

        }
        return res;
    }
}
