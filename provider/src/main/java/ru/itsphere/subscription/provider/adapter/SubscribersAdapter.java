package ru.itsphere.subscription.provider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.provider.R;

public class SubscribersAdapter extends ArrayAdapter<Subscription> {

    public SubscribersAdapter(Context context, int textViewResourceId, int text, List<Subscription> items) {
        super(context, textViewResourceId, text, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_subscription, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.nameView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subscription item = getItem(position);
        if (item != null) {
            viewHolder.nameView.setText(String.format("%s", item.getName()));
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView nameView;
    }
}
