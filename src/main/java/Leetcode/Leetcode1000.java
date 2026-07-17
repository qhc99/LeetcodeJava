package Leetcode;

import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings({"JavaDoc"})
public class Leetcode1000 {

    /**
     * #902
     *
     * @param digits
     * @param n
     * @return
     */
    public static int atMostNGivenDigitSet(String[] digits, int n) {
        var S = String.valueOf(n);
        int len_n = S.length();
        int[] dp = new int[len_n + 1]; // preprocess
        dp[len_n] = 1;
        for (int idx = len_n - 1; idx >= 0; idx--) {
            var S_digit = Integer.parseInt(String.valueOf(S.charAt(idx)));
            for (var num_s : digits) {
                var num = Integer.parseInt(num_s);
                if (num < S_digit) dp[idx] += Math.pow(digits.length, len_n - 1 - idx);
                else if (num == S_digit) dp[idx] += dp[idx + 1];
            }
        }
        for (int i = 1; i < len_n; i++) {
            dp[0] += Math.pow(digits.length, i);
        }
        return dp[0];
    }

    /**
     * @param digits
     * @param n      start from 1, radix 10
     * @return
     */
    public static int nthNum(String[] digits, int n) {
        n--;
        if (n == 0) {
            return Integer.parseInt(digits[0]);
        }
        StringBuilder num = new StringBuilder();
        while (n != 0) {
            int idx = n % digits.length;
            num.insert(0, digits[idx]);
            n -= idx;
            n = n / digits.length;
        }
        return Integer.parseInt(num.toString());
    }

    /**
     * #925
     *
     * @param name  name string
     * @param typed input string
     * @return is long press
     */
    public static boolean isLongPressedName(String name, String typed) {
        if (name.charAt(0) != typed.charAt(0)) {
            return false;
        }
        char lastChar = name.charAt(0);
        int namePtr = 1, typedPtr = 1;
        for (; typedPtr < typed.length(); typedPtr++) {
            if (namePtr < name.length()) {
                if (typed.charAt(typedPtr) == name.charAt(namePtr)) {
                    lastChar = name.charAt(namePtr);
                    namePtr++;
                }
                else {
                    if (typed.charAt(typedPtr) != lastChar) {
                        return false;
                    }
                }
            }
            else {
                if (typed.charAt(typedPtr) != lastChar) {
                    return false;
                }
            }
        }
        return namePtr == name.length();
    }
    /**
     * #968
     * <br/>监控二叉树
     * grady algorithm
     *
     * @param root tree
     * @return number of camera
     */
    public int minCameraCover(TreeNode root) {
        var solver = new MinCameraCoverSolver();
        if (solver.dfs(root) == MinCameraCoverSolver.Status.NOT_BEING_MONITORED) {
            solver.res++;
        }
        return solver.res;
    }

    private static class MinCameraCoverSolver {
        enum Status {
            NOT_BEING_MONITORED,
            BEING_MONITORED,
            CAMERA_INSTALLED
        }

        int res = 0;

        Status dfs(TreeNode tn) {
            if (tn == null) {
                return Status.BEING_MONITORED;
            }
            Status left = dfs(tn.left);
            Status right = dfs(tn.right);
            //左右子节点均已被监控，此时跳过
            if (left == Status.BEING_MONITORED && right == Status.BEING_MONITORED) {
                return Status.NOT_BEING_MONITORED;
            }
            //2种情况，1、一个子节点安装监控，另一个已被监控  2、两个子节点均有监控  此时不需要安装监控器，且该节点已被监控
            if ((left == Status.CAMERA_INSTALLED && right == Status.CAMERA_INSTALLED) ||
                    (left == Status.CAMERA_INSTALLED && right == Status.BEING_MONITORED) ||
                    (left == Status.BEING_MONITORED && right == Status.CAMERA_INSTALLED)) {
                return Status.BEING_MONITORED;
            }
            //其他情况均需要安装监控，不然会有节点监控不到
            res++;
            return Status.CAMERA_INSTALLED;
        }
    }


    /**
     * #973
     *
     * @param points
     * @param k
     * @return
     */
    public static int[][] kClosest(int[][] points, int k) {
        class Data {
            final int dist;
            final int[] point;

            Data(int d, int[] p) {
                dist = d;
                point = p;
            }
        }
        PriorityQueue<Data> queue = new PriorityQueue<>(Comparator.comparing(d -> d.dist));
        int[][] ans = new int[k][];
        for (var p : points) {
            queue.add(new Data(p[0] * p[0] + p[1] * p[1], p));
        }
        for (int i = 0; i < k; i++) {
            ans[i] = queue.poll().point;
        }

        return ans;
    }

    /**
     * #997
     *
     * @param n
     * @param trust
     * @return
     */
    public static int findJudge(int n, int[][] trust) {
        int[] in = new int[n + 1];
        int[] out = new int[n + 1];
        for (var t : trust) {
            var a = t[0];
            var b = t[1];
            out[a]++;
            in[b]++;
        }
        for (int i = 1; i <= n; i++) {
            if (in[i] == n - 1 && out[i] == 0) {
                return i;
            }
        }
        return -1;
    }
}
