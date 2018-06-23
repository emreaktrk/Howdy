package istanbul.codify.muudy.deeplink.action;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.CreatePostTextRequest;
import istanbul.codify.muudy.api.pojo.request.NewPostRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.CreateTextPostResponse;
import istanbul.codify.muudy.api.pojo.response.NewPostResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.Coordinate;
import istanbul.codify.muudy.model.Place;
import istanbul.codify.muudy.model.PostMediaType;
import istanbul.codify.muudy.model.PostVisibility;
import istanbul.codify.muudy.model.Selectable;
import istanbul.codify.muudy.model.VideoResult;
import istanbul.codify.muudy.model.event.ShareEvent;

public class ShareAction extends Action {

    public ShareAction(RemoteMessage message) {
        super(message);
    }

    @NonNull
    @Override
    protected String getAction() {
        return "Share";
    }

    @Override
    public void execute(Context context, Intent intent) {
        String placeName = getPlaceName(mMessage);

        Place place = new Place();
        place.place_name = placeName;

        List<Selectable> selecteds = new ArrayList<>();
        selecteds.add(place);

        CreatePostTextRequest request = new CreatePostTextRequest(selecteds);
        request.token = AccountUtils.tokenLegacy(context);

        ApiManager
                .getInstance()
                .createPostText(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ServiceConsumer<CreateTextPostResponse>() {
                    @Override
                    protected void success(CreateTextPostResponse response) {
                        ShareEvent event = new ShareEvent();
                        event.sentence = response.data;
                        event.visibility = PostVisibility.ALL;
                        post(event,selecteds,context);
                    }

                    @Override
                    protected void error(ApiError error) {
                        Logcat.e(error);

                    }
                });
        // TODO Request api
    }

    String getPlaceName(RemoteMessage message) {
        String placeName = "";
        placeName = new Gson().fromJson(message.getData().get("extraData"), JsonObject.class).get("palce_name").toString();
        if (placeName.contains("\"")) {
            placeName = placeName.replace("\"", "");
        }
        return placeName;
    }

    void post(ShareEvent event, List<Selectable> selecteds, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices
                .getFusedLocationProviderClient(context)
                .getLastLocation()
                .continueWith(task -> {
                    Location result = task.getResult();
                    if (result == null) {
                        Location location = new Location("default");
                        location.setLatitude(40.991955);
                        location.setLatitude(28.712913);
                        return location;
                    }

                    return result;
                })
                .addOnSuccessListener(location ->

                                Single
                                        .just(location)
                                        .subscribeOn(Schedulers.io())
                                        .flatMap((Function<Location, SingleSource<NewPostResponse>>) point -> {
                                            NewPostRequest request = new NewPostRequest(selecteds);
                                            request.token = AccountUtils.token(context);
                                            request.text = event.sentence;
                                            request.visibility = event.visibility;
                                            request.coordinates = Coordinate.from(location);
                                            request.mediaType = PostMediaType.NONE;
                                            request.mediaData = null;
                                            return ApiManager
                                                    .getInstance()
                                                    .newPost(request)
                                                    .observeOn(AndroidSchedulers.mainThread());
                                        })
                                        .subscribe(new ServiceConsumer<NewPostResponse>() {
                                            @Override
                                            protected void success(NewPostResponse response) {
                                                Logcat.v("New post created with id " + response.data.id);


                                            }

                                            @Override
                                            protected void error(ApiError error) {
                                                Logcat.e(error);


                                            }
                                        }))
                .addOnFailureListener(Logcat::e);
    }
}
