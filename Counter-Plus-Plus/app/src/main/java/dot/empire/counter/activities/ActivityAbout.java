package dot.empire.counter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import dot.empire.counter.R;
import dot.empire.counter.Util;

import static dot.empire.counter.Preferences.BG_COLOUR;
import static dot.empire.counter.Preferences.BUTTON_COLOUR;

/**
 * About Activity. The app can be shared from this activity. Created by Matthew Van der Bijl on 23
 * Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class ActivityAbout extends AppCompatActivity implements View.OnClickListener {

    /**
     * Default constructor.
     */
    public ActivityAbout() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int bgColour = Util.PREFERENCES.getInteger(BG_COLOUR.name(), -1);
        if (bgColour != -1) {
            findViewById(R.id.view).setBackgroundColor(bgColour);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        int btnColour = Util.PREFERENCES.getInteger(BUTTON_COLOUR.name(), -1);
        if (btnColour != -1) {
            fab.setBackgroundColor(btnColour);
        }

        try {
            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
            analytics.setAnalyticsCollectionEnabled(true);
        } catch (Exception ex) {
            Log.e("Analytics error", ex.getClass().getSimpleName(), ex);
            Toast.makeText(this, ex.getLocalizedMessage().toLowerCase(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        Util.share(this);
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
}
