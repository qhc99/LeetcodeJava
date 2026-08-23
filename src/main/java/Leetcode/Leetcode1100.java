package Leetcode;

import java.util.*;

@SuppressWarnings({ "JavaDoc" })
public class Leetcode1100 {

    /**
     * #1004
     * 
     * @param nums
     * @param k
     * @return
     */
    public int longestOnes(int[] nums, int k) {
        for (int i = 1; i < nums.length; i++) {
            nums[i] += nums[i - 1];
        }
        int l = 0;
        int res = 0;
        for (int r = 0; r < nums.length; r++) {
            // var zeros = (r + 1 - l) - (nums[r] - (l-1>=0 ? nums[l-1] : 0));
            while (l <= r && (r + 1 - l)
                    - (nums[r] - (l - 1 >= 0 ? nums[l - 1] : 0)) > k) {
                l++;
            }
            res = Math.max(res, r + 1 - l);

        }
        return res;
        // int res = 0;
        // int n = nums.length;
        // int[] left2right = new int[n];
        // for (int i = 0; i < n; i++) {
        // left2right[i] = nums[i];
        // if (i - 1 >= 0 && nums[i - 1] > 0 && nums[i] == 1) {
        // left2right[i] += left2right[i - 1];
        // }
        // res = Math.max(res, left2right[i]);
        // }
        // int[] right2left = new int[n];
        // for (int i = n - 1; i >= 0; i--) {
        // right2left[i] = nums[i];
        // if (i + 1 < n && nums[i + 1] > 0 && nums[i] == 1) {
        // right2left[i] += right2left[i + 1];
        // }
        // }
        // Deque<Integer> deque = new ArrayDeque<>();
        // for (int i = 0; i < n; i++) {
        // if (nums[i] == 0) {
        // deque.addLast(i);
        // while (deque.size() > k) {
        // deque.pollFirst();
        // }
        // if (!deque.isEmpty()) {
        // var head = deque.peekFirst();
        // var tail = deque.peekLast();
        // res = Math.max(res, (tail + 1 - head)
        // + (head - 1 >= 0 ? left2right[head - 1] : 0)
        // + (tail + 1 < n ? right2left[tail + 1] : 0));
        // }
        // }
        // }
        // return res;
    }

    /**
     * #1006 <br/>
     * 笨阶乘
     * 
     * <pre>
     * clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1
     * </pre>
     *
     * @param N number
     * @return result
     */
    public static int clumsy(int N) {
        Deque<Integer> stack = new LinkedList<>();
        stack.addLast(N);
        N--;
        int flag = 0;
        while (N >= 1) {
            int reminder = flag % 4;
            switch (reminder) {
            case 0 -> {
                stack.addLast(stack.removeLast() * N);
                flag++;
            }
            case 1 -> {
                stack.addLast(stack.removeLast() / N);
                flag++;
            }
            case 2 -> {
                stack.addLast(N);
                flag++;
            }
            case 3 -> {
                stack.addLast(-N);
                flag = 0;
            }
            }
            N--;
        }
        return stack.parallelStream().mapToInt(Integer::intValue).sum();
    }

    /**
     * #1010
     *
     * @param time
     * @return
     */
    public static int numPairsDivisibleBy60(int[] time) {
        Map<Integer, Integer> remCount = new HashMap<>();
        for (int t : time) {
            int r = t % 60;
            remCount.put(r, remCount.getOrDefault(r, 0) + 1);
        }

        int ans = 0;
        for (int i = 1; i < 30; i++) {
            if (remCount.containsKey(i) && remCount.containsKey(60 - i)) {
                ans += remCount.get(i) * remCount.get(60 - i);
            }
        }
        if (remCount.containsKey(30)) {
            int c = remCount.get(30);
            ans += c * (c - 1) / 2;
        }

        if (remCount.containsKey(0)) {
            int c = remCount.get(0);
            ans += c * (c - 1) / 2;
        }

        return ans;
    }

    /**
     * #1011
     * 
     * @param weights
     * @param days
     * @return
     */
    public int shipWithinDays(int[] weights, int days) {
        var r = Arrays.stream(weights).sum();
        var l = Arrays.stream(weights).max().getAsInt();
        while (l < r) {
            int mid = l + (r - l) / 2;
            int c = 0;
            int d = 1;
            for (var w : weights) {
                if (c + w > mid) {
                    d++;
                    c = w;
                } else
                    c += w;
            }
            if (d <= days)
                r = mid;
            else
                l = mid;
        }

        return l;
    }

    /**
     * #1015
     *
     * @param k
     * @return
     */
    public static int smallestRepunitDivByK(int k) {
        if (k % 2 == 0 || k % 5 == 0) {
            return -1;
        }
        int temp = 1;
        int len = 1;
        while (temp % k != 0) {
            temp = temp % k;
            temp = temp * 10 + 1;
            len += 1;
        }
        return len;
    }

    /**
     * #1022
     *
     * @param root
     * @return
     */
    public static int sumRootToLeaf(TreeNode root) {
        int[] sum = new int[1];
        StringBuilder str = new StringBuilder();
        sumRootToLeafRecursiveSolve(root, str, sum);
        return sum[0];
    }

    private static void sumRootToLeafRecursiveSolve(TreeNode n,
            StringBuilder str, int[] sum) {
        int len = str.length();
        str.append(n.val);
        if (n.left == null && n.right == null) {
            sum[0] += Integer.parseInt(str.toString(), 2);
        } else if (n.left == null) {
            sumRootToLeafRecursiveSolve(n.right, str, sum);
        } else if (n.right == null) {
            sumRootToLeafRecursiveSolve(n.left, str, sum);
        } else {
            sumRootToLeafRecursiveSolve(n.left, str, sum);
            sumRootToLeafRecursiveSolve(n.right, str, sum);
        }
        str.delete(len, str.length());
    }

    /**
     * #1026
     *
     * @param root
     * @return
     */
    public static int maxAncestorDiff(TreeNode root) {
        int[] ans = new int[1];
        ans[0] = Integer.MIN_VALUE;

        maxAncestorDiffApply(root, Integer.MIN_VALUE, Integer.MAX_VALUE, ans);
        return ans[0];
    }

    private static void maxAncestorDiffApply(TreeNode n, int max_ancestor,
            int min_ancestor, int[] ans) {
        max_ancestor = Math.max(n.val, max_ancestor);
        min_ancestor = Math.min(n.val, min_ancestor);
        if (n.left != null) {
            ans[0] = Math.max(ans[0], Math.abs(max_ancestor - n.left.val));
            ans[0] = Math.max(ans[0], Math.abs(min_ancestor - n.left.val));
            maxAncestorDiffApply(n.left, max_ancestor, min_ancestor, ans);
        }
        if (n.right != null) {
            ans[0] = Math.max(ans[0], Math.abs(max_ancestor - n.right.val));
            ans[0] = Math.max(ans[0], Math.abs(min_ancestor - n.right.val));
            maxAncestorDiffApply(n.right, max_ancestor, min_ancestor, ans);
        }
    }

    /**
     * #1024 <br/>
     * 视频剪辑
     * 
     * <pre>
     * 输入：clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], T = 10
     * 输出：3
     * 解释：
     * 我们选中 [0,2], [8,10], [1,9] 这三个片段。
     * 然后，按下面的方案重制比赛片段：
     * 将 [1,9] 再剪辑为 [1,2] + [2,8] + [8,9] 。
     * 现在我们手上有 [0,2] + [2,8] + [8,10]，而这些涵盖了整场比赛 [0, 10]。
     * </pre>
     *
     * @param clips video clips
     * @param T     video length
     * @return is intact
     */
    public static int videoStitching(int[][] clips, int T) {
        int[] startAndEnd = new int[T];
        for (int[] clip : clips) {
            if (clip[0] < T) {
                startAndEnd[clip[0]] = Math.max(startAndEnd[clip[0]], clip[1]);
            }
        }
        int remotest = 0, preRemotest = 0, count = 0;
        for (int i = 0; i < startAndEnd.length; i++) {
            remotest = Math.max(remotest, startAndEnd[i]);
            if (i == remotest) {
                return -1;
            }
            if (i == preRemotest) {
                count++;
                preRemotest = remotest;
            }
        }
        return count;
    }

    /**
     * #1032
     */
    public static class StreamChecker {

        public static class ModifiedTernaryTries {
            Node root;

            public static class Node {
                private char ctr;
                private boolean contain;
                private Node left, mid, right;

                public char getChar() {
                    return ctr;
                }

                public boolean isContain() {
                    return contain;
                }

                public Node getLeft() {
                    return left;
                }

                public Node getMid() {
                    return mid;
                }

                public Node getRight() {
                    return right;
                }
            }

            public void put(String key) {
                root = put(root, key, 0);
            }

            private Node put(Node x, String key, int d) {
                char c = key.charAt(d);
                if (x == null) {
                    x = new Node();
                    x.ctr = c;
                }
                if (c < x.ctr) {
                    x.left = put(x.left, key, d);
                } else if (c > x.ctr) {
                    x.right = put(x.right, key, d);
                } else if (d < key.length() - 1) {
                    x.mid = put(x.mid, key, d + 1);
                } else {
                    x.contain = true;
                }
                return x;
            }

            Queue<Node> nodeQueue = new LinkedList<>();

            public boolean query(char letter) {
                boolean ans = false;
                Node s = search(root, letter);
                int count = nodeQueue.size();
                if (s != null) {
                    nodeQueue.add(s.mid);
                    ans = ans || s.contain;
                }

                while (count > 0) {
                    Node ptr = nodeQueue.poll();
                    if (ptr != null) {
                        Node n = search(ptr, letter);
                        if (n != null) {
                            ans = ans || n.contain;
                            nodeQueue.add(n.mid);
                        }
                    }
                    count--;
                }

                return ans;
            }

            Node search(Node current, char letter) {
                if (current == null) {
                    return null;
                }
                if (letter == current.ctr) {
                    return current;
                } else if (letter < current.ctr) {
                    return search(current.left, letter);
                } else
                    return search(current.right, letter);
            }
        }

        ModifiedTernaryTries tries = new ModifiedTernaryTries();

        public StreamChecker(String[] words) {
            for (String w : words) {
                tries.put(w);
            }
        }

        public boolean query(char letter) {
            return tries.query(letter);
        }
    }

    /**
     * #1041
     * 
     * @param instructions
     * @return
     */
    public boolean isRobotBounded(String instructions) {
        int state = 0;
        int i = 0, j = 0;
        for (var s : instructions.toCharArray()) {
            if (s == 'G') {
                switch (state) {
                case 0:
                    j++;
                    break;
                case 1:
                    i++;
                    break;
                case 2:
                    j--;
                    break;
                case 3:
                    i--;
                    break;
                default:
                    break;
                }
            } else if (s == 'L') {
                state--;
                if (state < 0)
                    state += 4;
            } else {
                state = (state + 1) % 4;
            }
        }
        return state != 0 || (i == 0 && j == 0);
    }

    /**
     * #1043
     * 
     * @param arr
     * @param k
     * @return
     */
    public int maxSumAfterPartitioning(int[] arr, int k) {
        int[] dp = new int[arr.length + 1];
        for (int i = 1; i <= arr.length; i++) {
            int max = arr[i - 1];
            for (int j = i - 1; j >= 0 && i - j <= k; j--) {
                dp[i] = Math.max(dp[i], dp[j] + max * (i - j));
                if (j - 1 >= 0)
                    max = Math.max(max, arr[j - 1]);
            }
        }

        return dp[arr.length];
    }

    /**
     * #1044
     * 
     * @param s
     * @return
     */
    public String longestDupSubstring(String s) {
        Random random = new Random();
        // 生成两个进制
        int a1 = random.nextInt(75) + 26;
        int a2 = random.nextInt(75) + 26;
        // 生成两个模
        int mod1 = random.nextInt(Integer.MAX_VALUE - 1000000007 + 1)
                + 1000000007;
        int mod2 = random.nextInt(Integer.MAX_VALUE - 1000000007 + 1)
                + 1000000007;
        int[] arr = new int[s.length()];
        for (int i = 0; i < arr.length; i++)
            arr[i] = s.charAt(i) - 'a';
        int l = 1, r = arr.length - 1, start = -1, len = 0;
        while (r - l >= 0) {
            int mid = l + (r - l + 1) / 2;
            int idx = findDup(arr, mid, a1, a2, mod1, mod2);
            if (idx == -1)
                r = mid - 1;
            else {
                l = mid + 1;
                start = idx;
                len = mid;
            }
        }
        return start != -1 ? s.substring(start, start + len) : "";
    }

    int findDup(int[] arr, int len, int a1, int a2, int mod1, int mod2) {
        Set<Long> seen = new HashSet<>();
        long h1 = 0, h2 = 0, a1_len = qpow(a1, len, mod1),
                a2_len = qpow(a2, len, mod2);
        for (int i = 0; i < len; i++) {
            h1 = (h1 * a1 % mod1 + arr[i]) % mod1;
            h2 = (h2 * a2 % mod2 + arr[i]) % mod2;
            if (h1 < 0)
                h1 += mod1;
            if (h2 < 0)
                h2 += mod2;
        }
        seen.add(h1 * mod2 + h2);
        for (int i = 1; i < arr.length - len + 1; i++) {
            h1 = (h1 * a1 % mod1 - a1_len * arr[i - 1] % mod1
                    + arr[i + len - 1]) % mod1;
            h2 = (h2 * a2 % mod2 - a2_len * arr[i - 1] % mod2
                    + arr[i + len - 1]) % mod2;
            if (h1 < 0)
                h1 += mod1;
            if (h2 < 0)
                h2 += mod2;
            long num = h1 * mod2 + h2;
            if (!seen.add(num)) {
                return i;
            }
        }
        return -1;
    }

    long qpow(long a, long b, int mod) {
        long res = 1;
        a %= mod;
        while (b > 0) {
            if ((b & 1) == 1) {
                res = res * a % mod;
                if (res < 0)
                    res += mod;
            }
            a = a * a % mod;
            if (a < 0)
                a += mod;
            b >>= 1;
        }
        return res;
    }

    /**
     * #1047
     * 
     * @param s
     * @return
     */
    public String removeDuplicates(String s) {
        StringBuilder sb = new StringBuilder();
        for (var c : s.toCharArray()) {
            if (sb.isEmpty() || sb.charAt(sb.length() - 1) != c)
                sb.append(c);
            else if (!sb.isEmpty())
                sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * #1151
     * 
     * @param data
     * @return
     */
    public int minSwaps(int[] data) {
        int numOf1 = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 1)
                numOf1++;
            if (i - 1 >= 0)
                data[i] += data[i - 1];
        }
        if (numOf1 == 0)
            return 0;
        int res = Integer.MAX_VALUE;
        for (int r = numOf1 - 1; r < data.length; r++) {
            int l = r - numOf1;
            res = Math.min(res, numOf1 - (data[r] - (l >= 0 ? data[l] : 0)));
        }
        return res;
    }

    /**
     * #1053
     * 
     * @param arr
     * @return
     */
    public int[] prevPermOpt1(int[] arr) {
        if (arr.length > 1) {
            int i = arr.length - 1;
            for (; i >= 1; i--) {
                if (arr[i - 1] > arr[i])
                    break;
            }
            if (i >= 1 && arr[i - 1] > arr[i]) {
                i = i - 1;
                int j = arr.length - 1;
                for (; j > i; j--) {
                    if (arr[i] > arr[j])
                        break;
                }
                if (j > i && arr[i] > arr[j]) {
                    while (arr[j - 1] == arr[j]) {
                        j--;
                    }
                    var t = arr[i];
                    arr[i] = arr[j];
                    arr[j] = t;
                }
            }
        }

        return arr;
    }

    public String gcdOfStrings(String str1, String str2) {
        if (str1.length() == str2.length()) {
            if (str1.equals(str2))
                return str1;
            else
                return "";
        }
        if (str2.length() > str1.length()) {
            var t = str1;
            str1 = str2;
            str2 = t;
        }
        if (!str1.startsWith(str2))
            return "";
        return gcdOfStrings(str1.substring(str2.length(), str1.length()), str2);
    }

    /**
     * #1064
     * 
     * @param arr
     * @return
     */
    public int fixedPoint(int[] arr) {
        int l = 0, r = arr.length - 1;
        while (r - l > 0) {
            int mid = l + (r - l) / 2;
            if (arr[mid] - mid >= 0) {
                r = mid;
            } else {
                l = mid + 1;
            }
        }
        if (arr[l] - l == 0)
            return l;
        return -1;
    }

    /**
     * #1087
     * 
     * @param s
     * @return
     */
    public String[] expand(String s) {
        List<StringBuilder> res = new ArrayList<>();
        res.add(new StringBuilder());
        Iter iter = new Iter(s);
        while (!iter.end()) {
            var chars = iter.next();
            int size = res.size() * chars.size();
            int initLength = res.size();
            for (int i = res.size(); i < size; i++) {
                res.add(new StringBuilder(res.get(i % initLength)));
            }
            for (int i = 0; i < res.size(); i++) {
                res.get(i).append(chars.get(i / initLength));
            }
        }

        return res.stream().map(sb -> sb.toString()).sorted().toList()
                .toArray(new String[0]);
    }

    static class Iter {
        String s;
        int i = 0;

        Iter(String s) {
            this.s = s;
        }

        boolean end() {
            return i >= s.length();
        }

        List<Character> next() {
            if (s.charAt(i) != '{') {
                return List.of(s.charAt(i++));
            } else {
                List<Character> res = new ArrayList<>();
                i++;
                do {
                    if (s.charAt(i) != ',')
                        res.add(s.charAt(i));
                } while (s.charAt(++i) != '}');
                i++;
                return res;
            }
        }

    }

    /**
     * #1091
     * 
     * @param grid
     * @return
     */
    public int shortestPathBinaryMatrix(int[][] grid) {
        int n = grid.length;
        if (grid[0][0] == 1 || grid[n - 1][n - 1] == 1)
            return -1;
        if (n == 1)
            return 1;
        Queue<Pos> queue = new ArrayDeque<>();
        boolean[][] inQueue = new boolean[n][n];
        queue.add(new Pos(0, 0, 1));
        while (!queue.isEmpty()) {
            var p = queue.poll();
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (!(dy == 0 && dx == 0)) {
                        var next = new Pos(p.i + dx, p.j + dy, p.dist + 1);
                        if (next.i >= 0 && next.i < n && next.j >= 0
                                && next.j < n && !inQueue[next.i][next.j]) {
                            if (next.i == n - 1 && next.j == n - 1) {
                                return next.dist;
                            }
                            if (grid[next.i][next.j] == 0) {
                                inQueue[next.i][next.j] = true;
                                queue.add(next);
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    static record Pos(int i, int j, int dist) {
    }

    /**
     * #1094
     *
     * @param trips
     * @param capacity
     * @return
     */
    public static boolean carPooling(int[][] trips, int capacity) {
        class DestCap {
            final int dest;
            final int cap;

            DestCap(int d, int c) {
                dest = d;
                cap = c;
            }
        }
        Arrays.sort(trips, Comparator.comparing(i -> i[1]));
        PriorityQueue<DestCap> queue = new PriorityQueue<>(
                Comparator.comparing(d -> d.dest));
        for (var trip : trips) {
            var c = trip[0];
            var start = trip[1];
            var dest = trip[2];
            while (queue.size() > 0 && queue.peek().dest <= start) {
                capacity += queue.poll().cap;
            }
            capacity -= c;
            if (capacity < 0) {
                return false;
            } else {
                queue.add(new DestCap(dest, c));
            }
        }
        return true;
    }
}
