# トピック9: Optionalと日付時刻API

## 1. これは何か、なぜ必要か

Javaでは、「値が存在しない」ことを`null`で表すことがよくあります。しかし`null`を
うっかり操作しようとすると`NullPointerException`（通称NPE）という実行時エラーが
発生します。これはJavaの実務でも非常によく遭遇するバグです。

**`Optional<T>`**は、「値があるかもしれないし、ないかもしれない」ということを
**型として明示する**仕組みです。「この戻り値はnullかもしれないので、呼び出す側は
必ずチェックしてください」という約束を、コンパイラに伝えられるようになります。

また、日付や時刻の扱いも実務では頻出です。Javaには古い`Date`/`Calendar`クラスも
ありますが、扱いにくく不具合の元になりやすいため、現在は**`java.time`パッケージ**
（`LocalDate`、`LocalDateTime`など）を使うのが標準です。

## 2. Javaでの書き方・構文（最小サンプル）

### Optional

```java
import java.util.Optional;

public Optional<String> findNameById(int id) {
    if (id == 1) {
        return Optional.of("佐藤");   // 値がある場合
    }
    return Optional.empty();          // 値がない場合（nullの代わり）
}
```

```java
Optional<String> maybeName = findNameById(2);

// 存在チェックしてから取り出す
if (maybeName.isPresent()) {
    System.out.println(maybeName.get());
}

// 値があるときだけ処理する（よく使う書き方）
maybeName.ifPresent(name -> System.out.println("見つかりました: " + name));

// 値がなければデフォルト値を使う
String name = maybeName.orElse("名無しさん");
System.out.println(name); // 名無しさん
```

### 日付時刻API

```java
import java.time.LocalDate;
import java.time.Period;

LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(2000, 1, 1); // 年, 月, 日

Period age = Period.between(birthday, today);
System.out.println(age.getYears() + "歳");

LocalDate nextWeek = today.plusDays(7); // 元のtodayは変更されない
```

## 3. Pythonとの対比

Pythonでは「値がないかもしれない」ことは、多くの場合`None`で表現し、実行時に
`if x is not None:`のようにチェックします。この点はJavaの`null`と似ていますが、
Pythonは`Optional`のような専用の型を言語標準では強制しません
（`typing.Optional[str]`は型ヒントであり、実行時には強制力がありません）。

```python
def find_name_by_id(id):
    if id == 1:
        return "佐藤"
    return None

name = find_name_by_id(2)
if name is not None:
    print(name)
else:
    print("名無しさん")
```

```java
Optional<String> maybeName = findNameById(2);
String name = maybeName.orElse("名無しさん");
System.out.println(name);
```

日付時刻については、Pythonの`datetime`モジュールとほぼ1対1で対応します。

| Python (`datetime`) | Java (`java.time`) |
|---|---|
| `date.today()` | `LocalDate.now()` |
| `date(2000, 1, 1)` | `LocalDate.of(2000, 1, 1)` |
| `datetime.now()` | `LocalDateTime.now()` |
| `timedelta` | `Period`（日単位）、`Duration`（時間単位） |
| f-stringでの整形 | `DateTimeFormatter`でパターン指定 |

## 4. つまずきやすいポイント・よくある間違い

- **`isPresent()`でチェックせずに`get()`を呼ぶ**：値がないOptionalに対して
  `get()`を呼ぶと`NoSuchElementException`が発生します。基本は`orElse`や
  `ifPresent`を使い、`get()`は最終手段と考えましょう
- **Optionalをフィールドやメソッドの引数に多用してしまう**：`Optional`は本来
  「戻り値の型」として使うのが基本的な用途です。フィールドに`Optional`を持たせるのは
  避けたほうがよいとされています（最初のうちは戻り値での利用に慣れれば十分です）
- **`LocalDate`はイミュータブル（不変）**：`today.plusDays(7)`は新しい`LocalDate`を
  **返す**だけで、`today`自体は変わりません。戻り値を受け取り忘れると「変更したはずなのに
  変わっていない」というバグになります
- **`Optional.of(null)`はエラーになる**：`null`かもしれない値を渡すときは
  `Optional.of`ではなく`Optional.ofNullable`を使います

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
