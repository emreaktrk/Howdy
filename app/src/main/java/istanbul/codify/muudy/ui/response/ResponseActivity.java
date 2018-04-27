package istanbul.codify.muudy.ui.response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;

public final class ResponseActivity extends MuudyActivity implements ResponseView {

    private ResponsePresenter mPresenter = new ResponsePresenter();

    public static void start() {
        Context context = Utils.getApp().getApplicationContext();
        Intent starter = new Intent(context, ResponseActivity.class);
        ActivityUtils.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);
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

    }

    @Override
    public void onSendClicked() {

    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}
