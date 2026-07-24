package Leetcode;

import java.util.*;

public class Leetcode2400 {
    /**
     * #2303
     * 
     * @param brackets
     * @param income
     * @return
     */
    public double calculateTax(int[][] brackets, int income) {
        double tax = 0;
        for (int i = 0; i < brackets.length && income > 0; i++) {
            int amount = brackets[i][0];
            if (i > 0) {
                amount -= brackets[i - 1][0];
            }
            int taxAmount = Math.min(amount, income);
            income -= taxAmount;
            tax += taxAmount * brackets[i][1] / 100.;
        }
        return tax;
    }

    /**
     * #2342
     * 
     * @param nums
     * @return
     */
    public int maximumSum(int[] nums) {
        int res = -1;
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (var n : nums) {
            var q = map.computeIfAbsent(sumOfDigits(n), k -> new ArrayList<>());
            q.add(n);
            q.sort((a, b) -> b - a);
            if (q.size() > 2)
                q.removeLast();
            if (q.size() == 2)
                res = Math.max(q.stream().mapToInt(i -> i).sum(), res);
        }
        return res;
    }

    int sumOfDigits(int i) {
        int res = 0;
        while (i > 0) {
            res += i % 10;
            i /= 10;
        }
        return res;
    }

    /**
     * #2360
     * 
     * @param edges
     * @return
     */
    public int longestCycle(int[] edges) {
        int res = -1;
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> visiting = new HashMap<>();
        for (int i = 0; i < edges.length; i++) {
            res = Math.max(res, visitCycle(i, edges, 0, visited, visiting));
        }
        return res;
    }

    int visitCycle(int i, int[] edges, int depth, Set<Integer> visited,
            Map<Integer, Integer> visiting) {
        if (visited.contains(i))
            return -1;
        visiting.put(i, depth);
        int res = -1;
        var next = edges[i];
        if (next != -1) {
            var d = visiting.get(next);
            if (d != null)
                return depth + 1 - d;
            res = visitCycle(next, edges, depth + 1, visited, visiting);
        }
        visited.add(i);
        visiting.remove(i);
        return res;
    }

    /**
     * #2365
     * 
     * @param tasks
     * @param space
     * @return
     */
    public long taskSchedulerII(int[] tasks, int space) {
        long day = 1;
        Map<Integer, Long> record = new HashMap<>();
        for (var task : tasks) {
            var d = record.get(task);
            if (d != null) {
                if (day - d <= space) {
                    day += space - day + d + 1;
                }
            }
            record.put(task, day++);
        }
        return --day;
    }

    /**
     * #2373
     * 
     * @param grid
     * @return
     */
    public int[][] largestLocal(int[][] grid) {
        int n = grid.length;
        int[][] res = new int[n - 2][n - 2];
        for (int i = 1; i < n - 1; i++) {
            for (int j = 1; j < n - 1; j++) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        var x = i + dx;
                        var y = j + dy;
                        if (x >= 0 && x < n && y >= 0 && y < n) {
                            res[i - 1][j - 1] = Math.max(res[i - 1][j - 1],
                                    grid[x][y]);
                        }
                    }
                }
            }
        }
        return res;
    }
}
