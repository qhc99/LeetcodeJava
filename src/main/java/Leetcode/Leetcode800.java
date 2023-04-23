package Leetcode;

import java.util.*;

@SuppressWarnings({ "unused", "JavaDoc" })
public class Leetcode800 {

    /**
     * #753
     * 
     * @param n
     * @param k
     * @return
     */
    public static String crackSafe(int n, int k) {
        if (n == 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < k; i++) {
                sb.append(i);
            }
            return sb.toString();
        }
        int[] biggest_edge = new int[(int) Math.pow(k, n - 1)];
        Arrays.fill(biggest_edge, k - 1);
        StringBuilder sb = new StringBuilder("0".repeat(n - 1));
        int current = 0;
        while (biggest_edge[current] != -1) {
            int edge = biggest_edge[current]--;
            sb.append(edge);
            current = (current * k) % (int) Math.pow(k, n - 1) + edge;
        }

        return sb.toString();
    }

    /**
     * max match length of prefix of P and suffix end with ith(start from 1)
     * character
     *
     * @param P string
     * @return prefix function (length = len(p) + 1)
     */
    private static int[] computePrefixFunction(String P) {
        int m = P.length();
        int[] pi = new int[m + 1];
        pi[1] = 0;
        for (int q = 1, k = 0; q < m; q++) {
            while (k > 0 && P.charAt(k) != P.charAt(q)) {
                k = pi[k];
            }
            if (P.charAt(k) == P.charAt(q)) {
                k++;
            }
            pi[q + 1] = k;
        }
        return pi;
    }

    public static boolean next(int[] arr, int k) {
        boolean remain = false;
        for (int i = arr.length - 1; i >= 0; i--) {
            arr[i]++;
            if (arr[i] == k) {
                arr[i] = 0;
                remain = true;
            } else {
                remain = false;
                break;
            }
        }
        return !remain;
    }

    private static String numArrToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (var i : arr) {
            sb.append(i);
        }
        return sb.toString();
    }

    /**
     * #763
     * <br/>
     * 划分字母区间
     * 
     * <pre>
     * 输入：S = "ababcbacadefegdehijhklij"
     * 输出：[9,7,8]
     * 解释：
     * 划分结果为 "ababcbaca", "defegde", "hijhklij"。
     * 每个字母最多出现在一个片段中。
     * 像 "ababcbacadefegde", "hijhklij" 的划分是错误的，因为划分的片段数较少。
     * </pre>
     *
     * @param S string
     * @return res
     */
    public static List<Integer> partitionLabels(String S) {
        int[] lastIndexOfChar = new int[26];
        for (int i = 0; i < S.length(); i++) {
            lastIndexOfChar[S.charAt(i) - 'a'] = i;
        }
        int startIdx = 0, endIndex = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < S.length(); i++) {
            endIndex = Math.max(lastIndexOfChar[S.charAt(i) - 'a'], endIndex);
            if (i == endIndex) {
                res.add(endIndex - startIdx + 1);
                startIdx = endIndex + 1;
            }
        }
        return res;
    }

    /**
     * #781
     * <br/>
     * 森林中的兔子
     * <br/>
     * 森林中，每个兔子都有颜色。其中一些兔子（可能是全部）告诉你还有多少其他的兔子和自己有相同的颜色。
     * 我们将这些回答放在answers数组里。
     * <br/>
     * 要求返回森林中兔子的最少数量。
     *
     * @param answers answers
     * @return count of rabbit
     */
    public static int numRabbits(int[] answers) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int y : answers) {
            count.put(y, count.getOrDefault(y, 0) + 1);
        }
        int ans = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            int y = entry.getKey(), x = entry.getValue();
            ans += (x + y) / (y + 1) * (y + 1);
        }
        return ans;
    }

    /**
     * #785
     * <br>
     * bipartite graph: vertex and its neighbors has different color
     *
     * @param graph graph
     * @return graph is bipartite
     */
    public static boolean isBipartite(int[][] graph) {
        int vertices_count = graph.length;
        Map<Integer, Color> vertex_color = new HashMap<>();
        for (int i = 0; i < vertices_count; i++) {
            vertex_color.put(i, Color.GRAY);
        }
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vertices_count; i++) {
            if (vertex_color.get(i) == Color.GRAY) {
                var is_bipartite = BFS(i, queue, vertex_color, graph);
                if (!is_bipartite) {
                    return false;
                }
            }
        }
        return true;
    }

    private enum Color {
        GRAY, RED, GREEN
    }

    private static boolean BFS(Integer start, Queue<Integer> queue, Map<Integer, Color> vertex_color, int[][] graph) {
        queue.add(start);
        vertex_color.put(start, Color.RED);
        while (!queue.isEmpty()) {
            var vertex = queue.remove();
            for (var neighbor : graph[vertex]) {
                if (vertex_color.get(neighbor) == Color.GRAY) {
                    queue.add(neighbor);
                }
                if (vertex_color.get(neighbor) == vertex_color.get(vertex)) {
                    return false;
                } else {
                    vertex_color.put(neighbor, (vertex_color.get(vertex) == Color.RED) ? Color.GREEN : Color.RED);
                }
            }
        }
        return true;
    }

    /**
     * #786
     *
     * @param arr
     * @param k
     * @return
     */
    public static int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> arr[x[0]] * arr[y[1]] - arr[y[0]] * arr[x[1]]);
        for (int j = 1; j < n; ++j) {
            pq.offer(new int[] { 0, j });
        }
        for (int i = 1; i < k; ++i) {
            int[] frac = pq.remove();
            int x = frac[0], y = frac[1];
            if (x + 1 < y) {
                pq.offer(new int[] { x + 1, y });
            }
        }
        return new int[] { arr[pq.peek()[0]], arr[pq.peek()[1]] };
    }
}
