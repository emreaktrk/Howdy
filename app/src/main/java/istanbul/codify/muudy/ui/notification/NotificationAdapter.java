package istanbul.codify.muudy.ui.notification;

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
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Notification;

import java.util.ArrayList;
import java.util.List;

public final class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.Holder> {

    private final PublishSubject<Notification> mPublish = PublishSubject.create();
    private List<Notification> mList;

    public NotificationAdapter(@Nullable List<Notification> list) {
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
        Notification notification = mList.get(position);

        holder.mText.setText(notification.notification_msg);
        Picasso
                .with(holder.itemView.getContext())
                .load(notification.fromUser == null ? null : BuildConfig.URL + notification.fromUser.imgpath1)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public PublishSubject<Notification> itemClicks() {
        return mPublish;
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mText;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.notification_image);
            mText = itemView.findViewById(R.id.notification_text);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Notification notification = mList.get(getAdapterPosition());
            mPublish.onNext(notification);
        }
    }
}
