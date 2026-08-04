package Leetcode;

import java.util.*;

public class Leetcode2100 {
    /**
     * #2104
     * 
     * @param nums
     * @return
     */
    public long subArrayRanges(int[] nums) {
        int[] minLeft = new int[nums.length];
        int[] minRight = new int[nums.length];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i])
                stack.pop();
            minLeft[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.add(i);
        }
        stack.clear();
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] >= nums[i])
                stack.pop();
            minRight[i] = stack.isEmpty() ? nums.length : stack.peek();
            stack.add(i);
        }
        stack.clear();
        int[] maxLeft = new int[nums.length];
        int[] maxRight = new int[nums.length];
        for (int i = 0; i < nums.length; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i])
                stack.pop();
            maxLeft[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.add(i);
        }
        stack.clear();
        for (int i = nums.length - 1; i >= 0; i--) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i])
                stack.pop();
            maxRight[i] = stack.isEmpty() ? nums.length : stack.peek();
            stack.add(i);
        }
        long res = 0;
        for (int i = 0; i < nums.length; i++) {
            long n = nums[i];
            res += n * ((i - maxLeft[i]) * (maxRight[i] - i)
                    - (i - minLeft[i]) * (minRight[i] - i));
        }

        return res;
    }

    /**
     * #2013 DetectSquares
     */
    class DetectSquares {

        static class Point {
            private final int x;
            private final int y;

            public Point(int[] data) {
                if (data == null || data.length != 2) {
                    throw new IllegalArgumentException(
                            "Point must have exactly 2 values");
                }
                this.x = data[0];
                this.y = data[1];
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (!(o instanceof Point other))
                    return false;
                return x == other.x && y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }

        Map<Integer, List<int[]>> xAxis = new HashMap<>();
        Map<Integer, List<int[]>> yAxis = new HashMap<>();
        Map<Point, Integer> count = new HashMap<>();

        public DetectSquares() {

        }

        public void add(int[] point) {
            xAxis.computeIfAbsent(point[0], t -> new ArrayList<>()).add(point);
            yAxis.computeIfAbsent(point[1], t -> new ArrayList<>()).add(point);
            count.put(new Point(point),
                    count.getOrDefault(new Point(point), 0) + 1);
        }

        public int count(int[] point) {
            var xPoints = xAxis.getOrDefault(point[0], List.of());
            var yPoints = yAxis.getOrDefault(point[1], List.of());
            Set<Integer> yOfXPoints = new HashSet<>(
                    xPoints.stream().map(p -> p[1]).toList());
            Set<Integer> xOfYPoints = new HashSet<>(
                    yPoints.stream().map(p -> p[0]).toList());
            int res = 0;
            for (var x : xOfYPoints) {
                for (var y : yOfXPoints) {
                    if (x != point[0] && Math.abs(x - point[0]) == Math
                            .abs(y - point[1])) {
                        var corner1 = count.getOrDefault(
                                new Point(new int[] { x, point[1] }), 0);
                        var corner2 = count.getOrDefault(
                                new Point(new int[] { point[0], y }), 0);
                        var corner3 = count
                                .getOrDefault(new Point(new int[] { x, y }), 0);
                        res += corner1 * corner2 * corner3;
                    }
                }
            }
            return res;
        }
    }

    /**
     * #2023
     * 
     * @param nums
     * @param target
     * @return
     */
    public int numOfPairs(String[] nums, String target) {
        Map<Integer, List<Integer>> startWith = new HashMap<>();
        Map<Integer, List<Integer>> endWith = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            var s = nums[i];
            if (target.startsWith(s))
                startWith.computeIfAbsent(s.length(), k -> new ArrayList<>())
                        .add(i);
            if (target.endsWith(s))
                endWith.computeIfAbsent(s.length(), k -> new ArrayList<>())
                        .add(i);
        }
        int res = 0;
        for (var e : startWith.entrySet()) {
            var len = e.getKey();
            var l = e.getValue();
            var r = endWith.getOrDefault(target.length() - len, List.of());
            if (r.isEmpty())
                continue;
            var s1 = new HashSet<>(l);
            var s2 = new HashSet<>(r);
            var lSize = s1.size();
            s1.retainAll(s2);
            lSize -= s1.size();
            s2.removeAll(s1);
            var rSize = s2.size();
            var mSize = s1.size();
            res += lSize * rSize + lSize * mSize + mSize * rSize
                    + mSize * (mSize - 1);
        }

        return res;
    }

    /**
     * #2043 Bank
     */
    class Bank {

        long[] balance;

        public Bank(long[] balance) {
            this.balance = balance;
        }

        boolean invalidAccount(int a) {
            return a < 1 || a > balance.length;
        }

        public boolean transfer(int account1, int account2, long money) {
            if (invalidAccount(account1) || invalidAccount(account2)
                    || balance[account1 - 1] < money)
                return false;
            balance[account1 - 1] -= money;
            balance[account2 - 1] += money;
            return true;
        }

        public boolean deposit(int account, long money) {
            if (invalidAccount(account))
                return false;
            balance[account - 1] += money;
            return true;
        }

        public boolean withdraw(int account, long money) {
            if (invalidAccount(account) || balance[account - 1] < money)
                return false;
            balance[account - 1] -= money;
            return true;
        }
    }

    /**
     * #2050
     * 
     * @param n
     * @param relations
     * @param time
     * @return
     */
    public int minimumTime(int n, int[][] relations, int[] time) {
        int[] totalTime = new int[n + 1];
        int[] depCount = new int[n + 1];
        List<Integer> toVisit = new ArrayList<>();
        Map<Integer, List<Integer>> dep = new HashMap<>();
        Map<Integer, List<Integer>> unblock = new HashMap<>();
        for (var r : relations) {
            dep.computeIfAbsent(r[1], k -> new ArrayList<>()).add(r[0]);
            depCount[r[1]]++;
            unblock.computeIfAbsent(r[0], k -> new ArrayList<>()).add(r[1]);
        }

        for (int i = 1; i <= n; i++) {
            if (depCount[i] == 0)
                toVisit.add(i);
        }

        while (!toVisit.isEmpty()) {
            List<Integer> next = new ArrayList<>();
            for (var i : toVisit) {
                for (var ub : unblock.getOrDefault(i, List.of())) {
                    depCount[ub]--;
                    if (depCount[ub] == 0) {
                        next.add(ub);
                    }
                }
                totalTime[i] = time[i - 1] + dep.getOrDefault(i, List.of())
                        .stream().map(idx -> totalTime[idx])
                        .max((a, b) -> a - b).orElse(0);
            }
            toVisit = next;
        }

        return Arrays.stream(totalTime).max().getAsInt();
    }

    /**
     * #2065
     * 
     * @param values
     * @param edges
     * @param maxTime
     * @return
     */
    public int maximalPathQuality(int[] values, int[][] edges, int maxTime) {
        int n = values.length;
        Map<Integer, Map<Integer, Integer>> edgeGraph = new HashMap<>();
        for (var e : edges) {
            edgeGraph.computeIfAbsent(e[0], k -> new HashMap<>()).put(e[1],
                    e[2]);
            edgeGraph.computeIfAbsent(e[1], k -> new HashMap<>()).put(e[0],
                    e[2]);

        }

        int[] backTime = new int[n];
        Arrays.fill(backTime, Integer.MAX_VALUE);
        backTime[0] = 0;
        Queue<NodeDist> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.dist, b.dist));
        queue.add(new NodeDist(0, 0));
        while (!queue.isEmpty()) {
            var node = queue.poll();
            for (var nb : edgeGraph.computeIfAbsent(node.id, k -> Map.of())
                    .entrySet()) {
                var nextDist = node.dist + nb.getValue();
                if (nextDist < backTime[nb.getKey()]) {
                    backTime[nb.getKey()] = nextDist;
                    queue.add(new NodeDist(nb.getKey(), nextDist));
                }
            }
        }
        var init = values[0];
        values[0] = 0;
        return dfs(0, 0, init, backTime, values, edgeGraph, maxTime);
    }

    static record NodeDist(int id, int dist) {
    }

    int dfs(int currentNode, int currentTime, int currentValue, int[] backTime,
            int[] values, Map<Integer, Map<Integer, Integer>> edgeGraph,
            int maxTime) {
        int max = currentValue;
        for (var nb : edgeGraph.computeIfAbsent(currentNode, k -> Map.of())
                .entrySet()) {
            if (currentTime + nb.getValue()
                    + backTime[nb.getKey()] <= maxTime) {
                var v = values[nb.getKey()];
                values[nb.getKey()] = 0;
                max = Math.max(max,
                        dfs(nb.getKey(), currentTime + nb.getValue(),
                                currentValue + v, backTime, values, edgeGraph,
                                maxTime));
                values[nb.getKey()] = v;
            }
        }
        return max;
    }

    /**
     * #2096
     * 
     * @param root
     * @param startValue
     * @param destValue
     * @return
     */
    public String getDirections(TreeNode root, int startValue, int destValue) {
        Map<Integer, Edge> graph = new HashMap<>();
        visitDir(root, startValue, destValue, graph, new FoundPair());
        StringBuilder sb = new StringBuilder();
        int ptr = startValue;
        while (ptr != destValue) {
            var edge = graph.get(ptr);
            ptr = edge.end;
            sb.append(edge.path);
        }
        return sb.toString();
    }

    static record Edge(int end, String path) {

    }

    static class FoundPair {
        boolean foundStart;
        boolean foundEnd;
    }

    FoundPair visitDir(TreeNode node, int startValue, int destValue,
            Map<Integer, Edge> graph, FoundPair globalFound) {
        // found start/dest
        FoundPair subTreeFound = new FoundPair();
        if (node == null)
            return subTreeFound;
        if (node.val == startValue || node.val == destValue) {
            globalFound.foundStart |= node.val == startValue;
            globalFound.foundEnd |= node.val == destValue;
            subTreeFound.foundStart |= node.val == startValue;
            subTreeFound.foundEnd |= node.val == destValue;
        }
        if (globalFound.foundStart && globalFound.foundEnd)
            return subTreeFound;
        var leftSubTreeFound = visitDir(node.left, startValue, destValue, graph,
                globalFound);
        if (leftSubTreeFound.foundStart ^ leftSubTreeFound.foundEnd) {
            if (leftSubTreeFound.foundEnd)
                graph.put(node.val, new Edge(node.left.val, "L"));
            if (leftSubTreeFound.foundStart)
                graph.put(node.left.val, new Edge(node.val, "U"));
        }
        subTreeFound.foundStart |= leftSubTreeFound.foundStart;
        subTreeFound.foundEnd |= leftSubTreeFound.foundEnd;
        if (globalFound.foundStart && globalFound.foundEnd)
            return subTreeFound;
        var rightSubTreeFound = visitDir(node.right, startValue, destValue,
                graph, globalFound);
        if (rightSubTreeFound.foundStart ^ rightSubTreeFound.foundEnd) {
            if (rightSubTreeFound.foundEnd)
                graph.put(node.val, new Edge(node.right.val, "R"));
            if (rightSubTreeFound.foundStart)
                graph.put(node.right.val, new Edge(node.val, "U"));
        }
        subTreeFound.foundStart |= rightSubTreeFound.foundStart;
        subTreeFound.foundEnd |= rightSubTreeFound.foundEnd;
        return subTreeFound;
    }
}
