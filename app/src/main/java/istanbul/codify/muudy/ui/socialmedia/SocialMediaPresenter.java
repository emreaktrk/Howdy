package istanbul.codify.muudy.ui.socialmedia;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.UpdateProfileRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.UpdateProfileResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

final class SocialMediaPresenter extends BasePresenter<SocialMediaView> {

    @Override
    public void attachView(SocialMediaView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.social_media_back))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onBackClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.social_media_save))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Save clicked");

                            view.onSaveClicked();
                        }));
    }

    void bind(User me){
        findViewById(R.id.social_media_facebook, TextInputEditText.class).setText(me.facebooklink);
        findViewById(R.id.social_media_twitter, TextInputEditText.class).setText(me.twitterlink);
        findViewById(R.id.social_media_instagram, TextInputEditText.class).setText(me.instagramlink);
    }

    void save() {
        String facebook = findViewById(R.id.social_media_facebook, TextInputEditText.class).getText().toString().trim();
        String twitter = findViewById(R.id.social_media_twitter, TextInputEditText.class).getText().toString().trim();
        String instagram = findViewById(R.id.social_media_instagram, TextInputEditText.class).getText().toString().trim();

        UpdateProfileRequest request = new UpdateProfileRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.facebooklink = facebook;
        request.twitterlink = twitter;
        request.instagramlink = instagram;

        mDisposables
                .add(
                        ApiManager
                                .getInstance()
                                .updateProfile(request)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new ServiceConsumer<UpdateProfileResponse>() {
                                    @Override
                                    protected void success(UpdateProfileResponse response) {
                                        Logcat.v("Social media updated");

                                        mView.onLoaded();
                                    }

                                    @Override
                                    protected void error(ApiError error) {
                                        Logcat.e(error);

                                        mView.onError(error);
                                    }
                                })
                );
    }
}
