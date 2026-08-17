public class Book{
    private String title;
    private String isbn;
    private boolean borrowed;
    
    public Book(String title, String isbn, boolean borrowed){
        this.title = title;
        this.isbn = isbn;
        this.borrowed = false;
    }

    public String getTitle(){
        return title;
    }

    public String getIsbn(){
        return isbn;
    }

    public boolean isBorrowed(){
        return borrowed;
    }

    public void setBorrowed(boolean borrowed){
        this.borrowed = borrowed;
    }
}