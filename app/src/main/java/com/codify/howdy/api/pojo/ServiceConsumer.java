package com.codify.howdy.api.pojo;

import android.text.TextUtils;

import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.BaseResponse;

import io.reactivex.functions.BiConsumer;

public abstract class ServiceConsumer<T extends BaseResponse<?>> implements BiConsumer<T, Throwable> {

    @Override
    public void accept(T t, Throwable throwable) {
        if (throwable != null) {
            error(new ApiError(throwable.getMessage()));
            return;
        }

        if (!TextUtils.isEmpty(t.err) || !TextUtils.isEmpty(t.errMes)) {
            error(new ApiError(t.errMes));
            return;
        }

        success(t);
    }

    protected abstract void success(T response);

    protected abstract void error(ApiError error);

}
