package ru.itsphere.subscription.common.server;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.PUT;
import retrofit.http.Path;
import ru.itsphere.subscription.domain.Organization;

/**
 * Responsible for actions related to the organizations
 */
public interface OrganizationServerInvoker {
    @PUT("/organization")
    Call<Void> save(@Body Organization organization);

    @GET("/organization")
    Call<List<Organization>> list();

    @GET("/organization/{organizationId}")
    Call<Organization> get(@Path("organizationId") long organizationId);
}
