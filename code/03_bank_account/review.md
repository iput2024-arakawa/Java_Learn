# 復習問題（トピック3: カプセル化）

次の実装には設計上の問題があります。何が問題か指摘してください（コードは書かなくてOKです、説明だけで構いません）。

```java
public class Product {
    public int stock; // 在庫数

    public Product(int stock) {
        this.stock = stock;
    }
}
```

```java
Product p = new Product(10);
p.stock = -500; // これが通ってしまう
```
