package istanbul.codify.muudy.ui.weeklytopuser;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.WeeklyTopUser;

import java.util.ArrayList;

public class WeeklyTopUsersAdapter extends  RecyclerView.Adapter<WeeklyTopUsersAdapter.Holder>{

    private PublishSubject<User> mPublish = PublishSubject.create();
    private ArrayList<WeeklyTopUser> mList;

    public WeeklyTopUsersAdapter(ArrayList<WeeklyTopUser> weeklyTopUsers) {
        mList = weeklyTopUsers == null ? new ArrayList<>() : weeklyTopUsers;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_weekly_top_user, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        WeeklyTopUser weeklyTopUser = mList.get(position);

        holder.mText.setText(weeklyTopUser.award.awards_top_user_text_for_other_users);

        String awardText = weeklyTopUser.user.username + ", " + weeklyTopUser.award.awards_top_user_text_for_other_users;

        SpannableStringBuilder str = new SpannableStringBuilder(awardText);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, weeklyTopUser.user.username.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.mText.setText(str);

        Picasso
                .with(holder.userPhoto.getContext())
                .load(BuildConfig.URL + weeklyTopUser.user.imgpath1)
                .into(holder.userPhoto);

        if (weeklyTopUser.award.awards_imgpath != null) {
            Picasso
                    .with(holder.userPhoto.getContext())
                    .load(BuildConfig.URL + weeklyTopUser.award.awards_imgpath)
                    .into(holder.award);
        }

        holder.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPublish.onNext(weeklyTopUser.user);
            }
        });
    }

    PublishSubject<User> itemClicks() {
        return mPublish;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private AppCompatImageView award;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.cell_weekly_top_user_photo);
            award = itemView.findViewById(R.id.cell_weekly_top_user_award_photo);
            mText = itemView.findViewById(R.id.cell_weekly_top_award);
        }
    }
}
