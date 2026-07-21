package Leetcode;

import java.math.BigInteger;
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
     * #2539
     * 
     * @param s
     * @return
     */
    public int countGoodSubsequences(String s) {
        long res = 0;
        int[] freq = new int['z' - 'a' + 1];
        int maxFreq = 0;
        for (var c : s.toCharArray()) {
            maxFreq = Math.max(maxFreq, ++freq[c - 'a']);
        }
        for (int f = 1; f <= maxFreq; f++) {
            long t = 1;
            for (var cf : freq) {
                if (cf >= f) {
                    t = t * (nCr(cf, f) + 1) % (1_000_000_000 + 7);
                }
            }
            res = (res + (t - 1)) % (1_000_000_000 + 7);
        }

        return (int) res;
    }

    private static final int MOD = 1_000_000_007;
    private static final int N = 10_001;

    private static final long[] fact = new long[N];
    private static final long[] invFact = new long[N];

    static {
        fact[0] = 1;

        for (int i = 1; i < N; i++) {
            fact[i] = fact[i - 1] * i % MOD;
        }

        invFact[N - 1] = pow(fact[N - 1], MOD - 2);

        for (int i = N - 1; i >= 1; i--) {
            invFact[i - 1] = invFact[i] * i % MOD;
        }
    }

    private static long pow(long base, long exponent) {
        long result = 1;

        while (exponent > 0) {
            if ((exponent & 1) != 0) {
                result = result * base % MOD;
            }

            base = base * base % MOD;
            exponent >>= 1;
        }

        return result;
    }

    private long nCr(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }

        return fact[n] * invFact[r] % MOD * invFact[n - r] % MOD;
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

    /**
     * #2592
     * 
     * @param nums
     * @return
     */
    public int maximizeGreatness(int[] nums) {
        Arrays.sort(nums);
        int res = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        for (var n : nums) {
            if (!queue.isEmpty() && queue.peek() < n) {
                queue.poll();
                res++;
            }
            queue.add(n);
        }
        return res;
    }
}
