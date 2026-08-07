public class RobotPet implements Feedable{
    private String model;

    public RobotPet(String model){
        this.model = model;
    }

    public String getModel(){
        return model;
    }

    @Override
    public void feed(){
        System.out.println("型番" + getModel() + "に電池を補給した");
    }
}
