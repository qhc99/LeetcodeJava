package Leetcode;

import static Leetcode.Leetcode950.isLongPressedName;
import static org.junit.jupiter.api.Assertions.*;

class Leetcode950Test {

    @org.junit.jupiter.api.Test
    void isLongPressedNameTest() {
        assertTrue(isLongPressedName("alex", "aaleex"));
        assertFalse(isLongPressedName("saeed", "ssaaedd"));
        assertTrue(isLongPressedName("leelee",  "lleeelee"));
        assertTrue(isLongPressedName("laiden",  "laiden"));
        assertTrue(isLongPressedName("vtkgn","vttkgnn"));
        assertFalse(isLongPressedName("pyplrz","ppyypllr"));
    }
}