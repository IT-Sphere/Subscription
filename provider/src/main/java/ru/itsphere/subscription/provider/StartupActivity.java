package ru.itsphere.subscription.provider;

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
    private ProviderApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = (ProviderApplication) this.getApplicationContext();
        Class<? extends Activity> nextActivity = RegistrationActivity.class;
        if (context.getCurrentOrganization() != null) {
            Log.d(tag, String.format("The organization (id: %d) already registered", context.getCurrentOrganization().getId()));
            nextActivity = SubscribersListActivity.class;
        }
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}
