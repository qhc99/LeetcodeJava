package Leetcode;

import java.util.*;

public class Leetcode3500 {
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
            if(Character.isDigit(cell.charAt(0))){
                return null;
            }
            var c = cell.charAt(0) - 'A';
            var r = Integer.valueOf(cell.substring(1)) - 1;
            return new int[] { r, c };
        }
    }
}
