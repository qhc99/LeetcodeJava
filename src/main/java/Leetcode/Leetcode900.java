package Leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
