package istanbul.codify.muudy.ui.mention;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.GetFollowedUsersRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.GetFollowedUsersResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

final class MentionPresenter extends BasePresenter<MentionView> {

    private ArrayList<User> mSelecteds = new ArrayList<>();

    @Override
    public void attachView(MentionView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.mention_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxTextView
                        .textChanges(findViewById(R.id.mention_search))
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .skip(1)
                        .subscribe(query -> {
                            Logcat.v("User searched : " + query);

                            view.onUserSearched(query.toString());
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.mention_done))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Done clicked");

                            view.onDoneClicked(mSelecteds);
                        }));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.mention_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.mention_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void search(String query) {
        // TODO Local search
    }

    void bind(List<User> users) {
        MentionAdapter adapter = new MentionAdapter(users);
        mDisposables.add(
                adapter
                        .mentionClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mention -> {
                            Logcat.v("Mention clicked");

                            mView.onMentionClicked(mention);
                        }));
        findViewById(R.id.mention_recycler, RecyclerView.class).setAdapter(adapter);
    }

    void add(User user) {
        if (!mSelecteds.contains(user)) {
            mSelecteds.add(user);
        }
    }

    void remove(User user) {
        if (mSelecteds.contains(user)) {
            mSelecteds.remove(user);
        }
    }

    ArrayList<User> getSelecteds() {
        return mSelecteds;
    }

    void getFollowedUsers() {
        GetFollowedUsersRequest request = new GetFollowedUsersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.userId = AccountUtils.me(getContext()).iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getFollowedUsers(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetFollowedUsersResponse>() {
                            @Override
                            protected void success(GetFollowedUsersResponse response) {
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
