public class Book{
    private String title;
    private String isbn;
    private boolean borrowed;
    
    public Book(String title, String isbn){
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

    @Override
    public String toString(){
        String status = borrowed ? "貸出中" : "貸出可";
        return title + " (" + isbn + ") [" + status + "]";
    }
}