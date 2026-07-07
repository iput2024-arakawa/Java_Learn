# 復習問題（トピック2: クラスとインスタンス）

以下の空欄を埋めて、`Book`クラスを完成させてください。

```java
class Book {
    String title;
    int price;

    // ここにコンストラクタを書く（titleとpriceを受け取って初期化）
    public Book (String title, int price){
        this.title = title;
        this.price = price;
    }

    public String bookname() {
        return title;
    }
}
public class Main{
    public static void main(String[] args){
        Book book1 = new Book("君の名は",1000);
        Book book2 = new Book("天気の子",1000);
        String title1 = book1.bookname();
        String title2 = book2.bookname();
        System.out.println("本のタイトルは" + title1 + "です！");
        System.out.println("本のタイトルは" + title2 + "です！");
    }
}
```

その上で、`Main`クラスで`Book`のインスタンスを2つ作り、それぞれの`title`を出力するコードを書いてください。
