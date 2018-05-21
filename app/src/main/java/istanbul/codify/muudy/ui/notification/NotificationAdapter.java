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
import istanbul.codify.muudy.model.FollowRequest;
import istanbul.codify.muudy.model.Notification;

import java.util.ArrayList;
import java.util.List;

public final class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PublishSubject<Notification> mPublish = PublishSubject.create();
    private List<Notification> mList;
    List<FollowRequest> mRequests;

    private static final int HEADER_COUNT = 2;

    public NotificationAdapter(@Nullable List<Notification> list) {
        mList = list == null ? new ArrayList<>() : list;
    }


    public NotificationAdapter(@Nullable List<Notification> list, List<FollowRequest> requests) {
        mList = list == null ? new ArrayList<>() : list;
        mRequests = requests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case NotificationAdapter.FollowRequestHeaderHolder.TYPE:
                return new NotificationAdapter.FollowRequestHeaderHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_statistic_event_chart, parent, false));
            case NotificationAdapter.FollowRequestHolder.TYPE:
                return new NotificationAdapter.FollowRequestHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity_stat, parent, false));
            case NotificationAdapter.NotificationHeaderHolder.TYPE:
                return new NotificationAdapter.NotificationHeaderHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity_stat, parent, false));
            case NotificationAdapter.NotificationHolder.TYPE:
                return new NotificationAdapter.NotificationHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_notification, parent, false));
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {
        if (mHolder instanceof FollowRequestHeaderHolder) {

        }else if (mHolder instanceof FollowRequestHolder){

        }else if (mHolder instanceof NotificationHeaderHolder){

        }else if (mHolder instanceof NotificationHolder){
            Notification notification = mList.get(getPosition(position));
            NotificationHolder holder = (NotificationHolder) mHolder;

            holder.mText.setText(notification.notification_msg);
            Picasso
                    .with(holder.itemView.getContext())
                    .load(notification.fromUser == null ? null : BuildConfig.URL + notification.fromUser.imgpath1)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.mImage);
        }

    }

    @Override
    public int getItemCount() {
        if(mRequests.size() >= 2){
            return mList.size() + NotificationAdapter.HEADER_COUNT + 2;
        }else if(mRequests.size() == 1) {
            return mList.size() + NotificationAdapter.HEADER_COUNT + 1;
        }else{
            return mList.size();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(mRequests.size() >= 2){
            if (position == 0) {
                return NotificationAdapter.FollowRequestHeaderHolder.TYPE;
            }else if(position == 1 || position == 2){
                return NotificationAdapter.FollowRequestHolder.TYPE;
            }else if (position == 3) {
                return NotificationAdapter.NotificationHeaderHolder.TYPE;
            }else {
                return NotificationAdapter.NotificationHolder.TYPE;
            }
        }else if(mRequests.size() == 1) {
            if (position == 0) {
                return NotificationAdapter.FollowRequestHeaderHolder.TYPE;
            }else if(position == 1){
                return NotificationAdapter.FollowRequestHolder.TYPE;
            }else if (position == 2) {
                return NotificationAdapter.NotificationHeaderHolder.TYPE;
            }else {
                return NotificationAdapter.NotificationHolder.TYPE;
            }
        }else{
            return NotificationHolder.TYPE;
        }

    }

    private int getPosition(int position){
        if(mRequests.size() >= 2){
            if (position == 0) {
                return 0;
            }else if(position == 1 || position == 2){
                return position - 1;
            }else if (position == 3) {
                return  0;
            }else {
                return position - 4;
            }
        }else if(mRequests.size() == 1) {
            if (position == 0) {
                return 0;
            }else if(position == 1){
                return 0;
            }else if (position == 2) {
                return 0;
            }else {
                return position - 3;
            }
        }else{
            return position;
        }
    }

    public PublishSubject<Notification> itemClicks() {
        return mPublish;
    }

    class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 271;

        private CircleImageView mImage;
        private AppCompatTextView mText;

        NotificationHolder(View itemView) {
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


    class FollowRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 272;

        private CircleImageView mImage;
        private AppCompatTextView mText;

        FollowRequestHolder(View itemView) {
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

    class FollowRequestHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 273;

        private CircleImageView mImage;
        private AppCompatTextView mText;

        FollowRequestHeaderHolder(View itemView) {
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

    class NotificationHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 274;

        private CircleImageView mImage;
        private AppCompatTextView mText;

        NotificationHeaderHolder(View itemView) {
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
