package istanbul.codify.muudy.api.pojo;

import com.blankj.utilcode.util.StringUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.BaseResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class ServiceCallback<T extends BaseResponse<?>> implements Callback<T> {

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        T body = response.body();

        if (body != null) {
            if (StringUtils.isEmpty(body.err) || StringUtils.isEmpty(body.errMes)) {
                success(body);
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        ApiError error = new ApiError(throwable == null ? "Bir hata olustu" : throwable.getMessage());
        error(error);
    }

    protected abstract void success(T response);

    protected abstract void error(ApiError error);
}
