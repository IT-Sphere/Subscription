package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.itsphere.subscription.client.R;
import ru.itsphere.subscription.client.VisitsListActivity;
import ru.itsphere.subscription.common.format.AppDateFormat;
import ru.itsphere.subscription.domain.Subscription;

public class SubscriptionAdapter extends ArrayAdapter<Subscription> {

    public SubscriptionAdapter(Context context, int textViewResourceId, int text, List<Subscription> items) {
        super(context, textViewResourceId, text, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_subscription, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            viewHolder.progressBar.setVisibility(View.INVISIBLE);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);
            viewHolder.purchaseDateView = (TextView) convertView.findViewById(R.id.purchaseDateView);
            viewHolder.visitsNumberView = (TextView) convertView.findViewById(R.id.visitsNumberView);
            viewHolder.showVisitsButton = (ImageButton) convertView.findViewById(R.id.showVisitsButton);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Subscription item = getItem(position);
        if (item != null) {
            viewHolder.nameView.setText(String.format("%s", item.getName()));
            viewHolder.purchaseDateView.setText(AppDateFormat.formatDateWithHoursAndMinutes(item.getCreationDate()));
            viewHolder.visitsNumberView.setText(String.valueOf(item.getVisitsNumber()));
            viewHolder.showVisitsButton.setOnClickListener(new ImageButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SubscriptionAdapter.this.getContext(), VisitsListActivity.class);
                    intent.putExtra(VisitsListActivity.PASSED_OBJECT_KEY, item.getVisits().toArray());
                    SubscriptionAdapter.this.getContext().startActivity(intent);
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        private ProgressBar progressBar;
        private TextView nameView;
        private TextView purchaseDateView;
        private TextView visitsNumberView;
        private ImageButton showVisitsButton;
    }
}
