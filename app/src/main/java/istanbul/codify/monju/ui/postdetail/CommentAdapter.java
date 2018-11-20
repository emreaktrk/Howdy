package istanbul.codify.monju.ui.postdetail;

import android.support.annotation.NonNull;
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
import istanbul.codify.monju.model.Comment;

import java.util.ArrayList;
import java.util.List;

final class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {

    private final PublishSubject<Long> mUserSubject = PublishSubject.create();

    private List<Comment> mList;

    CommentAdapter(List<Comment> list) {
        this.mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_comment, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Comment comment = mList.get(position);

        holder.mText.setText(comment.postcomment_text);
        holder.mDate.setText(comment.humanDate);
        Picasso
                .with(holder.mAvatar.getContext())
                .load(BuildConfig.URL + comment.commenterUser.imgpath1)
                .placeholder(R.drawable.ic_avatar)
                .into(holder.mAvatar);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mAvatar;
        private AppCompatTextView mText;
        private AppCompatTextView mDate;

        public Holder(View itemView) {
            super(itemView);

            mAvatar = itemView.findViewById(R.id.comment_avatar);
            mText = itemView.findViewById(R.id.comment_text);
            mDate = itemView.findViewById(R.id.comment_date);

            mAvatar.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Long userId = mList.get(getAdapterPosition()).postcomment_commenterid;
            mUserSubject.onNext(userId);
        }
    }
}
