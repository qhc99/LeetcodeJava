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
            putBlock(b);
        }

        void putBlock(Block b) {
            left.put(b.s, b);
            right.put(b.e, b);
        }

        void removeBlock(Block b) {
            left.remove(b.s);
            right.remove(b.e);
        }

        public int allocate(int size, int mID) {
            for (var e : left.entrySet()) {
                var b = e.getValue();
                if (b.e - b.s >= size) {
                    removeBlock(b);
                    var remainder = new Block(b.s + size, b.e);
                    if (remainder.e - remainder.s > 0) {
                        putBlock(remainder);
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
            int res = 0;
            if (bs == null)
                return res;
            for (var b : bs) {
                res += b.e - b.s;
                var l = right.get(b.s);
                if (l != null) {
                    removeBlock(l);
                    b = new Block(l.s, b.e);
                }
                var r = left.get(b.e);
                if (r != null) {
                    removeBlock(r);
                    b = new Block(b.s, r.e);
                }
                putBlock(b);
            }
            return res;
        }
    }

    /**
     * #2571
     * 
     * @param n
     * @return
     */
    public int minOperations(int n) {
        int res = 0;
        boolean[] bits = new boolean[32];
        for (int i = 0; i < 32; i++) {
            bits[i] = ((n >> i) & 1) == 1;
        }
        boolean add = false;
        for (int i = 0; i < 32; i++) {
            if (add && !bits[i]) {
                bits[i] = true;
                add = false;
            } else if (add && bits[i]) {
                bits[i] = false;
            }
            if (bits[i] && (i >= 32 || !bits[i + 1])) {
                res++; // subtract
            } else if (bits[i] && (i < 32 && bits[i + 1])) {
                res++; // add
                add = true;
            }
        }
        // no overflow
        return res;
    }
}
