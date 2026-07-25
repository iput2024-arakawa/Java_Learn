# トピック15: JDBCによるデータベース接続

## 1. これは何か、なぜ必要か

多くのシステムは、データを永続的に（プログラムを終了しても消えないように）
保存する必要があります。その代表的な方法がデータベース（DB）です。

**JDBC（Java Database Connectivity）**は、JavaからDB（MySQL、PostgreSQLなど）に
接続し、SQL文を実行するための標準API（インターフェース群）です。トピック6で学んだ
「インターフェースを実装したクラスを、DBの種類ごとにベンダーが提供する」という
形で、DBの種類が変わってもJavaコード側の書き方はほぼ同じにできる、という設計に
なっています。

## 2. Javaでの書き方・構文（最小サンプル）

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRepository {

    public void printEmployee(int id) throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/mydb";

        // try-with-resources：{}を抜けるときに自動でクローズされる
        try (Connection conn = DriverManager.getConnection(url, "user", "password");
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT name, base_salary FROM employees WHERE id = ?")) {

            stmt.setInt(1, id); // ?（プレースホルダー）に値をセットする

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    System.out.println(rs.getString("name") + ": " + rs.getInt("base_salary"));
                }
            }
        }
    }
}
```

ポイント：
- `Connection` … DBへの接続そのものを表す
- `PreparedStatement` … 実行するSQL文（`?`はあとから値を埋め込む場所）
- `ResultSet` … クエリ結果の集合。`rs.next()`で1行ずつ進めながら読み取る
- `try-with-resources`（`try (...)`の形）… `{}`を抜けるときに、`Connection`や
  `PreparedStatement`を自動的に閉じてくれる構文

## 3. Pythonとの対比

```python
import psycopg2

conn = psycopg2.connect("dbname=mydb user=user password=password")
try:
    with conn.cursor() as cur:
        cur.execute("SELECT name, base_salary FROM employees WHERE id = %s", (id,))
        for name, base_salary in cur.fetchall():
            print(f"{name}: {base_salary}")
finally:
    conn.close()
```

| 項目 | Python (`psycopg2`など) | Java (JDBC) |
|---|---|---|
| 接続 | `psycopg2.connect(...)` | `DriverManager.getConnection(...)` |
| SQL実行 | `cursor.execute(sql, params)` | `PreparedStatement` + `setInt`/`setString`など |
| 結果の取得 | `cursor.fetchall()`（タプルのリスト） | `ResultSet`を`while (rs.next())`でループ |
| リソース解放 | `with`文（コンテキストマネージャ） | `try-with-resources` |

Pythonの`with`文とJavaの`try-with-resources`は、考え方がとてもよく似ています。
どちらも「ブロックを抜けるときに後片付け（接続のクローズなど）を自動化する」
仕組みです。書き方は違いますが、対応関係として覚えておくと理解しやすいです。

## 4. つまずきやすいポイント・よくある間違い

- **SQLを文字列連結で組み立ててしまう（SQLインジェクションの危険）**：
  `"SELECT * FROM employees WHERE id = " + id`のように値を直接埋め込むと、
  悪意のある入力によってSQL文自体を書き換えられる危険があります。必ず
  `PreparedStatement`の`?`とセッターメソッド（`setInt`など）を使いましょう
- **`Connection`のクローズ忘れ**：クローズし忘れると接続がリークし、いずれDBに
  接続できなくなります。`try-with-resources`を使う習慣をつければ防げます
- **`SQLException`（チェック例外）への対応漏れ**：トピック7で学んだチェック例外の
  代表例です。`throws SQLException`で呼び出し元に伝播させるか、`try`/`catch`で
  自分で処理するかを選ぶ必要があります
- **`ResultSet`のカラム名のタイプミス**：`rs.getString("name")`のカラム名は
  実行時までチェックされないため、スペルミスがあっても実行するまで気づけません

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
