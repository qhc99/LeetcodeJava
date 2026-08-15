package Leetcode;

import java.util.*;

import Leetcode.Leetcode1300.PosBudget;

@SuppressWarnings("JavaDoc")
public class Leetcode1300 {

    /**
     * #1200
     * 
     * @param arr
     * @return
     */
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr);
        int dist = Integer.MAX_VALUE;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            dist = Math.min(dist, arr[i] - arr[i - 1]);
        }
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] - arr[i - 1] == dist)
                res.add(List.of(arr[i - 1], arr[i]));

        }
        return res;

    }

    /**
     * #1207 <br/>
     * 
     * <pre>
     * 输入：arr = [1,2,2,1,1,3]
     * 输出：true
     * 解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。
     * </pre>
     *
     * @param arr int array
     * @return is unique
     */
    public static boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> m = new HashMap<>();
        for (var i : arr) {
            m.put(i, m.getOrDefault(i, 0) + 1);
        }
        Set<Integer> counts = new HashSet<>(m.values());
        return counts.size() == m.keySet().size();
    }

    /**
     * arr = [1,5,7,8,5,3,4,2,1], difference = -2 answer: 4 ([7,5,3,1])
     *
     * @param difference d
     * @return length
     */
    public static int longestSubsequence(int[] arr, int difference) {
        Map<Integer, Integer> m = new HashMap<>(arr.length);
        int ans = 1;
        for (var i : arr) {
            var t = m.getOrDefault(i - difference, 0) + 1;
            m.put(i, t);
            ans = Math.max(ans, t);
        }
        return ans;
    }

    /**
     * #1209
     * 
     * @param s
     * @param k
     * @return
     */
    public String removeDuplicates(String s, int k) {
        StringBuilder res = new StringBuilder();
        Deque<Character> deque = new ArrayDeque<>();
        Map<Character, Stack<Integer>> endCount = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            var c = s.charAt(i);
            if (!deque.isEmpty() && deque.peekLast().equals(c)) {
                var stack = endCount.get(c);
                stack.add(stack.pop() + 1);
            } else
                endCount.computeIfAbsent(c, key -> new Stack<>()).add(1);
            deque.addLast(c);
            var stack = endCount.get(c);
            if (stack.peek().equals(k)) {
                stack.pop();
                for (int j = 0; j < k; j++)
                    deque.pollLast();
            }
        }
        while (!deque.isEmpty()) {
            res.append(deque.pollFirst());
        }
        return res.toString();
    }

    /**
     * #1213
     * 
     * @param arr1
     * @param arr2
     * @param arr3
     * @return
     */
    public List<Integer> arraysIntersection(int[] arr1, int[] arr2,
            int[] arr3) {
        List<Integer> res = new ArrayList<>();
        int i = 0, j = 0, k = 0;
        while (i < arr1.length && j < arr2.length && k < arr3.length) {
            if (arr1[i] == arr2[j] && arr2[j] == arr3[k]) {
                res.add(arr1[i]);
                i++;
                j++;
                k++;
            } else if (arr1[i] <= arr2[j] && arr1[i] <= arr3[k])
                i++;
            else if (arr2[j] <= arr1[i] && arr2[j] <= arr3[k])
                j++;
            else
                k++;
        }
        return res;
    }

    public boolean isValidPalindrome(String s, int k) {
        int[] dp = new int[s.length()];
        for (int i = s.length() - 2; i >= 0; i--) {
            int prev = 0;// dp[i+1][j]
            int t;
            for (int j = i + 1; j < s.length(); j++) {
                t = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = prev; // dp[i+1][j-1]
                } else {
                    dp[j] = 1 + Math.min(dp[j], dp[j - 1]);// dp[i+1][j],
                                                           // dp[i][j-1]
                }
                prev = t;
            }
        }
        return dp[s.length() - 1] <= k;
    }

    /**
     * #1217
     *
     * @param position
     * @return
     */
    public static int minCostToMoveChips(int[] position) {
        int odd = 0, even = 0;
        for (var p : position) {
            if (p % 2 == 0)
                even++;
            else
                odd++;
        }
        return Math.min(odd, even);
    }

    interface HtmlParser {
        public List<String> getUrls(String url);
    }

    /**
     * #1229
     * 
     * @param slots1
     * @param slots2
     * @param duration
     * @return
     */
    public List<Integer> minAvailableDuration(int[][] slots1, int[][] slots2,
            int duration) {
        Arrays.sort(slots1, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(slots2, (a, b) -> Integer.compare(a[0], b[0]));

        List<Integer> res = new ArrayList<>();
        int i = 0, j = 0;
        while (i < slots1.length && j < slots2.length) {
            var r1 = slots1[i];
            var r2 = slots2[j];
            if (r1[1] <= r2[1]) {
                if (r1[1] >= r2[0]) {
                    var s = Math.max(r1[0], r2[0]);
                    var e = r1[1];
                    if (e - s >= duration) {
                        res.add(s);
                        res.add(Math.min(s + duration, e));
                        return res;
                    }
                }
                i++;
            } else {
                if (r2[1] >= r1[0]) {
                    var s = Math.max(r1[0], r2[0]);
                    var e = r2[1];
                    if (e - s >= duration) {
                        res.add(s);
                        res.add(Math.min(s + duration, e));
                        return res;
                    }
                }
                j++;
            }
        }
        return res;
    }

    static class CustomFunction {
        // Returns f(x, y) for any given positive integers x and y.
        // Note that f(x, y) is increasing with respect to both x and y.
        // i.e. f(x, y) < f(x + 1, y), f(x, y) < f(x, y + 1)
        public int f(int x, int y) {
            return 0;
        }

    };

    /**
     * #1237
     * 
     * @param customfunction
     * @param z
     * @return
     */
    public List<List<Integer>> findSolution(CustomFunction customfunction,
            int z) {
        List<List<Integer>> res = new ArrayList<>();
        for (int x = 1; x <= 1000; x++) {
            int l = 1, r = 1000;
            while (r - l > 0) {
                int mid = l + (r - l) / 2;
                if (customfunction.f(x, mid) < z) {
                    l = mid + 1;
                } else {
                    r = mid;
                }
            }
            while (customfunction.f(x, l) == z) {
                res.add(List.of(x, l));
                l++;
            }

        }
        return res;
    }

    /**
     * #1242
     * 
     * @param startUrl
     * @param htmlParser
     * @return
     */
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        int count = 16;
        Thread[] threads = new Thread[count];
        var workQueue = new WorkQueue();
        List<String> result = Collections.synchronizedList(new ArrayList<>());
        workQueue.add(startUrl);
        var targetDomain = startUrl.split("/")[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                while (!workQueue.isEnd()) {
                    var url = workQueue.poll();
                    if (url != null) {
                        if (url.split("/")[2].equals(targetDomain)) {
                            result.add(url);
                            var ret = htmlParser.getUrls(url);
                            for (var u : ret) {
                                if (u.split("/")[2].equals(targetDomain)) {
                                    workQueue.add(u);
                                }
                            }
                        }
                        workQueue.finishTask();
                    }
                }
            });

            threads[i].start();
        }
        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * #1245
     * 
     * @param edges
     * @return
     */
    public int treeDiameter(int[][] edges) {
        int n = edges.length + 1;
        if (n == 1)
            return 0;
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (var edge : edges) {
            graph.computeIfAbsent(edge[0], k -> new ArrayList<>()).add(edge[1]);
            graph.computeIfAbsent(edge[1], k -> new ArrayList<>()).add(edge[0]);
        }
        boolean[] visited = new boolean[n];
        var s = dfsNode(0, graph, visited);
        Arrays.fill(visited, false);
        var e = dfsNode(s[1], graph, visited);
        return e[0];
    }

    int[] dfsNode(int i, Map<Integer, List<Integer>> graph, boolean[] visited) {
        visited[i] = true;
        int[] res = new int[] { 0, i };
        for (var nb : graph.get(i)) {
            if (!visited[nb]) {
                var depthNode = dfsNode(nb, graph, visited);
                if (depthNode[0] + 1 > res[0]) {
                    res[0] = depthNode[0] + 1;
                    res[1] = depthNode[1];
                }
            }
        }
        return res;
    }

    /**
     * #1248
     * 
     * @param nums
     * @param k
     * @return
     */
    public int numberOfSubarrays(int[] nums, int k) {
        int lk = 0, lk_1 = 0, countK = 0, countK_1 = 0;
        int res = 0;
        for (int r = 0; r < nums.length; r++) {
            countK += nums[r] % 2;
            countK_1 += nums[r] % 2;
            while (countK > k) {
                countK -= nums[lk++] % 2;
            }
            while (countK_1 > k - 1) {
                countK_1 -= nums[lk_1++] % 2;
            }
            res += lk_1 - lk;
        }
        return res;
    }

    /**
     * #1249
     * 
     * @param s
     * @return
     */
    public String minRemoveToMakeValid(String s) {
        int count = 0;
        var sb = new StringBuilder();
        for (var c : s.toCharArray()) {
            if (c == '(') {
                count++;
                sb.append(c);
            } else if (c == ')') {
                if (count > 0) {
                    count--;
                    sb.append(c);
                }
            } else {
                sb.append(c);
            }
        }
        for (int i = sb.length() - 1; i >= 0 && count > 0; i--) {
            var c = sb.charAt(i);
            if (c == '(') {
                count--;
                sb.delete(i, i + 1);
            }
        }
        return sb.toString();
    }

    /**
     * #1268
     * 
     * @param products
     * @param searchWord
     * @return
     */
    public List<List<String>> suggestedProducts(String[] products,
            String searchWord) {
        List<List<String>> res = new ArrayList<>();
        var root = new TrieNode();
        for (var product : products) {
            var ptr = root;
            for (var c : product.toCharArray()) {
                ptr = ptr.children.computeIfAbsent(c, k -> new TrieNode());
                ptr.strs.add(product);
            }
        }
        var ptr = root;
        for (var c : searchWord.toCharArray()) {
            ptr = ptr != null ? ptr.children.get(c) : null;
            if (ptr == null) {
                res.add(List.of());
                continue;
            }
            Queue<String> queue = new PriorityQueue<>();
            queue.addAll(ptr.strs);
            List<String> suggest = new ArrayList<>();
            for (int i = 0; i < 3 && !queue.isEmpty(); i++) {
                suggest.add(queue.poll());
            }
            res.add(suggest);
        }
        return res;
    }

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        List<String> strs = new ArrayList<>();
    }

    /**
     * #1275
     * 
     * @param moves
     * @return
     */
    public String tictactoe(int[][] moves) {
        var t = new TicTacToe(3);
        for (int i = 0; i < moves.length; i++) {
            var m = moves[i];
            var res = t.move(m[0], m[1], i % 2 == 0 ? 1 : 2);
            if (res == 1)
                return "A";
            else if (res == 2)
                return "B";
        }
        return moves.length == 9 ? "Draw" : "Pending";
    }

    static class TicTacToe {
        int[][] rows;
        int[][] cols;
        int[][] diag = new int[2][2];
        int n;

        public TicTacToe(int n) {
            this.n = n;
            rows = new int[n][2];
            cols = new int[n][2];
        }

        public int move(int row, int col, int player) {
            if (++rows[row][player - 1] == n)
                return player;
            if (++cols[col][player - 1] == n)
                return player;
            if (row == col && ++diag[0][player - 1] == n)
                return player;
            if (row + col == n - 1 && ++diag[1][player - 1] == n)
                return player;
            return 0;
        }
    }

    /**
     * #1288
     * 
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            var c = a[0] - b[0];
            if (c != 0)
                return c;
            return b[1] - a[1];
        });
        int top = -1;
        int rm = 0;
        for (int i = 1; i < intervals.length; i++) {
            var current = intervals[i];
            if (current[1] > top)
                top = current[1];
            else
                rm++;
        }

        return intervals.length - rm;
    }

    /**
     * #1290
     *
     * @param head
     * @return
     */
    public static int getDecimalValue(ListNode head) {
        int ans = 0;
        do {
            ans *= 2;
            ans += head.val;
            head = head.next;
        } while (head != null);
        return ans;
    }

    /**
     * #1293
     * 
     * @param grid
     * @param k
     * @return
     */
    public int shortestPath(int[][] grid, int k) {
        int m = grid.length, n = grid[0].length;
        if (m == 1 && n == 1) {
            if (grid[0][0] == 1 || k >= grid[0][0])
                return 0;
            else
                return -1;
        }
        int[][] maxK = new int[m][n];
        for (var r : maxK)
            Arrays.fill(r, -1);
        Queue<PosBudget> queue = new ArrayDeque<>();
        queue.add(new PosBudget(0, 0, k - grid[0][0], 0));
        int[] dx = new int[] { 0, 0, 1, -1 };
        int[] dy = new int[] { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            var pair = queue.poll();
            for (int i = 0; i < 4; i++) {
                var x = pair.i + dx[i];
                var y = pair.j + dy[i];
                if (x >= 0 && x < m && y >= 0 && y < n) {
                    var nextBuget = pair.budget - grid[x][y];
                    if (nextBuget >= 0
                            && (maxK[x][y] < 0 || nextBuget > maxK[x][y])) {
                        maxK[x][y] = nextBuget;
                        var next = new PosBudget(x, y, nextBuget,
                                pair.steps + 1);
                        if (x == m - 1 && y == n - 1) {
                            return next.steps;
                        }
                        queue.add(next);
                    }
                }
            }
        }
        return -1;
    }

    static record PosBudget(int i, int j, int budget, int steps) {
    }

}

class WorkQueue {
    private int activeTasks = 0;
    private Set<String> visited = new HashSet<>();
    private Queue<String> workQueue = new ArrayDeque<>();

    synchronized String poll() {
        var res = workQueue.poll();
        if (res != null) {
            activeTasks++;
        }
        return res;
    }

    synchronized void add(String url) {
        if (!visited.contains(url)) {
            workQueue.add(url);
            visited.add(url);
        }
    }

    synchronized void finishTask() {
        activeTasks--;
    }

    synchronized boolean isEnd() {
        return activeTasks == 0 && workQueue.isEmpty();
    }

}
