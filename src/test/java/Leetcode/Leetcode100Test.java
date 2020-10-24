package Leetcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static Leetcode.Leetcode100.*;

class Leetcode100Test {

    @Test
    void canJumpTest() {
        assertTrue(canJump(new int[]{2,3,1,1,4}));
        assertFalse(canJump(new int[]{3,2,1,0,4}));
    }
}