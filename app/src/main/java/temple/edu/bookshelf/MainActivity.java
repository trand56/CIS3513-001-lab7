package temple.edu.bookshelf;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedListener {

    // List of Books. Books represented as HashMaps
    // HashMap: <title, author>
    private ArrayList<HashMap<String, String>> books;
    private BookListFragment booksFrag;
    private BookDetailsFragment detailFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        // load books here
        String[] titles = getResources().getStringArray(R.array.book_titles);
        int titleslen = titles.length;
        String[] authors = getResources().getStringArray(R.array.book_authors);
        int authorslen = titles.length;

        books = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < titleslen && i < authorslen; i++) {
            HashMap<String, String> newBook = new HashMap<String, String>();
            newBook.put("title", titles[i]);
            newBook.put("author", authors[i]);
            books.add(newBook);
        }

        booksFrag = BookListFragment.newInstance(books);
        detailFrag = BookDetailsFragment.newInstance(books.get(0));

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container1, booksFrag)
                .commit();
        if(findViewById(R.id.container2) != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container2, detailFrag)
                    .commit();

    }


    @Override
    public void onBookSelected(int index) {

        if (findViewById(R.id.container2) == null) {
            BookDetailsFragment newDetail = BookDetailsFragment.newInstance(books.get(index));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container1, newDetail)
                    .addToBackStack(null)
                    .commit();
        } else {
            detailFrag.displayBook(books.get(index));
            getSupportFragmentManager()
                    .beginTransaction()
                    .detach(detailFrag)
                    .attach(detailFrag)
                    .commit();
        }
    }
}
