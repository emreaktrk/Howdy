package istanbul.codify.muudy.ui.around;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.helper.BlurBuilder;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.userprofile.UserProfileActivity;
import istanbul.codify.muudy.ui.users.UsersActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class AroundActivity extends MuudyActivity implements AroundView {

    private AroundPresenter mPresenter = new AroundPresenter();

    public static void start(@NonNull ArrayList<AroundUsers> around, Bitmap bitmap, Long id) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, AroundActivity.class);
        starter.putExtra(around.getClass().getSimpleName(), around);
        starter.putExtra(id.getClass().getSimpleName(),id);

     //   starter.putExtra("BitmapImage",bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        starter.putExtra("bitmapImage",byteArray);
        ActivityUtils.startActivity(starter);
    }



    @Override
    protected int getLayoutResId() {
        return R.layout.layout_around;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Long postId = getSerializable(Long.class);

        byte[] byteArray = getIntent().getByteArrayExtra("bitmapImage");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        if(bmp != null) {
            mPresenter.addBlurredBackground(bmp);
        }
        List<AroundUsers> around = (ArrayList<AroundUsers>) getSerializable(ArrayList.class);
        if (around != null) {
            mPresenter.getPostDetail(postId,around);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onPostLoaded(List<AroundUsers> around, Post post) {
        mPresenter.bind(around,post);
    }

    @Override
    public void onPostLoadedError(List<AroundUsers> around) {
        mPresenter.bind(around,null);
    }

    @Override
    public void onMoreClicked(ArrayList<User> users) {
        UsersActivity.start(users,false);
    }

    @Override
    public void onUserClicked(User user) {
        UserProfileActivity.start(user.iduser);
    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
