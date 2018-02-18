package com.codify.howdy.ui.search;

import android.support.v7.widget.AppCompatEditText;
import android.view.View;

import com.codify.howdy.R;
import com.codify.howdy.account.AccountUtils;
import com.codify.howdy.api.ApiManager;
import com.codify.howdy.api.pojo.ServiceConsumer;
import com.codify.howdy.api.pojo.request.SearchUserRequest;
import com.codify.howdy.api.pojo.response.ApiError;
import com.codify.howdy.api.pojo.response.SearchUserResponse;
import com.codify.howdy.logcat.Logcat;
import com.codify.howdy.model.User;
import com.codify.howdy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class UserSearchPresenter extends BasePresenter<UserSearchView> {

    @Override
    public void attachView(UserSearchView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_search_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.search_search))
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .skip(1)
                        .subscribe(word -> {
                            Logcat.v("User searched : " + findViewById(R.id.search_search, AppCompatEditText.class).getText().toString());
                            view.onUserSearched(word.toString());
                        }));
    }

    void search(String query) {
        SearchUserRequest request = new SearchUserRequest(query);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .searchUser(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SearchUserResponse>() {
                            @Override
                            protected void success(SearchUserResponse response) {
                                mView.onLoaded(response.data.result);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(ArrayList<User> users) {
        UserSearchAdapter adapter = new UserSearchAdapter(users);
    }
}
