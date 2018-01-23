package dot.empire.counter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import dot.empire.counter.activities.ActivitySettings;

import static dot.empire.counter.Preferences.TEXT_COLOUR;

/**
 * Settings List View Adapter. Created by siD on 23 Jan 2018.
 */
// http://www.vogella.com/tutorials/AndroidListView/article.html
public final class SettingsListAdapter extends ArrayAdapter<SettingOption> {

    private final ActivitySettings context;
    private final ArrayList<SettingOption> values;

    public SettingsListAdapter(ActivitySettings context, ArrayList<SettingOption> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.settings_rowlayout, parent, false);

        final String name = values.get(position).getName();

        if (name.isEmpty()) {
            return rowView;
        }

        TextView label = (TextView) rowView.findViewById(R.id.label);
        label.setText(name);

        int colour = Util.PREFERENCES.getInteger(TEXT_COLOUR.name(), -1);
        if (colour != -1) {
            label.setTextColor(colour);
        }

        ImageView icon = (ImageView) rowView.findViewById(R.id.imgIcon);
        icon.setImageDrawable(values.get(position).getIcon());

        return rowView;
    }
}