package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.itsphere.subscription.client.R;
import ru.itsphere.subscription.common.format.AppDateFormat;
import ru.itsphere.subscription.domain.Subscription;

public class SubscriptionAdapter extends ArrayAdapter<Subscription> {

    private Context context;

    public SubscriptionAdapter(Context context, int textViewResourceId, int text, List<Subscription> items) {
        super(context, textViewResourceId, text, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        context = getContext();
        final SubscriptionAdapterItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_subscription, parent, false);
            viewHolder = new SubscriptionAdapterItemViewHolder(convertView);
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SubscriptionAdapterItemViewHolder) convertView.getTag();
        }

        final Subscription item = getItem(position);
        if (item != null) {
            viewHolder.nameView.setText(item.getName());
            viewHolder.purchaseDateView.setText(item.getCreationDate() == null ? "" : AppDateFormat.formatDateWithHoursAndMinutes(item.getCreationDate()));
            viewHolder.visitsNumberView.setText(item.getVisits().size() + "/" + item.getVisitsNumber());
            setShowSubscriptionVisitsButton(viewHolder, item);
            setStartNewVisitButton(viewHolder, item);
        }

        return convertView;
    }

    private void setShowSubscriptionVisitsButton(SubscriptionAdapterItemViewHolder viewHolder, Subscription item) {
        ShowSubscriptionVisitsButton showVisitsButton = new ShowSubscriptionVisitsButton(context, item);
        viewHolder.showVisitsButton.setOnClickListener(showVisitsButton);
    }

    private void setStartNewVisitButton(SubscriptionAdapterItemViewHolder viewHolder, Subscription item) {
        StartNewVisitButton newVisitButton = new StartNewVisitButton(context, item, viewHolder);
        viewHolder.startNewVisitButton.setOnClickListener(newVisitButton);
    }
}
