package temple.edu.bookshelf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedListener {

    // List of Books. Books represented as HashMaps
    // HashMap: <title, author>
    private ArrayList<Book> books;
    private BookListFragment booksFrag;
    private BookDetailsFragment detailFrag;
    private int curIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchBox = findViewById(R.id.searchBox);
                System.out.println("DEBUG DEBUG len: " + searchBox.getText().length());
                if(searchBox.getText().length() > 0)
                    searchBooks("test");
            }
        });

        if(savedInstanceState != null)
        {
            System.out.println("DEBUG Load Instance");
            books = (ArrayList<Book>)savedInstanceState.getSerializable("books");
            curIndex = savedInstanceState.getInt("index", 0);
            if(books != null) {
                System.out.println("DEBUG 2 " + books.size());
                booksFrag = BookListFragment.newInstance(books);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container1, booksFrag)
                        .addToBackStack(null)
                        .commit();


                detailFrag = BookDetailsFragment.newInstance(books.get(curIndex));
                if(findViewById(R.id.container2) != null)
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container2, detailFrag)
                            .commit();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (books != null)
        {
            outState.putSerializable("books", books);
            outState.putInt("index", curIndex);
        }
    }

    public void searchBooks(String query){

        String[] titles = getResources().getStringArray(R.array.book_titles);
        int titleslen = titles.length;
        String[] authors = getResources().getStringArray(R.array.book_authors);
        int authorslen = titles.length;

        books = new ArrayList<Book>();
        for (int i = 0; i < titleslen && i < authorslen; i++) {
            Book newBook = new Book(i, titles[i], authors[i], null);
            books.add(newBook);
        }

        booksFrag = BookListFragment.newInstance(books);
        detailFrag = BookDetailsFragment.newInstance(books.get(0));

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container1, booksFrag)
                .addToBackStack(null)
                .commit();
        if(findViewById(R.id.container2) != null)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container2, detailFrag)
                    .commit();
    }

    @Override
    public void onBookSelected(int index) {
        curIndex = index;
        if (findViewById(R.id.container2) == null) {
            BookDetailsFragment newDetail = BookDetailsFragment.newInstance(books.get(index));
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container1, newDetail)
                    .addToBackStack(null)
                    .commit();
        } else {
            detailFrag.displayBook(books.get(index));

        }
    }
}
