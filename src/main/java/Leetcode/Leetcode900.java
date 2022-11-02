package Leetcode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings("JavaDoc")
public class Leetcode900 {

    /**
     * #876
     *
     * @param head
     * @return
     */
    public static ListNode middleNode(ListNode head) {
        if (head.next == null) return head;
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
     * #878
     * periodical method
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
        if (num == a) return (int) (num * ((mul + iter * bound_a) % mod) % mod);
        else return (int) (num * ((mul + iter * bound_b) % mod) % mod);
    }


    private static long gcd(long a, long b) {
        return b != 0 ? gcd(b, a % b) : a;
    }

    private static long lcm(long a, long b) {
        return a * b / gcd(a, b);
    }
}
