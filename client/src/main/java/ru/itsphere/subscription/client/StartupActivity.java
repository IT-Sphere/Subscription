package ru.itsphere.subscription.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Responsible for dispatching appropriate activity for the current user
 */
public class StartupActivity extends AppCompatActivity {

    private static String tag = StartupActivity.class.getName();
    private ClientApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (ClientApplication) this.getApplicationContext();
        Class<? extends Activity> nextActivity = RegistrationActivity.class;
        if (context.getCurrentClient() != null) {
            Log.d(tag, String.format("The user (id: %d) already registered", context.getCurrentClient().getId()));
            nextActivity = SubscriptionsListActivity.class;
        }
        Intent intent = new Intent(StartupActivity.this, nextActivity);
        startActivity(intent);
        finish();
    }
}
