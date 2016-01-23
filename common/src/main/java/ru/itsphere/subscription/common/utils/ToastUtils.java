package ru.itsphere.subscription.common.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * ToastUtils - helps to work with Toast messages
 */
public class ToastUtils {

    public static void makeText(final Context context, final String msg, final int length) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, length).show();
                }
            });
        }
    }
}
