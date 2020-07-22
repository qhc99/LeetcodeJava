package src;

public class Leetcode51_100 {
    // #96

    // count of all binary search tree given range [1,n]
    //  3 -> 5
    //    1         3     3      2      1
    //     \       /     /      / \      \
    //      3     2     1      1   3      2
    //     /     /       \                 \
    //    2     1         2                 3
    public static int numTrees(int n) {
        int[] G = new int[n + 1];
        G[0] = 1;
        G[1] = 1;
        for (int i = 2; i <= n; i++) {
            G[i] = 0;
            for (int j = 1; j <= i; j++) {
                G[i] += G[j - 1] * G[i - j];
            }
        }
        return G[n];
    }
}
