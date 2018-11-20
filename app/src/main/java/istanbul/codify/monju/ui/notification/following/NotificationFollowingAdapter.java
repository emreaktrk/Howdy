package istanbul.codify.monju.ui.notification.following;

import android.support.annotation.NonNull;
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
import istanbul.codify.monju.model.NotificationFollowing;

import java.util.ArrayList;
import java.util.List;

final class NotificationFollowingAdapter extends RecyclerView.Adapter<NotificationFollowingAdapter.Holder> {

    private final PublishSubject<NotificationFollowing> mPublish = PublishSubject.create();
    private List<NotificationFollowing> mList;

    NotificationFollowingAdapter(@Nullable List<NotificationFollowing> list) {
        mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_notification, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NotificationFollowing notification = mList.get(position);

        holder.mText.setText(notification.notification_msg);
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + notification.fromUser.imgpath1)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.mImage);

        holder.mDate.setText(notification.humanDate);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public PublishSubject<NotificationFollowing> itemClicks() {
        return mPublish;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mDate;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.notification_image);
            mText = itemView.findViewById(R.id.notification_text);
            mDate = itemView.findViewById(R.id.notification_date);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            NotificationFollowing notification = mList.get(getAdapterPosition());
            mPublish.onNext(notification);
        }
    }
}
