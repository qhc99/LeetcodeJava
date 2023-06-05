package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc", "unused" })
public class Leetcode200 {

    /**
     * #152
     * <br/>
     * 乘积最大子数组
     *
     * @param nums numbers
     * @return max sub array multiple
     */
    public static int maxProduct(int[] nums) {
        int maxF = nums[0];
        int minF = nums[0];
        int ans = nums[0];
        int length = nums.length;
        for (int i = 1; i < length; ++i) {
            int mx = maxF;
            int mn = minF;
            maxF = Math.max(mx * nums[i], Math.max(nums[i], mn * nums[i]));
            minF = Math.min(mn * nums[i], Math.min(nums[i], mx * nums[i]));
            ans = Math.max(maxF, ans);
        }
        return ans;
    }

    /**
     * #155
     */
    public static class MinStack {

        Deque<Integer> deque = new LinkedList<>();
        Deque<Integer> minDeque = new LinkedList<>();

        public MinStack() {
            minDeque.add(Integer.MAX_VALUE);
        }

        public void push(int val) {
            deque.addLast(val);
            minDeque.addLast(Math.min(minDeque.getLast(), val));
        }

        public void pop() {
            deque.removeLast();
            minDeque.removeLast();
        }

        public int top() {
            return deque.getLast();
        }

        public int getMin() {
            return minDeque.getLast();
        }
    }

    /**
     * #160 相交链表
     *
     * @param headA linked list
     * @param headB linked list
     * @return intersect
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA == null || headB == null) {
            return null;
        }
        ListNode pA = headA;
        ListNode pB = headB;
        while (pA != pB) {
            pA = pA == null ? headB : pA.next;
            pB = pB == null ? headA : pB.next;
        }
        return pA;
    }

    /**
     * #162
     *
     * @param nums
     * @return
     */
    public static int findPeakElement(int[] nums) {
        int len = nums.length;
        if (len == 1) {
            return 0;
        }

        if (nums[0] > nums[1]) {
            return 0;
        } else if (nums[len - 1] > nums[len - 2]) {
            return len - 1;
        }
        for (int i = 1; i < len - 1; i++) {
            if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) {
                return i;
            }
        }

        throw new RuntimeException();
    }

    /**
     * #166
     *
     * @param numerator
     * @param denominator
     * @return
     */
    public static String fractionToDecimal(int numerator, int denominator) {
        StringBuilder sb = new StringBuilder();
        boolean geqZero = (long) numerator * (long) denominator >= 0;
        long nu = Math.abs((long) numerator);
        long de = Math.abs((long) denominator);
        var integer = nu / de;
        sb.append(integer);
        var frac = nu % de;
        if (frac != 0) {
            sb.append(".");
            int idx = sb.length();
            Map<Long, Integer> remainderToIdx = new HashMap<>(16);
            remainderToIdx.put(frac, idx);
            while (frac != 0) {
                var this_remainder = frac;
                frac *= 10;
                var d = frac / de;
                sb.append(d);

                idx++;
                frac %= de;

                var searchIdx = remainderToIdx.get(frac);
                if (searchIdx != null) {
                    sb.append(")");
                    sb.insert(searchIdx, "(");
                    break;
                } else {
                    remainderToIdx.put(this_remainder, idx - 1);
                }
            }
        }
        if (!geqZero) {
            sb.insert(0, "-");
        }
        return sb.toString();
    }

    /**
     * #173
     */
    public static class BSTIterator {
        private final Deque<TreeNode> stack = new LinkedList<>();
        private TreeNode ptr;
        private boolean poppedBefore = false;
        private boolean finish = false;

        public BSTIterator(TreeNode root) {
            ptr = root;
        }

        public int next() {
            if (finish) {
                throw new NoSuchElementException("Iterate finish.");
            }

            while (ptr != null) {
                if (ptr.left != null && !poppedBefore) // if popped before, walk to right
                {
                    stack.push(ptr);
                    ptr = ptr.left;
                } else {
                    var t = ptr;
                    if (ptr.right != null) {
                        ptr = ptr.right;
                        poppedBefore = false;
                    } else {
                        if (stack.size() != 0) {
                            ptr = stack.pop();
                            poppedBefore = true;
                        } else {
                            ptr = null;
                        }
                    }
                    return t.val;
                }
            }
            finish = true;
            throw new NoSuchElementException("Iterate finish.");
        }

        public boolean hasNext() {
            return !finish && ptr != null;
        }
    }

    /**
     * #190
     *
     * @param n number
     * @return binary reversed
     */
    public static int reverseBits(int n) {
        int M1 = 0x55555555; // 01010101010101010101010101010101
        int M2 = 0x33333333; // 00110011001100110011001100110011
        int M4 = 0x0f0f0f0f; // 00001111000011110000111100001111
        int M8 = 0x00ff00ff; // 00000000111111110000000011111111
        n = (n >>> 1) & M1 | ((n & M1) << 1);
        n = (n >>> 2) & M2 | ((n & M2) << 2);
        n = (n >>> 4) & M4 | ((n & M4) << 4);
        n = (n >>> 8) & M8 | ((n & M8) << 8);
        return n >>> 16 | n << 16;
    }

    /**
     * #200
     *
     * @param grid
     * @return
     */
    public static int numIslands(char[][] grid) {
        class BooleanMatrix {
            final BitSet[] matrix;

            public BooleanMatrix(int m, int n) {
                matrix = new BitSet[m];
                for (int i = 0; i < m; i++) {
                    matrix[i] = new BitSet(n);
                }
            }

            public boolean get(int m, int n) {
                return matrix[m].get(n);
            }

            public void set(int m, int n, boolean b) {
                matrix[m].set(n, b);
            }
        }
        int m = grid.length;
        int n = grid[0].length;
        var bMatrix = new BooleanMatrix(m, n);

        var funcVisit = new Object() {
            void apply(int i, int j) {
                if (grid[i][j] == '1' && !bMatrix.get(i, j)) {
                    bMatrix.set(i, j, true);
                    if (i - 1 >= 0) {
                        apply(i - 1, j);
                    }
                    if (i + 1 < m) {
                        apply(i + 1, j);
                    }
                    if (j + 1 < n) {
                        apply(i, j + 1);
                    }
                    if (j - 1 >= 0) {
                        apply(i, j - 1);
                    }
                }
            }
        };
        int ans = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !bMatrix.get(i, j)) {
                    ans++;
                    funcVisit.apply(i, j);
                }
            }
        }
        return ans;
    }
}
