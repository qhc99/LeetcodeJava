package Leetcode;

@SuppressWarnings("JavaDoc")
public class Leetcode950 {

  /**
   * #902
   *
   * @param digits
   * @param n
   * @return
   */
  public static int atMostNGivenDigitSet(String[] digits, int n) {
    int min_num_of_every_len = Integer.parseInt(digits[0]);
    int start_digit = min_num_of_every_len;
    int order = 1;
    while (min_num_of_every_len <= n) {
      order = order * digits.length + 1;
      min_num_of_every_len = min_num_of_every_len * 10 + start_digit;
    }

    int start = order;
    int end = order * digits.length + 1;

    while (end - start > 1) {
      var mid = (start + end) / 2;
      var num = nthNum(digits, mid);
      if (num <= n) start = mid;
      else end = mid;
    }
    return start;
  }

  /**
   * @param digits
   * @param n      start from 1, radix 10
   * @return
   */
  public static int nthNum(String[] digits, int n) {
    n--;
    if (n == 0) {
      return Integer.parseInt(digits[0]);
    }
    StringBuilder num = new StringBuilder();
    while (n != 0) {
      int idx = n % digits.length;
      num.insert(0, digits[idx]);
      n -= idx;
      n = n / digits.length;
    }
    return Integer.parseInt(num.toString());
  }

  /**
   * #925
   *
   * @param name  name string
   * @param typed input string
   * @return is long press
   */
  public static boolean isLongPressedName(String name, String typed) {
    if (name.charAt(0) != typed.charAt(0)) {
      return false;
    }
    char lastChar = name.charAt(0);
    int namePtr = 1, typedPtr = 1;
    for (; typedPtr < typed.length(); typedPtr++) {
      if (namePtr < name.length()) {
        if (typed.charAt(typedPtr) == name.charAt(namePtr)) {
          lastChar = name.charAt(namePtr);
          namePtr++;
        }
        else {
          if (typed.charAt(typedPtr) != lastChar) {
            return false;
          }
        }
      }
      else {
        if (typed.charAt(typedPtr) != lastChar) {
          return false;
        }
      }
    }
    return namePtr == name.length();
  }
}
