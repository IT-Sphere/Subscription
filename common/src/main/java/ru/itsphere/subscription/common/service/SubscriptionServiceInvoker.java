package ru.itsphere.subscription.common.service;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import ru.itsphere.subscription.domain.Subscription;

/**
 * Responsible for actions related to the subscriptions
 */
public interface SubscriptionServiceInvoker {
    @GET("/subscription/{clientId}")
    Call<List<Subscription>> list(@Path("clientId") long clientId);
}