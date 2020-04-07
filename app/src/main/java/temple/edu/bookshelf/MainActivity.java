package temple.edu.bookshelf;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedListener {

    // List of Books. Books represented as HashMaps
    // HashMap: <title, author>
    private ArrayList<Book> books;
    private BookListFragment booksFrag;
    private BookDetailsFragment detailFrag;
    private int curIndex;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        requestQueue = Volley.newRequestQueue(this);

        findViewById(R.id.searchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchBox = findViewById(R.id.searchBox);
                if(searchBox.getText().length() > 0)
                    searchBooks(searchBox.getText().toString());
            }
        });

        if(savedInstanceState != null)
        {
            books = (ArrayList<Book>)savedInstanceState.getSerializable("books");
            curIndex = savedInstanceState.getInt("index", 0);
            if(books != null) {
                booksFrag = BookListFragment.newInstance(books);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container1, booksFrag)
                        .addToBackStack(null)
                        .commit();

                detailFrag = BookDetailsFragment.newInstance(books.size() > 0 ? books.get(curIndex) : null);
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
        if (books != null && books.size() > 0)
        {
            outState.putSerializable("books", books);
            outState.putInt("index", curIndex);
        }
    }

    public void searchBooks(String query){

        loadBooksHTTP(query);
    }


    /// Return an array list of Book objects given a String query
    private void loadBooksHTTP(String query)
    {
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, JSONHandler.getBookQuery(query), null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if(books != null) books.clear();
                            else books = new ArrayList<Book>();

                            for(int i=0; i < response.length(); i++){
                                JSONObject obj = response.getJSONObject(i);

                                String book_id = obj.getString("book_id");
                                String title = obj.getString("title");
                                String author = obj.getString("author");
                                String cover_url = obj.getString("cover_url");
                                books.add(new Book(Integer.parseInt(book_id), title, author, cover_url));
                            }
                            if(books != null) {
                                booksFrag = BookListFragment.newInstance(books);
                                detailFrag = BookDetailsFragment.newInstance(null);

                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.container1, booksFrag)
                                        .addToBackStack(null)
                                        .commit();
                                if (findViewById(R.id.container2) != null)
                                    getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.container2, detailFrag)
                                            .commit();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(req);
        /*
        String[] titles = getResources().getStringArray(R.array.book_titles);
        int titleslen = titles.length;
        String[] authors = getResources().getStringArray(R.array.book_authors);
        int authorslen = titles.length;

        ArrayList<Book> bks = new ArrayList<Book>();
        for (int i = 0; i < titleslen && i < authorslen; i++) {
            Book newBook = new Book(i, titles[i], authors[i], null);
            bks.add(newBook);
        }
        return bks;

         */
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
