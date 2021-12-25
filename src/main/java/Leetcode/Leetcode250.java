package Leetcode;

import java.util.*;

@SuppressWarnings("JavaDoc")
public class Leetcode250 {

  /**
   * #208
   */
  public static class Trie {
    static class Node {
      char ctr;
      Node left;
      Node right;
      Node mid;
      boolean contain;

      Node(char c) {
        ctr = c;
      }
    }

    Node head = null;

    public Trie() {

    }

    public void insert(String word) {
      head = put(head, word, 0);
    }

    private Node put(Node ptr, String s, int idx) {
      var current_ctr = s.charAt(idx);
      if (ptr == null) {
        ptr = new Node(current_ctr);
      }

      if (current_ctr < ptr.ctr) {
        ptr.left = put(ptr.left, s, idx);
      }
      else if (current_ctr > ptr.ctr) {
        ptr.right = put(ptr.right, s, idx);
      }
      else if (idx < s.length() - 1) {
        ptr.mid = put(ptr.mid, s, idx + 1);
      }
      else {
        ptr.contain = true;
      }
      return ptr;
    }

    public boolean search(String word) {
      return search(head, word, 0);
    }

    private boolean search(Node ptr, String s, int idx) {
      if (ptr == null) return false;
      var current_ctr = s.charAt(idx);
      if (ptr.ctr == current_ctr) {
        if (idx == s.length() - 1) return ptr.contain;
        else return search(ptr.mid, s, idx + 1);
      }
      else if (current_ctr < ptr.ctr) return search(ptr.left, s, idx);
      else return search(ptr.right, s, idx);
    }

    public boolean startsWith(String prefix) {
      return startsWith(head, prefix, 0);
    }

    private boolean startsWith(Node ptr, String prefix, int idx) {
      if (ptr == null) return false;
      var current_ctr = prefix.charAt(idx);
      if (ptr.ctr == current_ctr) {
        if (idx == prefix.length() - 1) return ptr.mid != null || ptr.contain;
        else return startsWith(ptr.mid, prefix, idx + 1);
      }
      else if (current_ctr < ptr.ctr) return startsWith(ptr.left, prefix, idx);
      else return startsWith(ptr.right, prefix, idx);
    }
  }

  /**
   * #212
   *
   * @param board
   * @param words
   * @return
   */
  @SuppressWarnings("UseBulkOperation")
  public static List<String> findWords(char[][] board, String[] words) {
    if (words.length == 0) return new ArrayList<>();
    Trie trie = new Trie();
    int max_len = 0;
    for (var w : words) {
      max_len = Math.max(max_len, w.length());
      trie.insert(w);
    }
    StringBuilder sb = new StringBuilder();
    int finalMax_len = max_len;
    Set<String> ans = new HashSet<>();
    var findWordsDFS_Func = new Object() {
      public void apply(int r, int c, Trie.Node ptr, int depth) {
        if (depth > finalMax_len || ptr == null) {
          return;
        }
        var ctr = board[r][c];
        if (ctr == '#') {
          return;
        }
        while (ptr != null) {
          if (ptr.ctr == ctr) {
            sb.append(ctr);
            board[r][c] = '#';
            if (ptr.contain) {
              ans.add(sb.toString());
            }
            ptr = ptr.mid;

            if (r - 1 >= 0) {
              apply(r - 1, c, ptr, depth + 1);
            }
            if (r + 1 < board.length) {
              apply(r + 1, c, ptr, depth + 1);
            }
            if (c - 1 >= 0) {
              apply(r, c - 1, ptr, depth + 1);
            }
            if (c + 1 < board[0].length) {
              apply(r, c + 1, ptr, depth + 1);
            }

            board[r][c] = ctr;
            sb.deleteCharAt(sb.length() - 1);

            break;
          }
          else if (ctr < ptr.ctr) {
            ptr = ptr.left;
          }
          else ptr = ptr.right;
        }
      }
    };
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        findWordsDFS_Func.apply(i, j, trie.head, 1);
      }
    }
    List<String> ans_list = new ArrayList<>(ans.size());
    for (var s : ans) {
      ans_list.add(s);
    }

    return ans_list;
  }


  /**
   * #214
   *
   * @param s
   * @return
   */
  public static String shortestPalindrome(String s) {
    char[] inserted = new char[2 * s.length() + 1];
    int idx_inserted = 0, idx_str = 0;
    char placeHolder = '$';
    while (idx_str < s.length()) {
      inserted[idx_inserted++] = placeHolder;
      inserted[idx_inserted++] = s.charAt(idx_str++);
    }
    inserted[idx_inserted] = placeHolder;

    int[] ArmLen = new int[inserted.length];
    int pos = 0, arm_len = 1;
    int target_pos = 0, target_len = 1;
    while (pos < inserted.length) {
      while ((pos + arm_len) < inserted.length &&
              (pos - arm_len) >= 0 &&
              inserted[pos + arm_len] == inserted[pos - arm_len]) {
        arm_len++;
      }
      ArmLen[pos] = arm_len;
      if (arm_len > target_len && (pos - (arm_len - 1) == 0)) {
        target_len = arm_len;
        target_pos = pos;
      }

      int mid_pos = pos, mid_arm_len = arm_len;
      pos++;
      arm_len = 1;
      while (pos <= mid_pos + mid_arm_len - 1) {
        int sym_pos = mid_pos - (pos - mid_pos);
        int sym_pos_arm_len = ArmLen[sym_pos];

        int mid_left_bound = mid_pos - (mid_arm_len - 1);
        int sym_left_bound = sym_pos - (sym_pos_arm_len - 1);

        if (sym_left_bound > mid_left_bound) {
          ArmLen[pos] = sym_pos_arm_len;
          pos++;
        }
        else if (sym_left_bound < mid_left_bound) {
          ArmLen[pos] = sym_pos - mid_left_bound + 1;
          pos++;
        }
        else {
          arm_len = sym_pos_arm_len;
          break;
        }
      }
    }

    int right = (target_pos + target_len - 1);
    int palindrome_len = right / 2;
    StringBuilder ans = new StringBuilder(s.substring(palindrome_len));
    ans = ans.reverse();
    ans.append(s);
    return ans.toString();
  }

  /**
   * #218
   *
   * @param buildings
   * @return
   */
  public static List<List<Integer>> getSkyline(int[][] buildings) {
    class RightHeight {
      final int right;
      final int height;

      RightHeight(int r, int h) {
        right = r;
        height = h;
      }
    }

    class XHeight {
      final int x;
      final int height;
      final int right;

      XHeight(int x, int h, int i) {
        this.x = x;
        this.height = h;
        right = i;
      }
    }

    List<XHeight> scan = new ArrayList<>(buildings.length * 2);
    for (var b : buildings) {
      int x = b[0];
      int x1 = b[1];
      int h = b[2];
      scan.add(new XHeight(x, h, x1));
      scan.add(new XHeight(x1, h, x1));
    }
    scan.sort(Comparator.comparing(t -> t.x));

    PriorityQueue<RightHeight> queue = new PriorityQueue<>((a, b) -> b.height - a.height);
    List<List<Integer>> ans = new ArrayList<>();
    for (var s : scan) {
      int x = s.x;
      int height = s.height;
      int right = s.right;
      if (x != right) {
        queue.add(new RightHeight(right, height));
      }
      while (queue.size() > 0 && x >= queue.peek().right) {
        queue.poll();
      }
      int max_height = queue.peek() != null ? queue.peek().height : 0;
      ans.add(List.of(x, max_height));

      if (ans.size() >= 2 && ans.get(ans.size() - 1).get(0).equals(ans.get(ans.size() - 2).get(0))) {
        var last = ans.remove(ans.size() - 1);
        var t = ans.remove(ans.size() - 1);
        ans.add(List.of(x, Math.max(t.get(1), last.get(1))));
      }
      if (ans.size() >= 2 && ans.get(ans.size() - 1).get(1).equals(ans.get(ans.size() - 2).get(1))) {
        ans.remove(ans.size() - 1);
      }

    }
    return ans;
  }


  /**
   * #224
   *
   * @param s
   * @return
   */
  public static int calculate(String s) {
    var calculator = new StackCalculator();
    int num = 0;
    for (int i = 0; i < s.length(); i++) {
      char ctr = s.charAt(i);
      calculator.acceptChar(ctr);
    }
    return calculator.getResult();
  }

  private static class StackCalculator {
    static class Data {
      int num;
      char str;

      Data(int n, char ctr) {
        this.num = n;
        this.str = ctr;
      }
    }

    Deque<Data> stack = new ArrayDeque<>();
    int num;
    boolean parsingDigit = false;

    public void acceptChar(char ctr) {
      switch (ctr) {
        case ' ' -> tryStopParseDigitAndCompute();
        case '(' -> {
          tryStopParseDigitAndCompute();
          stack.addLast(new Data(0, '('));
        }
        case ')' -> {
          tryStopParseDigitAndCompute();
          var num_node = stack.pollLast();
          stack.pollLast();
          computeAlongStack(num_node.num);
        }
        case '+' -> {
          tryStopParseDigitAndCompute();
          stack.addLast(new Data(0, '+'));
        }
        case '-' -> {
          tryStopParseDigitAndCompute();
          stack.addLast(new Data(0, '-'));
        }
        default -> {
          parsingDigit = true;
          var digit = Integer.parseInt(String.valueOf(ctr));
          num = num * 10 + digit;
        }
      }
    }

    public int getResult() {
      tryStopParseDigitAndCompute();
      return stack.pollLast().num;
    }

    private void computeAlongStack(int digits) {
      while (stack.peekLast() != null && stack.peekLast().str != '(') {
        switch (stack.peekLast().str) {
          case '+' -> {
            stack.pollLast();
            var numData = stack.pollLast();
            digits += numData.num;
          }
          case '-' -> {
            stack.pollLast();
            if (stack.peekLast() != null && stack.peekLast().str == 'd') {
              var numData = stack.pollLast().num;
              digits = numData - digits;
            }
            else {
              stack.pollLast();
              digits = -digits;
            }
          }
          case 'd' -> throw new RuntimeException();
        }
      }
      stack.addLast(new Data(digits, 'd'));
    }

    private void tryStopParseDigitAndCompute() {
      if (parsingDigit) {
        parsingDigit = false;
        int digits = num;
        num = 0;
        computeAlongStack(digits);
      }
    }
  }

  /**
   * #227
   *
   * @param s
   * @return
   */
  public static int calculate2(String s) {
    var cal = new StackCalculator2();
    for(int i = 0; i < s.length(); i++){
      var c = s.charAt(i);
      cal.acceptChar(c);
    }
    return cal.getResult();
  }

  private static class StackCalculator2 {
    static class Data {
      int num;
      char str;

      Data(int n, char ctr) {
        this.num = n;
        this.str = ctr;
      }
    }

    Deque<Data> stack = new ArrayDeque<>();
    int num = 0;
    boolean parsingNum = false;
    boolean hasMulOrDiv = false;

    public void acceptChar(char ctr) {
      switch (ctr) {
        case ' ' -> {
          tryStopParsingInt();
        }
        case '*' -> {
          tryStopParsingInt();
          tryEvalMulDiv();
          hasMulOrDiv = true;
          stack.addLast(new Data(0,'*'));
        }
        case '/'-> {
          tryStopParsingInt();
          tryEvalMulDiv();
          hasMulOrDiv = true;
          stack.addLast(new Data(0,'/'));
        }
        case '+'-> {
          tryStopParsingInt();
          tryEvalMulDiv();
          evalStack();
          stack.addLast(new Data(0,'+'));
        }
        case '-'-> {
          tryStopParsingInt();
          tryEvalMulDiv();
          evalStack();
          stack.addLast(new Data(0,'-'));
        }
        default -> {
          parsingNum = true;
          num = num * 10 + Integer.parseInt(String.valueOf(ctr));
        }
      }
    }

    public int getResult(){
      tryStopParsingInt();
      tryEvalMulDiv();
      evalStack();
      return stack.pollLast().num;
    }

    private void tryEvalMulDiv(){
      if(hasMulOrDiv){
        hasMulOrDiv = false;
        var num1 = stack.pollLast();
        var op = stack.pollLast();
        var num2 = stack.pollLast();
        switch (op.str) {
          case '*' -> stack.addLast(new Data(num2.num * num1.num, 'd'));
          case '/' -> stack.addLast(new Data(num2.num / num1.num, 'd'));
          default -> throw new RuntimeException();
        }
      }
    }

    private void tryStopParsingInt(){
      if(parsingNum){
        parsingNum = false;
        stack.addLast(new Data(num,'d'));
        num = 0;
      }
    }

    private void evalStack() {
      while (stack.size() >= 3) {
        var num1 = stack.pollLast();
        var op = stack.pollLast();
        var num2 = stack.pollLast();
        switch (op.str) {
          case '-' -> stack.addLast(new Data(num2.num - num1.num, 'd'));
          case '+' -> stack.addLast(new Data(num2.num + num1.num, 'd'));
          default -> throw new RuntimeException();
        }
      }
    }
  }

  /**
   * #228
   *
   * @param nums
   * @return
   */
  public static List<String> summaryRanges(int[] nums) {
    if (nums.length == 0) return new ArrayList<>();
    int left = nums[0];
    List<String> ans = new ArrayList<>();
    for (int i = 1; i < nums.length; i++) {
      if (nums[i] - 1 != nums[i - 1]) {
        int right = nums[i - 1];
        if (right == left) {
          ans.add(String.valueOf(left));
        }
        else {
          ans.add(left + "->" + right);
        }
        left = nums[i];
      }
    }
    {
      int right = nums[nums.length - 1];
      if (right == left) {
        ans.add(String.valueOf(left));
      }
      else {
        ans.add(left + "->" + right);
      }
    }
    return ans;
  }

  /**
   * #234
   * <br/>回文链表
   *
   * @param head node
   * @return res
   */
  public static boolean isPalindrome(ListNode head) {
    List<Integer> vals = new ArrayList<>();

    // 将链表的值复制到数组中
    ListNode currentNode = head;
    while (currentNode != null) {
      vals.add(currentNode.val);
      currentNode = currentNode.next;
    }

    int front = 0;
    int back = vals.size() - 1;
    while (front < back) {
      if (!vals.get(front).equals(vals.get(back))) {
        return false;
      }
      front++;
      back--;
    }
    return true;
  }

  /**
   * #235
   * <br/>二叉搜索树的最近公共祖先
   *
   * @param root tree
   * @param p    node 1
   * @param q    node 2
   * @return closest ancestor
   */
  @SuppressWarnings("unused")
  public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
    if (root.val > p.val && root.val > q.val) {
      return lowestCommonAncestor(root.left, p, q);
    }
    else if (root.val < p.val && root.val < q.val) {
      return lowestCommonAncestor(root.right, p, q);
    }
    else {
      return root;
    }
  }
}
