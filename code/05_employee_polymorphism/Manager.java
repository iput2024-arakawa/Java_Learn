public class Manager extends Employee{
    private int managementAllowance;
    
    public Manager(String name, int baseSalary, int managementAllowance){
        super(name, baseSalary);
        setManagementAllowance(managementAllowance);
    }
    public int getManagementAllowance(){
        return managementAllowance;
    }
    
    public void setManagementAllowance(int managementAllowance){
        if (managementAllowance < 0){
            this.managementAllowance = 0;
        }else{
            this.managementAllowance = managementAllowance;
        }
    }
    
    @Override
    public void printInfo(){
        System.out.println("名前： " + getName() + ", " + "合計給与: " + getTotalSalary() + "円(役職手当込み)");
    }

    public int getTotalSalary(){
        int salary = getBaseSalary() + managementAllowance;
        return salary;
    }
}