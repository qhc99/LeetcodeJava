package Leetcode;

import java.util.*;

public class Leetcode3400 {

    /**
     * #3341
     * 
     * @param moveTime
     * @return
     */
    public int minTimeToReach(int[][] moveTime) {
        int m = moveTime.length, n = moveTime[0].length;
        if (m == 1 && n == 1)
            return moveTime[0][0];
        Queue<Pos> queue = new PriorityQueue<>(
                Comparator.comparing(p -> p.time));
        queue.add(new Pos(0, 0, 0));
        boolean[][] visited = new boolean[m][n];
        int[] dx = { 0, 0, 1, -1 }, dy = { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            var p = queue.poll();
            for (int i = 0; i < 4; i++) {
                int x = p.i + dx[i], y = p.j + dy[i];
                if (x >= 0 && x < m && y >= 0 && y < n && !visited[x][y]) {
                    visited[x][y] = true;
                    int t = Math.max(moveTime[x][y] + 1, p.time + 1);
                    if (x == m - 1 && y == n - 1)
                        return t;
                    queue.add(new Pos(t, x, y));
                }
            }
        }
        return -1;
    }

    static record Pos(int time, int i, int j) {
    }

    /**
     * #3342
     * 
     * @param moveTime
     * @return
     */
    public int minTimeToReach2(int[][] moveTime) {
        int m = moveTime.length, n = moveTime[0].length;
        if (m == 1 && n == 1)
            return moveTime[0][0];
        Queue<Pos2> queue = new PriorityQueue<>(
                Comparator.comparing(p -> p.time));
        queue.add(new Pos2(0, 0, 0, 0));
        boolean[][] visited = new boolean[m][n];
        int[] dx = { 0, 0, 1, -1 }, dy = { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            var p = queue.poll();
            for (int i = 0; i < 4; i++) {
                int x = p.i + dx[i], y = p.j + dy[i];
                if (x >= 0 && x < m && y >= 0 && y < n && !visited[x][y]) {
                    visited[x][y] = true;
                    int t = Math.max(moveTime[x][y], p.time) + p.step + 1;
                    if (x == m - 1 && y == n - 1)
                        return t;
                    queue.add(new Pos2(t, x, y, (p.step + 1) % 2));
                }
            }
        }
        return -1;
    }

    static record Pos2(int time, int i, int j, int step) {
    }

    /**
     * #3389
     * 
     * @param s
     * @return
     */
    public int makeStringGood(String s) {
        long res = Long.MAX_VALUE;
        int[] count = new int['z' - 'a' + 1];
        for (var c : s.toCharArray())
            count[c - 'a']++;
        for (int len = 0; len <= s.length(); len++) {
            long[][] dp = new long[2][2];

            for (int c = 0; c < 'z' - 'a' + 1; c++) {
                if (count[c] == 0 || count[c] == len) {
                    continue;
                }
                if (count[c] > len) {
                    dp[1][0] = Math.min(dp[0][0], dp[0][1]) + count[c] - len;
                    dp[1][1] = dp[1][0];
                } else {
                    dp[1][0] = Math.min(dp[0][0], dp[0][1]) + count[c];
                    int borrow = 0;
                    if (c - 1 >= 0 && count[c - 1] != len
                            && count[c - 1] != 0) {
                        borrow = (count[c - 1] > len) ? count[c - 1] - len
                                : count[c - 1];
                    }
                    dp[1][1] = Math.min(dp[0][1] + len - count[c],
                            dp[0][0] + len - count[c]
                                    - Math.min(borrow, len - count[c]));
                }
                dp[0][0] = dp[1][0];
                dp[0][1] = dp[1][1];
            }
            res = Math.min(Math.min(dp[0][0], dp[0][1]), res);
        }
        return (int) res;
    }
}