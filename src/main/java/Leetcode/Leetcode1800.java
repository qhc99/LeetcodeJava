package Leetcode;

import java.util.*;

public class Leetcode1800 {
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
