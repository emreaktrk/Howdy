package istanbul.codify.monju.ui.RegisterLastStep;

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
import istanbul.codify.monju.ui.main.MainActivity;


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
