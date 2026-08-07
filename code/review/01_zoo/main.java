public class main{
    public static void main (String[] args){
        Animal [] animals = {
            new Dog("ポチ", 3, "柴犬"),
            new Bird("ピーちゃん", 1, true)
        };

        for (Animal a : animals){
            a.makeSound();
        }

        Feedable[] items = {
            new Dog("ポチ", 3, "柴犬"),
            new Bird("ピーちゃん", 1, true),
            new RobotPet("AIBO-X1")
        };

        for (Feedable item : items) {
            item.feed();
        }
    }

    
}
