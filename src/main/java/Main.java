

import java.util.*;

import leetcode.Leetcode50;

public class Main {
    public static void main(String[] args){
        long t1 = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            char[][] t = new char[][]{
                    {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                    {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                    {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                    {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                    {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                    {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                    {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                    {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                    {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
            };
            Leetcode50.solveSudoku(t);
        }
        long t2 = System.nanoTime();
        System.out.println((t2-t1)/Math.pow(10,9));
    }
}
