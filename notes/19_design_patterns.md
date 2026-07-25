# トピック19: デザインパターン入門

## 1. これは何か、なぜ必要か

**デザインパターン**とは、ソフトウェア設計でよく現れる問題に対する「型にはまった
解決策」のカタログです。トピック5・6で学んだポリモーフィズムやインターフェースを
実際にどう活用するかの、代表的な応用例だと考えてください。

すべてを覚える必要はありませんが、実務のコードでは頻繁に登場するため、
代表的な2つ（**Strategy**と**Factory**）を最初に押さえておきます。

## 2. Javaでの書き方・構文（最小サンプル）

### Strategyパターン：処理の中身を「差し替え可能」にする

「割引の計算方法」を、条件分岐（if文）ではなく、**インターフェースの実装を
差し替えることで**切り替える例です。

```java
public interface DiscountStrategy {
    int applyDiscount(int price);
}

public class NoDiscount implements DiscountStrategy {
    public int applyDiscount(int price) { return price; }
}

public class TenPercentOff implements DiscountStrategy {
    public int applyDiscount(int price) { return (int) (price * 0.9); }
}
```

```java
public class Order {
    private DiscountStrategy discountStrategy;

    public Order(DiscountStrategy discountStrategy) {
        this.discountStrategy = discountStrategy; // 割引ロジックを外から渡す
    }

    public int getFinalPrice(int price) {
        return discountStrategy.applyDiscount(price);
    }
}
```

```java
Order order1 = new Order(new NoDiscount());
Order order2 = new Order(new TenPercentOff());
System.out.println(order1.getFinalPrice(1000)); // 1000
System.out.println(order2.getFinalPrice(1000)); // 900
```

もし`if (isVip) { ... } else { ... }`のような条件分岐で割引方法を切り替えて
いたら、新しい割引方法を追加するたびに`Order`クラス自体を修正する必要が
あります。Strategyパターンでは、新しい割引クラスを追加するだけで済みます
（トピック5で学んだ「ポリモーフィズムによる拡張性」そのものです）。

### Factoryパターン：オブジェクトの作り方をカプセル化する

```java
public class DiscountStrategyFactory {
    public static DiscountStrategy create(String customerType) {
        if (customerType.equals("VIP")) {
            return new TenPercentOff();
        }
        return new NoDiscount();
    }
}
```

「どの実装クラスを`new`するか」の判断ロジックを1か所（Factory）に集約することで、
呼び出す側は具体的なクラス名を知らなくてもよくなります。

## 3. Pythonとの対比

Pythonでも同じ設計は可能ですが、Pythonは**ダックタイピング**（クラスの継承関係に
関わらず、必要なメソッドさえ持っていれば同じように扱える性質）を持つ言語なので、
`DiscountStrategy`のような**明示的なインターフェース宣言なし**で似たことができます。

```python
class NoDiscount:
    def apply_discount(self, price):
        return price

class TenPercentOff:
    def apply_discount(self, price):
        return int(price * 0.9)

class Order:
    def __init__(self, discount_strategy):
        self.discount_strategy = discount_strategy  # 型を宣言しなくても渡せる

    def get_final_price(self, price):
        return self.discount_strategy.apply_discount(price)
```

Pythonでは「`apply_discount`というメソッドさえあれば、クラスの種類は問わない」
という柔軟さがあるため、`interface`を明示的に書かなくても同じパターンが実現
できます。一方Javaでは、トピック6で学んだ`interface`という**形式的な契約**を
使うことで、コンパイラが「このクラスは`DiscountStrategy`の資格を満たしているか」
をチェックしてくれます。これは「暗黙のルール（Python）」か「明示的な契約
（Java）」かという、2つの言語の設計思想の違いをよく表しています。

## 4. つまずきやすいポイント・よくある間違い

- **パターンを無理に当てはめようとする（オーバーエンジニアリング）**：
  分岐が1〜2個しかない単純な条件分岐に、わざわざインターフェースとクラスを
  複数作るのはやり過ぎです。「将来、種類が増えそうか」「テストしやすくしたいか」
  を考えて判断しましょう
- **パターンの名前だけ覚えて、目的を理解しないまま使う**：「Strategyパターンを
  使いました」ということ自体が目的化してしまうと、かえって読みにくいコードに
  なります。「何が変化しやすい部分で、それをどう差し替え可能にしたいか」を
  常に意識しましょう
- **Factoryが肥大化する**：`if`/`else if`がどんどん増えていく`Factory`は、
  結局同じ問題を先送りしているだけです。設定ファイルやMapを使った登録方式など、
  発展的な解決策もありますが、最初は「集約する」という考え方に慣れれば十分です

---

ここまでで分からない点はありますか？大丈夫であれば「OK」と言ってください。
演習問題に進みます。
