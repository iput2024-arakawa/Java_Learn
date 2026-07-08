# トピック5: ポリモーフィズム（オーバーライド、参照型と実体）

## 1. これは何か、なぜ必要か

ポリモーフィズム（polymorphism、日本語で「多態性」）とは、
**「同じ呼び出し方をしても、実際のインスタンスの種類によって異なる動作が起きる」**という性質のことです。

前回のトピック4では、あえて`printInfo()`をオーバーライドせず、
「親クラスのメソッドをそのまま使う」ケースを扱いました。
今回はその逆で、**子クラスで親クラスのメソッドを上書き（オーバーライド）する**ことで、
「同じ`printInfo()`という呼び方なのに、`Employee`なら基本給だけ、`Manager`なら役職手当込みの合計を表示する」
といったことができるようになります。

これがなぜ必要かというと、例えば「従業員のリストを1つのループでまとめて処理したい」場合に、
一つ一つの要素が`Employee`なのか`Manager`なのかを`if`文でいちいち判定しなくても、
**「それぞれが自分に合った`printInfo()`を勝手に実行してくれる」**という書き方ができるからです。
これによってコードの分岐が減り、新しい子クラス（例えば`Director`）を追加したときも
呼び出し側のコードを変更せずに済みます。

## 2. Javaでの書き方・構文

### オーバーライドの書き方

```java
public class Employee {
    protected String name;      // 子クラスからも直接見えるように protected に変更する場合がある
    protected int baseSalary;

    public void printInfo() {
        System.out.println("名前: " + name + ", 給与: " + baseSalary + "円");
    }
}

public class Manager extends Employee {
    private int managementAllowance;

    @Override
    public void printInfo() {
        int total = baseSalary + managementAllowance;
        System.out.println("名前: " + name + ", 合計給与: " + total + "円（役職手当込み）");
    }
}
```

ポイント：
- 親クラスと**全く同じシグネチャ**（メソッド名・引数・戻り値の型）でメソッドを定義すると、
  それが「上書き」として扱われます。
- `@Override`アノテーションを付けるのが**必須ではないが強く推奨**されます。
  もし親クラスに該当するメソッドが存在しない状態で`@Override`を付けると、
  **コンパイルエラーになる**ので、タイプミス（例：`printinfo`など）にすぐ気づけます。

### 参照型と実体（重要な概念）

Javaでは「変数の型（見た目上の型）」と「実際に入っているインスタンスの種類（実体の型）」が
異なることがあります。

```java
Employee e = new Manager("鈴木", 350000, 50000);
e.printInfo();
```

このとき：
- `e`という変数の**型（参照型）**は`Employee`
- しかし中身の**実体**は`Manager`のインスタンス

`e.printInfo()`を呼んだとき、実行されるのは**実体（Manager）側の`printInfo()`**です。
変数の型が`Employee`だからといって、`Employee`のメソッドが呼ばれるわけではありません。
これが「動的束縛（dynamic binding）」または「実行時ポリモーフィズム」と呼ばれる仕組みです。

これを利用すると、配列やリストに`Employee`型としてまとめて格納しても、
それぞれが自分自身の実体に応じた動作をしてくれます。

```java
Employee[] staff = {
    new Employee("佐藤", 300000),
    new Manager("鈴木", 350000, 50000)
};

for (Employee person : staff) {
    person.printInfo();  // 佐藤はEmployeeのprintInfo、鈴木はManagerのprintInfoが呼ばれる
}
```

## 3. Pythonとの対比

Pythonでもオーバーライド自体は同じように書けます。

```python
class Employee:
    def print_info(self):
        print(f"名前: {self.name}, 給与: {self.base_salary}円")

class Manager(Employee):
    def print_info(self):  # 同名メソッドを定義するだけでオーバーライドになる
        total = self.base_salary + self.management_allowance
        print(f"名前: {self.name}, 合計給与: {total}円（役職手当込み）")
```

Pythonとの大きな違いは2点あります。

1. **型を明示的に書くかどうか**
   Pythonでは`e = Manager(...)`と書いても`e`という変数自体に「型」という概念が
   （少なくとも書き方の上では）はっきり現れません。実行時にどんなオブジェクトが
   入っていても`e.print_info()`と書けば、そのオブジェクトが持つメソッドが呼ばれます
   （ダックタイピング）。
   一方Javaでは`Employee e = new Manager(...)`のように**変数の型（参照型）を必ず宣言**します。
   これは「Javaは静的型付け言語であり、コンパイル時に変数がどのクラスの
   メソッド・フィールドを呼び出せるかをチェックするから」です。
   例えば`e`の型が`Employee`である以上、`e.getManagementAllowance()`のような
   `Manager`だけが持つメソッドは、たとえ実体が`Manager`でも**コンパイルエラーになります**
   （呼び出せるのはあくまで`Employee`型が持つメソッドの範囲内）。

2. **オーバーライドの安全性チェック**
   Pythonには`@Override`に相当する言語標準機能はなく、同名メソッドを定義すれば
   常に上書きが成立します（親に存在しないメソッド名を書いても静かに新規メソッドとして
   扱われるだけ）。
   Javaでは`@Override`を付けることで「これは絶対に親のメソッドを上書きするつもりです」
   という宣言をコンパイラに伝えられ、もし該当する親メソッドがなければ
   **その場でエラーにしてくれる**という安全装置になっています。

## 4. つまずきやすいポイント・よくある間違い

- **フィールドはオーバーライドされない**
  メソッドと違い、フィールド（変数）はポリモーフィズムの対象外です。
  参照型（見た目の型）で決まったフィールドが使われます。ややこしいので今回はメソッドの
  オーバーライドだけに集中してください。

- **`private`フィールドは子クラスから直接触れない（トピック4の復習）**
  上のサンプルで`baseSalary`を`protected`にしたのは、`Manager`側の`printInfo()`内で
  直接使うためです。`private`のままだと、前回学んだ通り`getBaseSalary()`を経由する
  必要があります。どちらの設計もあり得ますが、状況に応じて使い分けます。

- **参照型でできることの範囲を勘違いする**
  `Employee e = new Manager(...)`と書いた場合、`e`経由で呼べるのは
  **`Employee`クラスに定義されているメソッドだけ**です（オーバーライドされていれば
  中身はManager版が動く）。`Manager`独自のメソッド（`getTotalSalary()`など）は
  `e.getTotalSalary()`のようには呼べません（コンパイルエラー）。
  「型」と「実体」を混同しないことが今回の一番の学習ポイントです。

- **オーバーロードとの混同**
  「オーバーライド（override, 上書き）」と「オーバーロード（overload, 多重定義）」は
  名前が似ていますが全く別の概念です。オーバーロードは同じクラス内で
  引数の数や型が違う同名メソッドを複数定義することを指し、今回のトピックとは関係ありません。
  混同しないよう注意してください。
