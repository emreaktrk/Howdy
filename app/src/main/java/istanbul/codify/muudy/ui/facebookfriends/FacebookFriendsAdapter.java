package istanbul.codify.muudy.ui.facebookfriends;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


final class FacebookFriendsAdapter extends RecyclerView.Adapter<FacebookFriendsAdapter.Holder> {

    private List<User> mList;

    public FacebookFriendsAdapter(@Nullable List<User> list) {
        mList = list == null ? new ArrayList<>() : mList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_facebook_friends, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        User user = mList.get(position);
        holder.mUsername.setText(user.username);
        holder.mFullname.setText(user.namesurname);

        Picasso
                .with(holder.mPicture.getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .into(holder.mPicture);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatTextView mUsername;
        private AppCompatTextView mFullname;
        private AppCompatImageView mPicture;

        Holder(View itemView) {
            super(itemView);

            mUsername = itemView.findViewById(R.id.facebook_friends_username);
            mFullname = itemView.findViewById(R.id.facebook_friends_fullname);
            mPicture = itemView.findViewById(R.id.facebook_friends_picture);
        }
    }
}
