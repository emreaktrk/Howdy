package istanbul.codify.muudy.ui.response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetUserProfileRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetUserProfileResponse;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.SayHiLink;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.ui.word.WordActivity;

public final class ResponseActivity extends MuudyActivity implements ResponseView {

    private ResponsePresenter mPresenter = new ResponsePresenter();

    public static void start(@NonNull Notification notification) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ResponseActivity.class);
        starter.putExtra(notification.getClass().getSimpleName(), notification);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull Long userId, String notificationMessage) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ResponseActivity.class);
        starter.putExtra(userId.getClass().getSimpleName(), userId);
        starter.putExtra(notificationMessage.getClass().getSimpleName(), notificationMessage);
        ActivityUtils.startActivity(starter);
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Notification notification = getSerializable(Notification.class);
        if (notification != null) {
            mPresenter.bind(notification);
        } else {
            Long userId = getSerializable(Long.class);
            if (userId != null) {
                String notificationMessage = getSerializable(String.class);
                mPresenter.getUserProfile(userId, notificationMessage);
            }
        }

        DeepLinkManager
                .getInstance()
                .nullifyIf(SayHiLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_response;
    }

    @Override
    public void onWordSelectClicked() {
        WordActivity.start(ResultTo.ACTIVITY, Category.EMOTION);
    }

    @Override
    public void onLoaded(User user, String text) {
        mPresenter.bind(user,text);
    }

    @Override
    public void onSendClicked() {
        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.answerHi(user.iduser);
        }
    }

    @Override
    public void onSent() {
        finish();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onError(Exception exception) {
        ToastUtils.showShort(exception.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Word word = resolveResult(requestCode, resultCode, data, Word.class, WordActivity.REQUEST_CODE);
        if (word != null) {
            mPresenter.bind(word);
        }
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onProfilePhotoClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }
}
