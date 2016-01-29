package ru.itsphere.subscription.client;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import ru.itsphere.subscription.client.adapter.SubscriptionAdapter;
import ru.itsphere.subscription.client.adapter.VisitAdapter;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Visit;

public class VisitsListActivity extends AppCompatActivity implements Observer {

    public static final String PASSED_OBJECT_KEY = "visits";
    private ClientApplication context;
    private ListView visitsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_list);
        context = (ClientApplication) this.getApplicationContext();
        context.registerObserverForCurrentClient(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createVisitsView();
    }

    private List<Visit> getVisits() {
        List<Visit> visits = new ArrayList<>();
        Object[] array = (Object[]) getIntent().getSerializableExtra(PASSED_OBJECT_KEY);
        for (Object o : array) {
            visits.add((Visit) o);
        }
        return visits;
    }

    private void createVisitsView() {
        visitsView = (ListView) findViewById(R.id.visits);
        visitsView.setAdapter(new VisitAdapter(
                this, android.R.layout.simple_list_item_1,
                android.R.id.text1, getVisits()))
        ;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_visit_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void update(Observable observable, final Object data) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                refreshVisitsView((Client) data);
            }
        });
    }

    private void refreshVisitsView(Client client) {
        SubscriptionAdapter adapter = new SubscriptionAdapter(
                this, android.R.layout.simple_list_item_1,
                android.R.id.text1, new ArrayList<>(client.getSubscriptions()));
        visitsView.setAdapter(adapter);
    }
}
