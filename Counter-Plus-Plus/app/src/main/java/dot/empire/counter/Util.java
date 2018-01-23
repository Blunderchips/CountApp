package dot.empire.counter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidPreferences;
import com.google.gson.Gson;

import java.security.SecureRandom;
import java.util.ArrayList;

import dot.empire.counter.activities.ActivityAbout;
import dot.empire.counter.activities.ActivitySettings;

/**
 * Utilities class. So basically anything that didn't fit anywhere else. Created by Matthew Van der
 * Bijl on 23 Jan 2018. <strong>You cannot instantiation this class.</strong>
 *
 * @author Matthew Van der Bijl
 */
public final class Util {

    /**
     *
     */
    public static final SecureRandom T_RNG = new SecureRandom();
    public static ArrayList<Counter> COUNTERS = new ArrayList<>();
    /**
     *
     */
    public static AndroidPreferences PREFERENCES;

    /**
     * @deprecated Prevent instantiation of this class
     */
    @Deprecated
    private Util() {
    }

    public static Counter getCounter(String name) {
        for (Counter i : COUNTERS) {
            if (i.getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

    public static void saveCounters() {
        Counter[] arr = new Counter[COUNTERS.size()];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = COUNTERS.get(i);
        }

        String json = new Gson().toJson(arr);
        PREFERENCES.putString("counters", json);
        PREFERENCES.flush();

        Log.i("Saved json", json);
    }

    public static void deleteCounter(String name) {
        for (int i = 0; i < COUNTERS.size(); i++) {
            if (COUNTERS.get(i).getName().equals(name)) {
                COUNTERS.remove(i);
                break;
            }
        }
        saveCounters();
    }

    /**
     * Closes the app.
     */
    public static void exit(Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception ex) {
            Log.e("Exit feature", ex.getMessage(), ex);
        }
    }

    public static void share(Context context) {
        final String url = "";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }

    public static void mainMenu(Context context, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                Util.deleteAll(context);
                break;
            case R.id.action_reset_all:
                Util.resetAll(context);
                break;
            case R.id.action_share:
                Util.share(context);
                break;
            case R.id.action_about:
                context.startActivity(new Intent(context, ActivityAbout.class));
                break;
            case R.id.action_exit:
                Util.exit(context);
                break;
            case R.id.action_settings:
                context.startActivity(new Intent(context, ActivitySettings.class));
                break;
        }
    }

    public static void deleteAll(Context context) {
        COUNTERS.clear();
        saveCounters();

        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
    }

    public static void resetAll(Context context) {
        for (Counter cnt : COUNTERS) {
            cnt.setValue(0l);
        }
        saveCounters();

        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
    }
}
