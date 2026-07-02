package Leetcode;

public class DisjointSet {
    public int[] parent;
    int[] rank;

    DisjointSet(int len) {
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

    boolean isLinked(int a, int b) {
        return parent(a) == parent(b);
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
