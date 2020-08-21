

import java.util.*;

import leetcode.Leetcode50.MatrixIndex;

public class Main {
    public static void main(String[] args){
        long t1 = System.nanoTime();
        for (int i = 0; i < 1; i++) {
            char[][] t = new char[][]{
                    {'5', '3', '.', '.', '7', '.', '.', '.', '.'},
                    {'6', '.', '.', '1', '9', '5', '.', '.', '.'},
                    {'.', '9', '8', '.', '.', '.', '.', '6', '.'},
                    {'8', '.', '.', '.', '6', '.', '.', '.', '3'},
                    {'4', '.', '.', '8', '.', '3', '.', '.', '1'},
                    {'7', '.', '.', '.', '2', '.', '.', '.', '6'},
                    {'.', '6', '.', '.', '.', '.', '2', '8', '.'},
                    {'.', '.', '.', '4', '1', '9', '.', '.', '5'},
                    {'.', '.', '.', '.', '8', '.', '.', '7', '9'}
            };
            SolveSudoku(t);
        }
        long t2 = System.nanoTime();
        System.out.println((t2-t1)/Math.pow(10,9));


    }

    public static void SolveSudoku(char[][] board)
    {
        List<MatrixIndex> spaces = new ArrayList<>();
        for (int r = 0; r < 9; r++)
        {
            for (int c = 0; c < 9; c++)
            {
                char f = board[r][c];
                if (f == '.')
                {
                    spaces.add(new MatrixIndex(r, c));
                }
            }
        }

        Map<MatrixIndex, List<MatrixIndex>> relatedSpaces = new HashMap<>();
        for(var spacePtr1 : spaces)
        {
            relatedSpaces.put(spacePtr1, new ArrayList<>());
            for (var spacePtr2 : spaces)
            {
                if (!spacePtr2.equals(spacePtr1) && Related(spacePtr1, spacePtr2))
                {
                    relatedSpaces.get(spacePtr1).add(spacePtr2);
                }
            }
        }

        Map<MatrixIndex, HashSet<Character>> availableChars = new HashMap<>();
        for (var space : spaces) {
            availableChars.put(space, GetAvailableChars(space.row, space.col, board));
        }

        DepthFirstSearchSudoku(spaces, 0, board, availableChars, relatedSpaces, new HashSet<>());

    }

    static boolean DepthFirstSearchSudoku(List<MatrixIndex> spaces, int spaceIdx, char[][] board,
                                          Map<MatrixIndex, HashSet<Character>> availableChars,
                                          Map<MatrixIndex, List<MatrixIndex>> relatedSpaces,
                                          HashSet<MatrixIndex> encountered)
    {
        if (spaceIdx == spaces.size())
        {
            return true;
        }

        MatrixIndex currentSpace = spaces.get(spaceIdx);
        encountered.add(currentSpace);
        int rIdx = currentSpace.row;
        int cIdx = currentSpace.col;
        for (var chr : availableChars.get(currentSpace))
        {
            board[rIdx][cIdx] = chr;
            List<Boolean> record = new ArrayList<>();
            for (var relatedSpace : relatedSpaces.get(currentSpace))
            {
                if (!encountered.contains(relatedSpace))
                {
                    record.add(availableChars.get(relatedSpace).remove(chr));
                }
            }
            boolean success = DepthFirstSearchSudoku(spaces, spaceIdx + 1, board, availableChars, relatedSpaces,encountered);
            if (success) return true;
            int accIdx = 0;
            for (var relatedSpace : relatedSpaces.get(currentSpace))
            {
                if (!encountered.contains(relatedSpace))
                {
                    if (record.get(accIdx++))
                    {
                        availableChars.get(relatedSpace).add(chr);
                    }
                }
            }

        }

        board[rIdx][cIdx] = '.';
        encountered.remove(currentSpace);
        return false;
    }


    static int BoxIndex(int row, int columns) {
        return (row / 3) * 3 + columns / 3;
    }

    static boolean Related(MatrixIndex a, MatrixIndex b)
    {

        return a.row == b.row || a.col == b.col
                || BoxIndex(a.row, a.col) == BoxIndex(b.row, b.col);
    }

    static HashSet<Character> GetAvailableChars(int rIdx, int cIdx, char[][] board)
    {
        Map<Integer, int[][]> groupIdxToBoxIdx = Map.of(
                0, new int[][]{{0,1,2},{0,1,2}},
                1, new int[][]{{0,1,2},{3,4,5}},
                2, new int[][]{{0,1,2},{6,7,8}},
                3, new int[][]{{3,4,5},{0,1,2}},
                4, new int[][]{{3,4,5},{3,4,5}},
                5, new int[][]{{3,4,5},{6,7,8}},
                6, new int[][]{{6,7,8},{0,1,2}},
                7, new int[][]{{6,7,8},{3,4,5}},
                8, new int[][]{{6,7,8},{6,7,8}});
        var availableChars = new HashSet<Character>();
        for (int i = 1; i <= 9; i++)
        {
            availableChars.add((char) (i + '0'));
        }

        for (int j = 0; j < 9; j++)
        {
            char chr = board[rIdx][j];
            if (chr != '.')
            {
                availableChars.remove(chr);
            }
        }

        for (int i = 0; i < 9; i++)
        {
            char chr = board[i][cIdx];
            if (chr != '.')
            {
                availableChars.remove(chr);
            }
        }

        int groupIdx = (rIdx / 3) * 3 + cIdx / 3;
        int[][] groupIndices = groupIdxToBoxIdx.get(groupIdx);
        int[] rowIndices = groupIndices[0];
        int[] colIndices = groupIndices[1];
        for (var i : rowIndices)
        {
            for (var j : colIndices)
            {
                char chr = board[i][j];
                if (chr != '.')
                {
                    availableChars.remove(chr);
                }
            }
        }

        return availableChars;
    }

    
}
