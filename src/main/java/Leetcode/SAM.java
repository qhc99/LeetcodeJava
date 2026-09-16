package Leetcode;

import java.util.*;

class SAM {
    int[][] next;
    int[] link;
    int[] len;

    int size = 1;
    int last = 0;

    SAM(int n) {
        next = new int[2 * n][26];
        link = new int[2 * n];
        len = new int[2 * n];

        for (int[] row : next) {
            Arrays.fill(row, -1);
        }

        Arrays.fill(link, -1);
    }

    void extend(char ch) {
        int c = ch - 'a';

        int cur = size++;
        len[cur] = len[last] + 1;

        int p = last;

        while (p != -1 && next[p][c] == -1) {
            next[p][c] = cur;
            p = link[p];
        }

        if (p == -1) {
            link[cur] = 0;
        } else {
            int q = next[p][c];

            if (len[p] + 1 == len[q]) {
                link[cur] = q;
            } else {
                int clone = size++;

                len[clone] = len[p] + 1;
                next[clone] = next[q].clone();
                link[clone] = link[q];

                while (p != -1 && next[p][c] == q) {
                    next[p][c] = clone;
                    p = link[p];
                }

                link[q] = clone;
                link[cur] = clone;
            }
        }

        last = cur;
    }

    long countDistinct() {
        long ans = 0;
        for (int v = 1; v < size; v++) {
            ans += len[v] - len[link[v]];
        }
        return ans;
    }
}
