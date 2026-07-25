# トピック14: JSON連携（Jackson）

## 1. これは何か、なぜ必要か

Web APIとのやり取りや設定ファイルなど、実務では**JSON**形式でデータを扱う場面が
非常に多くあります。Pythonでは`json`モジュールが標準で使えましたが、Javaには
JSONを扱う標準ライブラリが含まれていません（JDK標準にはない）。そのため、
**Jackson**（または**Gson**）という外部ライブラリを使うのが一般的です
（トピック10で学んだビルドツールで依存関係として追加します）。

JSONとJavaのオブジェクト（クラスのインスタンス）を相互に変換することを、
それぞれ**シリアライズ（オブジェクト→JSON）**、**デシリアライズ（JSON→オブジェクト）**
と呼びます。

## 2. Javaでの書き方・構文（最小サンプル）

```java
public class Employee {
    private String name;
    private int baseSalary;

    // Jacksonはgetter/setterを見てフィールドを判断する
    public Employee() {} // デシリアライズのために引数なしコンストラクタが必要

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getBaseSalary() { return baseSalary; }
    public void setBaseSalary(int baseSalary) { this.baseSalary = baseSalary; }
}
```

```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();

// オブジェクト → JSON文字列（シリアライズ）
Employee employee = new Employee();
employee.setName("佐藤");
employee.setBaseSalary(300000);
String json = mapper.writeValueAsString(employee);
System.out.println(json); // {"name":"佐藤","baseSalary":300000}

// JSON文字列 → オブジェクト（デシリアライズ）
Employee restored = mapper.readValue(json, Employee.class);
System.out.println(restored.getName()); // 佐藤
```

## 3. Pythonとの対比

```python
import json

employee = {"name": "佐藤", "base_salary": 300000}
json_str = json.dumps(employee, ensure_ascii=False)

restored = json.loads(json_str)
print(restored["name"])
```

| 項目 | Python (`json`) | Java (Jackson) |
|---|---|---|
| オブジェクト→JSON | `json.dumps(dict)` | `mapper.writeValueAsString(オブジェクト)` |
| JSON→オブジェクト | `json.loads(str)` → `dict` | `mapper.readValue(str, クラス.class)` → 専用クラスのインスタンス |
| 扱うデータの形 | `dict`（何でも入る） | 自分で定義したクラス（トピック2）＝**POJO**
  （Plain Old Java Object、特別な継承などをしていない普通のクラスを指す用語） |

最大の違いは、Pythonの`dict`はキーが何でも自由に入る**動的な入れ物**であるのに
対し、Javaでは「このJSONはこういう構造だ」ということを、あらかじめ**クラスとして
定義しておく**必要がある点です。これもJavaが静的型付け言語だからこその設計です。
JSONのキー名とクラスのフィールド名（getter/setterの名前）を対応させることで、
Jacksonが自動的にマッピングしてくれます。

## 4. つまずきやすいポイント・よくある間違い

- **getter/setterがないとシリアライズ・デシリアライズできない**：トピック3で
  学んだ`private`フィールドだけでは、Jacksonはアクセスできません。基本的には
  getter/setterを用意する必要があります
- **引数なしコンストラクタを忘れる**：JSONからオブジェクトを作る際、Jacksonは
  まず空のインスタンスを作ってからsetterで値を埋めていくため、引数なしの
  コンストラクタが必要になることが多いです
- **JSONのキー名とフィールド名の不一致**：JSON側が`base_salary`（スネークケース）で
  Java側が`baseSalary`（キャメルケース）のように命名規則が違うと、そのままでは
  正しく変換されません（アノテーションで対応関係を指定する方法もありますが、
  最初は名前を揃えるのが簡単です）
- **日付や特殊な型のシリアライズで詰まる**：トピック9で学んだ`LocalDate`などは、
  標準のままだとJacksonがうまく変換できないことがあり、追加の設定が必要になる
  場合があります（発展的な内容なので、つまずいたらその都度調べれば大丈夫です）

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
