package Leetcode;

import java.util.*;

public class Leetcode650 {

    /**
     * #621
     * 
     * @param tasks
     * @param n
     * @return
     */
    public int leastInterval(char[] tasks, int n) {
        Map<Character, Integer> latestPlaces = new HashMap<>();
        Map<Character, Integer> count = new HashMap<>();
        PriorityQueue<Character> queue = new PriorityQueue<>((a, b) -> {
            var c1 = count.get(a);
            var c2 = count.get(b);
            var diff = c2 - c1;
            if (diff != 0)
                return diff; // Larger count
            return a - b; // lex
        });

        for (var c : tasks) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }

        for (var k : count.keySet()) {
            queue.add(k);
        }
        int i = 1;
        Deque<Character> stack = new ArrayDeque<>();
        while (!count.isEmpty()) {
            stack.clear();
            while (!queue.isEmpty()) {
                var t = queue.poll();
                var idx = latestPlaces.getOrDefault(t, -n);
                var dist = i - idx;
                if (dist <= n) {
                    stack.add(t);
                    continue;
                }
                int c = count.get(t);
                if (c == 1) {
                    count.remove(t);
                } else {
                    count.put(t, c - 1);
                }
                if (c != 1)
                    queue.add(t);
                latestPlaces.put(t, i);
                break;
            }
            queue.addAll(stack);
            i++;
        }

        return i - 1;
    }

    /**
     * #631
     */
    class Excel {

        static record Cell(int row, int column) {
            @Override
            public final int hashCode() {
                return Objects.hash(row, column);
            }

            @Override
            public final boolean equals(Object arg0) {
                if (arg0 instanceof Cell o)
                    return o.row == row && o.column == column;
                return false;
            }
        }

        static record Range(int x1, int y1, int x2, int y2) {
        }

        int[][] mat;
        Map<Cell, List<Range>> formulas = new HashMap<>();

        public Excel(int height, char width) {
            mat = new int[height][width];
        }

        public void set(int row, char column, int val) {
            row--;
            int col = column - 'A';
            var cell = new Cell(row, col);
            if (formulas.containsKey(cell))
                formulas.remove(cell);
            updateCells(row, col, val - mat[row][col]);
        }

        void updateCells(int row, int column, int diff) {
            mat[row][column] += diff;
            for (var e : formulas.entrySet()) {
                var c = e.getKey();
                var rs = e.getValue();
                for (var r : rs) {
                    if (row >= r.x1 && row <= r.x2 && column >= r.y1
                            && column <= r.y2) {
                        updateCells(c.row, c.column, diff);
                    }
                }
            }
        }

        public int get(int row, char column) {
            row--;
            int col = column - 'A';
            return mat[row][col];
        }

        public int sum(int row, char column, String[] numbers) {
            row--;
            int col = column - 'A';

            var cell = new Cell(row, col);
            List<Range> ranges = new ArrayList<>();
            int new_val = 0;
            for (var n : numbers) {
                if (!n.contains(":")) {
                    var y = (int) (n.charAt(0) - 'A');
                    var x = Integer.valueOf(n.substring(1)) - 1;
                    ranges.add(new Range(x, y, x, y));
                    new_val += mat[x][y];
                } else {
                    var cs = n.split(":");
                    var y1 = (int) (cs[0].charAt(0) - 'A');
                    var x1 = Integer.valueOf(cs[0].substring(1)) - 1;
                    var y2 = (int) (cs[1].charAt(0) - 'A');
                    var x2 = Integer.valueOf(cs[1].substring(1)) - 1;
                    ranges.add(new Range(x1, y1, x2, y2));
                    for (int i = x1; i <= x2; i++) {
                        for (int j = y1; j <= y2; j++) {
                            new_val += mat[i][j];
                        }
                    }
                }
            }
            formulas.put(cell, ranges);
            updateCells(row, col, new_val - mat[row][col]);
            return mat[row][col];
        }

    }

    /**
     * #632
     * 
     * @param nums
     * @return
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        int[] res = new int[2];
        res[0] = 0;
        res[1] = Integer.MAX_VALUE;
        List<Integer> idx = new ArrayList<>();
        int max = Integer.MIN_VALUE;
        for (var arr : nums) {
            max = Math.max(max, arr.get(0));
            idx.add(0);
        }
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>(
                (i, j) -> nums.get(i).get(idx.get(i))
                        - nums.get(j).get(idx.get(j)));
        for (int i = 0; i < nums.size(); i++) {
            queue.add(i);
        }
        while (true) {
            var row = queue.poll();
            var i = idx.get(row);
            var val = nums.get(row).get(i);
            if (((max - val) < (res[1] - res[0]))
                    || (val < res[0] && (max - val) == (res[1] - res[0]))) {
                res[0] = val;
                res[1] = max;
            }
            i++;
            if (i >= nums.get(row).size())
                break;
            idx.set(row, i);
            max = Math.max(max, nums.get(row).get(i));
            queue.add(row);
        }
        return res;
    }

    /**
     * #638
     *
     * @param price
     * @param special
     * @param needs
     * @return
     */
    public static int shoppingOffers(List<Integer> price,
            List<List<Integer>> special, List<Integer> needs) {
        List<List<Integer>> valid_special = new ArrayList<>(special.size());
        for (var spec : special) {
            if (spec.get(spec.size() - 1) < non_special_price_of(
                    spec.subList(0, spec.size() - 1), price)) {
                valid_special.add(spec);
            }
        }
        Map<String, Integer> cache = new HashMap<>(1024);
        return min_offer_of(needs, price, valid_special, cache);
    }

    private static int min_offer_of(List<Integer> current_needs,
            List<Integer> price, List<List<Integer>> special,
            Map<String, Integer> cache) {
        var str_current_needs = current_needs.toString();
        if (cache.containsKey(str_current_needs)) {
            return cache.get(str_current_needs);
        } else {
            int non_spec_price = non_special_price_of(current_needs, price);
            int min_spec_price = Integer.MAX_VALUE;
            for (var spec : special) {
                var new_need = new_needs(current_needs, spec);
                if (new_need != null) {
                    min_spec_price = Math.min(min_spec_price,
                            min_offer_of(new_need, price, special, cache)
                                    + spec.get(spec.size() - 1));
                }
            }
            var ans = Math.min(non_spec_price, min_spec_price);
            cache.put(str_current_needs, ans);
            return ans;
        }
    }

    private static List<Integer> new_needs(List<Integer> current_needs,
            List<Integer> spec) {
        List<Integer> n = new ArrayList<>(current_needs.size());
        for (int i = 0; i < current_needs.size(); i++) {
            var n_i = current_needs.get(i) - spec.get(i);
            if (n_i >= 0)
                n.add(n_i);
            else
                return null;
        }
        return n;
    }

    private static int non_special_price_of(List<Integer> needs,
            List<Integer> price) {
        int ans = 0;
        for (int i = 0; i < needs.size(); i++) {
            ans += needs.get(i) * price.get(i);
        }
        return ans;
    }

    /**
     * #643
     *
     * @param nums
     * @param k
     * @return
     */
    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= k) {
                sum -= nums[i - k];
            }
            if (i + 1 >= k) {
                max = Math.max(sum, max);
            }

        }
        return max / (double) k;
    }
}
