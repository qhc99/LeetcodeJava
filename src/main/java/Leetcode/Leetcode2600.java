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
     * #2506
     * 
     * @param words
     * @return
     */
    public int similarPairs(String[] words) {
        Map<Integer, Integer> count = new HashMap<>();
        int res = 0;
        for (var w : words) {
            int b = str2bytes(w);
            var bc = count.getOrDefault(b, 0);
            res += bc;
            count.put(b, 1 + bc);
        }
        return res;
    }

    int str2bytes(String s) {
        int res = 0;
        for (var c : s.toCharArray())
            res |= (1 << (c - 'a'));
        return res;
    }

    /**
     * #2523
     * 
     * @param left
     * @param right
     * @return
     */
    public int[] closestPrimes(int left, int right) {
        boolean[] notPrime = new boolean[right + 1];
        notPrime[0] = notPrime[1] = true;

        for (int n = 2; n <= right; n++) {
            if (!notPrime[n]) {
                for (int i = 2; i * n <= right; i++) {
                    notPrime[i * n] = true;
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        for (int n = left; n <= right; n++) {
            if (!notPrime[n]) {
                res.add(n);
            }
        }
        int[] ans = new int[] { -1, -1 };
        if (res.size() >= 2) {
            ans[0] = res.get(0);
            ans[1] = res.get(1);
            for (int i = 1; i < res.size() - 1; i++) {
                var n1 = res.get(i);
                var n2 = res.get(i + 1);
                if (n2 - n1 < ans[1] - ans[0]) {
                    ans[0] = n1;
                    ans[1] = n2;
                }
            }
        }
        return ans;

    }

    /**
     * #2530
     * 
     * @param nums
     * @param k
     * @return
     */
    public long maxKelements(int[] nums, int k) {
        long res = 0;
        Queue<Integer> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(b, a));
        for (var n : nums)
            queue.add(n);
        for (int i = 0; i < k; i++) {
            var n = queue.poll();
            res += n;
            queue.add(Math.ceilDiv(n, 3));
        }
        return res;
    }

    /**
     * #2537
     * 
     * @param nums
     * @param k
     * @return
     */
    public long countGood(int[] nums, int k) {
        long res = 0;
        long good = 0;
        Map<Integer, Integer> count = new HashMap<>();
        int l = 0;
        for (int r = 0; r < nums.length; r++) {
            var n = nums[r];
            var cnt = count.getOrDefault(n, 0);
            good += cnt;
            count.put(n, 1 + cnt);
            while (good >= k && l <= r) {
                res += nums.length - r;
                var lNum = nums[l++];
                var lCnt = count.get(lNum);
                if (lCnt > 1)
                    count.put(lNum, lCnt - 1);
                else
                    count.remove(lNum);
                good -= lCnt - 1;
            }
        }
        return res;
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
     * #2551
     * 
     * @param weights
     * @param k
     * @return
     */
    public long putMarbles(int[] weights, int k) {
        long max = weights[0] + weights[weights.length - 1];
        long min = max;
        int[] sum = new int[weights.length - 1];
        for (int i = 0; i < sum.length; i++) {
            sum[i] = weights[i] + weights[i + 1];
        }
        Queue<Integer> maxQueue = new PriorityQueue<>(
                (a, b) -> Integer.compare(b, a));
        Queue<Integer> minQueue = new PriorityQueue<>();
        for (var s : sum) {
            maxQueue.add(s);
            minQueue.add(s);
        }
        for (; k > 1; k--) {
            max += maxQueue.poll();
            min += minQueue.poll();
        }
        return max - min;
    }

    /**
     * #2555
     * @param prizePositions
     * @param k
     * @return
     */
    public int maximizeWin(int[] prizePositions, int k) {
        int total = 0;
        int l = 0;
        int[] dp = new int[prizePositions.length];
        for (int r = 0; r < prizePositions.length; r++) {
            while (l <= r && prizePositions[r] - prizePositions[l] > k) {
                l++;
            }

            dp[r] = Math.max((r - 1 >= 0 ? dp[r - 1] : 0), r + 1 - l);
            total = Math.max(total, r + 1 - l + (l - 1 >= 0 ? dp[l - 1] : 0));
        }

        return total;
    }

    /**
     * #2561
     * 
     * @param basket1
     * @param basket2
     * @return
     */
    public long minCost(int[] basket1, int[] basket2) {
        Map<Integer, Integer> count = new HashMap<>();
        int min = Integer.MAX_VALUE;
        for (var v : basket1) {
            count.put(v, 1 + count.getOrDefault(v, 0));
            min = Math.min(v, min);
        }
        for (var v : basket2) {
            count.put(v, count.getOrDefault(v, 0) - 1);
            min = Math.min(v, min);
        }

        List<Integer> notMatch = new ArrayList<>();
        for (var k : count.keySet()) {
            var v = count.get(k);
            if (v % 2 != 0)
                return -1;
            for (int i = 0; i < Math.abs(v) / 2; i++) {
                notMatch.add(k);
            }
        }
        notMatch.sort(Integer::compare);
        long res = 0;
        for (int i = 0; i < notMatch.size() / 2; i++) {
            res += Math.min(2 * min, notMatch.get(i));
        }
        return res;
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
     * #2591
     * 
     * @param money
     * @param children
     * @return
     */
    public int distMoney(int money, int children) {
        money -= children; // all 1
        if (money < 0)
            return -1;
        int res = Math.min(money / 7, children);
        money -= 7 * res;
        if ((children - res == 0 && money != 0)
                || (children - res == 1 && money == 3)) {
            res--;
        }
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
