# トピック4: 継承（extends、super）

## 1. これは何か、なぜ必要か

継承とは、あるクラス（親クラス／スーパークラス）が持っているフィールドやメソッドを、
別のクラス（子クラス／サブクラス）がそのまま受け継いで使えるようにする仕組みです。
「AはBの一種である」（is-a関係）と言える場合に使います。

例えば、トピック3で作った`BankAccount`（銀行口座）を考えます。世の中には普通の口座
だけでなく、「定期預金口座（利息が付く）」もあります。定期預金口座も`owner`、`balance`、
`deposit`、`withdraw`といった性質は普通の口座と全く同じで、「利息を計算する」機能が
追加されるだけです。

継承を使わないと、`SavingsAccount`クラスをゼロから書き直すことになり、`BankAccount`
と全く同じコードを重複して書くことになってしまいます（コードの重複はバグの温床です）。
継承を使えば、`BankAccount`が持つ機能をそのまま引き継ぎ、差分（利息計算など）だけを
追加で書けばよくなります。

## 2. Javaでの書き方・構文（最小サンプル）

```java
// 親クラス（スーパークラス）
public class BankAccount {
    private String owner;
    private int balance;

    public BankAccount(String owner, int balance) {
        this.owner = owner;
        setBalance(balance);
    }

    public String getOwner() { return owner; }
    public int getBalance() { return balance; }

    public void setBalance(int balance) {
        this.balance = (balance < 0) ? 0 : balance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            System.out.println("入金額は正の数にしてください");
            return;
        }
        balance += amount;
        System.out.println("残高: " + balance);
    }
}

// 子クラス（サブクラス）：extendsで親クラスを継承する
public class SavingsAccount extends BankAccount {
    private double interestRate; // 利率（定期預金口座だけが持つ追加のフィールド）

    public SavingsAccount(String owner, int balance, double interestRate) {
        super(owner, balance); // 親クラスのコンストラクタを呼び出す（必ず1行目に書く）
        this.interestRate = interestRate;
    }

    // 子クラスだけが持つ追加のメソッド
    public void addInterest() {
        int interest = (int) (getBalance() * interestRate);
        deposit(interest); // 親クラスのdepositメソッドをそのまま使える
        System.out.println("利息" + interest + "円が付与されました");
    }
}
```

```java
SavingsAccount account = new SavingsAccount("山田", 10000, 0.05);
account.deposit(5000);      // 親クラスのメソッドがそのまま使える → 残高: 15000
account.addInterest();      // 子クラス独自のメソッド → 利息750円が付与されました
System.out.println(account.getOwner()); // 親クラスのgetterもそのまま使える → 山田
```

ポイント：
- `extends` … 親クラスを継承することを宣言するキーワード
- `super(...)` … 親クラスのコンストラクタを呼び出す。子クラスのコンストラクタの
  **一番最初の行**に書く必要がある（省略すると親の引数なしコンストラクタが自動的に
  呼ばれるが、今回のように引数ありのコンストラクタしかない場合は明示的に書く必要がある）
- 子クラスは親クラスの`public`なフィールド・メソッドをそのまま自分のものとして使える
  （ただし`private`なフィールドには直接アクセスできない。getter/setter経由で使う）
- `protected` … `private`よりゆるく、子クラス（や同じパッケージ）からは直接アクセス
  できるアクセス修飾子

## 3. Pythonとの対比

Pythonの継承も基本的な考え方は同じですが、書き方が異なります。

```python
class BankAccount:
    def __init__(self, owner, balance):
        self.owner = owner
        self.balance = balance if balance >= 0 else 0

    def deposit(self, amount):
        if amount <= 0:
            print("入金額は正の数にしてください")
            return
        self.balance += amount
        print(f"残高: {self.balance}")

class SavingsAccount(BankAccount):  # ()の中に親クラスを書く
    def __init__(self, owner, balance, interest_rate):
        super().__init__(owner, balance)  # super().__init__(...)で親のコンストラクタを呼ぶ
        self.interest_rate = interest_rate

    def add_interest(self):
        interest = int(self.balance * self.interest_rate)
        self.deposit(interest)
        print(f"利息{interest}円が付与されました")
```

```java
public class SavingsAccount extends BankAccount { // extendsキーワードを使う
    public SavingsAccount(String owner, int balance, double interestRate) {
        super(owner, balance); // super(...)で親のコンストラクタを呼ぶ（super().__init__ではない）
        // ...
    }
}
```

| 項目 | Python | Java |
|---|---|---|
| 継承の宣言 | `class Child(Parent):` | `class Child extends Parent {}` |
| 親コンストラクタ呼び出し | `super().__init__(...)` | `super(...)` |
| 親コンストラクタ呼び出しの位置 | どこに書いてもよい（通常は先頭） | 必ずコンストラクタの最初の1行でなければならない（それ以外はコンパイルエラー） |
| 多重継承 | 複数の親クラスを継承できる（`class C(A, B):`） | 1つの親クラスしか継承できない（複数の型を扱いたい場合はinterfaceを使う） |
| privateフィールドへのアクセス | 実質アクセス可能（強制力なし） | 子クラスでも直接アクセス不可。getter経由が必要 |

なぜJavaは1つの親クラスしか継承できないのかというと、複数の親から同じ名前のメソッドを
継承した場合にどちらを使うべきか曖昧になる（いわゆる「ダイヤモンド問題」）ことを、
言語仕様レベルで防ぐためです。Javaで「複数の型としてふるまいたい」場合はinterface
（トピック6で扱います）を使います。

## 4. つまずきやすいポイント・よくある間違い

- **`super(...)`を書き忘れる**：親クラスに引数なしコンストラクタが存在しない場合、
  `super(...)`を書かないとコンパイルエラーになる（「暗黙のsuper()」が呼べないため）
- **`super(...)`を1行目以外に書いてしまう**：Javaでは必ずコンストラクタの最初の行に
  書かなければならず、それ以外の場所に書くとコンパイルエラーになる
- **親クラスの`private`フィールドに子クラスから直接アクセスしようとしてしまう**：
  継承していても`private`は`private`なので、getter/setterか`protected`にする必要がある
- **「継承すれば全部使える」と思ってしまう**：継承されるのは`public`と`protected`の
  メンバーだけで、`private`なフィールド自体は（存在はしているが）子クラスから
  直接読み書きできない

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
