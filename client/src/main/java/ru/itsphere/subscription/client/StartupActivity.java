package ru.itsphere.subscription.client;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import ru.itsphere.subscription.common.service.RegistrationService;

/**
 * Responsible for dispatching appropriate activity for the current user
 */
public class StartupActivity extends AppCompatActivity {

    private static String tag = StartupActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "Launched application...");
        Class<? extends Activity> nextActivity = RegistrationActivity.class;
        if (RegistrationService.isAlreadyRegistered()) {
            Log.d(tag, "The user already registered");
            nextActivity = SubscriptionsListActivity.class;
        }
        Intent intent = new Intent(this, nextActivity);
        startActivity(intent);
        finish();
    }
}
