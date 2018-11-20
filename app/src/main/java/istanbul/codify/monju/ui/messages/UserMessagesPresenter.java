package istanbul.codify.monju.ui.messages;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.DeleteUserMessagesRequest;
import istanbul.codify.monju.api.pojo.request.GetUserChatWallRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.DeleteUserMessagesResponse;
import istanbul.codify.monju.api.pojo.response.GetUserChatWallResponse;
import istanbul.codify.monju.helper.OnSwipeDialogCallback;
import istanbul.codify.monju.helper.RecyclerViewHelper;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.UserMessage;
import istanbul.codify.monju.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class UserMessagesPresenter extends BasePresenter<UserMessagesView> {

    RecyclerViewHelper recyclerViewHelper;

    @Override
    public void attachView(UserMessagesView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_messages_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.user_messages_new))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("New clicked");
                            view.onNewClicked();
                        }));

        DividerItemDecoration divider = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.background_divider));
        findViewById(R.id.user_search_recycler, RecyclerView.class).addItemDecoration(divider);
        findViewById(R.id.user_search_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));
    }

    void getMessages() {
        GetUserChatWallRequest request = new GetUserChatWallRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getUserChatWall(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetUserChatWallResponse>() {
                            @Override
                            protected void success(GetUserChatWallResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(List<UserMessage> messages) {
        UserMessagesAdapter adapter = new UserMessagesAdapter(messages);
        mDisposables.add(
                adapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(message -> {
                            Logcat.v("User message clicked");
                            mView.onUserMessageClicked(message);
                        }));
        findViewById(R.id.user_search_recycler, RecyclerView.class).setAdapter(adapter);

        if (recyclerViewHelper != null) {
            recyclerViewHelper.detachFromRecyclerView(findViewById(R.id.user_search_recycler, RecyclerView.class));
        }

        recyclerViewHelper = new RecyclerViewHelper();
        recyclerViewHelper.setItemViewSwipeEnable(true);

        recyclerViewHelper.initSwipeForMessages(findViewById(R.id.user_search_recycler, RecyclerView.class),getContext(), messages, new OnSwipeDialogCallback() {
            @Override
            public void onDialogButtonClick(Boolean isYes, int position, Boolean isDelete) {
                adapter.notifyDataSetChanged();
                if(isYes) {
                    if (isDelete) {

                        mView.onUserMessagesDeleted(messages.get(position));
                        Logcat.d("silinecek post: " + messages.get(position).otherUser.username + "");
                    }
                }
            }


        });
    }

    void deleteUserMessage(UserMessage userMessage){

        DeleteUserMessagesRequest request = new DeleteUserMessagesRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.otherUserId = userMessage.otherUser.iduser;

        mDisposables.add(
                ApiManager
                        .getInstance()
                .deleteUserMessages(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ServiceConsumer<DeleteUserMessagesResponse>() {
                    @Override
                    protected void success(DeleteUserMessagesResponse response) {
                        getMessages();
                    }

                    @Override
                    protected void error(ApiError error) {

                    }
                })


        );
    }
}
