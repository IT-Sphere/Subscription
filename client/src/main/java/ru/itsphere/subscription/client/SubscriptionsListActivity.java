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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ru.itsphere.subscription.client.adapter.SubscriptionAdapter;
import ru.itsphere.subscription.domain.Client;

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
