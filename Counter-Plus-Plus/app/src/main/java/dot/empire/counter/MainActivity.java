package dot.empire.counter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Main Activity. Created by Matthew Van der Bijl on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private AdView adView;

    private long value;
    private TextView txtOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getViewById(R.id.mainActivity).

        if (Core.PREFERENCES == null) {
            Core.PREFERENCES = new AndroidPreferences(getSharedPreferences(getResources().getString(
                    R.string.app_name), Context.MODE_PRIVATE));
        }

        this.txtOutput = (TextView) findViewById(R.id.txtOutput);
        this.txtOutput.setText((value = Core.PREFERENCES.getLong("value", 0)) + "");

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param event The MotionEvent object containing full information about
     *              the event
     * @return True if the listener has consumed the event, false otherwise
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return true;
    }

    /**
     * Called to process touch screen events.  You can override this to
     * intercept all touch screen events before they are dispatched to the
     * window.  Be sure to call this implementation for touch screen events
     * that should be handled normally.
     *
     * @param ev The touch screen event.
     * @return boolean Return true if this event was consumed.
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            this.value = value + 1 * (true ? 1 : -1);
            this.txtOutput.setText(value + "");

            Core.PREFERENCES.putLong("value", value);
            Core.PREFERENCES.flush();
        }
        return super.dispatchTouchEvent(ev);
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
        Core.PREFERENCES.flush(); // just to be safe
        super.onDestroy();
    }
}
