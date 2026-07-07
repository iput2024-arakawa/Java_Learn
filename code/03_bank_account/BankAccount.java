public class BankAccount{
    private String owner;
    private int balance;

    public BankAccount(String owner, int balance){
        this.owner = owner;
        setBalance(balance);
    }

    public String getOwner(){
        return owner;
    }

    public int getBalance(){
        return balance;
    }

    public void setBalance(int balance){
        if(balance < 0){
            this.balance = 0;
        }else{
            this.balance = balance;
        }
    }

    public void deposit(int amount){
        if(amount <= 0){
            System.out.println("入金額は正の数にしてください");
            return;
        }
        balance += amount;
        System.out.println("残高: " + balance);
    }

    public void  withdraw(int amount){
        if (amount > 0 && amount <= balance){
            balance -= amount;
            System.out.println("残高: " + balance);
        } else {
            System.out.println("残高不足です");
        }
    }
}