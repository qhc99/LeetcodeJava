package Leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("JavaDoc")
public class Leetcode1300 {

    /**
     * #1207 <br/>
     * 
     * <pre>
     * 输入：arr = [1,2,2,1,1,3]
     * 输出：true
     * 解释：在该数组中，1 出现了 3 次，2 出现了 2 次，3 只出现了 1 次。没有两个数的出现次数相同。
     * </pre>
     *
     * @param arr int array
     * @return is unique
     */
    public static boolean uniqueOccurrences(int[] arr) {
        Map<Integer, Integer> m = new HashMap<>();
        for (var i : arr) {
            m.put(i, m.getOrDefault(i, 0) + 1);
        }
        Set<Integer> counts = new HashSet<>(m.values());
        return counts.size() == m.keySet().size();
    }

    /**
     * arr = [1,5,7,8,5,3,4,2,1], difference = -2 answer: 4 ([7,5,3,1])
     *
     * @param difference d
     * @return length
     */
    public static int longestSubsequence(int[] arr, int difference) {
        Map<Integer, Integer> m = new HashMap<>(arr.length);
        int ans = 1;
        for (var i : arr) {
            var t = m.getOrDefault(i - difference, 0) + 1;
            m.put(i, t);
            ans = Math.max(ans, t);
        }
        return ans;
    }

    /**
     * #1217
     *
     * @param position
     * @return
     */
    public static int minCostToMoveChips(int[] position) {
        int odd = 0, even = 0;
        for (var p : position) {
            if (p % 2 == 0)
                even++;
            else
                odd++;
        }
        return Math.min(odd, even);
    }

    interface HtmlParser {
        public List<String> getUrls(String url);
    }

    /**
     * #1242
     * 
     * @param startUrl
     * @param htmlParser
     * @return
     */
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        int count = 16;
        Thread[] threads = new Thread[count];
        var workQueue = new WorkQueue();
        List<String> result = Collections.synchronizedList(new ArrayList<>());
        workQueue.add(startUrl);
        var targetDomain = startUrl.split("/")[2];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                while (!workQueue.isEnd()) {
                    var url = workQueue.poll();
                    if (url != null) {
                        if (url.split("/")[2].equals(targetDomain)) {
                            result.add(url);
                            var ret = htmlParser.getUrls(url);
                            for (var u : ret) {
                                if (u.split("/")[2].equals(targetDomain)) {
                                    workQueue.add(u);
                                }
                            }
                        }
                        workQueue.finishTask();
                    }
                }
            });

            threads[i].start();
        }
        for (var t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * #1288
     * 
     * @param intervals
     * @return
     */
    public int removeCoveredIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b) -> {
            var c = a[0] - b[0];
            if (c != 0)
                return c;
            return b[1] - a[1];
        });
        int top = -1;
        int rm = 0;
        for (int i = 1; i < intervals.length; i++) {
            var current = intervals[i];
            if (current[1] > top)
                top = current[1];
            else
                rm++;
        }

        return intervals.length - rm;
    }

    /**
     * #1290
     *
     * @param head
     * @return
     */
    public static int getDecimalValue(ListNode head) {
        int ans = 0;
        do {
            ans *= 2;
            ans += head.val;
            head = head.next;
        } while (head != null);
        return ans;
    }

}

class WorkQueue {
    private int activeTasks = 0;
    private Set<String> visited = new HashSet<>();
    private Queue<String> workQueue = new ArrayDeque<>();

    synchronized String poll() {
        var res = workQueue.poll();
        if (res != null) {
            activeTasks++;
        }
        return res;
    }

    synchronized void add(String url) {
        if (!visited.contains(url)) {
            workQueue.add(url);
            visited.add(url);
        }
    }

    synchronized void finishTask() {
        activeTasks--;
    }

    synchronized boolean isEnd() {
        return activeTasks == 0 && workQueue.isEmpty();
    }

}
