package com.com.tweetSweet.ui;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;

import com.com.tweetSweet.R;
import com.com.tweetSweet.TwitterSweetApp;
import com.com.tweetSweet.adapters.EndlessRecyclerOnScrollListener;
import com.com.tweetSweet.adapters.TweetListAdapter;
import com.com.tweetSweet.constants.ApplicationConstants;
import com.com.tweetSweet.models.TweetDTO;
import com.com.tweetSweet.models.TweetDateFormatter;
import com.com.tweetSweet.repository.TwitterRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchViewInterface, SearchView.OnQueryTextListener {

    private static final String TWEET_LIST_KEY = "TWEET_LIST_KEY";
    private static final String QUERY_KEY = "QUERY_KEY";

    private SearchPresenter searchPresenter;
    private MenuItem mSearchItem;
    private String mLastSubmittedQuery;
    private RecyclerView mTweetsRecyclerView;
    private TweetListAdapter mAdapter;
    private List<TweetDTO> mTweetDTOList;
    private List<TweetDTO> originalTweetList;
    private SwitchCompat switchCompat;

    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mHelpView;

    private Animation mLoadingAnimation;

    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupMVP();
        setupViews();

        if (savedInstanceState != null) {
            mLastSubmittedQuery = savedInstanceState.getString(QUERY_KEY);
            mTweetDTOList = savedInstanceState.getParcelableArrayList(TWEET_LIST_KEY);
            if (mTweetDTOList != null) {
                showResults(mTweetDTOList);
                return;
            }
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    /*
                    mAdapter = new TweetListAdapter(SearchActivity.this,new TweetDateFormatter());
                    mAdapter.addAll(mTweetDTOList);
                    mTweetsRecyclerView.setAdapter(mAdapter);
                    */
                    originalTweetList.clear();
                    originalTweetList.addAll(mTweetDTOList);
                    mTweetDTOList = searchPresenter.sortResults(mTweetDTOList);
                    mAdapter.notifyDataSetChanged();

                }
                else{
                    /*
                    mAdapter = new TweetListAdapter(SearchActivity.this,new TweetDateFormatter(),mTweetDTOList);
                    mTweetsRecyclerView.setAdapter(mAdapter);
                    */
                    if(!originalTweetList.isEmpty()) {
                        mTweetDTOList.clear();
                        mTweetDTOList.addAll(originalTweetList);
                        mAdapter.notifyDataSetChanged();
                    }

                }
            }
        });

        showHelp();
    }

    private void setupViews(){

        mTweetsRecyclerView = (RecyclerView) findViewById(R.id.tweetsRecyclerView);
        setupTweetListView();

        mLoadingView = findViewById(R.id.loadingLogo);

        mEmptyView = findViewById(R.id.emptyView);

        mErrorView = findViewById(R.id.errorView);

        mHelpView = findViewById(R.id.helpView);

        mLoadingAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce);

        switchCompat = findViewById(R.id.sort_switch);

        switchCompat.setEnabled(false);
    }

    private void setupMVP() {
        searchPresenter = new SearchPresenter(this);
    }

    private void setupTweetListView() {
        mTweetDTOList = new ArrayList<>();
        originalTweetList = new ArrayList<>();
        mAdapter = new TweetListAdapter(this, new TweetDateFormatter(),mTweetDTOList);
        mTweetsRecyclerView.setAdapter(mAdapter);
        layoutManager = new LinearLayoutManager(this);
        mTweetsRecyclerView.setLayoutManager(layoutManager);

        mTweetsRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                Log.d(ApplicationConstants.TAG,"calling on load more");

                String lastId = mAdapter.getLastTweetId();

                searchPresenter.getResultsWithQueryAfterId(mLastSubmittedQuery,lastId);
            }
        });

    }

    @Override
    public TwitterRepository getRepository() {
        return ((TwitterSweetApp) getApplication()).getRepository();
    }

    @Override
    public void showResults(List<TweetDTO> tweetDTOList) {
        if(tweetDTOList!=null && !tweetDTOList.isEmpty()) {
            Log.d(ApplicationConstants.TAG, String.valueOf(tweetDTOList.size() + tweetDTOList.get(0).getUsername()));

            originalTweetList.clear();
            mTweetDTOList.clear();
            mTweetDTOList.addAll(tweetDTOList);
            switchCompat.setChecked(false);
            mAdapter.notifyDataSetChanged();

        }

        boolean empty = tweetDTOList == null || tweetDTOList.size() == 0;

        showLoadingView(false);
        showEmptyView(empty);
        showRecyclerView(!empty);
        showErrorView(false);
        showHelpView(false);
        switchCompat.setEnabled(!empty);

    }

    @Override
    public void updateResults(List<TweetDTO> tweetDTOList) {
        if(tweetDTOList!=null && !tweetDTOList.isEmpty()) {
            Log.d(ApplicationConstants.TAG, String.valueOf(tweetDTOList.size() + tweetDTOList.get(0).getUsername()));

            mTweetDTOList.addAll(tweetDTOList);
            mAdapter.notifyDataSetChanged();

            /*
            if(switchCompat.isChecked())
            {
                mAdapter.addAll(tweetDTOList);
                mAdapter.notifyDataSetChanged();
            }
            else{
                mTweetDTOList.addAll(tweetDTOList);
                mAdapter.notifyDataSetChanged();
            }
            */

        }
    }

    @Override
    public void showErrorMessage(String s) {
        mTweetDTOList = new ArrayList<>();

        showLoadingView(false);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(true);
        showHelpView(false);
    }

    @Override
    public void showToast(String s) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mSearchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setQuery(mLastSubmittedQuery, false);
            }
        });

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTweetDTOList != null) {
            outState.putParcelableArrayList(TWEET_LIST_KEY, new ArrayList<Parcelable>(mTweetDTOList));
        }
        outState.putString(QUERY_KEY, mLastSubmittedQuery);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mLastSubmittedQuery = query;
        if (TextUtils.isEmpty(query)) {
            showHelp();
        } else {
            showLoading();
            Log.d(ApplicationConstants.TAG,"calling submit");
            searchPresenter.getResultsWithQuery(query);
            MenuItemCompat.collapseActionView(mSearchItem);
            return true;
        }

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    private void showLoading() {
        showLoadingView(true);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(false);
        showHelpView(false);
    }

    private void showHelp() {
        showLoadingView(false);
        showEmptyView(false);
        showRecyclerView(false);
        showErrorView(false);
        showHelpView(true);
    }

    private void showLoadingView(boolean visible) {
        showView(mLoadingView, visible);
        if (visible) {
            mLoadingView.startAnimation(mLoadingAnimation);
        } else {
            mLoadingView.clearAnimation();
        }
    }

    private void showEmptyView(boolean visible) {
        showView(mEmptyView, visible);
    }

    private void showRecyclerView(boolean visible) {
        showView(mTweetsRecyclerView, visible);
    }

    private void showErrorView(boolean visible) {
        showView(mErrorView, visible);
    }

    private void showHelpView(boolean visible) {
        showView(mHelpView, visible);
    }

    private void showView(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
