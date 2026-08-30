package Leetcode;

import java.util.*;

public class Leetcode1800 {

    /**
     * #1705
     * @param apples
     * @param days
     * @return
     */
    public int eatenApples(int[] apples, int[] days) {
        int res = 0;
        Queue<int[]> queue = new PriorityQueue<>(
                Comparator.comparing(a -> a[0]));
        int i = 0;
        for (; i < apples.length || !queue.isEmpty(); i++) {
            if (i < apples.length)
                queue.add(new int[] { days[i] + i, apples[i] });
            while (!queue.isEmpty()
                    && (queue.peek()[0] <= i || queue.peek()[1] <= 0)) {
                queue.poll();
            }
            if (!queue.isEmpty()) {
                queue.peek()[1]--;
                res++;
            }
        }
        return res;
    }

    /**
     * #1719
     * @param pairs
     * @return
     */
    public int checkWays(int[][] pairs) {
        Map<Integer, Set<Integer>> neighbor = new HashMap<>();
        for (var p : pairs) {

            neighbor.computeIfAbsent(p[0], k -> new HashSet<>()).add(p[1]);
            neighbor.computeIfAbsent(p[1], k -> new HashSet<>()).add(p[0]);
        }
        int n = neighbor.size(), root = -1;
        for (var i : neighbor.keySet()) {
            if (neighbor.get(i).size() == n - 1) {
                root = i;
                break;
            }
        }
        if (root == -1)
            return 0;
        int ans = 1;
        for (var i : neighbor.keySet()) {
            if (i.equals(root))
                continue;
            int min = Integer.MAX_VALUE, parent = -1;
            for (var nb : neighbor.get(i)) {
                if (neighbor.get(nb).size() >= neighbor.get(i).size()
                        && neighbor.get(nb).size() < min) {
                    parent = nb;
                    min = neighbor.get(nb).size();
                }
            }
            if (parent == -1)
                return 0;
            var adj = neighbor.get(i);
            var superSet = neighbor.get(parent);
            for (var node : adj) {
                if (node.equals(parent))
                    continue;
                if (!superSet.contains(node))
                    return 0;
            }
            if (neighbor.get(parent).size() == neighbor.get(i).size())
                ans = 2;
        }
        return ans;
    }

    /**
     * #1740
     * 
     * @param root
     * @param p
     * @param q
     * @return
     */
    public int findDistance(TreeNode root, int p, int q) {
        if (p == q)
            return 0;
        List<Integer> pathP = new ArrayList<>();
        List<Integer> pathQ = new ArrayList<>();
        visit(root, p, q, pathP, pathQ);
        while (!pathP.isEmpty() && !pathQ.isEmpty()
                && pathP.getLast().equals(pathQ.getLast())) {
            pathP.removeLast();
            pathQ.removeLast();
        }
        return pathP.size() + pathQ.size();
    }

    boolean[] visit(TreeNode n, int p, int q, List<Integer> pathP,
            List<Integer> pathQ) {
        if (n == null)
            return new boolean[2];
        var res = new boolean[2];
        var l = visit(n.left, p, q, pathP, pathQ);
        var r = visit(n.right, p, q, pathP, pathQ);
        res[0] |= n.val == p;
        res[1] |= n.val == q;
        res[0] |= l[0] || r[0];
        res[1] |= l[1] || r[1];
        if (res[0])
            pathP.add(n.val);
        if (res[1])
            pathQ.add(n.val);
        return res;
    }

    /**
     * #1768
     * @param word1
     * @param word2
     * @return
     */
    public String mergeAlternately(String word1, String word2) {
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        while (i < word1.length() || j < word2.length()) {
            if (i < word1.length() && j < word2.length()) {
                if (sb.length() % 2 == 0)
                    sb.append(word1.charAt(i++));
                else
                    sb.append(word2.charAt(j++));
            } else if (i < word1.length())
                sb.append(word1.charAt(i++));

            else
                sb.append(word2.charAt(j++));
        }
        return sb.toString();
    }

    /**
     * #1769
     * 
     * @param boxes
     * @return
     */
    public int[] minOperations(String boxes) {
        int[] res = new int[boxes.length()];
        int countOf1 = 0;
        int dist = 0;
        for (int i = 0; i < boxes.length(); i++) {
            var c = boxes.charAt(i);
            if (c == '1') {
                countOf1++;
                dist += i;
            }
        }
        int right1count = countOf1 - (boxes.charAt(0) == '1' ? 1 : 0);
        int left1count = 0;
        for (int i = 0; i < boxes.length(); i++) {
            var c = boxes.charAt(i);
            res[i] = dist;
            dist -= right1count;
            if (i + 1 < boxes.length() && boxes.charAt(i + 1) == '1')
                right1count--;
            if (c == '1')
                left1count++;
            dist += left1count;
        }

        return res;
    }

    /**
     * #1797 AuthenticationManager
     */
    class AuthenticationManager {
        static record Token(String id, int expire) {
            @Override
            public final boolean equals(Object arg0) {
                if (arg0 instanceof Token o) {
                    return o.id.equals(id) && o.expire == expire;
                }
                return false;
            }
        }

        int timeToLive;
        Map<String, Token> tokens = new HashMap<>();
        Queue<Token> queue = new PriorityQueue<>((a, b) -> a.expire - b.expire);

        public AuthenticationManager(int timeToLive) {
            this.timeToLive = timeToLive;
        }

        public void generate(String tokenId, int currentTime) {
            var t = new Token(tokenId, currentTime + timeToLive);
            tokens.put(tokenId, t);
            queue.add(t);
        }

        public void renew(String tokenId, int currentTime) {
            invalidate(currentTime);
            if (tokens.containsKey(tokenId)) {
                generate(tokenId, currentTime);
            }
        }

        void invalidate(int time) {
            while (!queue.isEmpty() && queue.peek().expire <= time) {
                var t = queue.poll();
                if (tokens.get(t.id).equals(t)) {
                    tokens.remove(t.id);
                }
            }
        }

        public int countUnexpiredTokens(int currentTime) {
            invalidate(currentTime);
            return tokens.size();
        }
    }

}
