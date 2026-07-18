package Leetcode;

import java.util.*;

public class Leetcode2500 {

    /**
     * #2408 SQL
     */
    class SQL {
        static class Table {
            int column;
            int id = 1;
            TreeMap<Integer, List<String>> rows = new TreeMap<>();

            Table(int col) {
                column = col;
            }

            boolean insert(List<String> row) {
                if (row.size() != column) {
                    return false;
                }
                rows.put(id++, row);
                return true;
            }

            void remove(int rowId) {
                rows.remove(rowId);
            }

            String select(int rowId, int columnId) {
                if (columnId < 1 || columnId > column)
                    return "<null>";
                var row = rows.getOrDefault(rowId, List.of());
                if (columnId - 1 >= row.size())
                    return "<null>";
                return row.get(columnId - 1);
            }

            public List<String> toExp() {
                List<String> res = new ArrayList<>();
                for (var e : rows.entrySet()) {
                    var sb = new StringBuilder();
                    sb.append(e.getKey());
                    sb.append(",");
                    sb.append(String.join(",", e.getValue()));
                    res.add(sb.toString());
                }
                return res;
            }
        }

        Map<String, Table> tables = new HashMap<>();

        public SQL(List<String> names, List<Integer> columns) {
            for (int i = 0; i < names.size(); i++) {
                tables.put(names.get(i), new Table(columns.get(i)));
            }
        }

        public boolean ins(String name, List<String> row) {
            var table = tables.get(name);
            if (table == null)
                return false;
            return table.insert(row);
        }

        public void rmv(String name, int rowId) {
            var table = tables.get(name);
            if (table == null)
                return;
            table.remove(rowId);
        }

        public String sel(String name, int rowId, int columnId) {
            var table = tables.get(name);
            if (table == null)
                return "<null>";
            return table.select(rowId, columnId);
        }

        public List<String> exp(String name) {
            return tables.getOrDefault(name, new Table(0)).toExp();
        }
    }

    /**
     * #2461
     * 
     * @param nums
     * @param k
     * @return
     */
    public long maximumSubarraySum(int[] nums, int k) {
        long res = 0;
        Set<Integer> inQueue = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();
        long sum = 0;
        for (var n : nums) {
            while (inQueue.contains(n) || queue.size() >= k) {
                var v = queue.poll();
                sum -= v;
                inQueue.remove(v);
            }
            sum += n;
            inQueue.add(n);
            queue.add(n);
            if (queue.size() == k)
                res = Math.max(res, sum);
        }
        return res;
    }

    /**
     * #2483
     * 
     * @param customers
     * @return
     */
    public int bestClosingTime(String customers) {
        int totalY = 0;
        var arr = customers.toCharArray();
        for (var c : arr) {
            if (c == 'Y')
                totalY++;
        }
        int minPenalty = totalY;
        int res = 0;
        int currentY = 0;
        int currentN = 0;
        for (int i = 0; i <= arr.length; i++) {
            int penalty = currentN + totalY - currentY;
            if (penalty < minPenalty) {
                minPenalty = penalty;
                res = i;
            }
            if (i < arr.length) {
                var c = arr[i];
                if (c == 'Y')
                    currentY++;
                else
                    currentN++;
            }
        }

        return res;
    }
}
