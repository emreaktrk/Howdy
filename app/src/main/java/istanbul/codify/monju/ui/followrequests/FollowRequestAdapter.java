package istanbul.codify.monju.ui.followrequests;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
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
import istanbul.codify.monju.model.FollowRequest;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final PublishSubject<FollowRequest> mPublishFollowRequest = PublishSubject.create();
    private final PublishSubject<FollowRequest> mPublishAcceptFollowRequest = PublishSubject.create();
    private final PublishSubject<FollowRequest> mPublishDeclineFollowRequest = PublishSubject.create();

    List<FollowRequest> mRequests;

    public FollowRequestAdapter(List<FollowRequest> requests) {
        mRequests = requests == null ? new ArrayList<>() : requests;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new FollowRequestAdapter.FollowRequestHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_follow_request, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder mHolder, int position) {
        if (mHolder instanceof FollowRequestAdapter.FollowRequestHolder) {
            FollowRequest request = mRequests.get(position);
            FollowRequestAdapter.FollowRequestHolder holder = (FollowRequestAdapter.FollowRequestHolder) mHolder;

            holder.mText.setText(request.msg);
            holder.mDate.setText(request.humanDate);
            Picasso
                    .with(holder.itemView.getContext())
                    .load(request.user == null ? null : BuildConfig.URL + request.user.imgpath1)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.mImage);

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


        }

    }

    @Override
    public int getItemCount() {
        return mRequests.size();

    }

    @Override
    public int getItemViewType(int position) {
        return FollowRequestAdapter.FollowRequestHolder.TYPE;

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

            FollowRequest request = mRequests.get(getAdapterPosition());
            mPublishFollowRequest.onNext(request);
        }
    }

}
