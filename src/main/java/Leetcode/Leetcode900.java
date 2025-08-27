package Leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

interface Master {
    public int guess(String word);
}

@SuppressWarnings("JavaDoc")
public class Leetcode900 {
    /**
     * #854
     * 
     * @param s1
     * @param s2
     * @return
     */
    public int kSimilarity(String s1, String s2) {
        var a = s1.toCharArray();
        var b = s2.toCharArray();
        int[] map = new int['z' - 'a' + 1];
        Arrays.fill(map, -1);
        int m = 0;
        for (var c : a) {
            if (map[c - 'a'] == -1) {
                map[c - 'a'] = m++;
            }
        }
        int[] mapped_b = new int[b.length];
        for (int i = 0; i < b.length; i++) {
            mapped_b[i] = map[b[i] - 'a'];
        }

        int[] ans = new int[1];
        count(mapped_b, 0, mapped_b.length, ans);
        return ans[0];
    }

    private static void count(int[] a, int s, int e, int[] ans) {
        if (e - s <= 1) {
            return;
        }
        if (e - s == 2) {
            if (a[e - 1] < a[s]) {
                var t = a[s];
                a[s] = a[e - 1];
                a[e - 1] = t;
                ans[0]++;
            }
            return;
        }
        // divide
        int mid = (s + e) / 2;
        count(a, s, mid, ans);
        count(a, mid, e, ans);
        // conquer
        int[] l = new int[mid - s];
        int[] r = new int[e - mid];
        System.arraycopy(a, s, l, 0, mid - s);
        System.arraycopy(a, mid, r, 0, e - mid);
        int l_ptr = 0, r_ptr = 0;
        int a_ptr = 0;
        while (l_ptr < l.length && r_ptr < r.length) {
            if (r[r_ptr] < l[l_ptr]) {
                ans[0]++;
                a[s + a_ptr++] = r[r_ptr++];
            } else {
                a[s + a_ptr++] = l[l_ptr++];
            }
        }
        while (l_ptr < l.length) {
            a[s + a_ptr++] = l[l_ptr++];
        }
        while (r_ptr < r.length) {
            a[s + a_ptr++] = r[r_ptr++];
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
