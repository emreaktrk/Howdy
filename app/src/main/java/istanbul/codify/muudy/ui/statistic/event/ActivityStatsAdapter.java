package istanbul.codify.muudy.ui.statistic.event;

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
import istanbul.codify.muudy.model.ActivityStat;

import java.util.ArrayList;
import java.util.List;

final class ActivityStatsAdapter extends RecyclerView.Adapter<ActivityStatsAdapter.Holder> {

    private final List<ActivityStat> mList;
    private final PublishSubject<ActivityStat> mPublish = PublishSubject.create();
    private List<ActivityStat> mFiltered;

    ActivityStatsAdapter(@Nullable List<ActivityStat> list) {
        mList = list == null ? new ArrayList<>() : list;
        mFiltered = new ArrayList<>(mList);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity_stat, parent, false);
        return new Holder(cell);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        ActivityStat stat = mFiltered.get(position);

        Picasso
                .with(holder.itemView.getContext())
                .load(BuildConfig.URL + stat.post_emoji)
                .into(holder.mImage);
        holder.mText.setText(stat.post_emoji_word);
        holder.mPercent.setText(String.format("%.02f", stat.percent) + "%");
    }

    @Override
    public int getItemCount() {
        return mFiltered.size();
    }

    PublishSubject<ActivityStat> itemClicks() {
        return mPublish;
    }

    List<ActivityStat> getWords() {
        return mList;
    }

    void setFiltered(@Nullable List<ActivityStat> filtered) {
        this.mFiltered = filtered == null ? new ArrayList<>(mList) : filtered;
        notifyDataSetChanged();
    }


    class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mPercent;

        Holder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.activity_stat_image);
            mText = itemView.findViewById(R.id.activity_stat_text);
            mPercent = itemView.findViewById(R.id.activity_stat_percent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ActivityStat stat = mFiltered.get(getAdapterPosition());
            mPublish.onNext(stat);
        }
    }
}
