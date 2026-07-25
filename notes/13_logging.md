# トピック13: ロギング（println からの卒業）

## 1. これは何か、なぜ必要か

これまでの演習では、動作確認やデバッグのために`System.out.println`を多用してきました。
しかし実務のシステムでは、これにはいくつかの問題があります。

- 本番環境で不要になったとき、`println`を1つずつ探して消す（または残ったままになる）
- 「重大なエラー」と「デバッグ用の一時的な出力」の区別がつかない
- 出力先（画面、ファイル、外部の監視サービスなど）を切り替えられない

**ロギング（logging）**は、これらを解決するために、「ログレベル（重要度）」を
指定しながら記録を残す仕組みです。Javaの実務では**SLF4J**というロギングの
「窓口（API）」と、**Logback**のような実際に出力を行う「実装」を組み合わせて
使うのが一般的です。

## 2. Javaでの書き方・構文（最小サンプル）

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankAccount {
    // クラスごとにLoggerを1つ用意するのが基本パターン
    private static final Logger logger = LoggerFactory.getLogger(BankAccount.class);

    private int balance;

    public void deposit(int amount) {
        if (amount <= 0) {
            logger.warn("不正な入金額が指定されました: {}", amount);
            throw new IllegalArgumentException("入金額は正の数にしてください");
        }
        balance += amount;
        logger.info("入金処理が完了しました。新しい残高: {}", balance);
    }
}
```

主なログレベル（重要度が低い順）：
- `debug` … 開発時にだけ詳しく見たい情報
- `info` … 通常の処理の記録（「入金処理が完了した」など）
- `warn` … 異常ではないが注意が必要な状況
- `error` … エラーが発生したことの記録

```java
try {
    // 何らかの処理
} catch (Exception e) {
    logger.error("処理中にエラーが発生しました", e); // 例外オブジェクトも渡せる
}
```

## 3. Pythonとの対比

Pythonにも標準ライブラリの`logging`モジュールがあり、考え方はほぼ同じです。

```python
import logging

logger = logging.getLogger(__name__)

def deposit(self, amount):
    if amount <= 0:
        logger.warning("不正な入金額が指定されました: %s", amount)
        raise ValueError("入金額は正の数にしてください")
    self.balance += amount
    logger.info("入金処理が完了しました。新しい残高: %s", self.balance)
```

| 項目 | Python (`logging`) | Java (SLF4J) |
|---|---|---|
| Loggerの取得 | `logging.getLogger(__name__)` | `LoggerFactory.getLogger(クラス名.class)` |
| レベル | debug / info / warning / error / critical | debug / info / warn / error |
| プレースホルダー | `%s`（または f-string） | `{}` |

大きな違いはなく、「文字列を`+`で連結するのではなく、プレースホルダーに値を渡す」
という書き方も共通です。これは、ログを出力しない設定（例えばdebugレベルを無効化）
のときに、無駄な文字列連結の処理自体を省略できるという性能上の理由があります。

## 4. つまずきやすいポイント・よくある間違い

- **文字列連結でメッセージを組み立ててしまう**：`logger.info("残高: " + balance)`
  ではなく、`logger.info("残高: {}", balance)`のようにプレースホルダーを使うのが
  推奨されます
- **`println`と混在させてしまう**：一度ロギングを導入したら、デバッグ出力も含めて
  `logger.debug(...)`に統一しましょう
- **ログレベルの使い分けを誤る**：何でも`error`にしてしまうと、本当に重大な問題を
  見逃しやすくなります。「異常だが処理は継続できる」なら`warn`、「想定内の記録」なら
  `info`、というように使い分けます
- **設定ファイル（Logbackの設定など）が必要なことを忘れる**：SLF4Jは「窓口」でしか
  なく、実際にどこにどう出力するかはLogbackなどの設定ファイルで決まります。
  設定がないとログが正しく出力されないことがあります

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
