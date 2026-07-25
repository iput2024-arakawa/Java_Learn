# トピック7: ジェネリクス、コレクション、例外処理

これまでのトピックはオブジェクト指向の基本（クラス・継承・ポリモーフィズム・抽象クラス／
インターフェース）でした。今回は、それらを実際の開発で活用するために欠かせない
3つの機能をまとめて学びます。分量が多いので、1つずつ順番に理解していきましょう。

---

## Part A. ジェネリクス（Generics）

### 1. これは何か、なぜ必要か

ジェネリクスとは、**クラスやメソッドを「扱うデータ型」を後から指定できるようにする仕組み**です。
`<T>`のような山括弧で「型のプレースホルダー（仮の型）」を宣言し、実際に使うときに
`String`や`Integer`など具体的な型をはめ込みます。

なぜ必要かというと、Javaは静的型付け言語なので、本来は「この箱には`String`しか
入れられない」「あの箱には`Integer`しか入れられない」というクラスを、型ごとに
`StringBox`・`IntegerBox`のように別々に作らなければなりません。
ジェネリクスを使えば、`Box<T>`という**1つの設計図**を作るだけで、
`Box<String>`にも`Box<Integer>`にも使い回せます。さらに、間違った型を
入れようとするとコンパイル時にエラーにしてくれるので、**実行前にバグに気づけます**。

### 2. Javaでの書き方・構文

```java
public class Box<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
```

```java
Box<String> stringBox = new Box<>();
stringBox.setValue("こんにちは");
String s = stringBox.getValue();  // castが不要、型が保証されている

Box<Integer> intBox = new Box<>();
intBox.setValue(100);
// intBox.setValue("文字列");  // コンパイルエラーになる（Integer専用のBoxだから）
```

ポイント：

- `<T>`の`T`は「Type」の頭文字で、慣例的な名前（`E`=Element、`K`=Key、`V`=Value なども
  よく使われる）。何の型が入るかは呼び出す側が決める
- `new Box<>()`のように、右辺の`<>`は中身を省略できる（**ダイヤモンド演算子**と呼ぶ）。
  左辺の`Box<String>`から型が推測されるため
- ジェネリクスは**クラス全体**だけでなく、メソッド単体にも付けられる
  （例：`public <T> void printItem(T item) { ... }`）が、最初はクラスへの適用から
  慣れれば十分

### 3. Pythonとの対比

Pythonのリストや変数は、そもそも型を宣言しなくても何でも入れられます。

```python
box = []
box.append("こんにちは")
box.append(100)  # 型が違っても普通に入ってしまう
```

Pythonにも`typing`モジュールで`List[str]`のような型ヒントを書く方法はありますが、
これは**あくまで人間やツール（IDE、mypyなど）向けの注釈であり、実行時には強制されません**。
一方Javaのジェネリクスは、**コンパイラが実際に型チェックを行う言語機能そのもの**です。
「Javaは静的型付け言語で、コンパイル時に型の整合性を保証したいから」、
ジェネリクスのような仕組みが言語レベルで必要になります。

### 4. つまずきやすいポイント・よくある間違い

- **プリミティブ型（`int`、`double`など）はそのまま`<>`に入れられない**
  `Box<int>`はコンパイルエラーになります。`Box<Integer>`のように
  ラッパークラス（プリミティブ型に対応するクラス版。`int`→`Integer`、`double`→`Double`など）
  を使う必要があります
- **`<T>`の中では`new T()`のようにインスタンス化できない**
  Javaのジェネリクスは「型消去（type erasure）」という仕組みで実現されており、
  実行時には型情報の一部が消えてしまうため、`T`を直接`new`することはできません
  （最初のうちは「そういうものだ」と覚えておけばOKです）
- **`<>`を書き忘れる（raw type）**
  `Box box = new Box();`のように型を指定しないと、警告が出たり
  型の恩恵（コンパイル時チェック）が受けられなくなります。必ず型を指定しましょう

---

## Part B. コレクション（Collections Framework）

### 1. これは何か、なぜ必要か

配列（`int[]`や`String[]`）は、これまで何度か使ってきましたが、
**一度作ったらサイズを変更できない**という制約があります。
「要素を後から追加・削除したい」「重複を許さない集合が欲しい」「キーと値のペアで
管理したい」といったニーズに応えるのが**コレクションフレームワーク**です。

代表的なものは3つあります。

- `List`：順序があり、重複を許す「可変長のリスト」（配列の柔軟版）
- `Set`：重複を許さない「集合」
- `Map`：キーと値のペアで管理する「辞書」

これらはジェネリクスと組み合わせて使うのが基本で、`List<String>`のように
「何を格納するリストなのか」を型で保証しながら使います。

### 2. Javaでの書き方・構文

```java
import java.util.ArrayList;
import java.util.List;

List<String> names = new ArrayList<>();
names.add("佐藤");
names.add("鈴木");
names.remove("佐藤");

for (String name : names) {
    System.out.println(name);
}

System.out.println(names.size());  // 要素数
```

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> salaries = new HashMap<>();
salaries.put("佐藤", 300000);
salaries.put("鈴木", 350000);

int salary = salaries.get("佐藤");  // 300000
System.out.println(salaries.containsKey("田中"));  // false
```

ポイント：

- `List`・`Map`は**インターフェース**（トピック6で学んだばかりです）で、
  実際に`new`するのは`ArrayList`や`HashMap`といった**実装クラス**
- 使うには`import java.util.List;`のように**インポート文が必要**
  （これは次のトピックで詳しく扱います）
- `List<Employee>`のように、**自分で作ったクラスもコレクションに格納**できます

### 3. Pythonとの対比

Pythonの組み込み型とJavaのコレクションは、おおよそ次のように対応します。

| Python | Java |
|---|---|
| `list` | `List`（`ArrayList`など） |
| `set` | `Set`（`HashSet`など） |
| `dict` | `Map`（`HashMap`など） |

書き方の対比：

```python
names = []
names.append("佐藤")
names.append("鈴木")
names.remove("佐藤")
```

```java
List<String> names = new ArrayList<>();
names.add("佐藤");
names.add("鈴木");
names.remove("佐藤");
```

大きな違いは2点です。

1. **型を宣言する必要がある**
   Pythonの`[]`は何でも入る万能リストですが、Javaでは`List<String>`のように
   「中身の型」を必ず宣言します。これもジェネリクスと同じく、静的型付け言語だからです
2. **`import`が必要、かつ「インターフェース／実装クラス」を区別する**
   Pythonの`list`や`dict`は言語に組み込まれていてimport不要ですが、Javaの
   コレクションは`java.util`パッケージに属するクラス・インターフェースなので、
   使う前に`import`文を書く必要があります

### 4. つまずきやすいポイント・よくある間違い

- **`List`や`Map`は直接`new`できない（インターフェースだから）**
  `List<String> list = new List<>();`はコンパイルエラーです。
  `new ArrayList<>()`のように、**具体的な実装クラス**を`new`する必要があります
  （トピック6の「インターフェースは直接インスタンス化できない」と同じ理屈です）
- **`import`忘れ**
  `List`や`ArrayList`、`Map`、`HashMap`はすべて`java.util`パッケージにあるため、
  ファイルの先頭で`import java.util.List;`などを書き忘れるとコンパイルエラーになります
- **プリミティブ型を直接格納できない**
  `List<int>`はエラーになります。ジェネリクスと同じ理由で、`List<Integer>`のように
  ラッパークラスを使う必要があります（Javaは自動的に`int`↔`Integer`を変換してくれる
  「オートボクシング」という機能があるので、`list.add(5);`のようにはそのまま書けます）

---

## Part C. 例外処理（Exception Handling）

### 1. これは何か、なぜ必要か

プログラム実行中に起きる「想定外の事態」（ファイルが見つからない、
0で割り算をした、配列の範囲外にアクセスした、など）を**例外（Exception）**と呼びます。
例外処理は、これらが起きてもプログラムを異常終了させず、
**「エラーが起きたときにどう振る舞うか」を自分で制御する仕組み**です。

Pythonでも`try`/`except`で似たことをしてきたと思いますが、Javaには
**「チェック例外（checked exception）」**という、Python にはない仕組みがあり、
「対処が必須なエラー」をコンパイラが強制的にチェックしてくれます。

### 2. Javaでの書き方・構文

```java
try {
    int[] numbers = {1, 2, 3};
    System.out.println(numbers[5]);  // 範囲外アクセス
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("配列の範囲外にアクセスしました: " + e.getMessage());
} finally {
    System.out.println("この処理は例外が起きても起きなくても必ず実行される");
}
```

- `try`：例外が起きるかもしれない処理を書く
- `catch`：指定した種類の例外が発生したときの処理を書く
- `finally`：例外の有無にかかわらず必ず実行したい後処理を書く（省略可）

自分で例外を発生させたいときは`throw`を使います。

```java
public void setBaseSalary(int baseSalary) {
    if (baseSalary < 0) {
        throw new IllegalArgumentException("給与は0以上である必要があります");
    }
    this.baseSalary = baseSalary;
}
```

呼び出し側で受け止めるには：

```java
try {
    employee.setBaseSalary(-100);
} catch (IllegalArgumentException e) {
    System.out.println("エラー: " + e.getMessage());
}
```

### 3. Pythonとの対比

```python
try:
    numbers = [1, 2, 3]
    print(numbers[5])
except IndexError as e:
    print(f"範囲外にアクセスしました: {e}")
finally:
    print("必ず実行される")
```

構文自体はJavaの`try`/`catch`/`finally`とほぼ1対1で対応しており、
Pythonの経験がそのまま活きます。大きく異なるのは次の点です。

**チェック例外（checked exception）の存在**
Javaの例外には大きく分けて2種類あります。

- **チェック例外**（例：`IOException`）：`catch`するか、メソッドの宣言に
  `throws IOException`と書いて「このメソッドはこの例外を投げる可能性がある」と
  明示しないと、**コンパイルエラーになる**
- **非チェック例外**（例：`RuntimeException`とその子孫、`IllegalArgumentException`や
  `NullPointerException`など）：`catch`しなくてもコンパイルは通る

Pythonにはこの区別がなく、**どんな例外も`except`で捕まえなくてもプログラムは
コンパイル（というよりインタプリタなのでその場での構文チェック）が通ります**。
実行時に初めて「捕まえ忘れていた」と気づくPythonに対し、Javaは
「チェック例外については、対処を忘れているとその場（コンパイル時）で教えてくれる」
という安全装置を持っている、と理解してください。

### 4. つまずきやすいポイント・よくある間違い

- **`catch (Exception e)`のように広く捕まえすぎる**
  なんでも`Exception`でまとめて捕まえると、本来気づくべき別のバグまで
  握りつぶしてしまうことがあります。可能な限り**具体的な例外クラス**
  （`IllegalArgumentException`など）を指定しましょう
- **チェック例外の`throws`宣言忘れ**
  チェック例外を投げる可能性のあるメソッドを呼ぶ場合、
  `try`/`catch`で自分が処理するか、呼び出し元のメソッドにも
  `throws`を伝播させるか、どちらかをしないとコンパイルエラーになります
- **`finally`の使いどころを誤解する**
  「`return`した後は`finally`は実行されない」と誤解しがちですが、
  Javaでは**`try`や`catch`の中で`return`しても`finally`は必ず実行されます**
  （ファイルを閉じる、接続を切るといった後処理に向いています）
- **例外とエラーメッセージの`getMessage()`を忘れる**
  `catch (Exception e)`で受け取った`e`が持つ`e.getMessage()`を使うと、
  何が原因だったかを具体的に表示できます。ただ`catch`するだけでなく、
  中身を活用しましょう
