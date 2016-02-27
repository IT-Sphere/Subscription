package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.itsphere.subscription.client.R;
import ru.itsphere.subscription.common.format.AppDateFormat;
import ru.itsphere.subscription.domain.Visit;

public class VisitAdapter extends ArrayAdapter<Visit> {

    public VisitAdapter(Context context, int textViewResourceId, int text, List<Visit> items) {
        super(context, textViewResourceId, text, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        VisitAdapterItemViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_visit, parent, false);

            viewHolder = new VisitAdapterItemViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VisitAdapterItemViewHolder) convertView.getTag();
        }

        Visit item = getItem(position);
        if (item != null) {
            viewHolder.startDateView.setText(
                    item.getStartDate() == null ? "in progress..."
                            : AppDateFormat.formatDateWithHoursAndMinutes(item.getStartDate())
            );
            viewHolder.endDateView.setText(
                    item.getEndDate() == null ? "in progress..."
                            : AppDateFormat.formatDateWithHoursAndMinutes(item.getEndDate())
            );
        }

        return convertView;
    }
}
