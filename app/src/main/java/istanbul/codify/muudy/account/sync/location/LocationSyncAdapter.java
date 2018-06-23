package istanbul.codify.muudy.account.sync.location;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import com.google.android.gms.location.LocationServices;

import java.util.Random;

import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceCallback;
import istanbul.codify.muudy.api.pojo.request.GetRecommendedPlacesRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetRecommendedPlacesResponse;
import istanbul.codify.muudy.logcat.Logcat;

public final class LocationSyncAdapter extends AbstractThreadedSyncAdapter {

    public LocationSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Logcat.v("Performing sync");

        LocationServices
                .getFusedLocationProviderClient(getContext())
                .getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location == null) {
                        return;
                    }

                    GetRecommendedPlacesRequest request = new GetRecommendedPlacesRequest();
                    request.token = AccountUtils.tokenLegacy(getContext());
                    request.lat = location.getLatitude();
                    request.lng = location.getLongitude();
                    //request.sendPush = 1;
                    request.sendPush = new Random().nextInt(4);

                    ApiManager
                            .getInstance()
                            .getRecommendedPlaces(request)
                            .enqueue(new ServiceCallback<GetRecommendedPlacesResponse>() {
                                @Override
                                protected void success(GetRecommendedPlacesResponse response) {

                                }

                                @Override
                                protected void error(ApiError error) {

                                }
                            });
                })
                .addOnFailureListener(Logcat::e);
    }
}
