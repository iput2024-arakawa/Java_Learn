# トピック5 演習問題: ポリモーフィズム

## 問題

トピック4で作った`Employee`・`Manager`を発展させます。
`Manager`に`printInfo()`の**オーバーライド**を追加し、さらに新しい子クラス`Intern`（研修生）
を作成したうえで、`Employee`型としてまとめて扱い、ポリモーフィズムを確認してください。

### 要件

1. `Employee`クラス（トピック4のものをそのまま使ってOK）
   - フィールド：`name`（`String`）、`baseSalary`（`int`）、どちらも`private`
   - コンストラクタ、`getName()`、`getBaseSalary()`
   - `printInfo()`：`"名前: ○○, 給与: ○○円"`と出力する

2. `Manager`クラス（`Employee`を`extends`）
   - 追加フィールド：`managementAllowance`（`int`、`private`）
   - コンストラクタで`super(...)`を使い`name`と`baseSalary`を渡す
   - 今回は`printInfo()`を**`@Override`を付けて上書き**する
     - 出力形式：`"名前: ○○, 合計給与: ○○円（役職手当込み）"`
     - 合計給与 = `getBaseSalary()` + `managementAllowance`
   - `getTotalSalary()`（合計給与を返すメソッド）はトピック4のものを流用してよい

3. 新しく`Intern`クラス（`Employee`を`extends`）を作成する
   - 追加フィールド：`mentor`（`String`、指導担当者の名前、`private`）
   - コンストラクタ：`name`、`baseSalary`、`mentor`を受け取り、`super(...)`で親に`name`と
     `baseSalary`を渡す
   - `printInfo()`を`@Override`で上書きする
     - 出力形式：`"名前: ○○, 給与: ○○円（メンター: ○○）"`

4. `Main`クラスで、**`Employee`型の配列**に`Employee`・`Manager`・`Intern`の
   インスタンスを1つずつ格納し、拡張for文（for-each）で1つずつ`printInfo()`を呼び出す

   ```java
   Employee[] staff = {
       new Employee("佐藤", 300000),
       new Manager("鈴木", 350000, 50000),
       new Intern("田中", 150000, "山田")
   };

   for (Employee person : staff) {
       person.printInfo();
   }
   ```

### 期待する動作・出力例

```
名前: 佐藤, 給与: 300000円
名前: 鈴木, 合計給与: 400000円（役職手当込み）
名前: 田中, 給与: 150000円（メンター: 山田）
```

配列の型は`Employee[]`で、中身は3種類の異なるクラスのインスタンスですが、
`person.printInfo()`という**同じ呼び出し方**で、それぞれの実体に応じた結果が出ることを
確認してください。

### ヒント

- `Manager`・`Intern`どちらも、`printInfo()`のシグネチャ（名前・引数・戻り値の型）を
  親クラスと完全に一致させること。`@Override`を付けておけば、シグネチャが違う場合に
  コンパイルエラーで教えてくれる
- `for (Employee person : staff)`の`person`の**型はあくまで`Employee`**。
  中身が`Manager`や`Intern`であっても、`person`経由で呼べるのは`Employee`が持つ
  メソッドの範囲内（今回は`printInfo()`だけを呼ぶので問題にはならない）
- `Manager`の`getTotalSalary()`のような`Manager`独自のメソッドは、`person`（`Employee`型）
  からは呼び出せない（コンパイルエラーになる）。試しに`person.getTotalSalary()`と
  書いてみて、エラーになることを確認してみるのもよい復習になる

---

コードを書けたら、このフォルダ内に`Employee.java`、`Manager.java`、`Intern.java`、
`Main.java`として提出してください。レビューします。
