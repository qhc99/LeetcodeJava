package Leetcode;

import java.util.*;

@SuppressWarnings({ "Unused", "JavadocDeclaration" })
public class Leetcode1700 {
    /**
     * #1604
     * 
     * @param keyName
     * @param keyTime
     * @return
     */
    public List<String> alertNames(String[] keyName, String[] keyTime) {

        Set<String> alerted = new HashSet<>();
        List<String> res = new ArrayList<>();
        List<Entry> entries = new ArrayList<>(keyName.length);
        for (int i = 0; i < keyName.length; i++) {
            entries.add(new Entry(keyName[i], keyTime[i].split(":")));
        }
        entries.sort((a, b) -> {
            var c1 = a.name.compareTo(b.name);
            if (c1 != 0)
                return c1;
            return Arrays.compare(a.time, b.time);
        });
        int s = 0;
        for (int e = 0; e < entries.size(); e++) {
            var entry = entries.get(e);
            if (alerted.contains(entry.name))
                continue;
            if (!entries.get(s).name.equals(entry.name)) {
                s = e;
                continue;
            }
            while (!inOneHour(entries.get(s).time, entry.time)) {
                s++;
            }
            if (e - s >= 2) {
                alerted.add(entry.name);
                res.add(entry.name);
            }

        }
        return res;
    }

    boolean inOneHour(String[] s, String[] e) {
        var d1 = Integer.valueOf(e[0]) - Integer.valueOf(s[0]);
        var d2 = Integer.valueOf(e[1]) - Integer.valueOf(s[1]);
        return d1 * 60 + d2 <= 60;
    }

    static record Entry(String name, String[] time) {
    }

    /**
     * #1606
     * 
     * @param k
     * @param arrival
     * @param load
     * @return
     */
    public List<Integer> busiestServers(int k, int[] arrival, int[] load) {
        TreeSet<Integer> idle = new TreeSet<>();
        Queue<int[]> busy = new PriorityQueue<>(
                (a, b) -> Integer.compare(a[0], b[0]));
        Map<Integer, Integer> busyCount = new HashMap<>();
        int maxCount = 0;
        for (int i = 0; i < k; i++)
            idle.add(i);
        for (int i = 0; i < arrival.length; i++) {
            while (!busy.isEmpty() && busy.peek()[0] <= arrival[i]) {
                idle.add(busy.poll()[1]);
            }

            if (!idle.isEmpty()) {
                var s = idle.ceiling(i % k);
                if (s == null)
                    s = idle.ceiling(0);

                idle.remove(s);

                busy.add(new int[] { arrival[i] + load[i], s });
                var v = 1 + busyCount.getOrDefault(s, 0);
                busyCount.put(s, v);
                maxCount = Math.max(maxCount, v);
            }
        }
        int max = maxCount;
        return busyCount.entrySet().stream().filter(e -> e.getValue() == max)
                .map(e -> e.getKey()).toList();
    }

    /**
     * #1610
     * 
     * @param points
     * @param angle
     * @param location
     * @return
     */
    public int visiblePoints(List<List<Integer>> points, int angle,
            List<Integer> location) {
        int zeros = 0;
        double range = angle / 180. * Math.PI;
        List<List<Integer>> sortedPoints = new ArrayList<>();
        for (var p : points) {
            p.set(0, p.get(0) - location.get(0));
            p.set(1, p.get(1) - location.get(1));
            if (p.get(0) == p.get(1) && p.get(0) == 0)
                zeros++;
            else
                sortedPoints.add(p);
        }

        List<Double> sortedAngles = sortedPoints.stream().map(p -> {
            var a = Math.atan2(p.get(1), p.get(0));
            if (a < 0)
                a += 2 * Math.PI;
            return a;
        }).sorted().toList();
        Deque<AnglePos> deque = new ArrayDeque<>();

        int maxLen = deque.size();
        for (int i = 0; i < sortedAngles.size() * 2; i++) {
            var a = sortedAngles.get(i % sortedAngles.size());
            if (i >= sortedAngles.size())
                a += Math.PI * 2;
            if (a > range + Math.PI * 2)
                break;
            deque.add(new AnglePos(i, a));
            var end = deque.peekLast();
            var start = deque.peekFirst();
            while (end.angle - start.angle > range
                    || end.idx - start.idx + 1 > sortedAngles.size()) {
                deque.poll();
                start = deque.peekFirst();
            }
            maxLen = Math.max(maxLen, deque.size());
        }

        return zeros + maxLen;
    }

    static record AnglePos(int idx, double angle) {
    }

    /**
     * #1642
     */
    public int furthestBuilding(int[] heights, int bricks, int ladders) {
        Queue<Integer> used = new PriorityQueue<>(
                (a, b) -> Integer.compare(b, a));
        for (int i = 1; i < heights.length; i++) {
            var h = heights[i] - heights[i - 1];
            if (h > 0) {
                if (bricks >= h) {
                    bricks -= h;
                    used.add(h);
                } else {
                    while (bricks < h && ladders > 0 && !used.isEmpty()
                            && h < used.peek()) {
                        bricks += used.poll();
                        ladders--;
                    }
                    if (bricks >= h) {
                        bricks -= h;
                        used.add(h);
                    } else if (ladders > 0)
                        ladders--;
                    else
                        return i - 1;
                }
            }
        }
        return heights.length - 1;
    }

    /**
     * #1650
     * 
     * @param p
     * @param q
     * @return
     */
    static class Nest1 {
        static class Node {
            public int val;
            public Node left;
            public Node right;
            public Node parent;
        };

        public Node lowestCommonAncestor(Node p, Node q) {
            Queue<Node> queue = new ArrayDeque<>();
            Set<Node> visited = new HashSet<>();
            visited.add(p);
            visited.add(q);
            queue.add(q);
            queue.add(p);
            while (!queue.isEmpty()) {
                var n = queue.poll();
                if (n != null) {
                    if (visited.contains(n))
                        return n;
                    visited.add(n);
                    if (n.parent != null)
                        queue.add(n.parent);
                }
            }
            return null;
        }
    }

    /**
     * #1657
     * 
     * @param word1
     * @param word2
     * @return
     */
    public boolean closeStrings(String word1, String word2) {
        int[] count1 = new int['z' - 'a' + 1];
        int[] count2 = new int['z' - 'a' + 1];
        for (var c : word1.toCharArray())
            count1[c - 'a']++;
        for (var c : word2.toCharArray())
            count2[c - 'a']++;
        for (int i = 0; i < count1.length; i++) {
            if ((count1[i] == 0 && count2[i] != 0)
                    || (count1[i] != 0 && count2[i] == 0))
                return false;
        }
        Arrays.sort(count1);
        Arrays.sort(count2);
        return Arrays.equals(count1, count2);
    }

    /**
     * #1662
     *
     * @param word1
     * @param word2
     * @return
     */
    public static boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        var iter1 = new IterStrArr(word1);
        var iter2 = new IterStrArr(word2);
        while (iter1.hasNext()) {
            if (iter2.hasNext()) {
                var i1 = iter1.next();
                var i2 = iter2.next();
                if (!i1.equals(i2))
                    return false;
            } else
                return false;
        }
        return !iter2.hasNext();
    }

    private static class IterStrArr implements Iterator<Character> {
        final String[] words;
        int arr_idx = 0;
        int word_idx = -1;

        IterStrArr(String[] arr) {
            words = arr;
        }

        @Override
        public boolean hasNext() {
            if (word_idx + 1 < words[arr_idx].length()) {
                return true;
            } else {
                return arr_idx + 1 < words.length;
            }
        }

        @Override
        public Character next() {
            word_idx++;
            if (word_idx >= words[arr_idx].length()) {
                word_idx = 0;
                arr_idx++;
            }
            return words[arr_idx].charAt(word_idx);
        }
    }

    /**
     * #1669
     * 
     * @param list1
     * @param a
     * @param b
     * @param list2
     * @return
     */
    public ListNode mergeInBetween(ListNode list1, int a, int b,
            ListNode list2) {

        ListNode handle = new ListNode();
        var ptr = list2;
        while (ptr.next != null) {
            ptr = ptr.next;
        }
        var list2Tail = ptr;
        handle.next = list1;
        ptr = handle;
        while (a > 0) {
            ptr = ptr.next;
            a--;
            b--;
        }
        var list1Tail = ptr;
        while (b > 0) {
            ptr = ptr.next;
            b--;
        }
        var tail = ptr.next.next;
        list1Tail.next = list2;
        list2Tail.next = tail;
        return handle.next;
    }

    /**
     * #1676
     * 
     * @param root
     * @param nodes
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode[] nodes) {
        var solver = new LCA(nodes);
        solver.visit(root);
        return solver.res;
    }

    static class LCA {

        Set<Integer> targets = new HashSet<>();
        int totalFound = 0;
        TreeNode res = null;

        LCA(TreeNode[] nodes) {
            for (var n : nodes)
                targets.add(n.val);
        }

        int visit(TreeNode node) {
            int treeFound = 0;
            if (targets.contains(node.val)) {
                treeFound++;
                totalFound++;
            }
            if (node.left != null && totalFound < targets.size())
                treeFound += visit(node.left);
            if (node.right != null && totalFound < targets.size())
                treeFound += visit(node.right);
            if (res == null && treeFound == targets.size()) {
                res = node;
            }
            return treeFound;
        }
    }
}
