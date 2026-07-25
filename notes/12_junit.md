# トピック12: JUnitによるユニットテスト

## 1. これは何か、なぜ必要か

これまでの演習では、`Main`クラスに`System.out.println`を書いて、目視で「正しそうか」
を確認してきました。しかしこの方法には限界があります。

- クラスを修正するたびに、手動で全部の動作を目視確認するのは時間がかかる
- 「前は動いていたのに、今回の修正で壊れていた（リグレッション）」に気づきにくい
- 何が「正しい」動作かが、コードの中に記録として残らない

**ユニットテスト**は、「このメソッドにこの入力を渡したら、この結果になるはず」という
期待値を**コードとして書いておき、自動的に検証する**仕組みです。**JUnit**はJavaで
最も広く使われるテスティングフレームワークです。

## 2. Javaでの書き方・構文（最小サンプル）

```java
// テスト対象のクラス（トピック4で作ったBankAccountを例にします）
public class BankAccount {
    private int balance;

    public BankAccount(int balance) {
        this.balance = balance;
    }

    public void deposit(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("入金額は正の数にしてください");
        }
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}
```

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void depositIncreasesBalance() {
        BankAccount account = new BankAccount(1000);
        account.deposit(500);
        assertEquals(1500, account.getBalance()); // 期待値, 実際の値 の順
    }

    @Test
    void depositWithNegativeAmountThrowsException() {
        BankAccount account = new BankAccount(1000);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
    }
}
```

ポイント：
- `@Test`アノテーションを付けたメソッドが「1つのテストケース」として実行される
- `assertEquals(期待値, 実際の値)`で「等しいはず」を検証する
- `assertThrows(例外クラス, () -> ...)`で「この処理は例外を投げるはず」を検証する
  （トピック8で学んだラムダ式がここで登場します）
- テストはビルドツール（トピック10）から`mvn test`のように実行するのが一般的

## 3. Pythonとの対比

Pythonでの単体テストは`pytest`（または標準の`unittest`）を使うことが多いです。

```python
def test_deposit_increases_balance():
    account = BankAccount(1000)
    account.deposit(500)
    assert account.balance == 1500

def test_deposit_with_negative_amount_raises_error():
    account = BankAccount(1000)
    with pytest.raises(ValueError):
        account.deposit(-100)
```

| 項目 | Python (pytest) | Java (JUnit) |
|---|---|---|
| テストの目印 | 関数名が`test_`で始まる（命名規則） | `@Test`アノテーション（明示的な印） |
| 検証 | `assert 式` | `assertEquals(期待値, 実際の値)`など専用メソッド |
| 例外の検証 | `with pytest.raises(例外):` | `assertThrows(例外.class, () -> ...)` |
| 実行方法 | `pytest`コマンドが自動的にテストを探す | ビルドツール（`mvn test`）経由、
  またはIDEから実行 |

Pythonの`assert`は言語標準の文をそのまま使いますが、Javaでは`assertEquals`のような
**専用のメソッド**を使います。これは、JUnitが「期待値と実際の値を分かりやすく
表示するため」に独自の仕組みを用意しているためです（失敗時に「期待値: 1500, 実際: 1000」
のように分かりやすく表示されます）。

## 4. つまずきやすいポイント・よくある間違い

- **`assertEquals`の引数の順番を逆にする**：`assertEquals(期待値, 実際の値)`が正しい
  順番です。逆にしてもテスト結果自体は変わりませんが、失敗したときのメッセージが
  分かりにくくなります
- **1つのテストメソッドで色々なことを検証しすぎる**：「入金のテスト」と「出金のテスト」
  は別々の`@Test`メソッドに分けるのが基本です。1つのテストが失敗したときに、
  何が壊れたのか特定しやすくなります
- **`private`なメソッドを直接テストしようとする**：JUnitは基本的に`public`な
  メソッド（外部から呼べる入り口）をテストします。`private`なロジックは、それを
  使う`public`メソッド経由でテストするのが基本的な考え方です
- **テストを書かずに「動きました」と報告してしまう**：目視確認だけでなく、
  今後は変更のたびにテストを実行する習慣をつけていきましょう

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
