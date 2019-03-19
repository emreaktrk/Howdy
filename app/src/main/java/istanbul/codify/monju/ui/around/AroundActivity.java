package istanbul.codify.monju.ui.around;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.monju.MuudyActivity;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.AroundUsers;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.userprofile.UserProfileActivity;
import istanbul.codify.monju.ui.users.UsersActivity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class AroundActivity extends MuudyActivity implements AroundView {

    private AroundPresenter mPresenter = new AroundPresenter();

    public static void start(@NonNull ArrayList<AroundUsers> around, Bitmap bitmap, long id) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, AroundActivity.class);
        starter.putExtra(around.getClass().getSimpleName(), around);

           // starter.putExtra(id.getClass().getSimpleName(), id);

        starter.putExtra(Long.class.getSimpleName(), id);

     //   starter.putExtra("BitmapImage",bitmap);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        starter.putExtra("bitmapImage",byteArray);
        ActivityUtils.startActivity(starter,android.R.anim.fade_in,android.R.anim.fade_out);

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
        if (around != null && postId != null) {
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
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
