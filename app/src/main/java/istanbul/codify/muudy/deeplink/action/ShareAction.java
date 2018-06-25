package istanbul.codify.muudy.deeplink.action;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.ShareEvent;

import java.util.ArrayList;
import java.util.List;

public class ShareAction extends Action {

    public ShareAction() {
        // Empty block
    }

    public ShareAction(RemoteMessage message) {
        super(message);
    }

    @NonNull
    @Override
    protected String getAction() {
        return "Share";
    }

    @SuppressLint("CheckResult")
    @Override
    public void execute(Context context, RemoteMessage message) {
        String placeName = getPlaceName(message);

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
                        post(event, selecteds, context);
                    }

                    @Override
                    protected void error(ApiError error) {
                        Logcat.e(error);

                    }
                });
        // TODO Request api
    }

    void post(ShareEvent event, List<Selectable> selecteds, Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private String getPlaceName(RemoteMessage message) {
        String placeName = "";
        placeName = new Gson().fromJson(message.getData().get("extraData"), JsonObject.class).get("palce_name").toString();
        if (placeName.contains("\"")) {
            placeName = placeName.replace("\"", "");
        }
        return placeName;
    }
}
