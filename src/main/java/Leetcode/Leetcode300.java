package Leetcode;

public class Leetcode300 {
  /**
   * #328
   * @param head
   * @return
   */
  public static ListNode oddEvenList(ListNode head) {
    if(head == null){
      return null;
    }
    ListNode oddTail = head, evenHead, evenTail;
    if(head.next == null){
      return head;
    }
    ListNode ptr = head.next;
    evenHead = ptr;
    evenTail = ptr;
    ptr = ptr.next;
    for(int i = 3; ptr != null; i++, ptr = ptr.next){
      if(i % 2 == 1){
        oddTail.next = ptr;
        oddTail = ptr;
      }
      else {
        evenTail.next = ptr;
        evenTail = ptr;
      }
    }
    oddTail.next = evenHead;
    evenTail.next = null;
    return head;
  }
}
