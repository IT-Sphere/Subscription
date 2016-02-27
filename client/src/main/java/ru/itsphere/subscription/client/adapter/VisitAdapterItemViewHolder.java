package ru.itsphere.subscription.client.adapter;

import android.view.View;
import android.widget.TextView;

import ru.itsphere.subscription.client.R;

public class VisitAdapterItemViewHolder {

    public final TextView startDateView;
    public final TextView endDateView;

    public VisitAdapterItemViewHolder(View parentView) {
        startDateView = (TextView) parentView.findViewById(R.id.startDateView);
        endDateView = (TextView) parentView.findViewById(R.id.endDateView);
    }
}
