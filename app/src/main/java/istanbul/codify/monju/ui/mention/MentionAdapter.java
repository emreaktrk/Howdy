package istanbul.codify.monju.ui.mention;

import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.blankj.utilcode.util.StringUtils;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Mention;
import istanbul.codify.monju.model.User;
import istanbul.codify.monju.view.MentionButton;

import java.util.ArrayList;
import java.util.List;

final class MentionAdapter extends RecyclerView.Adapter<MentionAdapter.Holder> {

    private final PublishSubject<Mention> mPublish = PublishSubject.create();
    private List<User> mList;
    private List<User> mFilter;

    MentionAdapter(@Nullable List<User> list) {
        mList = list == null ? new ArrayList<>() : list;
        mFilter = new ArrayList<>(mList);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_mention, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = mFilter.get(position);

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .into(holder.mImage);
        holder.mUsername.setText(user.username);
        holder.mNameSurname.setText(user.namesurname);
    }

    @Override
    public int getItemCount() {
        return mFilter.size();
    }

    PublishSubject<Mention> mentionClicks() {
        return mPublish;
    }

    void notifyDataSetChanged(List<User> list) {
        mList = list;
        notifyDataSetChanged();
    }

    void setFiltered(String query) {
        if (StringUtils.isEmpty(query)) {
            mFilter = new ArrayList<>(mList);
        } else {
            mFilter.clear();

            for (User user : mList) {
                if (user.username.startsWith(query) || user.namesurname.startsWith(query)) {
                    mFilter.add(user);
                }
            }
        }

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
            User user = mFilter.get(getAdapterPosition());
            Mention mention = new Mention(user, isChecked);
            mPublish.onNext(mention);
        }
    }
}
