package Leetcode;

import java.util.*;


public class Leetcode1050 {

    /**
     * #1006
     * <br/>笨阶乘
     * <pre>
     * clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1
     * </pre>
     * @param N number
     * @return result
     */
    public static int clumsy(int N) {
        Deque<Integer> stack = new LinkedList<>();
        stack.addLast(N);
        N--;
        int flag = 0;
        while(N >= 1){
            int reminder = flag%4;
            switch(reminder){
                case 0->{
                    stack.addLast(stack.removeLast()*N);
                    flag++;
                }
                case 1->{
                    stack.addLast(stack.removeLast()/N);
                    flag++;
                }
                case 2->{
                    stack.addLast(N);
                    flag++;
                }
                case 3->{
                    stack.addLast(-N);
                    flag = 0;
                }
            }
            N--;
        }
        return stack.parallelStream().mapToInt(Integer::intValue).sum();
    }

    /**
     * #1024
     * <br/>视频剪辑
     * <pre>
     * 输入：clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], T = 10
     * 输出：3
     * 解释：
     * 我们选中 [0,2], [8,10], [1,9] 这三个片段。
     * 然后，按下面的方案重制比赛片段：
     * 将 [1,9] 再剪辑为 [1,2] + [2,8] + [8,9] 。
     * 现在我们手上有 [0,2] + [2,8] + [8,10]，而这些涵盖了整场比赛 [0, 10]。
     * </pre>
     * @param clips video clips
     * @param T video length
     * @return is intact
     */
    public static int videoStitching(int[][] clips, int T) {
        int[] startAndEnd = new int[T];
        for(var clip : clips){
            if(clip[0] < T){
                startAndEnd[clip[0]] = Math.max(startAndEnd[clip[0]], clip[1]);
            }
        }
        int remotest = 0, preRemotest = 0, count = 0;
        for(int i = 0; i < startAndEnd.length; i++){
            remotest = Math.max(remotest, startAndEnd[i]);
            if(i == remotest){
                return -1;
            }
            if(i == preRemotest){
                count++;
                preRemotest = remotest;
            }
        }
        return count;
    }
}
