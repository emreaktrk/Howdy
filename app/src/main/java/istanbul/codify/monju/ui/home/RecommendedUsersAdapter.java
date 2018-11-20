package istanbul.codify.monju.ui.home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.CancelFollowRequest;
import istanbul.codify.monju.api.pojo.request.FollowRequest;
import istanbul.codify.monju.api.pojo.request.SendFollowRequest;
import istanbul.codify.monju.api.pojo.request.UnfollowRequest;
import istanbul.codify.monju.api.pojo.response.*;
import istanbul.codify.monju.helper.utils.InflaterUtils;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.Follow;
import istanbul.codify.monju.model.FollowState;
import istanbul.codify.monju.model.ProfileVisibility;
import istanbul.codify.monju.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.monju.utils.AndroidUtils;

import java.util.ArrayList;
import java.util.List;

public final class RecommendedUsersAdapter extends RecyclerView.Adapter<RecommendedUsersAdapter.UserHolder> {

    private PublishSubject<User> mUserSubject = PublishSubject.create();
    private PublishSubject<Follow> mFollowSubject = PublishSubject.create();
    private List<User> mList;

    public RecommendedUsersAdapter(@Nullable List<User> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserHolder(InflaterUtils.cell(R.layout.cell_recommended_user, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User user = mList.get(position);
        holder.mFullname.setText(user.namesurname);
        holder.mUsername.setText(user.username);
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.mImage);


        holder.mImage.setBorderWidth((int) AndroidUtils.convertDpToPixel(1, holder.mImage.getContext()));
        holder.mImage.setBorderColor(holder.mImage.getContext().getResources().getColor(R.color.black));
        holder.mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isfollowing == null) {
                    user.isfollowing = FollowState.NOT_FOLLOWING;
                }

                if (user.isprofilehidden == ProfileVisibility.HIDDEN){
                    if (user.isfollowing == FollowState.NOT_FOLLOWING) {
                        user.isfollowing = FollowState.REQUEST_SENT;
                        holder.mFollow.setText("İstek Gönderildi");
                        requestFollow(user,holder.itemView.getContext(),holder.mFollow);
                    }else if (user.isfollowing == FollowState.FOLLOWING){
                        user.isfollowing = FollowState.NOT_FOLLOWING;
                        holder.mFollow.setText("+ Takip Et");
                        unfollow(user,holder.itemView.getContext(),holder.mFollow);
                    }else{
                        user.isfollowing = FollowState.NOT_FOLLOWING;
                        holder.mFollow.setText("+ Takip Et");
                        cancelRequestFollow(user,holder.itemView.getContext(),holder.mFollow);
                    }
                }else{
                    if (user.isfollowing == FollowState.NOT_FOLLOWING) {
                        user.isfollowing = FollowState.FOLLOWING;
                        holder.mFollow.setText("Takip Ediliyor");
                        follow(user,holder.itemView.getContext(),holder.mFollow);
                    }else if (user.isfollowing == FollowState.FOLLOWING){
                        user.isfollowing = FollowState.NOT_FOLLOWING;
                        holder.mFollow.setText("+ Takip Et");
                        unfollow(user,holder.itemView.getContext(),holder.mFollow);
                    }else{
                        user.isfollowing = FollowState.NOT_FOLLOWING;
                        holder.mFollow.setText("+ Takip Et");
                        cancelRequestFollow(user,holder.itemView.getContext(),holder.mFollow);
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public PublishSubject<User> userClicks() {
        return mUserSubject;
    }

    public PublishSubject<Follow> followClicks() {
        return mFollowSubject;
    }


    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mFullname;
        private AppCompatTextView mUsername;
        private AppCompatButton mFollow;

        UserHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.recommended_user_image);
            mFullname = itemView.findViewById(R.id.recommended_user_fullname);
            mUsername = itemView.findViewById(R.id.recommended_user_username);
            mFollow = itemView.findViewById(R.id.recommended_user_follow);



            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = mList.get(getAdapterPosition());

            switch (view.getId()) {
                default:
                    mUserSubject.onNext(user);
            }
        }
    }

    private void follow(User mUser, Context context,AppCompatButton button){
        FollowRequest request = new FollowRequest();
        request.token = AccountUtils.tokenLegacy(context);
        request.followtouserid = mUser.iduser;
        ApiManager
                .getInstance()
                .follow(request)
                .subscribe(new ServiceConsumer<FollowResponse>() {
                    @Override
                    protected void success(FollowResponse response) {

                    }

                    @Override
                    protected void error(ApiError error) {
                        Logcat.e(error);
                        mUser.isfollowing = FollowState.NOT_FOLLOWING;
                        button.setText("+ Takip Et");
                    }
                });
    }

    void unfollow(User mUser, Context context, AppCompatButton button) {
        UnfollowRequest request = new UnfollowRequest();
        request.token = AccountUtils.tokenLegacy(context);
        request.unfollowtouserid = mUser.iduser;

                ApiManager
                        .getInstance()
                        .unfollow(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<UnfollowResponse>() {
                            @Override
                            protected void success(UnfollowResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mUser.isfollowing = FollowState.FOLLOWING;
                                button.setText("Takip Ediliyor");

                            }
                        });
    }


    void requestFollow(User mUser, Context context, AppCompatButton button) {
        SendFollowRequest request = new SendFollowRequest();
        request.token = AccountUtils.tokenLegacy(context);
        request.userid = mUser.iduser;

                ApiManager
                        .getInstance()
                        .sendFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<SendFollowResponse>() {
                            @Override
                            protected void success(SendFollowResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mUser.isfollowing = FollowState.NOT_FOLLOWING;
                                button.setText("+ Takip Et");
                            }
                        });
    }

    void cancelRequestFollow(User mUser, Context context, AppCompatButton button) {
        CancelFollowRequest request = new CancelFollowRequest();
        request.token = AccountUtils.tokenLegacy(context);
        request.followRequestedUserId = mUser.iduser;

                ApiManager
                        .getInstance()
                        .cancelFollowRequest(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<CancelFollowResponse>() {
                            @Override
                            protected void success(CancelFollowResponse response) {

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mUser.isfollowing = FollowState.REQUEST_SENT;
                                button.setText("İstek Gönderildi");
                            }
                        });
    }
}
