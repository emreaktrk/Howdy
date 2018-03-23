package com.codify.howdy.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.codify.howdy.R;
import com.codify.howdy.exception.ImplementationMissingException;
import com.codify.howdy.helper.utils.InflaterUtils;
import com.codify.howdy.model.Post;
import com.codify.howdy.model.PostMediaType;

import java.util.List;

import io.reactivex.subjects.PublishSubject;

final class PostAdapter extends RecyclerView.Adapter<PostAdapter.NoneHolder> {

    private PublishSubject<Post> mPublish = PublishSubject.create();
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
                return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_image, parent));
            case VIDEO:
                return new NoneHolder(InflaterUtils.cell(R.layout.cell_post_video, parent));
            default:
                throw new ImplementationMissingException();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.NoneHolder holder, int position) {
        Post post = mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).post_media_type.ordinal();
    }

    class NoneHolder extends RecyclerView.ViewHolder {

        NoneHolder(View itemView) {
            super(itemView);
        }
    }

    class ImageHolder extends NoneHolder {

        ImageHolder(View itemView) {
            super(itemView);
        }
    }

    class VideoHolder extends NoneHolder {

        VideoHolder(View itemView) {
            super(itemView);
        }
    }
}
