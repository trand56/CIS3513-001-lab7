package temple.edu.bookshelf;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
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

import edu.temple.audiobookplayer.AudiobookService;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookSelectedListener, BookDetailsFragment.BookInteractionListener {

    // List of Books. Books represented as HashMaps
    // HashMap: <title, author>
    private ArrayList<Book> books;
    private BookListFragment booksFrag;
    private BookDetailsFragment detailFrag;
    private int curIndex;
    private int curProgress;
    private int isPlaying;

    RequestQueue requestQueue;

    private Intent audioServiceIntent;
    private ImageButton rewindButton; // acts as a stop button
    private ImageButton pauseButton; // pauses audio
    private SeekBar seekBar; // seek bar

    private boolean connected;


    AudiobookService.MediaControlBinder audiobookService;
    private final Handler progressHandler = new Handler(){
        @Override
        public void handleMessage(Message arg){
            curProgress = ((AudiobookService.BookProgress) arg.obj).getProgress();
        }
    };


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            connected = true;
            audiobookService = (AudiobookService.MediaControlBinder) service;
            audiobookService.setProgressHandler(progressHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            audiobookService = null;
        }
    };

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

        rewindButton = findViewById(R.id.rewindButton);
        pauseButton = findViewById(R.id.pauseButton);
        seekBar = findViewById(R.id.seekBar);

        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Rewind Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Pause Button Pressed", Toast.LENGTH_SHORT).show();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Value changed to " + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
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
                else
                    onBookSelected(curIndex);

                curProgress = savedInstanceState.getInt("progress", 0);
            }
        }

        // Make audio book player service and binds it
        //AudiobookService audioBook = new AudiobookService();
        audioServiceIntent = new Intent(MainActivity.this, AudiobookService.class);
        bindService(audioServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbindService(serviceConnection);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (books != null && books.size() > 0)
        {
            outState.putSerializable("books", books);
            outState.putInt("index", curIndex);
            outState.putInt("progress", curProgress);
            outState.putBoolean("playing", audiobookService.isPlaying());
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
                                String duration = obj.getString("duration");
                                books.add(new Book(Integer.parseInt(book_id), title, author, cover_url, Integer.parseInt(duration)));
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
    }

    public void stopAudio(){

    }

    public void pauseAudio(){

    }

    public void progressChanged(){

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

        onPlay(books.get(index));
    }

    @Override
    public void onPlay(Book b){
        Toast.makeText(this, "PLAY BUTTON CLICK", Toast.LENGTH_SHORT).show();
        if(audiobookService != null)
        {
            if(audiobookService.isPlaying()){
                audiobookService.stop();
            }
            audiobookService.play(b.getId(), b.getDuration());
        }
    }
}
