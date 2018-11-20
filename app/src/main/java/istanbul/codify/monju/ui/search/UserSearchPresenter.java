package istanbul.codify.monju.ui.search;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.GetFollowedUsersRequest;
import istanbul.codify.monju.api.pojo.request.SearchUserRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetFollowedUsersResponse;
import istanbul.codify.monju.api.pojo.response.SearchUserResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.FollowState;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.ui.base.BasePresenter;

import java.util.List;
import java.util.concurrent.TimeUnit;

final class UserSearchPresenter extends BasePresenter<UserSearchView> {

    @SuppressWarnings("ConstantConditions")
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

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.user_search_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.user_search_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
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

    void bind(List<User> users) {
        UserSearchAdapter adapter = new UserSearchAdapter(users);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Logcat.v("User clicked");

                            mView.onUserClicked(user);
                        }));
        findViewById(R.id.user_search_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void getFollowedUsers(Long userId) {
        GetFollowedUsersRequest request = new GetFollowedUsersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = userId;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getFollowedUsers(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetFollowedUsersResponse>() {
                            @Override
                            protected void success(GetFollowedUsersResponse response) {
                                for (User user : response.data) {
                                    user.isfollowing = FollowState.FOLLOWING;
                                }

                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }
}
