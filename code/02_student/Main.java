class Student {
    String name;
    int score;

    public Student(String name, int score){
        this.name = name;
        this.score = score;
    }

    public String introduce(){
        return "名前： " + name + ", " + "点数： " + score;
    }

    public void isPassed(){
        if(score >= 60){
            System.out.println(introduce() + "\n"  + "合格");
        }else{
            System.out.println(introduce() + "\n"  + "不合格");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Student student1 = new Student("山田", 75);
        Student student2 = new Student("鈴木", 45);

        student1.isPassed();
        student2.isPassed();
    }
}