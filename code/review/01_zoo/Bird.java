public class Bird extends Animal{
    private boolean canFly;
    
    public Bird(String name, int age, boolean canFly){
        this.canFly = canFly;
        super(name, age);
    }
    public boolean getCanFly(){
        return canFly;
    }
    @Override
    public void makeSound(){
        System.out.println(getName() + ": チュンチュン！");
    }

    @Override
    public void feed(){
        System.out.println(getName() + "に木の実をあげた");
    }
}
