package com.codify.howdy.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.exception.ImplementationMissingException;
import com.codify.howdy.helper.utils.InflaterUtils;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.PostMediaType;
import com.codify.howdy.view.LikeButton;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

final class PostAdapter extends RecyclerView.Adapter<PostAdapter.NoneHolder> {

    private PublishSubject<Post> mPosts = PublishSubject.create();
    private PublishSubject<Post> mLikes = PublishSubject.create();
    private PublishSubject<Post> mComments = PublishSubject.create();
    private List<Post> mList;

    PostAdapter(List<Post> list) {
        mList = list;
    }

    @NonNull
    @Override
    public PostAdapter.NoneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostMediaType mediaType = PostMediaType.values()[viewType];
        switch (mediaType) {
            case NONE:
                return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_none, parent));
            case IMAGE:
                return new ImageHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
            case VIDEO:
                return new VideoHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
            default:
                throw new ImplementationMissingException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.NoneHolder holder, int position) {
        Post post = mList.get(position);

        holder.mMessage.setText(post.post_text);
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + post.imgpath)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).post_media_type.ordinal();
    }

    PublishSubject<Post> itemClicks() {
        return mPosts;
    }

    PublishSubject<Post> likeClicks() {
        return mLikes;
    }

    PublishSubject<Post> commentClicks() {
        return mComments;
    }

    class NoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AppCompatTextView mMessage;
        private CircleImageView mImage;
        private LikeButton mLike;
        private AppCompatTextView mComment;

        NoneHolder(View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.post_message);
            mImage = itemView.findViewById(R.id.post_image);

            mLike = itemView.findViewById(R.id.post_like);
            mLike.setOnClickListener(this);

            mComment = itemView.findViewById(R.id.post_comment);
            mComment.setOnClickListener(this);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Post post = mList.get(getAdapterPosition());

            switch (view.getId()) {
                case R.id.post_like:
                    mLikes.onNext(post);
                    return;
                case R.id.post_comment:
                    mComments.onNext(post);
                    return;
                default:
                    mPosts.onNext(post);
            }
        }
    }

    class ImageHolder extends NoneHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_image:
                    return;
                default:
                    super.onClick(view);
            }
        }
    }

    class VideoHolder extends NoneHolder {

        VideoHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.activity_image:
                    return;
                default:
                    super.onClick(view);
            }
        }
    }
}
