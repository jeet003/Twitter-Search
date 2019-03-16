package com.com.tweetSweet.ui;

import com.com.tweetSweet.models.TweetDTO;
import com.com.tweetSweet.repository.TwitterRepository;

import java.util.List;

public interface SearchViewInterface {

    void showResults(List<TweetDTO> tweetDTOList);

    void updateResults(List<TweetDTO> tweetDTOList);

    void showErrorMessage(String s);

    void showToast(String s);

    TwitterRepository getRepository();
}
