package Leetcode;

@SuppressWarnings("unused")
public class ListNode
{
    public int val;
    public ListNode next;

    public ListNode(int x)
    {
        val = x;
    }

    public ListNode()
    {
    }

    public ListNode(int x, ListNode o)
    {
        val = x;
        next = o;
    }
}
