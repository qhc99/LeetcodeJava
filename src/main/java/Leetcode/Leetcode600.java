package Leetcode;

import java.util.*;
import java.util.function.ToDoubleFunction;


@SuppressWarnings({"JavadocDeclaration"})
public class Leetcode600 {

    /**
     * #551
     *
     * @param s
     * @return
     */
    public static boolean checkRecord(String s) {
        int a = 0, l = 0;
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (c == 'A') {
                a++;
                l++;
                if (a == 2) return false;
            }
            else if (c == 'L') {
                l++;
                if (l == 3) return false;
            }
            else l = 0;
        }
        return true;
    }

    /**
     * #552
     *
     * @param n
     * @return
     */
    public static int checkRecord(int n) {
        // (0 A) P, L, LL, (1 A) P, L, LL, A
        long[] dp = new long[7];
        dp[0] = 1;
        dp[1] = 1;
        dp[6] = 1;
        int mod = 1_000_000_000 + 7;
        for (int i = 2; i <= n; i++) {
            var zero_A_P = dp[0];
            var zero_A_L = dp[1];
            var zero_A_LL = dp[2];
            var one_A_P = dp[3];
            var one_A_L = dp[4];
            var one_A_LL = dp[5];
            var one_A_A = dp[6];
            dp[0] = (zero_A_P + zero_A_L + zero_A_LL) % mod;
            dp[1] = zero_A_P;
            dp[2] = zero_A_L;
            dp[3] = (one_A_P + one_A_L + one_A_LL + one_A_A) % mod;
            dp[4] = (one_A_P + one_A_A) % mod;
            dp[5] = one_A_L;
            dp[6] = dp[0];
        }
        long ans = 0;
        for (var i : dp) {
            ans += i;
        }
        return (int) (ans % mod);
    }

    /**
     * #560
     * <br>find the count of continue sub-arrays which sum is k<br>
     * [1, 2, 3, 4], 3 ---> 2    (answer: [1, 2] and [3])
     *
     * @param nums array
     * @param k    sum target
     * @return count
     */
    public static int subarraySum(int[] nums, int k) {
        int count = 0, pre = 0;
        HashMap<Integer, Integer> mp = new HashMap<>();
        mp.put(0, 1);
        for (int num : nums) {
            pre += num;
            if (mp.containsKey(pre - k)) {
                count += mp.get(pre - k);
            }
            mp.put(pre, mp.getOrDefault(pre, 0) + 1);
        }
        return count;
    }

    /**
     * #583
     *
     * @param word1
     * @param word2
     * @return
     */
    public static int minDistance(String word1, String word2) {
        return word1.length() + word2.length() - 2 * maxCommonLen(word1, word2);
    }

    private static int maxCommonLen(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i < a.length() + 1; i++) {
            var ac = a.charAt(i - 1);
            for (int j = 1; j < b.length() + 1; j++) {
                var bc = b.charAt(j - 1);
                if (ac == bc) dp[i][j] = dp[i - 1][j - 1] + 1;
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[a.length()][b.length()];
    }

    /**
     * #587
     *
     * @param trees
     * @return
     */
    public static int[][] outerTrees(int[][] trees) {
        int n = trees.length;
        if (n < 4) {
            return trees;
        }
        int bottom = 0;
        /* 找到 y 最小的点 bottom*/
        for (int i = 0; i < n; i++) {
            if (trees[i][1] < trees[bottom][1]) {
                bottom = i;
            }
        }
        swap(trees, bottom, 0);
        /* 以 bottom 原点，按照极坐标的角度大小进行排序 */
        Arrays.sort(trees, 1, n, (a, b) -> {
            int diff = ccw(trees[0], a, b);
            if (diff == 0) {
                return distance(trees[0], a) - distance(trees[0], b);
            }
            else {
                return -diff;
            }
        });
        /* 对于凸包最后且在同一条直线的元素按照距离从大到小进行排序 */
        int r = n - 1;
        while (r >= 0 && ccw(trees[0], trees[n - 1], trees[r]) == 0) {
            r--;
        }
        for (int l = r + 1, h = n - 1; l < h; l++, h--) {
            swap(trees, l, h);
        }
        Deque<Integer> stack = new ArrayDeque<Integer>();
        stack.push(0);
        stack.push(1);
        for (int i = 2; i < n; i++) {
            int top = stack.pop();
            /* 如果当前元素与栈顶的两个元素构成的向量顺时针旋转，则弹出栈顶元素 */
            while (!stack.isEmpty() && ccw(trees[stack.peek()], trees[top], trees[i]) < 0) {
                top = stack.pop();
            }
            stack.push(top);
            stack.push(i);
        }

        int size = stack.size();
        int[][] res = new int[size][2];
        for (int i = 0; i < size; i++) {
            res[i] = trees[stack.pop()];
        }
        return res;
    }

    private static int ccw(int[] a, int[] b, int[] c) {
        // (qx - px) * (ry - py) - (by - ay) * (cx - bx);
        return (b[0] - a[0]) * (c[1] - b[1]) - (b[1] - a[1]) * (c[0] - b[0]);
    }

    private static int distance(int[] p, int[] q) {
        return (p[0] - q[0]) * (p[0] - q[0]) + (p[1] - q[1]) * (p[1] - q[1]);
    }

    private static void swap(int[][] trees, int i, int j) {
        int temp0 = trees[i][0], temp1 = trees[i][1];
        trees[i][0] = trees[j][0];
        trees[i][1] = trees[j][1];
        trees[j][0] = temp0;
        trees[j][1] = temp1;
    }

    public static int[][] outerTrees2(int[][] trees) {
        List<int[]> ps = new ArrayList<>(trees.length);
        Collections.addAll(ps, trees);
        var out = ConvexHull.GrahamScan(ps, i -> i[0], i -> i[1]);
        int[][] ans = new int[out.size()][2];
        for (int i = 0; i < out.size(); i++) {
            ans[i] = out.get(i);
        }
        return ans;
    }

    public static class ConvexHull {
        public static <E> List<E> GrahamScan(
                List<E> points,
                ToDoubleFunction<E> getX,
                ToDoubleFunction<E> getY) {
            if (points.size() <= 3) {
                return points;
            }
            var start = points.get(0);
            int start_idx = 0;
            for (var i = 0; i < points.size(); i++) {
                var p = points.get(i);
                if (getY.applyAsDouble(start) > getY.applyAsDouble(p)) {
                    start = p;
                    start_idx = i;
                }
            }

            final var final_start = start;
            swap(points, start_idx, points.size() - 1);
            points.remove(points.size() - 1);
            points.sort((a, b) -> {
                var comp_a = ccw(final_start, a, b, getX, getY);
                if (comp_a == 0) {
                    var comp_d = distance(a, final_start, getX, getY) - distance(b, final_start, getX, getY);
                    return comp_d < 0 ? -1 : comp_d > 0 ? 1 : 0;
                }
                else return -comp_a < 0 ? -1 : -comp_a > 0 ? 1 : 0;
            });
            List<E> temp = new ArrayList<>(points.size() + 1);
            temp.add(start);
            temp.addAll(points);
            points = temp;
            int s = points.size() - 1;
            while (s >= 0 && ccw(start, points.get(s), points.get(points.size() - 1), getX, getY) == 0) {
                s--;
            }
            s++;

            for (int e = points.size() - 1; s < e; e--, s++) {
                swap(points, s, e);
            }

            Deque<E> points_stack = new ArrayDeque<>();

            points_stack.addLast(points.get(0));
            points_stack.addLast(points.get(1));


            for (int i = 2; i < points.size(); i++) {
                var p = points.get(i);
                var last = points_stack.removeLast();
                while (!points_stack.isEmpty() && ccw(points_stack.getLast(), last, p, getX, getY) < 0) {
                    last = points_stack.removeLast();
                }
                points_stack.addLast(last);
                points_stack.addLast(p);
            }

            List<E> res = new ArrayList<>(points_stack.size());
            res.addAll(points_stack);
            return res;
        }

        private static <E> void swap(List<E> points, int i, int j) {
            var t = points.get(i);
            points.set(i, points.get(j));
            points.set(j, t);
        }

        private static <E> double distance(E a, E b, ToDoubleFunction<E> getX, ToDoubleFunction<E> getY) {
            double ax = getX.applyAsDouble(a);
            double ay = getY.applyAsDouble(a);

            double bx = getX.applyAsDouble(b);
            double by = getY.applyAsDouble(b);
            return Math.pow(ax - bx, 2) + Math.pow(ay - by, 2);
        }

        private static <E> double ccw(E a, E b, E c, ToDoubleFunction<E> getX, ToDoubleFunction<E> getY) {
            double ax = getX.applyAsDouble(a);
            double ay = getY.applyAsDouble(a);

            double bx = getX.applyAsDouble(b);
            double by = getY.applyAsDouble(b);

            double cx = getX.applyAsDouble(c);
            double cy = getY.applyAsDouble(c);
            // (b[0] - a[0]) * (c[1] - b[1]) - (b[1] - a[1]) * (c[0] - b[0]);
            return (bx - ax) * (cy - by) - (by - ay) * (cx - bx);
        }
    }

}
