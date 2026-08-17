import java.util.HashMap;
import java.util.Map;
public class Library {
    private Map<String, Book> books;

    public Library(){
        this.books = new HashMap<>();
    }

    public void addBooks(Book book){
        books.put(book);
    }
}
