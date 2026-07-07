public class Main{
    public static void main(String[] args) {
        BankAccount account = new BankAccount("山田", 1000);
    account.deposit(500);      // 残高: 1500
    account.withdraw(2000);    // 残高不足です（残高は変わらず1500のまま）
    account.withdraw(300);     // 残高: 1200
    System.out.println(account.getOwner() + "さんの残高: " + account.getBalance());
    }
}