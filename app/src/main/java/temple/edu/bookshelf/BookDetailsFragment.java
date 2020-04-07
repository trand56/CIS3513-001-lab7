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
    private Book book = null;
    private TextView titleText;
    private TextView authorText;

    public BookDetailsFragment() {
        // Required empty public constructor
    }


    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();

        args.putSerializable("book", book);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(savedInstanceState != null){
        //    book = (Book)savedInstanceState.getSerializable("book");
        //}
        if (getArguments() != null) {
            book = (Book)getArguments().getSerializable("book");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("CREATE VIEW");
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_book_details, container, false);
        titleText = layout.findViewById(R.id.title);
        authorText = layout.findViewById(R.id.author);
        if(book != null){
            displayBook(book);
        }
        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void displayBook(Book b) {
        System.out.println("TITLE AUTHOR " + b.getTitle() + ", " + b.getAuthor() );
        book = b;
        titleText.setText(b.getTitle());
        authorText.setText(b.getAuthor());
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
