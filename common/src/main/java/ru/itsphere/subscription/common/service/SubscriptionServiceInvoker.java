package ru.itsphere.subscription.common.service;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for actions related to the subscriptions
 */
public interface SubscriptionServiceInvoker {
    @GET("/subscription/list")
    Call<List<Subscription>> list(@Query("clientId") long clientId);
}