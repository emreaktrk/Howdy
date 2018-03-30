package com.codify.howdy.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.helper.utils.InflaterUtils;
import com.codify.howdy.model.Follow;
import com.codify.howdy.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

public final class RecommendedUsersAdapter extends RecyclerView.Adapter<RecommendedUsersAdapter.UserHolder> {

    private PublishSubject<User> mUserSubject = PublishSubject.create();
    private PublishSubject<Follow> mFollowSubject = PublishSubject.create();
    private List<User> mList;

    public RecommendedUsersAdapter(List<User> list) {
        mList = list;
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


    class UserHolder extends RecyclerView.ViewHolder {

        private CircleImageView mImage;
        private AppCompatTextView mFullname;
        private AppCompatTextView mUsername;

        UserHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.recommended_user_image);
            mFullname = itemView.findViewById(R.id.recommended_user_fullname);
            mUsername = itemView.findViewById(R.id.recommended_user_username);
        }
    }
}
