package com.com.tweetSweet.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class TweetItems {
    private final List<TweetObject> statuses;

    public TweetItems(List<TweetObject> statuses) {
        this.statuses = statuses;
    }

    public static class TweetObject {
        @SerializedName("id_str")
        private final String id;
        @SerializedName("created_at")
        private final Date createdAt;
        @SerializedName("text")
        private final String text;

        private final User user;

        private final Entities entities;

        @SerializedName("retweet_count")
        private final Integer retweetCount;

        @SerializedName("favorite_count")
        private final Integer favoriteCount;

        private static class User {
            @SerializedName("name")
            private final String name;
            @SerializedName("profile_image_url_https")
            private final String profilePic;

            private User(String name, String profilePic) {
                this.name = name;
                this.profilePic = profilePic;
            }

            public String getName() {
                return name;
            }

            public String getProfilePic() {
                return profilePic;
            }
        }

        private static class Entities {
            private final List<Media> media;

            private static class Media {
                @SerializedName("media_url_https")
                private final String url;

                private final String type;

                public Media(String url, String type) {
                    this.url = url;
                    this.type = type;
                }

                public String getUrl() {
                    return url;
                }

                public boolean isPhoto() {
                    return "photo".equalsIgnoreCase(type) && !TextUtils.isEmpty(getUrl());
                }
            }

            public Entities(List<Media> media) {
                this.media = media;
            }
        }

        public TweetObject(String id, Date createdAt, String text, User user, Entities entities,Integer retweetCount,Integer favoriteCount) {
            this.id=id;
            this.createdAt = createdAt;
            this.text = text;
            this.user = user;
            this.entities = entities;
            this.retweetCount=retweetCount;
            this.favoriteCount=favoriteCount;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getText() {
            return text;
        }

        public String getUserName() {
            return user.getName();
        }

        public String getProfilePic() {
            return user.getProfilePic();
        }

        public String getImageUrl() {
            if (entities != null && entities.media != null) {
                for (Entities.Media media : entities.media) {
                    if (media.isPhoto()) {
                        return media.getUrl();
                    }
                }
            }
            return null;
        }

        public Integer getRetweetCount(){
            return retweetCount;
        }

        public Integer getFavoriteCount(){
            return favoriteCount;
        }

        public String getId() {
            return id;
        }
    }

    public List<TweetObject> getItems() {
        return statuses;
    }
}

