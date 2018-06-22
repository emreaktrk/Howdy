package istanbul.codify.muudy.ui.compose;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.PlaceRecommendationLink;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.PostEvent;
import istanbul.codify.muudy.model.event.ShareEvent;
import istanbul.codify.muudy.ui.compose.dialog.ComposeDialog;
import istanbul.codify.muudy.ui.media.MediaBottomSheet;
import istanbul.codify.muudy.ui.places.PlacesActivity;
import istanbul.codify.muudy.ui.word.WordActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public final class ComposeActivity extends MuudyActivity implements ComposeView, EventSupport {

    private final ComposePresenter mPresenter = new ComposePresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ComposeActivity.class);
        ActivityUtils.startActivity(starter);
    }

    public static void start(Word word) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ComposeActivity.class);
        starter.putExtra(Word.class.getSimpleName(), word);
        ActivityUtils.startActivity(starter);
    }

    public static void start(Place place) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, ComposeActivity.class);
        starter.putExtra(Place.class.getSimpleName(), place);
        ActivityUtils.startActivity(starter);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.layout_compose;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);



        Word word = getSerializable(Word.class);
        if (word != null){
            mPresenter.addSelected(word);
        }

        Place place = getSerializable(Place.class);
        if (place != null){
            mPresenter.addSelected(place);
        }

        mPresenter.getWordsWithFilter();

        DeepLinkManager
                .getInstance()
                .nullifyIf(PlaceRecommendationLink.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSendClicked() {
        Analytics
                .getInstance()
                .custom(Analytics.Events.COMPOSE);

        mPresenter.createTextPost();
    }

    @Override
    public void onCloseClicked() {
        finish();
    }

    @Override
    public void onSearchClicked() {
        WordActivity.start(mPresenter.getSelecteds(), ResultTo.ACTIVITY);
    }

    @Override
    public void onLoaded(ArrayList<Category> categories, ArrayList<Activity> activities) {
        mPresenter.bind(categories, activities);
    }

    @Override
    public void onLoaded(ArrayList<Category> filtered) {
        mPresenter.bind(filtered);
    }

    @Override
    public void onLoaded(String sentence) {
        ComposeDialog
                .newInstance(sentence)
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onLoaded(NewPost post) {

        PostEvent event = new PostEvent(post);

        EventBus
                .getDefault()
                .post(event);
        finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareEvent(ShareEvent event) {
        mPresenter.post(event);
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
    public void onCategoryClicked(Category category) {
        if (category.isLocation()) {
            PlacesActivity.start(ResultTo.ACTIVITY);
        } else {
            WordActivity.start(ResultTo.ACTIVITY, category);
        }
    }

    @Override
    public void onActivityClicked(Activity activity) {
        mPresenter.addSelected(activity);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onSelectedRemoved(Selectable selected) {
        mPresenter.removeSelected(selected);
        mPresenter.getWordsWithFilter();
    }

    @Override
    public void onPictureClicked() {
        MediaBottomSheet
                .newInstance()
                .setOnCameraClickListener(() -> mPresenter.capturePhoto(ComposeActivity.this))
                .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ComposeActivity.this))
                .setOnVideoClickListener(() -> mPresenter.captureVideo(ComposeActivity.this))
                .show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPhotoSelected(Uri uri) {
        mPresenter.bindPhoto(uri);
    }

    @Override
    public void onMediaCancelClicked() {
        mPresenter.cancel();
    }

    @Override
    public void onGalleryPhotoSelected(Uri uri) {
        mPresenter.bindGalleryPhoto(uri);
    }

    @SuppressWarnings({"unchecked", "UnnecessaryReturnStatement"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Word word = resolveResult(requestCode, resultCode, data, Word.class, WordActivity.REQUEST_CODE);
        if (word != null) {
            mPresenter.addSelected(word);
            mPresenter.getWordsWithFilter();
            return;
        }

        ArrayList<User> users = resolveResult(requestCode, resultCode, data, ArrayList.class, WordActivity.REQUEST_CODE);
        if (users != null) {
            mPresenter.addSelected(users);
            mPresenter.getWordsWithFilter();
            return;
        }

        Place place = resolveResult(requestCode, resultCode, data, Place.class, PlacesActivity.REQUEST_CODE);
        if (place != null) {
            mPresenter.addSelected(place);
            mPresenter.getWordsWithFilter();
            return;
        }

        Uri video = mPresenter.resolveVideo(requestCode, resultCode, data);
        if (video != null) {
            mPresenter.bindVideo(video);
            return;
        }
    }
}
