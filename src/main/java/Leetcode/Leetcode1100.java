package Leetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

@SuppressWarnings("JavaDoc")
public class Leetcode1100 {

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
    Arrays.sort(trips, Comparator.comparing(i->i[1]));
    PriorityQueue<DestCap> queue = new PriorityQueue<>(Comparator.comparing(d -> d.dest));
    for (var trip : trips) {
      var c = trip[0];
      var start = trip[1];
      var dest = trip[2];
      while (queue.size() > 0 && queue.peek().dest <= start) {
        capacity += queue.poll().cap;
      }
      capacity -= c;
      if(capacity < 0){
        return false;
      }
      else {
        queue.add(new DestCap(dest,c));
      }
    }
    return true;
  }
}
