package istanbul.codify.muudy.ui.RegisterLastStep;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.RegisterForm;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.main.MainActivity;
import istanbul.codify.muudy.ui.register.RegisterFragment;


public class RegisterLastStepFragment extends MuudyFragment implements RegisterLastStepView {


    RegisterLastStepPresenter mPresenter = new RegisterLastStepPresenter();

    public RegisterLastStepFragment() {
        // Required empty public constructor
    }

    public static RegisterLastStepFragment newInstance(RegisterForm form) {
        Bundle args = new Bundle();
        args.putSerializable(form.getClass().getSimpleName(),form);
        RegisterLastStepFragment fragment = new RegisterLastStepFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_register_last_step;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this,view);
        RegisterForm form;
        form = getSerializable(RegisterForm.class);
        if (form != null){
            mPresenter.bind(form);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onCloseClicked() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onRegisterClicked(RegisterForm form) {
        mPresenter.register(form);
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
