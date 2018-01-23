package dot.empire.counter.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

import dot.empire.counter.Counter;
import dot.empire.counter.CounterListAdapter;
import dot.empire.counter.R;
import dot.empire.counter.Util;

import static dot.empire.counter.Preferences.ACTIVE;
import static dot.empire.counter.Preferences.BG_COLOUR;
import static dot.empire.counter.Preferences.BUTTON_COLOUR;

/**
 * Activity Counter List. Created by Matthew Van der Bijl on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class ActivityCounterList extends AppCompatActivity implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private final ArrayList<String> list;

    public ActivityCounterList() {
        this.list = new ArrayList<String>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.view).setBackgroundColor(Util.PREFERENCES.getInteger(BG_COLOUR.name(), 0xFFFFFF));

        ListView listView = (ListView) findViewById(R.id.listView);
        for (Counter cnt : Util.COUNTERS) {
            this.list.add(cnt.getName());
        }
        CounterListAdapter adapter = new CounterListAdapter(this, list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);


//        ((Button) findViewById(R.id.btnNew)).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setBackgroundColor(Util.PREFERENCES.getInteger(BUTTON_COLOUR.name(), 0xFF4081));
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter
     * @param id       The row id of the item that was clicked
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(this, list.get(position), Toast.LENGTH_SHORT).show();

        ActivityMain.target = list.get(position);
        Util.PREFERENCES.putString(ACTIVE.name(), list.get(position));
        Util.PREFERENCES.flush();

        startActivity(new Intent(this, ActivityMain.class));
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.btnNew:
        final Str str = new Str();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What is the name of the new Counter?");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_DATETIME_VARIATION_NORMAL);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                str.str = input.getText().toString().trim();

                Log.i("User input", str.str);

                if (str.str != null && !str.str.isEmpty()) {
                    Util.COUNTERS.add(new Counter(str.str));
                    Collections.sort(Util.COUNTERS);
                    Util.saveCounters();

                    ActivityMain.target = null;
                    Util.PREFERENCES.putString(ACTIVE.name(), str.str);
                    Util.PREFERENCES.flush();

                    // Log.i("target", Util.PREFERENCES.getString(Util.ACTIVE, ""), new RuntimeException());


                    startActivity(new Intent(ActivityCounterList.this, ActivityMain.class));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

//                break;
//        }
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(this, ActivityMain.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Util.mainMenu(this, item);
        return super.onOptionsItemSelected(item);
    }

    private class Str {
        String str;
    }
}
