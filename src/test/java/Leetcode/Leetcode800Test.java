package Leetcode;

import org.junit.jupiter.api.Test;

import java.util.List;

import static Leetcode.Leetcode800.partitionLabels;
import static org.junit.jupiter.api.Assertions.*;

class Leetcode800Test {

    @Test
    void partitionLabelsTest() {
        assertEquals(List.of(9,7,8),partitionLabels("ababcbacadefegdehijhklij"));
    }
}