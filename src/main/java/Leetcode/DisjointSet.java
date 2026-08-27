package Leetcode;

public class Disjointset {
    public int[] prev;
    int[] rank;

    Disjointset(Disjointset d) {
        prev = new int[d.prev.length];
        rank = new int[d.rank.length];
        System.arraycopy(d.prev, 0, prev, 0, prev.length);
        System.arraycopy(d.rank, 0, rank, 0, rank.length);
    }

    Disjointset(int len) {
        prev = new int[len];
        rank = new int[len];
        for (int i = 0; i < len; i++) {
            prev[i] = i;
        }
    }

    int parent(int i) {
        if (prev[i] != i) {
            prev[i] = parent(prev[i]);
        }
        return prev[i];
    }

    void union(int a, int b) {
        var pa = parent(a);
        var pb = parent(b);
        if (pa == pb)
            return;
        if (rank[pa] < rank[pb]) {
            prev[pa] = pb;
        } else {
            prev[pb] = pa;
            if (rank[pa] == rank[pb])
                rank[pa]++;
        }
    }
}
