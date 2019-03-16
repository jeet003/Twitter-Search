package com.com.tweetSweet.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TweetDTO implements Parcelable {
    private final String id;
    private final String username;
    private final String profilePic;
    private final String content;
    private final Date createdAt;
    private final String imageUrl;
    private final Integer retweetCount;
    private final Integer favoriteCount;

    public TweetDTO(String id,String username, String profilePic, String content, Date createdAt, String imageUrl, Integer retweetCount, Integer favoriteCount) {
        this.id=id;
        this.username = username;
        this.profilePic=profilePic;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.retweetCount=retweetCount;
        this.favoriteCount=favoriteCount;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public static List<TweetDTO> buildTweets(TweetItems items) {
        List<TweetDTO> tweetDTOList = new ArrayList<>();
        for (TweetItems.TweetObject tweetObject : items.getItems()) {
            String id = tweetObject.getId();
            String content = tweetObject.getText();
            String username = tweetObject.getUserName();
            String profilePic = tweetObject.getProfilePic();
            Date createdAt = tweetObject.getCreatedAt();
            String imageUrl = tweetObject.getImageUrl();
            Integer retweetCount = tweetObject.getRetweetCount();
            Integer favoriteCount = tweetObject.getFavoriteCount();
            tweetDTOList.add(new TweetDTO(id,username,profilePic, content, createdAt, imageUrl,retweetCount,favoriteCount));
        }
        return tweetDTOList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.username);
        dest.writeString(this.profilePic);
        dest.writeString(this.content);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeString(this.imageUrl);
        dest.writeValue(this.retweetCount);
        dest.writeValue(this.favoriteCount);
    }

    protected TweetDTO(Parcel in) {
        this.id = in.readString();
        this.username = in.readString();
        this.profilePic = in.readString();
        this.content = in.readString();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        this.imageUrl = in.readString();
        this.retweetCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favoriteCount = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<TweetDTO> CREATOR = new Creator<TweetDTO>() {
        @Override
        public TweetDTO createFromParcel(Parcel source) {
            return new TweetDTO(source);
        }

        @Override
        public TweetDTO[] newArray(int size) {
            return new TweetDTO[size];
        }
    };
}
