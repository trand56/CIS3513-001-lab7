package temple.edu.bookshelf;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BookListAdapter extends BaseAdapter {

    Context context;
    LinearLayout[] entries;

    public BookListAdapter(Context parent, ArrayList<Book> books){
        int len = books.size();
        context = parent;
        entries = new LinearLayout[len];
        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            LinearLayout bookEntry = new LinearLayout(parent);
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    0.50f
            );
            param1.setMargins(10, 0, 10, 0);

            bookEntry.setOrientation(LinearLayout.HORIZONTAL);

            TextView titleText = new TextView(parent);
            TextView authorText = new TextView(parent);

            titleText.setText(book.getTitle());
            titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
            titleText.setLayoutParams(param1);
            authorText.setText(book.getAuthor());

            bookEntry.addView(titleText);
            bookEntry.addView(authorText);
            entries[i] = bookEntry;

        }
    }

    @Override
    public int getCount() {
        return entries.length;
    }

    @Override
    public Object getItem(int position) {
        return this.entries[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = entries[position];

        return convertView;
    }
}
