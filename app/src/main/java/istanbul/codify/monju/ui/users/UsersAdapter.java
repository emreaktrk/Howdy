package istanbul.codify.monju.ui.users;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.model.Follow;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.view.FollowButton;

import java.util.ArrayList;
import java.util.List;

final class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Holder> {

    private final PublishSubject<User> mUserSubject = PublishSubject.create();
    private final PublishSubject<Follow> mFollowSubject = PublishSubject.create();
    private List<User> mList;

    UsersAdapter(@Nullable List<User> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = mList.get(position);

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .into(holder.mImage);
        holder.mUsername.setText(user.username);
        holder.mNameSurname.setText(user.namesurname);

        if (user.isfollowing != null && user.iduser != AccountUtils.me(holder.mFollow.getContext()).iduser) {

            holder.mFollow.setVisibility(View.VISIBLE);
            switch (user.isfollowing) {
                case FOLLOWING:
                    holder.mFollow.setState(FollowButton.State.UNFOLLOW);
                    break;
                case NOT_FOLLOWING:
                    holder.mFollow.setState(FollowButton.State.FOLLOW);
                    break;
                case REQUEST_SENT:
                    holder.mFollow.setState(FollowButton.State.REQUEST_CANCEL);
                    break;
            }
        } else {
            holder.mFollow.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<User> itemClicks() {
        return mUserSubject;
    }

    public PublishSubject<Follow> followClicks() {
        return mFollowSubject;
    }

    void notifyDataSetChanged(List<User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, FollowButton.OnFollowClickListener, FollowButton.OnUnfollowClickListener, FollowButton.OnRequestCancelClickListener, FollowButton.OnRequestClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mUsername;
        private AppCompatTextView mNameSurname;
        private FollowButton mFollow;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.user_image);
            mUsername = itemView.findViewById(R.id.user_username);
            mNameSurname = itemView.findViewById(R.id.user_namesurname);
            mFollow = itemView.findViewById(R.id.user_follow);
            mFollow
                    .setFollowClickListener(this)
                    .setUnfollowClickListener(this)
                    .setRequestClickListener(this)
                    .setRequestCancelClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = mList.get(getAdapterPosition());
            mUserSubject.onNext(user);
        }

        @Override
        public void onFollowClicked(FollowButton compound) {
            User user = mList.get(getAdapterPosition());
            mFollowSubject.onNext(new Follow(user, compound));
        }

        @Override
        public void onUnfollowClicked(FollowButton compound) {
            User user = mList.get(getAdapterPosition());
            mFollowSubject.onNext(new Follow(user, compound));
        }

        @Override
        public void onRequestClicked(FollowButton compound) {
            User user = mList.get(getAdapterPosition());
            mFollowSubject.onNext(new Follow(user, compound));
        }

        @Override
        public void onRequestCancelClicked(FollowButton compound) {
            User user = mList.get(getAdapterPosition());
            mFollowSubject.onNext(new Follow(user, compound));
        }
    }
}
