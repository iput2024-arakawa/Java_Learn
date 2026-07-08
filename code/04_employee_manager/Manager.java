public class Manager extends Employee{
    private int managementAllowance;
    public Manager (String name, int baseSalary, int managementAllowance){
        super(name, baseSalary);
        this.managementAllowance = managementAllowance;
    }

    public int getTotalSalary(){
        int salary = getBaseSalary() + managementAllowance;
        return salary;
    }
}