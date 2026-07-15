package Leetcode;

import java.util.*;

import Leetcode.Leetcode2600.Allocator.Block;

public class Leetcode2600 {
    /**
     * #2502 Allocator
     */
    class Allocator {
        static record Block(int s, int e) {
        }

        TreeMap<Integer, Block> left = new TreeMap<>();
        TreeMap<Integer, Block> right = new TreeMap<>();
        Map<Integer, List<Block>> alloc = new HashMap<>();

        public Allocator(int n) {
            var b = new Block(0, n);
            left.put(0, b);
            right.put(n, b);
        }



        public int allocate(int size, int mID) {
            for (var e : left.entrySet()) {
                var b = e.getValue();
                if (b.e - b.s >= size) {
                    left.remove(b.s);
                    right.remove(b.e);
                    var remainder = new Block(b.s + size, b.e);
                    if (remainder.e - remainder.s > 0) {
                        left.put(remainder.s, remainder);
                        right.put(remainder.e, remainder);
                    }
                    alloc.computeIfAbsent(mID, k -> new ArrayList<>())
                            .add(new Block(b.s, b.s + size));
                    return b.s;
                }
            }
            return -1;
        }

        public int freeMemory(int mID) {
            var bs = alloc.remove(mID);
            if (bs == null)
                bs = List.of();
            for (var b : bs) {
                if (right.containsKey(b.s)) {
                    var l = right.get(b.s);
                    left.remove(l.s);
                    right.remove(l.e);
                    b = new Block(l.s, b.e);
                    left.put(b.s, b);
                    right.put(b.e, b);
                }
                if (left.containsKey(b.e)) {
                    var r = left.get(b.e);
                    left.remove(b.s);
                    right.remove(b.e);
                    left.remove(r.s);
                    right.remove(r.e);
                    b = new Block(b.s, r.e);
                    left.put(b.s, b);
                    right.put(b.e, b);
                }
            }
            return bs.size();
        }
    }

}
