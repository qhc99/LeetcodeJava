package Leetcode;

public class Leetcode2050 {
    /**
     * #2043 Bank
     */
    class Bank {

        long[] balance;

        public Bank(long[] balance) {
            this.balance = balance;
        }

        boolean invalidAccount(int a) {
            return a < 1 || a > balance.length;
        }

        public boolean transfer(int account1, int account2, long money) {
            if (invalidAccount(account1) || invalidAccount(account2)
                    || balance[account1 - 1] < money)
                return false;
            balance[account1 - 1] -= money;
            balance[account2 - 1] += money;
            return true;
        }

        public boolean deposit(int account, long money) {
            if (invalidAccount(account))
                return false;
            balance[account - 1] += money;
            return true;
        }

        public boolean withdraw(int account, long money) {
            if (invalidAccount(account) || balance[account - 1] < money)
                return false;
            balance[account - 1] -= money;
            return true;
        }
    }
}
