public class Main{
    public static void main (String[] args){
        Printable[] items = {
            new Manager("鈴木", 350000, 50000),
            new Intern("田中", 150000, "山田"),
            new Robot("R2D2")
        };

        for (Printable item : items){
            item.printInfo();
        }
    }
}