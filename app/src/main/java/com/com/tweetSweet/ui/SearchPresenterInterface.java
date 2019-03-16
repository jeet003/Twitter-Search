package com.com.tweetSweet.ui;

import com.com.tweetSweet.models.TweetDTO;

import java.util.List;

public interface SearchPresenterInterface {

    void getResultsWithQuery(String q);

    void getResultsWithQueryAfterId(String q,String id);

    List<TweetDTO> sortResults(List<TweetDTO> list);
}
