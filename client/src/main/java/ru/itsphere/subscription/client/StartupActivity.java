package ru.itsphere.subscription.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.service.Repository;
import ru.itsphere.subscription.domain.Client;

/**
 * Responsible for dispatching appropriate activity for the current user
 */
public class StartupActivity extends AppCompatActivity {

    private static String tag = StartupActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(tag, "Launched application...");

        new Repository().getCurrentUser().enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Response<Client> response, Retrofit retrofit) {
                Class<? extends Activity> nextActivity = RegistrationActivity.class;
                if (response.body() != null) {
                    Log.d(tag, "The user already registered");
                    nextActivity = SubscriptionsListActivity.class;
                }
                Intent intent = new Intent(StartupActivity.this, nextActivity);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(tag, "getCurrentUser has thrown: ", t);
                Toast.makeText(getApplicationContext(),
                        getString(R.string.sa_error_getting_user_information), Toast.LENGTH_LONG).show();
            }
        });
    }
}
