package istanbul.codify.monju.ui.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.RegisterForm;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.RegisterLastStep.RegisterLastStepFragment;
import istanbul.codify.monju.ui.main.MainActivity;


public final class RegisterFragment extends MuudyFragment implements RegisterView {

    private RegisterPresenter mPresenter = new RegisterPresenter();

    public static RegisterFragment newInstance() {
        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_register;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    @Override
    public void onCloseClicked() {
        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        }
    }

    @Override
    public void onRegisterClicked(RegisterForm form) {

        if (getActivity() != null) {
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(android.R.id.content, RegisterLastStepFragment.newInstance(form))
                    .commit();
        }
    }

    @Override
    public void onRegister(User user) {
        AccountUtils.login(getContext(), user);

        MainActivity.start();
    }

    @Override
    public void onError(ApiError error) {
        ToastUtils.showShort(error.message);
    }
}
