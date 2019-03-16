package com.com.tweetSweet.ui;

import android.util.Log;

import com.com.tweetSweet.constants.ApplicationConstants;
import com.com.tweetSweet.models.TweetDTO;
import com.com.tweetSweet.repository.RepositoryCallback;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchPresenter implements SearchPresenterInterface {

    private SearchViewInterface mvi;
    private String TAG = "SearchPresenter";

    public SearchPresenter(SearchViewInterface mvi) {
        this.mvi = mvi;
    }

    @Override
    public void getResultsWithQuery(String q) {

        mvi.getRepository().getTweetList(q, new RepositoryCallback<List<TweetDTO>>() {
            @Override
            public void onSuccess(List<TweetDTO> object) {
                Log.d(ApplicationConstants.TAG,"called success");
                mvi.showResults(object);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(ApplicationConstants.TAG,"called error");
                mvi.showErrorMessage("error");

            }
        },null);

    }

    @Override
    public void getResultsWithQueryAfterId(String q, String id) {
        mvi.getRepository().getTweetList(q, new RepositoryCallback<List<TweetDTO>>() {
            @Override
            public void onSuccess(List<TweetDTO> object) {
                Log.d(ApplicationConstants.TAG,"called success after ID");
                mvi.updateResults(object);
            }

            @Override
            public void onFailure(Throwable error) {
                Log.d(ApplicationConstants.TAG,"called error after ID");
                mvi.showErrorMessage("error");

            }
        },id);
    }

    @Override
    public List<TweetDTO> sortResults(List<TweetDTO> list) {
        Collections.sort(list, new Comparator<TweetDTO>() {
            @Override
            public int compare(TweetDTO o1, TweetDTO o2) {
                if(!o2.getRetweetCount().equals(o1.getRetweetCount())){
                    return o2.getRetweetCount()-o1.getRetweetCount();
                }else{
                    return o2.getFavoriteCount()-o1.getFavoriteCount();
                }
            }
        });

        return list;
    }
}
