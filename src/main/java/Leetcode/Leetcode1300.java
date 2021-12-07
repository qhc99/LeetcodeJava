package Leetcode;

@SuppressWarnings("JavaDoc")
public class Leetcode1300 {


  /**
   * #1290
   * @param head
   * @return
   */
  public static int getDecimalValue(ListNode head){
    int ans = 0;
    do {
      ans *= 2;
      ans += head.val;
      head = head.next;
    }while (head != null);
    return ans;
  }
}
