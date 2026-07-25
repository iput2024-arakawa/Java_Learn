# トピック6 演習問題: 抽象クラスとインターフェース

## 問題

これまでの`Employee`・`Manager`・`Intern`を発展させ、
`Employee`を**抽象クラス**にします。さらに、`Employee`とは継承関係のない
新しいクラス`Robot`を作り、**インターフェース**を使って
「`Employee`系のクラスとも`Robot`とも同じ呼び方で`printInfo()`を呼べる」
状態を作ってください。

### 要件

1. インターフェース`Printable`を作成する
   - メソッド`void printInfo();`を1つだけ持つ（中身は書かない）

2. `Employee`クラスを**抽象クラス**にし、`Printable`を`implements`する
   - フィールド：`name`（`String`）、`baseSalary`（`int`）、どちらも`private`
   - コンストラクタ、`getName()`、`getBaseSalary()`は今まで通り
   - `printInfo()`は**中身を書かず、抽象メソッドのままにする**
     （`public abstract void printInfo();`）
   - `Employee`単体では`new`できないことを確認してみてください
     （試しに`new Employee(...)`と書いてコンパイルエラーになることを見るのも勉強になります）

3. `Manager`クラス（`Employee`を`extends`）
   - 追加フィールド：`managementAllowance`（`int`、`private`）
   - `super(...)`でコンストラクタを呼ぶ
   - `printInfo()`を`@Override`で実装：`"名前: ○○, 合計給与: ○○円（役職手当込み）"`
   - `getTotalSalary()`はトピック5のものを流用してよい

4. `Intern`クラス（`Employee`を`extends`）
   - 追加フィールド：`mentor`（`String`、`private`）
   - `super(...)`でコンストラクタを呼ぶ
   - `printInfo()`を`@Override`で実装：`"名前: ○○, 給与: ○○円（メンター: ○○）"`

5. 新しく`Robot`クラスを作成する（`Employee`とは**継承関係を持たせない**、
   `Printable`だけを`implements`する）
   - フィールド：`model`（`String`、型番）、`private`
   - コンストラクタで`model`を受け取る
   - `printInfo()`を`@Override`で実装：`"型番: ○○（ロボット）"`

6. `Main`クラスで、**`Printable`型の配列**に`Manager`・`Intern`・`Robot`の
   インスタンスを1つずつ格納し、拡張for文で`printInfo()`を呼び出す

   ```java
   Printable[] items = {
       new Manager("鈴木", 350000, 50000),
       new Intern("田中", 150000, "山田"),
       new Robot("R2-D2")
   };

   for (Printable item : items) {
       item.printInfo();
   }
   ```

### 期待する動作・出力例

```
名前: 鈴木, 合計給与: 400000円（役職手当込み）
名前: 田中, 給与: 150000円（メンター: 山田）
型番: R2-D2（ロボット）
```

`Manager`・`Intern`は`Employee`の子孫、`Robot`は`Employee`と全く継承関係が
ありませんが、どちらも`Printable`を実装しているので、**同じ`Printable[]`配列に
まとめて入れられる**ことを確認してください。

### ヒント

- `Employee`が`Printable`を`implements`していて、かつ`printInfo()`を
  抽象メソッドのままにしていれば、「インターフェースの要求（`printInfo()`を持つこと）」
  は`Employee`を継承した`Manager`・`Intern`が実装することで満たされます
- `Robot`は`Employee`を継承しないので、`getName()`や`getBaseSalary()`は使えません。
  `Printable`だけを直接`implements`してください
- `Manager`・`Intern`・`Robot`はそれぞれ全く別の継承関係ですが、
  `Printable`という共通の型でまとめられる点がインターフェースの利点です

---

コードを書けたら、このフォルダ内に`Printable.java`、`Employee.java`、`Manager.java`、
`Intern.java`、`Robot.java`、`Main.java`として提出してください。レビューします。
