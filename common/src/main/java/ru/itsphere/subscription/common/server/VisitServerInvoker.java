package ru.itsphere.subscription.common.server;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Query;
import ru.itsphere.subscription.domain.Visit;

/**
 * Responsible for actions related to the visits
 */
public interface VisitServerInvoker {
    @PUT("/visit")
    Call<Void> save(@Body Visit visit);

    @GET("/visit")
    Call<List<Visit>> list(@Query("subscriptionId") long subscriptionId);
}