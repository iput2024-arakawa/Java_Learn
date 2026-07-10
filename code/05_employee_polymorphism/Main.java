public class Main{
    public static void main(String[] args){
        Employee[] staff = {
            new Employee("佐藤", 300000),
            new Manager("鈴木", 350000, 50000),
            new Intern("田中", 150000, "山田")
        };

        for (Employee person : staff) {
            person.printInfo();
        }
    }
}