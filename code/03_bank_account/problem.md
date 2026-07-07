# トピック3 演習問題: カプセル化

## 問題

`BankAccount`（銀行口座）クラスを作成してください。

### 要件

1. `BankAccount` クラスを作成する
   - フィールド：`owner`（`String`型）、`balance`（`int`型、残高）
   - どちらのフィールドも**`private`**にする
   - コンストラクタ：`owner`と初期残高`balance`を受け取り初期化する
     - ただし、初期残高が負の値で渡された場合は`0`として初期化する
   - getter：`getOwner()`、`getBalance()`
   - メソッド `deposit(int amount)`（入金）
     - `amount`が正の数（1以上）なら残高に加算する
     - `amount`が0以下なら、加算せずに「入金額は正の数にしてください」と出力する
   - メソッド `withdraw(int amount)`（出金）
     - `amount`が正の数で、かつ残高以下なら残高から減算する
     - 残高が足りない場合は、減算せずに「残高不足です」と出力する
2. `Main`クラスの`main`メソッドで`BankAccount`のインスタンスを1つ作成し、
   入金・出金をいくつか試して、最終的な残高を出力する

### 期待する動作・出力例

```java
BankAccount account = new BankAccount("山田", 1000);
account.deposit(500);      // 残高: 1500
account.withdraw(2000);    // 残高不足です（残高は変わらず1500のまま）
account.withdraw(300);     // 残高: 1200
System.out.println(account.getOwner() + "さんの残高: " + account.getBalance());
```

出力：
```
残高不足です
山田さんの残高: 1200
```

### ヒント

- フィールドを`private`にすると、`account.balance`のように外部から直接触ることはできなくなる
- コンストラクタの中でも、直接`this.balance = balance;`とせず、
  マイナスの値をチェックしてから代入するようにする
- `deposit`/`withdraw`の中で残高を変更する処理は、フィールド`balance`を直接使ってよい
  （同じクラスの中からのアクセスなので`private`でも問題ない）

---

コードを書けたら、このフォルダ内に`BankAccount.java`または`Main.java`（1ファイルにまとめてもよい）として提出してください。レビューします。
