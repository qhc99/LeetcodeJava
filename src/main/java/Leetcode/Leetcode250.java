package Leetcode;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;


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
    for (int i = 0; i < s.length(); i++) {
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
        case ' ' -> tryStopParsingInt();
        case '*' -> {
          tryStopParsingInt();
          tryEvalMulDiv();
          hasMulOrDiv = true;
          stack.addLast(new Data(0, '*'));
        }
        case '/' -> {
          tryStopParsingInt();
          tryEvalMulDiv();
          hasMulOrDiv = true;
          stack.addLast(new Data(0, '/'));
        }
        case '+' -> {
          tryStopParsingInt();
          tryEvalMulDiv();
          evalStack();
          stack.addLast(new Data(0, '+'));
        }
        case '-' -> {
          tryStopParsingInt();
          tryEvalMulDiv();
          evalStack();
          stack.addLast(new Data(0, '-'));
        }
        default -> {
          parsingNum = true;
          num = num * 10 + Integer.parseInt(String.valueOf(ctr));
        }
      }
    }

    public int getResult() {
      tryStopParsingInt();
      tryEvalMulDiv();
      evalStack();
      return stack.pollLast().num;
    }

    private void tryEvalMulDiv() {
      if (hasMulOrDiv) {
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

    private void tryStopParsingInt() {
      if (parsingNum) {
        parsingNum = false;
        stack.addLast(new Data(num, 'd'));
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
   * #230
   *
   * @param root
   * @param k
   * @return
   */
  public static int kthSmallest(TreeNode root, int k) {
    var tree = new OrderStatTree<Integer, Void>(Comparator.comparingInt(Integer::intValue));
    add(root, tree);
    return tree.getKeyOfRank(k);
  }

  private static void add(TreeNode n, OrderStatTree<Integer, Void> tree) {
    if (n == null) {
      return;
    }
    tree.insertKV(n.val, null);
    add(n.left, tree);
    add(n.right, tree);
  }

  private static class Tuple<T1, T2> {
    final T1 first;
    final T2 second;

    public Tuple(T1 f, T2 s) {
      first = f;
      second = s;
    }

    public T1 first() {
      return first;
    }

    public T2 second() {
      return second;
    }


    @Override
    public int hashCode() {
      return Objects.hash(first, second);
    }

    @Override
    public String toString() {
      return "Tuple{" +
              "first=" + first +
              ", second=" + second +
              '}';
    }
  }

  private static class OrderStatTree<K, V> {

    final Comparator<K> comparator;

    final Node<K, V> sentinel = new Node<>(RBTreeTemplate.BLACK);
    Node<K, V> root = sentinel;

    private final RBTreeTemplate<K, Node<K, V>> template;

    public OrderStatTree(Comparator<K> comparator) {
      this.comparator = comparator;
      template = new RBTreeTemplate<>(
              sentinel, comparator,
              n -> n.key,
              () -> this.root,
              r -> this.root = r,
              n -> n.parent,
              (n, p) -> n.parent = p,
              n -> n.left,
              (n, l) -> n.left = l,
              n -> n.right,
              (n, r) -> n.right = r,
              n -> n.color,
              (n, c) -> n.color = c);
    }

    public static <V> OrderStatTree<Integer, V> ofInt() {
      return new OrderStatTree<>(Integer::compareTo);
    }

    public static <V> OrderStatTree<Double, V> ofDouble() {
      return new OrderStatTree<>(Double::compareTo);
    }

    /**
     * 1d range search
     *
     * @param low  low
     * @param high high (inclusive)
     * @return list of key-value in range
     */
    public List<Tuple<K, V>> keyRangeSearch(K low, K high) {
      List<Tuple<K, V>> res = new ArrayList<>();
      if (root == sentinel) {
        return res;
      }
      keyRangeSearch(root, low, high, res);
      return res;
    }

    private void keyRangeSearch(Node<K, V> n, K low, K high, List<Tuple<K, V>> l) {
      if (n == sentinel) {
        return;
      }

      if (comparator.compare(n.key, low) > 0) {
        keyRangeSearch(n.left, low, high, l);
      }

      if (comparator.compare(n.key, low) >= 0 && comparator.compare(n.key, high) <= 0) {
        l.add(new Tuple<>(n.key, n.value));
      }

      if (comparator.compare(n.key, high) < 0) {
        keyRangeSearch(n.right, low, high, l);
      }
    }


    private Node<K, V> ceiling(Node<K, V> x, K key) {
      if (x == sentinel) {
        return sentinel;
      }
      else {
        int cmp = comparator.compare(key, x.key);
        if (cmp == 0) {
          return x;
        }
        else if (cmp > 0) {
          return ceiling(x.right, key);
        }
        else {
          var t = ceiling(x.left, key);
          return t != sentinel ? t : x;
        }
      }
    }

    public K getKeyOfRank(int rank) {
      if (rank <= 0 || rank > size()) {
        throw new IndexOutOfBoundsException();
      }
      Node<K, V> n = getNodeOfRank(rank);
      return n.key;
    }

    Node<K, V> getNodeOfRank(int ith) {
      return getNodeOfRank(root, ith);
    }

    Node<K, V> getNodeOfRank(Node<K, V> current, int ith) {
      int rank = current.left.size + 1;
      if (rank == ith) {
        return current;
      }
      else if (ith < rank) {
        return getNodeOfRank(current.left, ith);
      }
      else {
        return getNodeOfRank(current.right, ith - rank);
      }
    }

    int getRankOfNode(Node<K, V> node) {
      int rank = node.left.size + 1;
      while (node != root) {
        if (node == node.parent.right) {
          rank += node.parent.left.size + 1;
        }
        node = node.parent;
      }
      return rank;
    }

    public int size() {
      return root.size;
    }

    public void insertKV(K key, V val) {
      var n = new Node<>(key, val);
      template.insert(n);
    }


    static final class Node<key, val> {
      key key;
      val value;
      boolean color;
      Node<key, val> parent;
      Node<key, val> left;
      Node<key, val> right;
      int size;

      Node(boolean color) {
        this.color = color;
      }

      Node(key key, val val) {
        color = RBTreeTemplate.RED;
        this.key = key;
        this.value = val;
      }

      @Override
      public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                ", size=" + size +
                '}';
      }
    }
  }

  @FunctionalInterface
  public interface Gettable<Item> {
    Item get();
  }

  /**
   * #231
   *
   * @param n
   * @return
   */
  public static boolean isPowerOfTwo(int n) {
    if (n <= 0) return false;
    while (n != 1) {
      var rem = n % 2;
      if (rem == 1) return false;
      n /= 2;
    }
    return true;
  }

  @SuppressWarnings({"ClassCanBeRecord", "PatternVariableCanBeUsed"})
  static class RBTreeTemplate<Key, Node> {

    final Node sentinel;
    final Comparator<Key> comparator;
    final Gettable<Node> getRoot;
    final Consumer<Node> setRoot;
    static final boolean RED = false;
    static final boolean BLACK = true;
    final Function<Node, Node> getParent;
    final BiConsumer<Node, Node> setParent;
    final Function<Node, Node> getLeft;
    final BiConsumer<Node, Node> setLeft;
    final Function<Node, Node> getRight;
    final BiConsumer<Node, Node> setRight;
    final Function<Node, Boolean> getColor;
    final BiConsumer<Node, Boolean> setColor;
    final Function<Node, Key> getKey;

    RBTreeTemplate(Node sentinel,
                   Comparator<Key> comparator,
                   Function<Node, Key> getKey,
                   Gettable<Node> getRoot,
                   Consumer<Node> setRoot,
                   Function<Node, Node> getParent,
                   BiConsumer<Node, Node> setParent,
                   Function<Node, Node> getLeft,
                   BiConsumer<Node, Node> setLeft,
                   Function<Node, Node> getRight,
                   BiConsumer<Node, Node> setRight,
                   Function<Node, Boolean> getColor,
                   BiConsumer<Node, Boolean> setColor) {
      this.sentinel = sentinel;
      this.comparator = comparator;
      this.getRoot = getRoot;
      this.setRoot = setRoot;
      this.getParent = getParent;
      this.setParent = setParent;
      this.getRight = getRight;
      this.setRight = setRight;
      this.getLeft = getLeft;
      this.setLeft = setLeft;
      this.getColor = getColor;
      this.setColor = setColor;
      this.getKey = getKey;
    }

    /**
     * insert a node <b>WITHOUT</b> duplicate key
     *
     * @param z node or sentinel
     */
    @SuppressWarnings({"SuspiciousNameCombination"})
    void insert(Node z) {
      var y = sentinel;
      var x = getRoot.get();
      while (x != sentinel) {
        y = x;
        {//
          if (x instanceof OrderStatTree.Node<?, ?>) {
            OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
            xo.size++;
          }
        }
        if (comparator.compare(getKey.apply(z), getKey.apply(x)) < 0) {
          x = getLeft.apply(x);
        }
        else if (comparator.compare(getKey.apply(z), getKey.apply(x)) > 0) {
          x = getRight.apply(x);
        }
        else {
          throw new IllegalArgumentException("duplicate key.");
        }
      }
      setParent.accept(z, y);
      if (y == sentinel) {
        setRoot.accept(z);
      }
      else if (comparator.compare(getKey.apply(z), getKey.apply(y)) < 0) {
        setLeft.accept(y, z);
      }
      else if (comparator.compare(getKey.apply(z), getKey.apply(y)) > 0) {
        setRight.accept(y, z);
      }
      else {
        throw new RuntimeException("impossible error.");
      }
      setLeft.accept(z, sentinel);
      setRight.accept(z, sentinel);
      setColor.accept(z, RED);
      {//
        if (z instanceof OrderStatTree.Node<?, ?>) {
          OrderStatTree.Node<?, ?> zo = (OrderStatTree.Node<?, ?>) z;
          zo.size = 1;
        }
      }
      insertFixUp(z);
    }

    private void insertFixUp(Node z) {
      while (getColor.apply(getParent.apply(z)) == RED) {
        if (getParent.apply(z) == getLeft.apply(getParent.apply(getParent.apply(z)))) {
          var y = getRight.apply(getParent.apply(getParent.apply(z)));
          if (getColor.apply(y) == RED) {
            setColor.accept(getParent.apply(z), BLACK);
            setColor.accept(y, BLACK);
            setColor.accept(getParent.apply(getParent.apply(z)), RED);
            z = getParent.apply(getParent.apply(z));
          }
          else {
            if (z == getRight.apply(getParent.apply(z))) {
              z = getParent.apply(z);
              leftRotate(z);
            }
            setColor.accept(getParent.apply(z), BLACK);
            setColor.accept(getParent.apply(getParent.apply(z)), RED);
            rightRotate(getParent.apply(getParent.apply(z)));
          }
        }
        else {
          var y = getLeft.apply(getParent.apply(getParent.apply(z)));
          if (getColor.apply(y) == RED) {
            setColor.accept(getParent.apply(z), BLACK);
            setColor.accept(y, BLACK);
            setColor.accept(getParent.apply(getParent.apply(z)), RED);
            z = getParent.apply(getParent.apply(z));
          }
          else {
            if (z == getLeft.apply(getParent.apply(z))) {
              z = getParent.apply(z);
              rightRotate(z);
            }
            setColor.accept(getParent.apply(z), BLACK);
            setColor.accept(getParent.apply(getParent.apply(z)), RED);
            leftRotate(getParent.apply(getParent.apply(z)));
          }
        }
      }
      setColor.accept(getRoot.get(), BLACK);
    }

    void delete(Node z) {
      var y = z;
      var y_origin_color = getColor.apply(y);
      Node x;
      if (getLeft.apply(z) == sentinel) {
        x = getRight.apply(z);
        RBTransplant(z, getRight.apply(z));
      }
      else if (getRight.apply(z) == sentinel) {
        x = getLeft.apply(z);
        RBTransplant(z, getLeft.apply(z));
      }
      else {
        y = minimumNodeOf(getRight.apply(z));
        y_origin_color = getColor.apply(y);
        x = getRight.apply(y);
        if (getParent.apply(y) == z) {
          setParent.accept(x, y);
        }
        else {
          RBTransplant(y, getRight.apply(y));
          setRight.accept(y, getRight.apply(z));
          setParent.accept(getRight.apply(y), y);
        }
        RBTransplant(z, y);
        setLeft.accept(y, getLeft.apply(z));
        setParent.accept(getLeft.apply(y), y);
        setColor.accept(y, getColor.apply(z));
      }

      {//
        if (y instanceof OrderStatTree.Node<?, ?>) {
          if (getRight.apply(y) != sentinel) {
            y = minimumNodeOf(getRight.apply(y));
          }
          //noinspection PatternVariableCanBeUsed
          var yo = (OrderStatTree.Node<?, ?>) y;
          while (yo != sentinel) {
            yo.size = yo.right.size + yo.left.size + 1;
            yo = yo.parent;
          }
        }
      }

      if (y_origin_color == BLACK) {
        deleteFixUp(x);
      }
    }

    private void deleteFixUp(Node x) {
      while (x != getRoot.get() && getColor.apply(x) == BLACK) {
        if (x == getLeft.apply(getParent.apply(x))) {
          var w = getRight.apply(getParent.apply(x));
          if (getColor.apply(w) == RED) {
            setColor.accept(w, BLACK);
            setColor.accept(getParent.apply(x), RED);
            leftRotate(getParent.apply(x));
            w = getRight.apply(getParent.apply(x));
          }
          if (getColor.apply(getLeft.apply(w)) == BLACK && getColor.apply(getRight.apply(w)) == BLACK) {
            setColor.accept(w, RED);
            x = getParent.apply(x);
          }
          else {
            if (getColor.apply(getRight.apply(w)) == BLACK) {
              setColor.accept(getLeft.apply(w), BLACK);
              setColor.accept(w, RED);
              rightRotate(w);
              w = getRight.apply(getParent.apply(x));
            }
            setColor.accept(w, getColor.apply(getParent.apply(x)));
            setColor.accept(getParent.apply(x), BLACK);
            setColor.accept(getRight.apply(w), BLACK);
            leftRotate(getParent.apply(x));
            x = getRoot.get();
          }
        }
        else {
          var w = getLeft.apply(getParent.apply(x));
          if (getColor.apply(w) == RED) {
            setColor.accept(w, BLACK);
            setColor.accept(getParent.apply(x), RED);
            rightRotate(getParent.apply(x));
            w = getLeft.apply(getParent.apply(x));
          }
          if (getColor.apply(getLeft.apply(w)) == BLACK && getColor.apply(getRight.apply(w)) == BLACK) {
            setColor.accept(w, RED);
            x = getParent.apply(x);
          }
          else {
            if (getColor.apply(getLeft.apply(w)) == BLACK) {
              setColor.accept(getRight.apply(w), BLACK);
              setColor.accept(w, RED);
              leftRotate(w);
              w = getLeft.apply(getParent.apply(x));
            }
            setColor.accept(w, getColor.apply(getParent.apply(x)));
            setColor.accept(getParent.apply(x), BLACK);
            setColor.accept(getLeft.apply(w), BLACK);
            rightRotate(getParent.apply(x));
            x = getRoot.get();
          }
        }
      }
      setColor.accept(x, BLACK);
    }

    private Node minimumNodeOf(Node x) {
      while (getLeft.apply(x) != sentinel) {
        x = getLeft.apply(x);
      }
      return x;
    }

    private void RBTransplant(Node u, Node v) {
      if (getParent.apply(u) == sentinel) {
        setRoot.accept(v);
      }
      else if (u == getLeft.apply(getParent.apply(u))) {
        setLeft.accept(getParent.apply(u), v);
      }
      else {
        setRight.accept(getParent.apply(u), v);
      }
      setParent.accept(v, getParent.apply(u));
    }

    private void leftRotate(Node x) {
      var y = getRight.apply(x);

      setRight.accept(x, getLeft.apply(y));
      if (getLeft.apply(y) != sentinel) {
        setParent.accept(getLeft.apply(y), x);
      }

      setParent.accept(y, getParent.apply(x));
      if (getParent.apply(x) == sentinel) {
        setRoot.accept(y);
      }
      else if (x == getLeft.apply(getParent.apply(x))) {
        setLeft.accept(getParent.apply(x), y);
      }
      else {
        setRight.accept(getParent.apply(x), y);
      }

      setLeft.accept(y, x);
      setParent.accept(x, y);
      {//
        if (x instanceof OrderStatTree.Node<?, ?> && y instanceof OrderStatTree.Node<?, ?>) {
          OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
          OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
          yo.size = xo.size;
          xo.size = xo.left.size + xo.right.size + 1;
        }
      }
    }

    private void rightRotate(Node x) {
      var y = getLeft.apply(x);

      setLeft.accept(x, getRight.apply(y));
      if (getRight.apply(y) != sentinel) {
        setParent.accept(getRight.apply(y), x);
      }

      setParent.accept(y, getParent.apply(x));
      if (getParent.apply(x) == sentinel) {
        setRoot.accept(y);
      }
      else if (x == getRight.apply(getParent.apply(x))) {
        setRight.accept(getParent.apply(x), y);
      }
      else {
        setLeft.accept(getParent.apply(x), y);
      }

      setRight.accept(y, x);
      setParent.accept(x, y);
      {//
        if (x instanceof OrderStatTree.Node<?, ?> && y instanceof OrderStatTree.Node<?, ?>) {
          OrderStatTree.Node<?, ?> yo = (OrderStatTree.Node<?, ?>) y;
          OrderStatTree.Node<?, ?> xo = (OrderStatTree.Node<?, ?>) x;
          yo.size = xo.size;
          xo.size = xo.left.size + xo.right.size + 1;
        }
      }
    }
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

  /**
   * #239
   *
   * @param nums
   * @param k
   * @return
   */
  public static int[] maxSlidingWindow(int[] nums, int k) {
    if (k == 1) return nums;
    var reverse_bin_str = toReversedBinaryString(k);
    int[][] dp = new int[reverse_bin_str.length()][];
    dp[0] = nums;
    for (int i = 1; i < dp.length; i++) {
      int wind_len = (int) Math.pow(2, i);
      dp[i] = new int[nums.length + 1 - wind_len];
    }
    for (int i = 1; i < dp.length; i++) {
      int wind_len = (int) Math.pow(2, i);
      for (int j = 0; j < dp[i].length; j++) {
        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j + wind_len / 2]);
      }
    }
    int[] ans = new int[nums.length + 1 - k];
    for (int i = 0; i < ans.length; i++) {
      int max = Integer.MIN_VALUE;
      int start = i;
      for (int j = 0; j < reverse_bin_str.length(); j++) {
        if (reverse_bin_str.charAt(j) == '1') {
          max = Math.max(max, dp[j][start]);
          start += Math.pow(2, j);
        }
      }
      ans[i] = max;
    }
    return ans;
  }

  private static String toReversedBinaryString(int k) {
    StringBuilder sb = new StringBuilder();
    while (k != 0) {
      var rem = k % 2;
      sb.append(rem);
      k = k / 2;
    }
    return sb.toString();
  }

  public static int[] maxSlidingWindow2(int[] nums, int k) {
    if (k == 1) return nums;
    PriorityQueue<Integer> queue = new PriorityQueue<>((a, b) -> b - a);
    int[] ans = new int[nums.length + 1 - k];
    for (int i = 0; i < k; i++) {
      queue.add(nums[i]);
    }
    Map<Integer, Integer> waitToDelete = new HashMap<>();
    for (int i = 0, tail = 0, head = k; i < ans.length; i++, tail++, head++) {
      while (waitToDelete.containsKey(queue.peek())) {
        var peek = queue.poll();
        var count = waitToDelete.get(peek);
        if (count == 1) waitToDelete.remove(peek);
        else waitToDelete.put(peek, count - 1);
      }
      ans[i] = queue.peek();
      waitToDelete.put(nums[tail], waitToDelete.getOrDefault(nums[tail], 0) + 1);
      if (head < nums.length) {
        queue.add(nums[head]);
      }
    }
    return ans;
  }

  public static int[] maxSlidingWindow3(int[] nums, int k) {
    if (k == 1) return nums;
    int[] ans = new int[nums.length + 1 - k];
    class IndexValue {
      final int idx;
      final int val;

      IndexValue(int i, int v) {
        idx = i;
        val = v;
      }
    }

    Deque<IndexValue> deque = new ArrayDeque<>(k);
    var funcAddToDeque = new Object() {
      void apply(int i, int current_val) {
        if (deque.size() == 0) {
          deque.addLast(new IndexValue(i, current_val));
        }
        else if (deque.peekLast().val > current_val) {
          deque.addLast(new IndexValue(i, current_val));
        }
        else {
          while (deque.size() > 0 && deque.peekLast().val <= current_val) {
            deque.pollLast();
          }
          deque.addLast(new IndexValue(i, current_val));
        }
      }
    };
    for (int i = 0; i < k; i++) {
      funcAddToDeque.apply(i, nums[i]);
    }

    for (int i = 0, head = k; i < ans.length; i++, head++) {
      while (deque.size() > 0 && deque.peekFirst().idx < i) {
        deque.pollFirst();
      }
      ans[i] = deque.peekFirst().val;
      if(head < nums.length){
        funcAddToDeque.apply(head, nums[head]);
      }
    }
    return ans;
  }
}
