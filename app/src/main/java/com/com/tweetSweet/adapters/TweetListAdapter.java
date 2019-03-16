package com.com.tweetSweet.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.com.tweetSweet.R;
import com.com.tweetSweet.models.TweetDTO;
import com.com.tweetSweet.models.TweetDateFormatter;

import java.util.Collections;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TweetListAdapter extends RecyclerView.Adapter<TweetListAdapter.ViewHolder> {

    private final TweetDateFormatter mFormatter;
    private List<TweetDTO> mTweetDTOList;
    private SortedList<TweetDTO> tweetDTOSortedList;
    private Context mContext;

    public TweetListAdapter(Context context, TweetDateFormatter formatter,List<TweetDTO> tweetDTOList) {
        mContext=context;
        this.mFormatter = formatter;
        this.mTweetDTOList = tweetDTOList;
    }

    public TweetListAdapter(Context context, TweetDateFormatter formatter) {
        mContext=context;
        this.mFormatter = formatter;
        tweetDTOSortedList = new SortedList<TweetDTO>(TweetDTO.class, new SortedList.Callback<TweetDTO>() {
            @Override
            public int compare(TweetDTO o1, TweetDTO o2) {
                if(!o2.getRetweetCount().equals(o1.getRetweetCount())){
                    return o2.getRetweetCount()-o1.getRetweetCount();
                }else{
                    return o2.getFavoriteCount()-o1.getFavoriteCount();
                }
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(TweetDTO oldItem, TweetDTO newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areItemsTheSame(TweetDTO oldItem, TweetDTO newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }
        });
    }

    public void addAll(List<TweetDTO> tweetDTOList) {
        tweetDTOSortedList.beginBatchedUpdates();
        for (int i = 0; i < tweetDTOList.size(); i++) {
            tweetDTOSortedList.add(tweetDTOList.get(i));
        }
        tweetDTOSortedList.endBatchedUpdates();
    }

    public void clear() {
        tweetDTOSortedList.beginBatchedUpdates();
        //remove items at end, to avoid unnecessary array shifting
        while (tweetDTOSortedList.size() > 0) {
            tweetDTOSortedList.removeItemAt(tweetDTOSortedList.size() - 1);
        }
        tweetDTOSortedList.endBatchedUpdates();
    }

    public void update(List<TweetDTO> tweetDTOList){
        mTweetDTOList=tweetDTOList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View tweetItemView = inflater.inflate(R.layout.tweet_item, parent, false);
        return new ViewHolder(tweetItemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);

        TweetDTO tweetDTO = mTweetDTOList.get(position);
        holder.content.setText(tweetDTO.getContent());
        holder.createdAt.setText(mFormatter.format(mContext, tweetDTO.getCreatedAt()));
        holder.username.setText(tweetDTO.getUsername());
        String imageUrl = tweetDTO.getImageUrl();
        if (TextUtils.isEmpty(imageUrl)) {
            holder.imageView.setVisibility(View.GONE);
        } else {
            Glide.with(mContext).load(imageUrl).transition(withCrossFade()).into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
        }

        holder.content.setText(mTweetDTOList.get(position).getContent());

        Glide.with(mContext).load(tweetDTO.getProfilePic()).apply(
                new RequestOptions()
                        .error(R.drawable.avatar)
                        .centerCrop()
        )
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        //on load failed
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        //on load success
                        return false;
                    }
                })
                .transition(withCrossFade()).into((holder).avatar);

        holder.retweetCount.setText(String.valueOf(tweetDTO.getRetweetCount()));
        holder.favCount.setText(String.valueOf(tweetDTO.getFavoriteCount()));


    }

    @Override
    public int getItemCount() {
        return mTweetDTOList == null ? 0 : mTweetDTOList.size();
                    //return mTweetDTOList.size();
    }

    public String getLastTweetId() {
        final TweetDTO tweet = mTweetDTOList.get(getItemCount() - 1);
        return tweet.getId();
    }

    public List<TweetDTO> getTweets() {
        return Collections.unmodifiableList(mTweetDTOList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView username;
        private final TextView createdAt;
        private final TextView content;
        private final ImageView imageView;
        private final ImageView avatar;
        private final TextView retweetCount;
        private final TextView favCount;

        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.usernameTextView);
            createdAt = (TextView) itemView.findViewById(R.id.createdAtTextView);
            content = (TextView) itemView.findViewById(R.id.contentTextView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            retweetCount = (TextView) itemView.findViewById(R.id.retweet_count);
            favCount = (TextView) itemView.findViewById(R.id.fav_count);
        }
    }
}
