package istanbul.codify.muudy.ui.mention;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Mention;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.view.MentionButton;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.List;

final class MentionAdapter extends RecyclerView.Adapter<MentionAdapter.Holder> {

    private final PublishSubject<Mention> mPublish = PublishSubject.create();
    private List<User> mList;

    MentionAdapter(@Nullable List<User> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_mention, parent, false);
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

    PublishSubject<Mention> mentionClicks() {
        return mPublish;
    }

    void notifyDataSetChanged(List<User> list) {
        mList = list;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private CircleImageView mImage;
        private AppCompatTextView mUsername;
        private AppCompatTextView mNameSurname;
        private MentionButton mMention;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.mention_image);
            mUsername = itemView.findViewById(R.id.mention_username);
            mNameSurname = itemView.findViewById(R.id.mention_namesurname);
            mMention = itemView.findViewById(R.id.mention_button);

            mMention.setOnCheckedChangeListener(this);
        }


        @Override
        public void onCheckedChanged(CompoundButton button, boolean isChecked) {
            User user = mList.get(getAdapterPosition());
            Mention mention = new Mention(user, isChecked);
            mPublish.onNext(mention);
        }
    }
}
