# トピック6: 抽象クラスとインターフェース

## 1. これは何か、なぜ必要か

トピック5で学んだポリモーフィズムでは、「同じ`printInfo()`という呼び方でも、
実体（`Employee`か`Manager`か）によって動作が変わる」ことを確認しました。

今回学ぶ**抽象クラス（abstract class）**と**インターフェース（interface）**は、
どちらも「ポリモーフィズムをさらに強制力のある形で使うための仕組み」です。

具体的には、こんな場面で必要になります。

- 「`Employee`を継承する子クラスは、必ず`printInfo()`を自分なりに実装してほしい。
  親クラスの実装をうっかりそのまま使われては困る」
  → **抽象クラス**で「このメソッドは必ず子クラスで実装しなさい」と強制できる
- 「`Employee`と`Robot`のように、継承関係にない全く別のクラス同士でも、
  `printInfo()`のような共通の呼び方を保証したい」
  → クラスの継承は1つしかできませんが、**インターフェース**は複数の型を
  同時に「実装（implements）」できるので、継承関係を超えた共通ルールを作れます

つまり、抽象クラス・インターフェースは「実装の中身」よりも
**「これを継承・実装するクラスは、必ずこのメソッドを持つこと」という“契約（設計図）”を
決めるための仕組み**だと考えてください。

## 2. Javaでの書き方・構文

### 抽象クラス（abstract class）

```java
public abstract class Employee {
    private String name;
    private int baseSalary;

    public Employee(String name, int baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    public String getName() {
        return name;
    }

    public int getBaseSalary() {
        return baseSalary;
    }

    // 中身を書かない「抽象メソッド」。実装は子クラスに強制する
    public abstract void printInfo();
}
```

```java
public class Manager extends Employee {
    private int managementAllowance;

    public Manager(String name, int baseSalary, int managementAllowance) {
        super(name, baseSalary);
        this.managementAllowance = managementAllowance;
    }

    // abstractメソッドは必ずオーバーライドしないとコンパイルエラーになる
    @Override
    public void printInfo() {
        int total = getBaseSalary() + managementAllowance;
        System.out.println("名前: " + getName() + ", 合計給与: " + total + "円（役職手当込み）");
    }
}
```

ポイント：

- クラス宣言に`abstract`を付けると「抽象クラス」になる
- 抽象クラスは`new Employee(...)`のように**直接インスタンス化できない**
  （「設計図の一部が空欄」の状態なので、そのままでは使えない）
- 中身のない`abstract void printInfo();`のようなメソッドを**抽象メソッド**と呼ぶ
  （`{}`すら書かず、セミコロンで終わる）
- 抽象メソッドを1つでも持つ子クラスは、**必ずそれをオーバーライドしないとコンパイルエラー**
- 抽象クラスでも、普通のフィールドやコンストラクタ、中身のある通常のメソッド
  （`getName()`など）は今まで通り持てる

### インターフェース（interface）

```java
public interface Printable {
    void printInfo();  // publicかつabstractがデフォルト（省略できる）
}
```

```java
public class Employee implements Printable {
    private String name;
    private int baseSalary;

    public Employee(String name, int baseSalary) {
        this.name = name;
        this.baseSalary = baseSalary;
    }

    @Override
    public void printInfo() {
        System.out.println("名前: " + name + ", 給与: " + baseSalary + "円");
    }
}
```

```java
public class Robot implements Printable {
    private String model;

    public Robot(String model) {
        this.model = model;
    }

    @Override
    public void printInfo() {
        System.out.println("型番: " + model + "（ロボット）");
    }
}
```

```java
Printable[] items = {
    new Employee("佐藤", 300000),
    new Robot("R2-D2")
};

for (Printable item : items) {
    item.printInfo();  // EmployeeとRobotは継承関係がなくても、同じように呼べる
}
```

ポイント：

- クラスではなく`interface`というキーワードで宣言する
- インターフェース内のメソッドは、書き方を省略しても
  **自動的に`public abstract`扱い**になる（中身は書けない）
- クラス側は`extends`ではなく**`implements`**を使う
- クラスの継承（`extends`）は1つのクラックしか指定できないが、
  **`implements`は複数のインターフェースをカンマ区切りで同時に実装できる**
  （例：`class Employee implements Printable, Comparable { ... }`）
- `Employee`と`Robot`のように**継承関係が全くない者同士**でも、
  同じインターフェースを実装していれば`Printable`型の配列にまとめて入れられる

## 3. Pythonとの対比

Pythonには「インターフェース」という言語標準のキーワードはありません。
Pythonで近いことをしたい場合は、`abc`モジュール（Abstract Base Class、抽象基底クラスの略）
を使います。

```python
from abc import ABC, abstractmethod

class Employee(ABC):
    def __init__(self, name, base_salary):
        self.name = name
        self.base_salary = base_salary

    @abstractmethod
    def print_info(self):
        pass  # 中身は書かず、子クラスに実装を強制する
```

対比のポイントは3つあります。

1. **「契約を強制する」こと自体はPythonでも一応できる**
   `abstractmethod`を付ければ、Pythonでも「未実装のままインスタンス化しようとするとエラー」
   という挙動をJavaに近い形で再現できます。ただしPythonでは`ABC`を使わなくても
   普通に動いてしまうことが多く、**言語の強制力というよりは「お作法・規約」に近い**扱いです。
   一方Javaでは`abstract`や`interface`は**コンパイラが必ずチェックする言語仕様そのもの**
   なので、実装漏れは実行前（コンパイル時）に確実に検出されます。

2. **「継承関係のない者同士に共通の型を持たせる」ときの考え方の違い**
   Pythonでは、`Employee`にも`Robot`にも`print_info()`さえ定義されていれば、
   継承関係が全くなくても`item.print_info()`と自由に呼べます
   （**ダックタイピング**：「アヒルのように鳴けばアヒルとして扱う」という考え方）。
   Javaは静的型付け言語なので、「型」を保証する仕組みがないと
   `Printable[] items = {...}`のような配列を作ること自体ができません。
   そこで**インターフェース**を使って「`Printable`という型を明示的に約束する」
   ことで、Pythonのダックタイピングに近いことを実現しています。

3. **`interface`というキーワード自体がない**
   Pythonの多重継承（`class Foo(Bar, Baz):`のように複数の親を持てる仕組み）を使えば
   似たようなことは書けますが、Java特有の「実装を持たない純粋な契約だけのinterface」
   という概念そのものはPythonには存在しません。「Javaは実装の継承（`extends`、1つだけ）
   と、型の約束（`implements`、複数OK）を意図的に分けている言語だから」だと理解してください。

## 4. つまずきやすいポイント・よくある間違い

- **抽象クラス／インターフェースは直接`new`できない**
  ```java
  Employee e = new Employee("佐藤", 300000);  // Employeeがabstractならコンパイルエラー
  Printable p = new Printable();              // interfaceは絶対にコンパイルエラー
  ```
  必ず`Manager`や`Employee`（インターフェースを実装した具象クラス）を`new`します。

- **抽象メソッドの実装漏れ**
  抽象クラスを継承した子クラス、あるいはインターフェースを実装したクラスが
  **すべての抽象メソッドを実装していない**と、その時点でコンパイルエラーになります。
  「継承・実装した側は、抽象メソッドをすべて埋める義務がある」と覚えてください。

- **「抽象クラス」と「インターフェース」の使い分けに迷う**
  ざっくりした目安：
  - 子クラス同士で**共通のフィールドや実装（コンストラクタ、共通メソッドなど）を
    たくさん共有したい** → 抽象クラス（`Employee`のように"is-a"の関係が強い場合）
  - 実装は全く共有しなくてよく、**「このメソッドを持つこと」という型の約束だけを
    継承関係のない複数のクラスに課したい** → インターフェース
  - 現場では「インターフェースで型を約束しつつ、共通実装は抽象クラスにまとめる」
    という組み合わせもよく使われます

- **インターフェースのメソッドに`public`や`abstract`を書き忘れて混乱する**
  書かなくても自動的にそう扱われるだけで、書いてもエラーにはなりません。
  最初のうちは省略した書き方（`void printInfo();`）に慣れておけば十分です。

- **抽象クラスにも普通のメソッド（中身あり）を書けることを忘れる**
  「抽象クラス＝すべて空っぽ」と誤解しがちですが、そうではありません。
  `abstract`が付いていないメソッドは、普通に中身を持てて子クラスにそのまま継承されます
  （トピック4で学んだ継承と同じ挙動です）。抽象メソッドは**あくまで一部だけ**でOKです。
