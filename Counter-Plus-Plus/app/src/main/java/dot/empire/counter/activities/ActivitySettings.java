package dot.empire.counter.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;

import dot.empire.counter.R;
import dot.empire.counter.SettingOption;
import dot.empire.counter.SettingsListAdapter;
import dot.empire.counter.Util;

import static dot.empire.counter.Preferences.BG_COLOUR;
import static dot.empire.counter.Preferences.BUTTON_COLOUR;
import static dot.empire.counter.Preferences.TEXT_COLOUR;

/**
 * Settings Activity. Created by Matthew Van der Bijl on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class ActivitySettings extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final ArrayList<SettingOption> list;

    public ActivitySettings() {
        this.list = new ArrayList<SettingOption>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.view).setBackgroundColor(Util.PREFERENCES.getInteger(BG_COLOUR.name(), 0xFFFFFF));

        {
            this.list.add(new SettingOption("Button Colour", getDrawable(R.drawable.ic_color_lens_black_24dp)));
            this.list.add(new SettingOption("Background Colour", getDrawable(R.drawable.ic_color_lens_black_24dp)));
            this.list.add(new SettingOption("Text Colour", getDrawable(R.drawable.ic_color_lens_black_24dp)));
            this.list.add(new SettingOption("Reset All Colours", getDrawable(R.drawable.ic_color_lens_black_24dp)));

            this.list.add(new SettingOption("Reset All Counters", getDrawable(R.drawable.ic_delete_black_24dp)));
            this.list.add(new SettingOption("Delete All Counters", getDrawable(R.drawable.ic_delete_black_24dp)));

            // spacer
            // this.list.add(new SettingOption("", getDrawable(R.drawable.ic_info_outline_black_24dp)));

            this.list.add(new SettingOption("About", getDrawable(R.drawable.ic_info_outline_black_24dp)));
            this.list.add(new SettingOption("Share", getDrawable(R.drawable.ic_share_black_24dp)));
            this.list.add(new SettingOption("Exit", getDrawable(R.drawable.ic_power_settings_new_black_24dp)));
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        SettingsListAdapter adapter = new SettingsListAdapter(this, list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(this, ActivityMain.class));
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (list.get(position).getName()) {
            case "Reset Colours":
                resetColours();
                break;
            case "Reset All":
                Util.resetAll(this);
                break;
            case "Delete All":
                Util.deleteAll(this);
                break;
            case "Share":
                Util.share(this);
                break;
            case "About":
                startActivity(new Intent(this, ActivityAbout.class));
                return;
            case "Button Colour":
                this.setButtonColour();
                return;
            case "Background Colour":
                this.setBackgroundColour();
                return;
            case "Text Colour":
                this.setTextColour();
                return;
            case "Exit":
                Util.exit(this);
                return;
        }
    }

    private void resetColours() {
        Util.PREFERENCES.remove(BUTTON_COLOUR.name());
        Util.PREFERENCES.remove(TEXT_COLOUR.name());
        Util.PREFERENCES.remove(BG_COLOUR.name());
        Util.PREFERENCES.flush();

        Toast.makeText(this, "Colours reset", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, ActivitySettings.class));
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

    private void setButtonColour() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose Primary colour")
//                .initialColor(Util.PREFERENCES.getInteger(BUTTON_COLOUR.name(), 0xff000000))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("Colour picker primary", "onColorSelected: 0x" + Integer.toHexString(selectedColor));

                        Util.PREFERENCES.putInteger(BUTTON_COLOUR.name(), selectedColor);
                        Util.PREFERENCES.flush();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).build().show();
    }

    private void setBackgroundColour() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose background colour")
//                .initialColor(Util.PREFERENCES.getInteger(BG_COLOUR.name(), 0xff000000))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("Colour picker accent", "onColorSelected: 0x" + Integer.toHexString(selectedColor));

                        Util.PREFERENCES.putInteger(BG_COLOUR.name(), selectedColor);
                        Util.PREFERENCES.flush();

                        startActivity(new Intent(ActivitySettings.this, ActivitySettings.class));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).build().show();
    }

    private void setTextColour() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose text colour")
//                .initialColor(Util.PREFERENCES.getInteger(TEXT_COLOUR.name(), 0xff000000))
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int selectedColor) {
                        Log.i("Colour picker text", "onColorSelected: 0x" + Integer.toHexString(selectedColor));

                        Util.PREFERENCES.putInteger(TEXT_COLOUR.name(), selectedColor);
                        Util.PREFERENCES.flush();

                        startActivity(new Intent(ActivitySettings.this, ActivitySettings.class));
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//                        changeBackgroundColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).build().show();
    }
}
