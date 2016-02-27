package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.client.ClientApplication;
import ru.itsphere.subscription.client.R;
import ru.itsphere.subscription.common.utils.CountDownTimerObservable;
import ru.itsphere.subscription.common.utils.ToastUtils;
import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.domain.Visit;

/**
 * Button for visit start.
 */
class StartNewVisitButton implements View.OnClickListener {

    private static final String tag = StartNewVisitButton.class.getName();

    private final ClientApplication context;
    private Subscription relatedSubscription;
    private SubscriptionAdapterItemViewHolder parentViewHolder;

    public StartNewVisitButton(Context context, Subscription relatedSubscription, SubscriptionAdapterItemViewHolder parentViewHolder) {
        this.context = (ClientApplication) context.getApplicationContext();
        this.relatedSubscription = relatedSubscription;
        this.parentViewHolder = parentViewHolder;
    }

    @Override
    public void onClick(View v) {
        Call<Visit> call = context.getServer().registerVisit(relatedSubscription);
        if (call == null) {
            String msg = new StringBuilder()
                    .append("You have no visits to ")
                    .append(relatedSubscription.getName())
                    .append(" anymore.").toString();
            ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
            return;
        }
        parentViewHolder.progressBar.setProgress(100);
        parentViewHolder.progressBar.setVisibility(View.VISIBLE);
        enqueueRegisterVisit(call, parentViewHolder, relatedSubscription);
    }

    private void enqueueRegisterVisit(Call<Visit> call, final SubscriptionAdapterItemViewHolder viewHolder, final Subscription subscription) {
        call.enqueue(new Callback<Visit>() {
            @Override
            public void onResponse(final Response<Visit> response, Retrofit retrofit) {
                onRegisterVisitResponse(response, viewHolder, subscription);
            }

            @Override
            public void onFailure(Throwable t) {
                onRegisterVisitFailure(t, viewHolder, subscription);
            }
        });
    }

    private void onRegisterVisitFailure(Throwable t, SubscriptionAdapterItemViewHolder viewHolder, Subscription subscription) {
        String msg = "registerVisit has thrown an exception: ";
        Log.e(tag, msg, t);
        viewHolder.progressBar.setVisibility(View.GONE);
        ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
        setVisitsNumberInUIThread(viewHolder, subscription);
    }

    private void setVisitsNumberInUIThread(final SubscriptionAdapterItemViewHolder viewHolder, final Subscription item) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                viewHolder.visitsNumberView.setText(item.getVisits().size() + "/" + item.getVisitsNumber());
            }
        });
    }

    private void onFinishVisitResponse(Response<Visit> response, Subscription subscription) {
        Visit finishedVisit = response.body();
        addVisitToSubscription(finishedVisit, subscription);
    }

    private void addVisitToSubscription(Visit visit, Subscription subscription) {
        subscription.getVisits().remove(visit);
        subscription.getVisits().add(visit);
    }

    private void onRegisterVisitResponse(Response<Visit> response, SubscriptionAdapterItemViewHolder viewHolder, Subscription subscription) {
        addVisitToSubscription(response.body(), subscription);
        setVisitsNumberInUIThread(viewHolder, subscription);

        final int millisInFuture = 10000;
        final int countDownInterval = 10;
        String msg = new StringBuilder().append(context.getString(R.string.sub_visit_progress_start)).toString();
        ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
        startCountDownTimer(response, subscription, millisInFuture, countDownInterval, viewHolder.progressBar);
    }

    private void startCountDownTimer(final Response<Visit> response, final Subscription subscription, final int millisInFuture, int countDownInterval, final ProgressBar clickedViewProgressBar) {
        new CountDownTimerObservable(new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                onCountDownTimerUpdate(data, subscription, clickedViewProgressBar, response, millisInFuture);
            }
        }, millisInFuture, countDownInterval).start();
    }

    private void onCountDownTimerUpdate(Object data, Subscription subscription, ProgressBar clickedViewProgressBar, Response<Visit> response, int millisInFuture) {
        if (data == null) {
            onCountDownTimerFinish(clickedViewProgressBar, response, subscription);
        } else {
            onCountDownTimerTick((long) data, millisInFuture, clickedViewProgressBar);
        }
    }

    private void onCountDownTimerTick(long data, int millisInFuture, ProgressBar clickedViewProgressBar) {
        long millisUntilFinished = data;
        int progress = (int) ((double) (millisUntilFinished * 100.0 / millisInFuture));
        clickedViewProgressBar.setProgress(progress);
    }

    private void onCountDownTimerFinish(final ProgressBar clickedViewProgressBar, Response<Visit> response, Subscription subscription) {
        clickedViewProgressBar.setVisibility(View.INVISIBLE);
        enqueueFinishVisit(clickedViewProgressBar, response, subscription);
        String msg = new StringBuilder().append(context.getString(R.string.sub_visit_progress_end)).toString();
        ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
    }

    private void enqueueFinishVisit(final ProgressBar clickedViewProgressBar, Response<Visit> response, final Subscription subscription) {
        context.getServer().finishVisit(response.body()).enqueue(new Callback<Visit>() {
            @Override
            public void onResponse(Response<Visit> response, Retrofit retrofit) {
                onFinishVisitResponse(response, subscription);
            }

            @Override
            public void onFailure(Throwable t) {
                onFinishVisitFailure(t, clickedViewProgressBar);
            }
        });
    }

    private void onFinishVisitFailure(Throwable t, ProgressBar clickedViewProgressBar) {
        String msg = "finishVisit has thrown an exception: ";
        Log.e(tag, msg, t);
        clickedViewProgressBar.setVisibility(View.GONE);
        ToastUtils.makeText(context, msg, Toast.LENGTH_LONG);
    }
}
