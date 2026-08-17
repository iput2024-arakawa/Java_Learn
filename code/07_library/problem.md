# トピック7 演習問題: ジェネリクス・コレクション・例外処理

今回はEmployee系ではなく、**図書館の貸出管理システム**を題材にします。

## 問題

図書館にある本を`Map`で管理し、貸出・返却を行うプログラムを作ってください。
存在しないISBNや、状態がおかしい貸出・返却操作に対しては、**自分で定義した例外**
を投げてエラーを表現します。

### 要件

1. `Book`クラスを作成する
   - フィールド：`title`（`String`）、`isbn`（`String`）、`borrowed`（`boolean`、
     貸出中かどうか）はすべて`private`
   - コンストラクタでは`title`・`isbn`を受け取り、`borrowed`は`false`で初期化する
   - `getTitle()`、`getIsbn()`、`isBorrowed()`（`boolean`のgetterなので`isXxx`）
   - `setBorrowed(boolean borrowed)`

2. **チェック例外**として`BookNotFoundException`を作成する
   - `Exception`を`extends`する（チェック例外にするため）
   - 指定したISBNの本が存在しないときに使う
   - コンストラクタでメッセージ（`String`）を受け取り、`super(message)`で親に渡す

3. **非チェック例外**を2つ作成する（どちらも`RuntimeException`を`extends`）
   - `AlreadyBorrowedException`：すでに貸出中の本をさらに借りようとしたときに使う
   - `NotBorrowedException`：借りられていない本を返却しようとしたときに使う
   - どちらもコンストラクタでメッセージを受け取り、`super(message)`で親に渡す

4. `Library`クラスを作成する
   - フィールド：`private Map<String, Book> books`（キーはISBN）を持つ
     （コンストラクタで`new HashMap<>()`などで初期化）
   - `void addBook(Book book)`：`books`に本を追加する（キーは`book.getIsbn()`）
   - `List<Book> searchByTitle(String keyword)`：タイトルに`keyword`を**部分一致**で
     含む本を全て`List<Book>`にまとめて返す（該当なしなら空の`List`を返す。
     `null`を返さないこと）
   - `void borrowBook(String isbn) throws BookNotFoundException`：
     - 指定ISBNの本が存在しなければ`BookNotFoundException`を`throw`する
       （チェック例外なので、メソッドの宣言に`throws`が必要）
     - 存在するがすでに貸出中なら`AlreadyBorrowedException`を`throw`する
       （非チェック例外なので`throws`宣言は不要）
     - どちらでもなければ`borrowed`を`true`にする
   - `void returnBook(String isbn) throws BookNotFoundException`：
     - 指定ISBNの本が存在しなければ`BookNotFoundException`を`throw`する
     - 存在するが貸出中でなければ`NotBorrowedException`を`throw`する
     - どちらでもなければ`borrowed`を`false`にする

5. 自分で**ジェネリックメソッド**を1つ定義する（クラスはどこでもよいが、
   `Library`に`static`メソッドとして置くのがおすすめ）
   - シグネチャ例：`public static <T> void printAll(List<T> items)`
   - 受け取った`List<T>`の中身を、拡張for文で1件ずつ`System.out.println(item)`する
     （`T`が何の型であっても使えるメソッドにすること）
   - `Book`をそのまま`println`しても分かりやすく表示されるように、
     `Book`クラスに`toString()`を`@Override`しておくとよい
     （例：`"title (isbn) [貸出中/貸出可]"`）

6. `Main`クラスで、次の流れを動かす
   - `Library`に本を3〜4冊`addBook`する（タイトルが似ているものを含めると
     `searchByTitle`の確認がしやすい）
   - `searchByTitle`で部分一致検索し、結果を`printAll`で表示する
   - 存在する本を1冊`borrowBook`する（成功）
   - 同じ本をもう一度`borrowBook`しようとして`AlreadyBorrowedException`を
     `catch`し、エラーメッセージを表示する
   - 存在しないISBNを`borrowBook`しようとして`BookNotFoundException`を
     `try`/`catch`する（チェック例外なので`catch`しないとコンパイルエラーになる
     はずです）
   - 借りた本を`returnBook`する（成功）
   - 借りていない本を`returnBook`しようとして`NotBorrowedException`を`catch`する

### 期待する動作・出力例（一例、文言は多少アレンジしてよい）

```
=== "プログラミング" を含む本を検索 ===
プログラミング入門 (978-0-000001) [貸出可]
実践プログラミング (978-0-000002) [貸出可]

--- 貸出処理 ---
「プログラミング入門」を貸し出しました

--- もう一度同じ本を借りようとする ---
エラー: この本はすでに貸出中です

--- 存在しないISBNを借りようとする ---
エラー: 指定されたISBNの本が見つかりません: 978-9-999999

--- 返却処理 ---
「プログラミング入門」を返却しました

--- 借りていない本を返却しようとする ---
エラー: この本は貸し出されていません
```

### ヒント

- `Map<String, Book>`から値を取り出すときは`books.get(isbn)`。存在しなければ`null`が
  返るので、まず`null`かどうかで「本が存在するか」を判定できます
- `List`・`Map`を使うには`import java.util.List;`、`import java.util.Map;`、
  `import java.util.ArrayList;`、`import java.util.HashMap;`が必要です
- チェック例外（`BookNotFoundException`）を呼び出す側は、`try`/`catch`するか、
  自分のメソッドにも`throws BookNotFoundException`を書いて呼び出し元に伝播させる
  必要があります。`Main`の`main`メソッドで`throws`するのは避け、`try`/`catch`で
  受け止める書き方を練習してみてください
- `AlreadyBorrowedException`・`NotBorrowedException`は非チェック例外なので、
  `catch`しなくてもコンパイルは通りますが、今回はエラーメッセージを表示したいので
  あえて`catch`します

---

コードを書けたら、このフォルダ内に`Book.java`、`BookNotFoundException.java`、
`AlreadyBorrowedException.java`、`NotBorrowedException.java`、`Library.java`、
`Main.java`として提出してください。レビューします。
