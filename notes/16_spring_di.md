# トピック16: Spring Boot基礎とDI（依存性注入）

## 1. これは何か、なぜ必要か

これまでのクラス設計では、あるクラスが別のクラスを使いたいとき、自分で`new`して
作っていました（例：`Main`クラスの中で`new BankAccount(...)`）。しかし実際の
システムでは、クラス同士の依存関係が複雑になり、「誰が・いつ・どの順番でオブジェクト
を作るか」の管理が大変になります。

**DI（Dependency Injection、依存性注入）**とは、「あるクラスが必要とする別の
クラスのインスタンスを、自分で`new`するのではなく、外部から渡してもらう」という
設計の考え方です。**Spring**（および**Spring Boot**）は、このDIの仕組みを中心に
据えた、Javaで最も広く使われるWebアプリケーションフレームワークです。

Spring Bootは、Springの複雑な初期設定を「まずはこれで動く」という形に簡略化した
ものです。今後のWebアプリ・API開発（トピック17）の土台になります。

## 2. Javaでの書き方・構文（最小サンプル）

```java
import org.springframework.stereotype.Repository;

@Repository // 「このクラスはDBアクセスを担当する部品です」という印
public class EmployeeRepository {
    public String findNameById(int id) {
        return "佐藤"; // 本来はJDBC（トピック15）などでDBから取得する
    }
}
```

```java
import org.springframework.stereotype.Service;

@Service // 「このクラスは業務ロジックを担当する部品です」という印
public class EmployeeService {
    private final EmployeeRepository repository;

    // コンストラクタでEmployeeRepositoryを受け取る（自分でnewしない）
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public String getGreeting(int id) {
        String name = repository.findNameById(id);
        return "こんにちは、" + name + "さん";
    }
}
```

`EmployeeService`は`EmployeeRepository`を必要としていますが、`new EmployeeRepository()`
とはどこにも書いていません。Springが起動時に自動的に`EmployeeRepository`の
インスタンスを作り、`EmployeeService`のコンストラクタに**注入（渡す）**してくれます。
これが「依存性注入」です。

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

## 3. Pythonとの対比

Pythonの軽量なWebフレームワーク（FlaskやFastAPIなど）では、多くの場合クラスや
関数を**明示的にimportして直接使う**シンプルな作りになっています。

```python
from employee_repository import EmployeeRepository

class EmployeeService:
    def __init__(self, repository: EmployeeRepository):
        self.repository = repository  # 呼び出し側が明示的に渡す

repository = EmployeeRepository()
service = EmployeeService(repository)  # 自分でインスタンス化して渡している
```

一見似ていますが、Springでは`@Service`や`@Repository`のような**アノテーション**を
付けておくだけで、「どのクラスをどこに注入するか」をフレームワーク側が実行時に
自動的に解決してくれます。Pythonの`@app.route("/path")`（Flask）のように
「アノテーション（デコレータ）でフレームワークに意味を伝える」という発想自体は
共通ですが、Springは**オブジェクトの生成・組み立てそのもの**まで自動化する点が
大きく異なります。

| 項目 | Python (Flask的な素朴な構成) | Java (Spring) |
|---|---|---|
| オブジェクトの生成 | 開発者が明示的に`new`（コンストラクタ呼び出し） | フレームワークが自動生成・注入 |
| 部品の登録 | 特になし（importして使うだけ） | `@Component`/`@Service`/`@Repository`などのアノテーション |
| 起動の仕組み | `python app.py` | `@SpringBootApplication`のクラスを実行 |

## 4. つまずきやすいポイント・よくある間違い

- **アノテーションの付け忘れ**：`@Service`や`@Repository`を付け忘れると、
  Springがそのクラスを「部品」として認識せず、注入できずにエラーになります
- **フィールドインジェクションを多用してしまう**：`@Autowired`をフィールドに
  直接付ける書き方（`@Autowired private EmployeeRepository repository;`）も
  存在しますが、テストのしやすさなどの理由から、上記のサンプルのような
  **コンストラクタでの注入**が推奨されています
- **循環依存**：AがBを必要とし、BもAを必要とする、という関係を作ってしまうと、
  Springがどちらを先に作ればよいか分からずエラーになります。設計を見直す
  サインだと考えましょう
- **「魔法」に見えて理解を諦めてしまう**：DIは最初、「勝手にインスタンスが
  用意されている」ように見えて戸惑いますが、やっていることは「誰かが代わりに
  `new`してコンストラクタに渡してくれている」だけです。トピック2・4で学んだ
  コンストラクタの知識がそのまま土台になっています

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
