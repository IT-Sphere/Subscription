package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.client.ClientApplication;
import ru.itsphere.subscription.client.R;
import ru.itsphere.subscription.client.VisitsListActivity;
import ru.itsphere.subscription.common.format.AppDateFormat;
import ru.itsphere.subscription.common.utils.CountDownTimerObservable;
import ru.itsphere.subscription.common.utils.ToastUtils;
import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.domain.Visit;

public class SubscriptionAdapter extends ArrayAdapter<Subscription> {

    private static final String tag = SubscriptionAdapter.class.getName();

    public SubscriptionAdapter(Context context, int textViewResourceId, int text, List<Subscription> items) {
        super(context, textViewResourceId, text, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_subscription, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
            viewHolder.purchaseDateView = (TextView) convertView.findViewById(R.id.purchaseDateView);
            viewHolder.visitsNumberView = (TextView) convertView.findViewById(R.id.visitsNumberView);
            viewHolder.showVisitsButton = (Button) convertView.findViewById(R.id.showVisitsButton);
            viewHolder.startNewVisitButton = (Button) convertView.findViewById(R.id.startNewVisitButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Subscription item = getItem(position);
        if (item != null) {
            viewHolder.nameView.setText(String.format("%s", item.getName()));
            viewHolder.purchaseDateView.setText(item.getCreationDate() ==null ? "" : AppDateFormat.formatDateWithHoursAndMinutes(item.getCreationDate()));
            viewHolder.visitsNumberView.setText(String.valueOf(item.getVisits().size() + "/" + item.getVisitsNumber()));
            viewHolder.showVisitsButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubscriptionAdapter.this.getContext(), VisitsListActivity.class);
                    intent.putExtra(VisitsListActivity.PASSED_OBJECT_KEY, item.getVisits().toArray());
                    SubscriptionAdapter.this.getContext().startActivity(intent);
                }
            });
            viewHolder.startNewVisitButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSubscriptionClick(v, viewHolder.progressBar, item);
                }
            });
        }

        return convertView;
    }

    private void onSubscriptionClick(View view, ProgressBar progressBar, final Subscription subscription) {
        ClientApplication context = (ClientApplication) getContext().getApplicationContext();
        Call<Visit> call = context.getServer().registerVisit(subscription);
        if (call == null) {
            String msg = new StringBuilder()
                    .append("You have no visits to ")
                    .append(subscription.getName())
                    .append(" anymore.").toString();
            ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
            return;
        }
        progressBar.setProgress(100);
        progressBar.setVisibility(View.VISIBLE);
        enqueueRegisterVisit(call, progressBar);
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
        String msg = new StringBuilder().append(getContext().getString(R.string.sub_visit_progress_start)).toString();
        ToastUtils.makeText(getContext(), msg, Toast.LENGTH_LONG);
        startCountDownTimer(response, millisInFuture, countDownInterval, clickedViewProgressBar);
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
        String msg = new StringBuilder().append(getContext().getString(R.string.sub_visit_progress_end)).toString();
        ToastUtils.makeText(getContext(), msg, Toast.LENGTH_LONG);
    }

    private void enqueueFinishVisit(final ProgressBar clickedViewProgressBar, Response<Visit> response) {
        ClientApplication context = (ClientApplication) getContext().getApplicationContext();
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
        ToastUtils.makeText(getContext(), msg, Toast.LENGTH_LONG);
    }

    private void addVisitToSubscription(Visit visit) {
        ClientApplication context = (ClientApplication) getContext().getApplicationContext();
        for (Subscription sub : context.getCurrentClient().getSubscriptions()) {
            if (sub.getId() == visit.getSubscriptionId()) {
                sub.getVisits().add(visit);
                return;
            }
        }
    }

    private void onRegisterVisitFailure(Throwable t, ProgressBar clickedViewProgressBar) {
        String msg = "registerVisit has thrown an exception: ";
        Log.e(tag, msg, t);
        clickedViewProgressBar.setVisibility(View.GONE);
        ToastUtils.makeText(getContext(), msg, Toast.LENGTH_LONG);
    }

    private static class ViewHolder {
        private ProgressBar progressBar;
        private TextView nameView;
        private TextView purchaseDateView;
        private TextView visitsNumberView;
        private Button showVisitsButton;
        private Button startNewVisitButton;
    }
}
