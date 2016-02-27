package ru.itsphere.subscription.client.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.itsphere.subscription.client.R;

class SubscriptionAdapterItemViewHolder {
    public final ProgressBar progressBar;
    public final TextView nameView;
    public final TextView purchaseDateView;
    public final TextView visitsNumberView;
    public final Button showVisitsButton;
    public final Button startNewVisitButton;

    public SubscriptionAdapterItemViewHolder(View parentView) {
        progressBar = (ProgressBar) parentView.findViewById(R.id.progressBar);
        nameView = (TextView) parentView.findViewById(R.id.nameView);
        purchaseDateView = (TextView) parentView.findViewById(R.id.purchaseDateView);
        visitsNumberView = (TextView) parentView.findViewById(R.id.visitsNumberView);
        showVisitsButton = (Button) parentView.findViewById(R.id.showVisitsButton);
        startNewVisitButton = (Button) parentView.findViewById(R.id.startNewVisitButton);
    }
}
