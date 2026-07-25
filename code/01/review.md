# 復習問題（トピック1: Javaの基本文法）

次のJavaコードのうち、コンパイルエラーになる行はどれですか？理由も一緒に説明してください。

```java
int count = 5;
count = "5個";        // (a)
String label = "件数";
label = label + count; // (b)
double rate = 1;       // (c)
```

aの部分でコンパイルエラーになると予想する。その理由として、まずcountの型はintと指定されている。そのため、aの行で文字列の"5個"と入力すると型が合わないためエラーになる。
また、予想になってしまうが、double rete = 1でSystem.out.println(rete) とすると1.0と表示されると予想する

---

## 復習（2回目・期間が空いたための再確認）

以下のコードを実行すると、(a)〜(d)にはそれぞれ何が出力されますか？
理由も添えて予想してください（実際に実行して確認する前に、まず自分で考えてみてください）。

```java
int a = 10;
int b = 3;
System.out.println(a / b);        // (a)

double c = a / b;
System.out.println(c);            // (b)

double d = (double) a / b;
System.out.println(d);            // (c)

char e = 'A';
int f = e + 1;
System.out.println(f);            // (d)
```

ヒント：
- Javaの`int`同士の割り算は、Pythonの`/`とは挙動が異なります
- `(double)`のような書き方を「キャスト（型変換）」と呼びます。どこで変換が起きているかに注目してください
- `char`型はJavaの内部では文字コード（数値）として扱われます

考えた予想と理由を書けたら提出してください。レビューします。