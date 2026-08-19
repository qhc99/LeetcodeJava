package Leetcode;

public class GospherHack {
    static void function(int n) {
        for (int k = 1; k <= n; k++) {
            int mask = (1 << k) - 1;

            while (mask < (1 << n)) {
                // use mask
                System.out.println(Integer.toBinaryString(mask));

                int c = mask & -mask;
                int r = mask + c;
                mask = (((r ^ mask) >>> 2) / c) | r;
            }
        }
    }
}
