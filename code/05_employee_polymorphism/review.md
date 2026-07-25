# 復習問題（トピック5: ポリモーフィズム）

以下のコードを見て、質問に答えてください。

```java
public class Shape {
    public void draw() {
        System.out.println("何かの図形を描画");
    }
}

public class Circle extends Shape {
    @Override
    public void draw() {
        System.out.println("円を描画");
    }

    public void printRadiusInfo() {
        System.out.println("半径についての情報");
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Shape(),
            new Circle()
        };

        for (Shape s : shapes) {
            s.draw();
            boolean result = printRadiusInfo() instanceof s;
            if (result == Ture){
                s.printRadiusInfo();
            }
        }
    }
}
```

1は最初のループではshapeのほうのdraw()が呼び出されて、２回目のループではcircleのほうのdraw()が呼び出される。同じsという変数を使用して呼び出されているが、sが同じでも参照型と実対が異なるため呼び出されるものが違ってくる。
2は実行時エラーになると思う。for文が実行されているとき、ShapeのクラスにはprintRadiusInfo()という関数は実装されていないためである。
3はinstanceofについてあまり知らなかったので軽く調べた感じinstanceofを使用するとこの関数が実際にそのクラスに存在するのかを確認してTrueかFalseを返すものである。そのため、もし特定の関数をしようするのなら、instanceofを使用してif文で関数　instanceof　クラス名　でTrueなら使用するという感じで使用すればよいのではないかと考えた。

一つ質問、説明資料の中でインスタンスとあったがこれは関数のことという認識であっているのかを教えてほしい。
## 答えてほしいこと

1. `for`ループの中で`s.draw()`を呼び出すと、1回目（`Shape`のインスタンス）と
   2回目（`Circle`のインスタンス）でそれぞれ何が出力されますか？
   また、`s`という同じ書き方で呼び出しているのに、なぜ実行結果が異なるのか、
   「参照型」と「実体（実際のインスタンスの型）」という言葉を使って説明してください。
2. (a)のコメントアウトを外すと何が起こりますか（コンパイルエラー／実行時エラー／
   問題なく動く、のいずれか）。そう考える理由も説明してください。
3. もし2回目のループの中でだけ`printRadiusInfo()`を呼び出したい場合、
   どうすればよいでしょうか。具体的なコード（ヒント：`instanceof`や
   キャスト（型変換）を調べてみましょう）を書いてみてください。
   ※これはトピック5の演習では扱っていない発展内容です。分からなければ
   「分かりません」でも構わないので、考えたことを書いてください。

コードと説明を書けたら提出してください。レビューします。
