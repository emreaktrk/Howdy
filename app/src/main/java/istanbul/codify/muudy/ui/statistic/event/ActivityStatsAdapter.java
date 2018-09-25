package istanbul.codify.muudy.ui.statistic.event;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.subjects.PublishSubject;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.ActivityStat;

import java.util.ArrayList;
import java.util.List;

final class ActivityStatsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ActivityStat> mList;
    private List<ActivityStat> mListDetail;
    private List<ActivityStat> mFiltered;
    private PublishSubject<ActivityStat> mPublish = PublishSubject.create();
    private PublishSubject<View> mClicks = PublishSubject.create();
    int mPostcount = 0;

    ActivityStatsAdapter(@Nullable List<ActivityStat> list,@Nullable List<ActivityStat> listDetail, int postCount) {
        mList = list == null ? new ArrayList<>() : list;
        mFiltered = new ArrayList<>(mList);
        mListDetail = listDetail == null ? new ArrayList<>() : listDetail;
        mPostcount  = postCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case ChartHolder.TYPE:
                return new ChartHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_statistic_event_chart, parent, false));
            case StatsHolder.TYPE:
                return new StatsHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_activity_stat, parent, false));
            default:
                throw new IllegalArgumentException("Not implemented");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChartHolder) {
            ChartHolder chart = (ChartHolder) holder;

            List<PieEntry> entries = new ArrayList<>();
            for (ActivityStat stat : mFiltered) {

                entries.add(new PieEntry(stat.percent, stat.post_emoji_word));
            }

            chart.mTextView.setText(mPostcount  + " Paylaşım");

            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(CHARTS_COLORS);
            set.setSliceSpace(2);
            set.setValueTextSize(0f);

            PieData data = new PieData(set);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(9);
            data.setValueTextColor(Color.WHITE);

            Description description = new Description();
            description.setText("");
            chart.mChart.setDescription(description);

            chart.mChart.getLegend().setEnabled(true);
            chart.mChart.setUsePercentValues(true);

            chart.mChart.setData(data);
            chart.mChart.invalidate();
        } else if (holder instanceof StatsHolder) {
            StatsHolder stats = (StatsHolder) holder;

            ActivityStat stat = mListDetail.get(position - 1);

            Picasso
                    .with(holder.itemView.getContext())
                    .load(BuildConfig.URL + stat.post_emoji)
                    .into(stats.mImage);
            stats.mText.setText(stat.post_emoji_word);
            stats.mPercent.setText(String.format("%.02f", stat.percent) + "%");
        }
    }

    @Override
    public int getItemCount() {
        return mListDetail.size() + 1;
    }

    PublishSubject<ActivityStat> itemClicks() {
        return mPublish;
    }

    PublishSubject<View> showAllPostsClicks() {
        return mClicks;
    }

    List<ActivityStat> getWords() {
        return mList;
    }

    void setFiltered(@Nullable List<ActivityStat> filtered) {
        this.mFiltered = filtered == null ? new ArrayList<>(mList) : filtered;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ChartHolder.TYPE;
        } else {
            return StatsHolder.TYPE;
        }
    }

    class StatsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 469;

        private CircleImageView mImage;
        private AppCompatTextView mText;
        private AppCompatTextView mPercent;

        StatsHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.activity_stat_image);
            mText = itemView.findViewById(R.id.activity_stat_text);
            mPercent = itemView.findViewById(R.id.activity_stat_percent);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ActivityStat stat = mListDetail.get(getAdapterPosition() - 1);
            mPublish.onNext(stat);
        }
    }

    class ChartHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final int TYPE = 270;

        private PieChart mChart;
        private AppCompatButton mButton;
        private AppCompatTextView mTextView;

        ChartHolder(View itemView) {
            super(itemView);

            mChart = itemView.findViewById(R.id.statistic_event_chart);
            mButton = itemView.findViewById(R.id.statistic_event_see_all_post);
            mTextView = itemView.findViewById(R.id.statistic_event_post_count);

            mButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClicks.onNext(view);
        }
    }

    public static final int[] CHARTS_COLORS = {
            Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
            Color.rgb(191, 134, 134), Color.rgb(179, 48, 80), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };

}
