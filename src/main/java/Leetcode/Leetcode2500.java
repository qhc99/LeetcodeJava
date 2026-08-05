package Leetcode;

import java.util.*;

public class Leetcode2500 {

    /**
     * #2402
     * 
     * @param n
     * @param meetings
     * @return
     */
    public int mostBooked(int n, int[][] meetings) {
        int[] count = new int[n];
        Arrays.sort(meetings, (a, b) -> Integer.compare(a[0], b[0]));
        Queue<Integer> freeRooms = new PriorityQueue<>();
        Queue<MeetingRoom> busyRooms = new PriorityQueue<>(
                (a, b) -> Long.compare(a.end, b.end));
        for (int i = 0; i < n; i++)
            freeRooms.add(i);
        int id = 0, max = 0;
        long prevStart = 0;
        for (var meeting : meetings) {
            long start = meeting[0], end = meeting[1];
            if (start < prevStart) {
                // wait in line
                var wait = prevStart - start;
                start += wait;
                end += wait;
            }
            if (freeRooms.isEmpty() && busyRooms.peek().end > start) {
                // wait for first free room
                var wait = busyRooms.peek().end - start;
                start += wait;
                end += wait;
            }
            while (!busyRooms.isEmpty() && busyRooms.peek().end <= start) {
                freeRooms.add(busyRooms.poll().id);
            }
            var room = freeRooms.poll();
            busyRooms.add(new MeetingRoom(room, end));
            prevStart = start;
            count[room]++;
            if (count[room] > max || (count[room] == max && room < id)) {
                max = count[room];
                id = room;
            }
        }

        return id;
    }

    static record MeetingRoom(int id, long end) {
    }

    /**
     * #2408 SQL
     */
    class SQL {
        static class Table {
            int column;
            int id = 1;
            TreeMap<Integer, List<String>> rows = new TreeMap<>();

            Table(int col) {
                column = col;
            }

            boolean insert(List<String> row) {
                if (row.size() != column) {
                    return false;
                }
                rows.put(id++, row);
                return true;
            }

            void remove(int rowId) {
                rows.remove(rowId);
            }

            String select(int rowId, int columnId) {
                if (columnId < 1 || columnId > column)
                    return "<null>";
                var row = rows.getOrDefault(rowId, List.of());
                if (columnId - 1 >= row.size())
                    return "<null>";
                return row.get(columnId - 1);
            }

            public List<String> toExp() {
                List<String> res = new ArrayList<>();
                for (var e : rows.entrySet()) {
                    var sb = new StringBuilder();
                    sb.append(e.getKey());
                    sb.append(",");
                    sb.append(String.join(",", e.getValue()));
                    res.add(sb.toString());
                }
                return res;
            }
        }

        Map<String, Table> tables = new HashMap<>();

        public SQL(List<String> names, List<Integer> columns) {
            for (int i = 0; i < names.size(); i++) {
                tables.put(names.get(i), new Table(columns.get(i)));
            }
        }

        public boolean ins(String name, List<String> row) {
            var table = tables.get(name);
            if (table == null)
                return false;
            return table.insert(row);
        }

        public void rmv(String name, int rowId) {
            var table = tables.get(name);
            if (table == null)
                return;
            table.remove(rowId);
        }

        public String sel(String name, int rowId, int columnId) {
            var table = tables.get(name);
            if (table == null)
                return "<null>";
            return table.select(rowId, columnId);
        }

        public List<String> exp(String name) {
            return tables.getOrDefault(name, new Table(0)).toExp();
        }
    }

    /**
     * #2422
     * 
     * @param nums
     * @return
     */
    public int minimumOperations(int[] nums) {
        int i = 0, j = nums.length - 1;
        int ops = 0;
        while (i < j) {
            if (nums[i] == nums[j]) {
                i++;
                j--;
            } else if (nums[i] < nums[j]) {
                nums[i + 1] += nums[i];
                i++;
                ops++;
            } else {
                nums[j - 1] += nums[j];
                j--;
                ops++;
            }
        }

        return ops;
    }

    /**
     * #2433
     * 
     * @param pref
     * @return
     */
    public int[] findArray(int[] pref) {
        int[] res = new int[pref.length];
        res[0] = pref[0];
        for (int i = 1; i < pref.length; i++) {
            res[i] = pref[i] ^ pref[i - 1];
        }
        return res;
    }

    /**
     * #2456
     * 
     * @param creators
     * @param ids
     * @param views
     * @return
     */
    public List<List<String>> mostPopularCreator(String[] creators,
            String[] ids, int[] views) {
        Set<String> popularCreators = new HashSet<>();
        int maxCreatorViews = 0;
        Map<String, Integer> creatorViews = new HashMap<>();
        Map<String, Integer> creatorMostPopularMovieViews = new HashMap<>();
        Map<String, String> creatorMostPopularMovieId = new HashMap<>();
        Map<String, Map<String, Integer>> creatorMovieViews = new HashMap<>();
        int n = creators.length;

        for (int i = 0; i < n; i++) {
            var creator = creators[i];
            var movieName = ids[i];
            var view = views[i];
            var map = creatorMovieViews.computeIfAbsent(creator,
                    k -> new HashMap<>());
            var movieTotalViews = view + map.getOrDefault(movieName, 0);
            map.put(movieName, movieTotalViews);
            if (movieTotalViews > creatorMostPopularMovieViews
                    .getOrDefault(creator, -1)) {
                creatorMostPopularMovieViews.put(creator, movieTotalViews);
                creatorMostPopularMovieId.put(creator, movieName);
            } else if (movieTotalViews == creatorMostPopularMovieViews
                    .get(creator)
                    && movieName.compareTo(
                            creatorMostPopularMovieId.get(creator)) < 0) {
                creatorMostPopularMovieId.put(creator, movieName);

            }
            var creatorTotalViews = view
                    + creatorViews.getOrDefault(creator, 0);
            creatorViews.put(creator, creatorTotalViews);
            if (creatorTotalViews > maxCreatorViews) {
                maxCreatorViews = creatorTotalViews;
                popularCreators.clear();
                popularCreators.add(creator);
            } else if (creatorTotalViews == maxCreatorViews)
                popularCreators.add(creator);
        }
        return popularCreators.stream()
                .map(c -> List.of(c, creatorMostPopularMovieId.get(c)))
                .toList();
    }

    /**
     * #2461
     * 
     * @param nums
     * @param k
     * @return
     */
    public long maximumSubarraySum(int[] nums, int k) {
        long res = 0;
        Set<Integer> inQueue = new HashSet<>();
        Queue<Integer> queue = new ArrayDeque<>();
        long sum = 0;
        for (var n : nums) {
            while (inQueue.contains(n) || queue.size() >= k) {
                var v = queue.poll();
                sum -= v;
                inQueue.remove(v);
            }
            sum += n;
            inQueue.add(n);
            queue.add(n);
            if (queue.size() == k)
                res = Math.max(res, sum);
        }
        return res;
    }

    /**
     * #2483
     * 
     * @param customers
     * @return
     */
    public int bestClosingTime(String customers) {
        int totalY = 0;
        var arr = customers.toCharArray();
        for (var c : arr) {
            if (c == 'Y')
                totalY++;
        }
        int minPenalty = totalY;
        int res = 0;
        int currentY = 0;
        int currentN = 0;
        for (int i = 0; i <= arr.length; i++) {
            int penalty = currentN + totalY - currentY;
            if (penalty < minPenalty) {
                minPenalty = penalty;
                res = i;
            }
            if (i < arr.length) {
                var c = arr[i];
                if (c == 'Y')
                    currentY++;
                else
                    currentN++;
            }
        }

        return res;
    }
}
