package com.codify.howdy.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.codify.howdy.BuildConfig;
import com.codify.howdy.R;
import com.codify.howdy.exception.ImplementationMissingException;
import com.codify.howdy.helper.utils.InflaterUtils;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.PostMediaType;
import com.codify.howdy.model.zipper.Like;
import com.codify.howdy.view.LikeButton;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;

import java.util.List;

final class PostAdapter extends RecyclerView.Adapter<PostAdapter.NoneHolder> {

    private PublishSubject<Post> mPosts = PublishSubject.create();
    private PublishSubject<Like> mLikes = PublishSubject.create();
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
        holder.mLike.setText(post.post_likecount + "");
        holder.mComment.setText(post.post_commentcount + "");
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + post.imgpath)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.mImage);
        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + post.post_emoji)
                .into(holder.mEmotion);
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

    PublishSubject<Like> likeClicks() {
        return mLikes;
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
            mImage = itemView.findViewById(R.id.post_image);
            mEmotion = itemView.findViewById(R.id.post_emotion);

            mLike = itemView.findViewById(R.id.post_like);
            mLike.setOnCheckedChangeListener(this);

            mComment = itemView.findViewById(R.id.post_comment);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Post post = mList.get(getAdapterPosition());
            mPosts.onNext(post);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            Post post = mList.get(getAdapterPosition());
            Like like = new Like(post, isChecked);
            mLikes.onNext(like);
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
