package Leetcode;

import java.util.Random;
import java.util.stream.IntStream;

public class LocalityDemo {

    record MatBlock(int r1, int c1, int r2, int c2) {
    }

    static class MatPartition {
        private final int size_block;
        final int rows;
        final int cols;
        private final int m;
        private final int n;

        MatPartition(float[][] m, int block_size) {
            size_block = block_size;
            this.m = m.length;
            this.n = m[0].length;
            rows = (int) Math.ceil(this.m / (float) block_size);
            cols = (int) Math.ceil(this.n / (float) block_size);
        }


        MatBlock at(int i, int j) {
            return new MatBlock(i * size_block, j * size_block,
                    Math.min(i * size_block + size_block, m), Math.min(j * size_block + size_block, n));
        }
    }

    static void mul_mat_block(float[][] m1, MatBlock m1r,
                              float[][] m2, MatBlock m2r,
                              float[][] m3, MatBlock m3r) {
        for (int i = 0; i + m3r.r1 < m3r.r2; i++) {
            for (int j = 0; j + m3r.c1 < m3r.c2; j++) {
                for (int k = 0; k + m2r.r1 < m2r.r2; k++) {
                    m3[i + m3r.r1][j + m3r.c1] +=
                            m1[i + m1r.r1][k + m1r.c1] * m2[k + m2r.r1][j + m2r.c1];
                }
            }
        }
    }

    public static void reset_mat(float[][] m1, float[][] m2) {
        Random r = new Random();
        int size_a = m1.length, size_b = m2.length, size_c = m2[0].length;
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_b; j++) {
                m1[i][j] = r.nextFloat();
            }
        }
        for (int i = 0; i < size_b; i++) {
            for (int j = 0; j < size_c; j++) {
                m2[i][j] = r.nextFloat();
            }
        }
    }

    public static void space_locality(float[][] m1, float[][] m2, float[][] m3) {
        int w_s = 64;
        int size_a = m1.length, size_b = m2.length, size_c = m2[0].length;
        MatPartition p1 = new MatPartition(m1, w_s);
        MatPartition p2 = new MatPartition(m2, w_s);
        MatPartition p3 = new MatPartition(m3, w_s);
        var t1 = System.nanoTime();

        for (int i = 0; i < p3.rows; i++) {
            for (int j = 0; j < p3.cols; j++) {
                MatBlock m3r = p3.at(i, j);
                for (int p = 0; p + m3r.r1 < m3r.r2; p++) {
                    for (int q = 0; q + m3r.c1 < m3r.c2; q++) {
                        m3[p + m3r.r1][q + m3r.c1] = 0;
                    }
                }
                for (int k = 0; k < p1.cols; k++) {
                    MatBlock m1r = p1.at(i, k);
                    MatBlock m2r = p2.at(k, j);
                    mul_mat_block(m1, m1r, m2, m2r, m3, m3r);
                }
            }
        }
        var t2 = System.nanoTime();

        double ans = 0;
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_a; j++) {
                ans += m3[i][j];
            }
        }
        System.out.println("locality optimization:");
        System.out.printf("spend %.2fms%n", (t2 - t1) / 1000000.);
        System.out.printf("sum: %f\n", ans);
        System.out.println();
    }

    public static void space_locality_parallel(float[][] m1, float[][] m2, float[][] m3) {
        var t1 = System.nanoTime();
        int w_s = 64;
        int size_a = m1.length, size_b = m2.length, size_c = m2[0].length;
        MatPartition p1 = new MatPartition(m1, w_s);
        MatPartition p2 = new MatPartition(m2, w_s);
        MatPartition p3 = new MatPartition(m3, w_s);

        IntStream.range(0, p3.rows).parallel().forEach(i -> {
            for (int j = 0; j < p3.cols; j++) {
                MatBlock m3r = p3.at(i, j);
                for (int p = 0; p + m3r.r1 < m3r.r2; p++) {
                    for (int q = 0; q + m3r.c1 < m3r.c2; q++) {
                        m3[p + m3r.r1][q + m3r.c1] = 0;
                    }
                }
                for (int k = 0; k < p1.cols; k++) {
                    MatBlock m1r = p1.at(i, k);
                    MatBlock m2r = p2.at(k, j);
                    mul_mat_block(m1, m1r, m2, m2r, m3, m3r);
                }
            }
        });
        var t2 = System.nanoTime();

        float ans = 0;
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_a; j++) {
                ans += m3[i][j];
            }
        }
        System.out.println("locality parallel optimization:");
        System.out.printf("spend %.2fms%n", (t2 - t1) / 1000000.);
        System.out.printf("sum: %f\n", ans);
        System.out.println();
    }

    public static void runDemo() {
        int size_a = 1300;
        int size_b = 1300;
        int size_c = 1300;
        float[][] m1 = new float[size_a][size_b];
        float[][] m2 = new float[size_b][size_c];
        float[][] m3 = new float[size_a][size_c];
        reset_mat(m1, m2);
        space_locality_parallel(m1, m2, m3);
        reset_mat(m1, m2);
        space_locality(m1, m2, m3);
    }
}
