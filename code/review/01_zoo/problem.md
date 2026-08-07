# 復習1: 動物園（抽象クラス・継承・ポリモーフィズム・インターフェース）

## 簡単な用語おさらい

- **カプセル化**：フィールドを`private`にして、外からは`getName()`のような
  getterメソッド経由でしか読めないようにすること
- **継承（`extends`）**：共通の性質を親クラスにまとめ、子クラスで`super(...)`
  を使って親のコンストラクタを呼ぶこと
- **ポリモーフィズム**：親（またはインターフェース）の型の変数に子クラスの
  インスタンスを入れておくと、実際には子クラスで`@Override`したメソッドが
  呼ばれること
- **インターフェース**：継承関係が全くないクラス同士でも、同じインターフェースを
  `implements`していれば「同じ型」として扱えること

## 問題

動物園で飼育している動物たちを表すクラス群を作ります。今回は`Employee`ではなく
**`Animal`（動物）**を中心にした設計にしてください。

### 要件

1. インターフェース`Feedable`を作成する
   - メソッド`void feed();`を1つだけ持つ（中身は書かない）

2. `Animal`クラスを**抽象クラス**にし、`Feedable`を`implements`する
   - フィールド：`name`（`String`）、`age`（`int`）、どちらも`private`
   - コンストラクタで`name`と`age`を受け取る
   - `getName()`、`getAge()`（それぞれ対応するフィールドを返す）
   - `makeSound()`は**中身を書かず、抽象メソッドのままにする**
     （`public abstract void makeSound();`）
   - `Animal`単体では`new`できないことを確認してみてください

3. `Dog`クラス（`Animal`を`extends`）
   - 追加フィールド：`breed`（`String`、犬種、`private`）
   - `super(...)`でコンストラクタを呼ぶ
   - `makeSound()`を`@Override`：`"○○（犬種：△△）: ワンワン！"`
   - `feed()`を`@Override`：`"○○にドッグフードをあげた"`

4. `Bird`クラス（`Animal`を`extends`）
   - 追加フィールド：`canFly`（`boolean`、飛べるかどうか、`private`）
   - `super(...)`でコンストラクタを呼ぶ
   - `makeSound()`を`@Override`：`"○○: チュンチュン！"`
   - `feed()`を`@Override`：`"○○に木の実をあげた"`

5. 新しく`RobotPet`クラスを作成する（`Animal`とは**継承関係を持たせない**、
   `Feedable`だけを`implements`する）
   - フィールド：`model`（`String`、型番、`private`）
   - コンストラクタで`model`を受け取る
   - `feed()`を`@Override`：`"型番○○に電池を補給した"`

6. `Main`クラスで、以下の2つを行う
   - `Animal`型の配列に`Dog`・`Bird`のインスタンスを格納し、拡張for文で
     `makeSound()`を呼び出す（ポリモーフィズムの確認）
   - **`Feedable`型の配列**に`Dog`・`Bird`・`RobotPet`のインスタンスを
     1つずつ格納し、拡張for文で`feed()`を呼び出す（インターフェースの確認）

   ```java
   Animal[] animals = {
       new Dog("ポチ", 3, "柴犬"),
       new Bird("ピーちゃん", 1, true)
   };
   for (Animal a : animals) {
       a.makeSound();
   }

   Feedable[] items = {
       new Dog("ポチ", 3, "柴犬"),
       new Bird("ピーちゃん", 1, true),
       new RobotPet("AIBO-X1")
   };
   for (Feedable item : items) {
       item.feed();
   }
   ```

### 期待する動作・出力例

```
ポチ（犬種：柴犬）: ワンワン！
ピーちゃん: チュンチュン！
ポチにドッグフードをあげた
ピーちゃんに木の実をあげた
型番AIBO-X1に電池を補給した
```

`Dog`・`Bird`は`Animal`の子孫、`RobotPet`は`Animal`と全く継承関係がありませんが、
どちらも`Feedable`を実装しているので、**同じ`Feedable[]`配列にまとめて
入れられる**ことを確認してください。

### ヒント

- `Animal`が`Feedable`を`implements`していても、`feed()`を実装しているのは
  あくまで`Dog`・`Bird`（子クラス）です。`Animal`自身は`feed()`の中身を
  持っていなくても構いません
- `RobotPet`は`Animal`を継承しないので、`getName()`や`getAge()`は使えません。
  `Feedable`だけを直接`implements`してください
- `Dog`・`Bird`・`RobotPet`はそれぞれ全く別の継承関係ですが、`Feedable`という
  共通の型でまとめられる点がインターフェースの利点です

---

コードを書けたら、このフォルダ内に`Feedable.java`、`Animal.java`、`Dog.java`、
`Bird.java`、`RobotPet.java`、`Main.java`として提出してください。レビューします。
