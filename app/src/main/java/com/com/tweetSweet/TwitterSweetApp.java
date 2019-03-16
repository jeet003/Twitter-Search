package com.com.tweetSweet;

import android.app.Application;

import com.com.tweetSweet.repository.TwitterRepository;

public class TwitterSweetApp extends Application {

    private TwitterRepository mTwitterRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        mTwitterRepository = new TwitterRepository(this,
                getString(R.string.base_url),
                BuildConfig.TWITTER_CONSUMER_KEY,
         BuildConfig.TWITTER_CONSUMER_SECRET);
    }

    public TwitterRepository getRepository() {
        return mTwitterRepository;
    }
}
