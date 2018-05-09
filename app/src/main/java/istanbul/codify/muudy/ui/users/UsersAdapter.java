package istanbul.codify.muudy.ui.users;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;

import java.util.ArrayList;
import java.util.List;

final class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.Holder> {

    private final PublishSubject<User> mPublish = PublishSubject.create();
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    PublishSubject<User> itemClicks() {
        return mPublish;
    }

    void notifyDataSetChanged(List<User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mUsername;
        private AppCompatTextView mNameSurname;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.user_image);
            mUsername = itemView.findViewById(R.id.user_username);
            mNameSurname = itemView.findViewById(R.id.user_namesurname);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            User user = mList.get(getAdapterPosition());
            mPublish.onNext(user);
        }
    }
}
