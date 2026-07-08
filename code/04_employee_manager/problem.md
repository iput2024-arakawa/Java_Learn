# トピック4 演習問題: 継承

## 問題

`Employee`（従業員）クラスと、それを継承した`Manager`（マネージャー）クラスを作成してください。

### 要件

1. `Employee`クラスを作成する
   - フィールド：`name`（`String`型）、`baseSalary`（`int`型、基本給）
   - どちらのフィールドも**`private`**にする
   - コンストラクタ：`name`と`baseSalary`を受け取り初期化する
   - getter：`getName()`、`getBaseSalary()`
   - メソッド`printInfo()`：`"名前: ○○, 給与: ○○円"`という形式で出力する

2. `Manager`クラスを作成し、`Employee`を**継承（`extends`）**する
   - 追加のフィールド：`managementAllowance`（`int`型、役職手当）を**`private`**で持つ
   - コンストラクタ：`name`、`baseSalary`、`managementAllowance`を受け取る
     - `super(...)`を使って親クラスのコンストラクタに`name`と`baseSalary`を渡すこと
   - 追加のメソッド`getTotalSalary()`：`getBaseSalary()`（継承したgetter）と
     `managementAllowance`を足した合計金額を`int`で返す
   - `printInfo()`は`Manager`クラスの中で新たに定義せず、**親クラスから継承したものを
     そのまま使う**こと（今回はオーバーライドを使わない）

3. `Main`クラスの`main`メソッドで、`Employee`のインスタンスを1つ、`Manager`のインスタンスを
   1つ作成し、それぞれの情報と、`Manager`については合計給与も出力する

### 期待する動作・出力例

```java
Employee employee = new Employee("佐藤", 300000);
employee.printInfo();

Manager manager = new Manager("鈴木", 350000, 50000);
manager.printInfo();
System.out.println("役職手当込みの合計給与: " + manager.getTotalSalary() + "円");
```

出力：
```
名前: 佐藤, 給与: 300000円
名前: 鈴木, 給与: 350000円
役職手当込みの合計給与: 400000円
```

### ヒント

- `Manager`のコンストラクタの**1行目**で`super(name, baseSalary);`を呼び出すこと
  （書き忘れる、または1行目以外に書くとコンパイルエラーになる）
- `printInfo()`は`Manager`クラスの中には一切書かない。継承しているのでそのまま呼び出せる
- `getTotalSalary()`の中では、`baseSalary`に直接アクセスせず、
  親クラスのgetterである`getBaseSalary()`を経由すること（`baseSalary`は親クラスの
  `private`フィールドなので、子クラスから直接は触れない）

---

コードを書けたら、このフォルダ内に`Employee.java`、`Manager.java`、`Main.java`
（まとめても構いません）として提出してください。レビューします。
