package Leetcode;

public class Disjointset {
    public int[] parent;
    int[] rank;

    Disjointset(int len) {
        parent = new int[len];
        rank = new int[len];
        for (int i = 0; i < len; i++) {
            parent[i] = i;
        }
    }

    int parent(int i) {
        if (parent[i] != i) {
            parent[i] = parent(parent[i]);
        }
        return parent[i];
    }


    void union(int a, int b) {
        var pa = parent(a);
        var pb = parent(b);
        if (pa == pb)
            return;
        if (rank[pa] < rank[pb]) {
            parent[pa] = pb;
        } else {
            parent[pb] = pa;
            if (rank[pa] == rank[pb])
                rank[pa]++;
        }
    }
}
