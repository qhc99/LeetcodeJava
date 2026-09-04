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
     * #2307
     * 
     * @param equations
     * @param values
     * @return
     */
    public boolean checkContradictions(List<List<String>> equations,
            double[] values) {
        var set = new DSet();
        for (var eq : equations)
            for (var s : eq)
                set.add(s);
        set.init();
        for (int i = 0; i < equations.size(); i++) {
            var eq = equations.get(i);
            if (!set.tryUnion(eq.get(0), eq.get(1), values[i])) {
                return true;
            }
        }
        return false;
    }

    static class DSet {

        List<Integer> arr = new ArrayList<>();
        List<Integer> rank = new ArrayList<>();
        Map<String, Integer> id = new HashMap<>();
        double[][] weight;

        void add(String s) {
            if (!id.containsKey(s)) {
                id.put(s, arr.size());
                arr.add(arr.size());
                rank.add(0);
            }
        }

        void init() {
            weight = new double[arr.size()][arr.size()];
            for (var r : weight)
                Arrays.fill(r, 1);
        }

        int find(int i) {
            if (arr.get(i).equals(i)) {
                return i;
            }
            var p = arr.get(i);
            var root = find(p);
            weight[i][root] = weight[i][p] * weight[p][root];
            arr.set(i, root);
            return root;
        }

        boolean tryUnion(String a, String b, double v) {
            int i = id.get(a), j = id.get(b);
            int pi = find(i), pj = find(j);
            if (pi != pj) {
                if (rank.get(pi).compareTo(rank.get(pj)) >= 0) {
                    arr.set(pj, pi);
                    // a/r1 b/r2 a/b=v
                    weight[pj][pi] = weight[i][pi] / (weight[j][pj] * v);
                    if (rank.get(pi).equals(rank.get(pj)))
                        rank.set(pi, rank.get(pi) + 1);
                } else {
                    arr.set(pi, pj);
                    weight[pi][pj] = weight[j][pj] * v / weight[i][pi];
                }

                return true;
            } else
                return Math.abs(v - weight[i][pi] / weight[j][pi]) < Math
                        .pow(10, -6);

        }
    }

    /**
     * #2333
     * 
     * @param nums1
     * @param nums2
     * @param k1
     * @param k2
     * @return
     */
    public long minSumSquareDiff(int[] nums1, int[] nums2, int k1, int k2) {
        int[] diff = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            diff[i] = Math.abs(nums1[i] - nums2[i]);
        }
        Arrays.sort(diff);
        int k = k1 + k2;
        int[] count = new int[1_00000 + 1];
        for (var d : diff)
            count[d]++;
        for (int n = count.length - 1; n >= 1 && k > 0; n--) {
            if (count[n] > 0) {
                var d = Math.min(k, count[n]);
                count[n] -= d;
                count[n - 1] += d;
                k -= d;
            }
        }
        long res = 0;
        for (int n = 1; n < count.length; n++) {
            res += (long) n * n * count[n];
        }
        return res;
    }

    /**
     * #2334
     * 
     * @param nums
     * @param threshold
     * @return
     */
    public int validSubarraySize(int[] nums, int threshold) {
        int[] left = new int[nums.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[i] <= nums[stack.peek()]) {
                var n = stack.pop();
                var len = i - left[n];
                if (nums[n] > threshold / len) {
                    return len;
                }
            }
            if (!stack.isEmpty())
                left[i] = stack.peek() + 1;
            stack.add(i);
        }
        while (!stack.isEmpty()) {
            var n = stack.pop();
            var len = nums.length - left[n];
            if (nums[n] > threshold / len) {
                return len;
            }
        }
        return -1;
    }

    /**
     * #2335
     * 
     * @param grid
     * @param k
     * @return
     */
    public int numberOfPaths(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        int[][][] dp = new int[m][n][k];
        int mod = 1_000_000_007;
        dp[0][0][grid[0][0] % k]++;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int t = 0; t < k; t++) {
                    // ? + grid[i][j] = t;
                    var tt = t - grid[i][j];
                    tt %= k;
                    if (tt < 0)
                        tt += k;
                    if (i - 1 >= 0) {
                        var up = dp[i - 1][j];
                        dp[i][j][t] += up[tt];
                        dp[i][j][t] %= mod;
                    }
                    if (j - 1 >= 0) {
                        var left = dp[i][j - 1];
                        dp[i][j][t] += left[tt];
                        dp[i][j][t] %= mod;
                    }
                }
            }
        }

        return dp[m - 1][n - 1][0];
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
     * #2347
     * 
     * @param ranks
     * @param suits
     * @return
     */
    public String bestHand(int[] ranks, char[] suits) {
        int[] suitCount = new int['d' - 'a' + 1];
        int[] rankCount = new int[14];
        for (var r : ranks)
            rankCount[r]++;
        for (var s : suits)
            suitCount[s - 'a']++;
        if (Arrays.stream(suitCount).anyMatch(i -> i == 5)) {
            return "Flush";
        }
        if (Arrays.stream(rankCount).anyMatch(i -> i >= 3)) {
            return "Three of a Kind";
        }
        if (Arrays.stream(rankCount).anyMatch(i -> i == 2)) {
            return "Pair";
        }
        return "High Card";
    }

    /**
     * #2351
     * 
     * @param s
     * @return
     */
    public char repeatedCharacter(String s) {
        int[] count = new int['z' - 'a' + 1];
        for (var c : s.toCharArray()) {
            count[c - 'a']++;
            if (count[c - 'a'] == 2)
                return c;
        }
        return ' ';
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

    /**
     * #2397
     * 
     * @param matrix
     * @param numSelect
     * @return
     */
    public int maximumRows(int[][] matrix, int numSelect) {
        List<Integer> mat = new ArrayList<>();
        int res = 0;
        int cols = matrix[0].length;
        for (var l : matrix) {
            var count1 = Arrays.stream(l).filter(i -> i == 1).count();
            if (count1 == 0) {
                res++;
            } else if (count1 <= numSelect) {
                int b = 0;
                for (int i = 0; i < l.length; i++) {
                    if (l[i] == 1) {
                        b |= 1 << i;
                    }
                }
                mat.add(b);
            }
        }

        return res + visit(-1, numSelect, mat, cols);
    }

    int visit(int start, int numSelect, List<Integer> mat, int cols) {
        if (mat.isEmpty())
            return 0;
        if (numSelect == 0) {
            return (int) mat.stream().filter(b -> b == 0).count();
        }
        int res = 0;
        for (int i = start + 1; i + numSelect <= cols; i++) {
            int ii = i;
            res = Math.max(visit(i, numSelect - 1,
                    mat.stream().map(b -> b & (~(1 << ii))).toList(), cols),
                    res);
        }
        return res;
    }
}
