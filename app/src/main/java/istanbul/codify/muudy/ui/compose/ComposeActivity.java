package istanbul.codify.muudy.ui.compose;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import istanbul.codify.muudy.EventSupport;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.analytics.Analytics;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.deeplink.DeepLinkManager;
import istanbul.codify.muudy.deeplink.PlaceRecommendationLink;
import istanbul.codify.muudy.helper.BlurBuilder;
import istanbul.codify.muudy.model.*;
import istanbul.codify.muudy.model.event.PostEvent;
import istanbul.codify.muudy.model.event.SeasonSelectionEvent;
import istanbul.codify.muudy.model.event.ShareEvent;
import istanbul.codify.muudy.model.event.TwitterShareEvent;
import istanbul.codify.muudy.ui.compose.dialog.ChooseSeasonDialog;
import istanbul.codify.muudy.ui.compose.dialog.ComposeDialog;
import istanbul.codify.muudy.ui.media.MediaBottomSheet;
import istanbul.codify.muudy.ui.places.PlacesActivity;
import istanbul.codify.muudy.ui.word.WordActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public final class ComposeActivity extends MuudyActivity implements ComposeView, EventSupport {

    private final ComposePresenter mPresenter = new ComposePresenter();
    private static final int PICK_IMAGE_FROM_GALLERY_ACTIVITY_REQUEST_CODE = 333;
    public static final int TWEET_COMPOSER_REQUEST_CODE = 14141;
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

    CallbackManager callbackManager;
    ShareDialog shareDialog;


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


        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

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
    public void onLoaded(String sentence, Bitmap bitmap, Bitmap selectedImage) {
        ComposeDialog
                .newInstance(sentence,bitmap,selectedImage)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeasonSelectionEvent(SeasonSelectionEvent event){
        if(event.selectedSeasonAndEpisode != null) {
            mPresenter.onSeasonSelect(event);
        }else{
            mPresenter.removeSeries();
            mPresenter.getWordsWithFilter();
        }
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

        if(mPresenter.getMediaType() == PostMediaType.NONE){
            MediaBottomSheet
                    .newInstance()
                    .setOnCameraClickListener(() -> mPresenter.capturePhoto(ComposeActivity.this))
                    .setOnGalleryClickListener(() -> mPresenter.selectPhoto(ComposeActivity.this))
                    .setOnVideoClickListener(() -> mPresenter.captureVideo(ComposeActivity.this))
                    .show(getSupportFragmentManager(), null);
        }else if (mPresenter.getMediaType() == PostMediaType.IMAGE){
            mPresenter.mediaClick();
        }else if (mPresenter.getMediaType() == PostMediaType.VIDEO){
            mPresenter.mediaClick();
        }

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

    @Override
    public void onSelectMedia() {

    }

    @Override
    public void openSeasonSelection(Bitmap bitmap) {
        ChooseSeasonDialog.newInstance(bitmap)
                .show(getSupportFragmentManager(), null);

    }

    @Override
    public void onOpenGallery() {
        Dexter
                .withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            Intent openGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            startActivityForResult(openGallery, PICK_IMAGE_FROM_GALLERY_ACTIVITY_REQUEST_CODE);

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        // Empty block
                    }
                })
                .check();

    }

    @Override
    public void onFacebookShare(NewPost newPostResponse, boolean isTwitterSelected, String link) {
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                mPresenter.facebookShareCompleted(newPostResponse,isTwitterSelected,true, link);

            }

            @Override
            public void onCancel() {
                mPresenter.facebookShareCompleted(newPostResponse,isTwitterSelected,false, link);
            }

            @Override
            public void onError(FacebookException error) {
                mPresenter.facebookShareCompleted(newPostResponse,isTwitterSelected,false, link);
            }
        });

        if (ShareDialog.canShow(ShareLinkContent.class)) {

            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse(link))
                    .build();
            shareDialog = new ShareDialog(this);

            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
        }


    }

    @Override
    public void onTwitterShare(NewPost newPostResponse, String link) {
        try {
            URL url = new URL(link);
            Intent intent = new TweetComposer.Builder(this)
                    .url(url)
                    .createIntent();
            startActivityForResult(intent,TWEET_COMPOSER_REQUEST_CODE);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    @SuppressWarnings({"unchecked", "UnnecessaryReturnStatement"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Word word = resolveResult(requestCode, resultCode, data, Word.class, WordActivity.REQUEST_CODE);
        if (word != null) {
            mPresenter.addSelected(word);
            if (word.words_top_category_id == 20){
                mPresenter.openSeasonSelection();

            }
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

        if (requestCode == PICK_IMAGE_FROM_GALLERY_ACTIVITY_REQUEST_CODE) {
            Uri photo = data.getData();
            mPresenter.bindGalleryPhoto(photo);
        }

        if (requestCode == TWEET_COMPOSER_REQUEST_CODE) {
            mPresenter.twitterShareCompleted(mPresenter.newPost);
        }

        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
