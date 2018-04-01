package com.codify.howdy.ui.home;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.exception.ImplementationMissingException;
import com.codify.howdy.helper.utils.InflaterUtils;
import com.codify.howdy.model.Follow;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.PostMediaType;
import com.codify.howdy.model.User;
import com.codify.howdy.model.zipper.Like;
import com.codify.howdy.view.LikeButton;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @IntRange(from = 0, to = Integer.MAX_VALUE)
    private static final int RECOMMENDED_USER_POSITION = 14;

    private PublishSubject<Post> mPostSubject = PublishSubject.create();
    private PublishSubject<Like> mLikeSubject = PublishSubject.create();
    private PublishSubject<Post> mImageSubject = PublishSubject.create();
    private PublishSubject<Post> mVideoSubject = PublishSubject.create();
    private PublishSubject<Post> mAvatarSubject = PublishSubject.create();
    private List<Post> mPosts;
    private RecommendedUsersAdapter mUsers;
    private int mCommentVisibility = View.VISIBLE;

    public PostAdapter(@Nullable List<Post> posts) {
        mPosts = posts == null ? new ArrayList<>() : posts;
    }

    public PostAdapter(@Nullable List<Post> posts, @Nullable List<User> users) {
        mPosts = posts == null ? new ArrayList<>() : posts;
        mUsers = new RecommendedUsersAdapter(users);
    }

    public PostAdapter(Post post) {
        mPosts = Collections.singletonList(post);
        mCommentVisibility = View.GONE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostMediaType mediaType = PostMediaType.values()[viewType];
        switch (mediaType) {
            case NONE:
                return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_none, parent));
            case IMAGE:
                return new ImageHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
            case VIDEO:
                return new VideoHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
            case RECOMMENDED_USERS:
                return new RecommendedHolder(InflaterUtils.cell(R.layout.cell_post_recommended_user, parent));
            default:
                throw new ImplementationMissingException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoneHolder) {
            Post post = getItem(position);

            NoneHolder none = (NoneHolder) holder;
            none.mMessage.setText(post.post_text);
            none.mLike.setText(post.post_likecount + "");
            none.mComment.setText(post.post_commentcount + "");
            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + post.imgpath1)
                    .placeholder(R.drawable.ic_avatar)
                    .into(none.mImage);
            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + post.post_emoji)
                    .into(none.mEmotion);

            if (holder instanceof MediaHolder) {
                MediaHolder media = (MediaHolder) holder;
                Picasso
                        .with(holder.itemView.getContext())
                        .load(BuildConfig.URL + post.post_mediapath)
                        .centerCrop()
                        .resize(180, 180)
                        .into(media.mMedia);
            }
        }

        if (holder instanceof RecommendedHolder) {
            RecommendedHolder recommended = (RecommendedHolder) holder;
            recommended.mRecycler.setAdapter(mUsers);
        }
    }

    @Override
    public int getItemCount() {
        return mUsers == null ? mPosts.size() : mPosts.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mUsers == null) {
            return mPosts.get(position).post_media_type.ordinal();
        } else {
            if (getItemCount() > RECOMMENDED_USER_POSITION) {
                if (position == RECOMMENDED_USER_POSITION) {
                    return PostMediaType.RECOMMENDED_USERS.ordinal();
                }
            } else {
                if (position == getItemCount()) {
                    return PostMediaType.RECOMMENDED_USERS.ordinal();
                }
            }

            return mPosts.get(position > 15 ? position - 1 : position).post_media_type.ordinal();
        }
    }

    private Post getItem(int position) {
        if (mUsers == null) {
            return mPosts.get(position);
        } else {
            if (getItemCount() > RECOMMENDED_USER_POSITION && position > RECOMMENDED_USER_POSITION) {
                return mPosts.get(position - 1);
            } else {
                return mPosts.get(position);
            }
        }
    }

    public PublishSubject<Post> itemClicks() {
        return mPostSubject;
    }

    public PublishSubject<Like> likeClicks() {
        return mLikeSubject;
    }

    public PublishSubject<Post> imageClicks() {
        return mImageSubject;
    }

    public PublishSubject<Post> videoClicks() {
        return mVideoSubject;
    }

    public PublishSubject<Post> avatarClicks() {
        return mAvatarSubject;
    }

    public PublishSubject<Follow> followClicks() {
        return mUsers.followClicks();
    }

    public PublishSubject<User> userClicks() {
        return mUsers.userClicks();
    }

    class NoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        private AppCompatTextView mMessage;
        private CircleImageView mImage;
        private CircleImageView mEmotion;
        private LikeButton mLike;
        private AppCompatTextView mComment;

        NoneHolder(View itemView) {
            super(itemView);

            mMessage = itemView.findViewById(R.id.post_message);
            mEmotion = itemView.findViewById(R.id.post_emotion);

            mImage = itemView.findViewById(R.id.post_image);
            mImage.setOnClickListener(this);

            mLike = itemView.findViewById(R.id.post_like);
            mLike.setOnCheckedChangeListener(this);

            mComment = itemView.findViewById(R.id.post_comment);
            mComment.setVisibility(mCommentVisibility);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Post post = mPosts.get(getAdapterPosition());
            switch (view.getId()) {
                case R.id.post_image:
                    mAvatarSubject.onNext(post);
                    return;
                default:
                    mPostSubject.onNext(post);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Post post = mPosts.get(getAdapterPosition());
            Like like = new Like(post, isChecked);
            mLikeSubject.onNext(like);
        }
    }

    abstract class MediaHolder extends NoneHolder {

        private AppCompatImageView mMedia;

        MediaHolder(View itemView) {
            super(itemView);

            mMedia = itemView.findViewById(R.id.post_media);
            mMedia.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.post_media:
                    onMediaClicked();
                    return;
                default:
                    super.onClick(view);
            }
        }

        abstract void onMediaClicked();
    }


    class ImageHolder extends MediaHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {
            Post post = mPosts.get(getAdapterPosition());
            mImageSubject.onNext(post);
        }
    }

    class VideoHolder extends MediaHolder {

        VideoHolder(View itemView) {
            super(itemView);
        }

        @Override
        void onMediaClicked() {
            Post post = mPosts.get(getAdapterPosition());
            mVideoSubject.onNext(post);
        }
    }

    class RecommendedHolder extends RecyclerView.ViewHolder {

        private RecyclerView mRecycler;

        RecommendedHolder(View itemView) {
            super(itemView);

            mRecycler = itemView.findViewById(R.id.post_recommended_user_recycler);
        }
    }
}
