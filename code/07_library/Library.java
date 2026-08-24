import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
public class Library {
    private Map<String, Book> books;

    public Library(){
        this.books = new HashMap<>();
    }

    public void addBooks(Book book){
        books.put(book.getIsbn(), book);
    }

    public List<Book> searchByTitle(String keyword){
        List<Book> result = new ArrayList<>();
        for (Book book : books.values()){
            if (book.getTitle().contains(keyword)){
                result.add(book);
            }
        }
        return result;
    }
    
    public void borrowBook(String isbn) throws BookNotFoundException{
        Book book = books.get(isbn);
        if (book == null){
            throw new BookNotFoundException("その本はありません");
        }
        if (book.isBorrowed()){
            throw new AlreadyBorrowedException("その本は貸し出されています");
        }
        book.setBorrowed(true);
    }

    public void returnBook(String isbn) throws BookNotFoundException{
        Book book = books.get(isbn);
        if (book == null){
            throw new BookNotFoundException("貸し出された本ではありません");
        }
        if (!book.isBorrowed()){
            throw new NotBorrowedException("その本は貸し出されていません");
        }
        book.setBorrowed(false);
    }

    public static <T> void printAll(List<T> items){
        for(T item : items){
            System.out.println(item);
        }
    }

    public Book getBooks(String isbn){
        return books.get(isbn);
    }
}
