package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc" })
public class Leetcode1500 {

    /**
     * #1400
     * @param s
     * @param k
     * @return
     */
    public boolean canConstruct(String s, int k) {
        int[] count = new int['z' - 'a' + 1];
        for (var c : s.toCharArray())
            count[c - 'a']++;
        int evenGroups = 0;
        int oddGroups = 0;
        for (var c : count) {
            if (c != 0) {
                evenGroups += c / 2;
                oddGroups += c % 2;
            }
        }
        return k >= oddGroups && oddGroups + evenGroups * 2 >= k;
    }

    /**
     * #1405
     * 
     * @param a
     * @param b
     * @param c
     * @return
     */
    public String longestDiverseString(int a, int b, int c) {
        int[] count = new int[] { a, b, c };
        StringBuilder res = new StringBuilder();
        Queue<Character> queue = new PriorityQueue<>(
                (l, r) -> Integer.compare(count[r - 'a'], count[l - 'a']));
        if (a > 0)
            queue.add('a');
        if (b > 0)
            queue.add('b');
        if (c > 0)
            queue.add('c');
        Stack<Character> stack = new Stack<>();
        while (!queue.isEmpty()) {
            var chr = queue.poll();
            var len = res.length();
            if (!(len >= 2 && res.charAt(len - 1) == chr
                    && res.charAt(len - 2) == chr)) {
                count[chr - 'a']--;
                res.append(chr);
                if (count[chr - 'a'] > 0)
                    queue.add(chr);
                while (!stack.isEmpty()) {
                    queue.add(stack.pop());
                }
            } else
                stack.add(chr);

        }
        return res.toString();
    }

    static record StrPos(String str, int pos) {
    }

    /**
     * # 1423
     * 
     * @param cardPoints
     * @param k
     * @return
     */
    public int maxScore(int[] cardPoints, int k) {
        int[] left = new int[k + 1];
        int[] right = new int[k + 1];
        for (int i = 1; i <= k; i++) {
            left[i] = cardPoints[i - 1] + left[i - 1];
        }

        for (int i = 1; i <= k; i++) {
            right[i] = cardPoints[cardPoints.length - i] + right[i - 1];
        }
        int res = -1;
        for (int i = 0; i <= k; i++) {
            res = Math.max(res, left[i] + right[k - i]);
        }

        return res;
    }

    interface BinaryMatrix {
        public int get(int row, int col);

        public List<Integer> dimensions();
    }

    /**
     * #1428
     * @param binaryMatrix
     * @return
     */
    public int leftMostColumnWithOne(BinaryMatrix binaryMatrix) {
        var dim = binaryMatrix.dimensions();
        int row = dim.get(0), col = dim.get(1);
        int res = col;
        for (int i = 0; i < row; i++) {
            int l = 0, r = res;
            while (r - l > 0) {
                int mid = l + (r - l) / 2;
                if (binaryMatrix.get(i, mid) == 1) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            res = l;
        }
        return res < col ? res : -1;
    }

    /**
     * #1438
     * @param nums
     * @param limit
     * @return
     */
    public int longestSubarray(int[] nums, int limit) {
        // desc max
        // asec min
        int l = 0, res = 0;
        Deque<Integer> max = new ArrayDeque<>(), min = new ArrayDeque<>();
        for (int r = 0; r < nums.length; r++) {
            while (!max.isEmpty() && nums[r] >= nums[max.peekLast()]) {
                max.pollLast();
            }
            max.addLast(r);
            while (!min.isEmpty() && nums[r] <= nums[min.peekLast()]) {
                min.pollLast();
            }
            min.addLast(r);
            while (l <= r
                    && nums[max.peekFirst()] - nums[min.peekFirst()] > limit) {
                while (!max.isEmpty() && max.peekFirst() <= l) {
                    max.pollFirst();
                }
                while (!min.isEmpty() && min.peekFirst() <= l) {
                    min.pollFirst();
                }
                l++;
            }
            res = Math.max(res, r + 1 - l);
        }
        return res;
    }

    /**
     * #1446
     *
     * @param s
     * @return
     */
    public static int maxPower(String s) {
        int left = 0, right = 1;
        int ans = 1;
        for (; right < s.length(); right++) {
            if (s.charAt(left) != s.charAt(right)) {
                left = right;
            }
            ans = Math.max(right + 1 - left, ans);
        }
        return ans;
    }

    /**
     * #1448
     * 
     * @param root
     * @return
     */
    public int goodNodes(TreeNode root) {
        return goodNodesVisit(root, Integer.MIN_VALUE);
    }

    public int goodNodesVisit(TreeNode node, int max) {
        if (node == null)
            return 0;
        int res = 0;
        if (node.val >= max)
            res++;
        return res + goodNodesVisit(node.left, Math.max(max, node.val))
                + goodNodesVisit(node.right, Math.max(max, node.val));
    }

    /**
     * #1462
     * 
     * @param numCourses
     * @param prerequisites
     * @param queries
     * @return
     */
    public List<Boolean> checkIfPrerequisite(int numCourses,
            int[][] prerequisites, int[][] queries) {
        Map<Integer, Set<Integer>> graph = new HashMap<>(numCourses);
        List<Boolean> res = new ArrayList<>();
        for (var req : prerequisites) {
            graph.computeIfAbsent(req[0], k -> new HashSet<>()).add(req[1]);
        }
        boolean[] visited = new boolean[numCourses];
        for (int i = 0; i < numCourses; i++) {
            dfs(i, graph, visited);
        }
        for (var query : queries) {
            if (graph.get(query[0]).contains(query[1]))
                res.add(true);
            else
                res.add(false);
        }
        return res;
    }

    Set<Integer> dfs(int idx, Map<Integer, Set<Integer>> graph,
            boolean[] visited) {
        visited[idx] = true;
        Set<Integer> set = new HashSet<>(graph.getOrDefault(idx, Set.of()));

        for (var nb : graph.getOrDefault(idx, Set.of())) {
            if (!visited[nb])
                set.addAll(dfs(nb, graph, visited));
            else
                set.addAll(graph.getOrDefault(nb, Set.of()));
        }
        graph.put(idx, set);
        return set;
    }

    /**
     * #1466
     * 
     * @param n
     * @param connections
     * @return
     */
    public int minReorder(int n, int[][] connections) {
        Map<Integer, Set<Integer>> source = new HashMap<>();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Queue<Integer> queue = new ArrayDeque<>();
        for (var c : connections) {
            source.computeIfAbsent(c[0], k -> new HashSet<>()).add(c[1]);
            graph.computeIfAbsent(c[0], k -> new ArrayList<>()).add(c[1]);
            graph.computeIfAbsent(c[1], k -> new ArrayList<>()).add(c[0]);
        }
        int res = 0;
        Set<Integer> inQueue = new HashSet<>();
        inQueue.add(0);
        queue.add(0);
        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (var nb : graph.get(node)) {
                if (inQueue.contains(nb))
                    continue;
                inQueue.add(nb);
                if (!source.containsKey(nb) || !source.get(nb).contains(node))
                    res++;

                queue.add(nb);
            }
        }

        return res;
    }

    /**
     * #1472 BrowserHistory
     */
    class BrowserHistory {
        String current;
        Stack<String> back = new Stack<>();
        Stack<String> forword = new Stack<>();

        public BrowserHistory(String homepage) {

            current = homepage;
        }

        public void visit(String url) {
            forword.clear();
            back.add(current);
            current = url;
        }

        public String back(int steps) {
            for (; !back.isEmpty() && steps > 0; steps--) {
                forword.add(current);
                current = back.pop();
            }

            return current;
        }

        public String forward(int steps) {
            for (; !forword.isEmpty() && steps > 0; steps--) {
                back.add(current);
                current = forword.pop();
            }

            return current;
        }
    }
}
