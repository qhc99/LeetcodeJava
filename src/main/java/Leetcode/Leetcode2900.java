package Leetcode;

import java.util.*;

public class Leetcode2900 {

    /**
     * #2817
     * 
     * @param nums
     * @param x
     * @return
     */
    public int minAbsoluteDifference(List<Integer> nums, int x) {
        TreeSet<Integer> set = new TreeSet<>();
        int min = Integer.MAX_VALUE;
        for (int i = x; i < nums.size(); i++) {
            set.add(nums.get(i - x));
            var ceil = set.ceiling(nums.get(i));
            if (ceil != null)
                min = Math.min(min, ceil - nums.get(i));
            var floor = set.floor(nums.get(i));
            if (floor != null)
                min = Math.min(min, nums.get(i) - floor);

        }
        return min;
    }

    /**
     * #2828
     * @param words
     * @param s
     * @return
     */
    public boolean isAcronym(List<String> words, String s) {
        if (words.size() != s.length())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != words.get(i).charAt(0))
                return false;
        }
        return true;
    }

    /**
     * #2848
     * @param nums
     * @return
     */
    public int numberOfPoints(List<List<Integer>> nums) {
        nums.sort(Comparator.comparing(l -> l.get(0)));
        int l = 0, r = -1;
        int res = 0;
        for (var p : nums) {
            if (p.get(0) > r) {
                res += r + 1 - l;
                l = p.get(0);
                r = p.get(1);
            } else {
                r = Math.max(r, p.get(1));
            }
        }
        res += r + 1 - l;
        return res;
    }

    /**
     * #2850
     * 
     * @param grid
     * @return
     */
    public int minimumMoves(int[][] grid) {
        List<Integer> less = new ArrayList<>();
        List<Integer> more = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i][j] == 0) {
                    less.add(i * 3 + j);
                } else if (grid[i][j] > 1) {
                    for (int k = 2; k <= grid[i][j]; k++) {
                        more.add(i * 3 + j);
                    }
                }
            }
        }

        return minimumMoves(-1, new boolean[more.size()], new ArrayList<>(),
                more, less);
    }

    int minimumMoves(int prevIdx, boolean[] selected, List<Integer> currentMore,
            List<Integer> more, List<Integer> less) {
        int res = Integer.MAX_VALUE;
        boolean end = true;
        for (var b : selected)
            end &= b;
        if (end) {
            res = 0;
            for (int j = 0; j < currentMore.size(); j++) {
                int idx1 = currentMore.get(j), idx2 = less.get(j);
                int x1 = idx1 / 3, y1 = idx1 % 3, x2 = idx2 / 3, y2 = idx2 % 3;
                res += Math.abs(x1 - x2) + Math.abs(y1 - y2);
            }
            return res;

        }
        for (int i = 0; i < selected.length; i++) {
            if (!selected[i] && !(prevIdx >= 0 && selected[prevIdx]
                    && more.get(prevIdx).equals(more.get(i)) && i < prevIdx)) {
                selected[i] = true;
                currentMore.add(more.get(i));
                res = Math.min(res,
                        minimumMoves(i, selected, currentMore, more, less));
                currentMore.removeLast();
                selected[i] = false;
            }
        }

        return res;
    }
}
