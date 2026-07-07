package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

interface Master {
    public int guess(String word);
}

@SuppressWarnings("JavaDoc")
public class Leetcode900 {
    /**
     * #853
     * 
     * @param target
     * @param position
     * @param speed
     * @return
     */
    public int carFleet(int target, int[] position, int[] speed) {
        var pos2speed = new TreeMap<Integer, Integer>();
        for (int i = 0; i < position.length; i++) {
            pos2speed.put(position[i], speed[i]);
        }
        int res = 0;
        double fleetTime = 0;
        while (!pos2speed.isEmpty()) {
            var car = pos2speed.pollLastEntry();
            double time = (target - car.getKey()) / (double) car.getValue();
            if (res != 0 && time < fleetTime) {
            } else {
                fleetTime = (target - car.getKey()) / (double) car.getValue();
                res++;
            }

        }
        return res;
    }

    /**
     * #854
     * 
     * @param s1
     * @param s2
     * @return
     */
    public int kSimilarity(String s1, String s2) {
        Set<Anagram> seen = new HashSet<>();
        Queue<Anagram> queue = new ArrayDeque<>();
        var target = s2.toCharArray();
        {
            var ag = new Anagram(s1.toCharArray(), 0, 0);
            queue.add(ag);
            seen.add(ag);
        }
        while (!queue.isEmpty()) {
            var ag = queue.poll();

            int head = ag.idx;
            while (head < ag.state.length && ag.state[head] == target[head]) {
                head++;
            }
            if (head == target.length)
                return ag.count;

            for (int i = head + 1; i < target.length; i++) {
                if (target[head] == ag.state[i]) {
                    var next_state = new char[s1.length()];
                    System.arraycopy(ag.state, 0, next_state, 0, target.length);
                    var t = next_state[head];
                    next_state[head] = next_state[i];
                    next_state[i] = t;
                    var a = new Anagram(next_state, head + 1, ag.count + 1);
                    if (!seen.contains(a)) {
                        queue.add(a);
                        seen.add(a);
                    }
                }
            }
        }
        return 0;
    }

    static record Anagram(char[] state, int idx, int count) {

        @Override
        public final int hashCode() {
            return Objects.hash(Arrays.hashCode(state));
        }

        @Override
        public final boolean equals(Object arg0) {
            if (arg0 instanceof Anagram other) {
                return Arrays.equals(state, other.state);
            }
            return false;
        }
    }

    /**
     * #855
     * 
     */
    public class ExamRoom {
        class Range implements Comparable<Range> {
            int[] arr = new int[2];

            public Range(int a, int b) {
                arr[0] = a;
                arr[1] = b;
            }

            public int left() {
                return arr[0];
            }

            public int right() {
                return arr[1];
            }

            public int len() {
                return (arr[0] == -1 || arr[1] == count) ? arr[1] - arr[0] - 1
                        : (arr[1] - arr[0]) >> 1;
            }

            public int seat() {
                if (arr[0] == -1) {
                    return 0;
                }
                return arr[0] + len();
            }

            @Override
            public int compareTo(Range o) {
                var c = Integer.compare(this.len(), o.len());
                if (c == 0) {
                    return Integer.compare(o.seat(), seat());
                }
                return c;
            }

        }

        TreeSet<Range> treeSet = new TreeSet<>();
        HashMap<Integer, Range> left;
        HashMap<Integer, Range> right;
        int count;

        public ExamRoom(int n) {
            count = n;
            left = new HashMap<>();
            right = new HashMap<>();
            var range = new Range(-1, n);
            treeSet.add(range);
            left.put(-1, range);
            right.put(n, range);
        }

        void addRange(Range r) {
            treeSet.add(r);
            left.put(r.left(), r);
            right.put(r.right(), r);
        }

        void removeRange(Range r) {
            treeSet.remove(r);
            left.remove(r.left());
            right.remove(r.right());
        }

        public int seat() {
            var range = treeSet.pollLast();
            var s = range.seat();
            var l_range = new Range(range.left(), s);
            var r_range = new Range(s, range.right());
            left.remove(range.left());
            right.remove(range.right());
            if (l_range.len() > 0) {
                addRange(l_range);
            }
            if (r_range.len() > 0) {
                addRange(r_range);
            }
            return s;
        }

        public void leave(int p) {
            var l_range = right.get(p);
            var r_range = left.get(p);
            if (l_range != null) {
                removeRange(l_range);
            }
            if (r_range != null) {
                removeRange(r_range);
            }
            Range new_range = null;
            if (l_range != null && r_range != null) {
                new_range = new Range(l_range.left(), r_range.right());
            } else if (l_range != null) {
                new_range = new Range(l_range.left(), p + 1);
            } else if (r_range != null) {
                new_range = new Range(p - 1, r_range.right());
            } else {
                new_range = new Range(p - 1, p + 1);
            }
            addRange(new_range);
        }
    }

    /**
     * #875
     * 
     * @param piles
     * @param h
     * @return
     */
    public int minEatingSpeed(int[] piles, int h) {
        int l = 1, r = Arrays.stream(piles).max().getAsInt();
        if (piles.length == h) {
            return r;
        }
        while (r - l >= 1) {
            int mid = l + (r - l) / 2;
            int time = Arrays.stream(piles).map(p -> Math.ceilDiv(p, mid))
                    .sum();
            if (time > h) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    /**
     * #876
     *
     * @param head
     * @return
     */
    public static ListNode middleNode(ListNode head) {
        if (head.next == null)
            return head;
        ListNode slow = head, fast = head;
        while (fast != null) {
            slow = slow.next;
            fast = fast != null ? fast.next : null;
            fast = fast != null ? fast.next : null;
            if (fast != null && fast.next == null) {
                return slow;
            }
        }
        return slow;
    }

    /**
     * #878 periodical method
     *
     * @param n
     * @param a
     * @param b
     * @return
     */
    public static int nthMagicalNumber(int n, int a, int b) {
        class Data {
            final long num;
            final long multiplier;
            final long result;

            Data(long n, long m, long r) {
                num = n;
                multiplier = m;
                result = r;
            }
        }
        int mod = (int) (1e9 + 7);
        var lcm_num = lcm(a, b);
        var bound_a = lcm_num / a;
        var bound_b = lcm_num / b;
        var len = bound_a + bound_b - 1;
        List<Data> array = new ArrayList<>((int) len);
        for (long i = 1; i < bound_a; i++) {
            array.add(new Data(a, i, a * i));
        }
        for (long i = 1; i < bound_b; i++) {
            array.add(new Data(b, i, b * i));
        }
        array.add(new Data(a, bound_a, a * bound_a));
        array.sort(Comparator.comparingLong(d -> d.result));

        var iter = (n - 1) / len;
        var idx = (n - 1) % len;
        var d = array.get((int) idx);
        var num = d.num;
        var mul = d.multiplier;
        if (num == a)
            return (int) (num * ((mul + iter * bound_a) % mod) % mod);
        else
            return (int) (num * ((mul + iter * bound_b) % mod) % mod);
    }

    private static long gcd(long a, long b) {
        return b != 0 ? gcd(b, a % b) : a;
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }
}

/**
 * #895
 */
class FreqStack {

    Map<Integer, Integer> val2freq = new HashMap<>();
    Map<Integer, Deque<Integer>> freq2stack = new HashMap<>();
    int max_freq = 0;

    public FreqStack() {

    }

    public void push(int val) {
        var freq = val2freq.getOrDefault(val, 0) + 1;
        val2freq.put(val, freq);
        max_freq = Math.max(freq, max_freq);
        freq2stack.computeIfAbsent(freq, (k) -> new ArrayDeque<>())
                .addLast(val);
    }

    public int pop() {
        var stack = freq2stack.get(max_freq);
        var val = stack.pollLast();
        if (stack.isEmpty()) {
            freq2stack.remove(max_freq--);
        }
        if (val2freq.get(val) == 1) {
            val2freq.remove(val);
        } else {
            val2freq.put(val, val2freq.get(val) - 1);
        }
        return val;
    }
}