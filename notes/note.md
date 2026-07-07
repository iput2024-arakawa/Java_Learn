# トピック3: カプセル化（private / public、getter・setter、なぜ隠すのか）

## 1. これは何か、なぜ必要か

「カプセル化」とは、クラスの内部データ（フィールド）を外部から直接触れないように隠し、
決められた方法（メソッド）を通してのみアクセスさせるという考え方です。

トピック2までのコードでは、`name`や`score`といったフィールドに`public`も`private`も
付けていなかったため、実は外部から自由に書き換えられてしまいます。

```java
Student student1 = new Student("山田", 75);
student1.score = -9999; // 本来ありえない値を外から自由に書き換えられてしまう
```

これでは「点数は0〜100の範囲であるべき」といったルールをクラス自身が守れません。
カプセル化は、こうした**不正な状態を防ぎ、データの安全性を保つ**ために必要です。
「オブジェクトが自分のデータを自分で管理する」という、オブジェクト指向の中心的な考え方です。

## 2. Javaでの書き方・構文（最小サンプル）

```java
public class Student {
    private String name;  // privateにして外部から直接触れないようにする
    private int score;

    public Student(String name, int score) {
        this.name = name;
        setScore(score); // コンストラクタからもチェック付きのsetterを使うと安全
    }

    // getter：privateフィールドの値を外部に読み取らせるためのメソッド
    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    // setter：privateフィールドに値を書き込ませるためのメソッド（チェックを入れられる）
    public void setScore(int score) {
        if (score < 0 || score > 100) {
            System.out.println("不正な点数です: " + score);
            return;
        }
        this.score = score;
    }
}
```

```java
Student student1 = new Student("山田", 75);
// student1.score = -9999;      // コンパイルエラー：privateなので外部から直接触れない
student1.setScore(-9999);        // 「不正な点数です」と表示され、書き換わらない
System.out.println(student1.getScore()); // getterを通して安全に読み取る
```

ポイント：
- `private` … そのクラスの中からしかアクセスできないという意味のアクセス修飾子
- `public` … どこからでもアクセスできるという意味のアクセス修飾子
- `getXxx()` … フィールドの値を「取得」するためのメソッド（getter）
- `setXxx()` … フィールドの値を「設定」するためのメソッド（setter）。値のチェック処理を
  ここに書けるのが最大のメリット
- フィールドは`private`にし、必要な分だけ`public`なgetter/setterを用意する、というのが
  Javaの基本パターン

## 3. Pythonとの対比

Pythonにも「アクセスを制限する」という発想はありますが、Javaほど厳密ではありません。

```python
# Python：慣習として「_」を先頭に付けるが、これは「触らないでね」という
# 開発者同士の申し合わせに過ぎず、実際には外部から普通にアクセスできてしまう
class Student:
    def __init__(self, name, score):
        self._name = name
        self._score = score

student1 = Student("山田", 75)
student1._score = -9999  # 一応アクセスできてしまう（Pythonは強制しない）
```

```java
// Java：privateはコンパイラが強制する。外部からのアクセスはコンパイルエラーになる
public class Student {
    private int score;
    // ...
}

student1.score = -9999; // コンパイルエラー（Pythonのように動いてしまうことがない）
```

| 項目 | Python | Java |
|---|---|---|
| アクセス制限の考え方 | 慣習（`_score`など）による自己申告 | `private`によりコンパイラが強制 |
| 外部からのアクセス | 一応可能（強制力なし） | `private`なら不可能（コンパイルエラー） |
| 値の取得・設定 | 直接`obj.score`でアクセスすることが多い | `getScore()`/`setScore()`を経由するのが基本 |
| プロパティ機能 | `@property`で「見た目は属性、中身はメソッド」にできる | 構文としては無く、getter/setterメソッドを素直に書く |

なぜJavaではここまで厳密にするのかというと、Javaは「大規模で複数人が関わる開発において、
クラスの内部実装を後から自由に変更できるようにしたい」という思想を持つ言語だからです。
フィールドを直接公開してしまうと、他の開発者がそのフィールドに直接依存したコードを
書いてしまい、後から実装を変更しづらくなります。`private`+getter/setterにしておけば、
外部から見えるのは「メソッドの入出力」だけなので、内部の実装は自由に変更できます。

## 4. つまずきやすいポイント・よくある間違い

- **フィールドに`private`を付け忘れる**：これまでの書き方の癖で修飾子なし
  （パッケージプライベート）のままにしてしまいがち。カプセル化をするなら
  基本的にフィールドは`private`にする
- **getter/setterを書いたのに、コンストラクタの中で`this.score = score;`と
  直接代入してしまう**：せっかくsetterにチェック処理を書いても、コンストラクタが
  それを経由せず直接代入していると、生成時だけチェックがすり抜けてしまう。
  コンストラクタの中でも`setScore(score);`のようにsetterを呼ぶと一貫性が保てる
- **すべてのフィールドに機械的にgetter/setterを付けてしまう**：本当に外部から
  書き換えてよいフィールドなのかを考えず、何でも`public`なsetterを用意すると、
  結局`private`にした意味が薄れる。「本当に外部から変更させてよいか」を考えて
  setterの要否を判断する
- **getterの名前規則**：`boolean`型のフィールドは`getXxx()`ではなく`isXxx()`
  という名前にするのがJavaの慣習（例：`isPassed`, `isActive`）

---

ここまでで分からない点はありますか？大丈夫であれば「次へ」または「OK」と
言ってください。演習問題（ステップ3）に進みます。
