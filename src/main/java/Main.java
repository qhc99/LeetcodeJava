import java.util.*;

import Leetcode.Leetcode2000;

import java.math.*;

public class Main {
    public static void main(String[] args) {
        var t = new Leetcode2000();
        // a -> x -> y
        // -> z
        // b -> x > y
        // -> z
        // -> w
        t.deleteDuplicateFolder(List.of(List.of("a"), List.of("a", "x"),
                List.of("a", "x", "y"), List.of("a", "z"), List.of("b"),
                List.of("b", "x"), List.of("b", "x", "y"), List.of("b", "z"),
                List.of("b", "w")));
    }
}
