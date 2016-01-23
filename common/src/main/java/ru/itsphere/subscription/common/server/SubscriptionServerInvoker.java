package ru.itsphere.subscription.common.server;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for actions related to the subscriptions
 */
public interface SubscriptionServerInvoker {
    @GET("/subscription")
    Call<List<Subscription>> list(@Query("clientId") long clientId);

    @PUT("/subscription")
    Call<Void> save(@Body Subscription subscription);

    @PUT("/subscription")
    Call<Void> update(@Body Subscription subscription);
}