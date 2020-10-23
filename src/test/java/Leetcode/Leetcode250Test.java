package Leetcode;

import org.junit.jupiter.api.Test;

import static Leetcode.Leetcode250.isPalindrome;
import static org.junit.jupiter.api.Assertions.*;

class Leetcode250Test {

    @Test
    void isPalindromeTest() {
        assertFalse(isPalindrome(new ListNode(1, new ListNode(2))));
        assertTrue(isPalindrome(new ListNode(1, new ListNode(2, new ListNode(2, new ListNode(1))))));
    }
}