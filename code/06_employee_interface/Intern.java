public class Intern extends Employee{
    private String mentor;
    public Intern(String name, int baseSalary, String mentor){
        super(name, baseSalary);
        this.mentor = mentor;
    }

    public String getMentor(){
        return mentor;
    }

    @Override
    public void printInfo(){
        System.out.println("名前: " + getName() + ", " + "給与: " + getBaseSalary() + "円(メンター: " + mentor + ")" );
    }
}