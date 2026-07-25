public class Robot implements Printable{
    private String model;

    public Robot (String model){
        this.model = model;
    }

    public String getModel(){
        return model;
    }

    @Override
    public void printInfo(){
        System.out.println("型番: " + model + "( ロボット)");
    }
}