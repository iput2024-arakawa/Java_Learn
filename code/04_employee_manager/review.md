# 復習問題（トピック4: 継承）

以下のコードには2つのコンパイルエラーがあります。それぞれどこが問題か指摘し、
正しく直したコードを書いてください。

```java
public class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void printInfo() {
        System.out.println("名前: " + name);
    }
}
```

```java
public class Dog extends Animal {
    private String breed; // 犬種

    public Dog(String name, String breed) {
        super(name);
        this.breed = breed;          // (a) 
        //今やろうとしていることはAnimalクラスからの継承を行うことでDogクラスでも使用できるようにしている。その作業を行うにはsuperを使わなければならない。←Superのことをメソッドと言っていいのか思い出せない。
         // (b)
         // さっきやろうとしていたことはthis.nameにそのままnameを代入しようとしていた。しかし、それはAnimalクラスでnameはpraivateになっているため不可能である。もし、nameを持ってきたいのならAnimalクラスにあるgetName()を使用する必要がある。
    }

    public void printBreed() {
        System.out.println(breed);
    }
}
```

```java
public class Main{
    public static void main(String [] args){
        Animal a = new Animal("ポチ");
        Dog d = new Dog("クロ", "柴犬");

        a.printInfo();
        d.printBreed();
        d.printInfo();

    }
}

## 答えてほしいこと

1. (a)の行がなぜコンパイルエラーになるか、`super`というキーワードを使って説明し、
   正しいコードに直してください。
2. (b)の行がなぜコンパイルエラーになるか、`private`というアクセス修飾子の意味を
   使って説明し、正しいコードに直してください（`name`に値を設定するには
   どうすればよいか考えてください）。
3. 直した`Dog`クラスを使って、`Main`クラスで`Dog`のインスタンスを1つ作り、
   `printInfo()`（親クラスから継承）と`printBreed()`（`Dog`独自）を
   両方呼び出すコードを書いてください。

コードと説明を書けたら提出してください。レビューします。
