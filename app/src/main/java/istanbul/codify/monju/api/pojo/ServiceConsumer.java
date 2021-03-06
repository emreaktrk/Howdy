package istanbul.codify.monju.api.pojo;

import com.blankj.utilcode.util.StringUtils;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.BaseResponse;

import io.reactivex.functions.BiConsumer;

public abstract class ServiceConsumer<T extends BaseResponse<?>> implements BiConsumer<T, Throwable> {

    @Override
    public void accept(T t, Throwable throwable) {
        if (throwable != null) {
            error(new ApiError(throwable.getMessage()));
            return;
        }

        if (!StringUtils.isEmpty(t.err) || !StringUtils.isEmpty(t.errMes)) {
            error(new ApiError(t.errMes));
            return;
        }

        success(t);
    }

    protected abstract void success(T response);

    protected abstract void error(ApiError error);

}
