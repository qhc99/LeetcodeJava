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
}
