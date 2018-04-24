package istanbul.codify.muudy.ui.profile;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.Post;
import istanbul.codify.muudy.view.StarView;

import java.util.ArrayList;
import java.util.List;

final class StarAdapter extends RecyclerView.Adapter<StarAdapter.Holder> {

    private List<Post> mList;

    public StarAdapter(List<Post> games) {
        mList = games == null ? new ArrayList<>() : games;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_star, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Post game = mList.get(position);

        holder.mText.setText(game.words_word);
        holder.mStar.setPoint(game.post_given_point);
        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + game.words_emoji_url)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;
        private StarView mStar;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.star_image);
            mText = itemView.findViewById(R.id.star_text);
            mStar = itemView.findViewById(R.id.star_star);
        }
    }
}
