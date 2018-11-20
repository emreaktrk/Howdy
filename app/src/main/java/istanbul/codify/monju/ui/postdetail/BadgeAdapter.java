package istanbul.codify.monju.ui.postdetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import istanbul.codify.monju.BuildConfig;
import istanbul.codify.monju.R;
import istanbul.codify.monju.model.Badge;

import java.util.ArrayList;
import java.util.List;

final class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.Holder> {

    private List<Badge> mList;

    BadgeAdapter(List<Badge> list) {
        this.mList = list == null ? new ArrayList<>() : list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_badge, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Badge badge = mList.get(position);

        holder.mText.setText(badge.r_text);
        Picasso
                .with(holder.mImage.getContext())
                .load(BuildConfig.URL + badge.r_img)
                .into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private AppCompatImageView mImage;
        private AppCompatTextView mText;

        public Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.badge_image);
            mText = itemView.findViewById(R.id.badge_text);
        }
    }
}
