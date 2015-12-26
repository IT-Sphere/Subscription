package ru.itsphere.subscription.common.service;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import ru.itsphere.subscription.domain.Client;

/**
 * Responsible for actions related to the subscriptions
 */
public interface RegistrationServiceInvoker {
    @PUT("/client")
    Call<Void> register(@Body Client client);

    @GET("/client")
    Call<List<Client>> list();

    @GET("/client/{clientId}")
    Call<Client> get(@Path("clientId") long clientId);
}
