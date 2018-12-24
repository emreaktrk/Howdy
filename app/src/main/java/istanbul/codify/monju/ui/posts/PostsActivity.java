package istanbul.codify.monju.ui.posts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.analytics.Analytics;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.Activity;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.model.UsersScreenMode;
import istanbul.codify.monju.model.event.DeleteEvent;
import istanbul.codify.monju.model.event.OwnProfileEvent;
import istanbul.codify.monju.model.zipper.Like;
import istanbul.codify.monju.ui.home.PostAdapter;
import istanbul.codify.monju.ui.photo.PhotoActivity;
import istanbul.codify.monju.ui.postdetail.PostDetailActivity;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import istanbul.codify.monju.ui.users.UsersActivity;
import istanbul.codify.monju.ui.video.VideoActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public final class PostsActivity extends MuudyActivity implements PostsView {

    private PostsPresenter mPresenter = new PostsPresenter();

    public static void start(@NonNull Activity activity) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PostsActivity.class);
        starter.putExtra(activity.getClass().getSimpleName(), activity);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull Activity activity, @Nullable String word) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PostsActivity.class);
        starter.putExtra(activity.getClass().getSimpleName(), activity);
        if (word != null) {
            starter.putExtra(word.getClass().getSimpleName(), word);
        }
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull ArrayList<Post> posts) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PostsActivity.class);
        starter.putExtra(posts.getClass().getSimpleName(), posts);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_posts;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Activity activity = getSerializable(Activity.class);
        String word = getSerializable(String.class);
        ArrayList<Post> posts = getSerializable(ArrayList.class);
        if (activity != null) {
            mPresenter.getPosts(activity, word);
            return;
        }

        if (posts != null) {
            mPresenter.bind(posts);
            mPresenter.setTitle("Begenilen Paylasimlar");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onLoaded(List<Post> posts) {
        mPresenter.bind(posts);
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }

    @Override
    public void onPostClicked(Post post) {
        PostDetailActivity.start(post.idpost);
    }

    @Override
    public void onLikeClicked(Like like) {
        if (like.isChecked) {
            mPresenter.like(like.post.idpost);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.LIKE);
        } else {
            mPresenter.dislike(like.post.idpost);

            Analytics
                    .getInstance()
                    .custom(Analytics.Events.DISLIKE);
        }
    }

    @Override
    public void onVideoClicked(Post post) {
        VideoActivity.start(post);
    }

    @Override
    public void onImageClicked(Post post) {
        PhotoActivity.start(post);
    }

    @Override
    public void onAvatarClicked(Post post) {
        boolean own = AccountUtils.own(this, post.iduser);
        if (own) {
            EventBus
                    .getDefault()
                    .post(new OwnProfileEvent());
        } else {
            UserProfileActivity.start(post.iduser);
        }
    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }

    @Override
    public void onDeleteClicked(Post post,PostAdapter adapter) {
        new AlertDialog
                .Builder(this)
                .setMessage("Silmek istiyor musunuz?")
                .setPositiveButton("Evet", (dialogInterface, which) -> {
                    mPresenter.delete(post,adapter);

                    Analytics
                            .getInstance()
                            .custom(Analytics.Events.DELETE_POST);
                })
                .setNegativeButton("Hayir", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onMuudyClicked(Post post) {
        new AlertDialog
                .Builder(this)
                .setMessage("Monju! demek istiyor musunuz?")
                .setPositiveButton("Monju!", (dialogInterface, which) -> {
                    mPresenter.sayHi(post.iduser);

                    Analytics
                            .getInstance()
                            .custom(Analytics.Events.MUUDY);
                })
                .setNegativeButton("Hayir", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    @Override
    public void onLikeCountClicked(Post post) {
        UsersActivity.start(UsersScreenMode.LIKERS, post.idpost);
    }

    @Override
    public void onPostDeleted() {
        EventBus
                .getDefault()
                .post(new DeleteEvent());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteEvent(DeleteEvent event) {

        String word = getSerializable(String.class);
        Activity activity = getSerializable(Activity.class);
        mPresenter.getPosts(activity,word);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
