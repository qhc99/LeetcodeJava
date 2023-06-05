package Leetcode;

import java.util.*;

import javax.management.RuntimeErrorException;

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

    public static final class DisjointSet {
        private int rank = 0;
        private DisjointSet parent = this;

        /**
         * find identifier of the set of an element
         *
         * @param x element
         * @return identifier
         */
        private static DisjointSet findGroupId(DisjointSet x) {
            if (x != x.parent) {
                x.parent = findGroupId(x.parent);
            }
            return x.parent;
        }

        public static boolean inSameSet(DisjointSet a, DisjointSet b) {
            return findGroupId(a) == findGroupId(b);
        }

        public static void union(DisjointSet a, DisjointSet b) {
            link(findGroupId(a), findGroupId(b));
        }

        private static void link(DisjointSet x, DisjointSet y) {
            if (x.rank > y.rank) {
                y.parent = x;
            } else {
                x.parent = y;
                if (x.rank == y.rank) {
                    y.rank = y.rank + 1;
                }
            }
        }
    }

    /**
     * #765
     * 
     * @param row
     * @return
     */
    public static int minSwapsCouples(int[] row) {
        int N = row.length / 2;
        DisjointSet[] unions = new DisjointSet[N];
        for(int i = 0; i < unions.length; i++){
            unions[i] = new DisjointSet();
        }
        for (int i = 0; i < row.length; i += 2) {
            int couple1 = row[i] / 2;
            int couple2 = row[i + 1] / 2;
            DisjointSet.union(unions[couple1], unions[couple2]);
        }
        int count = 0;
        for(var d : unions){
            if(d == d.parent){
                count++;
            }
        }
        return N - count;
    }

    /**
     * #778
     * 
     * @param grid
     * @return
     */
    public static int swimInWater(int[][] grid) {
        final int n = grid.length;
        if (n == 1) {
            return grid[0][0];
        }

        int currentHeight = grid[0][0];

        Queue<SwimPoolHeight> surrounding = new PriorityQueue<>(Comparator.comparing(d -> d.height));
        surrounding.add(new SwimPoolHeight(0, 1, grid[0][1]));
        surrounding.add(new SwimPoolHeight(1, 0, grid[1][0]));

        boolean[][] visited = new boolean[n][n];
        visited[0][0] = true;
        visited[0][1] = true;
        visited[1][0] = true;

        while (!surrounding.isEmpty()) {
            while (surrounding.peek().height <= currentHeight) {
                var d = surrounding.poll();
                int x = d.x, y = d.y;
                if (x == n - 1 && y == n - 1) {
                    return currentHeight;
                }
                if (x - 1 >= 0 && !visited[x - 1][y]) {
                    visited[x - 1][y] = true;
                    surrounding.add(new SwimPoolHeight(x - 1, y, grid[x - 1][y]));
                }
                if (y - 1 >= 0 && !visited[x][y - 1]) {
                    visited[x][y - 1] = true;
                    surrounding.add(new SwimPoolHeight(x, y - 1, grid[x][y - 1]));
                }
                if (x + 1 < n && !visited[x + 1][y]) {
                    visited[x + 1][y] = true;
                    surrounding.add(new SwimPoolHeight(x + 1, y, grid[x + 1][y]));
                }
                if (y + 1 < n && !visited[x][y + 1]) {
                    visited[x][y + 1] = true;
                    surrounding.add(new SwimPoolHeight(x, y + 1, grid[x][y + 1]));
                }
            }
            currentHeight = surrounding.peek().height;
        }

        throw new RuntimeException();
    }

    private static class SwimPoolHeight {
        final int x;
        final int y;
        final int height;

        public SwimPoolHeight(int x, int y, int h) {
            this.x = x;
            this.y = y;
            this.height = h;
        }
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
