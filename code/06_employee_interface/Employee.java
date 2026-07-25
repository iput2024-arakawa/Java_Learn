public abstract class Employee implements Printable{
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

    public abstract void printInfo();
}