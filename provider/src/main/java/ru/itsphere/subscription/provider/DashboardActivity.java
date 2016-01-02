package ru.itsphere.subscription.provider;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.domain.Client;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private static final String tag = DashboardActivity.class.getName();
    private FloatingActionButton scanButton;
    private ProviderApplication context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = (ProviderApplication) this.getApplicationContext();
        initActivity();
    }

    private void initActivity() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scanButton = (FloatingActionButton) findViewById(R.id.fab);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException e) {
            Log.e(tag, "Start scanning error:", e);
            showDownloadQRCodeReaderDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String scanResult = data.getStringExtra("SCAN_RESULT");
        if (TextUtils.isEmpty(scanResult)) {
            Log.e(tag, "Scan result is empty:");
            showTryAgainScanQRCodeDialog();
        } else {
            final Client newClient = new Gson().fromJson(scanResult, Client.class);
            context.getServer().subscribeClientForOrganization(newClient, context.getCurrentOrganization()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Response<Void> response, Retrofit retrofit) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.dash_message_subscription_added), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(tag, String.format("subscribeClientForOrganization (clientId: %d, orgId: %d) has thrown: ",
                            newClient.getId(), context.getCurrentOrganization().getId()), t);
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.dash_error_create_new_subscription), Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    private void showTryAgainScanQRCodeDialog() {
        AlertDialog.Builder tryAgainDialog = new AlertDialog.Builder(DashboardActivity.this);
        tryAgainDialog.setTitle(R.string.dash_scan_result_empty_dialog_title);
        tryAgainDialog.setMessage(R.string.dash_scan_result_empty_dialog_message);
        tryAgainDialog.setPositiveButton(R.string.dash_scan_result_empty_dialog_button_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                scanButton.performClick();
            }
        });
        tryAgainDialog.setNegativeButton(R.string.dash_scan_result_empty_dialog_button_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });
        tryAgainDialog.show().show();
    }

    private void showDownloadQRCodeReaderDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(DashboardActivity.this);
        downloadDialog.setTitle(R.string.dash_download_qr_reader_dialog_title);
        downloadDialog.setMessage(R.string.dash_download_qr_reader_dialog_message);
        downloadDialog.setPositiveButton(R.string.dash_download_qr_reader_dialog_button_yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    DashboardActivity.this.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Log.e(tag, "Download QR code reade error:", e);
                    Toast.makeText(getApplicationContext(),
                            "Download QR code reade error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        downloadDialog.setNegativeButton(R.string.dash_download_qr_reader_dialog_button_no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });
        downloadDialog.show().show();
    }
}
