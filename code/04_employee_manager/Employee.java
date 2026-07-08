public class Employee{
    private String name;
    private int baseSalary;

    public Employee(String name, int baseSalary){
        this.name = name;
        setBaseSalary(baseSalary);
    }

    public String getName(){
        return name;
    }

    public int getBaseSalary(){
        return baseSalary;
    }

    public void setBaseSalary(int baseSalary){
        if (baseSalary < 0){
            this.baseSalary = 0;
        }else{
            this.baseSalary = baseSalary;
        }
    }

    public void printInfo(){
        System.out.println("名前: " + name + ", " + "給与: " + baseSalary + "円");
        return;
    }
}