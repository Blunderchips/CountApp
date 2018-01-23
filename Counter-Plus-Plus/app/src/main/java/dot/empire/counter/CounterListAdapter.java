package dot.empire.counter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import dot.empire.counter.activities.ActivityCounterList;

import static dot.empire.counter.Preferences.TEXT_COLOUR;

/**
 * Counter List View Adapter. Created by siD on 23 Jan 2018.
 */
// http://www.vogella.com/tutorials/AndroidListView/article.html
public final class CounterListAdapter extends ArrayAdapter<String> {

    private final ActivityCounterList context;
    private final ArrayList<String> values;

    public CounterListAdapter(ActivityCounterList context, ArrayList<String> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_rowlayout, parent, false);

        final String name = values.get(position);

        TextView label = (TextView) rowView.findViewById(R.id.label);
        label.setText(name);

        TextView value = ((TextView) rowView.findViewById(R.id.txtValue));
        value.setText(Util.getCounter(name).getValue() + "");

        int colour = Util.PREFERENCES.getInteger(TEXT_COLOUR.name(), -1);
        if (colour != -1) {
            label.setTextColor(colour);
            value.setTextColor(colour);
        }

        ImageButton btn = rowView.findViewById(R.id.btnDelete);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.deleteCounter(name);
                context.startActivity(new Intent(context, ActivityCounterList.class));
            }
        });

        return rowView;
    }
}