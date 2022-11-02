package Leetcode;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("JavaDoc")
public class Leetcode300 {

    /**
     * #257
     *
     * @param root
     * @return
     */
    public static List<String> binaryTreePaths(TreeNode root) {
        StringBuilder stringStack = new StringBuilder();
        List<String> ans = new ArrayList<>();
        var recurFunc = new Object() {
            void apply(TreeNode node) {
                var idx = stringStack.length();
                if (node.left == null && node.right == null) {
                    if (stringStack.length() == 0) stringStack.append(node.val);
                    else stringStack.append("->").append(node.val);
                    ans.add(stringStack.toString());
                    stringStack.delete(idx, stringStack.length());
                }
                else {
                    if (node.left != null) {
                        if (stringStack.length() == 0) stringStack.append(node.val);
                        else stringStack.append("->").append(node.val);
                        apply(node.left);
                        stringStack.delete(idx, stringStack.length());
                    }
                    if (node.right != null) {
                        if (stringStack.length() == 0) stringStack.append(node.val);
                        else stringStack.append("->").append(node.val);
                        apply(node.right);
                        stringStack.delete(idx, stringStack.length());
                    }
                }
            }

        };
        recurFunc.apply(root);
        return ans;
    }

    /**
     * #274
     *
     * @param citations
     * @return
     */
    public static int hIndex(int[] citations) {
        Arrays.sort(citations);
        int n = citations.length;
        int s = 0, e = citations.length + 1;
        while (e - s > 1) {
            int mid = (s + e) / 2;
            if (mid == 0) {
                if (citations[citations.length - 1] <= mid) s = mid;
                else e = mid;
            }
            else {
                if (citations[n - mid] >= mid) s = mid;
                else e = mid;
            }
        }
        return s;
    }

    /**
     * #279
     *
     * @param n
     * @return
     */
    public static int numSquares(int n) {
        int[] squares = new int[(int) Math.sqrt(n)];
        for (int i = 1; i <= squares.length; i++) {
            squares[squares.length - i] = i * i;
        }
        int[] dp = new int[n + 1];
        dp[0] = 0;
        for (var s : squares) {
            dp[s] = 1;
        }
        if (dp[n] == 1) return 1;


        while (true) {
            for (int i = n; i > 0; i--) {
                for (var s : squares) {
                    if (i - s >= 0 && dp[i - s] != 0 && dp[i] == 0) {
                        dp[i] = dp[i - s] + 1;
                        if (i == n) return dp[i];
                    }
                }
            }
        }
    }

    /**
     * #282
     *
     * @param num
     * @param target
     * @return
     */
    public static List<String> addOperators(String num, int target) {
        var digits_ctr = num.toCharArray();
        List<String> ans = new CopyOnWriteArrayList<>();
        traceBackAddOperators(0, null, null, digits_ctr, target, ans);
        return ans;
    }

    static class SharedStack {
        final double num;
        final char ctr; // p d * / + -
        final SharedStack prev;

        SharedStack(double n, char c, SharedStack p) {
            num = n;
            ctr = c;
            prev = p;
        }
    }

    static class SharedString {
        final SharedString prev;
        final char ctr;

        SharedString(char c, SharedString p) {
            ctr = c;
            prev = p;
        }
    }

    public static void traceBackAddOperators(
            int idx,
            SharedStack stack,
            SharedString expr,
            char[] digits_ctr,
            int target,
            List<String> ans) {
        long current_digit = digits_ctr[idx] - '0';
        if (stack != null && stack.ctr == 'p')
            stack = new SharedStack(stack.prev.num * 10 + current_digit, 'd', stack.prev.prev);
        else
            stack = new SharedStack(current_digit, 'd', stack);
        expr = new SharedString(digits_ctr[idx], expr);

        if (idx == digits_ctr.length - 1) {
            var res = computeRightToLeft(stack);
            if (res == null) return;
            if (res.num == target) {
                StringBuilder sb = new StringBuilder();
                var ptr = expr;
                while (ptr != null) {
                    sb.insert(0, ptr.ctr);
                    ptr = ptr.prev;
                }
                ans.add(sb.toString());
            }
        }
        else {
            var mulDivMergedStack = mergeMulDiv(stack);
            if (mulDivMergedStack != null) {
                traceBackAddOperators(idx + 1, new SharedStack(0, '*', mulDivMergedStack), new SharedString('*', expr), digits_ctr, target, ans);
            }
            var computedStack = computeRightToLeft(stack);
            if (computedStack != null) {
                traceBackAddOperators(idx + 1, new SharedStack(0, '+', computedStack), new SharedString('+', expr), digits_ctr, target, ans);
                traceBackAddOperators(idx + 1, new SharedStack(0, '-', computedStack), new SharedString('-', expr), digits_ctr, target, ans);
            }
            if (stack.num != 0) {
                traceBackAddOperators(idx + 1, new SharedStack(0, 'p', stack), expr, digits_ctr, target, ans);
            }
        }
    }

    public static SharedStack computeRightToLeft(SharedStack stack) {
        var numR = stack.num;
        stack = stack.prev;
        while (stack != null) {
            var op = stack.ctr;
            stack = stack.prev;
            var numL = stack.num;
            stack = stack.prev;
            switch (op) {
                case '*' -> numR *= numL;
                case '/' -> {
                    if (numR == 0) return null;
                    else numR = numL / numR;
                }
                case '+' -> numR += numL;
                case '-' -> numR = numL - numR;
                default -> throw new RuntimeException(String.valueOf(op));
            }
        }
        return new SharedStack(numR, 'd', null);
    }

    public static SharedStack mergeMulDiv(SharedStack stack) {
        var numR = stack.num;
        stack = stack.prev;
        while (stack != null && (stack.ctr == '*' || stack.ctr == '/')) {
            var op = stack.ctr;
            stack = stack.prev;
            var numL = stack.num;
            stack = stack.prev;

            switch (op) {
                case '*' -> numR *= numL;
                case '/' -> {
                    if (numR == 0) return null;
                    numR = numL / numR;
                }
                default -> throw new RuntimeException();
            }
        }
        return new SharedStack(numR, 'd', stack);
    }

    public static List<String> addOperators2(String num, int target) {
        class StackData {
            final char ctr; // p d * + -
            final double num;

            StackData(double n, char c) {
                ctr = c;
                num = n;
            }
        }
        var digits_ctr = num.toCharArray();
        List<String> ans = new ArrayList<>();
        final List<Character> expr = new ArrayList<>();
        final List<StackData> stack = new ArrayList<>();
        var recurFunc = new Object() {

            void apply(int idx, List<StackData> stack) {
                if (stack.size() >= 1 && stack.get(stack.size() - 1).ctr == 'd') {
                    throw new RuntimeException();
                }
                int current_digit = digits_ctr[idx] - '0';
                boolean parsed = false;
                double before_parse_num = 0;
                if (stack.size() != 0 && stack.get(stack.size() - 1).ctr == 'p') {
                    stack.remove(stack.size() - 1);
                    before_parse_num = stack.remove(stack.size() - 1).num;
                    stack.add(new StackData(before_parse_num * 10 + current_digit, 'd'));
                    parsed = true;
                }
                else
                    stack.add(new StackData(current_digit, 'd'));

                expr.add(digits_ctr[idx]);

                if (idx == digits_ctr.length - 1) {
                    var res = computeRightToLeft(stack);
                    if (res.size() == 0) return;
                    if (res.get(0).num == target) {
                        var sb = new StringBuilder();
                        for (var c : expr) {
                            sb.append(c);
                        }
                        ans.add(sb.toString());
                    }
                }
                else {
                    var mulDivMergedStack = mergeMulDiv(stack);
                    expr.add('*');
                    if (mulDivMergedStack == stack) {
                        stack.add(new StackData(0, '*'));
                        apply(idx + 1, stack);
                        stack.remove(stack.size() - 1);
                    }
                    else {
                        mulDivMergedStack.add(new StackData(0, '*'));
                        apply(idx + 1, mulDivMergedStack);
                    }
                    expr.remove(expr.size() - 1);


                    var computedStack = computeRightToLeft(stack);
                    expr.add('+');
                    if (stack == computedStack) {
                        stack.add(new StackData(0, '+'));
                        apply(idx + 1, stack);
                        stack.remove(stack.size() - 1);
                    }
                    else {
                        computedStack.add(new StackData(0, '+'));
                        apply(idx + 1, computedStack);
                        computedStack.remove(computedStack.size() - 1);
                    }
                    expr.remove(expr.size() - 1);

                    expr.add('-');
                    if (stack == computedStack) {
                        stack.add(new StackData(0, '-'));
                        apply(idx + 1, stack);
                        stack.remove(stack.size() - 1);
                    }
                    else {
                        computedStack.add(new StackData(0, '-'));
                        apply(idx + 1, computedStack);
                    }
                    expr.remove(expr.size() - 1);

                    if (stack.get(stack.size() - 1).num != 0) {
                        stack.add(new StackData(0, 'p'));
                        apply(idx + 1, stack);
                        stack.remove(stack.size() - 1);
                    }
                }

                stack.remove(stack.size() - 1);
                if (parsed) {
                    stack.add(new StackData(before_parse_num, 'd'));
                    stack.add(new StackData(0, 'p'));
                }
                expr.remove(expr.size() - 1);
            }


            List<StackData> computeRightToLeft(List<StackData> stack) {
                if (stack.size() == 0) {
                    return stack;
                }
                var numR = stack.get(stack.size() - 1).num;
                int idx = stack.size() - 2;
                while (idx >= 1) {
                    var op = stack.get(idx).ctr;
                    idx--;
                    var numL = stack.get(idx).num;
                    idx--;

                    switch (op) {
                        case '*' -> numR *= numL;
                        case '+' -> numR += numL;
                        case '-' -> numR = numL - numR;
                        default -> throw new RuntimeException(String.valueOf(op));
                    }
                }
                return new ArrayList<>(List.of(new StackData(numR, 'd')));
            }

            List<StackData> mergeMulDiv(List<StackData> stack) {
                if (stack.size() < 3 || stack.get(stack.size() - 2).ctr != '*') {
                    return stack;
                }
                var numR = stack.get(stack.size() - 1).num;
                int idx = stack.size() - 2;
                while (idx >= 1 && stack.get(idx).ctr == '*') {
                    idx--;
                    var numL = stack.get(idx).num;
                    idx--;
                    numR *= numL;
                }
                List<StackData> ans = new ArrayList<>();
                for (int i = 0; i <= idx; i++) {
                    ans.add(stack.get(i));
                }
                ans.add(new StackData(numR, 'd'));
                return ans;
            }
        };
        recurFunc.apply(0, stack);
        return ans;
    }


    /**
     * #284
     */
    static class PeekingIterator implements Iterator<Integer> {
        final Iterator<Integer> iterator;
        Integer peek;

        public PeekingIterator(Iterator<Integer> iterator) {
            // initialize any member here.
            this.iterator = iterator;
            if (iterator.hasNext()) {
                peek = iterator.next();
            }
        }

        // Returns the next element in the iteration without advancing the iterator.
        public Integer peek() {
            return peek;
        }

        // hasNext() and next() should behave the same as in the Iterator interface.
        // Override them if needed.
        @Override
        public Integer next() {
            var t = peek;
            if (iterator.hasNext()) {
                peek = iterator.next();
            }
            else {
                peek = null;
            }
            return t;
        }

        @Override
        public boolean hasNext() {
            return peek != null;
        }
    }

    /**
     * #289
     *
     * @param board
     */
    public static void gameOfLife(int[][] board) {
        var neighborsFunc = new Object() {
            int apply(int i, int j) {
                int count = 0;
                for (int r = i - 1; r <= i + 1; r++) {
                    if (r >= 0 && r < board.length) {
                        for (int c = j - 1; c <= j + 1; c++) {
                            if (c >= 0 && c < board[0].length) {
                                if (r != i || c != j) {
                                    if (board[r][c] >= 1) {
                                        count++;
                                    }
                                }
                            }
                        }
                    }
                }
                return count;
            }
        };
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                var n = neighborsFunc.apply(i, j);
                if (board[i][j] == 1) {
                    if (n < 2 || n > 3) {
                        board[i][j] = 2;
                    }
                }
                else if (board[i][j] == 0) {
                    if (n == 3) {
                        board[i][j] = -1;
                    }
                }
                else throw new RuntimeException();
            }
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == -1) {
                    board[i][j] = 1;
                }
                else if (board[i][j] == 2) {
                    board[i][j] = 0;
                }
            }
        }
    }

    /**
     * #295
     */
    static class MedianFinder {
        PriorityQueue<Integer> lessMaxQueue = new PriorityQueue<>((a, b) -> b - a);
        PriorityQueue<Integer> gEqMinQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a));

        public void addNum(int num) {
            if (lessMaxQueue.size() == 0 && gEqMinQueue.size() == 0) {
                lessMaxQueue.add(num);
                return;
            }

            var median = findMedian();
            if (num < median) lessMaxQueue.add(num);
            else gEqMinQueue.add(num);
            while (lessMaxQueue.size() > gEqMinQueue.size() + 1) {
                gEqMinQueue.add(lessMaxQueue.poll());
            }
            while (gEqMinQueue.size() > lessMaxQueue.size()) {
                lessMaxQueue.add(gEqMinQueue.poll());
            }
        }

        public double findMedian() {
            if (lessMaxQueue.size() == gEqMinQueue.size()) {
                return (lessMaxQueue.peek() + gEqMinQueue.peek()) / 2.;
            }
            else if (lessMaxQueue.size() == gEqMinQueue.size() + 1) {
                return lessMaxQueue.peek();
            }
            else throw new RuntimeException();
        }
    }

    /**
     * #297
     */
    public static class Codec {

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            Map<TreeNode, Short> idOfNode = new HashMap<>(10);
            Queue<TreeNode> deque = new ArrayDeque<>();
            var id = new Object() {
                short data = 0;
            };
            if (root == null) {
                return "";
            }
            StringBuilder strB = new StringBuilder();
            int count = 0;
            deque.add(root);
            while (deque.size() > 0) {
                int s = deque.size();
                while (s-- > 0) {
                    count++;
                    var n = deque.poll();
                    var id_n = idOfNode.computeIfAbsent(n, a -> ++id.data);
                    var id_l = n.left != null ? idOfNode.computeIfAbsent(n.left, a -> ++id.data) : 0;
                    var id_r = n.right != null ? idOfNode.computeIfAbsent(n.right, a -> ++id.data) : 0;
                    strB.append(";").append(id_n).append(",").append(n.val).append(",").append(id_l).append(",").append(id_r);
                    if (id_l != 0) deque.add(n.left);
                    if (id_r != 0) deque.add(n.right);
                }
            }
            strB.insert(0, count);
            return strB.toString();
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            if (data.equals("")) {
                return null;
            }
            var d = data.split(";");
            Map<Short, TreeNode> memory = new HashMap<>(Integer.parseInt(d[0]));
            memory.put((short) 0, null);
            for (int i = 1; i < d.length; i++) {
                var node_info = d[i];
                var infos = node_info.split(",");
                var id_n = Short.parseShort(infos[0]);
                var n_val = Integer.parseInt(infos[1]);
                memory.put(id_n, new TreeNode(n_val));
            }
            for (int i = 1; i < d.length; i++) {
                var node_info = d[i];
                var infos = node_info.split(",");
                var id_n = Short.parseShort(infos[0]);
                var id_l = Short.parseShort(infos[2]);
                var id_r = Short.parseShort(infos[3]);

                var n = memory.get(id_n);
                n.left = memory.get(id_l);
                n.right = memory.get(id_r);
            }
            return memory.get((short) 1);
        }
    }
}
