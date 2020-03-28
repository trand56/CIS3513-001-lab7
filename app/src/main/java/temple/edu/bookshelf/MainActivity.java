package temple.edu.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedListener, BookDetailsFragment.BookDisplayListener {

    // List of Books. Books represented as HashMaps
    // HashMap: <title, author>
    private ArrayList<HashMap> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        // load books here
        String[] titles = getResources().getStringArray(R.array.book_titles);
        int titleslen = titles.length;
        String[] authors = getResources().getStringArray(R.array.book_authors);
        int authorslen = titles.length;

        books = new ArrayList<HashMap>();
        for(int i = 0; i < titleslen; i++)
        {
            HashMap<String, String> newBook = new HashMap<String, String>();
            newBook.put(titles[i], i < authorslen ? authors[i] : "None");
            books.add(newBook);
        }
    }


    @Override
    public void onBookSelected(int index) {

    }

    @Override
    public void displayBook(HashMap book) {

    }
}
