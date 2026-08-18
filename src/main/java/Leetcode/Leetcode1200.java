package Leetcode;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

public class Leetcode1200 {

    /**
     * #1117 H2O
     */
    class H2O {
        Semaphore ox = new Semaphore(0);
        Semaphore hy = new Semaphore(2);

        public H2O() {

        }

        public void hydrogen(Runnable releaseHydrogen)
                throws InterruptedException {
            hy.acquire();
            // releaseHydrogen.run() outputs "H". Do not change or remove this
            // line.
            releaseHydrogen.run();
            ox.release(1);
        }

        public void oxygen(Runnable releaseOxygen) throws InterruptedException {
            ox.acquire(2);
            // releaseOxygen.run() outputs "O". Do not change or remove this
            // line.
            releaseOxygen.run();
            hy.release(2);
        }
    }

    /**
     * #1136
     * 
     * @param n
     * @param relations
     * @return
     */
    public int minimumSemesters(int n, int[][] relations) {
        Set<Integer> currentSet = new HashSet<>();
        Queue<Integer> current = new ArrayDeque<>();
        Queue<Integer> next = new ArrayDeque<>();
        Map<Integer, Integer> lock = new HashMap<>();
        Map<Integer, List<Integer>> unlock = new HashMap<>();
        for (int i = 1; i <= n; i++)
            currentSet.add(i);
        for (var link : relations) {
            currentSet.remove(link[1]);
            lock.put(link[1], 1 + lock.getOrDefault(link[1], 0));
            unlock.computeIfAbsent(link[0], k -> new ArrayList<>())
                    .add(link[1]);
        }
        current.addAll(currentSet);
        int res = 0;
        while (!current.isEmpty()) {
            res++;
            while (!current.isEmpty()) {
                var course = current.poll();
                for (var ub : unlock.getOrDefault(course, List.of())) {
                    var count = lock.get(ub);
                    if (count != null) {
                        if (count == 1) {
                            next.add(ub);
                            lock.remove(ub);
                        } else
                            lock.put(ub, count - 1);
                    }
                }
            }
            var t = next;
            next = current;
            current = t;
        }

        return lock.isEmpty() ? res : -1;
    }

    /**
     * #1143
     * 
     * @param text1
     * @param text2
     * @return
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int[][] dp = new int[2][text2.length() + 1];
        for (int i = 1; i <= text1.length(); i++) {
            for (int j = 1; j <= text2.length(); j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[1][j] = dp[0][j - 1] + 1;
                } else {
                    dp[1][j] = Math.max(dp[0][j - 1], dp[1][j - 1]);
                }
            }
            System.arraycopy(dp[1], 0, dp[0], 0, text2.length() + 1);
        }
        return dp[1][text2.length()];
    }

    /**
     * #1146 SnapshotArray
     */
    class SnapshotArray {
        static class Node {
            int snap_id;
            int val;
        }

        List<List<Node>> list = null;
        int snapId;

        public SnapshotArray(int length) {
            list = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                List<Node> l = new ArrayList<>();
                l.add(new Node());
                list.add(l);
            }
        }

        public void set(int index, int val) {
            var n = list.get(index).getLast();
            if (n.snap_id == snapId) {
                n.val = val;
            } else {
                n = new Node();
                n.snap_id = snapId;
                n.val = val;
                list.get(index).add(n);
            }
        }

        public int snap() {
            return snapId++;
        }

        public int get(int index, int snap_id) {
            var l = list.get(index);
            int i = 0, j = l.size();
            while (j - i > 1) {
                int mid = i + (j - i) / 2;
                var n = l.get(mid);
                if (n.snap_id > snap_id) {
                    j = mid;
                } else if (n.snap_id < snap_id) {
                    i = mid;
                } else {
                    return l.get(mid).val;
                }
            }
            return l.get(i).val;
        }
    }

    /**
     * #1162
     * 
     * @param grid
     * @return
     */
    public int maxDistance(int[][] grid) {
        int res = -1, n = grid.length;
        Queue<int[]> queue = new ArrayDeque<>(), next = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.add(new int[] { i, j });
                    visited[i][j] = true;
                }
            }
        }
        int[] dx = { 0, 0, 1, -1 }, dy = { 1, -1, 0, 0 };
        while (!queue.isEmpty()) {
            res++;
            while (!queue.isEmpty()) {

                var p = queue.poll();
                for (int i = 0; i < 4; i++) {
                    int x = p[0] + dx[i], y = p[1] + dy[i];
                    if (x >= 0 && x < n && y >= 0 && y < n && !visited[x][y]) {
                        visited[x][y] = true;
                        next.add(new int[] { x, y });
                    }
                }
            }
            var t = queue;
            queue = next;
            next = t;
        }
        return res > 0 ? res : -1;
    }

    /**
     * #1168
     * 
     * @param n
     * @param wells
     * @param pipes
     * @return
     */
    public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
        Queue<Pipe> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(a.cost, b.cost));
        for (int i = 0; i < wells.length; i++)
            queue.add(new Pipe(0, i + 1, wells[i]));
        for (var p : pipes)
            queue.add(new Pipe(p[0], p[1], p[2]));
        var set = new Disjointset(n + 1);
        int cost = 0;
        while (!queue.isEmpty()) {
            var p = queue.poll();
            if (set.parent(p.s) != set.parent(p.e)) {
                set.union(p.s, p.e);
                cost += p.cost;
            }
        }
        return cost;
    }

    static record Pipe(int s, int e, int cost) {
    }

    /**
     * #1169
     * 
     * @param transactions
     * @return
     */
    public List<String> invalidTransactions(String[] transactions) {
        List<String> res = new ArrayList<>();
        List<Transaction> ts = new ArrayList<>();
        for (int i = 0; i < transactions.length; i++) {
            var t = transactions[i];
            var arr = t.split(",");
            ts.add(new Transaction(i, arr[0], Integer.valueOf(arr[1]),
                    Integer.valueOf(arr[2]), arr[3]));
        }
        ts.sort((a, b) -> {
            var c = a.name.compareTo(b.name);
            if (c != 0)
                return c;
            return a.time - b.time;
        });
        int s = 0;
        Set<Integer> invalid = new HashSet<>();
        for (int e = 0; e < ts.size(); e++) {
            var t = ts.get(e);
            if (t.amount > 1000) {
                res.add(transactions[t.idx]);
                invalid.add(t.idx);
            }
            if (!t.name.equals(ts.get(s).name)) {
                s = e;
            }
            while (t.time - ts.get(s).time > 60) {
                s++;
            }
            String city = ts.get(s).city;
            boolean anotherCity = false;
            for (int i = s + 1; i <= e; i++) {
                if (!ts.get(i).city.equals(city)) {
                    anotherCity = true;
                    break;
                }
            }
            if (anotherCity) {
                for (int i = s; i <= e; i++) {
                    if (!invalid.contains(ts.get(i).idx)) {
                        invalid.add(ts.get(i).idx);
                        res.add(transactions[ts.get(i).idx]);
                    }
                }
            }

        }
        return res;
    }

    static record Transaction(int idx, String name, int time, int amount,
            String city) {
    }

    /**
     * #1195 FizzBuzz
     */
    class FizzBuzz {
        private AtomicInteger c;
        private final int n;

        public FizzBuzz(int n) {
            this.n = n;
            this.c = new AtomicInteger(1);
        }

        // printFizz.run() ouatputs "fizz".
        public void fizz(Runnable printFizz) throws InterruptedException {
            int v = c.get();
            while (v <= n) {
                var t = v % 3;
                var f = v % 5;
                if (t == 0 && f != 0) {
                    printFizz.run();
                    c.incrementAndGet();
                }
                v = c.get();
            }
        }

        // printBuzz.run() outputs "buzz".
        public void buzz(Runnable printBuzz) throws InterruptedException {
            int v = c.get();
            while (v <= n) {
                var t = v % 3;
                var f = v % 5;
                if (t != 0 && f == 0) {
                    printBuzz.run();
                    c.incrementAndGet();
                }
                v = c.get();
            }
        }

        // printFizzBuzz.run() outputs "fizzbuzz".
        public void fizzbuzz(Runnable printFizzBuzz)
                throws InterruptedException {
            int v = c.get();
            while (v <= n) {
                var t = v % 3;
                var f = v % 5;
                if (t == 0 && f == 0) {
                    printFizzBuzz.run();
                    c.incrementAndGet();
                }
                v = c.get();
            }
        }

        // printNumber.accept(x) outputs "x", where x is an integer.
        public void number(IntConsumer printNumber)
                throws InterruptedException {
            int v = c.get();
            while (v <= n) {
                var t = v % 3;
                var f = v % 5;
                if (t != 0 && f != 0) {
                    printNumber.accept(v);
                    ;
                    c.incrementAndGet();
                }
                v = c.get();
            }
        }
    }

    /**
     * #1197
     * 
     * @param x
     * @param y
     * @return
     */
    public int minKnightMoves(int x, int y) {
        if (x == 0 && y == 0)
            return 0;
        Map<Pos, Integer> visited = new HashMap<>();
        Map<Pos, Integer> visited2 = new HashMap<>();
        Queue<Pos> queue = new ArrayDeque<>();
        Queue<Pos> queue2 = new ArrayDeque<>();
        queue.add(new Pos(0, 0));
        queue2.add(new Pos(x, y));
        visited.put(new Pos(0, 0), 0);
        visited2.put(new Pos(x, y), 0);
        int[] dx = new int[] { -1, -2, 1, 2, -1, -2, 1, 2 };
        int[] dy = new int[] { -2, -1, -2, -1, 2, 1, 2, 1 };
        while (!queue.isEmpty() || !queue2.isEmpty()) {
            boolean pollFirst = queue.size() <= queue2.size();
            var thisQueue = pollFirst ? queue : queue2;
            var thisVisited = pollFirst ? visited : visited2;
            var anotherVisited = pollFirst ? visited2 : visited;
            var current = thisQueue.poll();
            var dist = thisVisited.get(current);

            for (int i = 0; i < 8; i++) {
                var a = current.x + dx[i];
                var b = current.y + dy[i];
                var next = new Pos(a, b);
                if (anotherVisited.containsKey(next)) {
                    return dist + 1 + anotherVisited.get(next);
                }
                if (!thisVisited.containsKey(next)) {
                    thisVisited.put(next, dist + 1);
                    thisQueue.add(new Pos(a, b));
                }

            }
        }
        return -1;
    }

    static record Pos(int x, int y) {
        @Override
        public final int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Pos o) {
                return o.x == x && o.y == y;
            }
            return false;
        }
    }

}
