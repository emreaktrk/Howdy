package istanbul.codify.muudy.ui.compose;

import android.net.Uri;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.Activity;
import istanbul.codify.muudy.model.Category;
import istanbul.codify.muudy.model.NewPost;
import istanbul.codify.muudy.model.Selectable;
import istanbul.codify.muudy.ui.base.MvpView;

import java.util.ArrayList;


interface ComposeView extends MvpView {

    void onSendClicked();

    void onCloseClicked();

    void onSearchClicked();

    void onLoaded(ArrayList<Category> categories, ArrayList<Activity> activities);

    void onLoaded(ArrayList<Category> filtered);

    void onLoaded(String sentence);

    void onLoaded(NewPost post);

    void onError(ApiError error);

    void onError(Exception exception);

    void onCategoryClicked(Category category);

    void onActivityClicked(Activity activity);

    void onSelectedRemoved(Selectable selected);

    void onPictureClicked();

    void onPhotoSelected(Uri uri);

    void onMediaCancelClicked();

}
