package Leetcode;

import java.util.*;

public class Leetcode1650 {
    /**
     * #1604
     * 
     * @param keyName
     * @param keyTime
     * @return
     */
    public List<String> alertNames(String[] keyName, String[] keyTime) {

        Set<String> alerted = new HashSet<>();
        List<String> res = new ArrayList<>();
        List<Entry> entries = new ArrayList<>(keyName.length);
        for (int i = 0; i < keyName.length; i++) {
            entries.add(new Entry(keyName[i], keyTime[i].split(":")));
        }
        entries.sort((a, b) -> {
            var c1 = a.name.compareTo(b.name);
            if (c1 != 0)
                return c1;
            return Arrays.compare(a.time, b.time);
        });
        int s = 0;
        for (int e = 0; e < entries.size(); e++) {
            var entry = entries.get(e);
            if (alerted.contains(entry.name))
                continue;
            if (!entries.get(s).name.equals(entry.name)) {
                s = e;
                continue;
            }
            while (!inOneHour(entries.get(s).time, entry.time)) {
                s++;
            }
            if (e - s >= 2) {
                alerted.add(entry.name);
                res.add(entry.name);
            }

        }
        return res;
    }

    boolean inOneHour(String[] s, String[] e) {
        var d1 = Integer.valueOf(e[0]) - Integer.valueOf(s[0]);
        var d2 = Integer.valueOf(e[1]) - Integer.valueOf(s[1]);
        return d1 * 60 + d2 <= 60;
    }

    static record Entry(String name, String[] time) {
    }
}
