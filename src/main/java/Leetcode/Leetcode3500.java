package Leetcode;

import java.util.*;

public class Leetcode3500 {

    /**
     * #3408 TaskManager
     */
    class TaskManager {
        Map<Integer, Integer> task2Priority = new HashMap<>();
        Map<Integer, Integer> task2user = new HashMap<>();
        TreeMap<Integer, TreeSet<Integer>> priority2task = new TreeMap<>();

        public TaskManager(List<List<Integer>> tasks) {
            for (var task : tasks) {
                add(task.get(0), task.get(1), task.get(2));
            }
        }

        public void add(int userId, int taskId, int priority) {
            task2Priority.put(taskId, priority);
            task2user.put(taskId, userId);
            priority2task.computeIfAbsent(priority, k -> new TreeSet<>())
                    .add(taskId);
        }

        public void edit(int taskId, int newPriority) {
            var oldPriority = task2Priority.put(taskId, newPriority);
            var s = priority2task.get(oldPriority);
            s.remove(taskId);
            if (s.isEmpty())
                priority2task.remove(oldPriority);
            priority2task.computeIfAbsent(newPriority, k -> new TreeSet<>())
                    .add(taskId);
        }

        public void rmv(int taskId) {
            var priority = task2Priority.remove(taskId);
            task2user.remove(taskId);
            var s = priority2task.get(priority);
            s.remove(taskId);
            if (s.isEmpty())
                priority2task.remove(priority);
        }

        public int execTop() {
            var e = priority2task.lastEntry();
            if (e == null)
                return -1;
            var task = e.getValue().last();
            var user = task2user.get(task);
            rmv(task);
            return user;
        }
    }

    /**
     * #3441
     * 
     * @param caption
     * @return
     */
    public String minCostGoodCaption(String caption) {
        if (caption.length() < 3)
            return "";
        int[] dp = new int[caption.length() + 1];
        char[] selected = new char[caption.length() + 1];
        int[] size = new int[caption.length() + 1];
        dp[caption.length()] = 0;
        dp[caption.length() - 1] = dp[caption.length() - 2] = Integer.MAX_VALUE
                / 2;
        for (int i = caption.length() - 3; i >= 0; i--) {
            var subStr3 = caption.substring(i, i + 3).toCharArray();
            Arrays.sort(subStr3);
            char c3 = subStr3[1];
            int cost3 = subStr3[2] - subStr3[0];
            var cmp3 = new int[] { dp[i + 3] + cost3, c3, selected[i + 3],
                    selected[i + 3], selected[i + 3] };
            size[i] = 3;
            selected[i] = c3;

            var cmp = cmp3;
            if (i + 4 <= caption.length()) {
                var subStr4 = caption.substring(i, i + 4).toCharArray();
                Arrays.sort(subStr4);
                char c4 = subStr4[1];
                int cost4 = subStr4[2] + subStr4[3] - subStr4[0] - subStr4[1];
                var cmp4 = new int[] { dp[i + 4] + cost4, c4, c4,
                        selected[i + 4], selected[i + 4] };
                if (Arrays.compare(cmp4, cmp) < 0) {
                    cmp = cmp4;
                    selected[i] = c4;
                    size[i] = 4;
                }
            }
            if (i + 5 <= caption.length()) {
                var subStr5 = caption.substring(i, i + 5).toCharArray();
                Arrays.sort(subStr5);
                char c5 = subStr5[2];
                int cost5 = subStr5[3] + subStr5[4] - subStr5[0] - subStr5[1];
                var cmp5 = new int[] { dp[i + 5] + cost5, c5, c5, c5,
                        selected[i + 5] };
                if (Arrays.compare(cmp5, cmp) < 0) {
                    cmp = cmp5;
                    selected[i] = c5;
                    size[i] = 5;
                }
            }
            dp[i] = cmp[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < caption.length();) {
            sb.append(String.valueOf(selected[i]).repeat(size[i]));
            i += size[i];
        }
        return sb.toString();
    }

    /**
     * #3474
     * 
     * @param str1
     * @param str2
     * @return
     */
    public String generateString(String str1, String str2) {
        int m = str1.length(), n = str2.length();
        char[] res = new char[m + n - 1];
        boolean[] fixed = new boolean[m + n - 1];
        for (int i = 0; i < m; i++) {
            if (str1.charAt(i) == 'T') {
                for (int j = 0; j < n; j++) {
                    if (fixed[i + j] && res[i + j] != str2.charAt(j))
                        return "";
                    res[i + j] = str2.charAt(j);
                    fixed[i + j] = true;
                }
            }
        }
        for (int i = 0; i < m + n - 1; i++) {
            if (!fixed[i])
                res[i] = 'a';
        }
        for (int i = 0; i < m; i++) {
            if (str1.charAt(i) == 'F') {
                boolean eq = true;
                for (int j = 0; j < n; j++) {
                    if (res[i + j] != str2.charAt(j)) {
                        eq = false;
                        break;
                    }
                }
                if (eq) {
                    int j = n - 1;
                    while (fixed[i + j] && j >= 0) {
                        j--;
                    }
                    if (j < 0)
                        return "";
                    res[i + j] = 'b';
                }
            }
        }

        return String.valueOf(res);
    }

    /**
     * #3484 Spreadsheet
     */
    class Spreadsheet {
        Map<Integer, Map<Integer, Integer>> mat = new HashMap<>();

        public Spreadsheet(int rows) {
        }

        public void setCell(String cell, int value) {
            var cl = parse(cell);
            var r = cl[0];
            var c = cl[1];
            mat.computeIfAbsent(r, k -> new HashMap<>()).put(c, value);
        }

        public void resetCell(String cell) {
            setCell(cell, 0);
        }

        public int getValue(String formula) {
            var sp = formula.split("\\+");
            var s1 = sp[0].substring(1);
            var c1 = parse(s1);
            var c2 = parse(sp[1]);
            var res = 0;
            res += c1 == null ? Integer.valueOf(s1)
                    : mat.getOrDefault(c1[0], Map.of()).getOrDefault(c1[1], 0);
            res += c2 == null ? Integer.valueOf(sp[1])
                    : mat.getOrDefault(c2[0], Map.of()).getOrDefault(c2[1], 0);
            return res;
        }

        int[] parse(String cell) {
            if (Character.isDigit(cell.charAt(0))) {
                return null;
            }
            var c = cell.charAt(0) - 'A';
            var r = Integer.valueOf(cell.substring(1)) - 1;
            return new int[] { r, c };
        }
    }
}
