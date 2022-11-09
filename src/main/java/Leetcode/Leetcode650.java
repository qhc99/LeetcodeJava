package Leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leetcode650 {

    /**
     * #638
     *
     * @param price
     * @param special
     * @param needs
     * @return
     */
    public static int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
        List<List<Integer>> valid_special = new ArrayList<>(special.size());
        for (var spec : special) {
            if (spec.get(spec.size() - 1) < non_special_price_of(spec.subList(0, spec.size() - 1), price)) {
                valid_special.add(spec);
            }
        }
        Map<String, Integer> cache = new HashMap<>(1024);
        return min_offer_of(needs, price, valid_special, cache);
    }

    private static int min_offer_of(
            List<Integer> current_needs,
            List<Integer> price,
            List<List<Integer>> special,
            Map<String, Integer> cache) {
        var str_current_needs = current_needs.toString();
        if (cache.containsKey(str_current_needs)) {
            return cache.get(str_current_needs);
        }
        else {
            int non_spec_price = non_special_price_of(current_needs, price);
            int min_spec_price = Integer.MAX_VALUE;
            for (var spec : special) {
                var new_need = new_needs(current_needs, spec);
                if (new_need != null) {
                    min_spec_price = Math.min(min_spec_price, min_offer_of(new_need, price, special, cache) + spec.get(spec.size()-1));
                }
            }
            var ans = Math.min(non_spec_price, min_spec_price);
            cache.put(str_current_needs, ans);
            return ans;
        }
    }

    private static List<Integer> new_needs(List<Integer> current_needs, List<Integer> spec) {
        List<Integer> n = new ArrayList<>(current_needs.size());
        for (int i = 0; i < current_needs.size(); i++) {
            var n_i = current_needs.get(i) - spec.get(i);
            if (n_i >= 0) n.add(n_i);
            else return null;
        }
        return n;
    }

    private static int non_special_price_of(List<Integer> needs, List<Integer> price) {
        int ans = 0;
        for (int i = 0; i < needs.size(); i++) {
            ans += needs.get(i) * price.get(i);
        }
        return ans;
    }

    /**
     * #643
     *
     * @param nums
     * @param k
     * @return
     */
    public static double findMaxAverage(int[] nums, int k) {
        int sum = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= k) {
                sum -= nums[i - k];
            }
            if (i + 1 >= k) {
                max = Math.max(sum, max);
            }

        }
        return max / (double) k;
    }
}
