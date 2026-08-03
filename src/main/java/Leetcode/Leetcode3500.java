package Leetcode;

import java.util.*;

public class Leetcode3500 {

    /**
     * #3408 TaskManager
     */
    class TaskManager {
        static record Task(int id, int userId) {
        }

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
