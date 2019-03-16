package com.com.tweetSweet.repository;

public interface RepositoryCallback<T> {
    void onSuccess(T object);
    void onFailure(Throwable error);
}
