package temple.edu.bookshelf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BookDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private Context parent;
    private HashMap<String,String> book = null;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    public static BookDetailsFragment newInstance(HashMap<String,String> book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();

        args.putSerializable("book", book);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            book = (HashMap<String,String>)getArguments().getSerializable("book");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("CREATE VIEW");
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_book_details, container, false);
        TextView titleText = layout.findViewById(R.id.title);
        TextView authorText = layout.findViewById(R.id.author);
        if(book != null){
            titleText.setText(book.get("title"));
            authorText.setText(book.get("author"));
        }
        else{
            titleText.setText("No book selected");
            authorText.setText("No book selected");
        }
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void displayBook(HashMap<String,String> b) {
        System.out.println("TITLE AUTHOR " + b.get("title") + ", " + b.get("author") );
        book = b;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }
}
