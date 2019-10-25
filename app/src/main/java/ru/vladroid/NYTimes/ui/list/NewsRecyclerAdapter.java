package ru.vladroid.NYTimes.ui.list;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import ru.vladroid.NYTimes.DTO.Result;
import ru.vladroid.NYTimes.R;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    private final List<Result> news;
    private final LayoutInflater layoutInflater;
    private RequestManager imageLoader;
    private final OnItemClickListener listener;
    private final Context context;

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public NewsRecyclerAdapter(Context context, List<Result> news, OnItemClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.news = news;
        imageLoader = Glide.with(context);
        this.context = context;
        this.listener = listener;
    }

    private enum NewsType {
        WITHOUT_IMAGE(0),
        WITH_IMAGE(1);

        private final int id;

        NewsType(int id) {
            this.id = id;
        }

        public static NewsType getById(int id) {
            for (NewsType e : values()) {
                if (e.id == id) return e;
            }
            return WITHOUT_IMAGE;
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (NewsType.getById(viewType)) {
            case WITH_IMAGE:
                v = layoutInflater.inflate(R.layout.news_view, parent, false);
                break;
            case WITHOUT_IMAGE:
                v = layoutInflater.inflate(R.layout.news_view2, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + NewsType.getById(viewType));
        }
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (news.get(position).getMultimedia().isEmpty()) {
            return NewsType.WITHOUT_IMAGE.id;
        }
        return NewsType.WITH_IMAGE.id;
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Result newsItem);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsIcon;
        private TextView newsCat, newsTitle, newsText, newsDate;

        private NewsViewHolder(View itemView) {
            super(itemView);
            bindViews();
        }

        private void bindViews() {
            newsIcon = itemView.findViewById(R.id.news_image);
            newsCat = itemView.findViewById(R.id.news_category);
            newsText = itemView.findViewById(R.id.news_text);
            newsDate = itemView.findViewById(R.id.news_date);
            newsTitle = itemView.findViewById(R.id.news_title);
        }

        private void bind(Result newsItem) {
            setTitle(newsItem);
            setCategory(newsItem);
            setText(newsItem);
            setPublishedDate(newsItem);

            if (containsImage(newsItem)) {
                loadImage(newsItem);
            }
        }

        private void setTitle(Result newsItem) {
            newsTitle.setText(newsItem.getTitle());
        }

        private void setCategory(Result newsItem) {
            newsCat.setText(newsItem.getSubsection().isEmpty() ? newsItem.getSection() : newsItem.getSubsection());
        }

        private void setText(Result newsItem) {
            newsText.setText(newsItem.getAbstract());
        }

        private void setPublishedDate(Result newsItem) {
            Date date;
            try {
                date = df.parse(newsItem.getPublishedDate());
                newsDate.setText(dateToString(date));
            } catch (ParseException e) {
                Log.e("mydaterr", newsItem.getPublishedDate());
            }
        }

        private boolean containsImage(Result newsItem) {
            return !newsItem.getMultimedia().isEmpty()
                    && newsItem.getMultimedia().get(0).isImage();
        }

        private void loadImage(Result newsItem) {
            imageLoader.load(newsItem.getMultimedia().get(1).getUrl())
                    .into(newsIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(news.get(pos));
                    }
                }
            });
        }
    }

    private static String dateToString(Date date) {
        if (date == null)
            return "error date parse";
        StringBuilder res = new StringBuilder();
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minutes = cal.get(Calendar.MINUTE);
        String fMonth = month >= 10 ? String.valueOf(month) : "0" + month;
        String fDay = day >= 10 ? String.valueOf(day) : "0" + day;
        String fHour = hour >= 10 ? String.valueOf(hour) : "0" + hour;
        String fMinutes = minutes >= 10 ? String.valueOf(minutes) : "0" + minutes;


        Calendar calNow = Calendar.getInstance();
        if (calNow.get(Calendar.YEAR) == year && calNow.get(Calendar.MONTH) == month) {
            if (calNow.get(Calendar.DAY_OF_MONTH) - day > 1) {
                res.append(fDay).append(".").append(fMonth).append(".").append(year).append(", ").append(fHour).append(":").append(fMinutes);
            } else if (calNow.get(Calendar.DAY_OF_MONTH) - day == 1) {
                res.append("Yesterday, ").append(fHour).append(":").append(fMinutes);
            } else {
                res.append("Today, ");
                if (calNow.get(Calendar.HOUR) - hour < 13) {
                    if (calNow.get(Calendar.HOUR) - hour == 0) {
                        res.append(calNow.get(Calendar.MINUTE) - minutes).append(" min. ago");
                    } else {
                        res.append(calNow.get(Calendar.HOUR) - hour).append(" hr. ago");
                    }
                } else {
                    res.append(fHour).append(":").append(fMinutes);
                }
            }
        } else {
            res.append(fDay).append(".").append(fMonth).append(".").append(year).append(", ").append(fHour).append(":").append(fMinutes);
        }

        return res.toString();
    }


}
