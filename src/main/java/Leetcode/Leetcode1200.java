package Leetcode;

import java.util.*;

public class Leetcode1200 {
    /**
     * #1169
     * 
     * @param transactions
     * @return
     */
    public List<String> invalidTransactions(String[] transactions) {
        List<String> res = new ArrayList<>();
        List<Transaction> ts = new ArrayList<>();
        for (int i = 0; i < transactions.length; i++) {
            var t = transactions[i];
            var arr = t.split(",");
            ts.add(new Transaction(i, arr[0], Integer.valueOf(arr[1]),
                    Integer.valueOf(arr[2]), arr[3]));
        }
        ts.sort((a, b) -> {
            var c = a.name.compareTo(b.name);
            if (c != 0)
                return c;
            return a.time - b.time;
        });
        int s = 0;
        Set<Integer> invalid = new HashSet<>();
        for (int e = 0; e < ts.size(); e++) {
            var t = ts.get(e);
            if (t.amount > 1000) {
                res.add(transactions[t.idx]);
                invalid.add(t.idx);
            }
            if (!t.name.equals(ts.get(s).name)) {
                s = e;
            }
            while (t.time - ts.get(s).time > 60) {
                s++;
            }
            String city = ts.get(s).city;
            boolean anotherCity = false;
            for (int i = s + 1; i <= e; i++) {
                if (!ts.get(i).city.equals(city)) {
                    anotherCity = true;
                    break;
                }
            }
            if (anotherCity) {
                for (int i = s; i <= e; i++) {
                    if (!invalid.contains(ts.get(i).idx)) {
                        invalid.add(ts.get(i).idx);
                        res.add(transactions[ts.get(i).idx]);
                    }
                }
            }

        }
        return res;
    }

    static record Transaction(int idx, String name, int time, int amount,
            String city) {
    }
}
