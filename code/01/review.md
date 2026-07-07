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