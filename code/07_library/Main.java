public class Main {
    public static void main(String args[]){
        Library library = new Library();
        Book book1 = new Book("プログラミング入門", "978-0-000001");
        Book book2 = new Book("実践プログラミング", "978-0-000002");
        Book book3 = new Book("プログラミング応用", "978-0-000003");
        Book book4 = new Book("プログラミング基礎", "978-0-000004");
        library.addBooks(book1);
        library.addBooks(book2);
        library.addBooks(book3);
        library.addBooks(book4);

        library.searchByTitle("プログラミング");
        try{
            library.borrowBook("978-0-000001");
            System.out.println("「" + library.getBooks("978-0-000001") + "」を貸し出しました。");
        }catch(AlreadyBorrowedException e){
            System.out.print("エラー: この本はすでに貸出中です");
        }
        
        try{
            library.borrowBook("978-0-000001");
            System.out.println("「" + library.getBooks("978-0-000001") + "」を貸し出しました。");
        }catch(AlreadyBorrowedException e){
            System.out.print("エラー: この本はすでに貸出中です");
        }
    }
}
