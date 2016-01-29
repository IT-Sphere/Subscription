package ru.itsphere.subscription.provider.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import ru.itsphere.subscription.common.format.AppDateFormat;
import ru.itsphere.subscription.domain.Client;
import ru.itsphere.subscription.domain.Subscription;
import ru.itsphere.subscription.provider.ProviderApplication;
import ru.itsphere.subscription.provider.R;

public class SubscribersAdapter extends ArrayAdapter<Subscription> {

    private static final String tag = SubscribersAdapter.class.getName();
    private final ProviderApplication context;

    public SubscribersAdapter(Context context, int textViewResourceId, int text, List<Subscription> items) {
        super(context, textViewResourceId, text, items);
        this.context = (ProviderApplication) context.getApplicationContext();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.listview_subscription, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.clientNameView = (TextView) convertView.findViewById(R.id.clientNameView);
            viewHolder.purchaseDateView = (TextView) convertView.findViewById(R.id.purchaseDateView);
            viewHolder.visitsNumberView = (TextView) convertView.findViewById(R.id.visitsNumberView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Subscription item = getItem(position);
        if (item != null) {
            viewHolder.visitsNumberView.setText(String.valueOf(item.getVisitsNumber()));
            viewHolder.purchaseDateView.setText(AppDateFormat.formatDateWithHoursAndMinutes(item.getCreationDate()));
            Call<Client> call = context.getServer().getClientById(item.getClientId());
            call.enqueue(new Callback<Client>() {
                @Override
                public void onResponse(Response<Client> response, Retrofit retrofit) {
                    Log.i(tag, "Getting client by id from server stopped.");
                    viewHolder.clientNameView.setText(response.body().getFirstName());
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(tag, String.format("getClientById (id: %d) has thrown: ", item.getClientId()), t);
                }
            });
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView clientNameView;
        private TextView purchaseDateView;
        private TextView visitsNumberView;
    }
}
