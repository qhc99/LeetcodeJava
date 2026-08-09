package Leetcode;

import java.util.*;

public class Leetcode1600 {
    /**
     * #1529
     * 
     * @param target
     * @return
     */
    public static int minFlips(String target) {
        char[] chrs = target.toCharArray();
        char d = chrs[0];
        int groups_count = 0;
        for (int i = 1; i < chrs.length; i++) {
            if (chrs[i] != d) {
                groups_count++;
            }
            d = chrs[i];
        }
        if (chrs[0] == '1') {
            groups_count++;
        }
        return groups_count;
    }

    /**
     * #1539
     * 
     * @param arr
     * @param k
     * @return
     */
    public int findKthPositive(int[] arr, int k) {
        var initMiss = arr[0] - 1;
        if (initMiss >= k)
            return k;
        int l = 0, r = arr.length;
        while (r - l > 1) {
            int mid = l + (r - l) / 2;
            var miss = arr[mid] - mid - 1;
            if (miss < k)
                l = mid;
            else
                r = mid;
        }
        return arr[l] + (k - (arr[l] - l - 1));
    }

    /**
     * #1570 SparseVector
     */
    class SparseVector {
        Map<Integer, Integer> kv = new HashMap<>();

        SparseVector(int[] nums) {
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0)
                    kv.put(i, nums[i]);
            }
        }

        // Return the dotProduct of two sparse vectors
        public int dotProduct(SparseVector vec) {
            var ks = new HashSet<>(kv.keySet());
            ks.retainAll(vec.kv.keySet());
            int res = 0;
            for (var k : ks) {
                res += kv.get(k) * vec.kv.get(k);
            }
            return res;
        }
    }

    /**
     * #1584
     * 
     * @param points
     * @return
     */
    public int minCostConnectPoints(int[][] points) {
        Queue<Edge> queue = new PriorityQueue<>((e1, e2) -> e1.len - e2.len);
        for (int i = 0; i < points.length; i++) {
            var pa = points[i];
            for (int j = i + 1; j < points.length; j++) {
                var pb = points[j];
                queue.add(new Edge(i, j,
                        Math.abs(pa[0] - pb[0]) + Math.abs(pa[1] - pb[1])));
            }
        }

        Disjointset set = new Disjointset(points.length);
        int res = 0;
        while (!queue.isEmpty()) {
            var e = queue.poll();
            if (set.parent(e.i) != set.parent(e.j)) {
                set.union(e.i, e.j);
                res += e.len;
            }
        }
        return res;
    }

    static record Edge(int i, int j, int len) {
    }

}
