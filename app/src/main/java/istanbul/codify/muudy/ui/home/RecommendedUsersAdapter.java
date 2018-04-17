package istanbul.codify.muudy.ui.home;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.helper.utils.InflaterUtils;
import istanbul.codify.muudy.model.Follow;
import istanbul.codify.muudy.model.User;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

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

        UserHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.recommended_user_image);
            mFullname = itemView.findViewById(R.id.recommended_user_fullname);
            mUsername = itemView.findViewById(R.id.recommended_user_username);

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
}
