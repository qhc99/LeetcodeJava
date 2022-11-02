package Leetcode;

public final class DPMatrix<V> {//matrix for dynamic programming problem

    public final int size;

    private final Object[][] matrix;

    public DPMatrix(int size) {
        this.size = size;
        matrix = new Object[size][];
        for (int i = 0; i < size; i++) {
            matrix[i] = new Object[size - i];
        }
    }


    @SuppressWarnings("unchecked")
    public V getAt(int r, int c) {
        return (V) matrix[r][c - r];
    }

    public void setAt(V v, int r, int c) {
        matrix[r][c - r] = v;
    }
}