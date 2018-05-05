package istanbul.codify.muudy.ui.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import istanbul.codify.muudy.MuudyActivity;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Web;

public final class WebActivity extends MuudyActivity implements WebView {

    private WebPresenter mPresenter = new WebPresenter();

    public static void start(@NonNull Web web) {
        Context context = Utils.getApp().getApplicationContext();

        Intent starter = new Intent(context, WebActivity.class);
        starter.putExtra(web.getClass().getSimpleName(), web);
        context.startActivity(starter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_web;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.attachView(this, this);

        Web web = getSerializable(Web.class);
        if (web != null) {
            mPresenter.bind(web);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPresenter.detachView();
    }

    @Override
    public void onSSLError(SslErrorHandler handler) {
        new AlertDialog
                .Builder(WebActivity.this)
                .setMessage("Devam etmek için güvenlik sertifikasını onaylayınız.")
                .setPositiveButton("Kabul Et", (dialog, which) -> handler.proceed())
                .setNegativeButton("İptal", (dialog, which) -> handler.cancel())
                .create()
                .show();
    }

    @Override
    public void onCloseClicked() {
        finish();
    }
}