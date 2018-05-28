package istanbul.codify.muudy.ui.profile;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.binaryfork.spanny.Spanny;
import com.squareup.picasso.Picasso;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.model.UserTop;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class UserWeeklyTopAdapter extends RecyclerView.Adapter<UserWeeklyTopAdapter.Holder> {

    private ArrayList<UserTop> mList;
    private User mUser;

    public UserWeeklyTopAdapter(ArrayList<UserTop> tops, User user) {
        mList = tops == null ? new ArrayList<>() : tops;
        mUser = user;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_user_weekly_top, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        UserTop userTop = mList.get(position);

        holder.mText.setText(new Spanny().append(mUser.username + ", ", new StyleSpan(BOLD)).append(userTop.awards_top_user_text_for_this_user));
        Format formatter = new SimpleDateFormat("dd.MM.yyyy");

        if (userTop.wtu_date != null) {
            String s = formatter.format(userTop.wtu_date);
            holder.mDate.setText(s);
        }

        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + userTop.awards_imgpath)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mDate;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.user_weekly_top_image);
            mText = itemView.findViewById(R.id.user_weekly_top_text);
            mDate = itemView.findViewById(R.id.user_weekly_top_date);
        }
    }
}
