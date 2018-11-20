package istanbul.codify.monju.ui.photo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;

public final class PhotoActivity extends MuudyActivity implements PhotoView {

    private PhotoPresenter mPresenter = new PhotoPresenter();

    public static void start(@NonNull String url) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PhotoActivity.class);
        starter.putExtra(url.getClass().getSimpleName(), url);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull String url, Boolean isUri) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, PhotoActivity.class);
        starter.putExtra(url.getClass().getSimpleName(),url);
        starter.putExtra(isUri.getClass().getSimpleName(),isUri);
        ActivityUtils.startActivity(starter);
    }

    public static void start(@NonNull User user) {
        start(user.imgpath1);
    }

    public static void start(@NonNull Post post) {
        start(post.post_mediapath);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_photo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Boolean isUri = getSerializable(Boolean.class);
        if (isUri != null && isUri){
            String url = getSerializable(String.class);
            if (!StringUtils.isEmpty(url)) {
                Uri fileUri = Uri.parse(url);
                mPresenter.bind(fileUri);
            }

        }else{
            String url = getSerializable(String.class);
            if (!StringUtils.isEmpty(url)) {
                mPresenter.bind(url);
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onBackClicked() {
        onBackPressed();
    }
}
