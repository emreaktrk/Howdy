package istanbul.codify.muudy.ui.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
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
import istanbul.codify.muudy.utils.PicassoHelper;

import java.util.ArrayList;
import java.util.List;

public final class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADER_COUNT = 2;
    private final PublishSubject<Notification> mPublish = PublishSubject.create();
    private final PublishSubject<FollowRequest> mPublishFollowRequest = PublishSubject.create();
    private final PublishSubject<FollowRequest> mPublishAcceptFollowRequest = PublishSubject.create();
    private final PublishSubject<FollowRequest> mPublishDeclineFollowRequest = PublishSubject.create();
    private final PublishSubject<List<FollowRequest>> mPublishSeeAllRequests = PublishSubject.create();
    private List<FollowRequest> mRequests;
    private List<Notification> mList;

    public NotificationAdapter(@Nullable List<Notification> list, List<FollowRequest> requests) {
        mList = list == null ? new ArrayList<>() : list;
        mRequests = requests == null ? new ArrayList<>() : requests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case NotificationAdapter.FollowRequestHeaderHolder.TYPE:
                return new NotificationAdapter.FollowRequestHeaderHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_follow_request_header, parent, false));
            case NotificationAdapter.FollowRequestHolder.TYPE:
                return new NotificationAdapter.FollowRequestHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_follow_request, parent, false));
            case NotificationAdapter.NotificationHeaderHolder.TYPE:
                return new NotificationAdapter.NotificationHeaderHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_notification_header, parent, false));
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
            FollowRequest request = mRequests.get(getItemPosition(position));
            FollowRequestHeaderHolder holder = (FollowRequestHeaderHolder) mHolder;
            holder.mText.setText("Takip İstekleri (" + mRequests.size() + ")");

            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPublishSeeAllRequests.onNext(mRequests);
                }
            });
        } else if (mHolder instanceof FollowRequestHolder) {
            FollowRequest request = mRequests.get(getItemPosition(position));
            FollowRequestHolder holder = (FollowRequestHolder) mHolder;

            holder.mText.setText(request.msg);
            holder.mDate.setText(request.humanDate);
            new PicassoHelper(holder.mImage.getContext(),holder.mImage,request.user.imgpath1);
            /*
            Picasso
                    .with(holder.itemView.getContext())
                    .load(request.user == null ? null : BuildConfig.URL + request.user.imgpath1)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.mImage);
                    */

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPublishAcceptFollowRequest.onNext(request);
                }
            });

            holder.decline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPublishDeclineFollowRequest.onNext(request);
                }
            });
        } else if (mHolder instanceof NotificationHeaderHolder) {
            Notification notification = mList.get(getItemPosition(position));
            NotificationHeaderHolder holder = (NotificationHeaderHolder) mHolder;

            holder.mText.setText("Bildirimler");
        } else if (mHolder instanceof NotificationHolder) {
            Notification notification = mList.get(getItemPosition(position));
            NotificationHolder holder = (NotificationHolder) mHolder;

            holder.mText.setText(notification.notification_msg);
            holder.mDate.setText(notification.humanDate);
            //new PicassoHelper(holder.mImage.getContext(),holder.mImage,notification.fromUser.imgpath1);
            PicassoHelper.setImageWithPlaceHolder(holder.mImage,notification.fromUser == null ? null : notification.fromUser.imgpath1);
           /* Picasso
                    .with(holder.itemView.getContext())
                    .load(notification.fromUser == null ? null : BuildConfig.URL + notification.fromUser.imgpath1)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.mImage);*/
        }
    }

    @Override
    public int getItemCount() {
        if (mRequests.size() >= 2) {
            return mList.size() + NotificationAdapter.HEADER_COUNT + 2;
        } else if (mRequests.size() == 1) {
            return mList.size() + NotificationAdapter.HEADER_COUNT + 1;
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRequests.size() >= 2) {
            if (position == 0) {
                return NotificationAdapter.FollowRequestHeaderHolder.TYPE;
            } else if (position == 1 || position == 2) {
                return NotificationAdapter.FollowRequestHolder.TYPE;
            } else if (position == 3) {
                return NotificationAdapter.NotificationHeaderHolder.TYPE;
            } else {
                return NotificationAdapter.NotificationHolder.TYPE;
            }
        } else if (mRequests.size() == 1) {
            if (position == 0) {
                return NotificationAdapter.FollowRequestHeaderHolder.TYPE;
            } else if (position == 1) {
                return NotificationAdapter.FollowRequestHolder.TYPE;
            } else if (position == 2) {
                return NotificationAdapter.NotificationHeaderHolder.TYPE;
            } else {
                return NotificationAdapter.NotificationHolder.TYPE;
            }
        } else {
            return NotificationHolder.TYPE;
        }
    }

    public int getItemPosition(int position) {
        if (mRequests.size() >= 2) {
            if (position == 0) {
                return 0;
            } else if (position == 1 || position == 2) {
                return position - 1;
            } else if (position == 3) {
                return 0;
            } else {
                return position - 4;
            }
        } else if (mRequests.size() == 1) {
            if (position == 0) {
                return 0;
            } else if (position == 1) {
                return 0;
            } else if (position == 2) {
                return 0;
            } else {
                return position - 3;
            }
        } else {
            return position;
        }
    }

    public PublishSubject<Notification> notificationClicks() {
        return mPublish;
    }

    public PublishSubject<FollowRequest> followRequestClicks() {
        return mPublishFollowRequest;
    }

    public PublishSubject<FollowRequest> acceptFollowRequestClicks() {
        return mPublishAcceptFollowRequest;
    }

    public PublishSubject<FollowRequest> declineFollowRequestClicks() {
        return mPublishDeclineFollowRequest;
    }

    public PublishSubject<List<FollowRequest>> seeAllRequestsClick() {
        return mPublishSeeAllRequests;
    }

    class NotificationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 271;

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mDate;

        NotificationHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.notification_image);
            mText = itemView.findViewById(R.id.notification_text);
            mDate = itemView.findViewById(R.id.notification_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Notification notification = mList.get(getItemPosition(getAdapterPosition()));
            mPublish.onNext(notification);
        }
    }


    class FollowRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 272;

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mDate;
        private AppCompatImageView accept;
        private AppCompatImageView decline;

        FollowRequestHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.follow_request_image);
            mText = itemView.findViewById(R.id.follow_request_text);
            mDate = itemView.findViewById(R.id.follow_request_date);
            accept = itemView.findViewById(R.id.follow_request_accept);
            decline = itemView.findViewById(R.id.follow_request_decline);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            FollowRequest request = mRequests.get(getItemPosition(getAdapterPosition()));
            mPublishFollowRequest.onNext(request);
        }
    }

    class FollowRequestHeaderHolder extends RecyclerView.ViewHolder {

        private static final int TYPE = 273;

        private AppCompatButton mButton;
        private AppCompatTextView mText;

        FollowRequestHeaderHolder(View itemView) {
            super(itemView);

            mButton = itemView.findViewById(R.id.follow_request_see_all_button);
            mText = itemView.findViewById(R.id.follow_request_header_text);

        }

    }

    class NotificationHeaderHolder extends RecyclerView.ViewHolder {

        private static final int TYPE = 274;

        private AppCompatTextView mText;

        NotificationHeaderHolder(View itemView) {
            super(itemView);

            mText = itemView.findViewById(R.id.notification_header_text);
        }
    }
}
