# トピック18: レイヤードアーキテクチャ

## 1. これは何か、なぜ必要か

トピック17までで、`Controller`（HTTPの受け口）、`Service`（業務ロジック）、
`Repository`（DBアクセス）というクラスがそれぞれ登場しました。実はこれは
偶然ではなく、**レイヤードアーキテクチャ（層状アーキテクチャ）**という、
Javaのシステム開発で広く使われる設計方針に沿ったものです。

すべての処理を1つの巨大なクラス（あるいは`Main`クラス）に詰め込んでしまうと、
次のような問題が起きます。

- 「HTTPの処理」と「業務ロジック」と「DB操作」が混ざり、どこに何が書いてあるか
  分からなくなる
- DBを別のものに変えたいとき、業務ロジックまで書き直す羽目になる
- テスト（トピック12）が書きにくい（DBに接続しないと業務ロジックのテストができない、など）

**責務（役割）ごとにクラスを分離する**ことで、これらの問題を避けます。

## 2. Javaでの書き方・構文（最小サンプル）

代表的な3層構成：

```java
// ① Controller層：HTTPリクエストの受け口。入出力の形式に専念する
@RestController
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return service.findById(id); // 業務ロジックはServiceに任せる
    }
}
```

```java
// ② Service層：業務ロジック（「本当は何をすべきか」）を担当する
@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Employee findById(int id) {
        Employee employee = repository.findById(id);
        if (employee == null) {
            throw new IllegalArgumentException("従業員が見つかりません: " + id);
        }
        return employee;
    }
}
```

```java
// ③ Repository層：DBアクセスに専念する（トピック15のJDBCなどを使う）
@Repository
public class EmployeeRepository {
    public Employee findById(int id) {
        // 実際にはJDBCやJPAでDBに問い合わせる
        return new Employee(id, "佐藤", 300000);
    }
}
```

各層は「1つ下の層だけを呼ぶ」というルールを守ります。`Controller`が直接DBに
アクセスしたり、`Repository`がHTTPのことを知っていたりしてはいけません。

## 3. Pythonとの対比

Pythonでも、Djangoの`views.py`（Controller相当）／`models.py`（Repository相当）
のような分離や、Flask/FastAPIで`routes/`・`services/`・`repositories/`のように
フォルダを分ける設計は同じ発想で行われます。

| 層 | 役割 | Java (Spring) | Python (例: Django) |
|---|---|---|---|
| Controller | HTTPの入出力を扱う | `@RestController` | `views.py` |
| Service | 業務ロジック | `@Service` | 専用の`services.py`（プロジェクトによる） |
| Repository | DBアクセス | `@Repository` | `models.py`（ORM経由） |

考え方自体はPythonでも共通ですが、Javaでは`@Controller`/`@Service`/`@Repository`
という**アノテーションでその役割を明示する文化**が強く根付いている点が特徴です。
これにより、クラス名やアノテーションを見ただけで「このクラスは何をすべきで、
何をすべきでないか」がチーム全体で共有しやすくなります。

## 4. つまずきやすいポイント・よくある間違い

- **Controllerに業務ロジックを書いてしまう**：「if文で条件判定して計算する」
  といったロジックがControllerに増えてくると、テストしにくく、再利用もしにくく
  なります。業務ロジックはServiceに寄せましょう
- **Serviceが肥大化する（Fat Service）**：何でもかんでも1つの`Service`クラスに
  詰め込んでしまう状態です。トピック4〜6で学んだ継承・インターフェースを使って、
  役割ごとにさらに分割することも検討します
- **層を飛び越えて直接呼び出してしまう**：Controllerが直接Repositoryを呼ぶ
  （Serviceを飛ばす）と、業務ルールがどこにも書かれないまま処理が進んでしまう
  ことがあります
- **「レイヤーを分けること」自体が目的になってしまう**：小さいプログラムでは
  無理に3層に分ける必要はありません。トピック19で学ぶ内容とも関連しますが、
  「なぜ分けるのか」（変更のしやすさ、テストのしやすさ）を意識することが大切です

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
