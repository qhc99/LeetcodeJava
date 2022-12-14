package Leetcode;

import java.util.Random;

public class LocalityDemo {

    record MatRange(int r1, int c1, int r2, int c2) {
    }

    static class PartitionMat {
        private final int p;
        final int rows;
        final int cols;
        private final int m;
        private final int n;

        PartitionMat(float[][] m, int parti) {
            p = parti;
            this.m = m.length;
            this.n = m[0].length;
            rows = (int) Math.ceil(this.m / (float) parti);
            cols = (int) Math.ceil(this.n / (float) parti);
        }


        MatRange at(int i, int j) {
            return new MatRange(i * p, j * p,
                    Math.min(i * p + p, m), Math.min(j * p + p, n));
        }
    }

    static void mul(float[][] m1, MatRange m1r,
                    float[][] m2, MatRange m2r,
                    float[][] m3, MatRange m3r) {
        for (int i = 0; i + m3r.r1 < m3r.r2; i++) {
            for (int j = 0; j + m3r.c1 < m3r.c2; j++) {
                for (int k = 0; k + m2r.r1 < m2r.r2; k++) {
                    m3[i + m3r.r1][j + m3r.c1] +=
                            m1[i + m1r.r1][k + m1r.c1] * m2[k + m2r.r1][j + m2r.c1];
                }
            }
        }
    }
    public static void runDemo(){
        Random r = new Random();

        int size_a = 1300;
        int size_b = 1300;
        int size_c = 1300;
        float[][] m1 = new float[size_a][size_b];
        float[][] m2 = new float[size_b][size_c];
        float[][] m3 = new float[size_a][size_c];

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

        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_c; j++) {
                m3[i][j] = 0;
            }
        }

        var t1 = System.nanoTime();
        int w_s = 32;
        PartitionMat a1 = new PartitionMat(m1, w_s);
        PartitionMat a2 = new PartitionMat(m2, w_s);
        PartitionMat a3 = new PartitionMat(m3, w_s);

        for (int i = 0; i < a3.rows; i++) {
            for (int j = 0; j < a3.cols; j++) {
                MatRange m3r = a3.at(i, j);
                for (int k = 0; k < a1.cols; k++) {
                    MatRange m1r = a1.at(i, k);
                    MatRange m2r = a2.at(k, j);
                    mul(m1, m1r, m2, m2r, m3, m3r);
                }
            }
        }
        var t2 = System.nanoTime();

        float ans = 0;
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_a; j++) {
                ans += m3[i][j];
            }
        }
        System.out.println("locality optimization:");
        System.out.printf("spend %.2fms%n",(t2 - t1) / 1000000.);
        System.out.printf("sum: %f\n",ans);
        System.out.println();

        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_c; j++) {
                m3[i][j] = 0;
            }
        }
        var t3 = System.nanoTime();
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_c; j ++) {
                for (int k = 0; k < size_b; k++) {
                    m3[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        var t4 = System.nanoTime();

        ans = 0;
        for (int i = 0; i < size_a; i++) {
            for (int j = 0; j < size_a; j++) {
                ans += m3[i][j];
            }
        }


        System.out.println("naive implementation:");
        System.out.printf("spend %.2fms%n",(t4 - t3) / 1000000.);
        System.out.printf("sum: %f\n",ans);
    }
}
