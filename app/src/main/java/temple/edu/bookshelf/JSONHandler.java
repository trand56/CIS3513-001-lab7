package temple.edu.bookshelf;


public class JSONHandler {
    private static final String URL = "https://kamorris.com/lab/abp/booksearch.php?search=";
    public static String getBookQuery(String keyword){
        String fullQuery = URL + keyword;
        System.out.println(fullQuery);
        return fullQuery;
    }
}
