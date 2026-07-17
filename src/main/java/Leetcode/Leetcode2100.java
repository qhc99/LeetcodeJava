package Leetcode;

import java.util.*;

public class Leetcode2100 {
    /**
     * #2043 Bank
     */
    class Bank {

        long[] balance;

        public Bank(long[] balance) {
            this.balance = balance;
        }

        boolean invalidAccount(int a) {
            return a < 1 || a > balance.length;
        }

        public boolean transfer(int account1, int account2, long money) {
            if (invalidAccount(account1) || invalidAccount(account2)
                    || balance[account1 - 1] < money)
                return false;
            balance[account1 - 1] -= money;
            balance[account2 - 1] += money;
            return true;
        }

        public boolean deposit(int account, long money) {
            if (invalidAccount(account))
                return false;
            balance[account - 1] += money;
            return true;
        }

        public boolean withdraw(int account, long money) {
            if (invalidAccount(account) || balance[account - 1] < money)
                return false;
            balance[account - 1] -= money;
            return true;
        }
    }
    /**
     * #2013 DetectSquares
     */
    class DetectSquares {

        static class Point {
            private final int x;
            private final int y;

            public Point(int[] data) {
                if (data == null || data.length != 2) {
                    throw new IllegalArgumentException(
                            "Point must have exactly 2 values");
                }
                this.x = data[0];
                this.y = data[1];
            }

            @Override
            public boolean equals(Object o) {
                if (this == o)
                    return true;
                if (!(o instanceof Point other))
                    return false;
                return x == other.x && y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }

        Map<Integer, List<int[]>> xAxis = new HashMap<>();
        Map<Integer, List<int[]>> yAxis = new HashMap<>();
        Map<Point, Integer> count = new HashMap<>();

        public DetectSquares() {

        }

        public void add(int[] point) {
            xAxis.computeIfAbsent(point[0], t -> new ArrayList<>()).add(point);
            yAxis.computeIfAbsent(point[1], t -> new ArrayList<>()).add(point);
            count.put(new Point(point),
                    count.getOrDefault(new Point(point), 0) + 1);
        }

        public int count(int[] point) {
            var xPoints = xAxis.getOrDefault(point[0], List.of());
            var yPoints = yAxis.getOrDefault(point[1], List.of());
            Set<Integer> yOfXPoints = new HashSet<>(
                    xPoints.stream().map(p -> p[1]).toList());
            Set<Integer> xOfYPoints = new HashSet<>(
                    yPoints.stream().map(p -> p[0]).toList());
            int res = 0;
            for (var x : xOfYPoints) {
                for (var y : yOfXPoints) {
                    if (x != point[0] && Math.abs(x - point[0]) == Math
                            .abs(y - point[1])) {
                        var corner1 = count.getOrDefault(
                                new Point(new int[] { x, point[1] }), 0);
                        var corner2 = count.getOrDefault(
                                new Point(new int[] { point[0], y }), 0);
                        var corner3 = count
                                .getOrDefault(new Point(new int[] { x, y }), 0);
                        res += corner1 * corner2 * corner3;
                    }
                }
            }
            return res;
        }
    }
}
