package istanbul.codify.monju.ui.compose;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Activity;
import istanbul.codify.monju.model.Category;
import istanbul.codify.monju.model.NewPost;
import istanbul.codify.monju.model.Selectable;
import istanbul.codify.monju.ui.base.MvpView;

import java.util.ArrayList;


interface ComposeView extends MvpView {

    void onSendClicked();

    void onCloseClicked();

    void onSearchClicked();

    void onLoaded(ArrayList<Category> categories, ArrayList<Activity> activities);

    void onLoaded(ArrayList<Category> filtered);

    void onLoaded(String sentence, Bitmap bitmap, Bitmap selectedImage, @Nullable String emoji);

    void onLoaded(NewPost post);

    void onError(ApiError error);

    void onError(Exception exception);

    void onCategoryClicked(Category category);

    void onActivityClicked(Activity activity);

    void onSelectedRemoved(Selectable selected);

    void onPictureClicked();

    void onPhotoSelected(Uri uri);

    void onMediaCancelClicked();

    void onGalleryPhotoSelected(Uri uri);

    void onSelectMedia();

    void openSeasonSelection(Bitmap bitmap);

    void onOpenGallery();

    void onFacebookShare(NewPost newPostResponse, boolean isTwitterSelected, String link);

    void onTwitterShare(NewPost newPostResponse, String link);

}
