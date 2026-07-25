# トピック8: ラムダ式とStream API

## 1. これは何か、なぜ必要か

トピック6で学んだインターフェースを実装するとき、「メソッドが1つしかない小さな
インターフェース」を使う場面が非常によくあります（例：`Comparator`で並び替えのルールを
渡す、`Runnable`で実行する処理を渡す、など）。そのたびにクラスを1つ作るのは面倒です。

**ラムダ式**は、「抽象メソッドが1つだけのインターフェース（関数型インターフェースと
呼びます）」を、その場で・名前を付けずに・短く実装するための書き方です。

**Stream API**は、`List`や`Set`などのコレクション（トピック7）に対して、
「絞り込む」「変換する」「集計する」といった操作を、for文を使わず**宣言的に**
書けるようにする仕組みです。ラムダ式と組み合わせて使います。

## 2. Javaでの書き方・構文（最小サンプル）

### ラムダ式

```java
// 関数型インターフェース（抽象メソッドが1つだけ）
public interface Greeter {
    void greet(String name);
}
```

```java
// 匿名クラスで書くと長い
Greeter g1 = new Greeter() {
    @Override
    public void greet(String name) {
        System.out.println("こんにちは、" + name);
    }
};

// ラムダ式で書くと短い（(引数) -> 処理 という形）
Greeter g2 = name -> System.out.println("こんにちは、" + name);

g2.greet("佐藤"); // こんにちは、佐藤
```

`Comparator`（並び替えルールを表す標準の関数型インターフェース）でもよく使います。

```java
List<String> names = new ArrayList<>(List.of("佐藤", "鈴木", "田中一郎"));
names.sort((a, b) -> a.length() - b.length()); // 文字数が短い順に並び替え
```

### Stream API

```java
import java.util.List;
import java.util.stream.Collectors;

List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

List<Integer> evenSquares = numbers.stream()   // ①streamに変換
    .filter(n -> n % 2 == 0)                   // ②偶数だけ残す
    .map(n -> n * n)                           // ③2乗に変換
    .collect(Collectors.toList());             // ④リストに戻す

System.out.println(evenSquares); // [4, 16, 36]
```

ポイント：
- `.stream()` → コレクションをStream（処理の流れ）に変換
- `.filter(条件)` → 条件に合うものだけ残す
- `.map(変換)` → 各要素を変換する
- `.collect(Collectors.toList())` → 最後にまたリストなどの形に戻す（これを忘れると
  結果を取り出せない）

## 3. Pythonとの対比

ラムダ式は、Pythonの`lambda`とほぼ同じ発想です。

```python
greet = lambda name: print(f"こんにちは、{name}")
```

```java
Greeter g = name -> System.out.println("こんにちは、" + name);
```

Stream APIは、Pythonのリスト内包表記や`map`/`filter`に相当します。

```python
numbers = [1, 2, 3, 4, 5, 6]
even_squares = [n * n for n in numbers if n % 2 == 0]
# または
even_squares = list(map(lambda n: n * n, filter(lambda n: n % 2 == 0, numbers)))
```

| 項目 | Python | Java |
|---|---|---|
| 無名関数 | `lambda x: x * x` | `x -> x * x` |
| 絞り込み | リスト内包表記の`if`、`filter()` | `.filter(...)` |
| 変換 | リスト内包表記の式部分、`map()` | `.map(...)` |
| 結果をリストに戻す | 自動（内包表記はリストを返す） | `.collect(Collectors.toList())`が必要 |

なぜJavaでは`.collect(...)`という一手間が必要かというと、Streamは「処理の流れ」を
表すだけの一時的なものであり、`List`そのものではないからです。Pythonの内包表記は
書いた瞬間にリストが出来上がりますが、Javaでは「流れを組み立てる（filter, map）」と
「最終的な形に固める（collect）」が明確に分かれています。

## 4. つまずきやすいポイント・よくある間違い

- **どんなインターフェースでもラムダにできるわけではない**：抽象メソッドが**1つだけ**の
  インターフェース（関数型インターフェース、`@FunctionalInterface`が付いていることが多い）
  にしか使えません
- **Streamは一度しか使えない**：`.collect()`などで一度処理を終えたStreamを、
  もう一度使おうとするとエラーになります。必要なら`.stream()`からやり直します
- **`.collect(Collectors.toList())`を忘れる**：filterやmapだけ書いて満足してしまい、
  最終的な`List`として受け取るのを忘れがちです
- **複数行の処理を書くときは`{}`と`return`が必要**：
  `n -> { int squared = n * n; return squared; }`のように、1行で済まない場合は
  波括弧とreturn文が必要です（1行なら省略可）

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
