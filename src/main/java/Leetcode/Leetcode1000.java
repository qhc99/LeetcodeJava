package Leetcode;

public class Leetcode1000
{

    /**
     * #968
     * <br/>监控二叉树
     * grady algorithm
     * @param root tree
     * @return number of camera
     */
    @SuppressWarnings("unused")
    public int minCameraCover(TreeNode root)
    {
        var solver = new MinCameraCoverSolver();
        if (solver.dfs(root) == MinCameraCoverSolver.Status.NOT_BEING_MONITORED)
        {
            solver.res++;
        }
        return solver.res;
    }

    private static class MinCameraCoverSolver
    {
        enum Status
        {
            NOT_BEING_MONITORED,
            BEING_MONITORED,
            CAMERA_INSTALLED
        }
        int res = 0;
        Status dfs(TreeNode tn)
        {
            if (tn == null)
            {
                return Status.BEING_MONITORED;
            }
            Status left = dfs(tn.left);
            Status right = dfs(tn.right);
            //左右子节点均已被监控，此时跳过
            if (left == Status.BEING_MONITORED && right == Status.BEING_MONITORED)
            {
                return Status.NOT_BEING_MONITORED;
            }
            //2种情况，1、一个子节点安装监控，另一个已被监控  2、两个子节点均有监控  此时不需要安装监控器，且该节点已被监控
            if ((left == Status.CAMERA_INSTALLED && right == Status.CAMERA_INSTALLED) ||
                    (left == Status.CAMERA_INSTALLED && right == Status.BEING_MONITORED) ||
                    (left == Status.BEING_MONITORED && right == Status.CAMERA_INSTALLED))
            {
                return Status.BEING_MONITORED;
            }
            //其他情况均需要安装监控，不然会有节点监控不到
            res++;
            return Status.CAMERA_INSTALLED;
        }
    }



}
