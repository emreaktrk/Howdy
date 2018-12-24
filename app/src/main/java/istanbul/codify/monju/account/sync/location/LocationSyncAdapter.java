package istanbul.codify.monju.account.sync.location;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;

import android.content.SyncResult;
import android.location.Location;
import android.os.Bundle;



import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.Random;


import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceCallback;
import istanbul.codify.monju.api.pojo.request.GetRecommendedPlacesRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetRecommendedPlacesResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.utils.SharedPrefs;

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
                    if(SharedPrefs.getPlaceRecommendationPermissionStatus(getContext())){
                        if(SharedPrefs.getLocation(getContext()) == null){
                            recommendedPlaceRequest(getContext(),location);
                        }else{
                            if(location.distanceTo(SharedPrefs.getLocation(getContext()))>100){
                                long lastTime = SharedPrefs.getLastTimeLocationSent(getContext());
                                if(lastTime == 0 || differenceBeetwenDatesAsSeconds(Calendar.getInstance().getTimeInMillis(),lastTime) > 3600){
                                    recommendedPlaceRequest(getContext(),location);
                                }else{

                                }
                            }else{

                            }
                        }
                    }


                })
                .addOnFailureListener(Logcat::e);
    }


    private void recommendedPlaceRequest(Context context, Location location){
        GetRecommendedPlacesRequest request = new GetRecommendedPlacesRequest();
        request.token = AccountUtils.tokenLegacy(context);
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
                        SharedPrefs.setLastTimeLocationSent(Calendar.getInstance().getTimeInMillis(),context);
                        SharedPrefs.setLocation(location,context);
                    }

                    @Override
                    protected void error(ApiError error) {

                    }
                });
    }

    private long differenceBeetwenDatesAsSeconds(long date1, long date2){

        long second = (date1-date2)/1000;
        return second;

    }
}
