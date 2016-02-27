package ru.itsphere.subscription.client.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import ru.itsphere.subscription.client.ClientApplication;
import ru.itsphere.subscription.client.VisitsListActivity;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Button for displaying all visits
 */
class ShowSubscriptionVisitsButton implements View.OnClickListener {
    private final Context context;
    private Subscription relatedSubscription;

    public ShowSubscriptionVisitsButton(Context context, Subscription relatedSubscription) {
        this.context = context;
        this.relatedSubscription = relatedSubscription;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, VisitsListActivity.class);
        intent.putExtra(VisitsListActivity.PASSED_OBJECT_KEY, relatedSubscription.getVisits().toArray());
        context.startActivity(intent);
    }
}
