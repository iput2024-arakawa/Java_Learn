# 復習問題（トピック6: 抽象クラスとインターフェース）

以下のコードにはコンパイルエラーが含まれています。どこが問題か指摘し、
正しく直したコードを書いてください（実装は`Manager`や`Robot`と似た形で構いません）。

```java
public interface Chargeable {
    void charge();
}

public abstract class Vehicle implements Chargeable {
    private String name;

    public Vehicle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

```java
public class ElectricCar extends Vehicle {
    public ElectricCar(String name) {
        super(name);
    }
}
```

```java
public class Main {
    public static void main(String[] args) {
        Vehicle v = new Vehicle("テスト号");
        v.charge();

        Chargeable c = new ElectricCar("リーフ");
        c.charge();
    }
}
```

## 追加で答えてほしいこと

1. `Vehicle v = new Vehicle("テスト号");`がなぜエラーになるのか、
   「抽象クラス」という言葉を使って説明してください。
2. `ElectricCar`クラスのままでは何が足りないためにコンパイルが通らないのか説明し、
   足りない部分を補ってコンパイルが通る状態にしてください。
3. 修正後、`Chargeable c = new ElectricCar("リーフ");`のように
   **`Vehicle`と継承関係のない`Chargeable`型の変数に代入できる**のはなぜか、
   トピック6で学んだ内容を使って説明してください。

コードと説明を書けたら提出してください。レビューします。
