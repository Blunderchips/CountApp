package dot.empire.counter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;

import dot.empire.counter.Counter;
import dot.empire.counter.R;
import dot.empire.counter.Util;

import static dot.empire.counter.Preferences.ACTIVE;
import static dot.empire.counter.Preferences.BG_COLOUR;
import static dot.empire.counter.Preferences.BUTTON_COLOUR;
import static dot.empire.counter.Preferences.FLAG;
import static dot.empire.counter.Preferences.TEXT_COLOUR;

/**
 * Main Activity. Created by Matthew Van der Bijl on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class ActivityMain extends AppCompatActivity implements View.OnClickListener,
        View.OnTouchListener {

    public static String target;

    private Counter counter;
    private AdView adView;
    private TextView txtOutput;
    private boolean flag;

    public ActivityMain() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.view).setOnTouchListener(this);

        if (Util.PREFERENCES == null) {
            Util.PREFERENCES = new AndroidPreferences(getSharedPreferences(getResources().getString(
                    R.string.app_name), Context.MODE_PRIVATE));
        }

        this.flag = Util.PREFERENCES.getBoolean(FLAG.name(), true);

        int bgColour = Util.PREFERENCES.getInteger(BG_COLOUR.name(), -1);
        if (bgColour != -1) {
            findViewById(R.id.view).setBackgroundColor(bgColour);
        }

        // hacky, but it works
        ImageButton btn = ((ImageButton) findViewById(R.id.btnAction));
        if (!flag) {
            btn.setImageDrawable(getDrawable(R.drawable.ic_remove_black_24dp));
        } else {
            btn.setImageDrawable(getDrawable(R.drawable.ic_add_black_24dp));
        }
        // --

        try {
            if (Util.COUNTERS.isEmpty()) {
                String str = Util.PREFERENCES.getString("counters", "no");
                Counter[] arr = new Gson().fromJson(str, Counter[].class);

                for (Counter cnt : arr) {
                    Util.COUNTERS.add(cnt);
                    Collections.sort(Util.COUNTERS);
                }
            }
        } catch (JsonSyntaxException jse) {
            Log.w("Can't init counter", "counter array not found", jse);
            // Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show(); // assume first time open
        }
        if (target == null) {
            target = Util.PREFERENCES.getString(ACTIVE.name(), "");
        }

//        Log.i("target", target, new RuntimeException(Util.getCounter(target).toString()));

        this.txtOutput = (TextView) findViewById(R.id.txtOutput);
        int txtColour = Util.PREFERENCES.getInteger(TEXT_COLOUR.name(), -1);
        if (txtColour != -1) {
            this.txtOutput.setTextColor(txtColour);
        }

        if (target != null && !target.isEmpty()) {
            this.counter = Util.getCounter(target);
            // Toast.makeText(this, "bang", Toast.LENGTH_SHORT).show();
        } else if (!Util.COUNTERS.isEmpty()) {
            this.counter = Util.COUNTERS.get(0);
        }
        if (counter == null) {
            Util.COUNTERS.add(new Counter("Default"));
            this.counter = Util.getCounter("Default");
        }
        this.txtOutput.setText(counter.value + "");
        Toast.makeText(this, counter.getName(), Toast.LENGTH_LONG).show();

        // Buttons
        ImageButton btnAction = ((ImageButton) findViewById(R.id.btnAction));
        btnAction.setOnClickListener(this);

        ImageButton btnList = ((ImageButton) findViewById(R.id.btnList));
        btnList.setOnClickListener(this);

        ImageButton btnSettings = ((ImageButton) findViewById(R.id.btnSettings));
        btnSettings.setOnClickListener(this);


        int btnColour = Util.PREFERENCES.getInteger(BUTTON_COLOUR.name(), -1);
        if (btnColour != -1) {
            btnAction.setBackgroundColor(btnColour);
            btnList.setBackgroundColor(btnColour);
            btnSettings.setBackgroundColor(btnColour);
        }
        // --

        try {
//            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
//            analytics.setAnalyticsCollectionEnabled(true);

            MobileAds.initialize(this, getResources().getString(R.string.adUnitID));

            this.adView = (AdView) findViewById(R.id.adView);

            AdRequest.Builder builder = new AdRequest.Builder();
            this.adView.loadAd(builder.build());
        } catch (Exception ex) {
            Log.e("Error creating ad", ex.getClass().getSimpleName(), ex);
            Toast.makeText(this, ex.getLocalizedMessage().toLowerCase(), Toast.LENGTH_LONG).show();
        }
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAction:
                ImageButton btn = ((ImageButton) findViewById(R.id.btnAction));
                if (flag) {
                    btn.setImageDrawable(getDrawable(R.drawable.ic_remove_black_24dp));
                } else {
                    btn.setImageDrawable(getDrawable(R.drawable.ic_add_black_24dp));
                }
                this.flag = !flag;

                Util.PREFERENCES.putBoolean(FLAG.name(), flag);
                Util.PREFERENCES.flush();
                break;
            case R.id.btnList:
                startActivity(new Intent(this, ActivityCounterList.class));
                break;
            case R.id.btnSettings:
                startActivity(new Intent(this, ActivitySettings.class));
                break;
        }

        // Toast.makeText(this, "button", Toast.LENGTH_SHORT).show();
    }

//    /**
//     * Called to process touch screen events.  You can override this to
//     * intercept all touch screen events before they are dispatched to the
//     * window.  Be sure to call this implementation for touch screen events
//     * that should be handled normally.
//     *
//     * @param ev The touch screen event.
//     * @return boolean Return true if this event was consumed.
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        boolean rtn = super.dispatchTouchEvent(ev);
//        if (!btnFlag && ev.getAction() == 0) {
//            counter.value = counter.value + 1 * (true ? 1 : -1);
//            this.txtOutput.setText(counter.value + "");
//
//            Util.saveCounters();
//        }
//
//        this.btnFlag = false;
//        return rtn;
//    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v   The view the touch event has been dispatched to
     * @param evt The MotionEvent object containing full information about
     *            the event
     * @return True if the listener has consumed the event, false otherwise
     */
    @Override
    public boolean onTouch(View v, MotionEvent evt) {
        if (evt.getAction() == 0) {
            counter.value = counter.value + 1 * (flag ? 1 : -1);
            this.txtOutput.setText(counter.value + "");

            Util.saveCounters();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Util.exit(this);
    }

    /**
     * Called when leaving the activity.
     */
    @Override
    public void onPause() {
        if (adView != null) {
            this.adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            this.adView.resume();
        }
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            this.adView.destroy();
        }
        // Util.PREFERENCES.flush(); // just to be safe
        super.onDestroy();
    }
}
