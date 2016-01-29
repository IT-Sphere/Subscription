package ru.itsphere.subscription.client;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.client.adapter.SubscriptionAdapter;
import ru.itsphere.subscription.common.utils.CountDownTimerObservable;
import ru.itsphere.subscription.common.utils.ToastUtils;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.domain.Visit;

public class SubscriptionsListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Observer {

    private static final String tag = SubscriptionsListActivity.class.getName();
    private ClientApplication context;
    private ListView subscriptionsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriptions_list);
        context = (ClientApplication) this.getApplicationContext();
        context.registerObserverForCurrentClient(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SubscriptionsListActivity.this, ShowQRCodeActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_subscription_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.sub_navigation_drawer_open, R.string.sub_navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createSubscriptionsView();
    }

    private void createSubscriptionsView() {
        subscriptionsView = (ListView) findViewById(R.id.subscriptions);
        subscriptionsView.setAdapter(new SubscriptionAdapter(
                this, android.R.layout.simple_list_item_1,
                android.R.id.text1, new ArrayList<>(context.getCurrentClient().getSubscriptions())));
        subscriptionsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                onSubscriptionClick(view, position);
            }
        });
    }

    private void onSubscriptionClick(View view, int position) {
        final Subscription subscription = (Subscription) subscriptionsView.getItemAtPosition(position);
        Call<Visit> call = context.getServer().registerVisit(subscription);
        ArrayAdapter adapter = (ArrayAdapter) subscriptionsView.getAdapter();
        adapter.notifyDataSetChanged();
        if (call == null) {
            String msg = new StringBuilder()
                    .append("You have no visits to ")
                    .append(subscription.getName())
                    .append(" anymore.").toString();
            ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
            return;
        }
        final ProgressBar clickedViewProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        clickedViewProgressBar.setVisibility(View.VISIBLE);
        enqueueRegisterVisit(call, clickedViewProgressBar);
    }

    private void enqueueRegisterVisit(Call<Visit> call, final ProgressBar clickedViewProgressBar) {
        call.enqueue(new Callback<Visit>() {
            @Override
            public void onResponse(final Response<Visit> response, Retrofit retrofit) {
                onRegisterVisitResponse(response, clickedViewProgressBar);
            }

            @Override
            public void onFailure(Throwable t) {
                onRegisterVisitFailure(t, clickedViewProgressBar);
            }
        });
    }

    private void onRegisterVisitResponse(Response<Visit> response, ProgressBar clickedViewProgressBar) {
        addVisitToSubscription(response.body());
        final int millisInFuture = 10000;
        final int countDownInterval = 10;
        String msg = new StringBuilder().append(getString(R.string.sub_visit_progress_start)).toString();
        ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
        startCountDownTimer(response, millisInFuture, countDownInterval, clickedViewProgressBar);
    }

    private void onRegisterVisitFailure(Throwable t, ProgressBar clickedViewProgressBar) {
        String msg = "registerVisit has thrown an exception: ";
        Log.e(tag, msg, t);
        clickedViewProgressBar.setVisibility(View.GONE);
        ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    }

    private void startCountDownTimer(final Response<Visit> response, final int millisInFuture, int countDownInterval, final ProgressBar clickedViewProgressBar) {
        new CountDownTimerObservable(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                onCountDownTimerUpdate(data, clickedViewProgressBar, response, millisInFuture);
            }
        }, millisInFuture, countDownInterval).start();
    }

    private void onCountDownTimerUpdate(Object data, ProgressBar clickedViewProgressBar, Response<Visit> response, int millisInFuture) {
        if (data == null) {
            onCountDownTimerFinish(clickedViewProgressBar, response);
        } else {
            onCountDownTimerTick((long) data, millisInFuture, clickedViewProgressBar);
        }
    }

    private void onCountDownTimerTick(long data, int millisInFuture, ProgressBar clickedViewProgressBar) {
        long millisUntilFinished = data;
        int progress = (int) ((double) (millisUntilFinished * 100.0 / millisInFuture));
        clickedViewProgressBar.setProgress(progress);
    }

    private void onCountDownTimerFinish(final ProgressBar clickedViewProgressBar, Response<Visit> response) {
        clickedViewProgressBar.setVisibility(View.INVISIBLE);
        enqueueFinishVisit(clickedViewProgressBar, response);
        String msg = new StringBuilder().append(getString(R.string.sub_visit_progress_end)).toString();
        ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    }

    private void enqueueFinishVisit(final ProgressBar clickedViewProgressBar, Response<Visit> response) {
        context.getServer().finishVisit(response.body()).enqueue(new Callback<Visit>() {
            @Override
            public void onResponse(Response<Visit> response, Retrofit retrofit) {
                onFinishVisitResponse(response);
            }

            @Override
            public void onFailure(Throwable t) {
                onFinishVisitFailure(t, clickedViewProgressBar);
            }
        });
    }

    private void onFinishVisitResponse(Response<Visit> response) {
        addVisitToSubscription(response.body());
    }

    private void onFinishVisitFailure(Throwable t, ProgressBar clickedViewProgressBar) {
        String msg = "finishVisit has thrown an exception: ";
        Log.e(tag, msg, t);
        clickedViewProgressBar.setVisibility(View.GONE);
        ToastUtils.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
    }

    private void addVisitToSubscription(Visit visit) {
        for (Subscription sub : context.getCurrentClient().getSubscriptions()) {
            if (sub.getId() == visit.getSubscriptionId()) {
                sub.getVisits().add(visit);
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_subscription_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subscriptions_list, menu);
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
        } else if (id == R.id.action_clean_db) {
            context.getApplicationService().cleanDataBase();
            return true;
        } else if (id == R.id.action_refresh_data_from_server) {
            context.initDataFromServer();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_subscription_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void update(Observable observable, final Object data) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                refreshSubscriptionsView((Client) data);
            }
        });
    }

    private void refreshSubscriptionsView(Client client) {
        SubscriptionAdapter adapter = new SubscriptionAdapter(
                this, android.R.layout.simple_list_item_1,
                android.R.id.text1, new ArrayList<>(client.getSubscriptions()));
        subscriptionsView.setAdapter(adapter);
    }
}
