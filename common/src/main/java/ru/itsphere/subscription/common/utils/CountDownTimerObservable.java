package ru.itsphere.subscription.common.utils;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by acer on 28.01.2016.
 */
public class CountDownTimerObservable extends Observable {
    private long millisInFuture;
    private long countDownInterval;

    public CountDownTimerObservable(Observer observer, long millisInFuture, long countDownInterval) {
        this.addObserver(observer);
        this.millisInFuture = millisInFuture;
        this.countDownInterval = countDownInterval;
    }

    public void start() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(millisInFuture, countDownInterval) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        setChanged();
                        notifyObservers(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        setChanged();
                        notifyObservers();
                    }
                }.start();
            }
        });
    }
}
