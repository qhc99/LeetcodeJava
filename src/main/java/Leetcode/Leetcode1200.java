package Leetcode;

import java.util.*;

public class Leetcode1200 {
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
