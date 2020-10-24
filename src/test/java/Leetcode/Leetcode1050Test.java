package Leetcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static Leetcode.Leetcode1050.*;

class Leetcode1050Test {

    @Test
    void videoStitchingTest() {
        assertEquals(3, videoStitching(new int[][]{{0,2},{4,6},{8,10},{1,9},{1,5},{5,9}},10));
        assertEquals(-1, videoStitching(new int[][]{{0,1},{1,2}},5));
        assertEquals(3, videoStitching(
                new int[][]{{0,1},{6,8},{0,2},{5,6},{0,4},{0,3},{6,7},{1,3},{4,7},
                        {1,4},{2,5},{2,6},{3,4},{4,5},{5,7},{6,9}},
                9));
        assertEquals(2, videoStitching(new int[][]{{0,4},{2,8}},5));
    }
}