public class Main{
    public static void main(String[] args){
        Employee employee = new Employee("佐藤", 300000);
        employee.printInfo();

        Manager manager = new Manager("鈴木", 350000, 50000);
        manager.printInfo();
        System.out.println("役職手当込みの合計給与: " + manager.getTotalSalary() + "円");
    }
}