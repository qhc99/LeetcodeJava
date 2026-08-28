package Leetcode;

import java.util.*;

public class Leetcode2000 {

    /**
     * #1926
     * @param maze
     * @param entrance
     * @return
     */
    public int nearestExit(char[][] maze, int[] entrance) {
        int steps = 0, m = maze.length, n = maze[0].length;
        int[] dx = { 0, 0, 1, -1 }, dy = { 1, -1, 0, 0 };
        Queue<int[]> queue = new ArrayDeque<>(), next = new ArrayDeque<>();
        queue.add(entrance);
        boolean[][] visited = new boolean[m][n];
        visited[entrance[0]][entrance[1]] = true;
        while (!queue.isEmpty()) {

            while (!queue.isEmpty()) {
                var p = queue.poll();
                for (int i = 0; i < 4; i++) {
                    int x = p[0] + dx[i], y = p[1] + dy[i];
                    if (x >= 0 && x < m && y >= 0 && y < n && !visited[x][y]
                            && maze[x][y] == '.') {
                        visited[x][y] = true;
                        if (x == 0 || x == m - 1 || y == 0 || y == n - 1)
                            return steps + 1;
                        next.add(new int[] { x, y });
                    }
                }
            }
            var t = queue;
            queue = next;
            next = t;
            steps++;
        }
        return -1;
    }

    /**
     * #1944
     * 
     * @param heights
     * @return
     */
    public int[] canSeePersonsCount(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int[] res = new int[heights.length];
        for (int i = heights.length - 1; i >= 0; i--) {
            var h = heights[i];
            while (!stack.isEmpty() && stack.peek() < h) {
                stack.pop();
                res[i]++;
            }
            if (!stack.isEmpty())
                res[i]++;
            stack.push(h);
        }
        return res;
    }

    /**
     * #1948
     * 
     * @param paths
     * @return
     */
    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        var root = new PathNode("");
        Set<PathNode> duplicated = new HashSet<>();
        for (var path : paths) {
            var ptr = root;
            for (var folder : path) {
                ptr = ptr.children.computeIfAbsent(folder,
                        key -> new PathNode(key));
            }
        }
        Map<PathNode, String> idCache = new HashMap<>();
        root.toId(idCache);
        Map<String, List<PathNode>> id2NodeGroup = new HashMap<>();
        for (var e : idCache.entrySet()) {
            id2NodeGroup.computeIfAbsent(e.getValue(), k -> new ArrayList<>())
                    .add(e.getKey());
        }
        for (var group : id2NodeGroup.values()) {
            if (group.size() < 2)
                continue;
            for (int i = 0; i < group.size(); i++) {
                if (!group.get(i).children.isEmpty())
                    duplicated.add(group.get(i));
            }
        }
        List<List<String>> res = new ArrayList<>();
        visitPaths(root, res, new ArrayList<>(), duplicated);
        return res;
    }

    void visitPaths(PathNode ptr, List<List<String>> res, List<String> current,
            Set<PathNode> duplicated) {
        if (duplicated.contains(ptr))
            return;
        boolean added = false;
        if (ptr.name.length() > 0) {
            current.add(ptr.name);
            added = true;
        }
        if (current.size() > 0)
            res.add(new ArrayList<>(current));
        for (var c : ptr.children.values()) {
            visitPaths(c, res, current, duplicated);
        }
        if (added)
            current.removeLast();
    }

    static class PathNode {
        String name;
        Map<String, PathNode> children = new HashMap<>();

        PathNode(String n) {
            name = n;
        }

        String toId(Map<PathNode, String> idCache) {
            var id = idCache.get(this);
            if (id == null) {
                id = children.values().stream()
                        .map(n -> n.name + n.toId(idCache)).sorted().toList()
                        .toString();
                idCache.put(this, id);
            }
            return id;
        }
    }

    /**
    * #1961
    * @param s
    * @param words
    * @return
    */
    public boolean isPrefixString(String s, String[] words) {
        int j = 0, k = 0;
        for (int i = 0; i < s.length(); i++, k++) {
            if (k >= words[j].length()) {
                j++;
                k = 0;
            }
            if (j >= words.length || s.charAt(i) != words[j].charAt(k))
                return false;
        }
        return k == words[j].length();
    }

    /**
     * #1962
     * 
     * @param piles
     * @param k
     * @return
     */
    public int minStoneSum(int[] piles, int k) {
        Queue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
        for (var v : piles)
            queue.add(v);
        for (; k > 0; k--) {
            var v = queue.poll();
            queue.add(v - v / 2);
        }
        return queue.stream().mapToInt(i -> i).sum();
    }

}
