# トピック17: REST APIの設計と実装（Spring Boot）

## 1. これは何か、なぜ必要か

Webシステムの多くは、ブラウザやスマホアプリなどの「クライアント」と、データを
処理する「サーバー」が、HTTP通信を通じてやり取りする構成になっています。

**REST（Representational State Transfer）**は、この通信の設計方針の1つで、
「操作対象（リソース）をURLで表し、操作の種類をHTTPメソッド（GET/POST/PUT/DELETE
など）で表す」という考え方です。トピック16で学んだSpring（DI）の上に、
この考え方を実装するための機能が用意されています。

## 2. Javaでの書き方・構文（最小サンプル）

```java
import org.springframework.web.bind.annotation.*;

@RestController // 「このクラスはHTTPリクエストを受け付ける入り口です」という印
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { // トピック16のDI
        this.service = service;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return service.findById(id); // 戻り値は自動的にJSONに変換される（トピック14）
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return service.save(employee); // リクエストボディのJSONが自動的にEmployeeに変換される
    }
}
```

- `GET /employees/1` … idが1の従業員情報を取得する
- `POST /employees` （リクエストボディにJSON） … 新しい従業員を作成する

ポイント：
- `@GetMapping`/`@PostMapping` … HTTPメソッドとURLパターンの対応を宣言する
- `@PathVariable` … URLの一部（`{id}`の部分）を引数として受け取る
- `@RequestBody` … リクエストのJSONボディを、トピック14のJacksonの仕組みで
  自動的にJavaのオブジェクトへ変換する
- 戻り値のオブジェクトも、自動的にJSONへ変換されてクライアントに返される

## 3. Pythonとの対比

Flaskで似た機能を書くと、次のようになります。

```python
from flask import Flask, jsonify, request

app = Flask(__name__)

@app.route("/employees/<int:id>", methods=["GET"])
def get_employee(id):
    employee = service.find_by_id(id)
    return jsonify(employee)

@app.route("/employees", methods=["POST"])
def create_employee():
    data = request.get_json()
    employee = service.save(data)
    return jsonify(employee)
```

| 項目 | Python (Flask) | Java (Spring) |
|---|---|---|
| ルーティングの宣言 | `@app.route("/employees/<int:id>")` | `@GetMapping("/{id}")` |
| URLパラメータの受け取り | 関数の引数（`def get_employee(id):`） | `@PathVariable` |
| リクエストボディの取得 | `request.get_json()`（`dict`が返る） | `@RequestBody`で指定したクラスの
  インスタンスが返る（型が保証される） |
| レスポンスのJSON変換 | `jsonify(...)`を明示的に呼ぶ | 戻り値をそのまま返せば自動変換される |

FastAPI（Pythonの型ヒントを活用するフレームワーク）は、実はSpringの考え方に近く、
リクエストボディを`Pydantic`モデルという専用クラスとして受け取り、自動で型変換・
バリデーションを行います。もしFastAPIを触ったことがあれば、`@RequestBody`と
`Pydantic`モデルの関係は、`Employee`クラスとの対応がよく似ていると感じられるはずです。

## 4. つまずきやすいポイント・よくある間違い

- **HTTPメソッドとアノテーションの対応を間違える**：データを取得するだけなのに
  `@PostMapping`を使ってしまう、など。「取得はGET」「作成はPOST」という
  RESTの基本方針を意識しましょう
- **`@RequestBody`で受け取るクラスにgetter/setter・引数なしコンストラクタがない**：
  トピック14のJSON変換の仕組みと同じ理由で、これらがないと正しく変換できません
- **URLの設計に一貫性がない**：`/employees`（複数形・リソース名）のような
  命名規則を意識しないと、後から見て分かりにくいAPIになります
- **エラー時のレスポンス（ステータスコード）を意識しない**：正常時は`200`、
  作成成功時は`201`、見つからない場合は`404`など、HTTPステータスコードで
  結果の意味を伝える文化があります。最初は「うまくいったかどうか」だけでなく
  「どういう意味の結果か」を意識すると設計が良くなります

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
